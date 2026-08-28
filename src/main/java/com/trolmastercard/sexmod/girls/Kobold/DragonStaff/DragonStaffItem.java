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
    final private AnimationFactory factory = new AnimationFactory(this);

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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(DRAGON_STAFF);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(DRAGON_STAFF, 0, new ModelResourceLocation("sexmod:dragon_staff"));
        DRAGON_STAFF.setTileEntityItemStackRenderer(new DragonStaffRenderer());
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static class EventHandler {
        @SubscribeEvent
        public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            World world = event.getWorld();
            if (world.isRemote) {
                EntityPlayer player = event.getEntityPlayer();
                if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == DRAGON_STAFF || player.getHeldItem(EnumHand.OFF_HAND).getItem() == DRAGON_STAFF) {
                    if (!KoboldEntity.ACTIVE_TRIBE_SCREEN_POSITIONS.isEmpty()) {
                        this.openStructureCommand();
                    }
                }
            }
        }

        @SideOnly(value=Side.CLIENT)
        void openStructureCommand() {
            Minecraft.getMinecraft().displayGuiScreen(new DragonStaffUI());
            PacketHandler.INSTANCE.sendToServer(new GetTribeUIValues());
        }

        @SubscribeEvent
        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            EntityPlayer player = event.getEntityPlayer();
            if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == DRAGON_STAFF || player.getHeldItem(EnumHand.OFF_HAND).getItem() == DRAGON_STAFF) {
                Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
                if (block instanceof BlockBed) {
                    event.setCancellationResult(EnumActionResult.FAIL);
                    event.setResult(Event.Result.DENY);
                    event.setCanceled(true);
                }
                if (block instanceof BlockChest) {
                    event.setCancellationResult(EnumActionResult.FAIL);
                    event.setResult(Event.Result.DENY);
                    event.setCanceled(true);
                }
            }
        }
    }
}

