package io.github.nitianstudio.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import io.github.nitianstudio.toml.recipe.TomlRecipe;

public final class ExampleModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        // Run our common setup.
        TomlRecipe.init();
    }
}
