package io.github.nitianstudio.toml.recipe.type.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.nitianstudio.toml.recipe.type.Ingredient;
import net.minecraft.core.registries.BuiltInRegistries;

import java.io.IOException;

public class IngredientAdapter extends TypeAdapter<Ingredient> {
    @Override
    public void write(JsonWriter out, Ingredient value) throws IOException {
        if (value.tagKey != null) {
            out.beginObject().name("tag").value(value.tagKey.location().toString()).endObject();
        } else if (value.item != null) {
            out.beginObject().name("item").value(BuiltInRegistries.ITEM.getKey(value.item).toString()).endObject();
        }
    }

    @Override
    public Ingredient read(JsonReader in) throws IOException {
        return null;
    }
}
