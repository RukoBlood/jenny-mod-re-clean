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

import com.trolmastercard.sexmod.Packets.UploadModelString;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.GirlID;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.CustomModel.ClothingGui;
import com.trolmastercard.sexmod.util.CustomPartCategory;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.ThreadNames;
import com.trolmastercard.sexmod.util.anim.BoneDeformProcessor;
import com.trolmastercard.sexmod.util.interfaces.IBoneRotationSupplier;
import com.trolmastercard.sexmod.world.WorldUtils;
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

public class CustomModelRenderer extends GeoEntityRenderer<CustomModelEntity> {
    final static public float RENDER_FLAG_GUI = 1.876945f;
    final static public float RENDER_FLAG_SPECIAL = 2.876945f;
    Minecraft mc;
    CustomModelEntity currentEntity = null;
    CustomModel.ModelData currentModelData = null;
    HashMap<String, String> legBonesMap = new HashMap();
    HashMap<String, String> armBonesMap = new HashMap();
    HashMap<String, IBoneRotationSupplier> boneRotationSuppliers = new HashMap();
    static public boolean forceRenderNextTick = false;
    Vec3d colorMultiplier = new Vec3d(1.0, 1.0, 1.0);
    Vec3d lightDirection;

    public CustomModelRenderer(RenderManager renderManager, AnimatedGeoModel<CustomModelEntity> animatedGeoModel) {
        super(renderManager, animatedGeoModel);
        this.mc = Minecraft.getMinecraft();
        this.initBoneMappings();
    }

    void initBoneMappings() {
        this.legBonesMap.put("customLegL", "legL");
        this.legBonesMap.put("customShinL", "shinL");
        this.legBonesMap.put("customLegR", "legR");
        this.legBonesMap.put("customShinR", "shinR");
        this.armBonesMap.put("top", "upperBody");
        this.armBonesMap.put("customArmL", "armL");
        this.armBonesMap.put("customLowerArmL", "lowerArmL");
        this.armBonesMap.put("customArmR", "armR");
        this.armBonesMap.put("customLowerArmR", "lowerArmR");
        this.boneRotationSuppliers.put("lowerArmR", girl -> TrigMath.wrapDegrees(girl.getRightArmRotation()));
        this.boneRotationSuppliers.put("lowerArmL", girl -> TrigMath.wrapDegrees(girl.getLeftArmRotation()));
    }

    boolean validateAndCleanModel(CustomModelEntity entity) {
        String modelName = entity.getModelName();
        if (entity.isItemModel) {
            return false;
        }
        if (CustomModel.isModelDisabled(modelName)) {
            return false;
        }
        if (CustomModel.getGlobalModelOverride() != null) {
            return true;
        }
        UUID girlUUID = entity.getGirlUUID();
        GirlEntity girl = GirlEntity.getClientGirlEntity(girlUUID);
        if (girl == null) {
            return true;
        }

        HashSet<String> customParts = girl.getCustomPartsSet();
        customParts.remove(modelName);
        String serializedParts = GirlEntity.serializePartsSet(customParts);

        PackageHandler.INSTANCE.sendToServer((IMessage)new UploadModelString(serializedParts, entity.getGirlUUID()));
        return true;
    }

    @SideOnly(value=Side.CLIENT)
    public static void renderGirlCustomParts(GirlEntity girl, float partialTicks) {
        if (girl.isDead) {
            return;
        }
        if (!girl.world.isRemote) {
            return;
        }
        if (!girl.hasCustomParts()) {
            return;
        }
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        for (String partName : girl.getCustomPartsSet()) {
            CustomModelEntity entity = new CustomModelEntity(girl.world, girl.girlID(), partName);
            forceRenderNextTick = true;
            renderManager.renderEntity(entity, 0.0, 0.0, 0.0, 0.0f, partialTicks, false);
        }
    }

    //a
    @Override
    public boolean shouldRender(CustomModelEntity entity, ICamera camera, double camX, double camY, double camZ) {
        return super.shouldRender(entity, camera, camX, camY, camZ);
    }

