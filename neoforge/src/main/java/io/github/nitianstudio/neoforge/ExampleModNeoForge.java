package io.github.nitianstudio.neoforge;

import net.neoforged.fml.common.Mod;

import io.github.nitianstudio.toml.recipe.TomlRecipe;

@Mod(TomlRecipe.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        TomlRecipe.init();
    }
}
