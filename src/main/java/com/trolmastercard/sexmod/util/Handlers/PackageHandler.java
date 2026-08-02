/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.NetworkRegistry
 *  net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
 */
package com.trolmastercard.sexmod.util.Handlers;

import com.trolmastercard.sexmod.Packages.*;

import com.trolmastercard.sexmod.Packages.SendChatMessage;
import com.trolmastercard.sexmod.Packages.SendGirlToSex;
import com.trolmastercard.sexmod.Packages.SetPlayerForGirl;
import com.trolmastercard.sexmod.Packages.SyncActionPacket;
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
        INSTANCE.registerMessage(SendGirlToSex.a_inner24.class, SendGirlToSex.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SetPlayerForGirl.a_inner59.class, SetPlayerForGirl.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SyncActionPacket.Handler.class, SyncActionPacket.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(ResetController.Handler.class, ResetController.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(ResetController.Handler.class, ResetController.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(ResetGirl.a_inner422.class, ResetGirl.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(ChangeDataParameter.Handler.class, ChangeDataParameter.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(bo_class90.Handler.class, bo_class90.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SendCompanionHome.Handler.class, SendCompanionHome.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SetNewHome.a_inner14.class, SetNewHome.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(UploadInventoryToServer.a_inner61.class, UploadInventoryToServer.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(RemoveItems.a_inner424.class, RemoveItems.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SummonAllie.a_inner81.class, SummonAllie.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(UploadInventoryToServerAlt.a_inner155.class, UploadInventoryToServerAlt.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(MakeRichWish.a_inner104.class, MakeRichWish.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(UpdatePlayerModel.a_inner72.class, UpdatePlayerModel.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SexPrompt.Handler.class, SexPrompt.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SexPrompt.Handler.class, SexPrompt.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(StartStandingSexAnimation.a_inner274.class, StartStandingSexAnimation.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(BeeOpenChest.a_inner288.class, BeeOpenChest.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(CatActivateFishing.Handler.class, CatActivateFishing.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(CatEatingDone.a_inner374.class, CatEatingDone.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(CatThrowAwayItem.a_inner198.class, CatThrowAwayItem.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(ClaimTribe.a_inner355.class, ClaimTribe.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(GetTribeUIValues.Handler.class, GetTribeUIValues.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(GetTribeUIValues.Handler.class, GetTribeUIValues.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SetTribeFollowMode.a_inner316.class, SetTribeFollowMode.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(FallTree.a_inner303.class, FallTree.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SendBlocks.Handler.class, SendBlocks.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SendBlocks.Handler.class, SendBlocks.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(CancelTask.a_inner45.class, CancelTask.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(SpawnParticle.a_inner261.class, SpawnParticle.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SendEgg.a_inner434.class, SendEgg.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(Mine.a_inner227.class, Mine.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(bd_class76.Handler.class, bd_class76.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(ForcePlayerGirlUpdate.a_inner362.class, ForcePlayerGirlUpdate.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(UploadModelString.a_inner333.class, UploadModelString.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(GalathRapePounce.a_inner357.class, GalathRapePounce.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(UpdateVelocity.a_inner145.class, UpdateVelocity.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(RequestServerModelAvailability.a_inner351.class, RequestServerModelAvailability.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(RequestServerModelAvailability.a_inner351.class, RequestServerModelAvailability.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(DownloadServerModel.a_inner147.class, DownloadServerModel.class, PackageHandler.discriminator(), Side.SERVER);
        INSTANCE.registerMessage(DownloadServerModel.a_inner147.class, DownloadServerModel.class, PackageHandler.discriminator(), Side.CLIENT);
        INSTANCE.registerMessage(SpawnEnergyBallParticlesAlt.a_inner102.class, SpawnEnergyBallParticlesAlt.class, PackageHandler.discriminator(), Side.CLIENT);
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

