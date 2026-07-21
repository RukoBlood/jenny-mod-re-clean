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
    static public SimpleNetworkWrapper networkWrapper;
    static private int discriminant;

    private static int discriminator() {
        return discriminant++;
    }

    public static void RegisterMessages() {
        networkWrapper = net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.newSimpleChannel("sexmodchannel");
        networkWrapper.registerMessage(SendChatMessage.Handler.class, SendChatMessage.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(SendChatMessage.Handler.class, SendChatMessage.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SetPlayerMovement.Handler.class, SetPlayerMovement.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(TeleportPlayer.Handler.class, TeleportPlayer.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SendGirlToSex.a_inner24.class, SendGirlToSex.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SetPlayerForGirl.a_inner59.class, SetPlayerForGirl.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SyncActionPacket.Handler.class, SyncActionPacket.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(ResetController.Handler.class, ResetController.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(ResetController.Handler.class, ResetController.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(ResetGirl.a_inner422.class, ResetGirl.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(ChangeDataParameter.Handler.class, ChangeDataParameter.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(bo_class90.a_inner91.class, bo_class90.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SendCompanionHome.Handler.class, SendCompanionHome.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SetNewHome.a_inner14.class, SetNewHome.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(UploadInventoryToServer.a_inner61.class, UploadInventoryToServer.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(RemoveItems.a_inner424.class, RemoveItems.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SummonAllie.a_inner81.class, SummonAllie.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(UploadInventoryToServerAlt.a_inner155.class, UploadInventoryToServerAlt.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(MakeRichWish.a_inner104.class, MakeRichWish.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(UpdatePlayerModel.a_inner72.class, UpdatePlayerModel.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SexPrompt.a_inner348.class, SexPrompt.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SexPrompt.a_inner348.class, SexPrompt.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(StartStandingSexAnimation.a_inner274.class, StartStandingSexAnimation.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(BeeOpenChest.a_inner288.class, BeeOpenChest.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(CatActivateFishing.a_inner254.class, CatActivateFishing.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(CatEatingDone.a_inner374.class, CatEatingDone.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(CatThrowAwayItem.a_inner198.class, CatThrowAwayItem.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(ClaimTribe.a_inner355.class, ClaimTribe.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(GetTribeUIValues.Handler.class, GetTribeUIValues.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(GetTribeUIValues.Handler.class, GetTribeUIValues.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(SetTribeFollowMode.a_inner316.class, SetTribeFollowMode.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(FallTree.a_inner303.class, FallTree.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SendBlocks.Handler.class, SendBlocks.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(SendBlocks.Handler.class, SendBlocks.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(CancelTask.a_inner45.class, CancelTask.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SpawnParticle.a_inner261.class, SpawnParticle.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(SendEgg.a_inner434.class, SendEgg.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(Mine.a_inner227.class, Mine.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(bd_class76.Handler.class, bd_class76.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(ForcePlayerGirlUpdate.a_inner362.class, ForcePlayerGirlUpdate.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(UploadModelString.a_inner333.class, UploadModelString.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(GalathRapePounce.a_inner357.class, GalathRapePounce.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(UpdateVelocity.a_inner145.class, UpdateVelocity.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(RequestServerModelAvailability.a_inner351.class, RequestServerModelAvailability.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(RequestServerModelAvailability.a_inner351.class, RequestServerModelAvailability.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(DownloadServerModel.a_inner147.class, DownloadServerModel.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(DownloadServerModel.a_inner147.class, DownloadServerModel.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(SpawnEnergyBallParticlesAlt.a_inner102.class, SpawnEnergyBallParticlesAlt.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(GalathBackOffRape.a_inner126.class, GalathBackOffRape.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(InformOfOwnership.Handler.class, InformOfOwnership.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(RequestRiding.Handler.class, RequestRiding.class, PackageHandler.discriminator(), Side.SERVER);
        networkWrapper.registerMessage(SpawnEnergyBallParticles.Handler.class, SpawnEnergyBallParticles.class, PackageHandler.discriminator(), Side.CLIENT);
        networkWrapper.registerMessage(SetPlayerCam.Handler.class, SetPlayerCam.class, PackageHandler.discriminator(), Side.CLIENT);
    }

    static {
        discriminant = 0;
    }
}

