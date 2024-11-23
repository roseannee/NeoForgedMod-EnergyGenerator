package com.toremuyu.energygenerator;

import com.toremuyu.energygenerator.init.BlocksEG;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.proxy.HLConstants;

@Mod(EnergyGenerator.MOD_ID)
public class EnergyGenerator
{
	public static final String MOD_ID = "energygenerator";
	
	@CreativeTab.RegisterTab
	public static final CreativeTab MOD_TAB = new CreativeTab(id("root"),
			builder -> builder
					.icon(() -> BlocksEG.GENERATOR.asItem().getDefaultInstance())
					.withTabsBefore(HLConstants.HL_TAB.id())
	);
	
	public EnergyGenerator()
	{
		LanguageAdapter.registerMod(MOD_ID);
	}
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}