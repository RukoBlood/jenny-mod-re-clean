/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ModelRegistryEvent
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.Kobold.KoboldManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TribeEgg
extends Item {
    final static public TribeEgg TRIBE_EGG = new TribeEgg();

    public TribeEgg() {
        this.setCreativeTab(CreativeTabs.MISC);
        this.maxStackSize = 1;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand enumHand) {
        ItemStack heldItem = entityPlayer.getHeldItem(enumHand);
        Vec3d eyesPos = entityPlayer.getPositionEyes(0.0f);
        Vec3d look = entityPlayer.getLook(0.0f);
        Vec3d vec3d3 = eyesPos.add(look.x * 5.0, look.y * 5.0, look.z * 5.0);
        RayTraceResult rayTraceResult = world.rayTraceBlocks(eyesPos, vec3d3, false, false, true);
        if (rayTraceResult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, entityPlayer.getHeldItem(enumHand));
        }
        if (rayTraceResult.typeOfHit == RayTraceResult.Type.MISS) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, entityPlayer.getHeldItem(enumHand));
        }
        if (!entityPlayer.capabilities.isCreativeMode) {
            heldItem.shrink(1);
        }
        if (!world.isRemote) {
            KoboldManager.a(world, rayTraceResult.hitVec);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, entityPlayer.getHeldItem(enumHand));
    }

    public static void RegisterTribeEgg() {
        TRIBE_EGG.setRegistryName("sexmod", "tribe_egg");
        TRIBE_EGG.setTranslationKey("tribe_egg");
        MinecraftForge.EVENT_BUS.register(TribeEgg.class);
    }

    @SubscribeEvent
    public static void a(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(TRIBE_EGG);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void a(ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation((Item) TRIBE_EGG, 0, (ModelResourceLocation)new ModelResourceLocation("sexmod:tribe_egg"));
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

