package com.example.bb.block.entity.custom;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class MyBannerBlockEntity extends BlockEntity implements Nameable {
    public static final int MAX_PATTERNS = 6;
    public static final String TAG_PATTERNS = "Patterns";
    public static final String TAG_PATTERN = "Pattern";
    public static final String TAG_COLOR = "Color";
    @Nullable
    private Component name;
    private DyeColor baseColor;
    @Nullable
    private ListTag itemPatterns;
    @Nullable
    private List<Pair<BannerPattern, DyeColor>> patterns;

    public MyBannerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityType.BANNER, blockPos, blockState);
        this.baseColor = ((AbstractBannerBlock)blockState.getBlock()).getColor();
    }

    public MyBannerBlockEntity(BlockPos blockPos, BlockState blockState, DyeColor dyeColor) {
        this(blockPos, blockState);
        this.baseColor = dyeColor;
    }

    @Nullable
    public static ListTag getItemPatterns(ItemStack itemStack) {
        ListTag listtag = null;
        CompoundTag compoundtag = BlockItem.getBlockEntityData(itemStack);
        if (compoundtag != null && compoundtag.contains("Patterns", 9)) {
            listtag = compoundtag.getList("Patterns", 10).copy();
        }

        return listtag;
    }

    public void fromItem(ItemStack itemStack, DyeColor dyeColor) {
        this.baseColor = dyeColor;
        this.fromItem(itemStack);
    }

    public void fromItem(ItemStack itemStack) {
        this.itemPatterns = getItemPatterns(itemStack);
        this.patterns = null;
        this.name = itemStack.hasCustomHoverName() ? itemStack.getHoverName() : null;
    }

    public Component getName() {
        return (Component)(this.name != null ? this.name : new TranslatableComponent("block.minecraft.banner"));
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }

    public void setCustomName(Component p_58502_) {
        this.name = p_58502_;
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (this.itemPatterns != null) {
            compoundTag.put("Patterns", this.itemPatterns);
        }

        if (this.name != null) {
            compoundTag.putString("CustomName", Component.Serializer.toJson(this.name));
        }

    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(compoundTag.getString("CustomName"));
        }

        this.itemPatterns = compoundTag.getList("Patterns", 10);
        this.patterns = null;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public static int getPatternCount(ItemStack p_58505_) {
        CompoundTag compoundtag = BlockItem.getBlockEntityData(p_58505_);
        return compoundtag != null && compoundtag.contains("Patterns") ? compoundtag.getList("Patterns", 10).size() : 0;
    }

    public List<Pair<BannerPattern, DyeColor>> getPatterns() {
        if (this.patterns == null) {
            this.patterns = createPatterns(this.baseColor, this.itemPatterns);
        }

        return this.patterns;
    }

    public static List<Pair<BannerPattern, DyeColor>> createPatterns(DyeColor dyeColor, @Nullable ListTag listTag) {
        List<Pair<BannerPattern, DyeColor>> list = Lists.newArrayList();
        list.add(Pair.of(BannerPattern.BASE, dyeColor));
        if (listTag != null) {
            for(int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundtag = listTag.getCompound(i);
                BannerPattern bannerpattern = BannerPattern.byHash(compoundtag.getString("Pattern"));
                if (bannerpattern != null) {
                    int j = compoundtag.getInt("Color");
                    list.add(Pair.of(bannerpattern, DyeColor.byId(j)));
                }
            }
        }

        return list;
    }

    public static void removeLastPattern(ItemStack itemStack) {
        CompoundTag compoundtag = BlockItem.getBlockEntityData(itemStack);
        if (compoundtag != null && compoundtag.contains("Patterns", 9)) {
            ListTag listtag = compoundtag.getList("Patterns", 10);
            if (!listtag.isEmpty()) {
                listtag.remove(listtag.size() - 1);
                if (listtag.isEmpty()) {
                    compoundtag.remove("Patterns");
                }

                BlockItem.setBlockEntityData(itemStack, BlockEntityType.BANNER, compoundtag);
            }
        }
    }

    public ItemStack getItem() {
        ItemStack itemstack = new ItemStack(BannerBlock.byColor(this.baseColor));
        if (this.itemPatterns != null && !this.itemPatterns.isEmpty()) {
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.put("Patterns", this.itemPatterns.copy());
            BlockItem.setBlockEntityData(itemstack, this.getType(), compoundtag);
        }

        if (this.name != null) {
            itemstack.setHoverName(this.name);
        }

        return itemstack;
    }

    public DyeColor getBaseColor() {
        return this.baseColor;
    }
}
