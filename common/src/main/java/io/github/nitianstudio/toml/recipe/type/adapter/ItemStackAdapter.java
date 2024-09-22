package io.github.nitianstudio.toml.recipe.type.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.IOException;

public class ItemStackAdapter extends TypeAdapter<ItemStack> {
    @Override
    public void write(JsonWriter out, ItemStack value) throws IOException {
        out
                .beginObject()
                .name("item").value(BuiltInRegistries.ITEM.getKey(value.getItem()).toString())
                .name("count").value(value.getCount())
                .endObject();
    }

    @Override
    public ItemStack read(JsonReader in) throws IOException {
        in.beginObject();
        String item = in.nextName();
        String vItem = in.nextString();
        String count = in.nextName();
        int vCount = in.nextInt();
        in.endObject();
        return new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(vItem)), vCount);
    }
}
