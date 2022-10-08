package com.tagnumelite.chickens.api.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tagnumelite.chickens.Chickens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Utils {
    public static final Random RANDOM = new Random();
    public static final String TYPE_NBT = "ChickenId";
    public static final String FLUID_NBT = "FluidId";

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(Chickens.MOD_ID, path);
    }

    public static CompoundTag typeTag(ResourceLocation type) {
        CompoundTag tag = new CompoundTag();
        tag.putString(TYPE_NBT, type.toString());
        return tag;
    }

    public static ResourceLocation getTypeFromTag(@Nullable CompoundTag tag) {
        if (tag == null) return rl("void"); // TODO: Replace default
        return new ResourceLocation(tag.getString(TYPE_NBT));
    }

    public static ResourceLocation getTypeFromStack(ItemStack stack) {
        return getTypeFromTag(stack.getTag());
    }

    public static ItemStack itemWithType(Item item, ResourceLocation chickenId) {
        if (chickenId == null) return ItemStack.EMPTY;
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(TYPE_NBT, chickenId.toString());
        ItemStack itemStack = new ItemStack(item);
        itemStack.setTag(compoundTag);
        return itemStack;
    }

    public static ResourceLocation getFluidFromStack(ItemStack stack) {
        return getFluidFromTag(stack.getTag());
    }

    public static ResourceLocation getFluidFromTag(@Nullable CompoundTag tag) {
        if (tag == null) return null;
        return new ResourceLocation(tag.getString(FLUID_NBT));
    }

    public static ItemStack itemWithFluid(Item item, ResourceLocation fluid) {
        if (fluid == null) return ItemStack.EMPTY;
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(FLUID_NBT, fluid.toString());
        ItemStack itemStack = new ItemStack(item);
        itemStack.setTag(compoundTag);
        return itemStack;
    }

    public static ResourceLocation getItemKey(Item item) {
        @Nullable ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        if (key == null) throw new JsonSyntaxException("Item doesn't exist: " + item);
        return key;
    }

    public static JsonElement getJsonStack(ItemStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("item", getItemKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());

        if (stack.hasTag()) {
            json.addProperty("nbt", stack.getTag().getAsString());
        }

        return json;

    }

    public static boolean compareChickenStacks(ItemStack itemStack, ItemStack compareStack) {
        return itemStack.sameItem(compareStack) && getTypeFromStack(itemStack).equals(getTypeFromStack(compareStack));
    }

    public static ItemStack getStackWithChance(ItemStack stack, Double chance) {
        ItemStack copy = stack.copy();
        double calcChance = RANDOM.nextDouble(1F);

        double count = Math.floor(chance);
        double rem = chance - count;

        Chickens.LOGGER.debug("CHANCE {}: Count: {} REM: {} CALC: {}", chance, count, rem, calcChance);

        if (rem > calcChance) {
            count += 1F;
        }

        copy.setCount((int) count);

        return copy;
    }

    public static <O> O randChoice(List<O> choices) {
        return choices.get(RANDOM.nextInt(choices.size() - 1));
    }

    public static boolean compareRL(@Nullable ResourceLocation rl1, @Nullable ResourceLocation rl2) {
        if (rl1 == null && rl2 != null) return false;
        if (rl1 != null && rl2 == null) return false;
        if (rl1 == null && rl2 == null) return true;

        return rl1.equals(rl2);
    }

    /**
     * @param itemStacks
     * @return
     */
    public static List<ItemStack> copyItemStacks(Collection<ItemStack> itemStacks) {
        return itemStacks.stream().map(ItemStack::copy).toList();
    }
}
