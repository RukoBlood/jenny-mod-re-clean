/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ModelRegistryEvent
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.minecraftforge.fml.common.eventhandler.Event$Result
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod.girls.Kobold.DragonStaff;

import com.trolmastercard.sexmod.Packets.GetTribeUIValues;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEntity;
import com.trolmastercard.sexmod.gui.DragonStaffUI;
import com.trolmastercard.sexmod.util.Handlers.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DragonStaffItem extends Item implements IAnimatable {
    final static public DragonStaffItem DRAGON_STAFF = new DragonStaffItem();
    final private AnimationFactory animationFactory = new AnimationFactory(this);

    public DragonStaffItem() {
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.maxStackSize = 1;
    }

    public static void RegisterStaff() {
        DRAGON_STAFF.setRegistryName("sexmod", "dragon_staff");
        DRAGON_STAFF.setTranslationKey("dragon_staff");
        MinecraftForge.EVENT_BUS.register(DragonStaffItem.class);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand enumHand) {
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, entityPlayer.getHeldItem(enumHand));
    }

    @SubscribeEvent
    public static void a(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(DRAGON_STAFF);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void a(ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(DRAGON_STAFF, 0, new ModelResourceLocation("sexmod:dragon_staff"));
        DRAGON_STAFF.setTileEntityItemStackRenderer(new DragonStaffRenderer());
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    public static class a_inner408 {
        @SubscribeEvent
        public void a(PlayerInteractEvent.RightClickItem rightClickItem) {
            World world = rightClickItem.getWorld();
            if (!world.isRemote) {
                return;
            }
            EntityPlayer entityPlayer = rightClickItem.getEntityPlayer();
            if (entityPlayer.getHeldItem(EnumHand.MAIN_HAND).getItem() != DRAGON_STAFF && entityPlayer.getHeldItem(EnumHand.OFF_HAND).getItem() != DRAGON_STAFF) {
                return;
            }
            if (KoboldEntity.ACTIVE_TRIBE_SCREEN_POSITIONS.isEmpty()) {
                return;
            }
            this.a();
        }

        @SideOnly(value=Side.CLIENT)
        void a() {
            Minecraft.getMinecraft().displayGuiScreen(new DragonStaffUI());
            PacketHandler.INSTANCE.sendToServer(new GetTribeUIValues());
        }

        @SubscribeEvent
        public void a(PlayerInteractEvent.RightClickBlock rightClickBlock) {
            EntityPlayer entityPlayer = rightClickBlock.getEntityPlayer();
            if (entityPlayer.getHeldItem(EnumHand.MAIN_HAND).getItem() == DRAGON_STAFF || entityPlayer.getHeldItem(EnumHand.OFF_HAND).getItem() == DRAGON_STAFF) {
                Block block = rightClickBlock.getWorld().getBlockState(rightClickBlock.getPos()).getBlock();
                if (block instanceof BlockBed) {
                    rightClickBlock.setCancellationResult(EnumActionResult.FAIL);
                    rightClickBlock.setResult(Event.Result.DENY);
                    rightClickBlock.setCanceled(true);
                }
                if (block instanceof BlockChest) {
                    rightClickBlock.setCancellationResult(EnumActionResult.FAIL);
                    rightClickBlock.setResult(Event.Result.DENY);
                    rightClickBlock.setCanceled(true);
                }
            }
        }
    }
}

