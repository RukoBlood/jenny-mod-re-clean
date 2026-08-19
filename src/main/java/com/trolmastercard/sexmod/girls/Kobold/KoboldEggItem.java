/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ModelRegistryEvent
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod.girls.Kobold;

import java.util.UUID;

import com.trolmastercard.sexmod.KoboldEggColor;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class KoboldEggItem
extends Item
implements IAnimatable {
    final private AnimationFactory b = new AnimationFactory(this);
    static public KoboldEggItem KOBOLD_EGG = new KoboldEggItem();

    public KoboldEggItem() {
        this.setMaxStackSize(1);
    }

    public static void RegisterEggItem() {
        KOBOLD_EGG.setRegistryName("sexmod", "kobold_egg_item");
        KOBOLD_EGG.setTranslationKey("kobold_egg_item");
        MinecraftForge.EVENT_BUS.register(KoboldEggItem.class);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.b;
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void a(ModelRegistryEvent modelRegistryEvent) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation("sexmod:kobold_egg_item");
        ModelLoader.setCustomMeshDefinition((Item) KOBOLD_EGG, itemStack -> modelResourceLocation);
        ModelBakery.registerItemVariants((Item) KOBOLD_EGG, (ResourceLocation[])new ResourceLocation[]{modelResourceLocation});
        KOBOLD_EGG.setTileEntityItemStackRenderer(new KoboldEggColor());
    }

    @SubscribeEvent
    public static void a(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(KOBOLD_EGG);
    }

    @SubscribeEvent
    public static void a(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        World world = rightClickBlock.getWorld();
        ItemStack itemStack = rightClickBlock.getItemStack();
        Vec3d vec3d = rightClickBlock.getHitVec();
        if (world.isRemote) {
            return;
        }
        if (itemStack.getItem() != KOBOLD_EGG) {
            return;
        }
        KoboldEggEntity i_class4102 = new KoboldEggEntity(world);
        i_class4102.setPosition(vec3d.x, vec3d.y, vec3d.z);
        i_class4102.getDataManager().set(KoboldEggEntity.b, EyeAndKoboldColor.getColorByWoolId(itemStack.getMetadata()).toString());
        NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
        if (nBTTagCompound != null) {
            i_class4102.f = UUID.fromString(nBTTagCompound.getString("tribeID"));
        }
        world.spawnEntity(i_class4102);
        itemStack.shrink(1);
    }
}

