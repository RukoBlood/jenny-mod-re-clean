/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ModelRegistryEvent
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$LeftClickEmpty
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod;

import com.trolmastercard.sexmod.girls.Custom.CustomModel;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirlEntity;
import com.trolmastercard.sexmod.gui.CustomModel.ClothingGui;
import com.trolmastercard.sexmod.util.ThreadNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EditorWand extends Item {
    final static public EditorWand EDITOR_WAND = new EditorWand();

    public EditorWand() {
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.maxStackSize = 1;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int n, boolean bl) {
        if (world.isRemote) {
            this.a(entity, itemStack);
        }
        super.onUpdate(itemStack, world, entity, n, bl);
    }

    @SideOnly(value=Side.CLIENT)
    void a(Entity entity, ItemStack itemStack) {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)entity;
        if (!itemStack.equals(entityPlayer.getHeldItemMainhand()) && !itemStack.equals(entityPlayer.getHeldItemOffhand())) {
            itemStack.setItemDamage(0);
            return;
        }
        RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;
        itemStack.setItemDamage(rayTraceResult != null && GirlEntity.isValidGirl(rayTraceResult.entityHit) ? 1 : 0);
    }

    @SubscribeEvent
    public void a(PlayerInteractEvent.EntityInteract entityInteract) {
        Entity entity = entityInteract.getTarget();
        if (!(entity instanceof GirlEntity)) {
            return;
        }
        if (!GirlEntity.isValidGirl(entity)) {
            return;
        }
        EntityPlayer entityPlayer = entityInteract.getEntityPlayer();
        if (entityPlayer == null) {
            return;
        }
        ItemStack itemStack = entityPlayer.getHeldItemMainhand();
        if (itemStack.getItem() != EDITOR_WAND) {
            itemStack = entityPlayer.getHeldItemOffhand();
        }
        if (itemStack.getItem() != EDITOR_WAND) {
            return;
        }
        entityInteract.setCanceled(true);
        if (!entityInteract.getWorld().isRemote) {
            return;
        }
        if (CustomModel.isGlobalRenderingDisabled) {
            boolean bl = CustomModel.isGlobalRenderingDisabled = 0 != CustomModel.b(true);
            if (CustomModel.isGlobalRenderingDisabled) {
                return;
            }
        }
        ClothingGui.openGuiForGirl(((GirlEntity)entity).asGirl());
    }

    @SubscribeEvent
    public void a(AttackEntityEvent attackEntityEvent) {
        Entity entity = attackEntityEvent.getTarget();
        if (entity == null) {
            return;
        }
        if (!(entity instanceof GirlEntity)) {
            return;
        }
        EntityPlayer entityPlayer = attackEntityEvent.getEntityPlayer();
        if (entityPlayer == null) {
            return;
        }
        ItemStack itemStack = entityPlayer.getHeldItemMainhand();
        if (itemStack.getItem() != EDITOR_WAND) {
            itemStack = entityPlayer.getHeldItemOffhand();
        }
        if (itemStack.getItem() != EDITOR_WAND) {
            return;
        }
        attackEntityEvent.setCanceled(true);
        if (!entityPlayer.world.isRemote) {
            return;
        }
        GirlEntity em_class2582 = (GirlEntity)entity;
        String string = em_class2582.getCustomModelCode();
        String string2 = GirlEntity.c(GirlEntity.h(em_class2582.girlID()));
        entityPlayer.sendMessage(new TextComponentString(String.format("%s's model-code: %s%s$%s", new Object[]{em_class2582.getGirlName(), TextFormatting.YELLOW, string, string2})));
        entityPlayer.sendMessage(new TextComponentString((Object)((Object)TextFormatting.ITALIC) + "copied to clipboard"));
        ThreadNames.copyToClipboard(String.format("%s$%s", string, string2));
    }

    @SubscribeEvent
    public void a(PlayerInteractEvent.LeftClickBlock leftClickBlock) {
        if (this.a(leftClickBlock.getEntityPlayer(), leftClickBlock.getWorld())) {
            leftClickBlock.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void a(PlayerInteractEvent.LeftClickEmpty leftClickEmpty) {
        this.a(leftClickEmpty.getEntityPlayer(), leftClickEmpty.getWorld());
    }

    boolean a(EntityPlayer entityPlayer, World world) {
        if (entityPlayer == null) {
            return false;
        }
        ItemStack itemStack = entityPlayer.getHeldItemMainhand();
        if (itemStack.getItem() != EDITOR_WAND) {
            itemStack = entityPlayer.getHeldItemOffhand();
        }
        if (itemStack.getItem() != EDITOR_WAND) {
            return false;
        }
        if (!world.isRemote) {
            return true;
        }
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(entityPlayer.getPersistentID());
        if (ei_class2512 == null) {
            entityPlayer.sendStatusMessage(new TextComponentString("you gotta turn into the girl, you want to copy the model-code off"), true);
            return true;
        }
        String string = ei_class2512.getCustomModelCode();
        String string2 = GirlEntity.c(GirlEntity.h(ei_class2512.girlID()));
        entityPlayer.sendMessage(new TextComponentString(String.format("%s's model-code: %s%s$%s", new Object[]{ThreadNames.CapitalizeString(PlayerGirlEntity.fromGirl(ei_class2512).toString()), TextFormatting.YELLOW, string, string2})));
        entityPlayer.sendMessage(new TextComponentString((Object)((Object)TextFormatting.ITALIC) + "copied to clipboard"));
        ThreadNames.copyToClipboard(String.format("%s$%s", string, string2));
        return true;
    }

    public static void RegisterWand() {
        EDITOR_WAND.setRegistryName("sexmod", "npc_editor_wand");
        EDITOR_WAND.setTranslationKey("npc_editor_wand");
        MinecraftForge.EVENT_BUS.register(EditorWand.class);
    }

    @SubscribeEvent
    public static void a(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(EDITOR_WAND);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void a(ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation((Item) EDITOR_WAND, 0, (ModelResourceLocation)new ModelResourceLocation("sexmod:npc_editor_wand"));
        ModelLoader.setCustomModelResourceLocation((Item) EDITOR_WAND, 1, (ModelResourceLocation)new ModelResourceLocation("sexmod:npc_editor_wand_active"));
    }
}

