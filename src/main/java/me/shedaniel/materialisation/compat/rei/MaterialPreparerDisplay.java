package me.shedaniel.materialisation.compat.rei;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class MaterialPreparerDisplay implements RecipeDisplay {

    private ItemStack first, result;
    private List<ItemStack> second;

    public MaterialPreparerDisplay(ItemStack first, List<ItemStack> second, ItemStack result) {
        this.first = first;
        this.second = second;
        this.result = result;
    }

    public ItemStack getFirst() {
        return first;
    }

    public List<ItemStack> getSecond() {
        return second;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public List<List<ItemStack>> getInput() {
        return ImmutableList.of(Collections.singletonList(getFirst()), getSecond());
    }

    @Override
    public List<ItemStack> getOutput() {
        return Collections.singletonList(getResult());
    }

    @Override
    public Identifier getRecipeCategory() {
        return MaterialisationREIPlugin.MATERIAL_PREPARER;
    }
}
