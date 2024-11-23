package com.toremuyu.energygenerator.init;

import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@ProvideRecipes
public class RecipesEG implements IRecipeProvider {
    @Override
    public void provideRecipes(RegisterRecipesEvent event) {
        BlocksEG.recipes(event);
    }
}
