package com.example.bb.item;

import com.example.bb.BetterBanners;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterBanners.MOD_ID);

    public static void register(IEventBus eventbus){
        ITEMS.register(eventbus);
    }
}
