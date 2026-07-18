/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector2f
 *  net.minecraftforge.fml.common.network.NetworkRegistry$TargetPoint
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 */
package com.trolmastercard.sexmod.girls;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector2f;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packages.ForcePlayerGirlUpdate;
import com.trolmastercard.sexmod.Packages.ResetGirl;
import com.trolmastercard.sexmod.Packages.SetPlayerMovement;
import com.trolmastercard.sexmod.Packages.SexPrompt;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.interfaces.IRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//ei
public abstract class PlayerGirl extends Fighter {
    final static public String aa = "sexmod:CustomModel";
    final static public String ae = "sexmod:GirlSpecific";
    final static public float ac = 0.0f;
    final static public int am = 100;
    final static public int Y = 65;
    static public boolean ag = true;
    public Vector2f ao = new Vector2f(0.0f, 0.0f);
    public boolean isPlayerSneaking = false;
    public boolean isPlayerSprinting = false;
    public boolean isPlayerRiding = false;
    public boolean isPlayerOnGround = true;
    public boolean ah = false;
    final static protected DataParameter<Optional<UUID>> ai = EntityDataManager.createKey(GirlEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID).getSerializer().createKey(118);

    static public Hashtable<UUID, PlayerGirl> playerGirlUUIDHashtable = new Hashtable();
    static public List<PlayerGirl> Z = new ArrayList<PlayerGirl>();
    int an = -1;
    public boolean ab = true;

    protected PlayerGirl(World world) {
        super(world);
        this.setSize(0.01f, 0.01f);
        Z.add(this);
    }

    protected PlayerGirl(World world, UUID uUID) {
        this(world);
        this.entityDataManager.set(ai, Optional.of(uUID));
    }

    // TODO clash
    @Nullable
    public static PlayerGirl getUUIDHashtable(UUID uUID) {
        return playerGirlUUIDHashtable.get(uUID);
    }

    @Nullable
    public static PlayerGirl GetPlayer(@Nonnull EntityPlayer entityPlayer) {
        return playerGirlUUIDHashtable.get(entityPlayer.getPersistentID());
    }

    @Nullable
    public static PlayerGirl com_trolmastercard_sexmod_ei_class251_a(UUID uUID) {
        for (GirlEntity girl : PlayerGirl.GirlEntityList()) {
            PlayerGirl playerGirl;
            if (girl.world.isRemote || !(girl instanceof PlayerGirl) || !uUID.equals((playerGirl = (PlayerGirl)girl).java_util_UUID_m())) continue;
            return playerGirl;
        }
        return null;
    }

