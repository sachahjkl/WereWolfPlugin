package io.github.ph1lou.pluginlg.listener.roleslisteners;

import io.github.ph1lou.pluginlg.MainLG;
import io.github.ph1lou.pluginlg.PlayerLG;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ListenerRolesMischievousWolf implements ListenerRoles {

    MainLG main;

    public void init(MainLG main) {
        this.main = main;
    }

    @Override
    public void onNight(Player player) {
        player.sendMessage(main.text.getText(14));
    }

    @Override
    public void onDay(Player player, PlayerLG plg) {
        if (!plg.hasPower()) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            plg.setPower(true);
            player.sendMessage(main.text.getText(18));
            main.optionlg.updateNameTag();
        }
    }

    @Override
    public void onSelectionEnd(Player player, PlayerLG plg) {

    }

    @Override
    public void onDayWillCome(Player player) {
        player.sendMessage(main.text.getText(197));
    }

    @Override
    public void onVoteEnd(Player player, PlayerLG plg) {

    }

}
