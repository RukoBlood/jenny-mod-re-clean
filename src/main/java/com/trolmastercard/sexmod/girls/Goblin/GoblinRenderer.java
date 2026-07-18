/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector4f
 */
package com.trolmastercard.sexmod.girls.Goblin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.Utils;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GoblinRenderer extends d6_class165<GoblinEntity> {
    final static Vec3i w = new Vec3i(255, 255, 255);
    final static float K = -420.69f;
    final static float A = 8.0f;
    final static float L = 3.0f;
    final static Vec3d G = new Vec3d(10.0, -20.0, -10.0);
    final static float J = 0.1f;
    final static HashSet<String> D = new HashSet<String>(Arrays.asList("meatTorso", "meatCheekR", "meatCheekL", "meatFootR", "meatFootL", "meatShinR", "meatShinL", "meatLegL", "meatLegR", "nippleR", "nippleL", "preggy", "shoeL", "shoeR", "frontAndInside", "Lside", "Rside", "cheekR", "cheekL", "fuckhole", "head", "nose", "neck", "armL", "lowerArmL", "armR", "lowerArmR", "torso", "LegL", "LegR", "shinL", "shinR"));
    final static HashSet<String> M = new HashSet<String>(Arrays.asList("lashR", "lashL", "closedR", "closedL", "browL", "browR", "closedL", "closedL"));
    final static HashSet<String> C = new HashSet<String>(Arrays.asList("meatLegR", "meatShinR", "meatFootR", "boobR", "boobR1", "boobR2"));
    static Minecraft minecraft;
    float v = 0.0f;
    boolean u = false;
    boolean F = false;
    static float B;
    float z = 0.0f;
    static float H;
    static float t;
    static float I;
    static float E;
    static float N;
    static float x;

    public GoblinRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
        minecraft = Minecraft.getMinecraft();
    }

    //a
    @Override
    protected ResourceLocation d(GoblinEntity e3_class2192) throws IOException {
        ResourceLocation resourceLocation;
        UUID uUID = e3_class2192.getID();
        if (uUID == null) {
            uUID = e3_class2192.java_util_UUID_e();
        }
        if (e3_class2192.world instanceof FakeWorld || uUID == null) {
            resourceLocation = (ResourceLocation)l.get(minecraft.getSession().getProfile().getId());
            if (resourceLocation == null) {
                return this.a(minecraft.getSession().getProfile().getId(), e3_class2192.world);
            }
        } else {
            resourceLocation = (ResourceLocation)l.get(uUID);
            if (resourceLocation == null) {
                return this.a(uUID, e3_class2192.world);
            }
        }
        return resourceLocation;
    }

    // TODO
    //public static void a(GirlEntity em_class2582, float f) {
    //    y.getRenderManager().renderEntity(em_class2582, 0.0, 0.0, 0.0, -420.69f, f, false);
    //}

    public static void a(float f) {
        if (!(minecraft.getRenderViewEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer) minecraft.getRenderViewEntity();
        float f2 = entityPlayer.distanceWalkedModified - entityPlayer.prevDistanceWalkedModified;
        float f3 = -(entityPlayer.distanceWalkedModified + f2 * f);
        float f4 = entityPlayer.prevCameraYaw + (entityPlayer.cameraYaw - entityPlayer.prevCameraYaw) * f;
        float f5 = MathHelper.sin(f3 * (float)Math.PI) * f4 * 0.5f;
        GlStateManager.translate(Math.cos((double) GoblinRenderer.minecraft.player.rotationYaw * (Math.PI / 180)) * (double)f5, Math.abs(MathHelper.cos(f3 * (float)Math.PI) * f4), Math.sin((double) GoblinRenderer.minecraft.player.rotationYaw * (Math.PI / 180)) * (double)f5);
    }

    @Override
    public void render(GeoModel geoModel, GoblinEntity goblinEntity, float f, float f2, float f3, float f4, float f5) {
        super.render(geoModel, goblinEntity, f, f2, f3, f4, goblinEntity.ar);
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double d, double d2, double d3, float f, float f2) {
        if (!(entity instanceof GoblinEntity)) {
            super.doRenderShadowAndFire(entity, d, d2, d3, f, f2);
            return;
        }
        GoblinEntity e3_class2192 = (GoblinEntity)entity;
        if (e3_class2192.currentAction() == Action.PICK_UP || e3_class2192.currentAction() == Action.SHOULDER_IDLE) {
            return;
        }
        super.doRenderShadowAndFire(entity, d, d2, d3, f, f2);
    }

    public static Vec3d a(World world, GirlEntity em_class2582, UUID uUID, double d, double d2, double d3) {
        if (world == null) {
            return new Vec3d(d, d2, d3);
        }
        if (uUID == null) {
            return new Vec3d(d, d2, d3);
        }
        if (em_class2582 == null) {
            return new Vec3d(d, d2, d3);
        }
        EntityPlayer entityPlayer = world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return new Vec3d(d, d2, d3);
        }
        Vec3d vec3d = entityPlayer.getPositionVector();
        Vec3d vec3d2 = GoblinRenderer.minecraft.player.getPositionVector();
        em_class2582.prevRenderYawOffset = entityPlayer.prevRotationYawHead;
        em_class2582.renderYawOffset = entityPlayer.rotationYawHead;
        em_class2582.setCurrentAction(Action.START_THROWING);
        return vec3d.subtract(vec3d2);
    }

    @Override
    public void doRender(GoblinEntity goblinEntity, double d, double d2, double d3, float f, float f2) {
        Object object;
        this.j = goblinEntity;
        this.u = -420.69f == f && goblinEntity.currentAction() == Action.SHOULDER_IDLE;
        this.F = -420.69f == f && goblinEntity.currentAction() == Action.PICK_UP;
        this.z = goblinEntity.world.getLight(goblinEntity.getPosition(), true);
        this.v = f2;
        B = f;
        Action action = goblinEntity.currentAction();
        UUID uUID = goblinEntity.java_util_UUID_e();
        if (goblinEntity.boolean_h()) {
            object = GoblinRenderer.a(goblinEntity.world, goblinEntity, uUID, d, d2, d3);
            d = ((Vec3d)object).x;
            d2 = ((Vec3d)object).y;
            d3 = ((Vec3d)object).z;
        }
        if (action == Action.THROWN || action == Action.START_THROWING) {
            if (GoblinRenderer.minecraft.gameSettings.thirdPersonView == 0 && f == -420.69f && !goblinEntity.boolean_h()) {
                return;
            }
            if (!goblinEntity.boolean_h()) {
                float f3;
                goblinEntity.prevRenderYawOffset = f3 = goblinEntity.java_lang_Float_I().floatValue();
                goblinEntity.renderYawOffset = f3;
            }
        }
        if (GoblinRenderer.a((GirlEntity)goblinEntity, action)) {
            if (GoblinRenderer.minecraft.player.getPersistentID().equals(uUID)) {
                if (-420.69f != f) {
                    return;
                }
                goblinEntity.renderYawOffset = GoblinRenderer.minecraft.player.rotationYaw + 180.0f;
                goblinEntity.prevRenderYawOffset = GoblinRenderer.minecraft.player.rotationYaw + 180.0f;
                object = GoblinRenderer.minecraft.player.getLookVec();
                GlStateManager.pushMatrix();
                GlStateManager.translate(((Vec3d)object).x, ((Vec3d)object).y + (double) GoblinRenderer.minecraft.player.getEyeHeight(), ((Vec3d)object).z);
                Vec3d vec3d = GoblinEntity.b(new Vec3d(-Math.abs(GoblinRenderer.minecraft.player.rotationPitch), 0.0, 0.0), GoblinRenderer.minecraft.player.rotationYaw);
                GlStateManager.rotate(GoblinRenderer.minecraft.player.rotationPitch, (float)vec3d.x, 0.0f, (float)vec3d.z);
                d = 0.0;
                d2 = 0.0;
                d3 = 0.0;
            } else {
                if (!goblinEntity.boolean_h() || uUID == null || GoblinRenderer.minecraft.player.getPersistentID().equals(uUID)) {
                    if (uUID != null && !GoblinRenderer.minecraft.player.getPersistentID().equals(uUID)) {
                        object = goblinEntity.world.getPlayerEntityByUUID(uUID);
                        if (object != null) {
                            goblinEntity.renderYawOffset = ((EntityPlayer)object).rotationYaw;
                            goblinEntity.prevRenderYawOffset = ((EntityPlayer)object).rotationYaw;
                        }
                    } else {
                        goblinEntity.renderYawOffset = GoblinRenderer.minecraft.player.rotationYaw;
                        goblinEntity.prevRenderYawOffset = GoblinRenderer.minecraft.player.rotationYaw;
                    }
                }
                object = GoblinRenderer.a((GirlEntity)goblinEntity, goblinEntity.java_util_UUID_e(), f2);
                d = ((Vec3d)object).x;
                d2 = ((Vec3d)object).y;
                d3 = ((Vec3d)object).z;
            }
        } else if (this.u) {
            GoblinRenderer.a(f2);
            object = new Vec3d(Reference.LerpFloat(-0.1f, 0.2f, GoblinRenderer.minecraft.gameSettings.fovSetting / 110.0f), 0.0, 0.0);
            object = GoblinEntity.b((Vec3d)object, GoblinRenderer.minecraft.player.rotationYaw);
            d = ((Vec3d)object).x;
            d2 = ((Vec3d)object).y;
            d3 = ((Vec3d)object).z;
            goblinEntity.renderYawOffset = GoblinRenderer.minecraft.player.rotationYaw;
            goblinEntity.prevRenderYawOffset = GoblinRenderer.minecraft.player.prevRotationYaw;
            if (GoblinRenderer.minecraft.player.isSneaking()) {
                d2 -= 0.075;
            }
        } else if (action == Action.SHOULDER_IDLE) {
            if (uUID == null) {
                return;
            }
            if (GoblinRenderer.minecraft.player.getPersistentID().equals(uUID) && GoblinRenderer.minecraft.gameSettings.thirdPersonView == 0) {
                return;
            }
            object = goblinEntity.world.getPlayerEntityByUUID(uUID);
            if (object == null) {
                return;
            }
            Vector4f vector4f = GoblinRenderer.a_0((EntityPlayer)object, f2);
            d = vector4f.x;
            d2 = vector4f.y;
            d3 = vector4f.z;
            goblinEntity.renderYawOffset = vector4f.w;
            if (((Entity)object).isSneaking()) {
                d2 -= 0.32;
            }
        } else if (action == Action.PICK_UP && uUID != null && (object = goblinEntity.world.getPlayerEntityByUUID(uUID)) != null) {
            goblinEntity.prevRenderYawOffset = ((EntityPlayer)object).prevRotationYawHead;
            goblinEntity.renderYawOffset = ((EntityPlayer)object).rotationYawHead;
        }
        super.doRender(goblinEntity, d, d2, d3, f, f2);
        if (GoblinRenderer.a((GirlEntity)goblinEntity, action) && GoblinRenderer.minecraft.gameSettings.thirdPersonView == 0 && GoblinRenderer.minecraft.player.getPersistentID().equals(uUID)) {
            GlStateManager.popMatrix();
        }
    }

    public static boolean a(GirlEntity em_class2582, Action fp_class3242) {
        if (fp_class3242 == Action.START_THROWING && !em_class2582.boolean_h()) {
            return false;
        }
        if (GoblinRenderer.minecraft.gameSettings.thirdPersonView != 0 && (fp_class3242 == Action.START_THROWING || fp_class3242 == Action.PICK_UP)) {
            return false;
        }
        switch (fp_class3242) {
            case PICK_UP: 
            case CATCH: 
            case CATCH_BJ: 
            case CATCH_BJ_IDLE: 
            case START_THROWING: {
                return true;
            }
        }
        return false;
    }

    public static Vec3d a(GirlEntity em_class2582, UUID uUID, float f) {
        if (uUID == null) {
            return Vec3d.ZERO;
        }
        EntityPlayer entityPlayer = em_class2582.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return Vec3d.ZERO;
        }
        Vec3d vec3d = Reference.LerpVec3d(new Vec3d(entityPlayer.prevPosX, entityPlayer.prevPosY, entityPlayer.prevPosZ), entityPlayer.getPositionVector(), (double)f);
        Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(GoblinRenderer.minecraft.player.prevPosX, GoblinRenderer.minecraft.player.prevPosY, GoblinRenderer.minecraft.player.prevPosZ), GoblinRenderer.minecraft.player.getPositionVector(), (double)f);
        return vec3d.subtract(vec3d2);
    }

    public static Vector4f a_0(EntityPlayer entityPlayer, float f) {
        EntityPlayerSP entityPlayerSP = GoblinRenderer.minecraft.player;
        float f2 = Reference.LerpFloat(entityPlayer.prevRenderYawOffset, entityPlayer.renderYawOffset, f);
        Vec3d vec3d = Reference.LerpVec3d(new Vec3d(entityPlayer.lastTickPosX, entityPlayer.lastTickPosY, entityPlayer.lastTickPosZ), entityPlayer.getPositionVector(), (double)f);
        Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), (double)f);
        Vec3d vec3d3 = vec3d.subtract(vec3d2);
        return new Vector4f((float)vec3d3.x, (float)vec3d3.y, (float)vec3d3.z, f2);
    }

    @Override
    protected Vec3i a(String string) {
        String[] stringArray = e4_class223.java_lang_String_arr_a(this.j);
        if (stringArray.length < 8) {
            return r;
        }
        if (string.contains("band")) {
            return w;
        }
        if (string.contains("eyeColor") || string.contains("eyeColor2")) {
            return GoblinRenderer.unknownCalcVec(stringArray[8]);
        }
        if (string.contains("variant") || string.contains("boob")) {
            return GoblinRenderer.c(stringArray[7]);
        }
        if (string.contains("hair")) {
            return GoblinRenderer.d(stringArray[6]);
        }
        if (D.contains(string)) {
            return GoblinRenderer.c(stringArray[7]);
        }
        if (M.contains(string)) {
            return GoblinRenderer.d(stringArray[6]);
        }
        return r;
    }

    public static Vec3i unknownCalcVec(String string) {
        return eh_class250.values()[Integer.parseInt(string)].a();
    }

    public static Vec3i c(String string) {
        return by_class106.values()[Integer.parseInt(string)].a();
    }

    public static Vec3i d(String string) {
        return g5_class349.values()[Integer.parseInt(string)].a();
    }

    @Override
    protected void a(BufferBuilder bufferBuilder, String string, GeoBone geoBone) {
        if (((GoblinEntity)this.j).world instanceof FakeWorld) {
            return;
        }
        String[] stringArray = e4_class223.java_lang_String_arr_a(this.j);
        if (stringArray.length < 8) {
            return;
        }
        switch (string) {
            case "earL": {
                GoblinRenderer.a(geoBone, stringArray[0], stringArray[1], stringArray[3]);
                break;
            }
            case "earR": {
                GoblinRenderer.a(geoBone, stringArray[0], stringArray[2], stringArray[4]);
                break;
            }
            case "hair": {
                GoblinRenderer.a(geoBone, stringArray[5]);
                break;
            }
            case "body": {
                geoBone.setPivotY(-0.15f);
                GoblinRenderer.a(this.j, geoBone);
                break;
            }
            case "LegR": {
                GoblinRenderer.a(this.u, geoBone, 25.0f, 25.0f);
                break;
            }
            case "boobR": {
                GoblinRenderer.a(this.u, geoBone, 30.0f, 30.0f);
                break;
            }
            case "boobR1": {
                GoblinRenderer.a(this.u, geoBone, 10.0f, 15.0f);
                break;
            }
            case "boobR2": {
                GoblinRenderer.a(this.u, geoBone, 5.0f, 3.0f);
            }
        }
        if (string.contains("crown")) {
            GoblinRenderer.a(this.j, geoBone, stringArray[9]);
        }
    }

    public static void a(GirlEntity em_class2582, GeoBone geoBone, String string) {
        if (em_class2582.boolean_h()) {
            geoBone.setHidden(true);
        } else if (em_class2582 instanceof GoblinEntity) {
            int n = Integer.parseInt(string);
            geoBone.setHidden(n == 0);
        } else if (em_class2582 instanceof PlayerGoblin) {
            geoBone.setHidden(em_class2582.getDataManager().get(Fighter.ITEM_SLOT_3).isEmpty());
        }
    }

    public static void a(boolean bl, GeoBone geoBone, float f, float f2) {
        if (minecraft.isGamePaused()) {
            return;
        }
        if (!bl) {
            return;
        }
        geoBone.setRotationX(geoBone.getRotationX() + TrigMath.toRadians(Utils.clamp(x, -f, f)));
        geoBone.setRotationZ(geoBone.getRotationZ() + TrigMath.toRadians(Utils.clamp(N, -f2, f2)));
    }

    public static void a(GirlEntity em_class2582, GeoBone geoBone) {
        if (B != -420.69f || em_class2582.currentAction() != Action.SHOULDER_IDLE) {
            return;
        }
        float f = -GoblinRenderer.minecraft.getRenderManager().playerViewX;
        geoBone.setPivotY(8.0f);
        if (minecraft.isGamePaused()) {
            return;
        }
        geoBone.setRotationX(geoBone.getRotationX() + TrigMath.toRadians(f));
    }

    public static void a(GeoBone geoBone, String string) {
        int n = Integer.parseInt(string);
        GoblinRenderer.a(geoBone, n);
    }

    static HashSet<Integer> b(int n, String string) {
        int n2;
        int n3 = n - 1;
        ArrayList<HashSet<Integer>> arrayList = GoblinRenderer.a(n3);
        for (n2 = Integer.parseInt(string); n2 >= arrayList.size(); n2 -= arrayList.size()) {
        }
        return arrayList.get(n2);
    }

    static ArrayList<HashSet<Integer>> a(int n) {
        ArrayList<HashSet<Integer>> arrayList = new ArrayList<HashSet<Integer>>();
        GoblinRenderer.a(0, new HashSet<Integer>(), n, arrayList);
        return arrayList;
    }

    static void a(int n, HashSet<Integer> hashSet, int n2, ArrayList<HashSet<Integer>> arrayList) {
        if (n > n2) {
            arrayList.add(hashSet);
            return;
        }
        HashSet<Integer> hashSet2 = new HashSet<Integer>(hashSet);
        GoblinRenderer.a(n + 1, hashSet, n2, arrayList);
        hashSet2.add(n);
        GoblinRenderer.a(n + 1, hashSet2, n2, arrayList);
    }

    static HashSet<Integer> a(int n, String string) {
        HashSet<Integer> hashSet = new HashSet<Integer>();
        int n2 = Integer.parseInt(string);
        n2 = (int)(0.01f * (float)n2 * (float)n2);
        int n3 = Math.round((float)n2 / 100.0f * (float)n);
        Random random = new Random(n2);
        for (int i = 0; i < n3; ++i) {
            int n4 = random.nextInt(n);
            if (!hashSet.contains(n4)) {
                hashSet.add(n4);
                continue;
            }
            --i;
        }
        return hashSet;
    }

    static void a(GeoBone geoBone2, String string, String string2, String string3) {
        GeoBone geoBone3 = GoblinRenderer.a(geoBone2, Integer.parseInt(string));
        GeoBone geoBone4 = GoblinRenderer.a(geoBone3, Integer.parseInt(string2));
        List<GeoBone> list = geoBone4.childBones;
        int n2 = list.size();
        HashSet<Integer> hashSet = GoblinRenderer.b(n2, string3);
        geoBone4.childBones.forEach(geoBone -> geoBone.setHidden(true));
        hashSet.forEach(n -> GoblinRenderer.b(geoBone4, n));
    }

    @Override
    protected Vec3i a(Vec3i vec3i) {
        if (!this.u && !this.F) {
            return vec3i;
        }
        float f = Utils.clamp(this.z, 2.0f, 15.0f) / 15.0f;
        return new Vec3i((float)vec3i.getX() * f, (float)vec3i.getY() * f, (float)vec3i.getZ() * f);
    }

    @Override
    protected ItemStack net_minecraft_item_ItemStack_a(@Nullable ItemStack itemStack) {
        Action fp_class3242 = ((GoblinEntity)this.j).currentAction();
        if (fp_class3242 == Action.RUN || fp_class3242 == Action.CATCH) {
            return ((GoblinEntity)this.j).getDataManager().get(GoblinEntity.a0);
        }
        return itemStack;
    }

    @Override
    public HashSet<String> getBlacklistedBoneNames() {
        return new HashSet<String>(){
            {
                this.add("boobs");
                this.add("booty");
                this.add("vagina");
                this.add("fuckhole");
                this.add("preggy");
                this.add("LegL");
                this.add("LegR");
                this.add("cheekR");
                this.add("cheekL");
            }
        };
    }

    @Override
    protected float a__() {
        if (((GoblinEntity)this.j).currentAction() == Action.CATCH) {
            return 0.5f;
        }
        return 1.0f;
    }

    @Override
    protected Vec3d a(ItemStack itemStack) {
        if (itemStack == null) {
            return Vec3d.ZERO;
        }
        if (itemStack.getItem() instanceof ItemBlock || itemStack.getMaxStackSize() == 1) {
            return super.a(itemStack);
        }
        return new Vec3d(180.0, 0.0, 0.0);
    }

    @Override
    public void a(BufferBuilder bufferBuilder, GeoCube geoCube, GeoBone geoBone, float f, float f2, float f3, float f4, double d) {
        if (this.u && !C.contains(geoBone.getName())) {
            return;
        }
        if (this.p.contains(geoBone.getName())) {
            return;
        }
        this.q = geoBone;
        super.a(bufferBuilder, geoCube, geoBone, f, f2, f3, f4, d);
    }

    //public void doRender(GirlEntity em_class2582, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GoblinEntity)em_class2582, d, d2, d3, f, f2);
    //}

    //public void render(GeoModel geoModel, GirlEntity em_class2582, float f, float f2, float f3, float f4, float f5) {
    //    this.render(geoModel, (GoblinEntity)em_class2582, f, f2, f3, f4, f5);
    //}

    //@Override
    //protected ResourceLocation d(GirlEntity em_class2582) throws IOException {
    //    return this.a((GoblinEntity)em_class2582);
    //}

    //@Override
    //public void doRender(EntityLivingBase entityLivingBase, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GoblinEntity)entityLivingBase, d, d2, d3, f, f2);
    //}

    //@Override
    //public void render(GeoModel geoModel, Object object, float f, float f2, float f3, float f4, float f5) {
    //    this.render(geoModel, (GoblinEntity)object, f, f2, f3, f4, f5);
    //}

    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((GoblinEntity)entity, d, d2, d3, f, f2);
    //}

    static {
        B = 0.0f;
        H = 0.0f;
        t = 0.0f;
        I = 0.0f;
        E = 0.0f;
        N = 0.0f;
        x = 0.0f;
    }
}

