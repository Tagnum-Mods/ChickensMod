package com.tagnumelite.chickens.datagen;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.api.utils.TranslationUtils;
import com.tagnumelite.chickens.api.utils.constants.TranslationConstants;
import com.tagnumelite.chickens.client.ModTabs;
import com.tagnumelite.chickens.common.blocks.ModBlocks;
import com.tagnumelite.chickens.common.entities.ModEntityTypes;
import com.tagnumelite.chickens.common.items.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator generator) {
        super(generator, Chickens.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addGeneral();
        addEntities();
        addBlocks();
        addItems();
        addGUIs();
        addJade();
        addPatchouli();
    }

    private void addGeneral() {
        addCreativeTab(ModTabs.GENERAL, "Chickens");
    }

    private void addEntities() {
        add(ModEntityTypes.CHICKEN.get(), "Chickens Chicken");

        // Misc
        addChicken("smart", "Smart Chicken");

        // Colored Chickens
        addChicken("white", "White Chicken");
        addChicken("yellow", "Yellow Chicken");
        addChicken("blue", "Blue Chicken");
        addChicken("green", "Green Chicken");
        addChicken("red", "Red Chicken");
        addChicken("black", "Black Chicken");
        addChicken("pink", "Pink Chicken");
        addChicken("purple", "Purple Chicken");
        addChicken("orange", "Orange Chicken");
        addChicken("light_blue", "Light Blue Chicken");
        addChicken("lime", "Lime Chicken");
        addChicken("gray", "Gray Chicken");
        addChicken("cyan", "Cyan Chicken");
        addChicken("light_gray", "Light Gray Chicken");
        addChicken("magenta", "Magenta Chicken");
        addChicken("brown", "Brown Chicken");

        // Tier 1
        addChicken("flint", "Flint Chicken");
        addChicken("quartz", "Quartz Chicken");
        addChicken("oak", "Oak Chicken");
        addChicken("spruce", "Spruce Chicken");
        addChicken("birch", "Birch Chicken");
        addChicken("acacia", "Acacia Chicken");
        addChicken("jungle", "Jungle Chicken");
        addChicken("dark_oak", "Dark Oak Chicken");
        addChicken("crimson", "Crimson Chicken");
        addChicken("warped", "Warped Chicken");
        addChicken("mangrove", "Mangrove Chicken");
        addChicken("sand", "Sand Chicken");

        // Tier 2
        addChicken("string", "String Chicken");
        addChicken("glowstone", "Glowstone Chicken");
        addChicken("gunpowder", "Gunpowder Chicken");
        addChicken("redstone", "Redstone Chicken");
        addChicken("glass", "Glass Chicken");
        addChicken("iron", "Iron Chicken");
        addChicken("coal", "Coal Chicken");

        // Tier 3
        addChicken("gold", "Gold Chicken");
        addChicken("snowball", "Snowball Chicken");
        addChicken("water", "Water Chicken");
        addChicken("lava", "Lava Chicken");
        addChicken("clay", "Clay Chicken");
        addChicken("leather", "Leather Chicken");
        addChicken("netherwart", "Netherwart Chicken");

        // Tier 4
        addChicken("diamond", "Sparkly Chicken");
        addChicken("blaze", "Blazing Chicken");
        addChicken("slime", "Slimy Chicken");

        // Tier 5
        addChicken("ender_pearl", "Ender Chicken");
        addChicken("ghast", "Ghastly Chicken");
        addChicken("emerald", "Emerald Chicken");
        addChicken("magma_cream", "Magma Cream Chicken");
    }

    private void addBlocks() {
        add(ModBlocks.COOP_ACACIA.get(), "Acacia Coop");
        add(ModBlocks.COOP_BIRCH.get(), "Birch Coop");
        add(ModBlocks.COOP_CRIMSON.get(), "Crimson Coop");
        add(ModBlocks.COOP_DARK_OAK.get(), "Dark Oak Coop");
        add(ModBlocks.COOP_JUNGLE.get(), "Jungle Coop");
        add(ModBlocks.COOP_MANGROVE.get(), "Mangrove Coop");
        add(ModBlocks.COOP_OAK.get(), "Oak Coop");
        add(ModBlocks.COOP_SPRUCE.get(), "Spruce Coop");
        add(ModBlocks.COOP_WARPED.get(), "Warped Coop");

        add(ModBlocks.HENHOUSE_ACACIA.get(), "Acacia Henhouse");
        add(ModBlocks.HENHOUSE_BIRCH.get(), "Birch Henhouse");
        add(ModBlocks.HENHOUSE_CRIMSON.get(), "Crimson Henhouse");
        add(ModBlocks.HENHOUSE_DARK_OAK.get(), "Dark Oak Henhouse");
        add(ModBlocks.HENHOUSE_JUNGLE.get(), "Jungle Henhouse");
        add(ModBlocks.HENHOUSE_MANGROVE.get(), "Mangrove Henhouse");
        add(ModBlocks.HENHOUSE_OAK.get(), "Oak Henhouse");
        add(ModBlocks.HENHOUSE_SPRUCE.get(), "Spruce Henhouse");
        add(ModBlocks.HENHOUSE_WARPED.get(), "Warped Henhouse");
    }

    private void addItems() {
        add(ModItems.ANALYZER.get(), "Chicken Analyzer");
        add(ModItems.SPAWN_EGG.get(), "~~~~~~ Spawn Egg");

        add("item.chickens.fluid_egg.", "");
    }

    private void addGUIs() {
        add(TranslationConstants.ANALYZER_TOOLTIP_1, "Use it on chickens to determine their stats.");
        add(TranslationConstants.ANALYZER_TOOLTIP_2, " - \"Which day of the week do chickens hate most? Fry-day!\"");
        add(TranslationConstants.NAME, "Name: ");

        add(TranslationUtils.GUI_CONTAINER(TranslationConstants.HENHOUSE_TOOLTIP), "It will automatically collect items from chickens in 9x9x9 radius when provided with hay bale.");
        add(TranslationUtils.GUI_CONTAINER("coop"), "Coop");

        add(TranslationConstants.CHICKEN_LAY_PROGRESS, "Next egg in ~%1$smin.");
        add(TranslationConstants.CHICKEN_NEXT_EGG_SOON, "Next egg in <1min.");
        add(TranslationConstants.CHICKEN_STATS_TIER, "Tier: %1$s");
        add(TranslationConstants.CHICKEN_STATS_GROWTH, "Growth: %1$s");
        add(TranslationConstants.CHICKEN_STATS_GAIN, "Gain: %1$s");
        add(TranslationConstants.CHICKEN_STATS_STRENGTH, "Strength: %1$s");
        add(TranslationConstants.NULL_CHICKEN, "Null Chicken - Doesn't Exist");

        add(TranslationUtils.GUI(TranslationConstants.JEI_THROWING), "Egg Throwing");
        add(TranslationUtils.GUI(TranslationConstants.JEI_BREEDING), "Chicken Breeding");
        add(TranslationUtils.GUI(TranslationConstants.JEI_LAYING), "Chicken Laying");
    }

    private void addJade() {
        add("config.jade.plugin_chickens.chicken_provider", "Chicken"); // TODO: Untested
        add("config.jade.plugin_chickens.coop", "Coop");
    }

    private void addPatchouli() {
        add("patchouli.chickens.book.title", "Art of Chickens");
        add("patchouli.chickens.book.landing", "- Not Sun Tzu");
    }

    protected void addCreativeTab(CreativeModeTab tab, String value) {
        add(tab.getDisplayName().getString(), value);
    }

    protected void addChicken(String key, String name) {
        add("entity." + Chickens.MOD_ID + '.' + key + ".name", name);
    }
}
