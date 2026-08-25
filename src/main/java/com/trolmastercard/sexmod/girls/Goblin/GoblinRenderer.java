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

import com.trolmastercard.sexmod.girls.base.*;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.util.RotationHelper;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.ThreadNames;
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

public class GoblinRenderer extends GirlRendererBase<GoblinEntity> {
    final static Vec3i DEFAULT_BONE_COLOR = new Vec3i(255, 255, 255);
    final static float SENTINEL_VALUE = -420.69f;
    final static float PIVOT_Y = 8.0f;
    final static float RENDER_SCALE_B = 3.0f;
    final static Vec3d MOVEMENT_DIR_VECTOR = new Vec3d(10.0, -20.0, -10.0);
    final static float LERP_FACTOR = 0.1f;
    final static HashSet<String> NUDE_BONE_NAMES = new HashSet<String>(Arrays.asList("meatTorso", "meatCheekR", "meatCheekL", "meatFootR", "meatFootL", "meatShinR", "meatShinL", "meatLegL", "meatLegR", "nippleR", "nippleL", "preggy", "shoeL", "shoeR", "frontAndInside", "Lside", "Rside", "cheekR", "cheekL", "fuckhole", "head", "nose", "neck", "armL", "lowerArmL", "armR", "lowerArmR", "torso", "LegL", "LegR", "shinL", "shinR"));
    final static HashSet<String> LASH_BONE_NAMES = new HashSet<String>(Arrays.asList("lashR", "lashL", "closedR", "closedL", "browL", "browR", "closedL", "closedL"));
    final static HashSet<String> LEG_BONE_NAMES = new HashSet<String>(Arrays.asList("meatLegR", "meatShinR", "meatFootR", "boobR", "boobR1", "boobR2"));
    static Minecraft mc2;
    float currentYawOffset = 0.0f;
    boolean isShoulderIdle = false;
    boolean isBeingPickedUp = false;
    static float currentActionValue;
    float lightLevel = 0.0f;
    static float lastPlayerYaw;
    static float lastPlayerPitch;
    static float prevStrafeRotation;
    static float prevForwardRotation;
    static float strafeRotation;
    static float forwardRotation;

