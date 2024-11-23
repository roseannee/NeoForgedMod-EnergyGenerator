package com.toremuyu.energygenerator.init;

import com.toremuyu.energygenerator.custom.tiles.TileGenerator;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.forge.BlockAPI;

@SimplyRegister
public interface TilesEG {
    @RegistryName("generator")
    BlockEntityType<TileGenerator> GENERATOR = BlockAPI.createBlockEntityType(TileGenerator::new, BlocksEG.GENERATOR);
}
