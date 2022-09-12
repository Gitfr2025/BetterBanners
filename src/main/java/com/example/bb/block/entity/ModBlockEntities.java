package com.example.bb.block.entity;

import com.example.bb.BetterBanners;
import com.example.bb.block.ModBlocks;
import com.example.bb.block.entity.custom.MyBannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BetterBanners.MOD_ID);

    public static final RegistryObject<BlockEntityType<MyBannerBlockEntity>> MY_BANNER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("my_banner_block_entity", () ->
                    BlockEntityType.Builder.of(MyBannerBlockEntity::new,
                            ModBlocks.WHITE_MY_BANNER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
