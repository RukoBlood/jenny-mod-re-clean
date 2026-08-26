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
package com.trolmastercard.sexmod.girls.base.EditorWand;

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
            this.applyEditor(entity, itemStack);
        }
        super.onUpdate(itemStack, world, entity, n, bl);
    }

    @SideOnly(value=Side.CLIENT)
    void applyEditor(Entity entity, ItemStack itemStack) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entity;
            if (!itemStack.equals(entityPlayer.getHeldItemMainhand()) && !itemStack.equals(entityPlayer.getHeldItemOffhand())) {
                itemStack.setItemDamage(0);
                return;
            }
            RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;
            itemStack.setItemDamage(rayTraceResult != null && GirlEntity.isValidGirl(rayTraceResult.entityHit) ? 1 : 0);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();
        if (entity instanceof GirlEntity) {
            if (GirlEntity.isValidGirl(entity)) {
                EntityPlayer player = event.getEntityPlayer();
                if (player != null) {
                    ItemStack itemStack = player.getHeldItemMainhand();
                    if (itemStack.getItem() != EDITOR_WAND) {
                        itemStack = player.getHeldItemOffhand();
                    }
                    if (itemStack.getItem() == EDITOR_WAND) {
                        event.setCanceled(true);
                        if (event.getWorld().isRemote) {
                            if (CustomModel.isGlobalRenderingDisabled) {
                                CustomModel.isGlobalRenderingDisabled = 0 != CustomModel.getModelCount(true);
                                if (CustomModel.isGlobalRenderingDisabled) {
                                    return;
                                }
                            }
                            ClothingGui.openGuiForGirl(((GirlEntity) entity).asGirl());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        Entity entity = event.getTarget();
        if (entity != null) {
            if (entity instanceof GirlEntity) {
                EntityPlayer player = event.getEntityPlayer();
                if (player != null) {
                    ItemStack itemStack = player.getHeldItemMainhand();
                    if (itemStack.getItem() != EDITOR_WAND) {
                        itemStack = player.getHeldItemOffhand();
                    }
                    if (itemStack.getItem() == EDITOR_WAND) {
                        event.setCanceled(true);
                        if (player.world.isRemote) {
                            GirlEntity girl = (GirlEntity) entity;
                            String modelCode = girl.getCustomModelCode();
                            String idList = GirlEntity.encodePartIdList(GirlEntity.getAllPartIdsForGirl(girl.girlID()));
                            player.sendMessage(new TextComponentString(String.format("%s's model-code: %s%s$%s", girl.getGirlName(), TextFormatting.YELLOW, modelCode, idList)));
                            player.sendMessage(new TextComponentString(TextFormatting.ITALIC + "copied to clipboard"));
                            ThreadNames.copyToClipboard(String.format("%s$%s", modelCode, idList));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (this.canEdit(event.getEntityPlayer(), event.getWorld())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        this.canEdit(event.getEntityPlayer(), event.getWorld());
    }

    boolean canEdit(EntityPlayer entityPlayer, World world) {
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
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(entityPlayer.getPersistentID());
        if (playerGirl == null) {
            entityPlayer.sendStatusMessage(new TextComponentString("you gotta turn into the girl, you want to copy the model-code off"), true);
            return true;
        }
        String string = playerGirl.getCustomModelCode();
        String string2 = GirlEntity.encodePartIdList(GirlEntity.getAllPartIdsForGirl(playerGirl.girlID()));
        entityPlayer.sendMessage(new TextComponentString(String.format("%s's model-code: %s%s$%s", ThreadNames.CapitalizeString(PlayerGirlEntity.getGirlType(playerGirl).toString()), TextFormatting.YELLOW, string, string2)));
        entityPlayer.sendMessage(new TextComponentString(TextFormatting.ITALIC + "copied to clipboard"));
        ThreadNames.copyToClipboard(String.format("%s$%s", string, string2));
        return true;
    }

    public static void RegisterWand() {
        EDITOR_WAND.setRegistryName("sexmod", "npc_editor_wand");
        EDITOR_WAND.setTranslationKey("npc_editor_wand");
        MinecraftForge.EVENT_BUS.register(EditorWand.class);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(EDITOR_WAND);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(EDITOR_WAND, 0, new ModelResourceLocation("sexmod:npc_editor_wand"));
        ModelLoader.setCustomModelResourceLocation(EDITOR_WAND, 1, new ModelResourceLocation("sexmod:npc_editor_wand_active"));
    }
}

