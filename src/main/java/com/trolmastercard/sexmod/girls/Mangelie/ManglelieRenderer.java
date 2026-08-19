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
import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.util.EntityLookVectorHelper;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.Galath.GalathEntity;
import com.trolmastercard.sexmod.girls.Galath.GalathGeometryRender;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlRenderer;
import com.trolmastercard.sexmod.util.*;
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
    final static ColorRGBA C = new ColorRGBA(115, 108, 188, 255);
    final static Vector3fSexmodSpecial D = new Vector3fSexmodSpecial(0.05f, 0.04f, 0.0f);
    final static Vector3fSexmodSpecial v = new Vector3fSexmodSpecial(0.0f, 0.065f, 0.0f);
    final static Vector3fSexmodSpecial z = new Vector3fSexmodSpecial(0.0f, 0.03f, 0.03f);
    final static ColorRGBA r = new ColorRGBA(63, 59, 150, 255);
    final static ColorRGBA x = new ColorRGBA(79, 74, 188, 255);
    final static float A = 0.5f;
    final static float w = 0.5f;
    final static int s = 40;
    final static float y = 0.01f;
    final static float t = 0.03f;
    final static public HashSet<String> B = new HashSet<String>(){
        {
            this.add("boobs2");
            this.add("booty2");
            this.add("vagina2");
            this.add("fuckhole2");
        }
    };
    boolean u = false;

    public ManglelieRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        if (!this.u) {
            B.addAll(BoneDeformProcessor.EXCLUDED_MESH_BONES);
            this.u = true;
        }
        return B;
    }

    @Override
    public void doRender(ManglelieEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (this.d_(entity)) {
            return;
        }
        if (this.a_0(entity)) {
            return;
        }
        if (ManglelieRenderer.c(entity, 0.5f)) {
            return;
        }
        if (this.c(entity)) {
            return;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        ManglelieRenderer.a_9(entity, partialTicks);
    }

    boolean c(ManglelieEntity f8_class2932) {
        GalathEntity f__class2972 = f8_class2932.getMommyGalath(false);
        if (f__class2972 == null) {
            return false;
        }
        switch (f__class2972.getCurrentAction()) {
            case CONTROLLED_FLIGHT: 
            case BOOST: {
                return true;
            }
        }
        return false;
    }

    // todo clashes
    boolean a_0(ManglelieEntity f8_class2932) {
        if (f8_class2932.getCurrentAction() != Action.RIDE_MOMMY_HEAD) {
            return false;
        }
        return f8_class2932.getMommyGalath(false) == null;
    }

    // TODO clashes
    boolean d_(ManglelieEntity f8_class2932) {
        GalathEntity f__class2972 = f8_class2932.getMommyGalath(false);
        if (f__class2972 == null) {
            return false;
        }
        if (f__class2972.isDead) {
            f8_class2932.setMommyUUID((UUID)null);
            return false;
        }
        return f__class2972.isHuggingManglelie();
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double d, double d2, double d3, float f, float f2) {
        if (!(entity instanceof ManglelieEntity)) {
            super.doRenderShadowAndFire(entity, d, d2, d3, f, f2);
            return;
        }
        ManglelieEntity manglelie = (ManglelieEntity)entity;
        if (this.d_(manglelie)) {
            return;
        }
        if (manglelie.isAttachedToMommy()) {
            return;
        }
        super.doRenderShadowAndFire(entity, d, d2, d3, f, f2);
    }

    static boolean c(GirlEntity em_class2582, float f) {
        if (!(em_class2582 instanceof ManglelieEntity)) {
            return false;
        }
        GalathEntity f__class2972 = ((ManglelieEntity)em_class2582).getMommyGalath(false);
        if (f__class2972 == null) {
            return false;
        }
        return f__class2972.bodyScaleY < f;
    }

    public static void a_9(GirlEntity em_class2582, float f) {
        EntityPlayerSP entityPlayerSP = ManglelieRenderer.mc.player;
        if (entityPlayerSP == null) {
            return;
        }
        if (ManglelieRenderer.c(em_class2582, 0.5f)) {
            return;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        if (em_class2582.isLocallyRegistered()) {
            GlStateManager.translate(0.0, 0.01, 0.0);
        } else {
            GalathGeometryRender.setupRenderTranslations(mc, em_class2582, f);
            ManglelieRenderer.b(em_class2582, f);
        }
        mc.getTextureManager().bindTexture(LINE);
        GlStateManager.disableCull();
        GlStateManager.disableLighting();

        ManglelieRenderer.a_6(em_class2582, bufferBuilder, tessellator, GirlRenderer.getInterpolatedYaw(em_class2582, f));
        ManglelieRenderer.a(em_class2582, bufferBuilder, tessellator);
        GlStateManager.popMatrix();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
    }

    static void b(GirlEntity em_class2582, float f) {
        if (!(em_class2582 instanceof ManglelieEntity)) {
            return;
        }
        ManglelieEntity f8_class2932 = (ManglelieEntity)em_class2582;
        if (!f8_class2932.isAttachedToMommy()) {
            return;
        }
        if (ManglelieModel.isThreesomeAction(f8_class2932)) {
            return;
        }
        GalathEntity f__class2972 = f8_class2932.getMommyGalath(false);
        if (f__class2972 == null) {
            return;
        }
        GlStateManager.rotate(-ReferenceAndRotationHelper.LerpAngleDegrees(em_class2582.prevRenderYawOffset, em_class2582.renderYawOffset, (double)f), 0.0f, 1.0f, 0.0f);
    }

    static boolean a_5(GirlEntity em_class2582) {
        if (em_class2582 instanceof GalathEntity) {
            em_class2582 = ((GalathEntity)em_class2582).getManglelieUUID(false);
        }
        if (em_class2582 == null) {
            return false;
        }
        return !Action.isAnyAction(em_class2582, Action.THREESOME_SLOW, Action.THREESOME_FAST, Action.THREESOME_CUM);
    }

    static void a(GirlEntity em_class2582, BufferBuilder bufferBuilder, Tessellator tessellator) {
        if (!ManglelieRenderer.a_5(em_class2582)) {
            return;
        }
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < 39; ++i) {
            ManglelieRenderer.a(em_class2582, bufferBuilder, i, i + 1);
        }
        ManglelieRenderer.a(em_class2582, bufferBuilder, 39, 0);
        tessellator.draw();
    }

    static void a(GirlEntity em_class2582, BufferBuilder bufferBuilder, int n, int n2) {
        Vec3d vec3d = em_class2582.getCachedBoneOffset("skirt_" + n + "_0");
        Vec3d vec3d2 = em_class2582.getCachedBoneOffset("skirt_" + n + "_1");
        Vec3d vec3d3 = em_class2582.getCachedBoneOffset("skirt_" + n + "_2");
        Vec3d vec3d4 = em_class2582.getCachedBoneOffset("skirt_" + n2 + "_0");
        Vec3d vec3d5 = em_class2582.getCachedBoneOffset("skirt_" + n2 + "_1");
        Vec3d vec3d6 = em_class2582.getCachedBoneOffset("skirt_" + n2 + "_2");
        ColorRGBA gv_class3882 = n % 2 == 0 ? x : r;
        bufferBuilder.pos(vec3d.x, vec3d.y, vec3d.z).color(gv_class3882.r, gv_class3882.g, gv_class3882.b, gv_class3882.a).endVertex();
        bufferBuilder.pos(vec3d2.x, vec3d2.y, vec3d2.z).color(gv_class3882.r, gv_class3882.g, gv_class3882.b, gv_class3882.a).endVertex();
        bufferBuilder.pos(vec3d5.x, vec3d5.y, vec3d5.z).color(gv_class3882.r, gv_class3882.g, gv_class3882.b, gv_class3882.a).endVertex();
        bufferBuilder.pos(vec3d4.x, vec3d4.y, vec3d4.z).color(gv_class3882.r, gv_class3882.g, gv_class3882.b, gv_class3882.a).endVertex();
        bufferBuilder.pos(vec3d2.x, vec3d2.y, vec3d2.z).color(gv_class3882.r, gv_class3882.g, gv_class3882.b, gv_class3882.a).endVertex();
        bufferBuilder.pos(vec3d5.x, vec3d5.y, vec3d5.z).color(gv_class3882.r, gv_class3882.g, gv_class3882.b, gv_class3882.a).endVertex();
        bufferBuilder.pos(vec3d6.x, vec3d6.y, vec3d6.z).color(gv_class3882.r, gv_class3882.g, gv_class3882.b, gv_class3882.a).endVertex();
        bufferBuilder.pos(vec3d3.x, vec3d3.y, vec3d3.z).color(gv_class3882.r, gv_class3882.g, gv_class3882.b, gv_class3882.a).endVertex();
    }

    @Override
    protected void onBoneProcessing(BufferBuilder buffer, String boneName, GeoBone bone) {
        ManglelieRenderer.a(this.renderEntity, boneName, bone, false);
        Entity entity = ((ManglelieEntity)this.renderEntity).getTargetEntity();
        if (entity == null) {
            return;
        }
        if ("weapon".equals(boneName) && ((ManglelieEntity)this.renderEntity).checkRelativeHandPosition(entity, mc.getRenderPartialTicks())) {
            this.a(buffer, bone, true);
        }
        if ("offhand".equals(boneName) && !((ManglelieEntity)this.renderEntity).checkRelativeHandPosition(entity, mc.getRenderPartialTicks())) {
            this.a(buffer, bone, false);
        }
    }

    void a(BufferBuilder bufferBuilder, GeoBone geoBone, boolean bl) {
        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        MatrixHelper.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, geoBone);
        GL11.glEnable(2896);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (bl) {
            GlStateManager.translate(-0.01, 0.0, 0.0);
            GlStateManager.rotate(120.0f, 1.0f, 0.0f, 0.0f);
        } else {
            GlStateManager.translate(0.15, 0.0, -0.05);
            GlStateManager.rotate(-140.0f, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.scale(0.7, 0.7, 0.7);
        ItemStack itemStack = new ItemStack(Items.BOW);
        float f = ((ManglelieEntity)this.renderEntity).getAttackProgress(mc.getRenderPartialTicks());
        if (f < 1.0f) {
            float f2 = (float) ReferenceAndRotationHelper.EaseOutQuart(f);
            ((ManglelieEntity)this.renderEntity).setItemUseCount((int)(11.0f * (1.0f - f2) + 71980.0f));
            ((ManglelieEntity)this.renderEntity).setHeldItemOverride(itemStack);
            ((ManglelieEntity)this.renderEntity).setActiveHand(EnumHand.MAIN_HAND);
            ((ManglelieEntity)this.renderEntity).W();
        } else {
            ((ManglelieEntity)this.renderEntity).setHeldItemOverride(ItemStack.EMPTY);
            ((ManglelieEntity)this.renderEntity).K();
        }
        itemRenderer.renderItem(this.renderEntity, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    public static void a(GirlEntity em_class2582, String string, GeoBone geoBone, boolean bl) {
        float f;
        String string2;
        if (!string.contains("skirt_")) {
            return;
        }
        int n = ManglelieRenderer.a(string);
        if (ThreadNames.isValueInBounds((double)n, 17.0, 35.0)) {
            if (mc.isGamePaused()) {
                return;
            }
            String string3 = string2 = n < 26 ? "cheekL" : "cheekR";
            if (bl) {
                string2 = string2 + "2";
            }
            if ((f = TrigMath.toDegrees(em_class2582.getAnimationProcessor().getBone(string2).getRotationX())) < 0.0f) {
                return;
            }
            geoBone.setPositionY(geoBone.getPositionY() + f * 0.01f);
        }
        if (ThreadNames.isValueInBounds((double)n, 1.0, 11.0)) {
            if (!string.endsWith("1")) {
                return;
            }
            String string4 = string2 = n < 6 ? "legR" : "legL";
            if (bl) {
                string2 = string2 + "2";
            }
            if ((f = TrigMath.toDegrees(em_class2582.getAnimationProcessor().getBone(string2).getRotationX())) < 0.0f) {
                return;
            }
            geoBone.setRotationX(TrigMath.wrapDegrees(f));
            geoBone.setPositionY(TrigMath.wrapDegrees(f * 0.03f));
        }
    }

    static int a(String string) {
        int n = string.indexOf('_');
        int n2 = string.indexOf('_', n + 1);
        if (n != -1 && n2 != -1) {
            String string2 = string.substring(n + 1, n2);
            try {
                return Integer.parseInt(string2);
            } catch (NumberFormatException numberFormatException) {
                return -1;
            }
        }
        return -1;
    }

    @Override
    protected void processModelSkeleton(GeoModel model, BufferBuilder buffer, ManglelieEntity entity, float r, float g, float b, float a, float partialTicks) {
        if (!ManglelieModel.isThreesomeAction(entity)) {
            super.processModelSkeleton(model, buffer, entity, r, g, b, a, partialTicks);
            return;
        }
        GeoBone geoBone = model.topLevelBones.get(0);
        GeoBone geoBone2 = null;
        GeoBone geoBone3 = null;
        for (GeoBone geoBone4 : geoBone.childBones) {
            switch (geoBone4.getName()) {
                case "steve": {
                    geoBone3 = geoBone4;
                    break;
                }
                case "body2": {
                    geoBone2 = geoBone4;
                }
            }
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        this.renderRecursively(buffer, geoBone2, r, g, b, a);
        Tessellator.getInstance().draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.getOrCreateDynamicSkin(this.renderEntity));
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.renderRecursively(buffer, geoBone3, r, g, b, (this.renderEntity).getRenderScaleFactor());
        Tessellator.getInstance().draw();
        MATRIX_STACK.pop();
    }

    static void a_6(GirlEntity em_class2582, BufferBuilder bufferBuilder, Tessellator tessellator, float f) {
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        Vec3d[][] vec3dArray = GalathGeometryRender.generateBoxMesh(em_class2582, f, "clothBoobLconStart", "clothBoobLconEnd", D, v);
        Vec3d[][] vec3dArray2 = GalathGeometryRender.generateBoxMesh(em_class2582, f, "clothBoobRconStart", "clothBoobRconEnd", D, v);
        Vec3d[][] vec3dArray3 = GalathGeometryRender.generateBoxMesh(em_class2582, f, "clothBoobMidconStart", "clothBoobMidconEnd", z, z);
        GalathGeometryRender.drawMesh(bufferBuilder, vec3dArray, C);
        GalathGeometryRender.drawMesh(bufferBuilder, vec3dArray2, C);
        GalathGeometryRender.drawMesh(bufferBuilder, vec3dArray3, C);
        tessellator.draw();
    }

    public boolean isBoneAllowed(HashSet hashSet, GeoBone bone) {
        while (bone.parent != null) {
            String string = bone.getName();
            if (string.contains("clothBoob")) {
                return true;
            }
            if (hashSet.contains(string)) {
                return false;
            }
            if (string.startsWith("armor")) {
                return false;
            }
            bone = bone.parent;
        }
        return true;
    }

    @Override
    protected Vec3d applyCustomTranslationOffsets(ManglelieEntity entity, float partialTicks, Vec3d baseVector) {
        GalathEntity f__class2972;
        if (entity.getCurrentAction() == Action.RUN) {
            float yaw;
            entity.rotationYaw = yaw = entity.getYawRotation();
            entity.prevRenderYawOffset = yaw;
            entity.renderYawOffset = yaw;
            entity.prevRotationYawHead = yaw;
            entity.rotationYawHead = yaw;
            return baseVector;
        }
        if (ManglelieRenderer.hasValidModel(entity) && (f__class2972 = entity.getMommyGalath(false)) != null) {
            ManglelieRenderer.setupModelPosition(f__class2972, partialTicks, entity);
            return ManglelieRenderer.getMommyHeadOffset(f__class2972, partialTicks);
        }
        return baseVector;
    }

    public static void setupModelPosition(GalathEntity f__class2972, float f, EntityLivingBase entityLivingBase) {
        boolean bl = f__class2972.isAnchored();
        float f2 = bl ? f__class2972.getYawRotation() : f__class2972.rotationYawHead;
        float f3 = bl ? f__class2972.getYawRotation() : f__class2972.prevRotationYawHead;
        Float f4 = GalathEntity.getAimYaw(f__class2972, f);
        if (f4 != null) {
            f2 = f4;
            f3 = f4;
        }
        entityLivingBase.rotationYaw = f2;
        entityLivingBase.prevRenderYawOffset = f3;
        entityLivingBase.renderYawOffset = f2;
        entityLivingBase.prevRotationYawHead = f3;
        entityLivingBase.rotationYawHead = f2;
    }

    public static boolean hasValidModel(ManglelieEntity f8_class2932) {
        return f8_class2932.isAttachedToMommy() && !ManglelieModel.isThreesomeAction(f8_class2932);
    }

    public static Vec3d getMommyHeadOffset(GalathEntity galath, float partialTicks) {
        return EntityLookVectorHelper.getAimVector(galath, ManglelieRenderer.mc.player, partialTicks).add(galath.getCachedBoneOffset("mangPos"));
    }

    public static Vec3d a(GalathEntity galath, float partialTicks) {
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

