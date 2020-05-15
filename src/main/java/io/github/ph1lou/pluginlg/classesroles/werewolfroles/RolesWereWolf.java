package io.github.ph1lou.pluginlg.classesroles.werewolfroles;

import io.github.ph1lou.pluginlg.classesroles.RolesImpl;
import io.github.ph1lou.pluginlg.events.DayEvent;
import io.github.ph1lou.pluginlg.events.NightEvent;
import io.github.ph1lou.pluginlg.game.GameManager;
import io.github.ph1lou.pluginlgapi.enumlg.Camp;
import io.github.ph1lou.pluginlgapi.enumlg.Day;
import io.github.ph1lou.pluginlgapi.enumlg.State;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public abstract class RolesWereWolf extends RolesImpl {

    public RolesWereWolf(GameManager game, UUID uuid) {
        super(game,uuid);
        setCamp(Camp.WEREWOLF);
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
    public void recoverPotionEffect(Player player) {
        super.recoverPotionEffect(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,0,false,false));
        if(game.isDay(Day.DAY)) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,-1,false,false));
    }


    @Override
    public void recoverPower(Player player) {

    }

    @Override
    public boolean isWereWolf() {
        return true;
    }
}
