package com.tagnumelite.chickens.datagen.chickens;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.chicken.ChickenData;
import com.tagnumelite.chickens.api.data.ChickenProvider;
import com.tagnumelite.chickens.common.FluidEggManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class VanillaChickenProvider extends ChickenProvider {
    public VanillaChickenProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Chickens.MOD_ID, existingFileHelper);
    }

    @Override
    protected void gatherChickens(@NotNull BiConsumer<ResourceLocation, ChickenData> consumer) {
        // Base Chickens
        chicken("smart").primaryColor(0xffff00).secondaryColor(0xffffff).addDrop(Items.EGG).addBiome(BiomeTags.IS_OVERWORLD).save(consumer);

        // - Dyes
        dyeChicken("white", DyeColor.WHITE).addDrop(Items.WHITE_DYE).addBiome(BiomeTags.IS_OVERWORLD).save(consumer);
        dyeChicken("yellow", DyeColor.YELLOW).addDrop(Items.YELLOW_DYE).save(consumer);
        dyeChicken("blue", DyeColor.BLUE).addDrop(Items.BLUE_DYE).save(consumer);
        dyeChicken("green", DyeColor.GREEN).addDrop(Items.GREEN_DYE).save(consumer);
        dyeChicken("red", DyeColor.RED).addDrop(Items.RED_DYE).save(consumer);
        dyeChicken("black", DyeColor.BLACK).addDrop(Items.BLACK_DYE).save(consumer);
        dyeChicken("pink", DyeColor.PINK).addDrop(Items.PINK_DYE).save(consumer);
        dyeChicken("purple", DyeColor.PURPLE).addDrop(Items.PURPLE_DYE).save(consumer);
        dyeChicken("orange", DyeColor.ORANGE).addDrop(Items.ORANGE_DYE).save(consumer);
        dyeChicken("light_blue", DyeColor.LIGHT_BLUE).addDrop(Items.LIGHT_BLUE_DYE).save(consumer);
        dyeChicken("lime", DyeColor.LIME).addDrop(Items.LIME_DYE).save(consumer);
        dyeChicken("gray", DyeColor.GRAY).addDrop(Items.GRAY_DYE).save(consumer);
        dyeChicken("cyan", DyeColor.CYAN).addDrop(Items.CYAN_DYE).save(consumer);
        dyeChicken("light_gray", DyeColor.LIGHT_GRAY).addDrop(Items.LIGHT_GRAY_DYE).save(consumer);
        dyeChicken("magenta", DyeColor.MAGENTA).addDrop(Items.MAGENTA_DYE).save(consumer);
        dyeChicken("brown", DyeColor.BROWN).addDrop(Items.BROWN_DYE).save(consumer);

        chicken("flint").color(0xa3a375, 0x6b6b47).addBiome(BiomeTags.IS_OVERWORLD).addDrop(Items.FLINT).save(consumer);
        chicken("quartz").color(0x1a0000, 0x4d0000).addDrop(Items.QUARTZ).addBiome(BiomeTags.IS_NETHER).save(consumer);
        // TODO: Log ChickenData colors
        chicken("oak").color(0x528358, 0x98846d).addDrop(Blocks.OAK_LOG).addBiome(BiomeTags.IS_OVERWORLD).save(consumer);
        chicken("spruce").color(0x528358, 0x98846d).addDrop(Blocks.SPRUCE_LOG).addBiome(Tags.Biomes.IS_COLD_OVERWORLD).save(consumer);// .addBiome(Biomes.TAIGA)
        chicken("birch").color(0x528358, 0x98846d).addDrop(Blocks.BIRCH_LOG).addBiome(BiomeTags.IS_FOREST).save(consumer); //.addBiome(Biomes.MEADOW)
        chicken("acacia").color(0x528358, 0x98846d).addDrop(Blocks.ACACIA_LOG).addBiome(BiomeTags.IS_SAVANNA).save(consumer);
        chicken("jungle").color(0x528358, 0x98846d).addDrop(Blocks.JUNGLE_LOG).addBiome(BiomeTags.IS_JUNGLE).save(consumer);
        chicken("dark_oak").color(0x528358, 0x98846d).addDrop(Blocks.DARK_OAK_LOG).addBiome(Tags.Biomes.IS_DENSE).save(consumer); // .addBiome(Biomes.DARK_FOREST)
        chicken("crimson").color(0x528358, 0x98846d).addDrop(Blocks.CRIMSON_STEM).addBiome(BiomeTags.IS_NETHER).save(consumer);
        chicken("warped").color(0x528358, 0x98846d).addDrop(Blocks.WARPED_STEM).addBiome(BiomeTags.IS_NETHER).save(consumer);
        chicken("mangrove").color(0x528358, 0x98846d).addDrop(Blocks.MANGROVE_LOG).save(consumer); //.addBiome(Biomes.MANGROVE_SWAMP)

        chicken("sand").color(0xa7a06c, 0xece5b1).addDrop(Blocks.SAND).addBiome(BiomeTags.IS_OVERWORLD).save(consumer);

        // Tier 2
        chicken("string").tier(2).color(0x800000, 0x331a00).addDrop(Items.STRING).addDrop(Items.SPIDER_EYE).save(consumer);
        chicken("glowstone").tier(2).color(0xffff00, 0xffff66).addDrop(Items.GLOWSTONE_DUST).save(consumer);
        chicken("gunpowder").tier(2).color(0x404040, 0x999999).addDrop(Items.GUNPOWDER).save(consumer);
        chicken("redstone").tier(2).color(0x800000, 0xe60000).addDrop(Items.REDSTONE).save(consumer);
        chicken("glass").tier(2).color(0xeeeeff, 0xffffff).addDrop(Blocks.GLASS).save(consumer);
        chicken("iron").tier(2).color(0xffcccc, 0xffffcc).addDrop(Items.IRON_INGOT).save(consumer);
        chicken("coal").tier(2).color(0x000000, 0x262626).addDrop(Items.COAL).save(consumer);

        // Tier 3
        chicken("gold").tier(3).color(0xffff80, 0xcccc00).addDrop(Items.GOLD_NUGGET).save(consumer);
        chicken("snowball").tier(3).color(0x0088cc, 0x33bbff).addDrop(Items.SNOWBALL).addBiome(Tags.Biomes.IS_COLD_OVERWORLD).save(consumer);
        chicken("water").tier(3).color(0x8080ff, 0x000099).addDrop(FluidEggManager.getEgg(modRl("water"))).save(consumer);
        chicken("lava").tier(3).color(0xffff00, 0xcc3300).addDrop(FluidEggManager.getEgg(modRl("lava"))).addBiome(BiomeTags.IS_NETHER).save(consumer);
        chicken("clay").tier(3).color(0xbfbfbf, 0xcccccc).addDrop(Items.CLAY_BALL).save(consumer);
        chicken("leather").tier(3).color(0x919191, 0xA7A06C).addDrop(Items.LEATHER).save(consumer);
        chicken("netherwart").tier(3).color(0x331a00, 0x800000).addDrop(Items.NETHER_WART).addBiome(BiomeTags.HAS_NETHER_FORTRESS).save(consumer);

        // Tier 4
        chicken("diamond").tier(4).color(0xe6f2ff, 0x99ccff).addDrop(Items.DIAMOND).save(consumer);
        chicken("blaze").tier(4).color(0xff3300, 0xffff66).addDrop(Items.BLAZE_ROD).save(consumer);
        chicken("slime").tier(4).color(0x99ffbb, 0x009933).addDrop(Items.SLIME_BALL).save(consumer);

        // Tier 5
        chicken("ender_pearl").tier(5).color(0x001a33, 0x001a00).addDrop(Items.ENDER_PEARL).save(consumer);
        chicken("ghast").tier(5).color(0xffffff, 0xffffcc).addDrop(Items.GHAST_TEAR).save(consumer);
        chicken("emerald").tier(5).color(0x003300, 0x00cc00).addDrop(Items.EMERALD).save(consumer);
        chicken("magma_cream").tier(5).color(0x000000, 0x1a0500).addDrop(Items.MAGMA_CREAM).save(consumer);
    }
}
