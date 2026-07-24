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
package com.trolmastercard.sexmod.events;

import java.util.ArrayList;
import java.util.List;

import com.trolmastercard.sexmod.Action;
import com.trolmastercard.sexmod.world.WorldUtils;
import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.Allie.PlayerAllie;
import com.trolmastercard.sexmod.girls.Bee.PlayerBee;
import com.trolmastercard.sexmod.girls.PlayerGirl;
import com.trolmastercard.sexmod.girls.Slime.PlayerSlime;
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

public class PlayerGirlEvents {
    final static int STRIP_BUTTON_ID = 284453;

    @SubscribeEvent
    public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        PlayerGirl playerGirl = PlayerGirl.GetPlayer(player);
        if (playerGirl == null) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }
        event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
    }

    @SubscribeEvent
    public void onGetCollisionBoxes(GetCollisionBoxesEvent event) {
    }

    @SubscribeEvent
    public void onRightClickBedBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(event.getEntityPlayer().getPersistentID());
        BlockPos pos = event.getPos();
        World world = event.getEntityPlayer().world;
        EntityPlayer player = event.getEntityPlayer();

        if (playerGirl == null) {
            return;
        }
        if (!playerGirl.shouldRenderArmor()) {
            return;
        }
        if (!WorldUtils.checkBedBlock(world, pos, event.getHitVec(), event.getFace(), player)) {
            return;
        }
        if (playerGirl.getDataManager().get(GirlEntity.IS_ANCHORED)) {
            event.setCanceled(true);
            return;
        }
        if (!player.isSneaking()) {
            return;
        }

        ArrayList<BlockPos> adjacentAirBlocks = new ArrayList<BlockPos>();
        if (world.getBlockState(pos.north()).getBlock() == Blocks.AIR) {
            adjacentAirBlocks.add(pos.north());
        }
        if (world.getBlockState(pos.east()).getBlock() == Blocks.AIR) {
            adjacentAirBlocks.add(pos.east());
        }
        if (world.getBlockState(pos.south()).getBlock() == Blocks.AIR) {
            adjacentAirBlocks.add(pos.south());
        }
        if (world.getBlockState(pos.west()).getBlock() == Blocks.AIR) {
            adjacentAirBlocks.add(pos.west());
        }

        Vec3i nearestPos = null;
        for (BlockPos airPos : adjacentAirBlocks) {
            double distToBest;
            if (nearestPos == null) {
                nearestPos = airPos;
                continue;
            }
            Vec3d playerPos = player.getPositionVector();
            double distToCurrent = this.getDistance(airPos.getX(), airPos.getY(), airPos.getZ(), playerPos.x, playerPos.y, playerPos.z);
            if (!(distToCurrent < (distToBest = this.getDistance(nearestPos.getX(), nearestPos.getY(), nearestPos.getZ(), playerPos.x, playerPos.y, playerPos.z)))) continue;
            nearestPos = airPos;
        }

        if (nearestPos == null) {
            player.sendMessage(new TextComponentString("Bed is obscured"));
            return;
        }

        player.setPosition((double)nearestPos.getX() + 0.5, nearestPos.getY(), (double)nearestPos.getZ() + 0.5);
        if (pos.north().equals(nearestPos)) {
            player.rotationYaw = 0.0f;
        }
        if (pos.east().equals(nearestPos)) {
            player.rotationYaw = 90.0f;
        }
        if (pos.south().equals(nearestPos)) {
            player.rotationYaw = 180.0f;
        }
        if (pos.west().equals(nearestPos)) {
            player.rotationYaw = -90.0f;
        }
        if (event.getWorld().isRemote) {
            HandlePlayerMovement.setMovementLock(false);
            playerGirl.beeOpenGUI();
            return;
        }
        playerGirl.setTargetPosition(new Vec3d((double)nearestPos.getX() + 0.5, (float)nearestPos.getY() + 0.0f, (double)nearestPos.getZ() + 0.5));
        playerGirl.setYawRotation(player.rotationYaw);
        playerGirl.getDataManager().set(GirlEntity.IS_ANCHORED, true);
        playerGirl.u_();
    }

    double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        if (player == null) {
            return;
        }
        PlayerGirl playerGirl = PlayerGirl.getByPlayerUUID(player.getPersistentID());
        if (playerGirl == null) {
            return;
        }
        Vec3d playerPos = player.getPositionVector();
        playerGirl.dimension = player.dimension;
        playerGirl.setPositionAndUpdate(playerPos.x, playerPos.y, playerPos.z);
        playerGirl.updateAITasks();
        System.out.println(player.world.isAreaLoaded(playerGirl.getPosition(), 2));
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onInteractWithPlayerAsGirl(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof EntityPlayer)) {
            return;
        }
        if (event.getEntityPlayer().isSneaking()) {
            return;
        }
        if (!event.getEntityPlayer().getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID())) {
            return;
        }

        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        PlayerGirl localPlayerGirl = PlayerGirl.getUUIDHashtable(clientPlayer.getPersistentID());
        EntityPlayer targetPlayer = (EntityPlayer)event.getTarget();
        PlayerGirl targetPlayerGirl = PlayerGirl.GetPlayer(targetPlayer);

        if (targetPlayerGirl == null) {
            return;
        }
        if (localPlayerGirl != null) {
            ((EntityPlayer)clientPlayer).sendStatusMessage(new TextComponentString("no lesbo yet owo"), true);
            return;
        }
        if (!targetPlayerGirl.canInteract()) {
            return;
        }
        if (targetPlayerGirl.canOpenGUI()) {
            targetPlayerGirl.openGuiForPlayer(Minecraft.getMinecraft().player);
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onInteractWithPlayerAsPartner(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof EntityPlayer)) {
            return;
        }
        if (!event.getEntityPlayer().getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID())) {
            return;
        }
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        PlayerGirl localPlayerGirl = PlayerGirl.getUUIDHashtable(clientPlayer.getPersistentID());
        if (localPlayerGirl == null) {
            return;
        }
        EntityPlayer targetPlayer = (EntityPlayer)event.getTarget();
        PlayerGirl targetPlayerGirl = PlayerGirl.getUUIDHashtable(targetPlayer.getPersistentID());

        if (targetPlayerGirl != null) {
            targetPlayer.sendStatusMessage(new TextComponentString("no lesbo yet owo"), true);
            return;
        }
        if (localPlayerGirl.canOpenGUI()) {
            localPlayerGirl.guiPending = false;
            localPlayerGirl.openGuiForPlayer(targetPlayer);
        }
    }

    @SubscribeEvent
    public void onSlimeGroundDoggyTrigger(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        PlayerGirl playerGirl = PlayerGirl.GetPlayer(player);
        if (playerGirl == null) {
            return;
        }
        if (!(playerGirl instanceof PlayerSlime)) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }
        if (!player.getHeldItemMainhand().equals(ItemStack.EMPTY)) {
            return;
        }
        if (playerGirl.getDataManager().get(GirlEntity.IS_ANCHORED)) {
            return;
        }
        if (player.rotationPitch < 20.0f) {
            return;
        }

        Vec3d hitVec = event.getHitVec();
        if (hitVec == null) {
            return;
        }
        Vec3d targetPos = new Vec3d(hitVec.x, Math.floor(hitVec.y) + 0.0, hitVec.z);
        if (hitVec.distanceTo(player.getPositionVector()) > 3.0) {
            return;
        }

        player.setPosition(targetPos.x, Math.floor(hitVec.y), targetPos.z);
        playerGirl.setTargetPosition(targetPos);
        playerGirl.setYawRotation(player.rotationYaw);
        playerGirl.getDataManager().set(GirlEntity.IS_ANCHORED, true);
        playerGirl.getDataManager().set(GirlEntity.OUTFIT_INDEX, 0);
        playerGirl.setCurrentAction(Action.STARTDOGGY);
        if (event.getWorld().isRemote && Minecraft.getMinecraft().player.getPersistentID().equals(player.getPersistentID())) {
            HandlePlayerMovement.setMovementLock(false);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        if (event.getSource() != DamageSource.FALL) {
            return;
        }
        EntityPlayer player = (EntityPlayer)event.getEntityLiving();
        PlayerGirl playerGirl = PlayerGirl.GetPlayer(player);
        if (playerGirl == null) {
            return;
        }
        if (playerGirl instanceof PlayerAllie || playerGirl instanceof PlayerBee) {
            event.setCanceled(true);
        }
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent event) {
        GuiScreen gui = event.getGui();
        if (!(gui instanceof GuiInventory) && !(gui instanceof GuiContainerCreative)) {
            return;
        }
        EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        if (clientPlayer == null) {
            return;
        }
        PlayerGirl playerGirl = PlayerGirl.GetPlayer(clientPlayer);
        if (playerGirl == null) {
            return;
        }
        if (playerGirl.useVanillaItemHolding()) {
            return;
        }
        List buttonList = event.getButtonList();
        String buttonText = I18n.format(playerGirl.getOutfitIndex() == 0 ? "action.names.dressup" : "action.names.strip");
        buttonList.add(new GuiButton(
                STRIP_BUTTON_ID,
                (int)((double)gui.width * 0.5 - 35.0),
                (int)((double)gui.height * 0.87),
                70,
                20,
                buttonText)
        );

        event.setButtonList(buttonList);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public void onGuiActionPerformed(GuiScreenEvent.ActionPerformedEvent event) {
        GuiScreen gui = event.getGui();
        if (!(gui instanceof GuiInventory) && !(gui instanceof GuiContainerCreative)) {
            return;
        }
        if (event.getButton().id != STRIP_BUTTON_ID) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(mc.player.getPersistentID());
        if (playerGirl == null) {
            return;
        }
        if (playerGirl.useVanillaItemHolding()) {
            return;
        }
        if (playerGirl.getID() != null) {
            return;
        }
        if (playerGirl.currentAction() != Action.NULL) {
            return;
        }

        mc.gameSettings.thirdPersonView = 2;
        mc.entityRenderer.loadEntityShader(null);
        playerGirl.setCurrentAction(Action.STRIP);
        HandlePlayerMovement.setMovementLock(false);
        mc.player.closeScreen();
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource() != DamageSource.FALL) {
            return;
        }
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(entity.getPersistentID());
        if (playerGirl == null) {
            return;
        }
        if (playerGirl instanceof PlayerSlime) {
            event.setResult(Event.Result.DENY);
            event.setAmount(0.0f);
            event.setCanceled(true);
        }
    }
}

