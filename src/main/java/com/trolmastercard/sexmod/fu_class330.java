/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.GuiScreenEvent$ActionPerformedEvent
 *  net.minecraftforge.client.event.GuiScreenEvent$InitGuiEvent
 *  net.minecraftforge.event.entity.living.LivingDamageEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.minecraftforge.event.entity.player.PlayerSleepInBedEvent
 *  net.minecraftforge.event.world.GetCollisionBoxesEvent
 *  net.minecraftforge.fml.common.eventhandler.Event$Result
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.PlayerEvent$PlayerRespawnEvent
 */
package com.trolmastercard.sexmod;

import java.util.ArrayList;
import java.util.List;

import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.girls.GirlEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class fu_class330 {
    final static int a = 284453;

    @SubscribeEvent
    public void a(PlayerSleepInBedEvent playerSleepInBedEvent) {
        EntityPlayer entityPlayer = playerSleepInBedEvent.getEntityPlayer();
        PlayerGirl ei_class2512 = PlayerGirl.GetPlayer(entityPlayer);
        if (ei_class2512 == null) {
            return;
        }
        if (!entityPlayer.isSneaking()) {
            return;
        }
        playerSleepInBedEvent.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
    }

    @SubscribeEvent
    public void a(GetCollisionBoxesEvent getCollisionBoxesEvent) {
    }

    @SubscribeEvent
    public void a(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        PlayerGirl ei_class2512 = PlayerGirl.d_(rightClickBlock.getEntityPlayer().getPersistentID());
        BlockPos blockPos = rightClickBlock.getPos();
        World world = rightClickBlock.getEntityPlayer().world;
        EntityPlayer entityPlayer = rightClickBlock.getEntityPlayer();
        if (ei_class2512 == null) {
            return;
        }
        if (!ei_class2512.boolean_v()) {
            return;
        }
        if (!cj_class134.a(world, blockPos, rightClickBlock.getHitVec(), rightClickBlock.getFace(), entityPlayer)) {
            return;
        }
        if (ei_class2512.getDataManager().get(GirlEntity.G).booleanValue()) {
            rightClickBlock.setCanceled(true);
            return;
        }
        if (!entityPlayer.isSneaking()) {
            return;
        }
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        if (world.getBlockState(blockPos.north()).getBlock() == Blocks.AIR) {
            arrayList.add(blockPos.north());
        }
        if (world.getBlockState(blockPos.east()).getBlock() == Blocks.AIR) {
            arrayList.add(blockPos.east());
        }
        if (world.getBlockState(blockPos.south()).getBlock() == Blocks.AIR) {
            arrayList.add(blockPos.south());
        }
        if (world.getBlockState(blockPos.west()).getBlock() == Blocks.AIR) {
            arrayList.add(blockPos.west());
        }
        Vec3i vec3i = null;
        for (BlockPos blockPos2 : arrayList) {
            double d;
            if (vec3i == null) {
                vec3i = blockPos2;
                continue;
            }
            Vec3d vec3d = entityPlayer.getPositionVector();
            double d2 = this.a(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), vec3d.x, vec3d.y, vec3d.z);
            if (!(d2 < (d = this.a(vec3i.getX(), vec3i.getY(), vec3i.getZ(), vec3d.x, vec3d.y, vec3d.z)))) continue;
            vec3i = blockPos2;
        }
        if (vec3i == null) {
            entityPlayer.sendMessage(new TextComponentString("Bed is obscured"));
            return;
        }
        entityPlayer.setPosition((double)vec3i.getX() + 0.5, vec3i.getY(), (double)vec3i.getZ() + 0.5);
        if (blockPos.north().equals(vec3i)) {
            entityPlayer.rotationYaw = 0.0f;
        }
        if (blockPos.east().equals(vec3i)) {
            entityPlayer.rotationYaw = 90.0f;
        }
        if (blockPos.south().equals(vec3i)) {
            entityPlayer.rotationYaw = 180.0f;
        }
        if (blockPos.west().equals(vec3i)) {
            entityPlayer.rotationYaw = -90.0f;
        }
        if (rightClickBlock.getWorld().isRemote) {
            HandlePlayerMovement.a(false);
            ei_class2512.void_H();
            return;
        }
        ei_class2512.c(new Vec3d((double)vec3i.getX() + 0.5, (float)vec3i.getY() + 0.0f, (double)vec3i.getZ() + 0.5));
        ei_class2512.void_b(entityPlayer.rotationYaw);
        ei_class2512.getDataManager().set(GirlEntity.G, true);
        ei_class2512.u_();
    }

    double a(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d - d4;
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        return Math.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
    }

    @SubscribeEvent
    public void a(PlayerEvent.PlayerRespawnEvent playerRespawnEvent) {
        EntityPlayer entityPlayer = playerRespawnEvent.player;
        if (entityPlayer == null) {
            return;
        }
        PlayerGirl ei_class2512 = PlayerGirl.com_trolmastercard_sexmod_ei_class251_a(entityPlayer.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        Vec3d vec3d = entityPlayer.getPositionVector();
        ei_class2512.dimension = entityPlayer.dimension;
        ei_class2512.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z);
        ei_class2512.updateAITasks();
        System.out.println(entityPlayer.world.isAreaLoaded(ei_class2512.getPosition(), 2));
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void b(PlayerInteractEvent.EntityInteract entityInteract) {
        if (!(entityInteract.getTarget() instanceof EntityPlayer)) {
            return;
        }
        if (entityInteract.getEntityPlayer().isSneaking()) {
            return;
        }
        if (!entityInteract.getEntityPlayer().getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID())) {
            return;
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        PlayerGirl ei_class2512 = PlayerGirl.d_(entityPlayerSP.getPersistentID());
        EntityPlayer entityPlayer = (EntityPlayer)entityInteract.getTarget();
        PlayerGirl ei_class2513 = PlayerGirl.GetPlayer(entityPlayer);
        if (ei_class2513 == null) {
            return;
        }
        if (ei_class2512 != null) {
            ((EntityPlayer)entityPlayerSP).sendStatusMessage(new TextComponentString("no lesbo yet owo"), true);
            return;
        }
        if (!ei_class2513.boolean_l()) {
            return;
        }
        if (ei_class2513.boolean_p()) {
            ei_class2513.boolean_b(Minecraft.getMinecraft().player);
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(PlayerInteractEvent.EntityInteract entityInteract) {
        if (!(entityInteract.getTarget() instanceof EntityPlayer)) {
            return;
        }
        if (!entityInteract.getEntityPlayer().getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID())) {
            return;
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        PlayerGirl ei_class2512 = PlayerGirl.d_(entityPlayerSP.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)entityInteract.getTarget();
        PlayerGirl ei_class2513 = PlayerGirl.d_(entityPlayer.getPersistentID());
        if (ei_class2513 != null) {
            entityPlayer.sendStatusMessage(new TextComponentString("no lesbo yet owo"), true);
            return;
        }
        if (ei_class2512.boolean_p()) {
            ei_class2512.ab = false;
            ei_class2512.boolean_b(entityPlayer);
        }
    }

    @SubscribeEvent
    public void b(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        EntityPlayer entityPlayer = rightClickBlock.getEntityPlayer();
        PlayerGirl ei_class2512 = PlayerGirl.GetPlayer(entityPlayer);
        if (ei_class2512 == null) {
            return;
        }
        if (!(ei_class2512 instanceof PlayerSlime)) {
            return;
        }
        if (!entityPlayer.isSneaking()) {
            return;
        }
        if (!entityPlayer.getHeldItemMainhand().equals(ItemStack.EMPTY)) {
            return;
        }
        if (ei_class2512.getDataManager().get(GirlEntity.G).booleanValue()) {
            return;
        }
        if (entityPlayer.rotationPitch < 20.0f) {
            return;
        }
        Vec3d vec3d = rightClickBlock.getHitVec();
        if (vec3d == null) {
            return;
        }
        Vec3d vec3d2 = new Vec3d(vec3d.x, Math.floor(vec3d.y) + 0.0, vec3d.z);
        if (vec3d.distanceTo(entityPlayer.getPositionVector()) > 3.0) {
            return;
        }
        entityPlayer.setPosition(vec3d2.x, Math.floor(vec3d.y), vec3d2.z);
        ei_class2512.c(vec3d2);
        ei_class2512.void_b(entityPlayer.rotationYaw);
        ei_class2512.getDataManager().set(GirlEntity.G, true);
        ei_class2512.getDataManager().set(GirlEntity.D, 0);
        ei_class2512.setCurrentAction(Action.STARTDOGGY);
        if (rightClickBlock.getWorld().isRemote && Minecraft.getMinecraft().player.getPersistentID().equals(entityPlayer.getPersistentID())) {
            HandlePlayerMovement.a(false);
        }
    }

    @SubscribeEvent
    public void a(LivingHurtEvent livingHurtEvent) {
        if (!(livingHurtEvent.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        if (livingHurtEvent.getSource() != DamageSource.FALL) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)livingHurtEvent.getEntityLiving();
        PlayerGirl ei_class2512 = PlayerGirl.GetPlayer(entityPlayer);
        if (ei_class2512 == null) {
            return;
        }
        if (ei_class2512 instanceof PlayerAllie || ei_class2512 instanceof PlayerBee) {
            livingHurtEvent.setCanceled(true);
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(GuiScreenEvent.InitGuiEvent initGuiEvent) {
        GuiScreen guiScreen = initGuiEvent.getGui();
        if (!(guiScreen instanceof GuiInventory) && !(guiScreen instanceof GuiContainerCreative)) {
            return;
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        if (entityPlayerSP == null) {
            return;
        }
        PlayerGirl ei_class2512 = PlayerGirl.GetPlayer(entityPlayerSP);
        if (ei_class2512 == null) {
            return;
        }
        if (ei_class2512.boolean_A()) {
            return;
        }
        List list = initGuiEvent.getButtonList();
        String string = I18n.format(ei_class2512.int_ah() == 0 ? "action.names.dressup" : "action.names.strip", new Object[0]);
        list.add(new GuiButton(284453, (int)((double)guiScreen.width * 0.5 - 35.0), (int)((double)guiScreen.height * 0.87), 70, 20, string));
        initGuiEvent.setButtonList(list);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void a(GuiScreenEvent.ActionPerformedEvent actionPerformedEvent) {
        GuiScreen guiScreen = actionPerformedEvent.getGui();
        if (!(guiScreen instanceof GuiInventory) && !(guiScreen instanceof GuiContainerCreative)) {
            return;
        }
        if (actionPerformedEvent.getButton().id != 284453) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        PlayerGirl ei_class2512 = PlayerGirl.d_(minecraft.player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        if (ei_class2512.boolean_A()) {
            return;
        }
        if (ei_class2512.getID() != null) {
            return;
        }
        if (ei_class2512.currentAction() != Action.NULL) {
            return;
        }
        minecraft.gameSettings.thirdPersonView = 2;
        minecraft.entityRenderer.loadEntityShader(null);
        ei_class2512.setCurrentAction(Action.STRIP);
        HandlePlayerMovement.a(false);
        minecraft.player.closeScreen();
    }

    @SubscribeEvent
    public void a(LivingDamageEvent livingDamageEvent) {
        if (livingDamageEvent.getSource() != DamageSource.FALL) {
            return;
        }
        EntityLivingBase entityLivingBase = livingDamageEvent.getEntityLiving();
        if (!(entityLivingBase instanceof EntityPlayer)) {
            return;
        }
        PlayerGirl ei_class2512 = PlayerGirl.d_(entityLivingBase.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        if (ei_class2512 instanceof PlayerSlime) {
            livingDamageEvent.setResult(Event.Result.DENY);
            livingDamageEvent.setAmount(0.0f);
            livingDamageEvent.setCanceled(true);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

