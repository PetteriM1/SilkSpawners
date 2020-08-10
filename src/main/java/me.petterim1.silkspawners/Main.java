package me.petterim1.silkspawners;

import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.plugin.PluginBase;
import nukkitcoders.mobplugin.entities.block.BlockEntitySpawner;

public class Main extends PluginBase implements Listener {

    private boolean useMobPlugin;
    private boolean dropMob;

    public void onEnable() {
        saveDefaultConfig();
        dropMob = getConfig().getBoolean("dropMob");
        if (getServer().getPluginManager().getPlugin("MobPlugin") != null) {
            useMobPlugin = true;
            getLogger().debug("MobPlugin found!");
        }
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        if (e.getBlock().getId() == BlockID.MONSTER_SPAWNER) {
            Item item = e.getItem();
            if (item != null && item.getEnchantment(Enchantment.ID_SILK_TOUCH) != null) {
                if (useMobPlugin && dropMob) {
                    BlockEntity be = e.getBlock().getLevel().getBlockEntity(e.getBlock());
                    if (be instanceof BlockEntitySpawner) {
                        int entity = ((BlockEntitySpawner) be).getSpawnEntityType();
                        if (entity > 0) {
                            e.setDrops(new Item[]{Item.get(BlockID.MONSTER_SPAWNER, 0, 1), Item.get(ItemID.SPAWN_EGG, entity, 1)});
                            return;
                        }
                    }

                }
                e.setDrops(new Item[]{Item.get(BlockID.MONSTER_SPAWNER, 0, 1)});
            }
        }
    }
}
