package com.example.examplemod;

import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BetterBanners.MODID)
public class BetterBanners
{
    // Directly reference a slf4j logger
    public static final String MODID = "bb";

    public BetterBanners()
    {
    	Registration.init();
    }
    
    
}