    public GoblinRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
        mc2 = Minecraft.getMinecraft();
    }

    //a
    @Override
    protected ResourceLocation getGoblinTexture(GoblinEntity goblin) throws IOException {
        ResourceLocation location;
        UUID uUID = goblin.getInteractionPlayerUUID();
        if (uUID == null) {
            uUID = goblin.getOwnerUUID();
        }
        if (goblin.world instanceof FakeWorld || uUID == null) {
            location = skinTextureCache.get(mc2.getSession().getProfile().getId());
            if (location == null) {
                return this.generateSkinTexture(mc2.getSession().getProfile().getId(), goblin.world);
            }
        } else {
            location = skinTextureCache.get(uUID);
            if (location == null) {
                return this.generateSkinTexture(uUID, goblin.world);
            }
        }
        return location;
    }

    //public static void a(GirlEntity em_class2582, float f) {
    //    y.getRenderManager().renderEntity(em_class2582, 0.0, 0.0, 0.0, -420.69f, f, false);
    //}

    public static void renderEntityInFirstPerson(GirlEntity girl, float partialTicks){
        mc2.getRenderManager().renderEntity(girl, 0.0, 0.0, 0.0, SENTINEL_VALUE, partialTicks, false);
    }

    public static void setFirstPersonCamera(float partialTicks) {
        if (mc2.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) mc2.getRenderViewEntity();
            float walkDelta = player.distanceWalkedModified - player.prevDistanceWalkedModified;
            float walkedDistance = -(player.distanceWalkedModified + walkDelta * partialTicks);
            float cameraYaw = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
            float bobOffset = MathHelper.sin(walkedDistance * (float) Math.PI) * cameraYaw * 0.5f;
            GlStateManager.translate(Math.cos((double) GoblinRenderer.mc2.player.rotationYaw * (Math.PI / 180)) * (double) bobOffset, Math.abs(MathHelper.cos(walkedDistance * (float) Math.PI) * cameraYaw), Math.sin((double) GoblinRenderer.mc2.player.rotationYaw * (Math.PI / 180)) * (double) bobOffset);
        }
    }

    @Override
    public void render(GeoModel model, GoblinEntity entity, float partialTicks, float r, float g, float b, float a) {
        super.render(model, entity, partialTicks, r, g, b, entity.opacity);
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (entity instanceof GoblinEntity) {
            GoblinEntity goblin = (GoblinEntity) entity;
            if (goblin.getCurrentAction() != Action.PICK_UP && goblin.getCurrentAction() != Action.SHOULDER_IDLE) {
                super.doRenderShadowAndFire(entity, x, y, z, yaw, partialTicks);
            }
        } else {
            super.doRenderShadowAndFire(entity, x, y, z, yaw, partialTicks);
        }
    }

    public static Vec3d getThrowPosition(World world, GirlEntity girl, UUID ownerId, double x, double y, double z) {
        if (world == null || ownerId == null || girl == null) {
            return new Vec3d(x, y, z);
        }
        EntityPlayer player = world.getPlayerEntityByUUID(ownerId);
        if (player == null) {
            return new Vec3d(x, y, z);
        }

        Vec3d pos = player.getPositionVector();
        Vec3d localPos = GoblinRenderer.mc2.player.getPositionVector();
        girl.prevRenderYawOffset = player.prevRotationYawHead;
        girl.renderYawOffset = player.rotationYawHead;
        girl.setCurrentAction(Action.START_THROWING);
        return pos.subtract(localPos);
    }

    @Override
    public void doRender(GoblinEntity goblin, double x, double y, double z, float entityYaw, float partialTicks) {
        //Object object;
        this.renderEntity = goblin;
        this.isShoulderIdle = -420.69f == entityYaw && goblin.getCurrentAction() == Action.SHOULDER_IDLE;
        this.isBeingPickedUp = -420.69f == entityYaw && goblin.getCurrentAction() == Action.PICK_UP;
        this.lightLevel = goblin.world.getLight(goblin.getPosition(), true);
        this.currentYawOffset = partialTicks;
        currentActionValue = entityYaw;
        Action action = goblin.getCurrentAction();
        UUID ownerUUid = goblin.getOwnerUUID();
        if (goblin.isLocallyRegistered()) {
            Vec3d throwPos2 = GoblinRenderer.getThrowPosition(goblin.world, goblin, ownerUUid, x, y, z);
            x = throwPos2.x;
            y = throwPos2.y;
            z = throwPos2.z;
        }
        if (action == Action.THROWN || action == Action.START_THROWING) {
            if (GoblinRenderer.mc2.gameSettings.thirdPersonView != 0 || entityYaw != -420.69f || goblin.isLocallyRegistered()) {
                if (!goblin.isLocallyRegistered()) {
                    float yaw2;
                    goblin.prevRenderYawOffset = yaw2 = goblin.getYawRotation();
                    goblin.renderYawOffset = yaw2;
                }
            }
        }

        if (GoblinRenderer.isThrowAction(goblin, action)) {
            if (GoblinRenderer.mc2.player.getPersistentID().equals(ownerUUid)) {
                if (SENTINEL_VALUE == entityYaw) {
                    goblin.renderYawOffset = GoblinRenderer.mc2.player.rotationYaw + 180.0f;
                    goblin.prevRenderYawOffset = GoblinRenderer.mc2.player.rotationYaw + 180.0f;
                    Vec3d lookVec2 = GoblinRenderer.mc2.player.getLookVec();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(lookVec2.x, lookVec2.y + (double) GoblinRenderer.mc2.player.getEyeHeight(), lookVec2.z);
                    Vec3d pitchVec2 = GoblinEntity.rotateVectorYaw(new Vec3d(-Math.abs(GoblinRenderer.mc2.player.rotationPitch), 0.0, 0.0), GoblinRenderer.mc2.player.rotationYaw);
                    GlStateManager.rotate(GoblinRenderer.mc2.player.rotationPitch, (float) pitchVec2.x, 0.0f, (float) pitchVec2.z);
                    x = 0.0;
                    y = 0.0;
                    z = 0.0;
                }
            } else {
                if (!goblin.isLocallyRegistered() || ownerUUid == null || GoblinRenderer.mc2.player.getPersistentID().equals(ownerUUid)) {
                    if (ownerUUid != null && !GoblinRenderer.mc2.player.getPersistentID().equals(ownerUUid)) {
                        EntityPlayer owner = goblin.world.getPlayerEntityByUUID(ownerUUid);
                        if (owner != null) {
                            goblin.renderYawOffset = owner.rotationYaw;
                            goblin.prevRenderYawOffset = owner.rotationYaw;
                        }
                    } else {
                        goblin.renderYawOffset = GoblinRenderer.mc2.player.rotationYaw;
                        goblin.prevRenderYawOffset = GoblinRenderer.mc2.player.rotationYaw;
                    }
                }
                Vec3d throwAim2 = GoblinRenderer.getThrowAim(goblin, goblin.getOwnerUUID(), partialTicks);
                x = throwAim2.x;
                y = throwAim2.y;
                z = throwAim2.z;
            }
        } else if (this.isShoulderIdle) {
            GoblinRenderer.setFirstPersonCamera(partialTicks);
            Vec3d shoulderOffset2 = new Vec3d(RotationHelper.LerpFloat(-0.1f, 0.2f, GoblinRenderer.mc2.gameSettings.fovSetting / 110.0f), 0.0, 0.0);
            shoulderOffset2 = GoblinEntity.rotateVectorYaw(shoulderOffset2, GoblinRenderer.mc2.player.rotationYaw);
            x = shoulderOffset2.x;
            y = shoulderOffset2.y;
            z = shoulderOffset2.z;
            goblin.renderYawOffset = GoblinRenderer.mc2.player.rotationYaw;
            goblin.prevRenderYawOffset = GoblinRenderer.mc2.player.prevRotationYaw;
            if (GoblinRenderer.mc2.player.isSneaking()) {
                y -= 0.075;
            }
        } else if (action == Action.SHOULDER_IDLE) {
            if (ownerUUid != null) {
                if (!GoblinRenderer.mc2.player.getPersistentID().equals(ownerUUid) || GoblinRenderer.mc2.gameSettings.thirdPersonView != 0) {
                    EntityPlayer owner2 = goblin.world.getPlayerEntityByUUID(ownerUUid);
                    if (owner2 != null) {
                        Vector4f firstPersonView = GoblinRenderer.getFirstPersonView(owner2, partialTicks);
                        x = firstPersonView.x;
                        y = firstPersonView.y;
                        z = firstPersonView.z;
                        goblin.renderYawOffset = firstPersonView.w;
                        if (owner2.isSneaking()) {
                            y -= 0.32;
                        }
                    }
                }
            }
        } else if (action == Action.PICK_UP) {
            EntityPlayer pickUpOwner = goblin.world.getPlayerEntityByUUID(ownerUUid);
            if (pickUpOwner != null) {
                goblin.prevRenderYawOffset = pickUpOwner.prevRotationYawHead;
                goblin.renderYawOffset = pickUpOwner.rotationYawHead;
            }
        }

        super.doRender(goblin, x, y, z, entityYaw, partialTicks);
        if (GoblinRenderer.isThrowAction(goblin, action) && GoblinRenderer.mc2.gameSettings.thirdPersonView == 0 && GoblinRenderer.mc2.player.getPersistentID().equals(ownerUUid)) {
            GlStateManager.popMatrix();
        }
    }

    public static boolean isThrowAction(GirlEntity girl, Action action) {
        if (action == Action.START_THROWING && !girl.isLocallyRegistered() || GoblinRenderer.mc2.gameSettings.thirdPersonView != 0 && (action == Action.START_THROWING || action == Action.PICK_UP)) {
            return false;
        }
        switch (action) {
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

    public static Vec3d getThrowAim(GirlEntity girl, UUID uUID, float partialTicks) {
        if (uUID == null) {
            return Vec3d.ZERO;
        }
        EntityPlayer player = girl.world.getPlayerEntityByUUID(uUID);
        if (player == null) {
            return Vec3d.ZERO;
        }

        Vec3d pos = RotationHelper.LerpVec3d(new Vec3d(player.prevPosX, player.prevPosY, player.prevPosZ), player.getPositionVector(), partialTicks);
        Vec3d localPos = RotationHelper.LerpVec3d(new Vec3d(GoblinRenderer.mc2.player.prevPosX, GoblinRenderer.mc2.player.prevPosY, GoblinRenderer.mc2.player.prevPosZ), GoblinRenderer.mc2.player.getPositionVector(), partialTicks);
        return pos.subtract(localPos);
    }

    public static Vector4f getFirstPersonView(EntityPlayer owner, float partialTicks) {
        EntityPlayerSP player = GoblinRenderer.mc2.player;
        float yaw = RotationHelper.LerpFloat(owner.prevRenderYawOffset, owner.renderYawOffset, partialTicks);
        Vec3d pos = RotationHelper.LerpVec3d(new Vec3d(owner.lastTickPosX, owner.lastTickPosY, owner.lastTickPosZ), owner.getPositionVector(), partialTicks);
        Vec3d localPos = RotationHelper.LerpVec3d(new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ), player.getPositionVector(), partialTicks);
        Vec3d offset = pos.subtract(localPos);
        return new Vector4f((float)offset.x, (float)offset.y, (float)offset.z, yaw);
    }

    @Override
    protected Vec3i resolveBoneColor(String boneName) {
        String[] stringArray = AbstractNpcOnlyEntity.getModelCodeParts(this.renderEntity);
        if (stringArray.length < 8) {
            return DEFAULT_COLOR;
        }
        if (boneName.contains("band")) {
            return DEFAULT_BONE_COLOR;
        }
        if (boneName.contains("eyeColor") || boneName.contains("eyeColor2")) {
            return GoblinRenderer.getEyeColor(stringArray[8]);
        }
        if (boneName.contains("variant") || boneName.contains("boob")) {
            return GoblinRenderer.getSkinColor(stringArray[7]);
        }
        if (boneName.contains("hair")) {
            return GoblinRenderer.getHairColor(stringArray[6]);
        }
        if (NUDE_BONE_NAMES.contains(boneName)) {
            return GoblinRenderer.getSkinColor(stringArray[7]);
        }
        if (LASH_BONE_NAMES.contains(boneName)) {
            return GoblinRenderer.getHairColor(stringArray[6]);
        }
        return DEFAULT_COLOR;
    }

    public static Vec3i getEyeColor(String string) {
        return EyeColor.values()[Integer.parseInt(string)].getColor();
    }

    public static Vec3i getSkinColor(String string) {
        return SkinColor.values()[Integer.parseInt(string)].getColor();
    }

    public static Vec3i getHairColor(String string) {
        return HairColor.values()[Integer.parseInt(string)].getColor();
    }

    @Override
    protected void onBoneProcessing(BufferBuilder buffer, String boneName, GeoBone bone) {
        if (!(this.renderEntity.world instanceof FakeWorld)) {
            String[] modelCodeParts = AbstractNpcOnlyEntity.getModelCodeParts(this.renderEntity);
            if (modelCodeParts.length >= 8) {
                switch (boneName) {
                    case "earL": {
                        GoblinRenderer.applyBoneParts(bone, modelCodeParts[0], modelCodeParts[1], modelCodeParts[3]);
                        break;
                    }
                    case "earR": {
                        GoblinRenderer.applyBoneParts(bone, modelCodeParts[0], modelCodeParts[2], modelCodeParts[4]);
                        break;
                    }
                    case "hair": {
                        GoblinRenderer.applyBonePart(bone, modelCodeParts[5]);
                        break;
                    }
                    case "body": {
                        bone.setPivotY(-0.15f);
                        GoblinRenderer.applyBoneState(this.renderEntity, bone);
                        break;
                    }
                    case "LegR": {
                        GoblinRenderer.applyBoneRot(this.isShoulderIdle, bone, 25.0f, 25.0f);
                        break;
                    }
                    case "boobR": {
                        GoblinRenderer.applyBoneRot(this.isShoulderIdle, bone, 30.0f, 30.0f);
                        break;
                    }
                    case "boobR1": {
                        GoblinRenderer.applyBoneRot(this.isShoulderIdle, bone, 10.0f, 15.0f);
                        break;
                    }
                    case "boobR2": {
                        GoblinRenderer.applyBoneRot(this.isShoulderIdle, bone, 5.0f, 3.0f);
                    }
                }
                if (boneName.contains("crown")) {
                    GoblinRenderer.applyBoneColor(this.renderEntity, bone, modelCodeParts[9]);
                }
            }
        }
    }

    public static void applyBoneColor(GirlEntity girl, GeoBone bone, String crownCode) {
        if (girl.isLocallyRegistered()) {
            bone.setHidden(true);
        } else if (girl instanceof GoblinEntity) {
            int variant = Integer.parseInt(crownCode);
            bone.setHidden(variant == 0);
        } else if (girl instanceof PlayerGoblin) {
            bone.setHidden(girl.getDataManager().get(Fighter.HELMET_SLOT).isEmpty());
        }
    }

    public static void applyBoneRot(boolean isShoulderIdle, GeoBone bone, float maxForward, float maxStrafe) {
        if (!mc2.isGamePaused()) {
            if (isShoulderIdle) {
                bone.setRotationX(bone.getRotationX() + TrigMath.wrapDegrees(ThreadNames.clamp(forwardRotation, -maxForward, maxForward)));
                bone.setRotationZ(bone.getRotationZ() + TrigMath.wrapDegrees(ThreadNames.clamp(strafeRotation, -maxStrafe, maxStrafe)));
            }
        }
    }

    public static void applyBoneState(GirlEntity girl, GeoBone bone) {
        if (currentActionValue == SENTINEL_VALUE && girl.getCurrentAction() == Action.SHOULDER_IDLE) {
            float cameraPitch = -GoblinRenderer.mc2.getRenderManager().playerViewX;
            bone.setPivotY(PIVOT_Y);
            if (!mc2.isGamePaused()) {
                bone.setRotationX(bone.getRotationX() + TrigMath.wrapDegrees(cameraPitch));
            }
        }
    }

    public static void applyBonePart(GeoBone bone, String partCode) {
        int index = Integer.parseInt(partCode);
        GoblinRenderer.selectAndShowExclusiveChildBone(bone, index);
    }

    static HashSet<Integer> buildColorIndexGroups(int groupCount, String code) {
        int groupIndex;
        int lastIndex = groupCount - 1;
        ArrayList<HashSet<Integer>> groups = GoblinRenderer.buildColorIndexGroups(lastIndex);
        for (groupIndex = Integer.parseInt(code); groupIndex >= groups.size(); groupIndex -= groups.size()) {
        }
        return groups.get(groupIndex);
    }

    static ArrayList<HashSet<Integer>> buildColorIndexGroups(int size) {
        ArrayList<HashSet<Integer>> groups = new ArrayList<>();
        GoblinRenderer.buildColorGroups(0, new HashSet<>(), size, groups);
        return groups;
    }

    static void buildColorGroups(int index, HashSet<Integer> current, int max, ArrayList<HashSet<Integer>> groups) {
        if (index > max) {
            groups.add(current);
        } else {
            HashSet<Integer> next = new HashSet<>(current);
            GoblinRenderer.buildColorGroups(index + 1, current, max, groups);
            next.add(index);
            GoblinRenderer.buildColorGroups(index + 1, next, max, groups);
        }
    }

    static HashSet<Integer> parseColorGroup(int n, String string) {
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

    static void applyBoneParts(GeoBone geoBone2, String string, String string2, String string3) {
        GeoBone geoBone3 = GoblinRenderer.selectAndShowExclusiveChildBone(geoBone2, Integer.parseInt(string));
        GeoBone geoBone4 = GoblinRenderer.selectAndShowExclusiveChildBone(geoBone3, Integer.parseInt(string2));
        List<GeoBone> list = geoBone4.childBones;
        int n2 = list.size();
        HashSet<Integer> hashSet = GoblinRenderer.buildColorIndexGroups(n2, string3);
        geoBone4.childBones.forEach(geoBone -> geoBone.setHidden(true));
        hashSet.forEach(n -> GoblinRenderer.ShowChildBoneByIndex(geoBone4, n));
    }

    @Override
    protected Vec3i filterFinalColor(Vec3i inputColor) {
        if (!this.isShoulderIdle && !this.isBeingPickedUp) {
            return inputColor;
        }
        float f = ThreadNames.clamp(this.lightLevel, 2.0f, 15.0f) / 15.0f;
        return new Vec3i((float) inputColor.getX() * f, (float) inputColor.getY() * f, (float) inputColor.getZ() * f);
    }

    @Override
    protected ItemStack getHeldItem(@Nullable ItemStack input) {
        Action action = this.renderEntity.getCurrentAction();
        if (action == Action.RUN || action == Action.CATCH) {
            return this.renderEntity.getDataManager().get(GoblinEntity.HELD_ITEM);
        }
        return input;
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
    protected float getRenderItemScale() {
        if (this.renderEntity.getCurrentAction() == Action.CATCH) {
            return 0.5f;
        }
        return 1.0f;
    }

    @Override
    protected Vec3d getItemRenderRotation(ItemStack item) {
        if (item == null) {
            return Vec3d.ZERO;
        }
        if (item.getItem() instanceof ItemBlock || item.getMaxStackSize() == 1) {
            return super.getItemRenderRotation(item);
        }
        return new Vec3d(180.0, 0.0, 0.0);
    }

    @Override
    public void renderCubeGeometry(BufferBuilder buffer, GeoCube cube, GeoBone bone, float r, float g, float b, float a, double textureOffset) {
        if (this.isShoulderIdle && !LEG_BONE_NAMES.contains(bone.getName())) {
            return;
        }
        if (this.activeCustomPartBones.contains(bone.getName())) {
            return;
        }
        this.currentRenderingBone = bone;
        super.renderCubeGeometry(buffer, cube, bone, r, g, b, a, textureOffset);
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
        currentActionValue = 0.0f;
        lastPlayerYaw = 0.0f;
        lastPlayerPitch = 0.0f;
        prevStrafeRotation = 0.0f;
        prevForwardRotation = 0.0f;
        strafeRotation = 0.0f;
        forwardRotation = 0.0f;
    }
}

