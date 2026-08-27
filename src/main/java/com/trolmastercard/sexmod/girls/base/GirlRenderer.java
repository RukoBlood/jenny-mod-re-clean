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
package com.trolmastercard.sexmod.girls.base;

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
import javax.vecmath.Vector4f;

import com.trolmastercard.sexmod.gui.CustomModel.ClothingGui;
import com.trolmastercard.sexmod.util.SkinHelper;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Custom.CustomModelRenderer;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.*;
import com.trolmastercard.sexmod.util.anim.BoneDeformProcessor;
import com.trolmastercard.sexmod.util.interfaces.IGirlRenderer;
import com.trolmastercard.sexmod.world.FakeWorld;
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
public abstract class GirlRenderer<T extends GirlEntity & IAnimatable> extends GeoEntityRenderer<T> implements IGirlRenderer {
    final static protected ResourceLocation LINE = new ResourceLocation("sexmod", "textures/line.png");
    //final static float m = 1.5f;
    protected double leashYOffset;
    protected T renderEntity;
    static protected Minecraft mc;
    static protected HashMap<UUID, ResourceLocation> skinTextureCache;
    Color baseSkinColor = new Color(245, 199, 165);
    Color blushColor = new Color(245, 157, 169);
    boolean fallbackSkinLoaded = false;
    protected HashSet<String> activeCustomPartBones = new HashSet();
    //Integer k = null;
    //Integer b = null;
    //Integer d = null;
    float bowPullProgressNotPlayer = 0.0f;
    static public BufferBuilder tempBuffer;
    Matrix4f globalModelMatrix = null;
    protected GeoBone currentRenderingBone = null;

    public GirlRenderer(RenderManager renderManager, AnimatedGeoModel<T> animatedGeoModel, double leashYOffset) {
        super(renderManager, animatedGeoModel);
        this.leashYOffset = leashYOffset;
        mc = Minecraft.getMinecraft();
        this.shadowSize = 0.2f;
    }

    //d
    //@Override
    // getResourceLocation
    protected ResourceLocation getGoblinTexture(T entity) throws IOException {
        ResourceLocation cachedLocation;
        if (entity.world instanceof FakeWorld || entity.getInteractionPlayerUUID() == null) {
            cachedLocation = skinTextureCache.get(mc.getSession().getProfile().getId());
            if (cachedLocation == null) {
                return this.generateSkinTexture(mc.getSession().getProfile().getId(), entity.world);
            }
        } else {
            cachedLocation = skinTextureCache.get(entity.getInteractionPlayerUUID());
            if (cachedLocation == null) {
                return this.generateSkinTexture(entity.getInteractionPlayerUUID(), entity.world);
            }
        }
        return cachedLocation;
    }

