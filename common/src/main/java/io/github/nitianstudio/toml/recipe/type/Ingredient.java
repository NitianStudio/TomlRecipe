package io.github.nitianstudio.toml.recipe.type;

import com.google.gson.annotations.JsonAdapter;
import io.github.nitianstudio.toml.recipe.type.adapter.IngredientAdapter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@JsonAdapter(IngredientAdapter.class)
public class Ingredient {
    public TagKey<Item> tagKey;
    public Item item;

    public Ingredient tagKey(TagKey<Item> tagKey) {
        this.tagKey = tagKey;
        return this;
    }

    public Ingredient item(Item item) {
        this.item = item;
        return this;
    }
}
