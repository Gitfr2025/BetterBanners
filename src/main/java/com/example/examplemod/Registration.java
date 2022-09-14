package com.example.examplemod;

import com.example.examplemod.blockentities.TestBannerBlockEntity;
import com.example.examplemod.blocks.TestBannerBlock;
import com.example.examplemod.blocks.TestWallBannerBlock;
import com.example.examplemod.items.TestBannerItem;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {

	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterBanners.MODID);
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BetterBanners.MODID);
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BetterBanners.MODID);

	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	//blocks
	public static final RegistryObject<Block> TEST_BANNER_BLOCK = BLOCKS.register("testbanner", () -> new TestBannerBlock(null, BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TEST_WALL_BANNER_BLOCK = BLOCKS.register("testwallbanner", () -> new TestWallBannerBlock(DyeColor.WHITE, 
			BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(()-> TEST_BANNER_BLOCK.get())));
	
	//items
	public static final RegistryObject<Item> TEST_BANNER_ITEM = ITEMS.register("testbanner", () -> new TestBannerItem(TEST_BANNER_BLOCK.get(), TEST_WALL_BANNER_BLOCK.get(), new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_MISC)));

	//block entity types
	public static final RegistryObject<BlockEntityType<TestBannerBlockEntity>> BANNERS = 
			BLOCK_ENTITY_TYPES.register("banners", ()->
			BlockEntityType.Builder.of(TestBannerBlockEntity::new, TEST_BANNER_BLOCK.get()).build(null));
}
