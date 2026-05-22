/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ad_class25 {
    final static int a = 60;
    static public float[] b;

    public ad_class25() {
        if (ad_class25.a()) {
            b = new float[60];
        }
    }

    public static boolean a() {
        return (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(ClientChatEvent clientChatEvent) {
        if (!ad_class25.a()) {
            return;
        }
        if (!"resetcolor".equalsIgnoreCase(clientChatEvent.getMessage())) {
            return;
        }
        KoboldRenderer.c();
        PlayerKoboldRenderer.e();
        GoblinRenderer.c();
        PlayerGoblinRenderer.e();
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void c(ClientChatEvent clientChatEvent) {
        float f;
        int n;
        if (!ad_class25.a()) {
            return;
        }
        String string = clientChatEvent.getOriginalMessage();
        String[] stringArray = string.split(" ");
        if (stringArray.length != 3) {
            return;
        }
        if (!"set".equalsIgnoreCase(stringArray[0])) {
            return;
        }
        try {
            n = Integer.parseInt(stringArray[1]);
            f = Float.parseFloat(stringArray[2]);
            if (b.length - 1 < n) {
                return;
            }
        } catch (Exception exception) {
            return;
        }
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("%sSet dev float N.%s from %s to %s", new Object[]{TextFormatting.GRAY, n, Float.valueOf(b[n]), Float.valueOf(f)})));
        ad_class25.b[n] = f;
        clientChatEvent.setCanceled(true);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void b(ClientChatEvent clientChatEvent) {
        int n;
        if (!ad_class25.a()) {
            return;
        }
        String string = clientChatEvent.getOriginalMessage();
        String[] stringArray = string.split(" ");
        if (stringArray.length != 2) {
            return;
        }
        if (!"get".equalsIgnoreCase(stringArray[0])) {
            return;
        }
        try {
            n = Integer.parseInt(stringArray[1]);
            if (b.length - 1 < n) {
                return;
            }
        } catch (Exception exception) {
            return;
        }
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("%sdev float N.%s is %s", new Object[]{TextFormatting.YELLOW, n, Float.valueOf(b[n])})));
        clientChatEvent.setCanceled(true);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(LivingHurtEvent livingHurtEvent) {
        // TODO this class is useless
        if (true)
            return;

        if (!ad_class25.a()) {
            return;
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        EntityLivingBase entityLivingBase = livingHurtEvent.getEntityLiving();
        if (!(entityLivingBase instanceof KoboldEntity)) {
            return;
        }
        KoboldEntity koboldEntity = (KoboldEntity)entityLivingBase;
        // TODO something is broken in kobold,
        //  because some kind of tribe identification with PLAYER ID results in null
        UUID uUID = KoboldManager.findTribeIdWith(entityPlayerSP.getPersistentID());
        {
            Collection<bs_class97> collection = KoboldManager.getTribeMembers(uUID);
            if (collection != null) {
                for (bs_class97 object : collection) {
                    this.sayMessage("task: " + object.getTaskType().name());
                    this.sayMessage("workers involved: ");
                    for (KoboldEntity ff_class3083 : object.c()) {
                        this.sayMessage(ff_class3083.getGirlName() + " " + ff_class3083.girlID());
                    }
                }
            }
        }
        this.sayMessage("tribe contains my exact reference: " + KoboldManager.n(uUID).contains(koboldEntity));
        this.sayMessage("tribe contains my ID: ");
        boolean bl2 = false;
        for (KoboldEntity ff_class3084 : KoboldManager.n(uUID)) {
            if (!ff_class3084.girlID().equals(koboldEntity.girlID())) continue;
            bl2 = true;
        }
        boolean bl = false;
        boolean bl3 = false;
        for (Map.Entry<UUID, BlockPos> entry : KoboldManager.a(uUID, entityPlayerSP.world).entrySet()) {
            if (!entry.getKey().equals(koboldEntity.girlID())) continue;
            bl = true;
        }
        this.sayMessage("loaded : " + bl2);
        this.sayMessage("saved : " + bl);
    }

    // TODO this is a very weird way of handling a command
    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void d(ClientChatEvent clientChatEvent) {
        //String[] stringArray;
        if (!ad_class25.a()) {
            return;
        }
        String string = clientChatEvent.getOriginalMessage().toLowerCase();
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if ("time".equals(string)) {
            ((Entity)entityPlayerSP).sendMessage(new TextComponentString(String.valueOf(entityPlayerSP.world.getTotalWorldTime())));
        }
        if ("girls".equals(string)) {
            List<GirlEntity> stringArray = entityPlayerSP.world.getEntities(GirlEntity.class, em_class2582 -> true);
            ((Entity)entityPlayerSP).sendMessage(new TextComponentString(String.valueOf(stringArray.size())));
            for (GirlEntity object : stringArray) {
                System.out.printf("%s at %s %s %s\n", object, object.posX, object.posY, object.posZ);
            }
        }
        if ("kobs".equals(string)) {
            UUID stringArray = KoboldManager.findTribeIdWith(entityPlayerSP.getPersistentID());
            int n = KoboldManager.h((UUID)stringArray);
            List<KoboldEntity> list = KoboldManager.n((UUID)stringArray);
            for (Object object : list) {
                this.sayMessage(String.format("alive member %s at %s world.isremote? %s isdead %s girlID %s entityID %s", ((KoboldEntity)object).getGirlName(), ((Entity)object).getPosition(), ((KoboldEntity)object).world.isRemote, ((KoboldEntity)object).isDead, ((GirlEntity)object).girlID(), ((Entity)object).getEntityId()));
                this.sayMessage(entityPlayerSP.world.getEntitiesWithinAABB(KoboldEntity.class, new AxisAlignedBB(((Entity)object).getPosition())).isEmpty() ? "couldn't be located" : "appears to actually exist");
            }
            HashMap<UUID, BlockPos> nullPointerException = KoboldManager.a((UUID)stringArray, entityPlayerSP.world);
            for (Map.Entry entry : nullPointerException.entrySet()) {
                this.sayMessage(String.format("saved pos of %s at %s", ((UUID)entry.getKey()).toString(), ((BlockPos)entry.getValue()).toString()));
            }
            this.sayMessage("total amount members: " + n);
        }
        if (string.startsWith("setcumtime ")) {
            long l;
            String[] stringArray = string.split(" ");
            try {
                l = Long.parseLong(stringArray[1]);
            } catch (NullPointerException nullPointerException) {
                System.out.println("long: " + stringArray[1]);
                nullPointerException.printStackTrace();
                return;
            }
            GalathMangTracker.setLastCumTime(entityPlayerSP.getPersistentID(), l);
            ((Entity)entityPlayerSP).sendMessage(new TextComponentString("set to: " + l));
        }
    }

    @SideOnly(value=Side.CLIENT)
    void sayMessage(String string) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(string));
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

