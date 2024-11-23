package com.toremuyu.energygenerator.init;

import com.toremuyu.energygenerator.custom.blocks.BlockGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@SimplyRegister
public interface BlocksEG  {
    @RegistryName("generator")
    BlockGenerator GENERATOR = new BlockGenerator();

    static void recipes(RegisterRecipesEvent event) {
        event.shaped()
                .shape("bbb", "bpb", "bbb")
                .map('b', Blocks.IRON_BLOCK)
                .map('p', Items.BLAZE_POWDER)
                .result(BlocksEG.GENERATOR)
                .register();
    }
}