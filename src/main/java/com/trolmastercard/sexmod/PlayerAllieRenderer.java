/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.Collection;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerAllieRenderer
extends PlayerGirlRenderer {
    final static float E = 8.0f;
    final static float K = 1.68f;
    final static float M = 5.0f;
    static Collection<PlayerAllieRenderer> J = new ArrayList<PlayerAllieRenderer>();
    double C = 0.0;
    double z = 0.0;
    double A = 0.0;
    double D = 0.0;
    float F = 0.0f;
    float B = 0.0f;
    float G;
    float I;
    double H = 0.0;
    double L = 0.0;

    public PlayerAllieRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel) {
        super(renderManager, animatedGeoModel);
        J.add(this);
    }

    @Override
    protected void void_c() {
        GlStateManager.translate(0.0f, -1.1f, 0.0f);
        GlStateManager.scale(0.7f, 0.7f, 0.7f);
    }

    @Override
    protected void a(boolean bl, ItemStack itemStack) {
        super.a(bl, itemStack);
        switch (itemStack.getItem().getItemUseAction(itemStack)) {
            case BLOCK: 
            case BOW: {
                break;
            }
            default: {
                if (!bl) {
                    GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                }
                GlStateManager.translate(0.0, 0.05, 0.0);
            }
        }
    }

    @Override
    protected void a(boolean bl) {
        super.a(bl);
        if (bl) {
            GlStateManager.translate(0.15, 0.0, 0.0);
        } else {
            GlStateManager.translate(-0.05, 0.0, 0.0);
        }
    }

    @Override
    protected void a(boolean bl, boolean bl2) {
        super.a(bl, bl2);
        if (bl && !bl2) {
            GlStateManager.translate(-0.025, -0.1, -0.1);
            GlStateManager.rotate(10.0f, 1.0f, 0.0f, 0.0f);
            return;
        }
        if (!bl && !bl2) {
            GlStateManager.translate(-0.05, -0.125, 0.125);
            GlStateManager.rotate(50.0f, 1.0f, 0.0f, 0.0f);
            return;
        }
    }

    @Override
    protected void a(String string, GeoBone geoBone) {
        if (this.w.getDataManager().get(GirlEntity.G).booleanValue()) {
            return;
        }
        if ("tail".equals(string)) {
            this.a(geoBone, 0.0f, 0.0f, 1.0f);
        }
        if ("body".equals(string)) {
            this.a(geoBone);
        }
        if (this.w.currentAction() == Action.BOW) {
            return;
        }
        if ("armL".equals(string)) {
            this.a(geoBone, 0.0f, -0.34906584f, 0.15f);
        }
        if (this.w.currentAction() == Action.ATTACK) {
            return;
        }
        if ("armR".equals(string)) {
            this.a(geoBone, 0.0f, 0.34906584f, 0.15f);
        }
    }

    void a(GeoBone geoBone, float f, float f2, float f3) {
        double d = this.C - this.A;
        double d2 = this.z - this.D;
        double d3 = Math.PI / 180 * (double)this.w.rotationYaw;
        Vec2f vec2f = new Vec2f((float)(d * Math.cos(d3) + d2 * Math.sin(d3)), (float)(-d * Math.sin(d3) + d2 * Math.cos(d3)));
        this.G = vec2f.y * -8.0f;
        this.I = vec2f.x * 8.0f;
        this.G = be_class78.b(this.G, -1.68f, 1.68f);
        this.I = be_class78.b(this.I, -1.68f, 1.68f);
        this.G = Reference.Lerp(this.F, this.G, this.y);
        this.I = Reference.Lerp(this.B, this.I, this.y);
        geoBone.setRotationX(f + this.G * f3);
        geoBone.setRotationZ(f2 + this.I * f3);
    }

    void a(GeoBone geoBone) {
        double d = this.C - this.A;
        double d2 = this.z - this.D;
        this.L = (Math.abs(d) + Math.abs(d2)) * 5.0;
        this.L = be_class78.b((float)this.L, 0.0f, 1.0f);
        geoBone.setPositionY((float) Reference.a(5.0, 0.0, Reference.Lerp(this.H, this.L, (double)this.y)));
        if (this.w instanceof PlayerAllie) {
            ((PlayerAllie)this.w).aq = (float) Reference.a((double)0.3f, 0.0, Reference.Lerp(this.H, this.L, (double)this.y));
        }
    }

    void void_a() {
        if (this.w == null) {
            return;
        }
        this.F = this.G;
        this.B = this.I;
        this.H = this.L;
        if (this.w.java_util_UUID_m() == null) {
            return;
        }
        EntityPlayer entityPlayer = this.j.world.getPlayerEntityByUUID(this.w.java_util_UUID_m());
        if (entityPlayer == null) {
            return;
        }
        this.A = this.C;
        this.D = this.z;
        this.C = entityPlayer.posX;
        this.z = entityPlayer.posZ;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class a_inner205 {
        @SubscribeEvent
        public void a(TickEvent.ClientTickEvent clientTickEvent) {
            for (PlayerAllieRenderer dv_class2042 : J) {
                dv_class2042.void_a();
            }
        }
    }
}

