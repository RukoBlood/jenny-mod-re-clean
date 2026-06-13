/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.girls.Galath;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.GirlModel;
import com.trolmastercard.sexmod.girls.Mangelie.ManglelieModel;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

public class GalathModel
extends GirlModel<GirlEntity> {
    static public ResourceLocation h = new ResourceLocation("sexmod", "textures/entity/galath/galath.png");
    float g = 0.0f;
    long f = -1L;
    long i = -1L;

    public GalathModel() {
        this.c = this.getAnimationResource();
    }

    @Override
    protected ResourceLocation[] getAnimationResource() {
        return new ResourceLocation[]{
                new ResourceLocation("sexmod", "geo/galath/galath.geo.json"),
                new ResourceLocation("sexmod", "geo/galath/galath.geo.json"),
                new ResourceLocation("sexmod", "geo/galath/galath_con_mang.geo.json")};
    }

    @Override
    public ResourceLocation getModelLocation(GirlEntity girlEntity) {
        if (girlEntity.world instanceof FakeWorld) {
            return this.c[0];
        }
        if (((b7_class68)((Object) girlEntity)).boolean_b()) {
            return this.c[2];
        }
        return this.c[girlEntity.getDataManager().get(GirlEntity.D)];
    }

    @Override
    public ResourceLocation getSkinLocation() {
        return h;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GirlEntity girlEntity) {
        return new ResourceLocation("sexmod", "animations/galath/galath.animation.json");
    }

    protected boolean boolean_e(GirlEntity em_class2582) {
        if (!(em_class2582 instanceof GalathEntity)) {
            return true;
        }
        GalathEntity f__class2972 = (GalathEntity)em_class2582;
        if (f__class2972.maybeMountedByMangFn()) {
            return true;
        }
        return f__class2972.net_minecraft_entity_EntityLivingBase_M() == null;
    }

    @Override
    public void setLivingAnimations(GirlEntity em_class2582, Integer n, AnimationEvent animationEvent) {
        this.k(em_class2582);
        super.setLivingAnimations(em_class2582, n, animationEvent);
        this.void_a(em_class2582);
        this.h(em_class2582);
        //this.f(em_class2582); // TODO
        assert(false);
        this.void_b(em_class2582);
        this.void_e(em_class2582);
        this.void_g(em_class2582);
        this.j(em_class2582);
        this.ShowFutaCock();
        this.void_c(em_class2582);
        this.i(em_class2582);
        this.void_d(em_class2582);
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        GalathEntity f__class2972 = (GalathEntity)em_class2582;
        f__class2972.aE = this.getAnimationProcessor().getBone("head").getRotationX();
        if (f__class2972.boolean_b()) {
            ManglelieModel.a(f__class2972, this.getAnimationProcessor(), animationEvent.getPartialTick());
        }
    }

    void i(GirlEntity em_class2582) {
        if (!Action.a(em_class2582, Action.PUSSY_LICKING)) {
            return;
        }
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        if (this.a.isGamePaused()) {
            return;
        }
        AnimationProcessor<GirlEntity> animationProcessor = this.getAnimationProcessor();
        IBone iBone = animationProcessor.getBone("head");
        float f = this.a.getRenderPartialTicks() + (float)this.a.player.ticksExisted;
        Vector3f f7_class2922 = this.a((GalathEntity)em_class2582, f);
        iBone.setRotationX(iBone.getRotationX() + f7_class2922.x);
        iBone.setRotationY(iBone.getRotationY() + f7_class2922.y);
        iBone.setRotationZ(iBone.getRotationZ() + f7_class2922.z);
        if (em_class2582.currentAction() != Action.PUSSY_LICKING || ((GalathEntity)em_class2582).a5) {
            return;
        }
        float f2 = (float)(Math.sin(f * 0.3f) * 10.0);
        if (f2 > 0.0f && this.g < 0.0f || f2 < 0.0f && this.g > 0.0f) {
            em_class2582.PlaySound(SoundsHandler.a(SoundsHandler.GIRLS_ALLIE_LIPSOUND));
        }
        this.g = f2;
    }

    Vector3f a(GalathEntity f__class2972, float f) {
        return Reference.LerpVector3f(this.a(f), Vector3f.ZERO, (double)f__class2972.float_b(this.a.getRenderPartialTicks()));
    }

    Vector3f a(float f) {
        return new Vector3f((float)Math.sin(f * 0.3f) * gc_class360.c(10.0f), (float)Math.sin(f * 0.15f) * gc_class360.c(7.0f), (float)Math.sin((double)f * -0.15) * gc_class360.c(7.0f));
    }

    void void_c(GirlEntity em_class2582) {
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        GalathEntity f__class2972 = (GalathEntity)em_class2582;
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        IBone iBone = animationProcessor.getBone("body");
        f__class2972.bw = iBone.getRotationY();
        f__class2972.bm = iBone.getScaleY();
    }

    void void_d(GirlEntity em_class2582) {
        if (em_class2582.actionController.getAnimationState() != AnimationState.Transitioning) {
            return;
        }
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        Action fp_class3242 = em_class2582.currentAction();
        if (fp_class3242 == Action.HUG_MANG) {
            IBone iBone = animationProcessor.getBone("body2");
            if (iBone == null) {
                return;
            }
            iBone.setPositionX(0.0f);
            iBone.setPositionY(-0.53f);
            iBone.setPositionZ(-40.05f);
        }
    }

    void k(GirlEntity em_class2582) {
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        if (em_class2582.currentAction() != Action.MASTERBATE) {
            return;
        }
        EntityPlayer entityPlayer = em_class2582.net_minecraft_entity_player_EntityPlayer_z();
        if (entityPlayer == null) {
            entityPlayer = this.a.player;
        }
        MolangParser molangParser = GeckoLibCache.getInstance().parser;
        Vec3d vec3d = ak_class32.b(em_class2582, entityPlayer, this.a.getRenderPartialTicks()).add(em_class2582.b("head"));
        float f = (float)gc_class360.b(Math.atan2(vec3d.z, vec3d.x)) - em_class2582.java_lang_Float_I().floatValue();
        float f2 = (float)gc_class360.b(Math.atan2(vec3d.y, Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z)));
        double d = Math.abs(vec3d.x) + Math.abs(vec3d.y) + Math.abs(vec3d.z);
        double d2 = d * 7.0 + -20.0;
        double d3 = d * 5.0 + -20.0;
        molangParser.setValue("pitch", d2 + (double)f2 - 80.0);
        molangParser.setValue("armpitch", d3 + (double)f2 + -110.0);
        molangParser.setValue("armyaw", f + 80.0f);
        molangParser.setValue("yaw", f + 90.0f);
    }

    void ShowFutaCock() {
        if (ClientProxy.IS_PRELOADING) {
            return;
        }
        this.getAnimationProcessor().getBone("futaCock").setHidden(!FutaCommand.enabled);
        this.getAnimationProcessor().getBone("futaBallLL").setHidden(!FutaCommand.enabled);
        this.getAnimationProcessor().getBone("futaBallLR").setHidden(!FutaCommand.enabled);
    }

    void j(GirlEntity girlEntity) {
        if (!(girlEntity instanceof PlayerGirl)) {
            return;
        }
        this.getAnimationProcessor().getBone("coin").setHidden(true);
    }

    void void_g(GirlEntity em_class2582) {
        this.getAnimationProcessor().getBone("wings").setHidden(!((b7_class68)((Object)em_class2582)).boolean_a());
    }

    void void_e(GirlEntity em_class2582) {
        AnimationProcessor animationProcessor = this.getAnimationProcessor();
        IBone iBone = animationProcessor.getBone("nippleR");
        IBone iBone2 = animationProcessor.getBone("nippleL");
        IBone iBone3 = animationProcessor.getBone("braBoobL");
        IBone iBone4 = animationProcessor.getBone("braBoobR");
        IBone iBone5 = animationProcessor.getBone("slip");
        boolean bl = ((b7_class68)((Object)em_class2582)).boolean_c();
        boolean bl2 = Action.a(em_class2582, Action.PUSSY_LICKING, Action.MASTERBATE_SITTING, Action.MASTERBATE_SITTING_CUM);
        if (iBone == null) {
            return;
        }
        if (iBone3 == null) {
            return;
        }
        iBone.setHidden(!bl);
        iBone2.setHidden(!bl);
        iBone3.setHidden(bl);
        iBone4.setHidden(bl);
        iBone5.setHidden(bl || bl2);
    }

    void void_b(GirlEntity em_class2582) {
        boolean bl;
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        if (!em_class2582.getDataManager().get(GalathEntity.bP).booleanValue()) {
            return;
        }
        if (em_class2582.currentAction() != Action.KNOCK_OUT_FLY) {
            return;
        }
        IBone iBone = this.getAnimationProcessor().getBone("body");
        Vec3d vec3d = new Vec3d(em_class2582.lastTickPosX, em_class2582.lastTickPosY, em_class2582.lastTickPosZ);
        Vec3d vec3d2 = em_class2582.getPositionVector().subtract(vec3d);
        boolean bl2 = bl = Math.abs(vec3d2.x) + Math.abs(vec3d2.z) < (double)0.01f;
        if (bl) {
            iBone.setRotationX(gc_class360.c(-90.0f));
            iBone.setPositionY(0.0f);
            iBone.setPositionZ(0.0f);
        } else {
            Vec3d vec3d3 = GalathModel.net_minecraft_util_math_Vec3d_d(em_class2582);
            iBone.setRotationX(-((float)vec3d3.x));
            iBone.setPositionY((float)vec3d3.y);
            iBone.setPositionZ((float)vec3d3.z);
        }
    }

    void h(GirlEntity em_class2582) {
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        if (em_class2582.currentAction() != Action.RAPE_CHARGE) {
            return;
        }
        Vec3d vec3d = GalathModel.net_minecraft_util_math_Vec3d_d(em_class2582);
        IBone iBone = this.getAnimationProcessor().getBone("body");
        IBone iBone2 = this.getAnimationProcessor().getBone("rotationTool");
        iBone2.setRotationX((float)vec3d.x);
        iBone.setPositionY((float)vec3d.y);
        iBone.setPositionZ((float)vec3d.z);
        float f = em_class2582.getDataManager().get(GalathEntity.bO);
        iBone.setRotationY(gc_class360.c(f * 180.0f));
    }

    /*
    void f(GirlEntity em_class2582) {
        if (!(em_class2582 instanceof GalathEntity)) {
            return;
        }
        GalathEntity f__class2972 = (GalathEntity)em_class2582;
        if (f__class2972.com_trolmastercard_sexmod_fp_class324_y() != fp_class324.ATTACK_SWORD) {
            this.f = -1L;
            this.i = -1L;
            return;
        }
        int n = f__class2972.az();
        if (n == 24 && this.f == -1L) {
            this.f = this.a.world.getTotalWorldTime();
            this.i = this.f + 8L;
        }
        if (!be_class78.a((double)n, 24.0, 32.0)) {
            return;
        }
        IBone iBone = this.getAnimationProcessor().getBone("body");
        Vec3d vec3d = GalathModel.a(f__class2972, f__class2972.net_minecraft_util_math_Vec3d_B());
        float f = ((float)Minecraft.getMinecraft().world.getTotalWorldTime() + this.a.getRenderPartialTicks() - (float)this.f) / (float)(this.i - this.f);
        vec3d = b6_class67.a(vec3d, Vec3d.ZERO, (double)f);
        iBone.setRotationX((float)vec3d.x);
        iBone.setPositionY((float)vec3d.y);
        iBone.setPositionZ((float)vec3d.z);
    }*/

    void void_a(GirlEntity em_class2582) {
        float f = 0.0f;
        switch (em_class2582.currentAction()) {
            case BOOST: {
                if (Action.BOOST.ticksPlaying[1] > 13 && Action.BOOST.ticksPlaying[1] < 40) {
                    f = 45.0f;
                }
            }
            case FLY: 
            case CONTROLLED_FLIGHT: {
                break;
            }
            default: {
                return;
            }
        }
        float f2 = Minecraft.getMinecraft().getRenderPartialTicks();
        IBone iBone = this.getAnimationProcessor().getBone("rotationTool");
        f2_class286 f2_class2862 = ((b7_class68)((Object)em_class2582)).com_trolmastercard_sexmod_f2_class286_d();
        iBone.setRotationX((float) Reference.LerpDouble(f2_class2862.c + (double)f, f2_class2862.d + (double)f, (double)f2));
        iBone.setRotationZ((float) Reference.LerpDouble(f2_class2862.b, f2_class2862.a, (double)f2));
    }

    @Override
    public String[] HeadArmor() {
        return new String[]{"armorHelmet"};
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

