package com.toremuyu.energygenerator.init;

import com.toremuyu.energygenerator.custom.blocks.BlockGenerator;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface BlocksEG  {
    @RegistryName("generator")
    BlockGenerator GENERATOR = new BlockGenerator();
}