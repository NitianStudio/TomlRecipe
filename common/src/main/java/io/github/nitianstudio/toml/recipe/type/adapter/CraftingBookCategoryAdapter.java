package io.github.nitianstudio.toml.recipe.type.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.world.item.crafting.CraftingBookCategory;

import java.io.IOException;
import java.util.Arrays;

public class CraftingBookCategoryAdapter extends TypeAdapter<CraftingBookCategory> {
    @Override
    public void write(JsonWriter out, CraftingBookCategory value) throws IOException {
        out.value(value.getSerializedName());
    }

    @Override
    public CraftingBookCategory read(JsonReader in) throws IOException {
        String s = in.nextString();
        return Arrays.stream(CraftingBookCategory.values()).filter(value -> value.getSerializedName().equals(s)).findFirst().get();
    }
}
