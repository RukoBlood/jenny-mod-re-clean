/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.trolmastercard.sexmod;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

// dh_class182
public class ManglelieRenderer
extends GirlRenderer<ManglelieEntity> {
    final static gv_class388 C = new gv_class388(115, 108, 188, 255);
    final static f7_class292 D = new f7_class292(0.05f, 0.04f, 0.0f);
    final static f7_class292 v = new f7_class292(0.0f, 0.065f, 0.0f);
    final static f7_class292 z = new f7_class292(0.0f, 0.03f, 0.03f);
    final static gv_class388 r = new gv_class388(63, 59, 150, 255);
    final static gv_class388 x = new gv_class388(79, 74, 188, 255);
    final static float A = 0.5f;
    final static float w = 0.5f;
    final static int s = 40;
    final static float y = 0.01f;
    final static float t = 0.03f;
    final static public HashSet<String> B = new HashSet<String>(){
        {
            this.add("boobs2");
            this.add("booty2");
            this.add("vagina2");
            this.add("fuckhole2");
        }
    };
    boolean u = false;

    public ManglelieRenderer(RenderManager renderManager, AnimatedGeoModel animatedGeoModel, double d) {
        super(renderManager, animatedGeoModel, d);
    }

    @Override
    public HashSet<String> a() {
        if (!this.u) {
            B.addAll(gx_class390.a);
            this.u = true;
        }
        return B;
    }

    @Override
    public void doRender(ManglelieEntity f8_class2932, double d, double d2, double d3, float f, float f2) {
        if (this.d_(f8_class2932)) {
            return;
        }
        if (this.a_0(f8_class2932)) {
            return;
        }
        if (ManglelieRenderer.c(f8_class2932, 0.5f)) {
            return;
        }
        if (this.c(f8_class2932)) {
            return;
        }
        super.doRender(f8_class2932, d, d2, d3, f, f2);
        ManglelieRenderer.a_9(f8_class2932, f2);
    }

    boolean c(ManglelieEntity f8_class2932) {
        GalathEntity f__class2972 = f8_class2932.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return false;
        }
        switch (f__class2972.currentAction()) {
            case CONTROLLED_FLIGHT: 
            case BOOST: {
                return true;
            }
        }
        return false;
    }

    // todo clashes
    boolean a_0(ManglelieEntity f8_class2932) {
        if (f8_class2932.currentAction() != Action.RIDE_MOMMY_HEAD) {
            return false;
        }
        return f8_class2932.com_trolmastercard_sexmod_f__class297_a(false) == null;
    }

    // TODO clashes
    boolean d_(ManglelieEntity f8_class2932) {
        GalathEntity f__class2972 = f8_class2932.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return false;
        }
        if (f__class2972.isDead) {
            f8_class2932.void_a((UUID)null);
            return false;
        }
        return f__class2972.boolean_b();
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double d, double d2, double d3, float f, float f2) {
        if (!(entity instanceof ManglelieEntity)) {
            super.doRenderShadowAndFire(entity, d, d2, d3, f, f2);
            return;
        }
        ManglelieEntity f8_class2932 = (ManglelieEntity)entity;
        if (this.d_(f8_class2932)) {
            return;
        }
        if (f8_class2932.boolean_r()) {
            return;
        }
        super.doRenderShadowAndFire(entity, d, d2, d3, f, f2);
    }

    static boolean c(GirlEntity em_class2582, float f) {
        if (!(em_class2582 instanceof ManglelieEntity)) {
            return false;
        }
        GalathEntity f__class2972 = ((ManglelieEntity)em_class2582).com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return false;
        }
        return f__class2972.bm < f;
    }

    public static void a_9(GirlEntity em_class2582, float f) {
        EntityPlayerSP entityPlayerSP = ManglelieRenderer.i.player;
        if (entityPlayerSP == null) {
            return;
        }
        if (ManglelieRenderer.c(em_class2582, 0.5f)) {
            return;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        if (em_class2582.boolean_h()) {
            GlStateManager.translate(0.0, 0.01, 0.0);
        } else {
            af_class27.a(i, em_class2582, f);
            ManglelieRenderer.b(em_class2582, f);
        }
        i.getTextureManager().bindTexture(e);
        GlStateManager.disableCull();
        GlStateManager.disableLighting();

        ManglelieRenderer.a_6(em_class2582, bufferBuilder, tessellator, GirlRenderer.a(em_class2582, f));
        ManglelieRenderer.a(em_class2582, bufferBuilder, tessellator);
        GlStateManager.popMatrix();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
    }

    static void b(GirlEntity em_class2582, float f) {
        if (!(em_class2582 instanceof ManglelieEntity)) {
            return;
        }
        ManglelieEntity f8_class2932 = (ManglelieEntity)em_class2582;
        if (!f8_class2932.boolean_r()) {
            return;
        }
        if (ManglelieModel.boolean_c(f8_class2932)) {
            return;
        }
        GalathEntity f__class2972 = f8_class2932.com_trolmastercard_sexmod_f__class297_a(false);
        if (f__class2972 == null) {
            return;
        }
        GlStateManager.rotate(-b6_class67.b(em_class2582.prevRenderYawOffset, em_class2582.renderYawOffset, (double)f), 0.0f, 1.0f, 0.0f);
    }

    static boolean a_5(GirlEntity em_class2582) {
        if (em_class2582 instanceof GalathEntity) {
            em_class2582 = ((GalathEntity)em_class2582).com_trolmastercard_sexmod_f8_class293_a(false);
        }
        if (em_class2582 == null) {
            return false;
        }
        return !Action.a(em_class2582, Action.THREESOME_SLOW, Action.THREESOME_FAST, Action.THREESOME_CUM);
    }

    static void a(GirlEntity em_class2582, BufferBuilder bufferBuilder, Tessellator tessellator) {
        if (!ManglelieRenderer.a_5(em_class2582)) {
            return;
        }
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < 39; ++i) {
            ManglelieRenderer.a(em_class2582, bufferBuilder, i, i + 1);
        }
        ManglelieRenderer.a(em_class2582, bufferBuilder, 39, 0);
        tessellator.draw();
    }

    static void a(GirlEntity em_class2582, BufferBuilder bufferBuilder, int n, int n2) {
        Vec3d vec3d = em_class2582.b("skirt_" + n + "_0");
        Vec3d vec3d2 = em_class2582.b("skirt_" + n + "_1");
        Vec3d vec3d3 = em_class2582.b("skirt_" + n + "_2");
        Vec3d vec3d4 = em_class2582.b("skirt_" + n2 + "_0");
        Vec3d vec3d5 = em_class2582.b("skirt_" + n2 + "_1");
        Vec3d vec3d6 = em_class2582.b("skirt_" + n2 + "_2");
        gv_class388 gv_class3882 = n % 2 == 0 ? x : r;
        bufferBuilder.pos(vec3d.x, vec3d.y, vec3d.z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3d2.x, vec3d2.y, vec3d2.z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3d5.x, vec3d5.y, vec3d5.z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3d4.x, vec3d4.y, vec3d4.z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3d2.x, vec3d2.y, vec3d2.z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3d5.x, vec3d5.y, vec3d5.z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3d6.x, vec3d6.y, vec3d6.z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
        bufferBuilder.pos(vec3d3.x, vec3d3.y, vec3d3.z).color(gv_class3882.a, gv_class3882.d, gv_class3882.c, gv_class3882.b).endVertex();
    }

    @Override
    protected void a(BufferBuilder bufferBuilder, String string, GeoBone geoBone) {
        ManglelieRenderer.a(this.j, string, geoBone, false);
        Entity entity = ((ManglelieEntity)this.j).b_7();
        if (entity == null) {
            return;
        }
        if ("weapon".equals(string) && ((ManglelieEntity)this.j).a(entity, i.getRenderPartialTicks())) {
            this.a(bufferBuilder, geoBone, true);
        }
        if ("offhand".equals(string) && !((ManglelieEntity)this.j).a(entity, i.getRenderPartialTicks())) {
            this.a(bufferBuilder, geoBone, false);
        }
    }

    void a(BufferBuilder bufferBuilder, GeoBone geoBone, boolean bl) {
        ItemRenderer itemRenderer = Minecraft.getMinecraft().getItemRenderer();
        GlStateManager.pushMatrix();
        Tessellator.getInstance().draw();
        p_class418.a(IGeoRenderer.MATRIX_STACK, geoBone);
        GL11.glEnable(2896);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (bl) {
            GlStateManager.translate(-0.01, 0.0, 0.0);
            GlStateManager.rotate(120.0f, 1.0f, 0.0f, 0.0f);
        } else {
            GlStateManager.translate(0.15, 0.0, -0.05);
            GlStateManager.rotate(-140.0f, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.scale(0.7, 0.7, 0.7);
        ItemStack itemStack = new ItemStack(Items.BOW);
        float f = ((ManglelieEntity)this.j).float_b(i.getRenderPartialTicks());
        if (f < 1.0f) {
            float f2 = (float)b6_class67.e(f);
            ((ManglelieEntity)this.j).d((int)(11.0f * (1.0f - f2) + 71980.0f));
            ((ManglelieEntity)this.j).void_a(itemStack);
            ((ManglelieEntity)this.j).setActiveHand(EnumHand.MAIN_HAND);
            ((ManglelieEntity)this.j).W();
        } else {
            ((ManglelieEntity)this.j).void_a(ItemStack.EMPTY);
            ((ManglelieEntity)this.j).K();
        }
        itemRenderer.renderItem(this.j, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        this.bindTexture(Objects.requireNonNull(this.getEntityTexture(this.j)));
        GL11.glDisable(2896);
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    public static void a(GirlEntity em_class2582, String string, GeoBone geoBone, boolean bl) {
        float f;
        String string2;
        if (!string.contains("skirt_")) {
            return;
        }
        int n = ManglelieRenderer.a(string);
        if (be_class78.a((double)n, 17.0, 35.0)) {
            if (i.isGamePaused()) {
                return;
            }
            String string3 = string2 = n < 26 ? "cheekL" : "cheekR";
            if (bl) {
                string2 = string2 + "2";
            }
            if ((f = gc_class360.d(em_class2582.b().getBone(string2).getRotationX())) < 0.0f) {
                return;
            }
            geoBone.setPositionY(geoBone.getPositionY() + f * 0.01f);
        }
        if (be_class78.a((double)n, 1.0, 11.0)) {
            if (!string.endsWith("1")) {
                return;
            }
            String string4 = string2 = n < 6 ? "legR" : "legL";
            if (bl) {
                string2 = string2 + "2";
            }
            if ((f = gc_class360.d(em_class2582.b().getBone(string2).getRotationX())) < 0.0f) {
                return;
            }
            geoBone.setRotationX(gc_class360.c(f));
            geoBone.setPositionY(gc_class360.c(f * 0.03f));
        }
    }

    static int a(String string) {
        int n = string.indexOf('_');
        int n2 = string.indexOf('_', n + 1);
        if (n != -1 && n2 != -1) {
            String string2 = string.substring(n + 1, n2);
            try {
                return Integer.parseInt(string2);
            } catch (NumberFormatException numberFormatException) {
                return -1;
            }
        }
        return -1;
    }

    @Override
    protected void a(GeoModel geoModel, BufferBuilder bufferBuilder, ManglelieEntity f8_class2932, float f, float f2, float f3, float f4, float f5) {
        if (!ManglelieModel.boolean_c(f8_class2932)) {
            super.a(geoModel, bufferBuilder, f8_class2932, f, f2, f3, f4, f5);
            return;
        }
        GeoBone geoBone = geoModel.topLevelBones.get(0);
        GeoBone geoBone2 = null;
        GeoBone geoBone3 = null;
        for (GeoBone geoBone4 : geoBone.childBones) {
            switch (geoBone4.getName()) {
                case "steve": {
                    geoBone3 = geoBone4;
                    break;
                }
                case "body2": {
                    geoBone2 = geoBone4;
                }
            }
        }
        MATRIX_STACK.push();
        MATRIX_STACK.translate(geoBone);
        MATRIX_STACK.moveToPivot(geoBone);
        MATRIX_STACK.rotate(geoBone);
        MATRIX_STACK.scale(geoBone);
        MATRIX_STACK.moveBackFromPivot(geoBone);
        this.renderRecursively(bufferBuilder, geoBone2, f, f2, f3, f4);
        Tessellator.getInstance().draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        try {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.d(this.j));
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.renderRecursively(bufferBuilder, geoBone3, f, f2, f3, (this.j).float_v());
        Tessellator.getInstance().draw();
        MATRIX_STACK.pop();
    }

    static void a_6(GirlEntity em_class2582, BufferBuilder bufferBuilder, Tessellator tessellator, float f) {
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        Vec3d[][] vec3dArray = af_class27.a(em_class2582, f, "clothBoobLconStart", "clothBoobLconEnd", D, v);
        Vec3d[][] vec3dArray2 = af_class27.a(em_class2582, f, "clothBoobRconStart", "clothBoobRconEnd", D, v);
        Vec3d[][] vec3dArray3 = af_class27.a(em_class2582, f, "clothBoobMidconStart", "clothBoobMidconEnd", z, z);
        af_class27.a(bufferBuilder, vec3dArray, C);
        af_class27.a(bufferBuilder, vec3dArray2, C);
        af_class27.a(bufferBuilder, vec3dArray3, C);
        tessellator.draw();
    }

    public boolean a(HashSet hashSet, GeoBone geoBone) {
        while (geoBone.parent != null) {
            String string = geoBone.getName();
            if (string.contains("clothBoob")) {
                return true;
            }
            if (hashSet.contains(string)) {
                return false;
            }
            if (string.startsWith("armor")) {
                return false;
            }
            geoBone = geoBone.parent;
        }
        return true;
    }

    @Override
    protected Vec3d a(ManglelieEntity f8_class2932, float f, Vec3d vec3d) {
        GalathEntity f__class2972;
        if (f8_class2932.currentAction() == Action.RUN) {
            float f2;
            f8_class2932.rotationYaw = f2 = f8_class2932.java_lang_Float_I().floatValue();
            f8_class2932.prevRenderYawOffset = f2;
            f8_class2932.renderYawOffset = f2;
            f8_class2932.prevRotationYawHead = f2;
            f8_class2932.rotationYawHead = f2;
            return vec3d;
        }
        if (ManglelieRenderer.b_4(f8_class2932) && (f__class2972 = f8_class2932.com_trolmastercard_sexmod_f__class297_a(false)) != null) {
            ManglelieRenderer.a(f__class2972, f, f8_class2932);
            return ManglelieRenderer.b(f__class2972, f);
        }
        return vec3d;
    }

    public static void a(GalathEntity f__class2972, float f, EntityLivingBase entityLivingBase) {
        boolean bl = f__class2972.boolean_Q();
        float f2 = bl ? f__class2972.java_lang_Float_I().floatValue() : f__class2972.rotationYawHead;
        float f3 = bl ? f__class2972.java_lang_Float_I().floatValue() : f__class2972.prevRotationYawHead;
        Float f4 = GalathEntity.a(f__class2972, f);
        if (f4 != null) {
            f2 = f4.floatValue();
            f3 = f4.floatValue();
        }
        entityLivingBase.rotationYaw = f2;
        entityLivingBase.prevRenderYawOffset = f3;
        entityLivingBase.renderYawOffset = f2;
        entityLivingBase.prevRotationYawHead = f3;
        entityLivingBase.rotationYawHead = f2;
    }

    public static boolean b_4(ManglelieEntity f8_class2932) {
        return f8_class2932.boolean_r() && !ManglelieModel.boolean_c(f8_class2932);
    }

    public static Vec3d b(GalathEntity f__class2972, float f) {
        return ak_class32.a(f__class2972, ManglelieRenderer.i.player, f).add(f__class2972.b("mangPos"));
    }

    public static Vec3d a(GalathEntity f__class2972, float f) {
        return ak_class32.a(f__class2972, f).add(f__class2972.b("mangPos"));
    }

    // gay synthetics

    //public void doRender(GirlEntity em_class2582, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((ManglelieEntity)em_class2582, d, d2, d3, f, f2);
    //}

    //@Override
    //protected Vec3d a(GirlEntity em_class2582, float f, Vec3d vec3d) {
    //    return this.a((ManglelieEntity)em_class2582, f, vec3d);
    //}

    //@Override
    //protected void a(GeoModel geoModel, BufferBuilder bufferBuilder, GirlEntity em_class2582, float f, float f2, float f3, float f4, float f5) {
    //    this.a(geoModel, bufferBuilder, (ManglelieEntity)em_class2582, f, f2, f3, f4, f5);
    //}

    //@Override
    //public void doRender(EntityLivingBase entityLivingBase, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((ManglelieEntity)entityLivingBase, d, d2, d3, f, f2);
    //}

    //@Override
    //public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
    //    this.doRender((ManglelieEntity)entity, d, d2, d3, f, f2);
    //}
}

