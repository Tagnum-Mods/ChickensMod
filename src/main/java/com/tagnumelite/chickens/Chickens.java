package com.tagnumelite.chickens;

import com.mojang.logging.LogUtils;
import com.tagnumelite.chickens.client.menus.ModMenuTypes;
import com.tagnumelite.chickens.client.renderers.ChickensChickenRenderer;
import com.tagnumelite.chickens.client.renderers.CoopBlockRenderer;
import com.tagnumelite.chickens.client.screens.CoopScreen;
import com.tagnumelite.chickens.client.screens.HenhouseScreen;
import com.tagnumelite.chickens.common.ChickenManager;
import com.tagnumelite.chickens.common.FluidEggManager;
import com.tagnumelite.chickens.common.blocks.ModBlockEntityTypes;
import com.tagnumelite.chickens.common.blocks.ModBlocks;
import com.tagnumelite.chickens.common.entities.ChickensChicken;
import com.tagnumelite.chickens.common.entities.ModEntityTypes;
import com.tagnumelite.chickens.common.items.ModItems;
import com.tagnumelite.chickens.config.ClientConfig;
import com.tagnumelite.chickens.config.ServerConfig;
import com.tagnumelite.chickens.crafting.ModRecipeSerializers;
import com.tagnumelite.chickens.crafting.ModRecipeTypes;
import com.tagnumelite.chickens.integration.top.ChickensTOPPlugin;
import com.tagnumelite.chickens.network.ModNetwork;
import com.tagnumelite.chickens.proxy.ClientProxy;
import com.tagnumelite.chickens.proxy.CommonProxy;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Created by setyc on 12.02.2016.
 */
@Mod(Chickens.MOD_ID)
public class Chickens {
    public static final String MOD_ID = "chickens";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    private static final ChickenManager CHICKEN_MANAGER = new ChickenManager();
    private static final FluidEggManager FLUID_EGG_MANAGER = new FluidEggManager();

    public Chickens() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onRegisterItemColorHandlers);
        modEventBus.addListener(this::onEntityAttributeCreation);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ChickenTeachHandler());
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(CHICKEN_MANAGER);
            event.addListener(FLUID_EGG_MANAGER);
        });
        MinecraftForge.EVENT_BUS.addListener((InterModEnqueueEvent event) -> {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", ChickensTOPPlugin::new);
        });
    }

    public static ChickenManager getChickenManager() {
        return CHICKEN_MANAGER;
    }

    public static FluidEggManager getLiquidEggManager() {
        return FLUID_EGG_MANAGER;
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModNetwork.register();
        });
        // TODO: Biome Spawning
        /*
        List<Biome> biomesForSpawning = getAllSpawnBiomes();
        if (biomesForSpawning.size() > 0) {
            EntityRegistry.addSpawn(ChickensChicken.class, ServerConfig.SPAWN_PROBABILITY.get(), ServerConfig.MIN_BROOD_SIZE.get(), ServerConfig.MAX_BROOD_SIZE.get(), EnumCreatureType.CREATURE,
                    biomesForSpawning.toArray(new Biome[0])
            );
            if (biomesForSpawning.contains(Biomes.NETHER_WASTES)) {
                MinecraftForge.TERRAIN_GEN_BUS.register(new ChickenNetherPopulateHandler(ServerConfig.NETHER_SPAWN_CHANCE.get()));
            }
        }
         */

        PROXY.registerItemHandlers();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.HENHOUSE.get(), HenhouseScreen::new);
        MenuScreens.register(ModMenuTypes.COOP.get(), CoopScreen::new);

        EntityRenderers.register(ModEntityTypes.COLORED_EGG.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntityTypes.CHICKEN.get(), ChickensChickenRenderer::new);

        BlockEntityRenderers.register(ModBlockEntityTypes.COOP.get(), CoopBlockRenderer::new);
    }

    private void onRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, layer) -> ((ItemColor) stack.getItem()).getColor(stack, layer), ModItems.SPAWN_EGG.get(), ModItems.COLORED_EGG.get(), ModItems.FLUID_EGG.get());
    }

    private void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.CHICKEN.get(), ChickensChicken.createAttributes().build());
    }
}
