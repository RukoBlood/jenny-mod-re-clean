/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.trolmastercard.sexmod.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.trolmastercard.sexmod.girls.Galath.GalathMangTracker;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.Goblin.GoblinRenderer;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import com.trolmastercard.sexmod.girls.Kobold.KoboldRenderer;
import com.trolmastercard.sexmod.girls.Kobold.KoboldTaskInfo;
import com.trolmastercard.sexmod.girls.Goblin.PlayerGoblinRenderer;
import com.trolmastercard.sexmod.girls.Kobold.PlayerKoboldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

public class DebugMode {
    final static int DEV_FLOATS_COUNT = 60;
    static public float[] devDebugFloats;

    public DebugMode() {
        if (DebugMode.GetEnv()) {
            devDebugFloats = new float[DEV_FLOATS_COUNT];
        }
    }

    public static boolean GetEnv() {
        return (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onChatResetColors(ClientChatEvent event) {
        if (!DebugMode.GetEnv()) {
            return;
        }
        if (!"resetcolor".equalsIgnoreCase(event.getMessage())) {
            return;
        }
        KoboldRenderer.ResetColors();
        PlayerKoboldRenderer.ResetColors();
        GoblinRenderer.ResetColors();
        PlayerGoblinRenderer.ResetColors();
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onChatSendDebugValue(ClientChatEvent event) {
        float newValue;
        int index;
        if (!DebugMode.GetEnv()) {
            return;
        }
        String originalMessage = event.getOriginalMessage();
        String[] args = originalMessage.split(" ");
        if (args.length != 3) {
            return;
        }
        if (!"set".equalsIgnoreCase(args[0])) {
            return;
        }
        try {
            index = Integer.parseInt(args[1]);
            newValue = Float.parseFloat(args[2]);
            if (devDebugFloats.length - 1 < index) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("%sSet dev float N.%s from %s to %s", new Object[]{TextFormatting.GRAY, index, Float.valueOf(devDebugFloats[index]), Float.valueOf(newValue)})));
        DebugMode.devDebugFloats[index] = newValue;
        event.setCanceled(true);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onChatGetDebugValue(ClientChatEvent event) {
        int index;
        if (!DebugMode.GetEnv()) {
            return;
        }
        String originalMessage = event.getOriginalMessage();
        String[] args = originalMessage.split(" ");
        if (args.length != 2) {
            return;
        }
        if (!"get".equalsIgnoreCase(args[0])) {
            return;
        }
        try {
            index = Integer.parseInt(args[1]);
            if (devDebugFloats.length - 1 < index) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("%sdev float N.%s is %s", new Object[]{TextFormatting.YELLOW, index, devDebugFloats[index]})));
        event.setCanceled(true);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
//    public void onLivingHurtDebug(LivingHurtEvent event) {
//        // TODO this class is useless
//        //if (true)
//        //    return;
//
//        if (!DebugMode.GetEnv()) {
//            return;
//        }
//        EntityPlayerSP player = Minecraft.getMinecraft().player;
//        EntityLivingBase target = event.getEntityLiving();
//        if (!(target instanceof KoboldEntity)) {
//            return;
//        }
//        KoboldEntity kobold = (KoboldEntity)target;
//        // TODO something is broken in kobold,
//        //  because some kind of tribe identification with PLAYER ID results in null
////        UUID tribeID = KoboldManager.findTribeIdWith(player.getPersistentID());
////        {
////            Collection<KoboldTaskInfo> tribeTasks = KoboldManager.getTribeTasks(tribeID);
////            if (tribeTasks != null) {
////                for (KoboldTaskInfo task : tribeTasks) {
////                    this.sayMessage("task: " + task.getTaskType().name());
////                    this.sayMessage("workers involved: ");
////                    for (KoboldEntity worker : task.getAssignedWorkers()) {
////                        this.sayMessage(worker.getGirlName() + " " + worker.girlID());
////                    }
////                }
////            }
////        }
//
//        /*
//        * I'm starting debugging
//        * This is a debug code
//        */
//
//        UUID tribeID = KoboldManager.findTribeIdWith(player.getPersistentID());
//        if (tribeID != null) {
//            Collection<KoboldTaskInfo> tribeTasks = KoboldManager.getTribeTasks(tribeID);
//            if (tribeTasks != null && !tribeTasks.isEmpty()) {
//                for (KoboldTaskInfo task : tribeTasks) {
//                    if (task == null) {
//                        this.sayMessage("task: [NULL TASK OBJECT]");
//                        continue;
//                    }
//                    String taskName = (task.getTaskType() != null) ? task.getTaskType().name() : "UNKNOWN_TYPE";
//                    this.sayMessage("task: " + taskName);
//                    this.sayMessage("workers involved: ");
//                    List<KoboldEntity> workers = task.getAssignedWorkers();
//                    if (workers == null || workers.isEmpty()) {
//                        this.sayMessage("  (none)");
//                        continue;
//                    }
//                    for (KoboldEntity worker : workers) {
//                        if (worker == null) {
//                            this.sayMessage("  [NULL WORKER - Unloaded or Dead]");
//                        } else {
//                            this.sayMessage("  " + worker.getGirlName() + " (UUID: " + worker.girlID() + ")");
//                        }
//                    }
//                }
//            } else {
//                this.sayMessage("No tasks found for tribe: " + tribeID);
//            }
//        } else {
//            this.sayMessage("Tribe not found for player: " + player.getName());
//        }
//        //End of debug code
//
//
//        this.sayMessage("tribe contains my exact reference: " + KoboldManager.getTribeMembersList(tribeID).contains(kobold));
//        this.sayMessage("tribe contains my ID: ");
//        boolean containsRef = false;
//        for (KoboldEntity member : KoboldManager.getTribeMembersList(tribeID)) {
//            if (!member.girlID().equals(kobold.girlID())) continue;
//            containsRef = true;
//        }
//        boolean containsSavedPos = false;
////        boolean bl3 = false;
//        for (Map.Entry<UUID, BlockPos> entry : KoboldManager.getUnloadedMembersMap(tribeID, player.world).entrySet()) {
//            if (!entry.getKey().equals(kobold.girlID())) continue;
//            containsSavedPos = true;
//        }
//        this.sayMessage("loaded : " + containsRef);
//        this.sayMessage("saved : " + containsSavedPos);
//    }

    public void onLivingHurtDebug(LivingHurtEvent event) {
        if (!DebugMode.GetEnv()) {
            return;
        }

        EntityLivingBase target = event.getEntityLiving();
        if (!(target instanceof KoboldEntity)) {
            return;
        }
        KoboldEntity kobold = (KoboldEntity) target;

        // Безопасный поиск ближайшего игрока в том же мире
        EntityPlayer player = kobold.world.getClosestPlayerToEntity(kobold, 50.0D);
        if (player == null) {
            System.out.println("[DebugMode] Kobold hurt, but no player nearby.");
            return;
        }

        UUID tribeID = KoboldManager.findTribeIdWith(player.getPersistentID());

        if (tribeID != null) {
            Collection<KoboldTaskInfo> tribeTasks = KoboldManager.getTribeTasks(tribeID);
            if (tribeTasks != null && !tribeTasks.isEmpty()) {
                for (KoboldTaskInfo task : tribeTasks) {
                    if (task == null) {
                        System.out.println("[DebugMode] task: [NULL TASK OBJECT]");
                        continue;
                    }
                    String taskName = (task.getTaskType() != null) ? task.getTaskType().name() : "UNKNOWN_TYPE";
                    System.out.println("[DebugMode] task: " + taskName);
                    System.out.println("[DebugMode] workers involved: ");

                    List<KoboldEntity> workers = task.getAssignedWorkers();
                    if (workers == null || workers.isEmpty()) {
                        System.out.println("[DebugMode]   (none)");
                        continue;
                    }
                    for (KoboldEntity worker : workers) {
                        if (worker == null) {
                            System.out.println("[DebugMode]   [NULL WORKER - Unloaded or Dead]");
                        } else {
                            System.out.println("[DebugMode]   " + worker.getGirlName() + " (UUID: " + worker.girlID() + ")");
                        }
                    }
                }
            } else {
                System.out.println("[DebugMode] No tasks found for tribe: " + tribeID);
            }

            // Блок проверки членов племени
            List<KoboldEntity> membersList = KoboldManager.getTribeMembersList(tribeID);
            boolean containsExactRef = membersList != null && membersList.contains(kobold);
            System.out.println("[DebugMode] tribe contains my exact reference: " + containsExactRef);

            boolean containsRef = false;
            if (membersList != null) {
                for (KoboldEntity member : membersList) {
                    if (member != null && member.girlID() != null && member.girlID().equals(kobold.girlID())) {
                        containsRef = true;
                        break;
                    }
                }
            }

            boolean containsSavedPos = false;
            Map<UUID, BlockPos> unloadedMap = KoboldManager.getUnloadedMembersMap(tribeID, kobold.world);
            if (unloadedMap != null) {
                for (Map.Entry<UUID, BlockPos> entry : unloadedMap.entrySet()) {
                    if (entry.getKey() != null && entry.getKey().equals(kobold.girlID())) {
                        containsSavedPos = true;
                        break;
                    }
                }
            }

            System.out.println("[DebugMode] loaded : " + containsRef);
            System.out.println("[DebugMode] saved : " + containsSavedPos);

        } else {
            System.out.println("[DebugMode] Tribe not found for player: " + player.getName());
        }
    }

    // TODO this is a very weird way of handling a command
    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onDebugCommandInterpreter(ClientChatEvent event) {
        //String[] stringArray;
        if (!DebugMode.GetEnv()) {
            return;
        }

        String message = event.getOriginalMessage().toLowerCase();
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if ("time".equals(message)) {
            ((Entity)player).sendMessage(new TextComponentString(String.valueOf(player.world.getTotalWorldTime())));
        }

        if ("girls".equals(message)) {
            List<GirlEntity> activeGirls = player.world.getEntities(GirlEntity.class, entity -> true);
            ((Entity)player).sendMessage(new TextComponentString(String.valueOf(activeGirls.size())));
            for (GirlEntity object : activeGirls) {
                System.out.printf("%s at %s %s %s\n", object, object.posX, object.posY, object.posZ);
            }
        }

        if ("kobs".equals(message)) {
            UUID tribeID = KoboldManager.findTribeIdWith(player.getPersistentID());
            int totalMembersCount = KoboldManager.getTribeMemberCount((UUID)tribeID);
            List<KoboldEntity> aliveMembers = KoboldManager.getTribeMembersList((UUID)tribeID);

            for (Object members : aliveMembers) {
                this.sayMessage(String.format("alive member %s at %s world.isremote? %s isdead %s girlID %s entityID %s", ((KoboldEntity)members).getGirlName(), ((Entity)members).getPosition(), ((KoboldEntity)members).world.isRemote, ((KoboldEntity)members).isDead, ((GirlEntity)members).girlID(), ((Entity)members).getEntityId()));
                this.sayMessage(player.world.getEntitiesWithinAABB(KoboldEntity.class, new AxisAlignedBB(((Entity)members).getPosition())).isEmpty() ? "couldn't be located" : "appears to actually exist");
            }

            HashMap<UUID, BlockPos> savedPositions = KoboldManager.getUnloadedMembersMap((UUID)tribeID, player.world);
            for (Map.Entry entry : savedPositions.entrySet()) {
                this.sayMessage(String.format("saved pos of %s at %s", ((UUID)entry.getKey()).toString(), ((BlockPos)entry.getValue()).toString()));
            }
            this.sayMessage("total amount members: " + totalMembersCount);
        }

        if (message.startsWith("setcumtime ")) {
            long cumtime;
            String[] args = message.split(" ");
            try {
                cumtime = Long.parseLong(args[1]);
            } catch (NullPointerException e) {
                System.out.println("long: " + args[1]);
                e.printStackTrace();
                return;
            }
            GalathMangTracker.setLastCumTime(player.getPersistentID(), cumtime);
            ((Entity)player).sendMessage(new TextComponentString("set to: " + cumtime));
        }
    }

    @SideOnly(value=Side.CLIENT)
    void sayMessage(String string) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(string));
    }
}

