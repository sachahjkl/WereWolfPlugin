package io.github.ph1lou.pluginlg.commandlg.utilities;

import io.github.ph1lou.pluginlg.MainLG;
import io.github.ph1lou.pluginlg.commandlg.Commands;
import io.github.ph1lou.pluginlg.enumlg.RoleLG;
import io.github.ph1lou.pluginlg.enumlg.State;
import io.github.ph1lou.pluginlg.enumlg.StateLG;
import io.github.ph1lou.pluginlg.game.GameManager;
import io.github.ph1lou.pluginlg.game.PlayerLG;
import io.github.ph1lou.pluginlg.savelg.TextLG;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRole extends Commands {


    public CommandRole(MainLG main) {
        super(main);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)){
            return;
        }

     GameManager game = main.currentGame;

        TextLG text = game.text;
        Player player = (Player) sender;
        String playername = player.getName();
        
        if(!game.playerLG.containsKey(playername)) {
            player.sendMessage(text.getText(67));
            return;
        }

        PlayerLG plg = game.playerLG.get(playername);


        if(!game.isState(StateLG.LG)) {
            player.sendMessage(text.getText(68));
            return;
        }

        if (args.length!=0) {
            player.sendMessage(String.format(text.getText(190),0));
            return;
        }

        if(!plg.isState(State.LIVING)){
            player.sendMessage(text.getText(97));
            return;
        }

        player.sendMessage(text.description.get(plg.getRole()));

        if(plg.isRole(RoleLG.SOEUR)) {
            StringBuilder list =new StringBuilder();
            for(String sister:game.playerLG.keySet()) {
                if(game.playerLG.get(sister).isState(State.LIVING) && game.playerLG.get(sister).isRole(RoleLG.SOEUR)) {
                    list.append(sister).append(" ");
                }
            }
            player.sendMessage(String.format(text.getText(22),list.toString()));
        }
        else if(plg.isRole(RoleLG.FRERE_SIAMOIS)) {
            StringBuilder list =new StringBuilder();
            for(String brother:game.playerLG.keySet()) {
                if(game.playerLG.get(brother).isState(State.LIVING) && game.playerLG.get(brother).isRole(RoleLG.FRERE_SIAMOIS)) {
                    list.append(brother).append(" ");
                }
            }
            player.sendMessage(String.format(text.getText(23),list.toString()));
        }
    }
}
