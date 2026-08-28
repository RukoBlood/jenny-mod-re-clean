/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.girls.Mangelie;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

import com.trolmastercard.sexmod.util.EntityLookVectorHelper;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathGeometryRender;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlRenderer;
import com.trolmastercard.sexmod.util.*;
import com.trolmastercard.sexmod.util.anim.BoneDeformProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

// dh_class182
public class ManglelieRenderer
extends GirlRenderer<ManglelieEntity> {
    final static ColorRGBA CORRUPTION_COLOR_MAIN = new ColorRGBA(115, 108, 188, 255);
    final static Vector3fSexmodSpecial OFFSET_BODY = new Vector3fSexmodSpecial(0.05f, 0.04f, 0.0f);
    final static Vector3fSexmodSpecial OFFSET_ARM = new Vector3fSexmodSpecial(0.0f, 0.065f, 0.0f);
    final static Vector3fSexmodSpecial OFFSET_LEG = new Vector3fSexmodSpecial(0.0f, 0.03f, 0.03f);
    final static ColorRGBA CORRUPTION_COLOR_DARK = new ColorRGBA(63, 59, 150, 255);
    final static ColorRGBA CORRUPTION_COLOR_LIGHT = new ColorRGBA(79, 74, 188, 255);
    final static public HashSet<String> BLACKLISTED_BONES = new HashSet<String>(){
        {
            this.add("boobs2");
            this.add("booty2");
            this.add("vagina2");
            this.add("fuckhole2");
        }
    };
    boolean initialized = false;

    public ManglelieRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        if (!this.initialized) {
            BLACKLISTED_BONES.addAll(BoneDeformProcessor.EXCLUDED_MESH_BONES);
            this.initialized = true;
        }
        return BLACKLISTED_BONES;
    }

    @Override
    public void doRender(ManglelieEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.isManglelieLooking(entity)) {
            if (!this.canRideMommy(entity)) {
                if (!isManglelieLooking(entity, 0.5f)) {
                    if (!this.isRidingMommy(entity)) {
                        super.doRender(entity, x, y, z, entityYaw, partialTicks);
                        renderMangleliePov(entity, partialTicks);
                    }
                }
            }
        }
    }

    boolean isRidingMommy(ManglelieEntity manglelie) {
        GalathEntity galath = manglelie.getMommyGalath(false);
        if (galath == null) {
            return false;
        }
        switch (galath.getCurrentAction()) {
            case CONTROLLED_FLIGHT: 
            case BOOST: {
                return true;
            }
        }
        return false;
    }

    boolean canRideMommy(ManglelieEntity manglelie) {
        return manglelie.getCurrentAction() == Action.RIDE_MOMMY_HEAD && manglelie.getMommyGalath(false) == null;
    }

    boolean isManglelieLooking(ManglelieEntity manglelie) {
        GalathEntity galath = manglelie.getMommyGalath(false);
        if (galath == null) {
            return false;
        }
        if (galath.isDead) {
            manglelie.setMommyUUID(null);
            return false;
        }
        return galath.isHuggingManglelie();
    }

    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        if (entityIn instanceof ManglelieEntity) {
            ManglelieEntity manglelie = (ManglelieEntity) entityIn;
            if (!this.isManglelieLooking(manglelie) && !manglelie.isAttachedToMommy()) {
                super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
            }
        } else {
            super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
        }
    }

    static boolean isManglelieLooking(GirlEntity girl, float threshold) {
        if (!(girl instanceof ManglelieEntity)) {
            return false;
        }
        GalathEntity galath = ((ManglelieEntity)girl).getMommyGalath(false);
        return galath != null && galath.bodyScaleY < threshold;
    }

    public static void renderMangleliePov(GirlEntity girl, float partialTicks) {
        EntityPlayerSP player = mc.player;
        if (player != null) {
            if (!isManglelieLooking(girl, 0.5f)) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                GlStateManager.pushMatrix();
                if (girl.isLocallyRegistered()) {
                    GlStateManager.translate(0.0, 0.01, 0.0);
                } else {
                    GalathGeometryRender.setupRenderTranslations(mc, girl, partialTicks);
                    renderManglelieRibbon(girl, partialTicks);
                }
                mc.getTextureManager().bindTexture(LINE);
                GlStateManager.disableCull();
                GlStateManager.disableLighting();

                ManglelieRenderer.renderManglelieRibbonMesh(girl, buffer, tessellator, GirlRenderer.getInterpolatedYaw(girl, partialTicks));
                ManglelieRenderer.renderManglelieMesh(girl, buffer, tessellator);
                GlStateManager.popMatrix();
                GlStateManager.enableCull();
                GlStateManager.enableLighting();
            }
        }
    }

    static void renderManglelieRibbon(GirlEntity girl, float partialTicks) {
        if (girl instanceof ManglelieEntity) {
            ManglelieEntity manglelie = (ManglelieEntity) girl;
            if (manglelie.isAttachedToMommy()) {
                if (!ManglelieModel.isThreesomeAction(manglelie)) {
                    GalathEntity galath = manglelie.getMommyGalath(false);
                    if (galath != null) {
                        GlStateManager.rotate(-RotationHelper.LerpAngleDegrees(girl.prevRenderYawOffset, girl.renderYawOffset, partialTicks), 0.0f, 1.0f, 0.0f);
                    }
                }
            }
        }
    }

    static boolean isGalathLooking(GirlEntity girl) {
        if (girl instanceof GalathEntity) {
            girl = ((GalathEntity)girl).getManglelieUUID(false);
        }
        return girl != null && !Action.isAnyAction(girl, Action.THREESOME_SLOW, Action.THREESOME_FAST, Action.THREESOME_CUM);
    }

    static void renderManglelieMesh(GirlEntity girl, BufferBuilder buffer, Tessellator tessellator) {
        if (isGalathLooking(girl)) {
            buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i < 39; ++i) {
                ManglelieRenderer.renderManglelieStrip(girl, buffer, i, i + 1);
            }
            ManglelieRenderer.renderManglelieStrip(girl, buffer, 39, 0);
            tessellator.draw();
        }
    }

    static void renderManglelieStrip(GirlEntity girl, BufferBuilder buffer, int start, int end) {
        Vec3d so0 = girl.getCachedBoneOffset("skirt_" + start + "_0");
        Vec3d so1 = girl.getCachedBoneOffset("skirt_" + start + "_1");
        Vec3d so2 = girl.getCachedBoneOffset("skirt_" + start + "_2");
        Vec3d eo0 = girl.getCachedBoneOffset("skirt_" + end + "_0");
        Vec3d eo1 = girl.getCachedBoneOffset("skirt_" + end + "_1");
        Vec3d eo2 = girl.getCachedBoneOffset("skirt_" + end + "_2");
        ColorRGBA col = start % 2 == 0 ? CORRUPTION_COLOR_LIGHT : CORRUPTION_COLOR_DARK;
        buffer.pos(so0.x, so0.y, so0.z).color(col.r, col.g, col.b, col.a).endVertex();
        buffer.pos(so1.x, so1.y, so1.z).color(col.r, col.g, col.b, col.a).endVertex();
        buffer.pos(eo1.x, eo1.y, eo1.z).color(col.r, col.g, col.b, col.a).endVertex();
        buffer.pos(eo0.x, eo0.y, eo0.z).color(col.r, col.g, col.b, col.a).endVertex();
        buffer.pos(so1.x, so1.y, so1.z).color(col.r, col.g, col.b, col.a).endVertex();
        buffer.pos(eo1.x, eo1.y, eo1.z).color(col.r, col.g, col.b, col.a).endVertex();
        buffer.pos(eo2.x, eo2.y, eo2.z).color(col.r, col.g, col.b, col.a).endVertex();
        buffer.pos(so2.x, so2.y, so2.z).color(col.r, col.g, col.b, col.a).endVertex();
    }

    @Override
    protected void onBoneProcessing(BufferBuilder buffer, String boneName, GeoBone bone) {
        ManglelieRenderer.applyBoneTransform(this.renderEntity, boneName, bone, false);
        Entity corruptEntity = this.renderEntity.getTargetEntity();
        if (corruptEntity != null) {
            if ("weapon".equals(boneName) && this.renderEntity.checkRelativeHandPosition(corruptEntity, mc.getRenderPartialTicks())) {
                this.renderEquippedItem(buffer, bone, true);
            }
            if ("offhand".equals(boneName) && !this.renderEntity.checkRelativeHandPosition(corruptEntity, mc.getRenderPartialTicks())) {
                this.renderEquippedItem(buffer, bone, false);
            }
        }
    }

    void renderEquippedItem(BufferBuilder bufferBuilder, GeoBone geoBone, boolean isOffhand) {
        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        MatrixHelper.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, geoBone);
        GL11.glEnable(0xb50);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (isOffhand) {
            GlStateManager.translate(-0.01, 0.0, 0.0);
            GlStateManager.rotate(120.0f, 1.0f, 0.0f, 0.0f);
        } else {
            GlStateManager.translate(0.15, 0.0, -0.05);
            GlStateManager.rotate(-140.0f, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.scale(0.7, 0.7, 0.7);
        ItemStack bowStack = new ItemStack(Items.BOW);

        float progress = this.renderEntity.getAttackProgress(mc.getRenderPartialTicks());
        if (progress < 1.0f) {
            float f2 = (float) RotationHelper.EaseOutQuart(progress);
            this.renderEntity.setItemUseCount((int)(11.0f * (1.0f - f2) + 71980.0f));
            this.renderEntity.setHeldItemOverride(bowStack);
            this.renderEntity.setActiveHand(EnumHand.MAIN_HAND);
            this.renderEntity.setHandActiveState();
        } else {
            this.renderEntity.setHeldItemOverride(ItemStack.EMPTY);
            this.renderEntity.clearHandActiveState();
        }
        itemRenderer.renderItem(this.renderEntity, bowStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    public static void applyBoneTransform(GirlEntity girl, String boneName, GeoBone geoBone, boolean isSecondary) {
        float rotation;
        String selectedBone;
        if (boneName.contains("skirt_")) {
            int boneIndex = ManglelieRenderer.parseBoneIndex(boneName);
            if (ThreadNames.isValueInBounds(boneIndex, 17.0, 35.0)) {
                if (!mc.isGamePaused()) {
                    selectedBone = boneIndex < 26 ? "cheekL" : "cheekR";
                    if (isSecondary) {
                        selectedBone = selectedBone + "2";
                    }
                    if (!((rotation = TrigMath.toDegrees(girl.getAnimationProcessor().getBone(selectedBone).getRotationX())) < 0.0f)) {
                        geoBone.setPositionY(geoBone.getPositionY() + rotation * 0.01f);
                    }
                }
            }
            if (ThreadNames.isValueInBounds(boneIndex, 1.0, 11.0)) {
                if (boneName.endsWith("1")) {
                    selectedBone = boneIndex < 6 ? "legR" : "legL";
                    if (isSecondary) {
                        selectedBone = selectedBone + "2";
                    }
                    if (!((rotation = TrigMath.toDegrees(girl.getAnimationProcessor().getBone(selectedBone).getRotationX())) < 0.0f)) {
                        geoBone.setRotationX(TrigMath.wrapDegrees(rotation));
                        geoBone.setPositionY(TrigMath.wrapDegrees(rotation * 0.03f));
                    }
                }
            }
        }
    }

    static int parseBoneIndex(String boneName) {
        int firstSnake = boneName.indexOf('_');
        int secondSnake = boneName.indexOf('_', firstSnake + 1);
        if (firstSnake != -1 && secondSnake != -1) {
            String idxString = boneName.substring(firstSnake + 1, secondSnake);
            try {
                return Integer.parseInt(idxString);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    @Override
    protected void renderModelBuffer(GeoModel model, BufferBuilder buffer, ManglelieEntity entity, float r, float g, float b, float a, float partialTicks) {
        if (!ManglelieModel.isThreesomeAction(entity)) {
            super.renderModelBuffer(model, buffer, entity, r, g, b, a, partialTicks);
            return;
        }
        GeoBone topBone = model.topLevelBones.get(0);
        GeoBone body2Bone = null;
        GeoBone steveBone = null;
        for (GeoBone bone : topBone.childBones) {
            switch (bone.getName()) {
                case "steve": {
                    steveBone = bone;
                    break;
                }
                case "body2": {
                    body2Bone = bone;
                }
            }
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(topBone);
        MATRIX_STACK.moveToPivot(topBone);
        MATRIX_STACK.rotate(topBone);
        MATRIX_STACK.scale(topBone);
        MATRIX_STACK.moveBackFromPivot(topBone);
        this.renderRecursively(buffer, body2Bone, r, g, b, a);
        Tessellator.getInstance().draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getGoblinTexture(this.renderEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.renderRecursively(buffer, steveBone, r, g, b, (this.renderEntity).getRenderScaleFactor());
        Tessellator.getInstance().draw();
        MATRIX_STACK.pop();
    }

    static void renderManglelieRibbonMesh(GirlEntity girl, BufferBuilder buffer, Tessellator tessellator, float partialTicks) {
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        Vec3d[][] leftMesh = GalathGeometryRender.generateBoxMesh(girl, partialTicks, "clothBoobLconStart", "clothBoobLconEnd", OFFSET_BODY, OFFSET_ARM);
        Vec3d[][] rightMesh = GalathGeometryRender.generateBoxMesh(girl, partialTicks, "clothBoobRconStart", "clothBoobRconEnd", OFFSET_BODY, OFFSET_ARM);
        Vec3d[][] midMesh = GalathGeometryRender.generateBoxMesh(girl, partialTicks, "clothBoobMidconStart", "clothBoobMidconEnd", OFFSET_LEG, OFFSET_LEG);
        GalathGeometryRender.drawMesh(buffer, leftMesh, CORRUPTION_COLOR_MAIN);
        GalathGeometryRender.drawMesh(buffer, rightMesh, CORRUPTION_COLOR_MAIN);
        GalathGeometryRender.drawMesh(buffer, midMesh, CORRUPTION_COLOR_MAIN);
        tessellator.draw();
    }

    public boolean isBoneAllowed(HashSet hashSet, GeoBone bone) {
        while (bone.parent != null) {
            String string = bone.getName();
            if (string.contains("clothBoob")) {
                return true;
            }
            if (hashSet.contains(string) || string.startsWith("armor")) {
                return false;
            }
            bone = bone.parent;
        }
        return true;
    }

    @Override
    protected Vec3d applyCustomTranslationOffsets(ManglelieEntity entity, float partialTicks, Vec3d baseVector) {
        GalathEntity galath;
        if (entity.getCurrentAction() == Action.RUN) {
            float yaw;
            entity.rotationYaw = yaw = entity.getYawRotation();
            entity.prevRenderYawOffset = yaw;
            entity.renderYawOffset = yaw;
            entity.prevRotationYawHead = yaw;
            entity.rotationYawHead = yaw;
            return baseVector;
        }
        if (ManglelieRenderer.hasValidModel(entity) && (galath = entity.getMommyGalath(false)) != null) {
            ManglelieRenderer.setupModelPosition(galath, partialTicks, entity);
            return ManglelieRenderer.getMommyHeadOffset(galath, partialTicks);
        }
        return baseVector;
    }

    public static void setupModelPosition(GalathEntity galath, float partialTicks, EntityLivingBase entity) {
        boolean anchored = galath.isAnchored();
        float yaw = anchored ? galath.getYawRotation() : galath.rotationYawHead;
        float prewYaw = anchored ? galath.getYawRotation() : galath.prevRotationYawHead;
        Float aimYaw = GalathEntity.getAimYaw(galath, partialTicks);
        if (aimYaw != null) {
            yaw = aimYaw;
            prewYaw = aimYaw;
        }
        entity.rotationYaw = yaw;
        entity.prevRenderYawOffset = prewYaw;
        entity.renderYawOffset = yaw;
        entity.prevRotationYawHead = prewYaw;
        entity.rotationYawHead = yaw;
    }

    public static boolean hasValidModel(ManglelieEntity manglelie) {
        return manglelie.isAttachedToMommy() && !ManglelieModel.isThreesomeAction(manglelie);
    }

    public static Vec3d getMommyHeadOffset(GalathEntity galath, float partialTicks) {
        return EntityLookVectorHelper.getAimVector(galath, ManglelieRenderer.mc.player, partialTicks).add(galath.getCachedBoneOffset("mangPos"));
    }

    public static Vec3d getEntityLookVector(GalathEntity galath, float partialTicks) {
        return EntityLookVectorHelper.getInterpolatedPosition(galath, partialTicks).add(galath.getCachedBoneOffset("mangPos"));
    }

    // gay synthetics

    //public void doRender(GirlEntity em_class2582, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((ManglelieEntity)em_class2582, d, d2, d3, f, f2);
    //}

    //@Override
    //protected Vec3d a(GirlEntity em_class2582, float f, Vec3d vec3d) {
    //    return this.a((ManglelieEntity)em_class2582, f, vec3d);
    //}

    //@Override
    //protected void a(GeoModel geoModel, BufferBuilder bufferBuilder, GirlEntity em_class2582, float f, float f2, float f3, float f4, float f5) {
    //    this.a(geoModel, bufferBuilder, (ManglelieEntity)em_class2582, f, f2, f3, f4, f5);
    //}

    //@Override
    //public void doRender(EntityLivingBase entityLivingBase, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((ManglelieEntity)entityLivingBase, d, d2, d3, f, f2);
    //}

    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((ManglelieEntity)entity, d, d2, d3, f, f2);
    //}
}

