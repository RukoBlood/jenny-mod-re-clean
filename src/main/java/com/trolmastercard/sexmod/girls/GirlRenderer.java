/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.vecmath.Matrix4f
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Tuple4f
 *  javax.vecmath.Vector3f
 *  javax.vecmath.Vector4f
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package com.trolmastercard.sexmod.girls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.vecmath.Matrix4f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Custom.CustomModelRenderer;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.GeckoMatrixBridge;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.Vector3f;
import com.trolmastercard.sexmod.util.VectorMath;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.geo.render.built.GeoQuad;
import software.bernie.geckolib3.geo.render.built.GeoVertex;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.renderers.geo.RenderHurtColor;
import software.bernie.geckolib3.util.MatrixStack;
import software.bernie.shadowed.eliotlash.mclib.utils.Interpolations;

//d__class169
// 'd_'
public abstract class GirlRenderer<T extends GirlEntity & IAnimatable>
extends GeoEntityRenderer<T>
implements c3_class112 {
    final static protected ResourceLocation LINE = new ResourceLocation("sexmod", "textures/line.png");
    final static float m = 1.5f;
    protected double c;
    protected T j;
    static protected Minecraft i;
    static protected HashMap<UUID, ResourceLocation> l;
    Color f = new Color(245, 199, 165);
    Color o = new Color(245, 157, 169);
    boolean h = false;
    protected HashSet<String> p = new HashSet();
    Integer k = null;
    Integer b = null;
    Integer d = null;
    float a = 0.0f;
    static public BufferBuilder n;
    Matrix4f g = null;
    protected GeoBone q = null;

    public GirlRenderer(RenderManager renderManager, AnimatedGeoModel<T> animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel);
        this.c = d;
        i = Minecraft.getMinecraft();
        this.shadowSize = 0.2f;
    }

    //d
    //@Override
    // getResourceLocation TODO is this supposed to override?
    protected ResourceLocation d(T t) throws IOException {
        ResourceLocation resourceLocation;
        if (((GirlEntity)t).world instanceof FakeWorld || ((GirlEntity)t).getID() == null) {
            resourceLocation = l.get(i.getSession().getProfile().getId());
            if (resourceLocation == null) {
                return this.a(i.getSession().getProfile().getId(), ((GirlEntity)t).world);
            }
        } else {
            resourceLocation = l.get(((GirlEntity)t).getID());
            if (resourceLocation == null) {
                return this.a(((GirlEntity)t).getID(), ((GirlEntity)t).world);
            }
        }
        return resourceLocation;
    }

    protected ResourceLocation a(UUID uUID, World world) throws IOException {
        BufferedImage skin;
        try {
            skin = PlayerSkin.GetPlayerSkin(uUID);
            Graphics graphics = skin.getGraphics();
            graphics.setColor(this.f);
            graphics.fillRect(0, 0, 4, 3);
            graphics.setColor(this.o);
            graphics.fillRect(4, 0, 3, 3);
        } catch (Exception exception) {
            if (!this.h) {
                this.h = true;
            }
            skin = ImageIO.read(i.getResourceManager().getResource(new ResourceLocation("sexmod", "textures/player/steve.png")).getInputStream());
        }
        l.put(uUID, this.renderManager.renderEngine.getDynamicTextureLocation("player" + uUID, new DynamicTexture(skin)));
        return l.get(uUID);
    }

    @CheckReturnValue
    public static float a(GirlEntity em_class2582, float f) {
        return em_class2582.boolean_Q() ? em_class2582.java_lang_Float_I().floatValue() : Reference.LerpFloat(em_class2582.prevRenderYawOffset, em_class2582.renderYawOffset, f);
    }

    protected void d() {
    }

    protected void void_b() {
    }

    float a(World world, Vec3d vec3d, float f, float f2) {
        RayTraceResult rayTraceResult = this.a(vec3d, vec3d.add(VectorMath.rotate(new Vec3d(0.0, 0.0, -4.0), f, f2)), world);
        if (rayTraceResult == null) {
            return 4.0f;
        }
        Vec3d vec3d2 = rayTraceResult.hitVec;
        if (vec3d2 == null) {
            return 4.0f;
        }
        return (float)vec3d.distanceTo(vec3d2);
    }

    boolean a(T t, EntityPlayer entityPlayer) {
        if (t instanceof PlayerGirl) {
            return true;
        }
        World world = ((GirlEntity)t).world;
        Vec3d vec3d = ((Entity)t).getPositionVector();
        float f = ((GirlEntity)t).width * 1.5f;
        float f2 = ((GirlEntity)t).height * 1.5f;
        Vec3d vec3d2 = entityPlayer.getPositionVector().add(0.0, entityPlayer.getEyeHeight(), 0.0);
        int n = GirlRenderer.i.gameSettings.thirdPersonView;
        if (n != 0) {
            return true;
        }
        if (n > 0) {
            float f3 = entityPlayer.rotationYaw;
            float f4 = entityPlayer.rotationPitch;
            if (n == 2) {
                f4 += 180.0f;
            }
            float f5 = 4.0f;
            Vec3d vec3d3 = vec3d2.add(MathHelper.sin(f3 * ((float)Math.PI / 180)) * MathHelper.cos(f4 * ((float)Math.PI / 180)) * f5, MathHelper.sin(f4 * ((float)Math.PI / 180)) * f5, -MathHelper.cos(f3 * ((float)Math.PI / 180)) * MathHelper.cos(f4 * ((float)Math.PI / 180)) * f5);
            BlockPos object = new BlockPos(vec3d3);
            boolean bl = world.isAirBlock(object);
            if (!bl) {
                vec3d2 = vec3d3;
            } else if (world.isAirBlock(object.add(0, 1, 0))) {
                vec3d2 = new Vec3d(vec3d3.x, object.getY() + 1, vec3d3.z);
            }
        }
        Vec3d[] vec3dArray = new Vec3d[]{vec3d.add(-f / 2.0f, 0.0, -f / 2.0f), vec3d.add(-f / 2.0f, 0.0, f / 2.0f), vec3d.add(f / 2.0f, 0.0, -f / 2.0f), vec3d.add(f / 2.0f, 0.0, f / 2.0f), vec3d.add(-f / 2.0f, f2, -f / 2.0f), vec3d.add(-f / 2.0f, f2, f / 2.0f), vec3d.add(f / 2.0f, f2, -f / 2.0f), vec3d.add(f / 2.0f, f2, f / 2.0f)};
        for (Vec3d vec3d3 : vec3dArray) {
            RayTraceResult rayTraceResult = this.a(vec3d2, vec3d3, world);
            if (rayTraceResult == null) {
                return true;
            }
            IBlockState iBlockState = world.getBlockState(rayTraceResult.getBlockPos());
            if (iBlockState.isTranslucent()) {
                return true;
            }
            if (iBlockState.getBlock().getRenderLayer() == BlockRenderLayer.SOLID) continue;
            return true;
        }
        return false;
    }

    HashSet<String> a(Boolean bl, boolean bl2) {
        if (ClientProxy.IS_PRELOADING) {
            return new HashSet<String>();
        }
        HashSet<String> hashSet = bl != false ? a_class4.b() : ((GirlEntity)this.j).Y();
        HashSet<String> hashSet2 = new HashSet<String>();
        for (String string : hashSet) {
            CustomModel.b_inner96 b_inner962 = CustomModel.b(string);
            if (b_inner962 == null || !b_inner962.a() && bl2) continue;
            hashSet2.addAll(b_inner962.h());
        }
        return hashSet2;
    }

    //a
    //render
    @Override
    public void render(GeoModel geoModel, T t, float f, float f2, float f3, float f4, float f5) {
        if (GirlRenderer.i.player != null && !((GirlEntity)t).boolean_h() && ((GirlEntity)t).boolean_d() && !this.a(t, GirlRenderer.i.player)) {
            return;
        }
        GlStateManager.enableRescaleNormal();
        this.renderEarly(t, f, f2, f3, f4, f5);
        this.renderLate(t, f, f2, f3, f4, f5);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
        this.p.clear();
        this.p = this.a(((GirlEntity)t).boolean_h(), ((GirlEntity)t).int_ah() == 0);
        this.d();
        gx_class390.a(((GirlEntity)t).b().getModelRendererList(), this.a(), this);
        gx_class390.a(t, f);
        this.a(geoModel, bufferBuilder, t, f2, f3, f4, f5, f);
        this.renderAfter(t, f, f2, f3, f4, f5);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GL20.glUseProgram(0);
    }

    // TODO
    //  does this override anything?
    //  it doesnt look like it...
    protected void a(GeoModel geoModel, BufferBuilder bufferBuilder, T t, float f, float f2, float f3, float f4, float f5) {
        GeoBone geoBone = null;
        for (GeoBone geoBone2 : geoModel.topLevelBones) {
            if (geoBone2.getName().equals("steve")) {
                geoBone = geoBone2;
                continue;
            }
            this.renderRecursively(bufferBuilder, geoBone2, f, f2, f3, f4);
        }
        Tessellator.getInstance().draw();
        this.void_b();
        if (geoBone != null) {
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            try {
                Minecraft.getMinecraft().renderEngine.bindTexture(this.d(this.j));
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
            this.renderRecursively(bufferBuilder, geoBone, f, f2, f3, ((GirlEntity)this.j).float_v());
            Tessellator.getInstance().draw();
        }
    }

    // TODO was this method ever referenced? It appears unused
    @CheckReturnValue
    String java_lang_String_a(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String string2;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(string));
            while ((string2 = bufferedReader.readLine()) != null) {
                stringBuilder.append(string2).append("//\n");
            }
            bufferedReader.close();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        return stringBuilder.toString();
    }

    protected void a(double d, double d2, double d3) {
        if (((GirlEntity)this.j).boolean_h()) {
            return;
        }
        if (((GirlEntity)this.j).currentAction().hideNameTag) {
            return;
        }
        if (GirlRenderer.i.getRenderManager().renderViewEntity == null) {
            return;
        }
        this.renderLivingLabel(this.j, ((GirlEntity)this.j).java_lang_String_ab(), d, d2 + (double)((GirlEntity)this.j).float_i(), d3, 300);
    }

    Vec3d a(EntityPlayer entityPlayer, float f) {
        EntityLiving entityLiving = (EntityLiving)entityPlayer.getRidingEntity();
        EntityPlayerSP entityPlayerSP = GirlRenderer.i.player;
        Vec3d vec3d = entityLiving.getLookVec();
        Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(entityPlayer.lastTickPosX, entityPlayer.lastTickPosY, entityPlayer.lastTickPosZ), entityPlayer.getPositionVector(), (double)f);
        Vec3d vec3d3 = Reference.LerpVec3d(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), (double)f);
        vec3d3 = vec3d2.subtract(vec3d3);
        ((GirlEntity)this.j).renderYawOffset = entityLiving.renderYawOffset;
        return new Vec3d(vec3d3.x + vec3d.x * -0.5, vec3d3.y + (double)0.15f, vec3d3.z + vec3d.z * -0.5);
    }

    protected Vec3d a(T t, float f, Vec3d vec3d) {
        return vec3d;
    }

    Vec3d a(T t, float f, double d, double d2, double d3) {
        float f2;
        EntityPlayer entityPlayer;
        Vec3d vec3d = new Vec3d(d, d2, d3);
        if (((GirlEntity)t).world instanceof FakeWorld) {
            return vec3d;
        }
        if (((GirlEntity)t).boolean_t() && (!(t instanceof PlayerGirl) || GirlRenderer.i.gameSettings.thirdPersonView != 0)) {
            this.a(d, d2, d3);
        }
        if ((entityPlayer = ((GirlEntity)t).net_minecraft_entity_player_EntityPlayer_z()) != null && entityPlayer.isRiding() && entityPlayer.getRidingEntity() instanceof EntityHorse && ((EntityHorse)entityPlayer.getRidingEntity()).isHorseSaddled()) {
            return this.a(entityPlayer, f);
        }
        if (!((GirlEntity)t).boolean_Q()) {
            return vec3d;
        }
        if (!(t instanceof PlayerGirl) || !((PlayerGirl)t).boolean_f() || GirlRenderer.i.gameSettings.thirdPersonView == 0) {
            Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(GirlRenderer.i.player.lastTickPosX, GirlRenderer.i.player.lastTickPosY, GirlRenderer.i.player.lastTickPosZ), GirlRenderer.i.player.getPositionVector(), (double)f);
            vec3d = ((GirlEntity)t).net_minecraft_util_math_Vec3d_o().subtract(vec3d2);
        }
        ((GirlEntity)t).rotationYaw = f2 = ((GirlEntity)t).java_lang_Float_I().floatValue();
        ((GirlEntity)t).prevRenderYawOffset = f2;
        ((GirlEntity)t).renderYawOffset = f2;
        ((GirlEntity)t).prevRotationYawHead = f2;
        ((GirlEntity)t).rotationYawHead = f2;
        return vec3d;
    }

    protected void b(T t) {
    }

    // TODO
    //a
    //doRender
    @Override
    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        float f3;
        this.j = t;
        Vec3d vec3d = this.a(t, f2, d, d2, d3);
        vec3d = this.a(t, f2, vec3d);
        d = vec3d.x;
        d2 = vec3d.y;
        d3 = vec3d.z;
        this.b(t);
        if (((EntityLiving)t).getLeashed()) {
            this.a((GirlEntity)t, d, d2 + this.c, d3, f2);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(d, d2, d3);
        GL11.glDisable(2896);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        boolean bl = ((Entity)t).getRidingEntity() != null && ((Entity)t).getRidingEntity().shouldRiderSit();
        EntityModelData entityModelData = new EntityModelData();
        entityModelData.isSitting = bl;
        entityModelData.isChild = ((EntityLivingBase)t).isChild();
        float f4 = Interpolations.lerpYaw(((GirlEntity)t).prevRenderYawOffset, ((GirlEntity)t).renderYawOffset, f2);
        float f5 = Interpolations.lerpYaw(((GirlEntity)t).prevRotationYawHead, ((GirlEntity)t).rotationYawHead, f2);
        float f6 = f5 - f4;
        if (bl && ((Entity)t).getRidingEntity() instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)((Entity)t).getRidingEntity();
            f4 = Interpolations.lerpYaw(entityLivingBase.prevRenderYawOffset, entityLivingBase.renderYawOffset, f2);
            f6 = f5 - f4;
            f3 = MathHelper.wrapDegrees(f6);
            if (f3 < -85.0f) {
                f3 = -85.0f;
            }
            if (f3 >= 85.0f) {
                f3 = 85.0f;
            }
            f4 = f5 - f3;
            if (f3 * f3 > 2500.0f) {
                f4 += f3 * 0.2f;
            }
            f6 = f5 - f4;
        }
        float f7 = Interpolations.lerp(((GirlEntity)t).prevRotationPitch, ((GirlEntity)t).rotationPitch, f2);
        f3 = this.handleRotationFloat(t, f2);
        //this.b(t, f3, f4, f2);
        this.applyRotations(t, f3, f4, f2);
        float f8 = 0.0f;
        float f9 = 0.0f;
        if (!bl && ((EntityLivingBase)t).isEntityAlive()) {
            f8 = Interpolations.lerp(((GirlEntity)t).prevLimbSwingAmount, ((GirlEntity)t).limbSwingAmount, f2);
            f9 = ((GirlEntity)t).limbSwing - ((GirlEntity)t).limbSwingAmount * (1.0f - f2);
            if (((EntityLivingBase)t).isChild()) {
                f9 *= 3.0f;
            }
            if (f8 > 1.0f) {
                f8 = 1.0f;
            }
        }
        entityModelData.headPitch = -f7;
        entityModelData.netHeadYaw = -f6;
        AnimationEvent<T> animationEvent = new AnimationEvent<T>(t, f9, f8, f2, !(f8 > -0.15f) || !(f8 < 0.15f), Collections.singletonList(entityModelData));
        GeoModelProvider geoModelProvider = super.getGeoModelProvider();
        ResourceLocation resourceLocation = geoModelProvider.getModelLocation(t);
        GeoModel geoModel = geoModelProvider.getModel(resourceLocation);
        if (geoModelProvider instanceof IAnimatableModel) {
            ((IAnimatableModel)((Object)geoModelProvider)).setLivingAnimations(t, ((Entity)t).getUniqueID().hashCode(), animationEvent);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.01f, 0.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(this.getEntityTexture(t));
        software.bernie.geckolib3.core.util.Color color = this.getRenderColor(t, f2);
        boolean bl2 = this.setDoRenderBrightness(t, f2);
        //this.a(geoModel, t, f2, (float)color.getRed() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getAlpha() / 255.0f);
        this.render(geoModel, t, f2, (float)color.getRed() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getAlpha() / 255.0f);
        if (bl2) {
            RenderHurtColor.unset();
        }
        for (GeoLayerRenderer geoLayerRenderer : this.layerRenderers) {
            geoLayerRenderer.render(t, f9, f8, f2, f9, f6, f7, color);
        }
        GL11.glEnable(2896);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        this.a(t);
        CustomModelRenderer.a(t, f2);
        Vector3f f7_class2922 = this.e(t);
        if (f7_class2922 != null) {
            this.a((GirlEntity)t, f2, f7_class2922);
        }
    }

    void a(T t) {
        ArrayList<String> arrayList = new ArrayList<String>(GirlModel.e);
        arrayList.addAll(((GirlEntity)t).p);
        for (String string : arrayList) {
            MatrixStack matrixStack = ((GirlEntity)t).a(string, !((GirlEntity)t).boolean_h());
            Matrix4f matrix4f = matrixStack.getModelMatrix();
            Vec3d vec3d = new Vec3d(-matrix4f.m03, matrix4f.m13, -matrix4f.m23);
            ((GirlEntity)t).a(string, vec3d);
        }
    }

    @Nullable
    protected Vector3f e(T t) {
        return null;
    }

    public Entity c(GirlEntity em_class2582) {
        return em_class2582;
    }

    void a(GirlEntity em_class2582, float f, Vector3f f7_class2922) {
        EntityPlayerSP entityPlayerSP = GirlRenderer.i.player;
        f7_class2922 = new Vector3f(f7_class2922.x / 255.0f, f7_class2922.y / 255.0f, f7_class2922.z / 255.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 0.01, 0.0);
        Entity entity = this.c(em_class2582);
        Vec3d vec3d = em_class2582.boolean_Q() ? em_class2582.net_minecraft_util_math_Vec3d_o() : Reference.LerpVec3d(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), (double)f);
        Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), (double)f);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        GlStateManager.translate(vec3d3.x, vec3d3.y, vec3d3.z);
        i.getTextureManager().bindTexture(LINE);
        float f2 = GirlRenderer.a(em_class2582, f, 1.0f, 5.0f);
        this.b(tessellator, bufferBuilder, em_class2582, f7_class2922, f2);
        GlStateManager.popMatrix();
    }

    protected static float a(GirlEntity em_class2582, float f, float f2, float f3) {
        EntityPlayerSP entityPlayerSP = GirlRenderer.i.player;
        Entity entity = ((GirlRenderer)i.getRenderManager().getEntityRenderObject(em_class2582)).c(em_class2582);
        Vec3d vec3d = em_class2582.boolean_Q() ? em_class2582.net_minecraft_util_math_Vec3d_o() : Reference.LerpVec3d(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), entity.getPositionVector(), (double)f);
        Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), (double)f);
        Vec3d vec3d3 = ActiveRenderInfo.getCameraPosition().add(vec3d2);
        float f4 = (float)vec3d3.distanceTo(vec3d);
        float f5 = Math.abs(f4) / 5.0f;
        return Reference.LerpFloat(f3, f2, be_class78.b(f5, 0.0f, 1.0f));
    }

    protected void b(Tessellator tessellator, BufferBuilder bufferBuilder, GirlEntity em_class2582, Vector3f f7_class2922, float f) {
    }

    protected static void a(BufferBuilder bufferBuilder, Tessellator tessellator, GirlEntity em_class2582, String string, String string2, float f, float f2, float f3, float f4) {
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.glLineWidth(f4);
        Vec3d vec3d = em_class2582.b(string);
        Vec3d vec3d2 = em_class2582.b(string2);
        bufferBuilder.pos(vec3d.x, vec3d.y, vec3d.z).tex(0.0, 0.0).color(f, f2, f3, 1.0f).endVertex();
        bufferBuilder.pos(vec3d2.x, vec3d2.y, vec3d2.z).tex(0.0, 0.0).color(f, f2, f3, 1.0f).endVertex();
        tessellator.draw();
    }

    protected static void a(Tessellator tessellator, BufferBuilder bufferBuilder, GirlEntity em_class2582, Vector3f f7_class2922, float f) {
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidStartR", "braStringMidMid1R", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidMid1R", "braStringMidMid2R", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidMid2R", "braStringMidMid3R", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidMid3R", "braStringMidEndR", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidEndR", "braStringBackR", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringBackR", "braStringRightEndR", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringRightEndR", "braStringRightStartR", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringRightR", "braStringRightL", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidStartL", "braStringMidMid1L", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidMid1L", "braStringMidMid2L", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidMid2L", "braStringMidMid3L", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidMid3L", "braStringMidEndL", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringMidEndL", "braStringBackL", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringBackL", "braStringLeftEndL", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
        GirlRenderer.a(bufferBuilder, tessellator, em_class2582, "braStringLeftEndL", "braStringLeftStartL", f7_class2922.x, f7_class2922.y, f7_class2922.z, f);
    }

    // TODO
    //b
    //protected void b(T t, float f, float f2, float f3) {
    // applyRotations
    @Override
    protected void applyRotations(T t, float f, float f2, float f3) {
        super.applyRotations(t, f, f2, f3);
        if (!(t instanceof PlayerGirl)) {
            return;
        }
        UUID uUID = ((PlayerGirl)t).java_util_UUID_m();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = ((GirlEntity)t).world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        if (!entityPlayer.isElytraFlying()) {
            return;
        }
        float f4 = (float)entityPlayer.getTicksElytraFlying() + f3;
        float f5 = MathHelper.clamp(f4 * f4 / 100.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f5 * (-90.0f - entityPlayer.rotationPitch), 1.0f, 0.0f, 0.0f);
        Vec3d vec3d = entityPlayer.getLook(f3);
        double d = entityPlayer.motionX * entityPlayer.motionX + entityPlayer.motionZ * entityPlayer.motionZ;
        double d2 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;
        if (d > 0.0 && d2 > 0.0) {
            double d3 = (entityPlayer.motionX * vec3d.x + entityPlayer.motionZ * vec3d.z) / (Math.sqrt(d) * Math.sqrt(d2));
            double d4 = entityPlayer.motionX * vec3d.z - entityPlayer.motionZ * vec3d.x;
            GlStateManager.rotate((float)(Math.signum(d4) * Math.acos(d3)) * 180.0f / (float)Math.PI, 0.0f, 1.0f, 0.0f);
        }
    }

    protected void a(BufferBuilder bufferBuilder, String string, GeoBone geoBone) {
    }

    protected void a(GirlEntity em_class2582, double d, double d2, double d3, float f) {
        float f2;
        float f3;
        float f4;
        float f5;
        int n;
        Entity entity = em_class2582.getLeashHolder();
        d2 -= (1.6 - (double)em_class2582.height) * 0.5;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        double d4 = (double) Reference.LerpFloat(entity.prevRotationYaw, entity.rotationYaw, f * 0.5f) * 0.01745329238474369;
        double d5 = (double) Reference.LerpFloat(entity.prevRotationPitch, entity.rotationPitch, f * 0.5f) * 0.01745329238474369;
        double d6 = Math.cos(d4);
        double d7 = Math.sin(d4);
        double d8 = Math.sin(d5);
        if (entity instanceof EntityHanging) {
            d6 = 0.0;
            d7 = 0.0;
            d8 = -1.0;
        }
        double d9 = Math.cos(d5);
        double d10 = Reference.LerpDouble(entity.prevPosX, entity.posX, (double)f) - d6 * 0.7 - d7 * 0.5 * d9;
        double d11 = Reference.LerpDouble(entity.prevPosY + (double)entity.getEyeHeight() * 0.7, entity.posY + (double)entity.getEyeHeight() * 0.7, (double)f) - d8 * 0.5 - 0.25;
        double d12 = Reference.LerpDouble(entity.prevPosZ, entity.posZ, (double)f) - d7 * 0.7 + d6 * 0.5 * d9;
        double d13 = (double) Reference.LerpFloat(em_class2582.prevRenderYawOffset, em_class2582.renderYawOffset, f) * 0.01745329238474369 + 1.5707963267948966;
        d6 = Math.cos(d13) * (double)em_class2582.width * 0.4;
        d7 = Math.sin(d13) * (double)em_class2582.width * 0.4;
        double d14 = Reference.LerpDouble(em_class2582.prevPosX, em_class2582.posX, (double)f) + d6;
        double d15 = Reference.LerpDouble(em_class2582.prevPosY, em_class2582.posY, (double)f);
        double d16 = Reference.LerpDouble(em_class2582.prevPosZ, em_class2582.posZ, (double)f) + d7;
        d += d6;
        d3 += d7;
        double d17 = (float)(d10 - d14);
        double d18 = (float)(d11 - d15);
        double d19 = (float)(d12 - d16);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        bufferBuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        for (n = 0; n <= 24; ++n) {
            f5 = 0.5f;
            f4 = 0.4f;
            f3 = 0.3f;
            if (n % 2 == 0) {
                f5 *= 0.7f;
                f4 *= 0.7f;
                f3 *= 0.7f;
            }
            f2 = (float)n / 24.0f;
            bufferBuilder.pos(d + d17 * (double)f2 + 0.0, d2 + d18 * (double)(f2 * f2 + f2) * 0.5 + (double)((24.0f - (float)n) / 18.0f + 0.125f), d3 + d19 * (double)f2).color(f5, f4, f3, 1.0f).endVertex();
            bufferBuilder.pos(d + d17 * (double)f2 + 0.025, d2 + d18 * (double)(f2 * f2 + f2) * 0.5 + (double)((24.0f - (float)n) / 18.0f + 0.125f) + 0.025, d3 + d19 * (double)f2).color(f5, f4, f3, 1.0f).endVertex();
        }
        tessellator.draw();
        bufferBuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        for (n = 0; n <= 24; ++n) {
            f5 = 0.5f;
            f4 = 0.4f;
            f3 = 0.3f;
            if (n % 2 == 0) {
                f5 *= 0.7f;
                f4 *= 0.7f;
                f3 *= 0.7f;
            }
            f2 = (float)n / 24.0f;
            bufferBuilder.pos(d + d17 * (double)f2 + 0.0, d2 + d18 * (double)(f2 * f2 + f2) * 0.5 + (double)((24.0f - (float)n) / 18.0f + 0.125f) + 0.025, d3 + d19 * (double)f2).color(f5, f4, f3, 1.0f).endVertex();
            bufferBuilder.pos(d + d17 * (double)f2 + 0.025, d2 + d18 * (double)(f2 * f2 + f2) * 0.5 + (double)((24.0f - (float)n) / 18.0f + 0.125f), d3 + d19 * (double)f2 + 0.025).color(f5, f4, f3, 1.0f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4) {
        if (((GirlEntity)this.j).world instanceof FakeWorld) {
            return;
        }
        String string = geoBone.getName();
        if (string.equals("weapon") && this.j instanceof Fighter) {
            this.a(bufferBuilder, geoBone);
        }
        if (string.equals("itemRenderer") && ((GirlEntity)this.j).currentAction() == Action.PAYMENT) {
            this.b(bufferBuilder, geoBone);
        }
        if (string.equals("ballL") || string.equals("ballR") || string.equals("cock")) {
            f4 = 1.0f;
        }
        n = bufferBuilder;
        this.a(bufferBuilder, string, geoBone);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        if ("Head2".equals(string) && !this.boolean_c()) {
            MATRIX_STACK.pop();
            return;
        }
        if (!this.b(string)) {
            MATRIX_STACK.pop();
            return;
        }
        if (!geoBone.isHidden) {
            Vector4f vector4f = this.a(string, f, f2, f3);
            f = vector4f.x;
            f2 = vector4f.y;
            f3 = vector4f.z;
            double d = vector4f.w;
            if (!this.p.contains(string)) {
                for (GeoCube object : geoBone.childCubes) {
                    MATRIX_STACK.push();
                    this.q = geoBone;
                    this.a(bufferBuilder, object, f, f2, f3, f4, d);
                    MATRIX_STACK.pop();
                }
            }
            for (GeoBone geoBone2 : geoBone.childBones) {
                if (d == 0.0) {
                    this.renderRecursively(bufferBuilder, geoBone2, f, f2, f3, f4);
                    continue;
                }
                this.a(bufferBuilder, geoBone2, f, f2, f3, f4, d);
            }
        }
        try {
            MATRIX_STACK.pop();
        } catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
    }

    protected Vector4f a(float f, float f2, float f3) {
        return new Vector4f(f, f2, f3, 0.0f);
    }

    boolean b(String string) {
        if (!string.startsWith("armor")) {
            return true;
        }
        return this.j instanceof Fighter;
    }

    protected Vector4f a(String string, float f, float f2, float f3) {
        if (!string.startsWith("armor")) {
            return this.a(f, f2, f3);
        }
        if (!(this.j instanceof Fighter)) {
            return this.a(f, f2, f3);
        }
        if (((GirlEntity)this.j).entityDataManager.get(GirlEntity.D) == 0) {
            return this.a(f, f2, f3);
        }
        GeoModelProvider geoModelProvider = this.getGeoModelProvider();
        if (!(geoModelProvider instanceof GirlModel)) {
            return this.a(f, f2, f3);
        }
        GirlModel cv_class1492 = (GirlModel)geoModelProvider;
        ItemStack itemStack = cv_class1492.a((GirlEntity)this.j, string);
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return this.a(f, f2, f3);
        }
        ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
        ItemArmor.ArmorMaterial armorMaterial = itemArmor.getArmorMaterial();
        float f4 = 0.0f;
        switch (armorMaterial) {
            case GOLD: {
                f4 = 1.0f;
                break;
            }
            case CHAIN: 
            case IRON: {
                f4 = 2.0f;
                break;
            }
            case LEATHER: {
                f4 = 4.0f;
                int n = itemArmor.getColor(itemStack);
                float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
                float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
                float f7 = (float)(n & 0xFF) / 255.0f;
                f *= f5;
                f2 *= f6;
                f3 *= f7;
            }
        }
        return new Vector4f(f, f2, f3, 72.0f * f4 / 4096.0f);
    }

    //TODO
    //a
    //renderEarly
    @Override
    public void renderEarly(T t, float f, float f2, float f3, float f4, float f5) {
        this.g = (Matrix4f)MATRIX_STACK.getModelMatrix().clone();
    }

    public void a(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4, double d) {
        if (((GirlEntity)this.j).world instanceof FakeWorld) {
            return;
        }
        String string = geoBone.getName();
        if (string.equals("weapon")) {
            this.a(bufferBuilder, geoBone);
        }
        if (string.equals("ballL") || string.equals("ballR") || string.equals("cock")) {
            f4 = 1.0f;
        }
        this.a(bufferBuilder, geoBone.getName(), geoBone);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        if (!geoBone.isHidden) {
            if (!this.p.contains(string)) {
                for (GeoCube object : geoBone.childCubes) {
                    MATRIX_STACK.push();
                    GlStateManager.pushMatrix();
                    this.q = geoBone;
                    this.a(bufferBuilder, object, f, f2, f3, f4, d);
                    GlStateManager.popMatrix();
                    MATRIX_STACK.pop();
                }
            }
            for (GeoBone geoBone2 : geoBone.childBones) {
                this.a(bufferBuilder, geoBone2, f, f2, f3, f4, d);
            }
        }
        MATRIX_STACK.pop();
    }

    @CheckReturnValue
    protected boolean boolean_c() {
        if (!((GirlEntity)this.j).boolean_n()) {
            return true;
        }
        return GirlRenderer.i.gameSettings.thirdPersonView != 0;
    }

    public void a(BufferBuilder bufferBuilder, GeoCube geoCube, float f, float f2, float f3, float f4, double d) {
        MATRIX_STACK.moveToPivot(geoCube);
        MATRIX_STACK.rotate(geoCube);
        MATRIX_STACK.moveBackFromPivot(geoCube);
        for (GeoQuad geoQuad : geoCube.quads) {
            if (geoQuad == null) continue;
            javax.vecmath.Vector3f vector3f = new javax.vecmath.Vector3f((float)geoQuad.normal.getX(), (float)geoQuad.normal.getY(), (float)geoQuad.normal.getZ());
            MATRIX_STACK.getNormalMatrix().transform((Tuple3f)vector3f);
            if ((geoCube.size.y == 0.0f || geoCube.size.z == 0.0f) && vector3f.getX() < 0.0f) {
                vector3f.x *= -1.0f;
            }
            if ((geoCube.size.x == 0.0f || geoCube.size.z == 0.0f) && vector3f.getY() < 0.0f) {
                vector3f.y *= -1.0f;
            }
            if ((geoCube.size.x == 0.0f || geoCube.size.y == 0.0f) && vector3f.getZ() < 0.0f) {
                vector3f.z *= -1.0f;
            }
            Vec3d vec3d = gx_class390.a(this, this.q, new Vec3d(f, f2, f3), vector3f);
            for (GeoVertex geoVertex : geoQuad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                MATRIX_STACK.getModelMatrix().transform((Tuple4f)vector4f);
                bufferBuilder.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex((double)geoVertex.textureU + d, geoVertex.textureV).color((float)vec3d.x, (float)vec3d.y, (float)vec3d.z, f4).normal(vector3f.getX(), vector3f.getY(), vector3f.getZ()).endVertex();
            }
        }
    }

    @CheckReturnValue
    protected ItemStack net_minecraft_item_ItemStack_a() {
        switch (((GirlEntity)this.j).entityDataManager.get(GirlEntity.h)) {
            case "doggy": {
                return new ItemStack(Items.DIAMOND, 2);
            }
            case "blowjob": {
                return new ItemStack(Items.EMERALD, 3);
            }
            case "strip": {
                return new ItemStack(Items.GOLD_INGOT, 1);
            }
            case "boobjob": {
                return new ItemStack(Items.ENDER_PEARL, 2);
            }
            case "touch_boobs": {
                return new ItemStack(Items.FISH, 2, 1);
            }
            case "sex": {
                return new ItemStack(Items.FISH, 3, 0);
            }
        }
        return null;
    }

    protected void b(BufferBuilder bufferBuilder, GeoBone geoBone) {
        ItemStack itemStack = this.net_minecraft_item_ItemStack_a();
        if (itemStack == null) {
            return;
        }
        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();
        for (int i = 0; i < itemStack.getCount(); ++i) {
            GlStateManager.pushMatrix();
            Tessellator.getInstance().draw();
            GeckoMatrixBridge.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, geoBone);
            GL11.glEnable(2896);
            GL11.glRotated((double)((double)geoBone.getRotationX() + 2.5), 0.0, 0.0, 1.0);
            GL11.glRotated((double)geoBone.getRotationY(), 0.0, 1.0, 0.0);
            GL11.glRotated((double)geoBone.getRotationZ(), 1.0, 0.0, 0.0);
            switch (i) {
                case 1: {
                    GL11.glRotated(-15.0, 0.0, 0.0, 1.0);
                    GlStateManager.translate(0.0, 0.0, -0.025);
                    break;
                }
                case 2: {
                    GL11.glRotated(15.0, 0.0, 0.0, 1.0);
                    GlStateManager.translate(0.0, 0.0, 0.025);
                }
            }
            GlStateManager.scale(((GirlEntity)this.j).n, ((GirlEntity)this.j).n, ((GirlEntity)this.j).n);
            itemRenderer.renderItem((EntityLivingBase)this.j, new ItemStack(itemStack.getItem(), 1), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            GL11.glDisable(2896);
            GlStateManager.popMatrix();
        }
    }

    @CheckReturnValue
    protected ItemStack net_minecraft_item_ItemStack_a(@Nullable ItemStack itemStack) {
        return itemStack;
    }

    protected void a(BufferBuilder bufferBuilder, GeoBone geoBone) {
        if (this.j == null) {
            return;
        }
        if (!(this.j instanceof Fighter)) {
            return;
        }
        EntityDataManager entityDataManager = ((Entity)this.j).getDataManager();
        Fighter e2_class2182 = (Fighter)this.j;
        int n = entityDataManager.get(Fighter.M);
        if (e2_class2182.currentAction() != Action.BOW) {
            this.a = 0.0f;
        }
        ItemStack itemStack = null;
        if (n == 1) {
            itemStack = entityDataManager.get(Fighter.ITEM_SLOT_1);
        } else if (n == 2) {
            itemStack = entityDataManager.get(Fighter.ITEM_SLOT_2);
        }
        itemStack = this.net_minecraft_item_ItemStack_a(itemStack);
        if (itemStack == null) {
            return;
        }
        if (itemStack.getItem().equals(Items.BOW) && e2_class2182.currentAction() == Action.BOW) {
            this.a += 0.015f;
            e2_class2182.d(Math.round(-this.a * 20.0f + (float)itemStack.getMaxItemUseDuration()));
            e2_class2182.void_a(itemStack);
        }
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        GeckoMatrixBridge.bindOpenGLToBone(MATRIX_STACK, geoBone);
        GL11.glEnable(2896);
        if (itemStack.getItem() instanceof ItemBow) {
            GL11.glRotatef((float)e2_class2182.K, 1.0f, 0.0f, 0.0f);
        } else if (e2_class2182.currentAction() == Action.ATTACK && e2_class2182.S == 0) {
            GlStateManager.translate(e2_class2182.V.x, e2_class2182.V.y, e2_class2182.V.z);
            GL11.glRotatef((float)e2_class2182.O, 1.0f, 0.0f, 0.0f);
        } else {
            GL11.glRotatef((float)e2_class2182.P, 1.0f, 0.0f, 0.0f);
        }
        Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)this.j, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
    }

    RayTraceResult a(Vec3d vec3d, Vec3d vec3d2, World world) {
        int n;
        int n2;
        if (Double.isNaN(vec3d.x) || Double.isNaN(vec3d.y) || Double.isNaN(vec3d.z)) {
            return null;
        }
        if (Double.isNaN(vec3d2.x) || Double.isNaN(vec3d2.y) || Double.isNaN(vec3d2.z)) {
            return null;
        }
        int n3 = MathHelper.floor(vec3d2.x);
        int n4 = MathHelper.floor(vec3d2.y);
        int n5 = MathHelper.floor(vec3d2.z);
        int n6 = MathHelper.floor(vec3d.x);
        BlockPos blockPos = new BlockPos(n6, n2 = MathHelper.floor(vec3d.y), n = MathHelper.floor(vec3d.z));
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (iBlockState.getCollisionBoundingBox(world, blockPos) != Block.NULL_AABB && iBlockState.getBlock().getRenderLayer() == BlockRenderLayer.SOLID) {
            return iBlockState.collisionRayTrace(world, blockPos, vec3d, vec3d2);
        }
        int n7 = 200;
        while (n7-- >= 0) {
            IBlockState iBlockState2;
            EnumFacing enumFacing;
            if (Double.isNaN(vec3d.x) || Double.isNaN(vec3d.y) || Double.isNaN(vec3d.z)) {
                return null;
            }
            if (n6 == n3 && n2 == n4 && n == n5) {
                return null;
            }
            boolean bl = true;
            boolean bl2 = true;
            boolean bl3 = true;
            double d = 999.0;
            double d2 = 999.0;
            double d3 = 999.0;
            if (n3 > n6) {
                d = (double)n6 + 1.0;
            } else if (n3 < n6) {
                d = (double)n6 + 0.0;
            } else {
                bl = false;
            }
            if (n4 > n2) {
                d2 = (double)n2 + 1.0;
            } else if (n4 < n2) {
                d2 = (double)n2 + 0.0;
            } else {
                bl2 = false;
            }
            if (n5 > n) {
                d3 = (double)n + 1.0;
            } else if (n5 < n) {
                d3 = (double)n + 0.0;
            } else {
                bl3 = false;
            }
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = 999.0;
            double d7 = vec3d2.x - vec3d.x;
            double d8 = vec3d2.y - vec3d.y;
            double d9 = vec3d2.z - vec3d.z;
            if (bl) {
                d4 = (d - vec3d.x) / d7;
            }
            if (bl2) {
                d5 = (d2 - vec3d.y) / d8;
            }
            if (bl3) {
                d6 = (d3 - vec3d.z) / d9;
            }
            if (d4 == -0.0) {
                d4 = -1.0E-4;
            }
            if (d5 == -0.0) {
                d5 = -1.0E-4;
            }
            if (d6 == -0.0) {
                d6 = -1.0E-4;
            }
            if (d4 < d5 && d4 < d6) {
                enumFacing = n3 > n6 ? EnumFacing.WEST : EnumFacing.EAST;
                vec3d = new Vec3d(d, vec3d.y + d8 * d4, vec3d.z + d9 * d4);
            } else if (d5 < d6) {
                enumFacing = n4 > n2 ? EnumFacing.DOWN : EnumFacing.UP;
                vec3d = new Vec3d(vec3d.x + d7 * d5, d2, vec3d.z + d9 * d5);
            } else {
                enumFacing = n5 > n ? EnumFacing.NORTH : EnumFacing.SOUTH;
                vec3d = new Vec3d(vec3d.x + d7 * d6, vec3d.y + d8 * d6, d3);
            }
            if ((iBlockState2 = world.getBlockState(blockPos = new BlockPos(n6 = MathHelper.floor(vec3d.x) - (enumFacing == EnumFacing.EAST ? 1 : 0), n2 = MathHelper.floor(vec3d.y) - (enumFacing == EnumFacing.UP ? 1 : 0), n = MathHelper.floor(vec3d.z) - (enumFacing == EnumFacing.SOUTH ? 1 : 0)))).getMaterial() != Material.PORTAL && iBlockState2.getCollisionBoundingBox(world, blockPos) == Block.NULL_AABB || iBlockState2.getBlock().getRenderLayer() != BlockRenderLayer.SOLID) continue;
            return iBlockState2.collisionRayTrace(world, blockPos, vec3d, vec3d2);
        }
        return null;
    }

    // super gay synthetics
    //  wuv u tmc!

    //@Override
    //protected void applyRotations(EntityLivingBase entityLivingBase, float f, float f2, float f3) {
    //    this.b((GirlEntity)entityLivingBase, f, f2, f3);
    //}

    //@Override
    //public void doRender(EntityLivingBase entityLivingBase, double d, double d2, double d3, float f, float f2) {
    //    this.a((GirlEntity)entityLivingBase, d, d2, d3, f, f2);
    //}

    //@Override
    //public void renderEarly(Object object, float f, float f2, float f3, float f4, float f5) {
    //    this.a((T)((GirlEntity)object), f, f2, f3, f4, f5);
    //}

    //@Override
    //public void render(GeoModel geoModel, Object object, float f, float f2, float f3, float f4, float f5) {
    //    this.a(geoModel, (GirlEntity)object, f, f2, f3, f4, f5);
    //}

    //@Override
    //public ResourceLocation getEntityTexture(Entity entity) {
    //    return super.getEntityTexture((GirlEntity)entity);
    //}

    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.a((GirlEntity)entity, d, d2, d3, f, f2);
    //}

    static {
        l = new HashMap();
    }
}

