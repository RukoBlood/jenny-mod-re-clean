/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.network.IGuiHandler
 */
package com.trolmastercard.sexmod;

import java.io.File;
import java.util.ConcurrentModificationException;
import java.util.UUID;

import com.trolmastercard.sexmod.girls.GirlEntity;
import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import com.trolmastercard.sexmod.gui.az_class56;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class et_class272 implements IGuiHandler {
    File b;
    File c;
    boolean a = false;

    public et_class272() {
    }

    public et_class272(boolean bl) {
        this.a();
    }

    @SideOnly(value=Side.CLIENT)
    void a() {
        int n = 2;
        if (n == 0) {
            try {
                for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                    if (em_class2582.world.isRemote || em_class2582.getPosition().getX() != 5 || em_class2582.getPosition().getY() != 7 || em_class2582.getPosition().getZ() != 5) continue;
                    if (em_class2582 instanceof LunaEntity) {
                        new ca_class121((LunaEntity)em_class2582, Minecraft.getMinecraft().player.inventory, UUID.randomUUID());
                    }
                    new d4_class162(em_class2582, Minecraft.getMinecraft().player.inventory, UUID.randomUUID());
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }
        if (n == 1) {
            try {
                for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                    if (em_class2582.world.isRemote || !(em_class2582 instanceof IInventory) || em_class2582.getPosition().getX() != 3 || em_class2582.getPosition().getY() != 1 || em_class2582.getPosition().getZ() != 7) continue;
                    IInventory iInventory = (IInventory)((Object)em_class2582);
                    new GirlInventory(Minecraft.getMinecraft().player.inventory, iInventory, Minecraft.getMinecraft().player, UUID.randomUUID());
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }
    }

    public Object getServerGuiElement(int n, EntityPlayer entityPlayer, World world, int n2, int n3, int n4) {
        if (n == 0) {
            try {
                for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                    if (em_class2582.world.isRemote || em_class2582.getPosition().getX() != n2 || em_class2582.getPosition().getY() != n3 || em_class2582.getPosition().getZ() != n4) continue;
                    if (em_class2582 instanceof LunaEntity) {
                        return new ca_class121((LunaEntity)em_class2582, entityPlayer.inventory, UUID.randomUUID());
                    }
                    return new d4_class162(em_class2582, entityPlayer.inventory, UUID.randomUUID());
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }
        if (n == 1) {
            try {
                for (GirlEntity girl : GirlEntity.GirlEntityList()) {
                    if (girl.world.isRemote || !(girl instanceof IInventory) || girl.getPosition().getX() != n2 || girl.getPosition().getY() != n3 || girl.getPosition().getZ() != n4) continue;
                    IInventory iInventory = (IInventory)((Object)girl);
                    return new GirlInventory(entityPlayer.inventory, iInventory, entityPlayer, UUID.randomUUID());
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }
        return null;
    }

    public Object getClientGuiElement(int n, EntityPlayer entityPlayer, World world, int n2, int n3, int n4) {
        if (n == 0) {
            try {
                for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                    if (!em_class2582.world.isRemote || em_class2582.getPosition().getX() != n2 || em_class2582.getPosition().getY() != n3 || em_class2582.getPosition().getZ() != n4) continue;
                    if (em_class2582 instanceof LunaEntity) {
                        return new az_class56((LunaEntity)em_class2582, entityPlayer.inventory, UUID.randomUUID());
                    }
                    return new fb_class301(em_class2582, entityPlayer.inventory, UUID.randomUUID());
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }
        if (n == 1) {
            try {
                for (GirlEntity em_class2582 : GirlEntity.GirlEntityList()) {
                    if (!em_class2582.world.isRemote || !(em_class2582 instanceof IInventory) || em_class2582.getPosition().getX() != n2 || em_class2582.getPosition().getY() != n3 || em_class2582.getPosition().getZ() != n4) continue;
                    return new ek_class255(entityPlayer, em_class2582, UUID.randomUUID());
                }
            } catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
        }
        return null;
    }

    private static ConcurrentModificationException a(ConcurrentModificationException concurrentModificationException) {
        return concurrentModificationException;
    }
}

