package com.example.examplemod.items;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.example.examplemod.blockentities.AbstractTestBannerBlock;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;

public class TestBannerItem extends StandingAndWallBlockItem {
   private static final String PATTERN_PREFIX = "block.bb.banner.";

   public TestBannerItem(Block p_40534_, Block p_40535_, Item.Properties p_40536_) {
      super(p_40534_, p_40535_, p_40536_);
      Validate.isInstanceOf(AbstractTestBannerBlock.class, p_40534_);
      Validate.isInstanceOf(AbstractTestBannerBlock.class, p_40535_);
   }

   public static void appendHoverTextFromBannerBlockEntityTag(ItemStack p_40543_, List<Component> p_40544_) {
      CompoundTag compoundtag = BlockItem.getBlockEntityData(p_40543_);
      if (compoundtag != null && compoundtag.contains("Patterns")) {
         ListTag listtag = compoundtag.getList("Patterns", 10);

         for(int i = 0; i < listtag.size() && i < 6; ++i) {
            CompoundTag compoundtag1 = listtag.getCompound(i);
            DyeColor dyecolor = DyeColor.byId(compoundtag1.getInt("Color"));
            BannerPattern bannerpattern = BannerPattern.byHash(compoundtag1.getString("Pattern"));
            if (bannerpattern != null) {
               net.minecraft.resources.ResourceLocation fileLoc = new net.minecraft.resources.ResourceLocation(bannerpattern.getFilename());
               p_40544_.add((new TranslatableComponent("block." + fileLoc.getNamespace() + ".banner." + fileLoc.getPath() + "." + dyecolor.getName())).withStyle(ChatFormatting.GRAY));
            }
         }

      }
   }

   public DyeColor getColor() {
      return ((AbstractTestBannerBlock)this.getBlock()).getColor();
   }

   public void appendHoverText(ItemStack p_40538_, @Nullable Level p_40539_, List<Component> p_40540_, TooltipFlag p_40541_) {
      appendHoverTextFromBannerBlockEntityTag(p_40538_, p_40540_);
   }
}
