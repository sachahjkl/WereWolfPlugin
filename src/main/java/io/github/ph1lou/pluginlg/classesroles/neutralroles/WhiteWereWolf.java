package io.github.ph1lou.pluginlg.classesroles.neutralroles;

import io.github.ph1lou.pluginlg.events.DayEvent;
import io.github.ph1lou.pluginlg.events.NewWereWolfEvent;
import io.github.ph1lou.pluginlg.events.NightEvent;
import io.github.ph1lou.pluginlg.game.GameManager;
import io.github.ph1lou.pluginlgapi.enumlg.Camp;
import io.github.ph1lou.pluginlgapi.enumlg.Day;
import io.github.ph1lou.pluginlgapi.enumlg.RoleLG;
import io.github.ph1lou.pluginlgapi.enumlg.State;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class WhiteWereWolf extends RolesNeutral {

    public WhiteWereWolf(GameManager game, UUID uuid) {
        super(game,uuid);
    }

    @EventHandler
    public void onNight(NightEvent event) {

        if(!event.getUuid().equals(game.getGameUUID())){
            return;
        }

        if(!game.playerLG.get(getPlayerUUID()).isState(State.ALIVE)){
            return;
        }

        if(Bukkit.getPlayer(getPlayerUUID())==null){
            return;
        }

        Player player = Bukkit.getPlayer(getPlayerUUID());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, -1, false, false));
    }

    @EventHandler
    public void onDay(DayEvent event) {

        if(!event.getUuid().equals(game.getGameUUID())){
            return;
        }

        if(!game.playerLG.get(getPlayerUUID()).isState(State.ALIVE)){
            return;
        }

        if(Bukkit.getPlayer(getPlayerUUID())==null){
            return;
        }

        Player player = Bukkit.getPlayer(getPlayerUUID());
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void stolen(UUID uuid) {

        if(Bukkit.getPlayer(getPlayerUUID())==null){
            return;
        }

        Bukkit.getPlayer(getPlayerUUID()).setMaxHealth(30);
    }

    @Override
    public void recoverPower(Player player) {
        player.setMaxHealth(30);
        player.setHealth(30);
    }

    @Override
    public void recoverPotionEffect(Player player) {
        super.recoverPotionEffect(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,0,false,false));
        if(game.isDay(Day.DAY)) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,-1,false,false));
    }

    @Override
    public RoleLG getRoleEnum() {
        return RoleLG.WHITE_WEREWOLF;
    }

    @Override
    public String getDescription() {
        return game.translate("werewolf.role.white_werewolf.description");
    }

    @Override
    public String getDisplay() {
        return game.translate("werewolf.role.white_werewolf.display");
    }

    @Override
    public boolean isWereWolf() {
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNewWereWolfForWhiteWerewolf(NewWereWolfEvent event){
        if(event.getUuid().equals(getPlayerUUID())){
            setCamp(Camp.WEREWOLF);
        }
    }
}
