package io.github.nitianstudio.toml.recipe.type;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import io.github.nitianstudio.toml.recipe.type.adapter.CraftingBookCategoryAdapter;
import io.github.nitianstudio.toml.recipe.type.adapter.IngredientAdapter;
import io.github.nitianstudio.toml.recipe.type.adapter.ItemStackAdapter;
import io.github.nitianstudio.toml.recipe.type.adapter.ResourceLocationAdapter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Map;

public record ShapedRecipe(
        @SerializedName("category") @JsonAdapter(CraftingBookCategoryAdapter.class) CraftingBookCategory category,
        @SerializedName("group") @JsonAdapter(ResourceLocationAdapter.class) ResourceLocation group,
        @SerializedName("key")  Map<Character, Ingredient> key,
        @SerializedName("result") @JsonAdapter(ItemStackAdapter.class) ItemStack result,
        @SerializedName("pattern")
        String... pattern
        ) {

}
