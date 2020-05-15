package io.github.ph1lou.pluginlg.listener.scenarioslisteners;

import io.github.ph1lou.pluginlg.MainLG;
import io.github.ph1lou.pluginlg.game.GameManager;
import io.github.ph1lou.pluginlgapi.enumlg.ScenarioLG;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Scenarios implements Listener {
    final MainLG main;
    final GameManager game;
    final ScenarioLG scenario;
    boolean register = false;

    public Scenarios(MainLG main, GameManager game,ScenarioLG scenario) {
        this.main = main;
        this.game=game;
        this.scenario=scenario;
    }


    public void register() {
        if (game.config.getScenarioValues().get(scenario)) {
            if (!register) {
                Bukkit.getPluginManager().registerEvents(this, main);
                register = true;
            }
        } else {
            if (register) {
                HandlerList.unregisterAll(this);
                register = false;
            }
        }
    }
}
