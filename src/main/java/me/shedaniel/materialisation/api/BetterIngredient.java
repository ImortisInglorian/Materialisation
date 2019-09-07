package me.shedaniel.materialisation.api;

import me.shedaniel.materialisation.config.MaterialisationConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BetterIngredient {
    public Type type;
    public String content;
    private transient Lazy<ItemStack[]> stacks = new Lazy<>(() -> {
        if (type == Type.ITEM)
            return new ItemStack[]{new ItemStack(Registry.ITEM.get(new Identifier(content)))};
        return new ItemTags.CachingTag(new Identifier(content)).values().stream().map(ItemStack::new).collect(Collectors.toList()).toArray(new ItemStack[0]);
    });

    public BetterIngredient(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public static BetterIngredient fromItem(Item item) {
        return new BetterIngredient(Type.ITEM, Registry.ITEM.getId(item).toString());
    }

    public static BetterIngredient fromItem(Identifier item) {
        return new BetterIngredient(Type.ITEM, item.toString());
    }

    public static BetterIngredient fromTag(Tag<Item> tag) {
        return new BetterIngredient(Type.TAG, tag.getId().toString());
    }

    public static BetterIngredient fromTag(Identifier tag) {
        return new BetterIngredient(Type.TAG, tag.toString());
    }

    public MaterialisationConfig.ConfigIngredient toConfigIngredient() {
        return new MaterialisationConfig.ConfigIngredient(type, content);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BetterIngredient))
            return false;
        if (obj == this)
            return true;
        return ((BetterIngredient) obj).type == this.type && ((BetterIngredient) obj).content.equalsIgnoreCase(this.content);
    }

    public ItemStack[] getStacks() {
        return stacks.get();
    }

    public List<ItemStack> getStacksList() {
        return Arrays.asList(getStacks());
    }

    public boolean isIncluded(ItemStack itemStack_1) {
        if (itemStack_1 == null)
            return false;
        for (ItemStack stack : getStacks())
            if (stack.getItem() == itemStack_1.getItem())
                return true;
        return false;
    }

    public static enum Type {
        ITEM,
        TAG
    }
}