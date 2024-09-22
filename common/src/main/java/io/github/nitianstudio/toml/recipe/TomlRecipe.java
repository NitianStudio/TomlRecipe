package io.github.nitianstudio.toml.recipe;

import com.moandjiezana.toml.Toml;
import com.mojang.datafixers.util.Pair;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

public final class TomlRecipe {
    public static final String MOD_ID = "toml_recipe";

    public static void init() {
        LifecycleEvent.SERVER_BEFORE_START.register(instance -> {
            ResourceManager resourceManager = instance.getResourceManager();
            RecipeManager recipeManager = instance.getRecipeManager();
            for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources("recipe", location -> location.getPath().endsWith(".toml")).entrySet()) {
                ResourceLocation k = entry.getKey();
                Resource resource = entry.getValue();
                try (InputStream open = resource.open()) {
                    Toml read = new Toml().read(open);
                    HashMap<Character, net.minecraft.world.item.crafting.Ingredient> ingredientMap = new HashMap<>();
                    HashMap<Character, ItemStack> itemStackMap = new HashMap<>();
                    checkStack(read, itemStackMap, ingredientMap);
                    for (Map.Entry<String, Object> e : read.entrySet()) {
                        if (e.getKey().equals("minecraft") && e.getValue() instanceof Toml toml) {
                            for (Map.Entry<String, Object> ee : toml.entrySet()) {
                                if (ee.getKey().equals("crafting_shaped") && ee.getValue() instanceof Toml shapedToml
                                        && shapedToml.contains("key") && shapedToml.contains("result") && shapedToml.contains("category")) {
                                    Map<Character, Ingredient> cim = new HashMap<>();
                                    List<String> keys = new ArrayList<>();
                                    for (Toml kToml : shapedToml.getTables("key")) {
                                        String key = kToml.getString("key");
                                        keys.add(key);
                                        for (int i = 0; i < key.length(); i++) {
                                            char c = key.charAt(i);
                                            if (ingredientMap.containsKey(c)) {
                                                cim.putIfAbsent(c, ingredientMap.get(c));
                                            } else if (itemStackMap.containsKey(c)) {
                                                ItemStack stack = itemStackMap.get(c).copy();
                                                stack.setCount(1);
                                                cim.putIfAbsent(c, Ingredient.of(stack));
                                            }
                                        }
                                    }
                                    ItemStack result = itemStackMap.get(shapedToml.getString("result").charAt(0));

                                    RecipeHolder<ShapedRecipe> shapedRecipeRecipeHolder = new RecipeHolder<>(shapedToml.contains("location")
                                            ? ResourceLocation.parse(shapedToml.getString("location"))
                                            : BuiltInRegistries.ITEM.getKey(result.getItem()),
                                            new ShapedRecipe(
                                                    shapedToml.contains("group") ? shapedToml.getString("group") : "",
                                                    CraftingBookCategory.valueOf(shapedToml.getString("category").toUpperCase(Locale.ROOT)),
                                                    ShapedRecipePattern.of(cim, keys), result
                                            )
                                    );
                                    recipeManager.getRecipes().add(shapedRecipeRecipeHolder);
                                }
                            }
                        }
                    }
//                    for (Toml minecraft : read.getTables("minecraft")) {
//                        for (Toml craftingShaped : minecraft.getTables("crafting_shaped")) {
//                            List<String> key = craftingShaped.getList("key");
//                            Map<Character, net.minecraft.world.item.crafting.Ingredient> objectObjectHashMap = new HashMap<>();
//
//                            for (String s : key) {
//                                for (int i = 0; i < s.length(); i++) {
//                                    char c = s.charAt(0);
//                                    for (Map.Entry<Character, Ingredient> cie : ingredientMap.entrySet()) {
//                                        if (cie.getKey() == c) {
//                                            objectObjectHashMap.put(c, cie.getValue());
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//
//                            char c = craftingShaped.getString("result").charAt(0);
//                            if (itemStackMap.containsKey(c)) {
//                                net.minecraft.world.item.crafting.ShapedRecipe shapedRecipe = new net.minecraft.world.item.crafting.ShapedRecipe(
//                                        craftingShaped.getString("group"),
//                                        CraftingBookCategory.valueOf(craftingShaped.getString("category")),
//                                        ShapedRecipePattern.of(objectObjectHashMap, key), itemStackMap.get(c));
//                                ResourceLocation resourceLocation = craftingShaped.contains("location")
//                                        ? ResourceLocation.parse(craftingShaped.getString("location"))
//                                        : BuiltInRegistries.ITEM.getKey(itemStackMap.get(c).getItem());
//                                RecipeHolder<ShapedRecipe> shapedRecipeRecipeHolder = new RecipeHolder<>(resourceLocation
//                                        , shapedRecipe);
//                                recipeManager.getRecipes().add(shapedRecipeRecipeHolder);
//                            }
//
//                        }
//                    }
                } catch (IOException ignored) {

                }

            }
        });
    }

    private static void checkStack(Toml read, HashMap<Character, ItemStack> itemStackMap, HashMap<Character, Ingredient> ingredientMap) {
        for (Map.Entry<String, Object> e : read.entrySet()) {
            if (e.getKey().equals("pattern") && e.getValue() instanceof Toml toml) {
                for (Map.Entry<String, Object> ee : toml.entrySet()) {
                    if (ee.getKey().equals("item") && ee.getValue() instanceof Toml item && item.contains("char") && item.contains("item")) {
                        int count = item.contains("count") ? item.getLong("count").intValue() : 1;
                        itemStackMap.put(item.getString("char").charAt(0), new ItemStack(
                                BuiltInRegistries.ITEM.get(ResourceLocation.parse(item.getString("item"))),
                                count
                        ));
                    } else if (ee.getKey().equals("tag") && ee.getValue() instanceof Toml tagToml && tagToml.contains("tag") && tagToml.contains("char")) {
                        ingredientMap.put(tagToml.getString("char").charAt(0), Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.parse(tagToml.getString("tag")))));
                    }
                }

            }
        }
    }
}
