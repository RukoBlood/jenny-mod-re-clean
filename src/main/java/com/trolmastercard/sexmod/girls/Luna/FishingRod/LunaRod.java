/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraftforge.client.event.ModelRegistryEvent
 *  net.minecraftforge.client.model.ModelLoader
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package com.trolmastercard.sexmod.girls.Luna.FishingRod;

import javax.annotation.Nullable;

import com.trolmastercard.sexmod.girls.Luna.LunaEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LunaRod
extends ItemFishingRod {
    final static public LunaRod LUNA_ROD = new LunaRod();

    public LunaRod() {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter(){
            @Override
            @SideOnly(value=Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0f;
                }
                if (!(entityIn instanceof LunaEntity)) {
                    return 0.0f;
                }
                return entityIn.getDataManager().get(LunaEntity.IS_FISHING) ? 1.0f : 0.0f;
            }
        });
    }

    public static void RegisterRod() {
        LUNA_ROD.setRegistryName("sexmod", "luna_rod");
        LUNA_ROD.setTranslationKey("luna_rod");
        MinecraftForge.EVENT_BUS.register(LunaRod.class);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> register) {
        register.getRegistry().register(LUNA_ROD);
    }

    @SideOnly(value=Side.CLIENT)
    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation((Item) LUNA_ROD, 0, (ModelResourceLocation)new ModelResourceLocation("fishing_rod"));
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, LunaEntity luna, EnumHand handIn) {
        ItemStack heldItem = luna.getHeldItem(handIn);
        if (luna.fishEntity != null) {
            int i = luna.fishEntity.handleHookRetraction();
            heldItem.damageItem(i, luna);
            luna.swingArm(handIn);
            worldIn.playSound(null, luna.posX, luna.posY, luna.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
        } else {
            worldIn.playSound(null, luna.posX, luna.posY, luna.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isRemote) {
                int luckBonus = EnchantmentHelper.getFishingLuckBonus(heldItem);
                LunaHookEntity.nextAngler = luna;
                double distance = luna.getPositionVector().distanceTo(new Vec3d(luna.chosenFishingSpot.getX(), luna.chosenFishingSpot.getY(), luna.chosenFishingSpot.getZ()));
                LunaHookEntity entityfishhook = new LunaHookEntity(worldIn, luna, distance * LunaEntity.ap);
                int speedBonus = EnchantmentHelper.getFishingSpeedBonus(heldItem);
                if (speedBonus > 0) {
                    entityfishhook.setLureSpeed(speedBonus);
                }
                if (luckBonus > 0) {
                    entityfishhook.setLuck(luckBonus);
                }
                worldIn.spawnEntity(entityfishhook);
            }
            luna.swingArm(handIn);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, heldItem);
    }
}

