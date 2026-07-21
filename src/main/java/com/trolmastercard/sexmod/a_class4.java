/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import com.trolmastercard.sexmod.Packages.UploadModelString;
import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.Custom.CustomModelEntity;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.girls.PlayerGirlEntity;
import com.trolmastercard.sexmod.proxy.ClientProxy;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//a.class
public class a_class4 extends GuiScreen {
    final static public ResourceLocation k = new ResourceLocation("sexmod", "textures/gui/clothing_icons.png");
    final static int r = 20;
    final static float j = 0.25f;
    int n = 0;
    int l = 0;
    float o = 0.0f;
    static public float b = 0.0f;
    static protected List<Integer> a = new ArrayList<Integer>();
    static protected int s = 0;
    static protected int h = 0;
    GirlEntity c;
    boolean p = false;
    gq_class381 q;
    static public List<Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>>> m = new ArrayList<Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>>>();
    final UUID ID;
    int i;
    int t;
    public boolean f = false;
    int d = 0;
    int e = 1;

    public a_class4(@Nonnull GirlEntity girlEntity) {
        //Object object;
        this.mc = Minecraft.getMinecraft();
        this.ID = girlEntity.girlID();
        PlayerGirlEntity playerGirlEntity = PlayerGirlEntity.fromGirl(girlEntity);
        if (playerGirlEntity == null) {
            playerGirlEntity = PlayerGirlEntity.JENNY;
        }
        try {
            Object object = playerGirlEntity.npcClass.getConstructor(World.class);
            this.c = (GirlEntity)((Constructor)object).newInstance(this.mc.world);
            this.c.setLocallyRegistered(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        this.e();
        String object = girlEntity.getCustomModelKey();
        this.c.getDataManager().set(GirlEntity.CUSTOM_MODEL_KEY, object);
        int n = 0;
        for (String string : this.c.getCustomPartsSet()) {
            EnumCustomPartCategory gw_class3892 = CustomModel.e(string);
            if (EnumCustomPartCategory.CUSTOM_BONE.equals((Object)gw_class3892)) {
                ++n;
            }
            Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> entry = null;
            if (EnumCustomPartCategory.CUSTOM_BONE.equals((Object)gw_class3892) && n > 1) {
                entry = a_class4.b(this.c);
            } else {
                for (Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> entry2 : m) {
                    if (!entry2.getKey().equals((Object)gw_class3892)) continue;
                    entry = entry2;
                }
            }
            if (entry == null) continue;
            m.remove(entry);
            int n2 = entry.getValue().getKey().indexOf(string);
            if (n2 == -1) {
                n2 = 0;
            }
            entry.getValue().setValue(n2);
            m.add(entry);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.q.handleMouseInput();
    }

    //somehow related to GirlRenderer
    public static HashSet<String> b() {
        HashSet<String> hashSet = new HashSet<String>();
        for (Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> entry : m) {
            if (entry.getValue().getKey().size() == 1) continue;
            Map.Entry<List<String>, Integer> entry2 = entry.getValue();
            List<String> list = entry2.getKey();
            Integer n = entry2.getValue();
            hashSet.add(list.get(n));
        }
        return hashSet;
    }

    public static Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> b(GirlEntity em_class2582) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("cross");
        arrayList.addAll((Collection) CustomModel.a(em_class2582).get((Object) EnumCustomPartCategory.CUSTOM_BONE));
        return new AbstractMap.SimpleEntry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>>(EnumCustomPartCategory.CUSTOM_BONE, new AbstractMap.SimpleEntry(arrayList, 0));
    }

    void e() {
        m.clear();
        List<Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>>> list = this.c.d(this.ID);
        this.i = list.size();
        m.addAll(list);
        for (EnumCustomPartCategory gw_class3892 : EnumCustomPartCategory.values()) {
            if (gw_class3892 == EnumCustomPartCategory.GIRL_SPECIFIC) continue;
            ArrayList<String> object2 = new ArrayList<String>();
            object2.add("cross");
            m.add(new AbstractMap.SimpleEntry(gw_class3892, new AbstractMap.SimpleEntry(object2, 0)));
        }
        for (Map.Entry entry : CustomModel.a(this.c).entrySet()) {
            Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> object = null;
            for (Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> entry2 : m) {
                if (!((EnumCustomPartCategory)((Object)entry.getKey())).equals((Object)entry2.getKey())) continue;
                object = entry2;
            }
            if (object == null) continue;
            int n = m.indexOf(object);
            m.remove(object);
            ((List)((Map.Entry)object.getValue()).getKey()).addAll((Collection)entry.getValue());
            m.add(n, object);
        }
    }

    @Override
    public void initGui() {
        this.q = new gq_class381(this.mc, this);
    }

    @Override
    public void setWorldAndResolution(Minecraft minecraft, int n, int n2) {
        super.setWorldAndResolution(minecraft, n, n2);
        this.n = this.a(76.0f);
        this.l = this.b(89.0f);
        this.o = 90.0f;
    }

    boolean a(int n, int n2, int n3, int n4, int n5, int n6) {
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

    @Override
    public void drawScreen(int n, int n2, float f) {
        super.drawScreen(n, n2, f);
        if (this.p) {
            b += Reference.LerpFloat((float)h, (float)s, f);
        }
        this.a();
        this.mc.renderEngine.bindTexture(k);
        int n3 = this.n - this.a(15.0f);
        int n4 = this.l - 20;
        this.drawTexturedModalRect(n3, n4, 100, this.a(n, n2, n3, n4, n3 + 20, n4 + 20) ? 40 : 20, 20, 20);
        if (CustomModel.g() == null) {
            this.b(n3, n, n2);
        }
        this.a(this.n, this.l, this.o, this.c, 1.2345679f);
        this.c.onUpdate();
        this.q.drawScreen(n, n2, f);
    }

    // void b(int n, int n2, int n3) {
    //     int n4;
    //     this.drawTexturedModalRect(n, n4, 120, this.a(n2, n3, n, n4 = this.l - 40, n + 20, n4 + 20) ? 40 : 20, 20, 20);
    //     this.drawTexturedModalRect(n, n4, 20, this.a(n2, n3, n, n4 -= 20, n + 20, n4 + 20) ? 170 : 150, 20, 20);
    //     this.drawTexturedModalRect(n, n4, 0, this.a(n2, n3, n, n4 -= 20, n + 20, n4 + 20) ? 170 : 150, 20, 20);
    // }

    void b(int var1, int var2, int var3) {
        int var4 = this.l - 40;
        this.drawTexturedModalRect(var1, var4, 120, this.a(var2, var3, var1, var4, var1 + 20, var4 + 20) ? 40 : 20, 20, 20);
        var4 -= 20;
        this.drawTexturedModalRect(var1, var4, 20, this.a(var2, var3, var1, var4, var1 + 20, var4 + 20) ? 170 : 150, 20, 20);
        var4 -= 20;
        this.drawTexturedModalRect(var1, var4, 0, this.a(var2, var3, var1, var4, var1 + 20, var4 + 20) ? 170 : 150, 20, 20);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    void c() {
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        HashSet<String> hashSet = new HashSet<String>();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> entry : m) {
            if (entry.getKey() == EnumCustomPartCategory.GIRL_SPECIFIC) {
                arrayList.add(entry.getValue().getValue());
                continue;
            }
            Map.Entry<List<String>, Integer> entry2 = entry.getValue();
            Integer n = entry2.getValue();
            if (n == 0) continue;
            String string = entry2.getKey().get(n);
            hashSet.add(string);
        }
        PackageHandler.networkWrapper.sendToServer((IMessage)new UploadModelString(GirlEntity.serializePartsSet(hashSet), this.ID, arrayList));
        this.mc.player.closeScreen();
    }

    public void a(EnumCustomPartCategory gw_class3892, boolean bl, int n) {
        int n2;
        //Object object;
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        ArrayList<Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>>> arrayList = new ArrayList<>();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        int n3 = 0;
        for (Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> entry : m) {
            if (entry.getKey().equals((Object)gw_class3892)) {
                arrayList.add(entry);
                arrayList2.add(n3);
            }
            ++n3;
        }
        if (arrayList.size() == 0) {
            return;
        }
        Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> object;
        if (arrayList.size() == 1) {
            object = arrayList.get(0);
            n2 = (Integer)arrayList2.get(0);
        } else {
            int n4 = this.i == 0 || n > this.i - 1 + EnumCustomPartCategory.a() ? n - (this.i + EnumCustomPartCategory.a()) : n;
            object = arrayList.get(n4);
            n2 = (Integer)arrayList2.get(n4);
        }
        if (object == null) {
            return;
        }
        Map.Entry<List<String>, Integer> entry = object.getValue();
        int n5 = (Integer)entry.getValue();
        int n6 = ((List)entry.getKey()).size();
        if (bl) {
            if (++n5 >= n6) {
                n5 = 0;
            }
        } else if (--n5 < 0) {
            n5 = n6 - 1;
        }
        m.set(n2, new AbstractMap.SimpleEntry<>(object.getKey(), new AbstractMap.SimpleEntry<>((object.getValue()).getKey(), n5)));
        ArrayList<Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>>> arrayList3 = new ArrayList<Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>>>();
        for (Map.Entry<EnumCustomPartCategory, Map.Entry<List<String>, Integer>> entry2 : m) {
            if (entry2.getKey() != EnumCustomPartCategory.GIRL_SPECIFIC) continue;
            arrayList3.add(entry2);
        }
        this.c.b(arrayList3);
    }

    public void a(int n, int n2, float f, CustomModelEntity cy_class1532) {
        this.a(n, n2, f, cy_class1532, 1.876945f);
    }

    public void a(CustomModelEntity cy_class1532) {
        this.a(this.n, this.l, this.o, cy_class1532, 2.876945f, cy_class1532.f ? 1 : 0);
    }

    public void a(String string, int n, int n2) {
        this.drawHoveringText(string, n, n2);
    }

    @Override
    protected void mouseClickMove(int n, int n2, int n3, long l) {
        super.mouseClickMove(n, n2, n3, l);
        if (n3 != 0) {
            return;
        }
        if (n < this.width / 2) {
            return;
        }
        int n4 = n - this.t;
        a.add(n4);
        this.t = n;
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        int n4;
        super.mouseClicked(n, n2, n3);
        this.q.mouseClicked(n, n2, n3);
        if (n3 != 0) {
            return;
        }
        this.f = true;
        this.p = true;
        this.t = n;
        int n5 = this.n - this.a(15.0f);
        if (this.a(n, n2, n5, n4 = this.l - 20, n5 + 20, n4 + 20)) {
            this.c();
        }
        if (CustomModel.g() != null) {
            return;
        }
        n4 = this.l - 40;
        if (this.a(n, n2, n5, n4, n5 + 20, n4 + 20)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            this.mc.player.closeScreen();
            int n6 = CustomModel.b(true);
            if (n6 != 0) {
                CustomModel.d = true;
                return;
            }
            GirlEntity em_class2582 = GirlEntity.getClientGirlEntity(this.ID);
            if (em_class2582 != null) {
                a_class4.a(em_class2582);
            }
            return;
        }
        if (this.a(n, n2, n5, n4 -= 20, n5 + 20, n4 + 20)) {
            Desktop.getDesktop().open(new File(CustomModel.d()));
            return;
        }
        if (this.a(n, n2, n5, n4 -= 20, n5 + 20, n4 + 20)) {
            try {
                //Desktop.getDesktop().browse(new URI("http://fapcraft.org/assets/video/tutorial/girl_wand.mp4"));
            } catch (Exception uRISyntaxException) {
                throw new RuntimeException(uRISyntaxException);
            }
        }
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        super.mouseReleased(n, n2, n3);
        if (n3 == 0) {
            this.p = false;
            this.f = false;
        }
        this.d = h;
    }

    int a(float f) {
        return Math.round((float)this.width * (f / 100.0f));
    }

    int b(float f) {
        return Math.round((float)this.height * (f / 100.0f));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        this.c.world.removeEntityDangerously(this.c);
        a.clear();
        m.clear();
    }

    public GirlEntity d() {
        return this.c;
    }

    public void a(int n, int n2, int n3, int n4) {
        this.mc.renderEngine.bindTexture(k);
        this.drawTexturedModalRect(n, n2, n3, n4, 20, 20);
    }

    public void a(int n, int n2, int n3) {
        this.a(n, n2, n3, 0);
    }

    public void a(int n, int n2, Point2D e1_class2172) {
        this.a(n, n2, e1_class2172.x, e1_class2172.y);
    }

    void a(int n, int n2, float f, EntityLivingBase entityLivingBase, float f2) {
        this.a(n, n2, f, entityLivingBase, f2, 0);
    }

    void a(int n, int n2, float f, EntityLivingBase entityLivingBase, float f2, int n3) {
        float f3 = entityLivingBase.renderYawOffset;
        float f4 = entityLivingBase.rotationYaw;
        float f5 = entityLivingBase.rotationPitch;
        float f6 = entityLivingBase.prevRotationYawHead;
        float f7 = entityLivingBase.rotationYawHead;
        entityLivingBase.renderYawOffset = 0.0f;
        entityLivingBase.rotationYaw = 0.0f;
        entityLivingBase.rotationPitch = 0.0f;
        entityLivingBase.prevRotationYawHead = 0.0f;
        entityLivingBase.rotationYawHead = 0.0f;
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(n, n2, 50.0f);
        GlStateManager.scale(-f, f, f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, n3);
        GlStateManager.rotate(b, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(0.25f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntity(entityLivingBase, 0.0, 0.0, 0.0, 0.0f, f2, false);
        renderManager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        entityLivingBase.renderYawOffset = f3;
        entityLivingBase.rotationYaw = f4;
        entityLivingBase.rotationPitch = f5;
        entityLivingBase.prevRotationYawHead = f6;
        entityLivingBase.rotationYawHead = f7;
    }

    void a() {
        if (this.p) {
            return;
        }
        float f = Minecraft.getDebugFPS();
        if (f == 0.0f) {
            f = 0.1f;
        }
        if (this.d == 0) {
            b += (float)(this.e * 10) / f;
            return;
        }
        b += (float)this.d / f;
        this.d = (int)((float)this.d * (1.0f - 0.25f / f));
        if (Math.abs(this.d) > 10) {
            return;
        }
        this.e = this.d > 0 ? 1 : -1;
        this.d = 0;
    }

    @SideOnly(value=Side.CLIENT)
    public static void a(@Nonnull GirlEntity em_class2582) {
        boolean bl;
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.currentScreen instanceof a_class4) {
            return;
        }
        boolean bl2 = bl = CustomModel.g() == null || CustomModel.b();
        if (!bl) {
            minecraft.player.sendStatusMessage(new TextComponentString("You have to whitelist the server to use its custom models. " + (Object)((Object)TextFormatting.YELLOW) + "/whitelistserver"), true);
            return;
        }
        minecraft.addScheduledTask(() -> minecraft.displayGuiScreen(new a_class4(em_class2582)));
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    @SideOnly(value=Side.CLIENT)
    public static class b_inner5 {
        @SubscribeEvent
        @SideOnly(value=Side.CLIENT)
        public void a(InputEvent.KeyInputEvent keyInputEvent) {
            if (!ClientProxy.keyBindings[1].isPressed()) {
                return;
            }
            if (CustomModel.d) {
                boolean bl = CustomModel.d = 0 != CustomModel.b(true);
                if (CustomModel.d) {
                    return;
                }
            }
            Minecraft mc = Minecraft.getMinecraft();
            PlayerGirl pg = PlayerGirl.getUUIDHashtable(mc.player.getPersistentID());
            if (pg == null) {
                mc.player.sendStatusMessage(new TextComponentString("You have to turn into the girl you want to customize"), true);
                return;
            }
            a_class4.a(pg);
        }

        @SubscribeEvent
        @SideOnly(value=Side.CLIENT)
        public void a(TickEvent.ClientTickEvent clientTickEvent) {
            h = s;
            s = 0;
            for (Integer n : a) {
                s += n.intValue();
            }
            a.clear();
        }

        private static RuntimeException a(RuntimeException runtimeException) {
            return runtimeException;
        }
    }
}

