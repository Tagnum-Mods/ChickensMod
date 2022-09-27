package com.tagnumelite.chickens.datagen;

import com.tagnumelite.chickens.Chickens;
import com.tagnumelite.chickens.common.entities.ModEntityTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModEntityTagProvider extends EntityTypeTagsProvider {
    public ModEntityTagProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, Chickens.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(EntityTypeTags.IMPACT_PROJECTILES).add(ModEntityTypes.COLORED_EGG.get());
    }
}
