/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Tuple4f
 *  javax.vecmath.Vector3f
 *  javax.vecmath.Vector4f
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod.girls.Custom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.UploadModelString;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.geo.render.built.GeoQuad;
import software.bernie.geckolib3.geo.render.built.GeoVertex;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.MatrixStack;

public class CustomModelRenderer
extends GeoEntityRenderer<CustomModelEntity> {
    final static public float e = 1.876945f;
    final static public float i = 2.876945f;
    Minecraft a;
    CustomModelEntity c = null;
    CustomModel.b_inner96 b = null;
    HashMap<String, String> h = new HashMap();
    HashMap<String, String> f = new HashMap();
    HashMap<String, gt_class385> g = new HashMap();
    static public boolean k = false;
    Vec3d d = new Vec3d(1.0, 1.0, 1.0);
    Vec3d j;

    public CustomModelRenderer(RenderManager renderManager, AnimatedGeoModel<CustomModelEntity> animatedGeoModel) {
        super(renderManager, animatedGeoModel);
        this.a = Minecraft.getMinecraft();
        this.a();
    }

    void a() {
        this.h.put("customLegL", "legL");
        this.h.put("customShinL", "shinL");
        this.h.put("customLegR", "legR");
        this.h.put("customShinR", "shinR");
        this.f.put("top", "upperBody");
        this.f.put("customArmL", "armL");
        this.f.put("customLowerArmL", "lowerArmL");
        this.f.put("customArmR", "armR");
        this.f.put("customLowerArmR", "lowerArmR");
        this.g.put("lowerArmR", em_class2582 -> gc_class360.c(em_class2582.float_ai()));
        this.g.put("lowerArmL", em_class2582 -> gc_class360.c(em_class2582.float_T()));
    }

    boolean d(CustomModelEntity cy_class1532) {
        String string = cy_class1532.a();
        if (cy_class1532.f) {
            return false;
        }
        if (CustomModel.f(string)) {
            return false;
        }
        if (CustomModel.g() != null) {
            return true;
        }
        UUID uUID = cy_class1532.b();
        GirlEntity em_class2582 = GirlEntity.getGirlEntity(uUID);
        if (em_class2582 == null) {
            return true;
        }
        HashSet<String> hashSet = em_class2582.Y();
        hashSet.remove(string);
        String string2 = GirlEntity.a(hashSet);
        PackageHandler.networkWrapper.sendToServer((IMessage)new UploadModelString(string2, cy_class1532.b()));
        return true;
    }

    @SideOnly(value=Side.CLIENT)
    public static void a(GirlEntity em_class2582, float f) {
        if (em_class2582.isDead) {
            return;
        }
        if (!em_class2582.world.isRemote) {
            return;
        }
        if (!em_class2582.boolean_H()) {
            return;
        }
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        for (String string : em_class2582.Y()) {
            CustomModelEntity cy_class1532 = new CustomModelEntity(em_class2582.world, em_class2582.girlID(), string);
            k = true;
            renderManager.renderEntity(cy_class1532, 0.0, 0.0, 0.0, 0.0f, f, false);
        }
    }

    //a
    @Override
    public boolean shouldRender(CustomModelEntity cy_class1532, ICamera iCamera, double d, double d2, double d3) {
        return super.shouldRender(cy_class1532, iCamera, d, d2, d3);
    }

    boolean a(float f) {
        if (f == 2.876945f) {
            return true;
        }
        if (f == 1.876945f) {
            return true;
        }
        if (k) {
            k = false;
            return true;
        }
        return false;
    }

    void a(CustomModel.b_inner96 b_inner962, CustomModelEntity cy_class1532, float f) {
        if (b_inner962 == null || b_inner962.i() == c8_class117.DEFAULT) {
            this.j = null;
            return;
        }
        GL11.glDisable(2896);
        this.j = b_inner962.i() == c8_class117.SEXMOD ? cj_class134.a(cy_class1532, f) : null;
    }

    // a
    @Override
    public void doRender(CustomModelEntity cy_class1532, double d, double d2, double d3, float f, float f2) {
        //Object object;
        Object object2;
        //GirlEntity em_class2582;
        EntityLivingBase em_class2582;
        if (!this.a(f2)) {
            return;
        }
        if (CustomModel.d) {
            return;
        }
        if (this.d(cy_class1532)) {
            return;
        }
        cy_class1532.c = new MatrixStack();
        CustomModel.b_inner96 b_inner962 = CustomModel.b(cy_class1532.a());
        this.c = cy_class1532;
        this.b = b_inner962;
        this.a(b_inner962, cy_class1532, f2);
        if (f2 == 1.876945f || f2 == 2.876945f) {
            this.d = new Vec3d(1.0, 1.0, 1.0);
            super.doRender(cy_class1532, d, d2, d3, f, f2);
            GL11.glEnable(2896);
            return;
        }
        UUID uUID = cy_class1532.b();
        if (uUID == null) {
            return;
        }
        GirlEntity em_class2583 = GirlEntity.getGirlEntity(uUID);
        if (em_class2583 == null) {
            return;
        }
        if (b_inner962 != null && !b_inner962.a() && em_class2583.int_ah() == 0) {
            return;
        }
        if (!(em_class2583 instanceof PlayerGirl)) {
            em_class2582 = em_class2583;
        } else {
            object2 = ((PlayerGirl)em_class2583).java_util_UUID_m();
            if (object2 == null) {
                return;
            }
            EntityPlayer object = cy_class1532.world.getPlayerEntityByUUID((UUID)object2);
            em_class2582 = object == null ? em_class2583 : object;
        }
        object2 = em_class2583.a(this.a, cy_class1532, em_class2582, f2);
        BlockPos object = new BlockPos(Math.floor(em_class2582.posX), Math.floor(em_class2582.posY), Math.floor(em_class2582.posZ));
        int n = em_class2582.world.getLight((BlockPos)object, true);
        Vec3d vec3d = new Vec3d(1.0, 1.0, 1.0);
        float f3 = be_class78.b(n, 10.0f, 15.0f) / 15.0f;
        this.d = new Vec3d(vec3d.x * (double)f3, vec3d.y * (double)f3, vec3d.z * (double)f3);
        GlStateManager.pushMatrix();
        GlStateManager.translate(((Vec3d)object2).x, ((Vec3d)object2).y, ((Vec3d)object2).z);
        if (em_class2583.boolean_Q()) {
            GlStateManager.rotate(em_class2583.java_lang_Float_I().floatValue(), 0.0f, 1.0f, 0.0f);
        }
        super.doRender(cy_class1532, 0.0, 0.0, 0.0, f, f2);
        GlStateManager.popMatrix();
        GL11.glEnable(2896);
    }

    public static Vec3d a(Minecraft minecraft, CustomModelEntity cy_class1532, EntityLivingBase entityLivingBase, GirlEntity em_class2582, float f) {
        Vec3d vec3d;
        //Object object;
        if (em_class2582.boolean_Q()) {
            Vec3d object = em_class2582.net_minecraft_util_math_Vec3d_o();
            float f2 = em_class2582.java_lang_Float_I().floatValue();
            cy_class1532.prevPosX = ((Vec3d)object).x;
            cy_class1532.prevPosY = ((Vec3d)object).y;
            cy_class1532.prevPosZ = ((Vec3d)object).z;
            cy_class1532.lastTickPosX = ((Vec3d)object).x;
            cy_class1532.lastTickPosY = ((Vec3d)object).y;
            cy_class1532.lastTickPosZ = ((Vec3d)object).z;
            cy_class1532.posX = ((Vec3d)object).x;
            cy_class1532.posY = ((Vec3d)object).y;
            cy_class1532.posZ = ((Vec3d)object).z;
            cy_class1532.rotationYaw = f2;
            cy_class1532.prevRotationYaw = f2;
            cy_class1532.rotationYawHead = f2;
            cy_class1532.prevRotationYawHead = f2;
            cy_class1532.renderYawOffset = f2;
            cy_class1532.prevRenderYawOffset = f2;
            cy_class1532.rotationPitch = f2;
            cy_class1532.prevRotationPitch = f2;
            vec3d = object;
        } else {
            cy_class1532.rotationYaw = entityLivingBase.rotationYaw;
            cy_class1532.prevRotationYaw = entityLivingBase.prevRotationYaw;
            cy_class1532.rotationYawHead = entityLivingBase.rotationYawHead;
            cy_class1532.prevRotationYawHead = entityLivingBase.prevRotationYawHead;
            cy_class1532.renderYawOffset = entityLivingBase.renderYawOffset;
            cy_class1532.prevRenderYawOffset = entityLivingBase.prevRenderYawOffset;
            cy_class1532.rotationPitch = entityLivingBase.rotationPitch;
            cy_class1532.prevRotationPitch = entityLivingBase.prevRotationPitch;
            cy_class1532.prevPosX = entityLivingBase.prevPosX;
            cy_class1532.prevPosY = entityLivingBase.prevPosY;
            cy_class1532.prevPosZ = entityLivingBase.prevPosZ;
            cy_class1532.lastTickPosX = entityLivingBase.lastTickPosX;
            cy_class1532.lastTickPosY = entityLivingBase.lastTickPosY;
            cy_class1532.lastTickPosZ = entityLivingBase.lastTickPosZ;
            cy_class1532.posX = entityLivingBase.posX;
            cy_class1532.posY = entityLivingBase.posY;
            cy_class1532.posZ = entityLivingBase.posZ;
            vec3d = Reference.LerpVec3d(new Vec3d(entityLivingBase.lastTickPosX, entityLivingBase.lastTickPosY, entityLivingBase.lastTickPosZ), entityLivingBase.getPositionVector(), (double)f);
        }
        EntityPlayerSP object = minecraft.player;
        Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(((EntityPlayer)object).lastTickPosX, ((EntityPlayer)object).lastTickPosY, ((EntityPlayer)object).lastTickPosZ), ((Entity)object).getPositionVector(), (double)f);
        return vec3d.subtract(vec3d2);
    }

    //a
    @Override
    public void render(GeoModel geoModel, CustomModelEntity cy_class1532, float f, float f2, float f3, float f4, float f5) {
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        for (GeoBone geoBone : geoModel.topLevelBones) {
            if (f != 1.876945f) {
                this.a(cy_class1532, geoBone, f);
            }
            cy_class1532.c.translate(-geoBone.getPivotX() / 16.0f, -geoBone.getPivotY() / 16.0f, -geoBone.getPivotZ() / 16.0f);
            this.renderRecursively(bufferBuilder, geoBone, f2, f3, f4, f5);
        }
        Tessellator.getInstance().draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
    }

    EntityLivingBase c(CustomModelEntity cy_class1532) {
        EntityPlayer entityPlayer;
        GirlEntity em_class2582 = this.b(cy_class1532);
        if (em_class2582 == null) {
            return null;
        }
        EntityLivingBase entityLivingBase = !(em_class2582 instanceof PlayerGirl) ? em_class2582 : ((entityPlayer = cy_class1532.world.getPlayerEntityByUUID(((PlayerGirl)em_class2582).java_util_UUID_m())) == null ? em_class2582 : entityPlayer);
        return entityLivingBase;
    }

    GirlEntity b(CustomModelEntity cy_class1532) {
        UUID uUID = cy_class1532.b();
        GirlEntity entity = GirlID.GetGirlID(uUID);
        if (entity != null) {
            return entity;
        }
        return GirlEntity.getGirlEntity(uUID);
    }

    void a(CustomModelEntity cy_class1532, GeoBone geoBone, float f) {
        String string = this.a(cy_class1532);
        if (string == null) {
            return;
        }
        this.a(cy_class1532, geoBone, f, string);
    }

    void a(CustomModelEntity cy_class1532, GeoBone geoBone, float f, String string) {
        GirlEntity em_class2582 = this.b(cy_class1532);
        EntityLivingBase entityLivingBase = this.c(cy_class1532);
        cy_class1532.c = em_class2582.a(string, false);
        if (!cy_class1532.f || f != 2.876945f) {
            return;
        }
        cy_class1532.c.scale(0.5f, 0.5f, 0.5f);
        cy_class1532.c.rotateY((float)Math.toRadians(-a_class4.b));
    }

    String a(CustomModelEntity cy_class1532) {
        if (cy_class1532.f) {
            return cy_class1532.d.boneName;
        }
        CustomModel.b_inner96 b_inner962 = CustomModel.b(cy_class1532.a());
        if (b_inner962 == null) {
            return null;
        }
        if (gw_class389.CUSTOM_BONE.equals((Object)b_inner962.j())) {
            return b_inner962.b();
        }
        return b_inner962.j().boneName;
    }

    @Override
    public void renderRecursively(BufferBuilder bufferBuilder, GeoBone geoBone, float f, float f2, float f3, float f4) {
        this.c.c.push();
        this.c.c.translate(geoBone);
        this.c.c.moveToPivot(geoBone);
        this.c.c.rotate(geoBone);
        this.c.c.scale(geoBone);
        this.c.c.moveBackFromPivot(geoBone);
        if (!geoBone.isHidden()) {
            for (GeoCube object : geoBone.childCubes) {
                this.c.c.push();
                GlStateManager.pushMatrix();
                this.renderCube(bufferBuilder, object, f, f2, f3, f4);
                GlStateManager.popMatrix();
                this.c.c.pop();
            }
        }
        if (!geoBone.childBonesAreHiddenToo()) {
            for (GeoBone geoBone2 : geoBone.childBones) {
                this.renderRecursively(bufferBuilder, geoBone2, f, f2, f3, f4);
            }
        }
        try {
            this.c.c.pop();
        } catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
    }

    @Override
    public void renderCube(BufferBuilder bufferBuilder, GeoCube geoCube, float f, float f2, float f3, float f4) {
        this.c.c.moveToPivot(geoCube);
        this.c.c.rotate(geoCube);
        this.c.c.moveBackFromPivot(geoCube);
        for (GeoQuad geoQuad : geoCube.quads) {
            if (geoQuad == null) continue;
            Vector3f vector3f = new Vector3f((float)geoQuad.normal.getX(), (float)geoQuad.normal.getY(), (float)geoQuad.normal.getZ());
            this.c.c.getNormalMatrix().transform((Tuple3f)vector3f);
            if ((geoCube.size.y == 0.0f || geoCube.size.z == 0.0f) && vector3f.getX() < 0.0f) {
                vector3f.x *= -1.0f;
            }
            if ((geoCube.size.x == 0.0f || geoCube.size.z == 0.0f) && vector3f.getY() < 0.0f) {
                vector3f.y *= -1.0f;
            }
            if ((geoCube.size.x == 0.0f || geoCube.size.y == 0.0f) && vector3f.getZ() < 0.0f) {
                vector3f.z *= -1.0f;
            }
            if (this.j != null) {
                this.d = gx_class390.a(this.d, vector3f, this.j);
            }
            for (GeoVertex geoVertex : geoQuad.vertices) {
                Vector4f vector4f = new Vector4f(geoVertex.position.getX(), geoVertex.position.getY(), geoVertex.position.getZ(), 1.0f);
                this.c.c.getModelMatrix().transform((Tuple4f)vector4f);
                bufferBuilder.pos(vector4f.getX(), vector4f.getY(), vector4f.getZ()).tex(geoVertex.textureU, geoVertex.textureV).color((float)this.d.x, (float)this.d.y, (float)this.d.z, f4).normal(vector3f.getX(), vector3f.getY(), vector3f.getZ()).endVertex();
            }
        }
    }

    //@Override
    //public void doRender(EntityLivingBase entityLivingBase, double d, double d2, double d3, float f, float f2) {
    //    this.a((cy_class153)entityLivingBase, d, d2, d3, f, f2);
    //}

    //@Override
    //public void render(GeoModel geoModel, Object object, float f, float f2, float f3, float f4, float f5) {
    //    this.a(geoModel, (cy_class153)object, f, f2, f3, f4, f5);
    //}

    //@Override
    //public ResourceLocation getEntityTexture(Entity entity) {
    //    return super.getEntityTexture((cy_class153)entity);
    //}

    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.a((cy_class153)entity, d, d2, d3, f, f2);
    //}

    //@Override
    //public boolean shouldRender(Entity entity, ICamera iCamera, double d, double d2, double d3) {
    //    return this.a((cy_class153)entity, iCamera, d, d2, d3);
    //}
}

