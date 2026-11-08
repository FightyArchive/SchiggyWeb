package de.schiggy.web.auth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WebsiteCommand implements CommandExecutor {

    private final VerificationService verificationService;

    public WebsiteCommand(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Dieser Befehl ist nur im Spiel verfügbar.");
            return true;
        }

        if (args.length != 2 || !args[0].equalsIgnoreCase("verify")) {
            player.sendMessage(ChatColor.RED + "Verwendung: /website verify <Code>");
            return true;
        }

        String code = args[1];

        boolean success = verificationService.completeVerification(
                code,
                player.getUniqueId(),
                player.getName()
        );

        if (!success) {
            player.sendMessage(ChatColor.RED
                    + "Ungültiger, abgelaufener oder bereits verwendeter Code.");
            return true;
        }

        player.sendMessage(ChatColor.GREEN
                + "Dein Minecraft-Account wurde erfolgreich verknüpft!");

        return true;
    }
}
