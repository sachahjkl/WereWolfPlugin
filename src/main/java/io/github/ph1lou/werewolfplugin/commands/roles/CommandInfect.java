package io.github.ph1lou.werewolfplugin.commands.roles;

import io.github.ph1lou.werewolfapi.Commands;
import io.github.ph1lou.werewolfapi.PlayerWW;
import io.github.ph1lou.werewolfapi.enumlg.Camp;
import io.github.ph1lou.werewolfapi.enumlg.State;
import io.github.ph1lou.werewolfapi.enumlg.StateLG;
import io.github.ph1lou.werewolfapi.enumlg.ToolLG;
import io.github.ph1lou.werewolfapi.events.InfectionEvent;
import io.github.ph1lou.werewolfapi.events.NewWereWolfEvent;
import io.github.ph1lou.werewolfapi.rolesattributs.AffectedPlayers;
import io.github.ph1lou.werewolfapi.rolesattributs.Power;
import io.github.ph1lou.werewolfapi.rolesattributs.Roles;
import io.github.ph1lou.werewolfplugin.Main;
import io.github.ph1lou.werewolfplugin.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandInfect implements Commands {


    private final Main main;

    public CommandInfect(Main main) {
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {


        GameManager game = main.getCurrentGame();

        if (!(sender instanceof Player)) {
            sender.sendMessage(game.translate("werewolf.check.console"));
            return;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if(!game.getPlayersWW().containsKey(uuid)) {
            player.sendMessage(game.translate("werewolf.check.not_in_game"));
            return;
        }

        PlayerWW plg = game.getPlayersWW().get(uuid);


        if (!game.isState(StateLG.GAME)) {
            player.sendMessage(game.translate("werewolf.check.game_not_in_progress"));
            return;
        }

        if (!(plg.getRole().isDisplay("werewolf.role.infect_father_of_the_wolves.display"))){
            player.sendMessage(game.translate("werewolf.check.role", game.translate("werewolf.role.infect_father_of_the_wolves.display")));
            return;
        }

        Roles infect = plg.getRole();

        if (args.length!=1) {
            player.sendMessage(game.translate("werewolf.check.player_input"));
            return;
        }

        if(!plg.isState(State.ALIVE)){
            player.sendMessage(game.translate("werewolf.check.death"));
            return;
        }

        if(!((Power)infect).hasPower()) {
            player.sendMessage(game.translate("werewolf.check.power"));
            return;
        }

        if(Bukkit.getPlayer(UUID.fromString(args[0]))==null){
            player.sendMessage(game.translate("werewolf.check.offline_player"));
            return;
        }
        UUID argUUID = UUID.fromString(args[0]);

        if (!game.getConfig().getConfigValues().get(ToolLG.AUTO_REZ_INFECT) && argUUID.equals(uuid)) {
            player.sendMessage(game.translate("werewolf.check.not_yourself"));
            return;
        }

        if(!game.getPlayersWW().containsKey(argUUID)) {
            player.sendMessage(game.translate("werewolf.check.player_not_found"));
            return;
        }
        PlayerWW plg1 = game.getPlayersWW().get(argUUID);

        if (!plg1.isState(State.JUDGEMENT)) {
            player.sendMessage(game.translate("werewolf.check.not_in_judgement"));
            return;
        }

        if (!plg1.canBeInfect()) {
            player.sendMessage(game.translate("werewolf.role.infect_father_of_the_wolves.player_cannot_be_infected"));
            return;
        }

        InfectionEvent infectionEvent = new InfectionEvent(uuid,argUUID);
        Bukkit.getPluginManager().callEvent(infectionEvent);

        if(infectionEvent.isCancelled()){
            player.sendMessage(game.translate("werewolf.check.cancel"));
            return;
        }

        ((AffectedPlayers)infect).addAffectedPlayer(argUUID);
        ((Power) infect).setPower(false);
        player.sendMessage(game.translate("werewolf.role.infect_father_of_the_wolves.infection_perform",plg1.getName()));
        game.resurrection(argUUID);

        if(!plg1.getRole().isCamp(Camp.WEREWOLF)) {
            NewWereWolfEvent newWereWolfEvent = new NewWereWolfEvent(argUUID);
            Bukkit.getPluginManager().callEvent(newWereWolfEvent);

            if(newWereWolfEvent.isCancelled()){
                return;
            }
        }
        plg1.setCanBeInfect(false);
        game.getPlayersWW().get(argUUID).getRole().setInfected(true);
        game.checkVictory();
    }
}
