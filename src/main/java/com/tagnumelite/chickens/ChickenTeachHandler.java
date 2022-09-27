package com.tagnumelite.chickens;

import com.tagnumelite.chickens.api.chicken.ChickenData;
import com.tagnumelite.chickens.api.utils.Utils;
import com.tagnumelite.chickens.common.entities.ChickensChicken;
import com.tagnumelite.chickens.common.entities.ModEntityTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Created by setyc on 21.03.2016.
 */
public class ChickenTeachHandler {
    @SubscribeEvent
    public void handleInteraction(PlayerInteractEvent.EntityInteract event) {
        ItemStack item = event.getEntity().getItemInHand(event.getHand());
        Entity target = event.getTarget();
        if (item.getItem() == Items.BOOK && target instanceof Chicken chicken) {
            Level level = event.getEntity().level;
            if (level.isClientSide) {
                return;
            }

            ChickenData smartChickenData = Chickens.getChickenManager().getChickenData(Utils.rl("smart"));
            //if (smartChicken == null || !smartChicken.isEnabled()) return;
            ChickensChicken smartChicken = convertToSmart(chicken, level, smartChickenData);

            chicken.remove(Entity.RemovalReason.DISCARDED);
            level.addFreshEntity(smartChicken);
            smartChicken.spawnAnim();

            event.setCanceled(true);
        }
    }

    private ChickensChicken convertToSmart(Chicken chicken, Level level, ChickenData smartChickenData) {
        ChickensChicken smartChicken = new ChickensChicken(ModEntityTypes.CHICKEN.get(), level);
        smartChicken.copyPosition(chicken);
        //smartChicken.onInitialSpawn(level.getCurrentDifficultyAt(chicken.blockPosition()), null);
        smartChicken.setChickenType(Utils.rl("smart"));
        if (chicken.hasCustomName()) {
            smartChicken.setCustomName(chicken.getCustomName());
        }
        smartChicken.setAge(chicken.getAge());
        return smartChicken;
    }
}
