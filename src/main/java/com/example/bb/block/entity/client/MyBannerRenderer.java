package com.example.bb.block.entity.client;

import com.example.bb.block.entity.custom.MyBannerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MyBannerRenderer implements BlockEntityRenderer<BannerBlockEntity> {
    private static final int BANNER_WIDTH = 20;
    private static final int BANNER_HEIGHT = 40;
    private static final int MAX_PATTERNS = 16;
    public static final String FLAG = "flag";
    private static final String POLE = "pole";
    private static final String BAR = "bar";
    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart bar;

    public MyBannerRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelpart = context.bakeLayer(ModelLayers.BANNER);
        this.flag = modelpart.getChild("flag");
        this.pole = modelpart.getChild("pole");
        this.bar = modelpart.getChild("bar");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("flag", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(44, 0).addBox(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("bar", CubeListBuilder.create().texOffs(0, 42).addBox(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void render(BannerBlockEntity bannerBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i1, int i2) {
        List<Pair<BannerPattern, DyeColor>> list = bannerBlockEntity.getPatterns();
        float f = 0.6666667F;
        boolean flag = bannerBlockEntity.getLevel() == null;
        poseStack.pushPose();
        long i;
        if (flag) {
            i = 0L;
            poseStack.translate(0.5D, 0.5D, 0.5D);
            this.pole.visible = true;
        } else {
            i = bannerBlockEntity.getLevel().getGameTime();
            BlockState blockstate = bannerBlockEntity.getBlockState();
            if (blockstate.getBlock() instanceof BannerBlock) {
                poseStack.translate(0.5D, 0.5D, 0.5D);
                float f1 = (float)(-blockstate.getValue(BannerBlock.ROTATION) * 360) / 16.0F;
                poseStack.mulPose(Vector3f.YP.rotationDegrees(f1));
                this.pole.visible = true;
            } else {
                poseStack.translate(0.5D, (double)-0.16666667F, 0.5D);
                float f3 = -blockstate.getValue(WallBannerBlock.FACING).toYRot();
                poseStack.mulPose(Vector3f.YP.rotationDegrees(f3));
                poseStack.translate(0.0D, -0.3125D, -0.4375D);
                this.pole.visible = false;
            }
        }

        poseStack.pushPose();
        poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer vertexconsumer = ModelBakery.BANNER_BASE.buffer(multiBufferSource, RenderType::entitySolid);
        this.pole.render(poseStack, vertexconsumer, i1, i2);
        this.bar.render(poseStack, vertexconsumer, i1, i2);
        BlockPos blockpos = bannerBlockEntity.getBlockPos();
        float f2 = ((float)Math.floorMod((long)(blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + i, 100L) + v) / 100.0F;
        this.flag.xRot = (-0.0125F + 0.01F * Mth.cos(((float)Math.PI * 2F) * f2)) * (float)Math.PI;
        this.flag.y = -32.0F;
        renderPatterns(poseStack, multiBufferSource, i1, i2, this.flag, ModelBakery.BANNER_BASE, true, list);
        poseStack.popPose();
        poseStack.popPose();
    }

    public static void renderPatterns(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, ModelPart modelPart, Material material, boolean b, List<Pair<BannerPattern, DyeColor>> pairs) {
        renderPatterns(poseStack, multiBufferSource, i, j, modelPart, material, b, pairs, false);
    }

    public static void renderPatterns(PoseStack poseStack, MultiBufferSource multiBufferSource, int i1, int i2, ModelPart modelPart, Material material1, boolean b, List<Pair<BannerPattern, DyeColor>> list, boolean b1) {
        modelPart.render(poseStack, material1.buffer(multiBufferSource, RenderType::entitySolid, b1), i1, i2);

        for(int i = 0; i < 17 && i < list.size(); ++i) {
            Pair<BannerPattern, DyeColor> pair = list.get(i);
            float[] afloat = pair.getSecond().getTextureDiffuseColors();
            BannerPattern bannerpattern = pair.getFirst();
            Material material = b ? Sheets.getBannerMaterial(bannerpattern) : Sheets.getShieldMaterial(bannerpattern);
            modelPart.render(poseStack, material.buffer(multiBufferSource, RenderType::entityNoOutline), i1, i2, afloat[0], afloat[1], afloat[2], 1.0F);
        }

    }
}
