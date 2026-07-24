/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

public class gq_class381
extends GuiListExtended {
    final static int c = 3809871;
    final static List<CustomPartCategory> f = Arrays.asList(CustomPartCategory.values());
    final static String a = "MMMMMMMMMM";
    static protected int i = 5;
    static protected int e = 200;
    private List<a_inner382> b = new ArrayList<a_inner382>();
    a_class4 d;
    boolean h = false;
    float g = 0.0f;

    public gq_class381(Minecraft minecraft, a_class4 a_class42) {
        super(minecraft, a_class42.width / 2, a_class42.height, 0, a_class42.height, 30);
        e = a_class42.width / 2;
        this.d = a_class42;
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int n) {
        return this.b.get(n);
    }

    @Override
    protected int getSize() {
        return this.b.size();
    }

    @Override
    protected int getScrollBarX() {
        return 0;
    }

    protected void drawContainerBackground(Tessellator tessellator) {
    }

    @Override
    public void handleMouseInput() {
        if (!this.isMouseYWithinSlotBounds(this.mouseY)) {
            return;
        }
        int n = Mouse.getEventDWheel();
        if (n == 0) {
            return;
        }
        n = n > 0 ? -1 : 1;
        this.amountScrolled += (float)(n * this.slotHeight / 2);
    }

    @Override
    protected void overlayBackground(int n, int n2, int n3, int n4) {
    }

    void a() {
        int n = this.b.size() * this.slotHeight;
        if (n > this.height) {
            this.top = 0;
            return;
        }
        int n2 = this.height - n;
        this.top = n2 / 2;
    }

    @Override
    public void drawScreen(int n, int n2, float flo) {
        this.b.clear();
        int n3 = 0;
        for (Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry : a_class4.m) {
            CustomPartCategory gw_class3892 = entry.getKey();
            Map.Entry<List<String>, Integer> entry2 = entry.getValue();
            this.b.add(new a_inner382(this, gw_class3892, entry2.getKey(), entry2.getValue()));
            if (!CustomPartCategory.CUSTOM_BONE.equals((Object)entry.getKey())) continue;
            ++n3;
        }
        this.b.sort(Comparator.comparingInt(a_inner3822 -> f.indexOf(a_inner3822.d)));
        List<String> list = CustomModel.a(this.d.c).get((Object) CustomPartCategory.CUSTOM_BONE);
        list.add(0, "cross");
        this.b.add(new a_inner382(this, n3 > 1));
        this.a();
        this.a(n, n2, flo);
        if (!this.h) {
            return;
        }
        this.scrollBy(999999);
        this.h = false;
    }

    void a(int n, int n2, float f) {
        if (!this.visible) {
            return;
        }
        this.mouseX = n;
        this.mouseY = n2;
        this.drawBackground();
        int n3 = this.getScrollBarX();
        int n4 = n3 + 6;
        this.bindAmountScrolled();
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        this.drawContainerBackground(tessellator);
        int n5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        int n6 = this.top + 4 - (int)this.amountScrolled;
        if (this.hasListHeader) {
            this.drawListHeader(n5, n6, tessellator);
        }
        this.drawSelectionBox(n5, n6, n, n2, f);
        GlStateManager.disableDepth();
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture2D();
        int n7 = this.getMaxScroll();
        if (n7 > 0) {
            int n8 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            int n9 = (int)this.amountScrolled * (this.bottom - this.top - (n8 = MathHelper.clamp(n8, 32, this.bottom - this.top - 8))) / n7 + this.top;
            if (n9 < this.top) {
                n9 = this.top;
            }
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferBuilder.pos(n3, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            bufferBuilder.pos(n4, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            bufferBuilder.pos(n4, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            bufferBuilder.pos(n3, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferBuilder.pos(n3, n9 + n8, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            bufferBuilder.pos(n4, n9 + n8, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            bufferBuilder.pos(n4, n9, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            bufferBuilder.pos(n3, n9, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            tessellator.draw();
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferBuilder.pos(n3, n9 + n8 - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
            bufferBuilder.pos(n4 - 1, n9 + n8 - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
            bufferBuilder.pos(n4 - 1, n9, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
            bufferBuilder.pos(n3, n9, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
            tessellator.draw();
        }
        this.renderDecorations(n, n2);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }

    @Override
    public boolean mouseClicked(int n, int n2, int n3) {
        this.a(n, n2, n3);
        return super.mouseClicked(n, n2, n3);
    }

    void a(int n, int n2, int n3) {
        if (n > this.width) {
            return;
        }
        int n4 = this.getAmountScrolled();
        float f = n4 + n2 - 5 - this.top;
        int n5 = Math.round((float)Math.floor(f / (float)this.slotHeight));
        int n6 = (int)Math.round(((double)(f / (float)this.slotHeight) - Math.floor(f / (float)this.slotHeight)) * (double)this.slotHeight);
        if (n5 < 0) {
            return;
        }
        if (n5 < this.b.size()) {
            this.b.get(n5).a(n, n6, n3, n5);
        }
    }

    static Minecraft access$000(gq_class381 gq_class3812) {
        return gq_class3812.mc;
    }

    static Minecraft access$100(gq_class381 gq_class3812) {
        return gq_class3812.mc;
    }

    static Minecraft access$200(gq_class381 gq_class3812) {
        return gq_class3812.mc;
    }

    static Minecraft access$300(gq_class381 gq_class3812) {
        return gq_class3812.mc;
    }

    static Minecraft access$400(gq_class381 gq_class3812) {
        return gq_class3812.mc;
    }

    static Minecraft access$500(gq_class381 gq_class3812) {
        return gq_class3812.mc;
    }

    static Minecraft access$600(gq_class381 gq_class3812) {
        return gq_class3812.mc;
    }

    static Minecraft access$700(gq_class381 gq_class3812) {
        return gq_class3812.mc;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    @SideOnly(value=Side.CLIENT)
    public class a_inner382
    implements GuiListExtended.IGuiListEntry {
        final static int g = 4;
        public CustomPartCategory d;
        public List<String> b;
        public int f;
        FontRenderer c;
        boolean a = false;
        boolean e = false;
        final gq_class381 this$0;

        public a_inner382(gq_class381 gq_class3812, CustomPartCategory gw_class3892, List<String> list, int n) {
            this.this$0 = gq_class3812;
            this.d = gw_class3892;
            this.b = list;
            this.f = n;
            this.c = gq_class381.access$000((gq_class381)gq_class3812).fontRenderer;
        }

        public a_inner382(gq_class381 gq_class3812, boolean bl) {
            this.this$0 = gq_class3812;
            this.e = bl;
            this.a = true;
        }

        boolean b(int n, int n2, int n3, int n4, int n5, int n6) {
            if (n < n3) {
                return false;
            }
            if (n > n5) {
                return false;
            }
            if (n2 < n4) {
                return false;
            }
            return n2 <= n6;
        }

        void b(int n, int n2, int n3) {
            int n4 = 30;
            gq_class381.access$100((gq_class381)this.this$0).renderEngine.bindTexture(a_class4.k);
            this.this$0.d.drawTexturedModalRect(n4, n, 40, this.b(n2, n3, n4, n += 5, n4 + 20, n + 20) ? 40 : 20, 20, 20);
            this.this$0.d.drawTexturedModalRect(n4, n, this.e ? 60 : 80, this.e && this.b(n2, n3, n4 += 40, n, n4 + 20, n + 20) ? 40 : 20, 20, 20);
        }

        void a(int n, int n2, int n3) {
            gq_class381.access$200((gq_class381)this.this$0).renderEngine.bindTexture(a_class4.k);
            this.this$0.d.drawTexturedModalRect(i, n, 0, 60, this.f == 0 ? 119 : 256, 30);
            int n4 = i + 10;
            this.this$0.d.a(n4, n += 5, this.d.iconXPos);
            n4 += 25;
            n4 = this.c(n4, n, n2, n3);
            GirlEntity em_class2582 = this.this$0.d.d();
            CustomModelEntity cy_class1532 = this.f == 0 ? CustomModelEntity.a(gq_class381.access$300((gq_class381)this.this$0).world, em_class2582.girlID(), this.d) : new CustomModelEntity(em_class2582.world, em_class2582.girlID(), this.b.get(this.f));
            CustomModel.b_inner96 b_inner962 = CustomModel.b(cy_class1532.a());
            float f = cy_class1532.f || b_inner962 == null ? 1.0f : b_inner962.d();
            int n5 = b_inner962 == null ? 0 : (int)(-b_inner962.g());
            this.this$0.d.a(n4, n + 10 + (cy_class1532.f ? 0 : 6) + n5, 30.0f * f, cy_class1532);
            if (this.f != 0) {
                this.this$0.d.a(cy_class1532);
            }
            gq_class381.access$400((gq_class381)this.this$0).world.removeEntityDangerously(cy_class1532);
            n4 = (int)((float)n4 + 30.0f);
            if (this.f == 0) {
                return;
            }
            int n6 = n4;
            String string = this.b.get(this.f);
            String string2 = string.length() > gq_class381.a.length() ? string.substring(0, gq_class381.a.length() - 3) + "..." : string;
            this.a(string2, n4, n + 10);
            int n7 = n4 += this.c.getStringWidth(gq_class381.a);
            int n8 = n4;
            String string3 = CustomModel.d(string);
            String string4 = string3.length() > gq_class381.a.length() ? string3.substring(0, gq_class381.a.length() - 3) + "..." : string3;
            this.a(string4, n4, n + 10);
            int n9 = n4 += this.c.getStringWidth(gq_class381.a);
            if (this.b(n2, n3, n6, n + 10, n7, n + 10 + this.c.FONT_HEIGHT)) {
                this.this$0.d.a(string, n2, n3);
            }
            if (this.b(n2, n3, n8, n + 10, n9, n + 10 + this.c.FONT_HEIGHT)) {
                this.this$0.d.a(string3, n2, n3);
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.color(255.0f, 255.0f, 255.0f, 255.0f);
        }

        int c(int n, int n2, int n3, int n4) {
            this.this$0.d.a(n, n2, 0, 20 * (this.b(n3, n4, n, n2, n + 20, n2 + 20) ? 2 : 1));
            this.this$0.d.a(n, n2, 20, 20 * (this.b(n3, n4, n += 20, n2, n + 20, n2 + 20) ? 2 : 1));
            return n + 40;
        }

        void a(int n, int n2, int n3, int n4, int n5) {
            this.this$0.d.drawTexturedModalRect(n, n2, 140, 20, 79, 20);
            int n6 = n += 4;
            int n7 = n + 71 - 4;
            float f = this.a(n2, n6, n7, n3, n4, n5);
            int n8 = (int) Reference.LerpFloat((float)n6, (float)n7, f);
            this.this$0.d.drawTexturedModalRect(n8, n2, this.b(n3, n4, n8, n2, n8 + 4, n2 + 20) ? 223 : 219, 20, 4, 20);
            this.this$0.d.c.a(n5, (int)(f * 100.0f));
        }

        float a(int n, int n2, int n3, int n4, int n5, int n6) {
            if (!this.this$0.d.f) {
                return this.a(n6);
            }
            if ((float)n4 > 0.33333334f * (float)this.this$0.d.width) {
                return this.a(n6);
            }
            if (n5 < n || n5 > n + 20) {
                return this.a(n6);
            }
            if (n4 < n2) {
                return 0.0f;
            }
            if (n4 > n3) {
                return 1.0f;
            }
            return (float)(n4 -= n2) / (float)(n3 -= n2);
        }

        float a(int n) {
            Map.Entry<CustomPartCategory, Map.Entry<List<String>, Integer>> entry = this.this$0.d.c.d(this.this$0.d.ID).get(n);
            return (float)entry.getValue().getValue().intValue() / 100.0f;
        }

        void b(int n, int n2, int n3, int n4) {
            boolean bl = this.this$0.d.c.h(n4);
            gq_class381.access$500((gq_class381)this.this$0).renderEngine.bindTexture(a_class4.k);
            if (bl) {
                this.this$0.d.drawTexturedModalRect(i, n, 0, 60, 119, 30);
            } else {
                this.this$0.d.drawTexturedModalRect(i, n, 0, 90, 95, 30);
            }
            int n5 = i + 10;
            this.this$0.d.a(n5, n += 5, this.this$0.d.c.g(n4));
            n5 += 25;
            if (bl) {
                this.a(n5, n, n2, n3, n4);
            } else {
                this.c(n5, n, n2, n3);
            }
        }

        @Override
        public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl, float f) {
            if (this.a) {
                this.b(n3, n6, n7);
            } else if (this.d == CustomPartCategory.GIRL_SPECIFIC) {
                this.b(n3, n6, n7, n);
            } else {
                this.a(n3, n6, n7);
            }
        }

        void a(String string, int n, int n2) {
            this.c.drawString(string, n, n2, 3809871);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }

        void b(int n, int n2) {
            int n3 = 30;
            if (n > n3 && n < n3 + 20) {
                this.this$0.h = true;
                gq_class381.access$600(this.this$0).getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add("cross");
                arrayList.addAll((Collection) CustomModel.a(this.this$0.d.c).get((Object) CustomPartCategory.CUSTOM_BONE));
                a_class4.m.add(a_class4.b(this.this$0.d.c));
            }
            if (!this.e) {
                return;
            }
            if (n > (n3 += 40) && n < n3 + 20) {
                gq_class381.access$700(this.this$0).getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                a_class4.m.remove(a_class4.m.size() - 1);
            }
        }

        void a(int n, int n2) {
            if (n > 40 && n < 60) {
                this.this$0.d.a(this.d, false, n2);
            }
            if (n > 60 && n < 80) {
                this.this$0.d.a(this.d, true, n2);
            }
        }

        void c(int n, int n2) {
            if (!this.this$0.d.c.h(n2)) {
                this.a(n, n2);
            }
        }

        public void a(int n, int n2, int n3, int n4) {
            if (n3 != 0) {
                return;
            }
            if (n2 < 5) {
                return;
            }
            if (n2 > 25) {
                return;
            }
            if (this.a) {
                this.b(n, n2);
            } else if (this.d == CustomPartCategory.GIRL_SPECIFIC) {
                this.c(n, n4);
            } else {
                this.a(n, n4);
            }
        }

        @Override
        public void updatePosition(int n, int n2, int n3, float f) {
        }

        @Override
        public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
            return false;
        }

        @Override
        public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
        }
    }
}