    boolean shouldProcessRender(float partialTicks) {
        if (partialTicks == RENDER_FLAG_SPECIAL) {
            return true;
        }
        if (partialTicks == RENDER_FLAG_GUI) {
            return true;
        }
        if (forceRenderNextTick) {
            forceRenderNextTick = false;
            return true;
        }
        return false;
    }

    void updateLighting(CustomModel.ModelData modelData, CustomModelEntity entity, float partialTicks) {
        if (modelData == null || modelData.getLightingType() == LightingType.DEFAULT) {
            this.lightDirection = null;
            return;
        }
        GL11.glDisable(GL11.GL_LIGHTING);
        this.lightDirection = modelData.getLightingType() == LightingType.SEXMOD ? WorldUtils.getLightDirectionVector(entity, partialTicks) : null;
    }

    // a
    @Override
    public void doRender(CustomModelEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //Object object;
        Object offset;
        //GirlEntity em_class2582;
        EntityLivingBase targetEntity;
        if (!this.shouldProcessRender(partialTicks)) {
            return;
        }
        if (CustomModel.isGlobalRenderingDisabled) {
            return;
        }
        if (this.validateAndCleanModel(entity)) {
            return;
        }
        entity.matrixStack = new MatrixStack();
        CustomModel.ModelData modelData = CustomModel.getModelData(entity.getModelName());
        this.currentEntity = entity;
        this.currentModelData = modelData;
        this.updateLighting(modelData, entity, partialTicks);
        if (partialTicks == RENDER_FLAG_GUI || partialTicks == RENDER_FLAG_SPECIAL) {
            this.colorMultiplier = new Vec3d(1.0, 1.0, 1.0);
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            GL11.glEnable(GL11.GL_LIGHTING);
            return;
        }

        UUID uUID = entity.getGirlUUID();
        if (uUID == null) {
            return;
        }
        GirlEntity girl = GirlEntity.getClientGirlEntity(uUID);
        if (girl == null) {
            return;
        }
        if (modelData != null && !modelData.isAlwaysVisible() && girl.getOutfitIndex() == 0) {
            return;
        }

        if (!(girl instanceof PlayerGirl)) {
            targetEntity = girl;
        } else {
            offset = ((PlayerGirl)girl).getOwnerUserUUID();
            if (offset == null) {
                return;
            }
            EntityPlayer object = entity.world.getPlayerEntityByUUID((UUID)offset);
            targetEntity = object == null ? girl : object;
        }

        offset = girl.renderCustomModelTransform(this.mc, entity, targetEntity, partialTicks);
        BlockPos entityBlockPos = new BlockPos(Math.floor(targetEntity.posX), Math.floor(targetEntity.posY), Math.floor(targetEntity.posZ));
        int blockLight = targetEntity.world.getLight((BlockPos)entityBlockPos, true);

        Vec3d vec3d = new Vec3d(1.0, 1.0, 1.0);
        float lightFactor = ThreadNames.clamp(blockLight, 10.0f, 15.0f) / 15.0f;
        this.colorMultiplier = new Vec3d(vec3d.x * (double)lightFactor, vec3d.y * (double)lightFactor, vec3d.z * (double)lightFactor);

        GlStateManager.pushMatrix();
        GlStateManager.translate(((Vec3d)offset).x, ((Vec3d)offset).y, ((Vec3d)offset).z);
        if (girl.isAnchored()) {
            GlStateManager.rotate(girl.getYawRotation(), 0.0f, 1.0f, 0.0f);
        }
        super.doRender(entity, 0.0, 0.0, 0.0, entityYaw, partialTicks);
        GlStateManager.popMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public static Vec3d renderTransformedModel(Minecraft mc, CustomModelEntity entity, EntityLivingBase targetEntity, GirlEntity girl, float partialTicks) {
        Vec3d targetPos;
        //Object object;
        if (girl.isAnchored()) {
            Vec3d anchorPos = girl.getTargetPosition();
            float yaw = girl.getYawRotation();
            entity.prevPosX = ((Vec3d)anchorPos).x;
            entity.prevPosY = ((Vec3d)anchorPos).y;
            entity.prevPosZ = ((Vec3d)anchorPos).z;
            entity.lastTickPosX = ((Vec3d)anchorPos).x;
            entity.lastTickPosY = ((Vec3d)anchorPos).y;
            entity.lastTickPosZ = ((Vec3d)anchorPos).z;
            entity.posX = ((Vec3d)anchorPos).x;
            entity.posY = ((Vec3d)anchorPos).y;
            entity.posZ = ((Vec3d)anchorPos).z;
            entity.rotationYaw = yaw;
            entity.prevRotationYaw = yaw;
            entity.rotationYawHead = yaw;
            entity.prevRotationYawHead = yaw;
            entity.renderYawOffset = yaw;
            entity.prevRenderYawOffset = yaw;
            entity.rotationPitch = yaw;
            entity.prevRotationPitch = yaw;
            targetPos = anchorPos;
        } else {
            entity.rotationYaw = targetEntity.rotationYaw;
            entity.prevRotationYaw = targetEntity.prevRotationYaw;
            entity.rotationYawHead = targetEntity.rotationYawHead;
            entity.prevRotationYawHead = targetEntity.prevRotationYawHead;
            entity.renderYawOffset = targetEntity.renderYawOffset;
            entity.prevRenderYawOffset = targetEntity.prevRenderYawOffset;
            entity.rotationPitch = targetEntity.rotationPitch;
            entity.prevRotationPitch = targetEntity.prevRotationPitch;
            entity.prevPosX = targetEntity.prevPosX;
            entity.prevPosY = targetEntity.prevPosY;
            entity.prevPosZ = targetEntity.prevPosZ;
            entity.lastTickPosX = targetEntity.lastTickPosX;
            entity.lastTickPosY = targetEntity.lastTickPosY;
            entity.lastTickPosZ = targetEntity.lastTickPosZ;
            entity.posX = targetEntity.posX;
            entity.posY = targetEntity.posY;
            entity.posZ = targetEntity.posZ;
            targetPos = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(targetEntity.lastTickPosX, targetEntity.lastTickPosY, targetEntity.lastTickPosZ), targetEntity.getPositionVector(), (double)partialTicks);
        }
        EntityPlayerSP object = mc.player;
        Vec3d vec3d2 = ReferenceAndRotationHelper.LerpVec3d(new Vec3d(((EntityPlayer)object).lastTickPosX, ((EntityPlayer)object).lastTickPosY, ((EntityPlayer)object).lastTickPosZ), ((Entity)object).getPositionVector(), (double)partialTicks);
        return targetPos.subtract(vec3d2);
    }

