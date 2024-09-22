package io.github.nitianstudio.toml.recipe.type.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.io.IOException;

public class RecipeTypeAdapter extends TypeAdapter<RecipeType<?>> {
    @Override
    public void write(JsonWriter out, RecipeType<?> value) throws IOException {
        ResourceLocation key = BuiltInRegistries.RECIPE_TYPE.getKey(value);
        if (key != null) {
            out.value(key.toString());
        }
    }

    @Override
    public RecipeType<?> read(JsonReader in) throws IOException {
        return BuiltInRegistries.RECIPE_TYPE.get(ResourceLocation.parse(in.nextString()));
    }
}