    @Override
    public net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint net_minecraftforge_fml_common_network_NetworkRegistry$TargetPoint_P() {
        return new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY - 0.0, this.posZ, 50.0);
    }

    public void a(int n, Action action) {
        PackageHandler.networkWrapper.sendToAllTracking((IMessage)new ForcePlayerGirlUpdate(this.java_util_UUID_m(), n, action), this.net_minecraftforge_fml_common_network_NetworkRegistry$TargetPoint_P());
    }

    public EntityPlayer getPlayerEntity(EntityPlayer player) {
        return player;
    }

    public boolean boolean_z() {
        return true;
    }

    public Vec3d c(Vec3d vec3d, float f) {
        return vec3d;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean boolean_v() {
        return true;
    }

    public boolean boolean_q() {
        return false;
    }

    @SideOnly(value=Side.CLIENT)
    public void void_H() {
    }

    public boolean boolean_p() {
        return true;
    }

    public boolean boolean_a(String string) {
        return false;
    }

    public boolean boolean_A() {
        return true;
    }

    @Override
    public String getGirlName() {
        EntityPlayer entityPlayer;
        if (this.entityDataManager.get(ai).isPresent() && (entityPlayer = this.world.getPlayerEntityByUUID((UUID)this.entityDataManager.get(ai).get())) != null) {
            return entityPlayer.getName();
        }
        return "anonymous horny girl";
    }

    // Base
    public void u_() {
    }

    public abstract void b(String var1, UUID var2);

    public abstract IRenderer getLimbRenderer(int var1);

    public abstract String HandTexture(int var1);

    public Vec3i net_minecraft_util_math_Vec3i_b(int n) {
        return new Vec3i(255, 255, 255);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isNotColliding() {
        return true;
    }

    public boolean boolean_F() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.entityDataManager.register(ai, Optional.absent());
    }

    @SideOnly(value=Side.CLIENT)
    public static void void_i() {
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(Minecraft.getMinecraft().player.getPersistentID());
        if (ei_class2512 == null) {
            return;
        }
        ei_class2512.void_r();
    }

    @Override
    public void void_r() {
        this.playerCamPos = null;
        this.setNoGravity(false);
        if (this.world.isRemote) {
            this.V();
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    protected void V() {
        if (this.boolean_n() || this.boolean_f()) {
            HandlePlayerMovement.a(true);
            EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
            entityPlayerSP.setInvisible(false);
            entityPlayerSP.setNoGravity(false);
            entityPlayerSP.noClip = false;
            this.entityDataManager.set(G, false);
            PackageHandler.networkWrapper.sendToServer((IMessage)new ResetGirl(this.girlID()));
        }
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean boolean_H() {
        Minecraft minecraft = Minecraft.getMinecraft();
        return !this.boolean_f() || minecraft.gameSettings.thirdPersonView != 0;
    }

    protected void c(boolean bl) {
        if (!ag) {
            return;
        }
        if (this.java_util_UUID_m() == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.java_util_UUID_m());
        if (entityPlayer == null) {
            return;
        }
        entityPlayer.capabilities.allowFlying = bl;
        if (!bl) {
            entityPlayer.capabilities.isFlying = false;
        }
        entityPlayer.sendPlayerAbilities();
    }

    public static boolean boolean_e(UUID uUID) {
        PlayerGirl.void_C();
        for (Map.Entry<UUID, PlayerGirl> entry : playerGirlUUIDHashtable.entrySet()) {
            UUID uUID2 = entry.getKey();
            if (!uUID.equals(uUID2)) continue;
            return true;
        }
        return false;
    }

    public static boolean e(EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return false;
        }
        return PlayerGirl.boolean_e(entityPlayer.getPersistentID());
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return super.getEntityBoundingBox().offset(0.0, 0.5, 0.0);
    }

    protected EntityPlayer net_minecraft_entity_player_EntityPlayer_j() {
        List<EntityPlayer> list = this.world.playerEntities;
        EntityPlayer entityPlayer = null;
        for (EntityPlayer entityPlayer2 : list) {
            if (entityPlayer2.getPersistentID().equals(this.entityDataManager.get(ai).get())) continue;
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                continue;
            }
            double d = entityPlayer.getDistanceSq(this.net_minecraft_util_math_Vec3d_w().x, this.net_minecraft_util_math_Vec3d_w().y, this.net_minecraft_util_math_Vec3d_w().z);
            double d2 = entityPlayer2.getDistanceSq(this.net_minecraft_util_math_Vec3d_w().x, this.net_minecraft_util_math_Vec3d_w().y, this.net_minecraft_util_math_Vec3d_w().z);
            if (!(d2 < d)) continue;
            entityPlayer = entityPlayer2;
        }
        return entityPlayer;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean boolean_e() {
        EntityPlayer entityPlayer = this.net_minecraft_entity_player_EntityPlayer_j();
        if (entityPlayer == null) {
            return false;
        }
        return entityPlayer.getPersistentID().equals(Minecraft.getMinecraft().player.getPersistentID());
    }

    public Vec3d net_minecraft_util_math_Vec3d_w() {
        return new Vec3d(this.posX, this.posY - 0.0, this.posZ);
    }

    protected void void_b(UUID uUID) {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP)this.world.getPlayerEntityByUUID(uUID);
        EntityPlayerMP entityPlayerMP2 = (EntityPlayerMP)this.world.getPlayerEntityByUUID((UUID)this.entityDataManager.get(ai).get());
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), entityPlayerMP);
        PackageHandler.networkWrapper.sendTo((IMessage)new SetPlayerMovement(false), entityPlayerMP2);
        this.void_e(uUID);
        this.rotationYaw = 0.0f;
        this.rotationYawHead = 0.0f;
        entityPlayerMP.rotationYaw = 180.0f;
        entityPlayerMP.rotationYawHead = 180.0f;
        entityPlayerMP.setNoGravity(true);
        entityPlayerMP.noClip = true;
        Vec3d vec3d = this.getPositionVector();
        entityPlayerMP.setPositionAndUpdate(vec3d.x, vec3d.y, vec3d.z + 1.0);
        entityPlayerMP.capabilities.isFlying = true;
        entityPlayerMP2.capabilities.isFlying = true;
        this.j(uUID);
        this.entityDataManager.set(G, true);
        this.c(vec3d);
        this.void_b(0.0f);
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        super.playStepSound(blockPos, block);
    }

    public AxisAlignedBB getPlayerBB(EntityPlayer player) {
        return player.getEntityBoundingBox();
    }

    @Override
    public void onUpdate() {
        this.noClip = true;
        this.setNoGravity(true);
        super.onUpdate();
        this.D_();
        if (!this.world.isRemote) {
            return;
        }
        if (this.boolean_f()) {
            w_class427.a.a();
        }
    }

    @SideOnly(value=Side.CLIENT)
    void void_h() {
        Minecraft.getMinecraft().player.eyeHeight = this.getEyeHeight();
    }

    @SideOnly(value=Side.CLIENT)
    public boolean boolean_f() {
        if (!this.entityDataManager.get(ai).isPresent()) {
            return false;
        }
        return ((UUID)this.entityDataManager.get(ai).get()).equals(Minecraft.getMinecraft().player.getPersistentID());
    }

    public boolean boolean_E() {
        return false;
    }

    void void_d(EntityPlayer entityPlayer) {
        NBTTagCompound nBTTagCompound = entityPlayer.getEntityData();
        String string = nBTTagCompound.getString(aa + (Object)((Object) PlayerGirlEntity.a(this)));
        this.f(string);
    }

    @Override
    public void updateAITasks() {
        //Object object;
        PlayerGirl.void_C();
        this.void_l();
        this.G();
        UUID uUID = this.java_util_UUID_m();
        if (uUID == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            this.setPositionAndUpdate(this.posX, 0.0, this.posZ);
            return;
        }
        this.void_d(entityPlayer);
        if (this.boolean_Q()) {
            Vec3d object = this.net_minecraft_util_math_Vec3d_o();
            this.setPositionAndUpdate(object.x, object.y, object.z);
        } else {
            this.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY + 0.0, entityPlayer.posZ);
        }
        Action object = this.currentAction();
        if (object == Action.NULL && entityPlayer.isSwingInProgress) {
            this.setCurrentAction(Action.ATTACK);
        }
        if (object == Action.ATTACK && !entityPlayer.isSwingInProgress) {
            this.setCurrentAction(Action.NULL);
        }
    }

    // TODO clashes
    void D_() {
        if (this.an == -1) {
            return;
        }
        ++this.an;
        if (!this.world.isRemote && this.an == 65) {
            this.f(this.int_ah() == 0 ? 1 : 0);
        }
        if (this.an < 100) {
            return;
        }
        if (this.currentAction() != Action.STRIP) {
            return;
        }
        if (this.world.isRemote) {
            this.void_n();
            return;
        }
        this.setCurrentAction(Action.NULL);
    }

    @SideOnly(value=Side.CLIENT)
    void void_n() {
        if (this.boolean_f()) {
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.gameSettings.thirdPersonView = 0;
            minecraft.entityRenderer.loadEntityShader(minecraft.getRenderViewEntity());
            HandlePlayerMovement.a(true);
        }
    }

    public boolean boolean_o() {
        return this.boolean_Q();
    }

    public Vec3d b(Vec3d vec3d, float f) {
        return vec3d;
    }

    public boolean a(Action fp_class3242, EntityPlayer entityPlayer) {
        return false;
    }

    public boolean boolean_l() {
        return true;
    }

    public void void_b(EntityPlayer entityPlayer) {
    }

    @Override
    public void setCurrentAction(Action action) {
        if (!this.world.isRemote && action == Action.NULL && this.boolean_Q()) {
            System.out.println("prevented a potential animation break");
            return;
        }
        if (action == Action.STRIP) {
            this.an = this.world.isRemote ? 5 : 0;
        }
        super.setCurrentAction(action);
    }

    void f(EntityPlayer entityPlayer) {
        this.entityDataManager.set(ITEM_SLOT_3, ItemStack.EMPTY);
        this.entityDataManager.set(ITEM_SLOT_4, ItemStack.EMPTY);
        this.entityDataManager.set(ITEM_SLOT_5, ItemStack.EMPTY);
        this.entityDataManager.set(ITEM_SLOT_6, ItemStack.EMPTY);
        for (ItemStack itemStack : entityPlayer.getArmorInventoryList()) {
            if (itemStack.getItem() instanceof ItemElytra) {
                this.entityDataManager.set(ITEM_SLOT_4, itemStack);
                continue;
            }
            if (!(itemStack.getItem() instanceof ItemArmor)) continue;
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            switch (itemArmor.getEquipmentSlot()) {
                case HEAD: {
                    this.entityDataManager.set(ITEM_SLOT_3, itemStack);
                    break;
                }
                case CHEST: {
                    this.entityDataManager.set(ITEM_SLOT_4, itemStack);
                    break;
                }
                case LEGS: {
                    this.entityDataManager.set(ITEM_SLOT_5, itemStack);
                    break;
                }
                case FEET: {
                    this.entityDataManager.set(ITEM_SLOT_6, itemStack);
                }
            }
        }
    }

    public UUID java_util_UUID_m() {
        if (this.entityDataManager.get(ai).isPresent()) {
            return (UUID)this.entityDataManager.get(ai).get();
        }
        return null;
    }

    @Nullable
    public EntityPlayer net_minecraft_entity_player_EntityPlayer_k() {
        UUID uUID = this.java_util_UUID_m();
        if (uUID == null) {
            return null;
        }
        return this.world.getPlayerEntityByUUID(uUID);
    }

    public void a(Optional<UUID> optional) {
        this.entityDataManager.set(ai, optional);
    }

    public void void_y() {
    }

    public void void_B() {
    }

    public static void void_C() {
        ArrayList<PlayerGirl> arrayList = new ArrayList<PlayerGirl>();
        for (PlayerGirl ei_class2512 : Z) {
            if (ei_class2512.java_util_UUID_m() == null) continue;
            playerGirlUUIDHashtable.put(ei_class2512.java_util_UUID_m(), ei_class2512);
            arrayList.add(ei_class2512);
        }
        for (PlayerGirl ei_class2512 : arrayList) {
            Z.remove(ei_class2512);
        }
        PlayerGirl.void_t();
    }

    static void void_t() {
        ArrayList<UUID> arrayList = new ArrayList<UUID>();
        for (Map.Entry<UUID, PlayerGirl> object : playerGirlUUIDHashtable.entrySet()) {
            if (!object.getValue().isDead) continue;
            arrayList.add(object.getKey());
        }
        for (UUID uUID : arrayList) {
            playerGirlUUIDHashtable.remove(uUID);
        }
    }

    protected boolean boolean_c(UUID uUID) {
        if (uUID == null) {
            return false;
        }
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(uUID);
        return ei_class2512 != null;
    }

    @Override
    public void a(String string, UUID uUID) {
        if (this.boolean_a(string)) {
            return;
        }
        if (!this.entityDataManager.get(ai).isPresent()) {
            return;
        }
        PackageHandler.networkWrapper.sendToServer((IMessage)new SexPrompt(string, uUID, (UUID)this.entityDataManager.get(ai).get(), this.ab));
        this.ab = true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setString("owner", ((UUID)this.entityDataManager.get(ai).get()).toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.entityDataManager.set(ai, Optional.of(UUID.fromString(nBTTagCompound.getString("owner"))));
        Z.add(this);
    }

    @Override
    public void PlaySoundAtPosition(SoundEvent soundEvent, float volume, float pitch) {
        Vec3d vec3d = this.net_minecraft_util_math_Vec3d_w();
        if (this.world.isRemote) {
            this.world.playSound(vec3d.x, vec3d.y, vec3d.z, soundEvent, SoundCategory.NEUTRAL, volume, pitch, false);
        } else {
            this.world.playSound(null, new BlockPos(vec3d.x, vec3d.y, vec3d.z), soundEvent, SoundCategory.PLAYERS, volume, pitch);
        }
    }

    @Override
    public void PlaySound(SoundEvent soundEvent) {
        this.PlaySoundAtPosition(soundEvent, 1.0f, 1.0f);
    }

    public void a(SoundEvent[] soundEventArray) {
        this.PlaySoundAtPosition(soundEventArray[this.getRNG().nextInt(soundEventArray.length)], 1.0f, 1.0f);
    }

    @Override
    public void a(SoundEvent soundEvent, float f) {
        this.PlaySoundAtPosition(soundEvent, f, 1.0f);
    }

    @Override
    protected void U() {
    }
}

