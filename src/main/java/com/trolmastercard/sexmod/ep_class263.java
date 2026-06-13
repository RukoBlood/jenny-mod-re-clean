/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.List;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

public class ep_class263 {
    final static int c = 30;
    final static int k = 6;
    final static int f = 6;
    final static float b = 0.15f;
    List<an_class35> g = new ArrayList<an_class35>();
    final int a;
    final ar_class41 i;
    final b8_class69 d;
    final GirlEntity e;
    final float j;
    final float h;

    public ep_class263(int n, ar_class41 ar_class412, b8_class69 b8_class692, GirlEntity em_class2582, float f, float f2) {
        this.a = n;
        this.i = ar_class412;
        this.d = b8_class692;
        this.e = em_class2582;
        this.j = f;
        this.h = f2;
    }

    void a(Minecraft minecraft, Tessellator tessellator, BufferBuilder bufferBuilder, float f) {
        Vec3d vec3d;
        if (this.g.size() < this.a) {
            for (int i = 0; i < 6; ++i) {
                vec3d = this.i.a(this.e);
                this.g.add(new an_class35(minecraft.world, this.d.a(this.e), new Vec3d(vec3d.x + (double)((Reference.RANDOM.nextFloat() * 2.0f - 1.0f) * this.j), vec3d.y + (double)((Reference.RANDOM.nextFloat() * 2.0f - 1.0f) * this.j), vec3d.z + (double)((Reference.RANDOM.nextFloat() * 2.0f - 1.0f) * this.j))));
            }
        }
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        Vec3d vec3d2 = Reference.LerpVec3d(new Vec3d(minecraft.player.lastTickPosX, minecraft.player.lastTickPosY, minecraft.player.lastTickPosZ), minecraft.player.getPositionVector(), (double)f);
        bufferBuilder.begin(9, DefaultVertexFormats.POSITION_COLOR);
        this.b();
        vec3d = null;
        for (an_class35 an_class352 : this.g) {
            Vec3d vec3d3 = Reference.LerpVec3d(an_class352.d, an_class352.f, (double)f);
            if (vec3d == null) {
                vec3d = vec3d3;
            }
            if (vec3d.distanceTo(vec3d3) > (double)this.h) {
                tessellator.draw();
                bufferBuilder.begin(9, DefaultVertexFormats.POSITION_COLOR);
            }
            bufferBuilder.pos(vec3d3.x - vec3d2.x, vec3d3.y - vec3d2.y, vec3d3.z - vec3d2.z).color(255, 255, 255, 255).endVertex();
            vec3d = vec3d3;
        }
        tessellator.draw();
        GlStateManager.enableCull();
    }

    void a() {
        for (an_class35 an_class352 : this.g) {
            an_class352.a();
        }
    }

    void b() {
        if (this.g.isEmpty() || this.g.size() <= 1) {
            return;
        }
        for (int i = 1; i < this.g.size(); ++i) {
            int n;
            an_class35 an_class352 = this.g.get(i);
            Vec3d vec3d = an_class352.f;
            for (n = i - 1; n >= 0 && vec3d.distanceTo(this.g.get((int)n).f) < vec3d.distanceTo(this.g.get((int)(n + 1)).f); --n) {
                this.g.set(n + 1, this.g.get(n));
            }
            this.g.set(n + 1, an_class352);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