    //a
    @Override
    public void render(GeoModel model, CustomModelEntity animatable, float partialTicks, float red, float green, float blue, float alpha) {
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

        for (GeoBone topBone : model.topLevelBones) {
            if (partialTicks != RENDER_FLAG_GUI) {
                this.attachBoneTransformation(animatable, topBone, partialTicks);
            }
            animatable.matrixStack.translate(-topBone.getPivotX() / 16.0f, -topBone.getPivotY() / 16.0f, -topBone.getPivotZ() / 16.0f);
            this.renderRecursively(bufferBuilder, topBone, red, green, blue, alpha);
        }
        Tessellator.getInstance().draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
    }

    EntityLivingBase getTargetLivingEntity(CustomModelEntity entity) {
        EntityPlayer owner;
        GirlEntity girl = this.getGirlEntity(entity);
        if (girl == null) {
            return null;
        }
        EntityLivingBase entityLivingBase = !(girl instanceof PlayerGirl)
                ? girl
                : ((owner = entity.world.getPlayerEntityByUUID(((PlayerGirl)girl).getOwnerUserUUID())) == null ? girl : owner);
        return entityLivingBase;
    }

    GirlEntity getGirlEntity(CustomModelEntity entity) {
        UUID girtId = entity.getGirlUUID();
        GirlEntity girl = GirlID.GetGirlID(girtId);
        if (girl != null) {
            return girl;
        }
        return GirlEntity.getClientGirlEntity(girtId);
    }

    void attachBoneTransformation(CustomModelEntity entity, GeoBone geoBone, float partialTicks) {
        String targetBoneName = this.getTargetBoneName(entity);
        if (targetBoneName == null) {
            return;
        }
        this.applyBoneMatrix(entity, geoBone, partialTicks, targetBoneName);
    }

