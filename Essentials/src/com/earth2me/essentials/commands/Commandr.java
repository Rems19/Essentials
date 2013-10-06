package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.IReplyTo;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import org.bukkit.Server;
import com.earth2me.essentials.CommandSource;
import org.bukkit.entity.Player;


public class Commandr extends EssentialsCommand
{
	public Commandr()
	{
		super("r");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 0);
		IReplyTo replyTo;
		String senderName;

		if (sender.isPlayer())
		{
			User user = ess.getUser(sender.getPlayer());
			message = FormatUtil.formatMessage(user, "essentials.msg", message);
			replyTo = user;
			senderName = user.getDisplayName();
		}
		else
		{
			message = FormatUtil.replaceFormat(message);
			replyTo = Console.getConsoleReplyTo();
			senderName = Console.NAME;
		}

		final CommandSource target = replyTo.getReplyTo();
		final String targetName = target instanceof Player ? ((Player)target).getDisplayName() : Console.NAME;

		if (target == null || ((target instanceof Player) && !((Player)target).isOnline()))
		{
			throw new Exception(_("foreverAlone"));
		}

		sender.sendMessage(_("msgFormat", _("me"), targetName, message));
		if (target.isPlayer())
		{
			User player = ess.getUser(target.getPlayer());
			if (sender.isPlayer() && player.isIgnoredPlayer(ess.getUser(sender.getPlayer())))
			{
				return;
			}
		}
		target.sendMessage(_("msgFormat", senderName, _("me"), message));
		replyTo.setReplyTo(target);
		if (target != sender)
		{
			if (target instanceof Player)
			{
				ess.getUser((Player)target).setReplyTo(sender);
			}
			else
			{
				Console.getConsoleReplyTo().setReplyTo(sender);
			}
		}
	}
}