    protected ResourceLocation generateSkinTexture(UUID uUID, World world) throws IOException {
        BufferedImage skin;
        try {
            skin = SkinHelper.GetPlayerSkin(uUID);
            Graphics graphics = skin.getGraphics();
            graphics.setColor(this.baseSkinColor);
            graphics.fillRect(0, 0, 4, 3);
            graphics.setColor(this.blushColor);
            graphics.fillRect(4, 0, 3, 3);
        } catch (Exception e) {
            if (!this.fallbackSkinLoaded) {
                this.fallbackSkinLoaded = true;
            }
            skin = ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation("sexmod", "textures/player/steve.png")).getInputStream());
        }
        skinTextureCache.put(uUID, this.renderManager.renderEngine.getDynamicTextureLocation("player" + uUID, new DynamicTexture(skin)));
        return skinTextureCache.get(uUID);
    }

    @CheckReturnValue
    public static float getInterpolatedYaw(GirlEntity girl, float partialTicks) {
        return girl.isAnchored() ? girl.getYawRotation() : RotationHelper.LerpFloat(girl.prevRenderYawOffset, girl.renderYawOffset, partialTicks);
    }

    protected void onRenderSetup() {}

    protected void onRenderCleanup() {}

    float CastCameraRay(World world, Vec3d origin, float yaw, float pitch) {
        RayTraceResult result = this.rayTraceBlocks(origin, origin.add(VectorMath.rotate(new Vec3d(0.0, 0.0, -4.0), yaw, pitch)), world);
        if (result == null) {
            return 4.0f;
        }
        Vec3d hitVec = result.hitVec;
        if (hitVec == null) {
            return 4.0f;
        }
        return (float)origin.distanceTo(hitVec);
    }

    boolean isVisibleToPlayer(T entity, EntityPlayer player) {
        if (entity instanceof PlayerGirl) {
            return true;
        }
        World world = entity.world;
        Vec3d pos = entity.getPositionVector();
        float halfW = entity.width * 1.5f;
        float height = entity.height * 1.5f;
        Vec3d eyePos = player.getPositionVector().add(0.0, player.getEyeHeight(), 0.0);

        int viewMode = GirlRenderer.mc.gameSettings.thirdPersonView;
        if (viewMode != 0) {
            return true;
        }

        Vec3d[] boundingPoints = new Vec3d[]{
                pos.add(-halfW / 2.0f, 0.0, -halfW / 2.0f),
                pos.add(-halfW / 2.0f, 0.0, halfW / 2.0f),
                pos.add(halfW / 2.0f, 0.0, -halfW / 2.0f),
                pos.add(halfW / 2.0f, 0.0, halfW / 2.0f),
                pos.add(-halfW / 2.0f, height, -halfW / 2.0f),
                pos.add(-halfW / 2.0f, height, halfW / 2.0f),
                pos.add(halfW / 2.0f, height, -halfW / 2.0f),
                pos.add(halfW / 2.0f, height, halfW / 2.0f)
        };

        for (Vec3d vec3d3 : boundingPoints) {
            RayTraceResult ray = this.rayTraceBlocks(eyePos, vec3d3, world);
            if (ray == null) {
                return true;
            }
            IBlockState state = world.getBlockState(ray.getBlockPos());
            if (state.isTranslucent()) {
                return true;
            }
            if (state.getBlock().getRenderLayer() == BlockRenderLayer.SOLID) continue;
            return true;
        }
        return false;
    }

    HashSet<String> queryCustomModelParts(Boolean isSpecialState, boolean isDressed) {
        if (ClientProxy.IS_PRELOADING) {
            return new HashSet<String>();
        }
        HashSet<String> rawParts = isSpecialState != false ? ClothingGui.getSelectedPartsSet() : this.renderEntity.getCustomPartsSet();
        HashSet<String> validatedBones = new HashSet<String>();
        for (String partKey : rawParts) {
            CustomModel.ModelData modelPart = CustomModel.getModelDataForGirl(partKey);
            if (modelPart == null || !modelPart.isDisabled() && isDressed) continue;
            validatedBones.addAll(modelPart.getCustomPartBones());
        }
        return validatedBones;
    }

    //a
    //render
    @Override
    public void render(GeoModel model, T entity, float partialTicks, float r, float g, float b, float a) {
        if (GirlRenderer.mc.player != null
                && !entity.isLocallyRegistered()
                && entity.isInteractable()
                && !this.isVisibleToPlayer(entity, GirlRenderer.mc.player)
        ) {
            return;
        }

        GlStateManager.enableRescaleNormal();
        this.renderEarly(entity, partialTicks, r, g, b, a);
        this.renderLate(entity, partialTicks, r, g, b, a);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
        this.activeCustomPartBones.clear();
        this.activeCustomPartBones = this.queryCustomModelParts(entity.isLocallyRegistered(), entity.getOutfitIndex() == 0);
        this.onRenderSetup();
        BoneDeformProcessor.preWarmFilterCache(entity.getAnimationProcessor().getModelRendererList(), this.getBlacklistedBoneNames(), this);
        BoneDeformProcessor.updateGlobalInfluence(entity, partialTicks);
        this.renderModelBuffer(model, buffer, entity, r, g, b, a, partialTicks);
        this.renderAfter(entity, partialTicks, r, g, b, a);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GL20.glUseProgram(0);
    }


    protected void renderModelBuffer(GeoModel model, BufferBuilder buffer, T entity, float r, float g, float b, float a, float partialTicks) {
        GeoBone steveSkinBone = null;
        for (GeoBone bone : model.topLevelBones) {
            if (bone.getName().equals("steve")) {
                steveSkinBone = bone;
                continue;
            }
            this.renderRecursively(buffer, bone, r, g, b, a);
        }

        Tessellator.getInstance().draw();
        this.onRenderCleanup();

        if (steveSkinBone != null) {
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            try {
                Minecraft.getMinecraft().renderEngine.bindTexture(this.getGoblinTexture(this.renderEntity));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.renderRecursively(buffer, steveSkinBone, r, g, b, this.renderEntity.getRenderScaleFactor());
            Tessellator.getInstance().draw();
        }
    }

    // OLD_TODO was this method ever referenced? It appears unused //RukoBlood: nope. Even gemini ignored it.
    @CheckReturnValue
    String loadTextResource(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String string2;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(string));
            while ((string2 = bufferedReader.readLine()) != null) {
                stringBuilder.append(string2).append("//\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    protected void renderNameTag(double x, double y, double z) {
        if (this.renderEntity.isLocallyRegistered()) {
            return;
        }
        if (this.renderEntity.getCurrentAction().hideNameTag) {
            return;
        }
        if (GirlRenderer.mc.getRenderManager().renderViewEntity == null) {
            return;
        }
        this.renderLivingLabel(this.renderEntity, this.renderEntity.getDisplayNameText(), x, y + (double) this.renderEntity.getScaleFactor(), z, 300);
    }

    Vec3d getRidingPassengerVector(EntityPlayer owner, float partialTicks) {
        EntityLiving mount = (EntityLiving)owner.getRidingEntity();
        EntityPlayerSP playerClient = GirlRenderer.mc.player;
        //is this crashes?
        //assert mount != null;
        Vec3d lookVec = mount.getLookVec();
        Vec3d ownerInterp = RotationHelper.LerpVec3d(new Vec3d(owner.lastTickPosX, owner.lastTickPosY, owner.lastTickPosZ), owner.getPositionVector(), partialTicks);
        Vec3d clientInterp = RotationHelper.LerpVec3d(new Vec3d(playerClient.lastTickPosX, playerClient.lastTickPosY, playerClient.lastTickPosZ), playerClient.getPositionVector(), partialTicks);
        clientInterp = ownerInterp.subtract(clientInterp);
        this.renderEntity.renderYawOffset = mount.renderYawOffset;
        return new Vec3d(clientInterp.x + lookVec.x * -0.5, clientInterp.y + (double)0.15f, clientInterp.z + lookVec.z * -0.5);
    }

    protected Vec3d applyCustomTranslationOffsets(T entity, float partialTicks, Vec3d baseVector) {
        return baseVector;
    }

    Vec3d calculateInterpolatedPosition(T entity, float partialTicks, double x, double y, double z) {
        float yaw;
        EntityPlayer owner;
        Vec3d basePos = new Vec3d(x, y, z);
        if (entity.world instanceof FakeWorld) {
            return basePos;
        }
        if (entity.shouldRenderNameTag()
                && (!(entity instanceof PlayerGirl)
                || GirlRenderer.mc.gameSettings.thirdPersonView != 0)) {
            this.renderNameTag(x, y, z);
        }

        if ((owner = entity.getMasterPlayer()) != null
                && owner.isRiding() && owner.getRidingEntity() instanceof EntityHorse
                && ((EntityHorse)owner.getRidingEntity()).isHorseSaddled()) {
            return this.getRidingPassengerVector(owner, partialTicks);
        }

        if (!entity.isAnchored()) {
            return basePos;
        }

        if (!(entity instanceof PlayerGirl) || !((PlayerGirl)entity).hasOwnerUUID() || GirlRenderer.mc.gameSettings.thirdPersonView == 0) {
            Vec3d clientPlayerPos = RotationHelper.LerpVec3d(new Vec3d(GirlRenderer.mc.player.lastTickPosX, GirlRenderer.mc.player.lastTickPosY, GirlRenderer.mc.player.lastTickPosZ), GirlRenderer.mc.player.getPositionVector(), partialTicks);
            basePos = entity.getTargetPosition().subtract(clientPlayerPos);
        }
        entity.rotationYaw = yaw = entity.getYawRotation();
        entity.prevRenderYawOffset = yaw;
        entity.renderYawOffset = yaw;
        entity.prevRotationYawHead = yaw;
        entity.rotationYawHead = yaw;
        return basePos;
    }

    protected void preRenderCallback(T entity) {}

    //a
    //doRender
    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        float wrappedYaw;
        this.renderEntity = entity;
        Vec3d finalPos = this.calculateInterpolatedPosition(entity, partialTicks, x, y, z);
        finalPos = this.applyCustomTranslationOffsets(entity, partialTicks, finalPos);
        x = finalPos.x;
        y = finalPos.y;
        z = finalPos.z;

        this.preRenderCallback(entity);
        if (entity.getLeashed()) {
            this.renderLeashConnection(entity, x, y + this.leashYOffset, z, partialTicks);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        boolean isSitting = entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit();
        EntityModelData modelData = new EntityModelData();
        modelData.isSitting = isSitting;
        modelData.isChild = entity.isChild();

        float renderYaw = Interpolations.lerpYaw(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float headYaw = Interpolations.lerpYaw(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float netHeadYaw = headYaw - renderYaw;

        if (isSitting && entity.getRidingEntity() instanceof EntityLivingBase) {
            EntityLivingBase rider = (EntityLivingBase) entity.getRidingEntity();
            renderYaw = Interpolations.lerpYaw(rider.prevRenderYawOffset, rider.renderYawOffset, partialTicks);
            netHeadYaw = headYaw - renderYaw;
            wrappedYaw = MathHelper.wrapDegrees(netHeadYaw);
            if (wrappedYaw < -85.0f) {
                wrappedYaw = -85.0f;
            }
            if (wrappedYaw >= 85.0f) {
                wrappedYaw = 85.0f;
            }
            renderYaw = headYaw - wrappedYaw;
            if (wrappedYaw * wrappedYaw > 2500.0f) {
                renderYaw += wrappedYaw * 0.2f;
            }
            netHeadYaw = headYaw - renderYaw;
        }

        float pitch = Interpolations.lerp(entity.prevRotationPitch, entity.rotationPitch, partialTicks);
        wrappedYaw = this.handleRotationFloat(entity, partialTicks);
        //this.b(t, f3, f4, f2);
        this.applyRotations(entity, wrappedYaw, renderYaw, partialTicks);
        float limbSwingAmount = 0.0f;
        float limbSwing = 0.0f;

        if (!isSitting && entity.isEntityAlive()) {
            limbSwingAmount = Interpolations.lerp(entity.prevLimbSwingAmount, entity.limbSwingAmount, partialTicks);
            limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
            if (entity.isChild()) {
                limbSwing *= 3.0f;
            }
            if (limbSwingAmount > 1.0f) {
                limbSwingAmount = 1.0f;
            }
        }

        modelData.headPitch = -pitch;
        modelData.netHeadYaw = -netHeadYaw;

        AnimationEvent<T> animEvent = new AnimationEvent<T>(entity, limbSwing, limbSwingAmount, partialTicks, !(limbSwingAmount > -0.15f) || !(limbSwingAmount < 0.15f), Collections.singletonList(modelData));
        GeoModelProvider provider = super.getGeoModelProvider();
        ResourceLocation modelLocation = provider.getModelLocation(entity);

        GeoModel geoModel = provider.getModel(modelLocation);

        if (provider instanceof IAnimatableModel) {
            ((IAnimatableModel) provider).setLivingAnimations(entity, entity.getUniqueID().hashCode(), animEvent);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.01f, 0.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(this.getEntityTexture(entity));

        software.bernie.geckolib3.core.util.Color renderColor = this.getRenderColor(entity, partialTicks);

        boolean hasBrightness = this.setDoRenderBrightness(entity, partialTicks);

        //this.a(geoModel, t, f2, (float)color.getRed() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getAlpha() / 255.0f);
        this.render(geoModel, entity, partialTicks,
                (float)renderColor.getRed() / 255.0f,
                (float)renderColor.getBlue() / 255.0f,
                (float)renderColor.getGreen() / 255.0f,
                (float)renderColor.getAlpha() / 255.0f
        );

        if (hasBrightness) {
            RenderHurtColor.unset();
        }
        for (GeoLayerRenderer layer : this.layerRenderers) {
            layer.render(entity, limbSwing, limbSwingAmount, partialTicks, limbSwing, netHeadYaw, pitch, renderColor);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

        this.updateModelMatrices(entity);
        CustomModelRenderer.renderGirlCustomParts(entity, partialTicks);

        Vector3fSexmodSpecial additionalOverlayColor = this.getAdditionalOverlayColor(entity);
        if (additionalOverlayColor != null) {
            this.renderAdditionalOverlays(entity, partialTicks, additionalOverlayColor);
        }
    }

    void updateModelMatrices(T entity) {
        ArrayList<String> bonesToTrack = new ArrayList<String>(GirlModel.CAMERA_PLACEMENTS);
        bonesToTrack.addAll(entity.boneTrackingList);

        for (String boneName : bonesToTrack) {
            MatrixStack matrixStack = entity.getBoneMatrixStack(boneName, !entity.isLocallyRegistered());
            Matrix4f m = matrixStack.getModelMatrix();
            Vec3d translatedVec = new Vec3d(-m.m03, m.m13, -m.m23);
            entity.setBoneWorldPosition(boneName, translatedVec);
        }
    }

    @Nullable
    protected Vector3fSexmodSpecial getAdditionalOverlayColor(T entity) {
        return null;
    }

    public Entity resolveTargetEntity(GirlEntity girl) {
        return girl;
    }

    void renderAdditionalOverlays(GirlEntity girl, float partialTicks, Vector3fSexmodSpecial rgbColor) {
        EntityPlayerSP entityPlayerSP = GirlRenderer.mc.player;
        rgbColor = new Vector3fSexmodSpecial(rgbColor.x / 255.0f, rgbColor.y / 255.0f, rgbColor.z / 255.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 0.01, 0.0);
        Entity resolvedEntity = this.resolveTargetEntity(girl);

        Vec3d interpTarget = girl.isAnchored() ? girl.getTargetPosition() : RotationHelper.LerpVec3d(new Vec3d(resolvedEntity.lastTickPosX, resolvedEntity.lastTickPosY, resolvedEntity.lastTickPosZ), resolvedEntity.getPositionVector(), partialTicks);
        Vec3d interpClient = RotationHelper.LerpVec3d(new Vec3d(entityPlayerSP.lastTickPosX, entityPlayerSP.lastTickPosY, entityPlayerSP.lastTickPosZ), entityPlayerSP.getPositionVector(), partialTicks);
        Vec3d relativeVector = interpTarget.subtract(interpClient);
        GlStateManager.translate(relativeVector.x, relativeVector.y, relativeVector.z);
        mc.getTextureManager().bindTexture(LINE);

        float thickness = GirlRenderer.calculateLineThickness(girl, partialTicks, 1.0f, 5.0f);
        this.drawOverlayLines(tessellator, buffer, girl, rgbColor, thickness);
        GlStateManager.popMatrix();
    }

    protected static float calculateLineThickness(GirlEntity girl, float partialTicks, float min, float max) {
        EntityPlayerSP player = GirlRenderer.mc.player;
        Entity target = ((GirlRenderer) mc.getRenderManager().getEntityRenderObject(girl)).resolveTargetEntity(girl);
        Vec3d interpTarget = girl.isAnchored() ? girl.getTargetPosition() : RotationHelper.LerpVec3d(new Vec3d(target.lastTickPosX, target.lastTickPosY, target.lastTickPosZ), target.getPositionVector(), partialTicks);
        Vec3d interpClient = RotationHelper.LerpVec3d(new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ), player.getPositionVector(), partialTicks);
        Vec3d cameraPos = ActiveRenderInfo.getCameraPosition().add(interpClient);
        float distance = (float)cameraPos.distanceTo(interpTarget);
        float ratio = Math.abs(distance) / 5.0f;
        return RotationHelper.LerpFloat(max, min, ThreadNames.clamp(ratio, 0.0f, 1.0f));
    }

    protected void drawOverlayLines(Tessellator tessellator, BufferBuilder buffer, GirlEntity girl, Vector3fSexmodSpecial rgb, float thickness) {
    }

    protected static void drawLineBetweenBones(BufferBuilder buffer, Tessellator tessellator, GirlEntity girl, String startBone, String endBone, float r, float g, float b, float thickness) {
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.glLineWidth(thickness);
        Vec3d posStart = girl.getCachedBoneOffset(startBone);
        Vec3d posEnd = girl.getCachedBoneOffset(endBone);
        buffer.pos(posStart.x, posStart.y, posStart.z).tex(0.0, 0.0).color(r, g, b, 1.0f).endVertex();
        buffer.pos(posEnd.x, posEnd.y, posEnd.z).tex(0.0, 0.0).color(r, g, b, 1.0f).endVertex();
        tessellator.draw();
    }

    protected static void drawCustomOverlayBundle(Tessellator tessellator, BufferBuilder buffer, GirlEntity girl, Vector3fSexmodSpecial rgb, float th) {
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidStartR", "braStringMidMid1R", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidMid1R", "braStringMidMid2R", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidMid2R", "braStringMidMid3R", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidMid3R", "braStringMidEndR", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidEndR", "braStringBackR", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringBackR", "braStringRightEndR", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringRightEndR", "braStringRightStartR", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringRightR", "braStringRightL", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidStartL", "braStringMidMid1L", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidMid1L", "braStringMidMid2L", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidMid2L", "braStringMidMid3L", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidMid3L", "braStringMidEndL", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringMidEndL", "braStringBackL", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringBackL", "braStringLeftEndL", rgb.x, rgb.y, rgb.z, th);
        GirlRenderer.drawLineBetweenBones(buffer, tessellator, girl, "braStringLeftEndL", "braStringLeftStartL", rgb.x, rgb.y, rgb.z, th);
    }

    //b
    //protected void b(T t, float f, float f2, float f3) {
    // applyRotations
    @Override
    protected void applyRotations(T entity, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
        if (!(entity instanceof PlayerGirl)) {
            return;
        }

        UUID OwnerUUID = ((PlayerGirl)entity).getOwnerUserUUID();
        if (OwnerUUID == null) {
            return;
        }
        EntityPlayer owner = entity.world.getPlayerEntityByUUID(OwnerUUID);
        if (owner == null) {
            return;
        }
        if (!owner.isElytraFlying()) {
            return;
        }

        float flyingTicks = (float)owner.getTicksElytraFlying() + partialTicks;
        float pitchFactor = MathHelper.clamp(flyingTicks * flyingTicks / 100.0f, 0.0f, 1.0f);
        GlStateManager.rotate(pitchFactor * (-90.0f - owner.rotationPitch), 1.0f, 0.0f, 0.0f);

        Vec3d lookVec = owner.getLook(partialTicks);
        double horizontalMotion = owner.motionX * owner.motionX + owner.motionZ * owner.motionZ;
        double horizontalLook = lookVec.x * lookVec.x + lookVec.z * lookVec.z;
        if (horizontalMotion > 0.0 && horizontalLook > 0.0) {
            double dotProduct = (owner.motionX * lookVec.x + owner.motionZ * lookVec.z) / (Math.sqrt(horizontalMotion) * Math.sqrt(horizontalLook));
            double crossProduct = owner.motionX * lookVec.z - owner.motionZ * lookVec.x;
            GlStateManager.rotate((float)(Math.signum(crossProduct) * Math.acos(dotProduct)) * 180.0f / (float)Math.PI, 0.0f, 1.0f, 0.0f);
        }
    }

    protected void onBoneProcessing(BufferBuilder buffer, String boneName, GeoBone bone) {
    }
    protected void renderLeashConnection(GirlEntity girl, double d, double d2, double d3, float f) {
        float f2;
        float f3;
        float f4;
        float f5;
        int n;
        Entity entity = girl.getLeashHolder();
        d2 -= (1.6 - (double)girl.height) * 0.5;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        double d4 = (double) RotationHelper.LerpFloat(entity.prevRotationYaw, entity.rotationYaw, f * 0.5f) * 0.01745329238474369;
        double d5 = (double) RotationHelper.LerpFloat(entity.prevRotationPitch, entity.rotationPitch, f * 0.5f) * 0.01745329238474369;
        double d6 = Math.cos(d4);
        double d7 = Math.sin(d4);
        double d8 = Math.sin(d5);
        if (entity instanceof EntityHanging) {
            d6 = 0.0;
            d7 = 0.0;
            d8 = -1.0;
        }
        double d9 = Math.cos(d5);
        double d10 = RotationHelper.LerpDouble(entity.prevPosX, entity.posX, f) - d6 * 0.7 - d7 * 0.5 * d9;
        double d11 = RotationHelper.LerpDouble(entity.prevPosY + (double)entity.getEyeHeight() * 0.7, entity.posY + (double)entity.getEyeHeight() * 0.7, f) - d8 * 0.5 - 0.25;
        double d12 = RotationHelper.LerpDouble(entity.prevPosZ, entity.posZ, f) - d7 * 0.7 + d6 * 0.5 * d9;
        double d13 = (double) RotationHelper.LerpFloat(girl.prevRenderYawOffset, girl.renderYawOffset, f) * 0.01745329238474369 + 1.5707963267948966;
        d6 = Math.cos(d13) * (double)girl.width * 0.4;
        d7 = Math.sin(d13) * (double)girl.width * 0.4;
        double d14 = RotationHelper.LerpDouble(girl.prevPosX, girl.posX, f) + d6;
        double d15 = RotationHelper.LerpDouble(girl.prevPosY, girl.posY, f);
        double d16 = RotationHelper.LerpDouble(girl.prevPosZ, girl.posZ, f) + d7;
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
    public void renderRecursively(BufferBuilder buffer, GeoBone bone, float red, float green, float blue, float alpha) {
        if (this.renderEntity.world instanceof FakeWorld) {
            return;
        }
        String boneName = bone.getName();
        if (boneName.equals("weapon") && this.renderEntity instanceof Fighter) {
            this.RenderHeldItem(buffer, bone);
        }
        if (boneName.equals("itemRenderer") && this.renderEntity.getCurrentAction() == Action.PAYMENT) {
            this.renderTradeOverlay(buffer, bone);
        }
        if (boneName.equals("ballL") || boneName.equals("ballR") || boneName.equals("cock")) {
            alpha = 1.0f;
        }
        tempBuffer = buffer;
        this.onBoneProcessing(buffer, boneName, bone);

        MATRIX_STACK.push();
        MATRIX_STACK.translate(bone);
        MATRIX_STACK.moveToPivot(bone);
        MATRIX_STACK.rotate(bone);
        MATRIX_STACK.scale(bone);
        MATRIX_STACK.moveBackFromPivot(bone);

        if ("Head2".equals(boneName) && !this.shouldRender()) {
            MATRIX_STACK.pop();
            return;
        }
        if (!this.isBoneAllowedForRender(boneName)) {
            MATRIX_STACK.pop();
            return;
        }
        if (!bone.isHidden) {
            Vector4f colorVector = this.calculateBoneArmorColor(boneName, red, green, blue);
            red = colorVector.x;
            green = colorVector.y;
            blue = colorVector.z;
            double uOffset = colorVector.w;

            if (!this.activeCustomPartBones.contains(boneName)) {
                for (GeoCube cube : bone.childCubes) {
                    MATRIX_STACK.push();
                    this.currentRenderingBone = bone;
                    this.renderCubeGeometry(buffer, cube, red, green, blue, alpha, uOffset);
                    MATRIX_STACK.pop();
                }
            }

            for (GeoBone child : bone.childBones) {
                if (uOffset == 0.0) {
                    this.renderRecursively(buffer, child, red, green, blue, alpha);
                    continue;
                }
                this.renderCustomBones(buffer, child, red, green, blue, alpha, uOffset);
            }
        }
        MATRIX_STACK.pop();
    }

    protected Vector4f getBaseColorVector(float r, float g, float b) {
        return new Vector4f(r, g, b, 0.0f);
    }

    boolean isBoneAllowedForRender(String boneName) {
        if (!boneName.startsWith("armor")) {
            return true;
        }
        return this.renderEntity instanceof Fighter;
    }

    protected Vector4f calculateBoneArmorColor(String boneName, float r, float g, float b) {
        if (!boneName.startsWith("armor")) {
            return this.getBaseColorVector(r, g, b);
        }
        if (!(this.renderEntity instanceof Fighter)) {
            return this.getBaseColorVector(r, g, b);
        }
        if (this.renderEntity.entityDataManager.get(GirlEntity.OUTFIT_INDEX) == 0) {
            return this.getBaseColorVector(r, g, b);
        }
        GeoModelProvider provider = this.getGeoModelProvider();
        if (!(provider instanceof GirlModel)) {
            return this.getBaseColorVector(r, g, b);
        }
        GirlModel girlModel = (GirlModel)provider;
        ItemStack armorStack = girlModel.getArmorStackForBone(this.renderEntity, boneName);
        if (!(armorStack.getItem() instanceof ItemArmor)) {
            return this.getBaseColorVector(r, g, b);
        }

        ItemArmor armor = (ItemArmor)armorStack.getItem();
        ItemArmor.ArmorMaterial material = armor.getArmorMaterial();

        float materialIdOffset = 0.0f;
        switch (material) {
            case GOLD: {
                materialIdOffset = 1.0f;
                break;
            }
            case CHAIN: 
            case IRON: {
                materialIdOffset = 2.0f;
                break;
            }
            case LEATHER: {
                materialIdOffset = 4.0f;
                int colorRGB = armor.getColor(armorStack);
                float fR = (float)(colorRGB >> 16 & 0xFF) / 255.0f;
                float fG = (float)(colorRGB >> 8 & 0xFF) / 255.0f;
                float fB = (float)(colorRGB & 0xFF) / 255.0f;
                r *= fR;
                g *= fG;
                b *= fB;
            }
        }
        return new Vector4f(r, g, b, 72.0f * materialIdOffset / 4096.0f);
    }

    //a
    //renderEarly
    @Override
    public void renderEarly(T entity, float ticks, float red, float green, float blue, float a) {
        this.globalModelMatrix = (Matrix4f)MATRIX_STACK.getModelMatrix().clone();
    }

    public void renderCustomBones(BufferBuilder buffer, GeoBone bone, float r, float g, float b, float a, double uOffset) {
        if (this.renderEntity.world instanceof FakeWorld) {
            return;
        }

        String boneName = bone.getName();
        if (boneName.equals("weapon")) {
            this.RenderHeldItem(buffer, bone);
        }
        if (boneName.equals("ballL") || boneName.equals("ballR") || boneName.equals("cock")) {
            a = 1.0f;
        }

        this.onBoneProcessing(buffer, bone.getName(), bone);
        MATRIX_STACK.push();
        MATRIX_STACK.translate(bone);
        MATRIX_STACK.moveToPivot(bone);
        MATRIX_STACK.rotate(bone);
        MATRIX_STACK.scale(bone);
        MATRIX_STACK.moveBackFromPivot(bone);

        if (!bone.isHidden) {
            if (!this.activeCustomPartBones.contains(boneName)) {
                for (GeoCube cube : bone.childCubes) {
                    MATRIX_STACK.push();
                    GlStateManager.pushMatrix();
                    this.currentRenderingBone = bone;
                    this.renderCubeGeometry(buffer, cube, r, g, b, a, uOffset);
                    GlStateManager.popMatrix();
                    MATRIX_STACK.pop();
                }
            }

            for (GeoBone child : bone.childBones) {
                this.renderCustomBones(buffer, child, r, g, b, a, uOffset);
            }
        }
        MATRIX_STACK.pop();
    }

    @CheckReturnValue
    protected boolean shouldRender() {
        return !this.renderEntity.isControlledByLocalPlayer() || GirlRenderer.mc.gameSettings.thirdPersonView != 0;
    }

    public void renderCubeGeometry(BufferBuilder buffer, GeoCube cube, float r, float g, float b, float a, double uOffset) {
        MATRIX_STACK.moveToPivot(cube);
        MATRIX_STACK.rotate(cube);
        MATRIX_STACK.moveBackFromPivot(cube);

        for (GeoQuad quad : cube.quads) {
            if (quad != null) {
                javax.vecmath.Vector3f normal = new javax.vecmath.Vector3f((float) quad.normal.getX(), (float) quad.normal.getY(), (float) quad.normal.getZ());

                MATRIX_STACK.getNormalMatrix().transform(normal);
                if ((cube.size.y == 0.0f || cube.size.z == 0.0f) && normal.getX() < 0.0f) {
                    normal.x *= -1.0f;
                }
                if ((cube.size.x == 0.0f || cube.size.z == 0.0f) && normal.getY() < 0.0f) {
                    normal.y *= -1.0f;
                }
                if ((cube.size.x == 0.0f || cube.size.y == 0.0f) && normal.getZ() < 0.0f) {
                    normal.z *= -1.0f;
                }

                Vec3d defColor = BoneDeformProcessor.applyBoneDeformation(this, this.currentRenderingBone, new Vec3d(r, g, b), normal);
                for (GeoVertex vertex : quad.vertices) {
                    Vector4f vertexPos = new Vector4f(vertex.position.getX(), vertex.position.getY(), vertex.position.getZ(), 1.0f);
                    MATRIX_STACK.getModelMatrix().transform(vertexPos);
                    buffer.pos(vertexPos.getX(), vertexPos.getY(), vertexPos.getZ()).tex((double) vertex.textureU + uOffset, vertex.textureV).color((float) defColor.x, (float) defColor.y, (float) defColor.z, a).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
                }
            }
        }
    }

    @CheckReturnValue
    protected ItemStack resolveTradePaymentItemStack() {
        switch (this.renderEntity.entityDataManager.get(GirlEntity.GIRL_HAND_STATES)) {
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

    protected void renderTradeOverlay(BufferBuilder buffer, GeoBone bone) {
        ItemStack paymentStack = this.resolveTradePaymentItemStack();
        if (paymentStack == null) {
            return;
        }

        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();

        for (int i = 0; i < paymentStack.getCount(); ++i) {
            GlStateManager.pushMatrix();
            Tessellator.getInstance().draw();
            MatrixHelper.bindOpenGLToBone(IGeoRenderer.MATRIX_STACK, bone);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glRotated((double)bone.getRotationX() + 2.5, 0.0, 0.0, 1.0);
            GL11.glRotated(bone.getRotationY(), 0.0, 1.0, 0.0);
            GL11.glRotated(bone.getRotationZ(), 1.0, 0.0, 0.0);
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
            GlStateManager.scale(this.renderEntity.scaleFactor, this.renderEntity.scaleFactor, this.renderEntity.scaleFactor);
            itemRenderer.renderItem(this.renderEntity, new ItemStack(paymentStack.getItem(), 1), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            GlStateManager.popMatrix();
        }
    }

    @CheckReturnValue
    protected ItemStack getHeldItem(@Nullable ItemStack input) {
        return input;
    }

    protected void RenderHeldItem(BufferBuilder buffer, GeoBone bone) {
        if (this.renderEntity == null) {
            return;
        }
        if (!(this.renderEntity instanceof Fighter)) {
            return;
        }
        EntityDataManager manager = this.renderEntity.getDataManager();
        Fighter fighter = (Fighter)this.renderEntity;
        int activeSlot = manager.get(Fighter.ATTACK_MODE);
        if (fighter.getCurrentAction() != Action.BOW) {
            this.bowPullProgressNotPlayer = 0.0f;
        }
        ItemStack weaponStack = null;
        if (activeSlot == 1) {
            weaponStack = manager.get(Fighter.WEAPON);
        } else if (activeSlot == 2) {
            weaponStack = manager.get(Fighter.BOW);
        }
        weaponStack = this.getHeldItem(weaponStack);
        if (weaponStack == null) {
            return;
        }
        if (weaponStack.getItem().equals(Items.BOW) && fighter.getCurrentAction() == Action.BOW) {
            this.bowPullProgressNotPlayer += 0.015f;
            fighter.setItemUseCount(Math.round(-this.bowPullProgressNotPlayer * 20.0f + (float)weaponStack.getMaxItemUseDuration()));
            fighter.setHeldItemOverride(weaponStack);
        }
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        MatrixHelper.bindOpenGLToBone(MATRIX_STACK, bone);
        GL11.glEnable(2896);
        if (weaponStack.getItem() instanceof ItemBow) {
            GL11.glRotatef((float)fighter.holdBowRot, 1.0f, 0.0f, 0.0f);
        } else if (fighter.getCurrentAction() == Action.ATTACK && fighter.nextAttack == 0) {
            GlStateManager.translate(fighter.swordOffsetStab.x, fighter.swordOffsetStab.y, fighter.swordOffsetStab.z);
            GL11.glRotatef((float)fighter.stabSwordRot, 1.0f, 0.0f, 0.0f);
        } else {
            GL11.glRotatef((float)fighter.slashSwordRot, 1.0f, 0.0f, 0.0f);
        }
        Minecraft.getMinecraft().getItemRenderer().renderItem(this.renderEntity, weaponStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.renderEntity)));
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
    }

    RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end, World world) {
        if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
            return null;
        }
        if (Double.isNaN(end.x) || Double.isNaN(end.y) || Double.isNaN(end.z)) {
            return null;
        }

        int endX = MathHelper.floor(end.x);
        int endY = MathHelper.floor(end.y);
        int endZ = MathHelper.floor(end.z);
        int startX = MathHelper.floor(start.x);
        int startY = MathHelper.floor(start.y);
        int startZ = MathHelper.floor(start.z);

        //gay stripping. Bad dev.
        //BlockPos checkPos = new BlockPos(startX, startY = MathHelper.floor(start.y), startZ = MathHelper.floor(start.z));
        BlockPos checkPos = new BlockPos(startX, startY, startZ);
        IBlockState state = world.getBlockState(checkPos);
        if (state.getCollisionBoundingBox(world, checkPos) != Block.NULL_AABB && state.getBlock().getRenderLayer() == BlockRenderLayer.SOLID) {
            return state.collisionRayTrace(world, checkPos, start, end);
        }

        int steps = 200;
        while (steps-- >= 0) {
            IBlockState nextState;
            EnumFacing side;

            if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                return null;
            }
            if (startX == endX && startY == endY && startZ == endZ) {
                return null;
            }

            boolean stepX = true; boolean stepY = true; boolean stepZ = true;
            double limitX = 999.0; double limitY = 999.0; double limitZ = 999.0;

            if (endX > startX) {
                limitX = (double)startX + 1.0;
            } else if (endX < startX) {
                limitX = (double)startX + 0.0;
            } else {
                stepX = false;
            }
            if (endY > startY) {
                limitY = (double)startY + 1.0;
            } else if (endY < startY) {
                limitY = (double)startY + 0.0;
            } else {
                stepY = false;
            }
            if (endZ > startZ) {
                limitZ = (double)startZ + 1.0;
            } else if (endZ < startZ) {
                limitZ = (double)startZ + 0.0;
            } else {
                stepZ = false;
            }

            double factorX = 999.0;
            double factorY = 999.0;
            double factorZ = 999.0;
            double d7 = end.x - start.x;
            double d8 = end.y - start.y;
            double d9 = end.z - start.z;

            if (stepX) {
                factorX = (limitX - start.x) / d7;
            }
            if (stepY) {
                factorY = (limitY - start.y) / d8;
            }
            if (stepZ) {
                factorZ = (limitZ - start.z) / d9;
            }

            if (factorX == -0.0) {
                factorX = -1.0E-4;
            }
            if (factorY == -0.0) {
                factorY = -1.0E-4;
            }
            if (factorZ == -0.0) {
                factorZ = -1.0E-4;
            }

            if (factorX < factorY && factorX < factorZ) {
                side = endX > startX ? EnumFacing.WEST : EnumFacing.EAST;
                start = new Vec3d(limitX, start.y + d8 * factorX, start.z + d9 * factorX);
            } else if (factorY < factorZ) {
                side = endY > startY ? EnumFacing.DOWN : EnumFacing.UP;
                start = new Vec3d(start.x + d7 * factorY, limitY, start.z + d9 * factorY);
            } else {
                side = endZ > startZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                start = new Vec3d(start.x + d7 * factorZ, start.y + d8 * factorZ, limitZ);
            }

            if ((nextState = world.getBlockState(checkPos = new BlockPos(startX = MathHelper.floor(start.x) - (side == EnumFacing.EAST ? 1 : 0), startY = MathHelper.floor(start.y) - (side == EnumFacing.UP ? 1 : 0), startZ = MathHelper.floor(start.z) - (side == EnumFacing.SOUTH ? 1 : 0)))).getMaterial() != Material.PORTAL && nextState.getCollisionBoundingBox(world, checkPos) == Block.NULL_AABB || nextState.getBlock().getRenderLayer() != BlockRenderLayer.SOLID) {
                return nextState.collisionRayTrace(world, checkPos, start, end);
            }
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
        skinTextureCache = new HashMap();
    }
}