    void applyBoneMatrix(CustomModelEntity entity, GeoBone geoBone, float partialTicks, String boneName) {
        GirlEntity girl = this.getGirlEntity(entity);
        EntityLivingBase entityLivingBase = this.getTargetLivingEntity(entity);
        entity.matrixStack = girl.getBoneMatrixStack(boneName, false);
        if (!entity.isItemModel || partialTicks != RENDER_FLAG_SPECIAL) {
            return;
        }
        entity.matrixStack.scale(0.5f, 0.5f, 0.5f);
        entity.matrixStack.rotateY((float)Math.toRadians(-ClothingGui.MODEL_Y_ROTATION));
    }

    String getTargetBoneName(CustomModelEntity entity) {
        if (entity.isItemModel) {
            return entity.itemModelData.boneName;
        }
        CustomModel.ModelData modelData = CustomModel.getModelData(entity.getModelName());
        if (modelData == null) {
            return null;
        }
        if (CustomPartCategory.CUSTOM_BONE.equals((Object)modelData.getCategory())) {
            return modelData.getCustomBoneName();
        }
        return modelData.getCategory().boneName;
    }

    @Override
    public void renderRecursively(BufferBuilder buffer, GeoBone bone, float red, float green, float blue, float alpha) {
        this.currentEntity.matrixStack.push();
        this.currentEntity.matrixStack.translate(bone);
        this.currentEntity.matrixStack.moveToPivot(bone);
        this.currentEntity.matrixStack.rotate(bone);
        this.currentEntity.matrixStack.scale(bone);
        this.currentEntity.matrixStack.moveBackFromPivot(bone);
        if (!bone.isHidden()) {
            for (GeoCube cube : bone.childCubes) {
                this.currentEntity.matrixStack.push();
                GlStateManager.pushMatrix();
                this.renderCube(buffer, cube, red, green, blue, alpha);
                GlStateManager.popMatrix();
                this.currentEntity.matrixStack.pop();
            }
        }
        if (!bone.childBonesAreHiddenToo()) {
            for (GeoBone childBone : bone.childBones) {
                this.renderRecursively(buffer, childBone, red, green, blue, alpha);
            }
        }
        this.currentEntity.matrixStack.pop();
    }

    @Override
    public void renderCube(BufferBuilder buffer, GeoCube cube, float red, float green, float blue, float alpha) {
        this.currentEntity.matrixStack.moveToPivot(cube);
        this.currentEntity.matrixStack.rotate(cube);
        this.currentEntity.matrixStack.moveBackFromPivot(cube);
        for (GeoQuad quad : cube.quads) {
            if (quad == null) continue;
            Vector3f normal = new Vector3f((float)quad.normal.getX(), (float)quad.normal.getY(), (float)quad.normal.getZ());
            this.currentEntity.matrixStack.getNormalMatrix().transform((Tuple3f)normal);

            if ((cube.size.y == 0.0f || cube.size.z == 0.0f) && normal.getX() < 0.0f) {
                normal.x *= -1.0f;
            }
            if ((cube.size.x == 0.0f || cube.size.z == 0.0f) && normal.getY() < 0.0f) {
                normal.y *= -1.0f;
            }
            if ((cube.size.x == 0.0f || cube.size.y == 0.0f) && normal.getZ() < 0.0f) {
                normal.z *= -1.0f;
            }

            if (this.lightDirection != null) {
                this.colorMultiplier = BoneDeformProcessor.calculatePhysicsVector(this.colorMultiplier, normal, this.lightDirection);
            }

            for (GeoVertex vertex : quad.vertices) {
                Vector4f vertexPos = new Vector4f(vertex.position.getX(), vertex.position.getY(), vertex.position.getZ(), 1.0f);
                this.currentEntity.matrixStack.getModelMatrix().transform((Tuple4f)vertexPos);
                buffer.pos(vertexPos.getX(), vertexPos.getY(), vertexPos.getZ()).tex(vertex.textureU, vertex.textureV).color((float)this.colorMultiplier.x, (float)this.colorMultiplier.y, (float)this.colorMultiplier.z, alpha).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
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

