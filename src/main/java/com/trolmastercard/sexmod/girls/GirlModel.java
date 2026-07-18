/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.util.Reference;
import com.trolmastercard.sexmod.util.TrigMath;
import com.trolmastercard.sexmod.util.interfaces.IGirlAnimGeoModel;
import com.trolmastercard.sexmod.util.interfaces.IGirlModelParts;
import com.trolmastercard.sexmod.world.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.CheckReturnValue;

public abstract class GirlModel<T extends GirlEntity>
extends IGirlAnimGeoModel<T>
implements IGirlModelParts {
    final static public List<String> b = Arrays.asList("braStringMidStartR", "braStringMidMid1R", "braStringMidMid2R", "braStringMidMid3R", "braStringMidEndR", "braStringBackR", "braStringRightEndR", "braStringRightStartR", "braStringRightL", "braStringMidMid1L", "braStringMidMid2L", "braStringMidMid3L", "braStringMidEndL", "braStringBackL", "braStringLeftEndL", "braStringLeftStartL", "braStringMidStartL", "braStringRightR");
    final static public List<String> e = Arrays.asList("boyCam", "girlCam");
    static public boolean d = true;
    protected ResourceLocation[] c = this.getAnimationResource();
    protected Minecraft a = Minecraft.getMinecraft();

    protected GirlModel() {
    }

    // TODO fix these override / names clashing synthetics
    protected abstract ResourceLocation[] getAnimationResource();

    public abstract ResourceLocation getSkinLocation();

    public abstract ResourceLocation getAnimationFileLocation(GirlEntity girlEntity);

    @Override
    public ResourceLocation getModelLocation(GirlEntity girlEntity) {
        if (girlEntity.world instanceof FakeWorld) {
            return this.c[0];
        }
        if (girlEntity.getDataManager().get(GirlEntity.D) > this.c.length) {
            System.out.println("Girl doesn't have an outfit Nr." + girlEntity.getDataManager().get(GirlEntity.D) + " so im just making her nude lol");
            return this.c[0];
        }
        return this.c[girlEntity.getDataManager().get(GirlEntity.D)];
    }

    //net_minecraft_util_ResourceLocation_g
    public ResourceLocation getTextureLocation(GirlEntity em_class2582) {
        //return this.n_et_minecraft_util_ResourceLocation_b();
        return this.getSkinLocation();
    }

    @Override
    public void setMolangQueries(IAnimatable iAnimatable, double d) {
        if (Minecraft.getMinecraft().world != null) {
            super.setMolangQueries(iAnimatable, d);
        }
    }

    //a
    @Override
    public void setLivingAnimations(T t, Integer n, AnimationEvent animationEvent) {
        super.setLivingAnimations(t, n, animationEvent);
        AnimationProcessor<T> animationProcessor = this.getAnimationProcessor();
        this.a(t, animationProcessor);
        if (t.world instanceof FakeWorld) {
            return;
        }
        if (t.getDataManager().get(GirlEntity.G)) {
            t.setPositionAndRotationDirect(t.net_minecraft_util_math_Vec3d_o().x, t.net_minecraft_util_math_Vec3d_o().y, t.net_minecraft_util_math_Vec3d_o().z, t.java_lang_Float_I(), 0.0f, 3, true);
        }
        if (t.actionController != null) {
            ((GirlEntity)t).actionController.transitionLengthTicks = t.world instanceof FakeWorld || t.currentAction() == null ? 5.0 : (double) t.currentAction().transitionTick;
        }
        this.a(t, animationProcessor, animationEvent);
        if (!(t instanceof Fighter) || t.boolean_h() || t.int_ah() == 0) {
            this.a(animationProcessor);
        } else {
            this.a(animationProcessor, t.entityDataManager.get(Fighter.ITEM_SLOT_3), t.entityDataManager.get(Fighter.ITEM_SLOT_4), t.entityDataManager.get(Fighter.ITEM_SLOT_5), t.entityDataManager.get(Fighter.ITEM_SLOT_6));
        }
    }

    @CheckReturnValue
    public static Vec3d net_minecraft_util_math_Vec3d_d(GirlEntity em_class2582) {
        return GirlModel.a(new Vec3d(em_class2582.lastTickPosX, em_class2582.lastTickPosY, em_class2582.lastTickPosZ), em_class2582.getPositionVector());
    }

    @CheckReturnValue
    public static Vec3d a(GirlEntity girlEntity, Vec3d vec3d) {
        return GirlModel.a(vec3d, girlEntity.getPositionVector());
    }

    @CheckReturnValue
    public static Vec3d a(Vec3d vec3d, Vec3d vec3d2) {
        float f;
        float f2;
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        Vec3d vec3d4 = new Vec3d(Math.abs(vec3d3.x), Math.abs(vec3d3.y), Math.abs(vec3d3.z));
        double d = vec3d4.x / (vec3d4.x + vec3d4.y + vec3d4.z);
        double d2 = vec3d4.y / (vec3d4.x + vec3d4.y + vec3d4.z);
        double d3 = vec3d4.z / (vec3d4.x + vec3d4.y + vec3d4.z);
        Vec3d vec3d5 = new Vec3d((double)(vec3d3.x > 0.0 ? 1 : -1) * d, (double)(vec3d3.y > 0.0 ? 1 : -1) * d2, (double)(vec3d3.z > 0.0 ? 1 : -1) * d3);
        double d4 = vec3d5.y / 2.0 + 0.5;
        float f3 = (float) Reference.LerpDouble(-180.0, 0.0, d4);
        if (Float.isNaN(f3)) {
            f3 = -90.0f;
        }
        float f4 = f2 = d4 < 0.5 ? 0.0f : (float) Reference.LerpDouble(0.0, 16.0, -d4);
        if (Float.isNaN(f2)) {
            f2 = 0.0f;
        }
        if (Float.isNaN(f = (float)(4.0 - Math.sin(1.5707963267948966 + d4 * 2.0 * Math.PI) * 4.0))) {
            f = 8.0f;
        }
        return new Vec3d(TrigMath.toRadians(f3), f2, f);
    }

    void a(AnimationProcessor<T> animationProcessor, ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3, ItemStack itemStack4) {
        this.c(animationProcessor, !itemStack.isEmpty());
        this.b(animationProcessor, itemStack2.getItem() instanceof ItemArmor);
        this.d(animationProcessor, !itemStack3.isEmpty());
        this.a(animationProcessor, !itemStack4.isEmpty());
    }

    protected void a(AnimationProcessor<T> animationProcessor) {
        this.c(animationProcessor, false);
        this.b(animationProcessor, false);
        this.d(animationProcessor, false);
        this.a(animationProcessor, false);
    }

    void c(AnimationProcessor<T> animationProcessor, boolean bl) {
        this.a(this.HeadArmor(), bl, animationProcessor);
        this.a(this.Attachments(), !bl, animationProcessor);
    }

    void b(AnimationProcessor<T> animationProcessor, boolean bl) {
        this.a(this.TopArmor(), bl, animationProcessor);
        this.a(this.Top(), !bl, animationProcessor);
    }

    void d(AnimationProcessor<T> animationProcessor, boolean bl) {
        this.a(this.BottomArmor(), bl, animationProcessor);
        this.a(this.Bottom(), !bl, animationProcessor);
    }

    void a(AnimationProcessor<T> animationProcessor, boolean bl) {
        this.a(this.ShoesArmor(), bl, animationProcessor);
        this.a(this.Feet(), !bl, animationProcessor);
    }

    void a(String[] stringArray, boolean bl, AnimationProcessor<T> animationProcessor) {
        for (String string : stringArray) {
            this.HideBone(string, bl, animationProcessor);
        }
    }

    void HideBone(String string, boolean bl, AnimationProcessor<T> animationProcessor) {
        if (animationProcessor.getBone(string) == null) {
            return;
        }
        animationProcessor.getBone(string).setHidden(!bl);
    }

    @CheckReturnValue
    protected boolean f(T t) {
        UUID uUID = t.getID();
        if (uUID == null) {
            return true;
        }
        World world = t.world;
        AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)world.getPlayerEntityByUUID(uUID);
        if (abstractClientPlayer == null) {
            return true;
        }
        return "default".equals(abstractClientPlayer.getSkinType());
    }

    void a(T t, AnimationProcessor<T> animationProcessor) {
        boolean bl = this.f(t);
        animationProcessor.getBone("rightArmAlex").setHidden(bl);
        animationProcessor.getBone("rightLowerArmAlex").setHidden(bl);
        animationProcessor.getBone("rightArmSteve").setHidden(!bl);
        animationProcessor.getBone("rightLowerArmSteve").setHidden(!bl);
        animationProcessor.getBone("leftArmAlex").setHidden(bl);
        animationProcessor.getBone("leftLowerArmAlex").setHidden(bl);
        animationProcessor.getBone("leftArmSteve").setHidden(!bl);
        animationProcessor.getBone("leftLowerArmSteve").setHidden(!bl);
        IBone iBone = animationProcessor.getBone("steve");
        if (iBone != null) {
            iBone.setHidden(!t.currentAction().hasPlayer);
        }
    }

    @CheckReturnValue
    protected boolean e(GirlEntity t) {
        return true;
    }

    protected void a(T t, AnimationProcessor<T> animationProcessor, AnimationEvent<T> animationEvent) {
        if (t.world instanceof FakeWorld) {
            return;
        }
        if (!this.e(t)) {
            return;
        }
        if (t.currentAction() != Action.NULL && t.currentAction() != Action.ATTACK && t.currentAction() != Action.BOW) {
            return;
        }
        EntityModelData entityModelData = animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        IBone iBone = animationProcessor.getBone("neck");
        iBone.setRotationY(entityModelData.netHeadYaw * 0.5f * ((float)Math.PI / 180));
        IBone iBone2 = animationProcessor.getBone("head");
        iBone2.setRotationY(entityModelData.netHeadYaw * ((float)Math.PI / 180));
        iBone2.setRotationX(entityModelData.headPitch * ((float)Math.PI / 180));
        IBone iBone3 = animationProcessor.getBone("body") == null ? animationProcessor.getBone("dd") : animationProcessor.getBone("body");
        iBone3.setRotationY(0.0f);
    }

    @CheckReturnValue
    public ItemStack a(GirlEntity em_class2582, String string) {
        if (Arrays.asList(this.HeadArmor()).contains(string)) {
            return em_class2582.entityDataManager.get(Fighter.ITEM_SLOT_3);
        }
        if (Arrays.asList(this.TopArmor()).contains(string)) {
            return em_class2582.entityDataManager.get(Fighter.ITEM_SLOT_4);
        }
        if (Arrays.asList(this.BottomArmor()).contains(string)) {
            return em_class2582.entityDataManager.get(Fighter.ITEM_SLOT_5);
        }
        if (Arrays.asList(this.ShoesArmor()).contains(string)) {
            return em_class2582.entityDataManager.get(Fighter.ITEM_SLOT_6);
        }
        return ItemStack.EMPTY;
    }

    //@Override
    //public void setLivingAnimations(IAnimatable iAnimatable, Integer n, AnimationEvent animationEvent) {
    //    this.a((em_class258)iAnimatable, n, animationEvent);
    //}

    //@Override
    //public void setLivingAnimations(Object object, Integer n, AnimationEvent animationEvent) {
    //    this.a((em_class258)object, n, animationEvent);
    //}

    // TODO override to accept EntityGirl
    //@Override
    //public void setLivingAnimations(GirlEntity iAnimatable, Integer n, AnimationEvent animationEvent) {
    //    this.a(iAnimatable, n, animationEvent);
    //}

    //@Override
    //public ResourceLocation getAnimationFileLocation(GirlEntity object) {
    //    return this.n_et_minecraft_util_ResourceLocation_c((GirlEntity)object);
    //}

    //@Override
    //public ResourceLocation getTextureLocation(GirlEntity object) {
    //    return this.net_minecraft_util_ResourceLocation_g((GirlEntity)object);
    //}

    //@Override
    //public ResourceLocation getModelLocation(GirlEntity object) {
    //    return this.net_minecraft_util_ResourceLocation_a((GirlEntity)object);
    //}
}

