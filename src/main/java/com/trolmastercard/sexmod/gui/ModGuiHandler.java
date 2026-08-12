/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.IGuiHandler
 */
package com.trolmastercard.sexmod.gui;

import java.util.UUID;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.gui.Menu.*;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModGuiHandler implements IGuiHandler {
    //File b;
    //File c;
    //boolean isDebugMode = false;

    public ModGuiHandler() {
    }

    public ModGuiHandler(boolean bl) {
        this.initDebugContainers();
    }

    @SideOnly(value=Side.CLIENT)
    void initDebugContainers() {
        int debugState = 2;
        if (debugState == 0) {
            for (GirlEntity girl : GirlEntity.GirlEntityList()) {
                if (girl.world.isRemote || girl.getPosition().getX() != 5 || girl.getPosition().getY() != 7 || girl.getPosition().getZ() != 5)
                    continue;

                if (girl instanceof LunaEntity) {
                    new LunaContainer((LunaEntity) girl, Minecraft.getMinecraft().player.inventory, UUID.randomUUID());
                }

                new GirlContainer(girl, Minecraft.getMinecraft().player.inventory, UUID.randomUUID());
            }
        }
        if (debugState == 1) {
            for (GirlEntity girl : GirlEntity.GirlEntityList()) {
                if (girl.world.isRemote || !(girl instanceof IInventory) || girl.getPosition().getX() != 3 || girl.getPosition().getY() != 1 || girl.getPosition().getZ() != 7)
                    continue;
                IInventory inventory = (IInventory) ((Object) girl);
                new GirlInventory(Minecraft.getMinecraft().player.inventory, inventory, Minecraft.getMinecraft().player, UUID.randomUUID());
            }
        }
    }

    //how ts was not overriden before
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0) {
            for (GirlEntity girl : GirlEntity.GirlEntityList()) {
                if (girl.world.isRemote || girl.getPosition().getX() != x || girl.getPosition().getY() != y || girl.getPosition().getZ() != z)
                    continue;
                if (girl instanceof LunaEntity) {
                    return new LunaContainer((LunaEntity) girl, player.inventory, UUID.randomUUID());
                }
                return new GirlContainer(girl, player.inventory, UUID.randomUUID());
            }
        }
        if (ID == 1) {
            for (GirlEntity girl : GirlEntity.GirlEntityList()) {
                if (girl.world.isRemote || !(girl instanceof IInventory) || girl.getPosition().getX() != x || girl.getPosition().getY() != y || girl.getPosition().getZ() != z)
                    continue;
                IInventory iInventory = (IInventory) ((Object) girl);
                return new GirlInventory(player.inventory, iInventory, player, UUID.randomUUID());
            }
        }
        return null;
    }

    //this too
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0) {
            for (GirlEntity girl : GirlEntity.GirlEntityList()) {
                if (!girl.world.isRemote || girl.getPosition().getX() != x || girl.getPosition().getY() != y || girl.getPosition().getZ() != z)
                    continue;
                if (girl instanceof LunaEntity) {
                    return new LunaInventoryUI((LunaEntity) girl, player.inventory, UUID.randomUUID());
                }
                return new GirlInventoryUI(girl, player.inventory, UUID.randomUUID());
            }
        }
        if (ID == 1) {
            for (GirlEntity girl : GirlEntity.GirlEntityList()) {
                if (!girl.world.isRemote || !(girl instanceof IInventory) || girl.getPosition().getX() != x || girl.getPosition().getY() != y || girl.getPosition().getZ() != z)
                    continue;
                return new GirlGUIContainer(player, girl, UUID.randomUUID());
            }
        }
        return null;
    }
}

