/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.NetworkRegistry
 *  net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
 */
package com.trolmastercard.sexmod.util.Handlers;

import com.trolmastercard.sexmod.Packets.*;

import com.trolmastercard.sexmod.Packets.SendChatMessage;
import com.trolmastercard.sexmod.Packets.SendGirlToSex;
import com.trolmastercard.sexmod.Packets.SetPlayerForGirl;
import com.trolmastercard.sexmod.Packets.SyncActionPacket;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PackageHandler {
    static public SimpleNetworkWrapper INSTANCE;
    static private int discriminant;

    private static int discriminator() {
        return discriminant++;
    }

    public static void RegisterMessages() {
        INSTANCE = net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.newSimpleChannel("sexmodchannel");
        INSTANCE.registerMessage(SendChatMessage.Handler.class, SendChatMessage.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SendChatMessage.Handler.class, SendChatMessage.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SetPlayerMovement.Handler.class, SetPlayerMovement.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(TeleportPlayer.Handler.class, TeleportPlayer.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SendGirlToSex.Handler.class, SendGirlToSex.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SetPlayerForGirl.Handler.class, SetPlayerForGirl.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SyncActionPacket.Handler.class, SyncActionPacket.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(ResetController.Handler.class, ResetController.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(ResetController.Handler.class, ResetController.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(ResetGirl.EventHandler.class, ResetGirl.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(ChangeDataParameter.Handler.class, ChangeDataParameter.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(PlayerAction.Handler.class, PlayerAction.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SendCompanionHome.Handler.class, SendCompanionHome.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SetNewHome.Handler.class, SetNewHome.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(UploadInventoryToServer.Handler.class, UploadInventoryToServer.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(RemoveItems.Handler.class, RemoveItems.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SummonAllie.Handler.class, SummonAllie.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(UploadInventoryToServerAlt.Handler.class, UploadInventoryToServerAlt.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(MakeRichWish.Handler.class, MakeRichWish.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(UpdatePlayerModel.Handler.class, UpdatePlayerModel.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SexPrompt.Handler.class, SexPrompt.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SexPrompt.Handler.class, SexPrompt.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(StartStandingSexAnimation.Handler.class, StartStandingSexAnimation.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(BeeOpenChest.Handler.class, BeeOpenChest.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(CatActivateFishing.Handler.class, CatActivateFishing.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(CatEatingDone.Handler.class, CatEatingDone.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(CatThrowAwayItem.Handler.class, CatThrowAwayItem.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(ClaimTribe.Handler.class, ClaimTribe.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(GetTribeUIValues.Handler.class, GetTribeUIValues.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(GetTribeUIValues.Handler.class, GetTribeUIValues.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SetTribeFollowMode.Handler.class, SetTribeFollowMode.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(FallTree.Handler.class, FallTree.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SendBlocks.Handler.class, SendBlocks.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SendBlocks.Handler.class, SendBlocks.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(CancelTask.handler.class, CancelTask.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SpawnParticle.Handler.class, SpawnParticle.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SendEgg.Handler.class, SendEgg.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(Mine.Handler.class, Mine.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(GenderChangePacket.Handler.class, GenderChangePacket.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(ForcePlayerGirlUpdate.Handler.class, ForcePlayerGirlUpdate.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(UploadModelString.Handler.class, UploadModelString.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(GalathRapePounce.Handler.class, GalathRapePounce.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(UpdateVelocity.Handler.class, UpdateVelocity.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(RequestServerModelAvailability.Handler.class, RequestServerModelAvailability.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(RequestServerModelAvailability.Handler.class, RequestServerModelAvailability.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(DownloadServerModel.Handler.class, DownloadServerModel.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(DownloadServerModel.Handler.class, DownloadServerModel.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SpawnEnergyBallParticlesPacket2.Handler.class, SpawnEnergyBallParticlesPacket2.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(GalathBackOffRape.Handler.class, GalathBackOffRape.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(InformOfOwnership.Handler.class, InformOfOwnership.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(RequestRiding.Handler.class, RequestRiding.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SpawnEnergyBallParticles.Handler.class, SpawnEnergyBallParticles.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SetPlayerCam.Handler.class, SetPlayerCam.class, PackageHandler.discriminator(), Side.CLIENT);
    }

    static {
        discriminant = 0;
    }
}

