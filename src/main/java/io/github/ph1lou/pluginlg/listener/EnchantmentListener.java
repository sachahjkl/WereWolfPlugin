package io.github.ph1lou.pluginlg.listener;

import io.github.ph1lou.pluginlg.classesroles.neutralroles.Assassin;
import io.github.ph1lou.pluginlg.classesroles.neutralroles.SerialKiller;
import io.github.ph1lou.pluginlg.classesroles.villageroles.Cupid;
import io.github.ph1lou.pluginlg.classesroles.villageroles.LittleGirl;
import io.github.ph1lou.pluginlg.classesroles.werewolfroles.MischievousWereWolf;
import io.github.ph1lou.pluginlg.game.GameManager;
import io.github.ph1lou.pluginlg.game.PlayerLG;
import io.github.ph1lou.pluginlgapi.enumlg.ScenarioLG;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnchantmentListener implements Listener {

    final GameManager game;


    public EnchantmentListener(GameManager game) {
        this.game=game;
    }

    @EventHandler
    public void onPrepareAnvilEvent(InventoryClickEvent event) {

        if(event.getInventory() ==null) return;
        if(!event.getInventory().getType().equals(InventoryType.ANVIL)) return;
        if(event.getSlot()!=2) return;
        ItemStack current = event.getCurrentItem();
        if (current==null) return;
        if(current.getEnchantments().isEmpty()){
            if(current.getType().equals(Material.ENCHANTED_BOOK)){
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) current.getItemMeta();
                event.setCurrentItem(checkEnchant(meta.getStoredEnchants(),(Player) event.getWhoClicked(),current));
            }
        }
        else event.setCurrentItem(checkEnchant(current.getEnchantments(),(Player) event.getWhoClicked(),current));
    }

    @EventHandler
    public void onItemEnchant(EnchantItemEvent event) {
        event.getInventory().setItem(0,checkEnchant(event.getEnchantsToAdd(),event.getEnchanter(),event.getItem()));
    }

    private ItemStack checkEnchant(Map<Enchantment,Integer> enchant, Player player, ItemStack item){

        Map<Enchantment,Integer> tempEnchant = new HashMap<>();
        ItemStack result = new ItemStack(item);
        UUID uuid = player.getUniqueId();

        if(!game.playerLG.containsKey(uuid)) return item;
        PlayerLG plg = game.playerLG.get(uuid);

        for(Enchantment e:enchant.keySet()){

            result.removeEnchantment(e);

            if(Arrays.asList(Enchantment.ARROW_FIRE,Enchantment.FIRE_ASPECT).contains(e)){
                if (!game.config.getScenarioValues().get(ScenarioLG.NO_FIRE_WEAPONS)) {
                    tempEnchant.put(e, enchant.get(e));
                }
            }
            else if(Enchantment.KNOCKBACK.equals(e)){
                if(game.config.getLimitKnockBack()==2){
                    tempEnchant.put(e,enchant.get(e));
                }
                else if(game.config.getLimitKnockBack()==1 && (plg.getRole() instanceof LittleGirl  || plg.getRole() instanceof MischievousWereWolf)){
                    tempEnchant.put(e,enchant.get(e));
                }
            }
            else if(Enchantment.PROTECTION_ENVIRONMENTAL.equals(e)){

                if (item.getType().equals(Material.DIAMOND_BOOTS) || item.getType().equals(Material.DIAMOND_LEGGINGS) ||  item.getType().equals(Material.DIAMOND_HELMET) ||  item.getType().equals(Material.DIAMOND_CHESTPLATE)){

                    if (!(plg.getRole() instanceof SerialKiller) && !(plg.getRole() instanceof Assassin)){
                        tempEnchant.put(e,Math.min(enchant.get(e),game.config.getLimitProtectionDiamond()));
                    }
                    else tempEnchant.put(e,Math.min(enchant.get(e),game.config.getLimitProtectionDiamond()+1));
                }
                else {
                    if (!(plg.getRole() instanceof SerialKiller) && !(plg.getRole() instanceof Assassin)){
                        tempEnchant.put(e,Math.min(enchant.get(e),game.config.getLimitProtectionIron()));
                    }
                    else tempEnchant.put(e,Math.min(enchant.get(e),game.config.getLimitProtectionIron()+1));
                }
            }
            else if(Enchantment.DAMAGE_ALL.equals(e)){
                if (item.getType().equals(Material.DIAMOND_SWORD)) {
                    if (!(plg.getRole() instanceof SerialKiller) && !(plg.getRole() instanceof Assassin)){
                        tempEnchant.put(e, Math.min(enchant.get(e), game.config.getLimitSharpnessDiamond()));
                    } else tempEnchant.put(e, Math.min(enchant.get(e), game.config.getLimitSharpnessDiamond() + 1));
                } else {
                    if (!(plg.getRole() instanceof SerialKiller) && !(plg.getRole() instanceof Assassin)){
                        tempEnchant.put(e, Math.min(enchant.get(e), game.config.getLimitSharpnessIron()));
                    } else
                        tempEnchant.put(e, Math.min(enchant.get(e), Math.min(4, game.config.getLimitSharpnessIron() + 1)));
                }
            }
            else if(Enchantment.ARROW_KNOCKBACK.equals(e)){
                if(game.config.getLimitPunch()==2){
                    tempEnchant.put(e,enchant.get(e));
                }
                else if(game.config.getLimitPunch()==1 && plg.getRole() instanceof Cupid){
                    tempEnchant.put(e,enchant.get(e));
                }
            }
            else if(Enchantment.ARROW_DAMAGE.equals(e)){
                if (!(plg.getRole() instanceof SerialKiller) && !(plg.getRole() instanceof Assassin)  && !(plg.getRole() instanceof Cupid)){
                    tempEnchant.put(e,Math.min(enchant.get(e),game.config.getLimitPowerBow()));
                }
                else tempEnchant.put(e,Math.min(enchant.get(e),game.config.getLimitPowerBow()+1));
            }
            else tempEnchant.put(e,enchant.get(e));
        }

        if(!result.getType().equals(Material.ENCHANTED_BOOK) && !result.getType().equals(Material.BOOK)){
            result.addUnsafeEnchantments(tempEnchant);
        }
        else{
            if(!tempEnchant.isEmpty()){
                result=new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) result.getItemMeta();
                for(Enchantment e:tempEnchant.keySet())
                    meta.addStoredEnchant(e,tempEnchant.get(e),false);
                result.setItemMeta(meta);
            }
           else  result=new ItemStack(Material.BOOK);
        }
        return result;
    }

}
