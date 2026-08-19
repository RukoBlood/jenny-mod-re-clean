/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  com.google.common.base.Optional
 *  javax.annotation.Nullable
 *  javax.vecmath.Vector4d
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.network.NetworkRegistry$TargetPoint
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.items.ItemStackHandler
 */
package com.trolmastercard.sexmod.girls.Kobold;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.vecmath.Vector4d;

import com.trolmastercard.sexmod.*;
import com.trolmastercard.sexmod.Packets.*;
import com.trolmastercard.sexmod.companion.DoorInteractAIGoal;
import com.trolmastercard.sexmod.companion.fighter.WatchClosestGirlGoal;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Menu.FighterUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.gui.g7_class352;
import com.trolmastercard.sexmod.util.Handlers.PackageHandler;
import com.trolmastercard.sexmod.util.Handlers.SoundsHandler;
import com.trolmastercard.sexmod.util.Point2D;
import com.trolmastercard.sexmod.util.ReferenceAndRotationHelper;
import com.trolmastercard.sexmod.util.VectorMath;
import com.trolmastercard.sexmod.util.interfaces.IEllie;
import com.trolmastercard.sexmod.util.interfaces.IKobold;
import com.trolmastercard.sexmod.world.FakeWorld;
import com.trolmastercard.sexmod.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.resource.GeckoLibCache;

// ff_class308
public class KoboldEntity extends AbstractNpcOnlyEntity implements IEllie, IInventory, IKobold {
    final static public EyeAndKoboldColor COLOR = EyeAndKoboldColor.PURPLE;
    final static public float SCALE = 0.25f;
    final static int ar = 20;
    final static int ag = 2;
    final static int aG = 30;
    final static int ah = 84;
    final static int a3 = 32;
    final static int a1 = 5;
    final static float ae = 1.5f;
    final static float aW = 20.0f;
    final static double au = 10.0;
    final static double ay = 2.0;
    final static double al = 3.0;
    final static int aQ = 300;
    final static int aq = 5;
    final static int aO = 100;
    final static int aB = 100;
    final static int ac = 2;
    final static float am = 2.0f;
    final static int aw = 300;
    final static float aj = 0.2f;
    final static double aH = 0.7;
    final static int aa = 142;
    final static public DataParameter<Float> aE = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.FLOAT).getSerializer().createKey(122);
    final static public DataParameter<String> KOBOLD_NAME = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.STRING).getSerializer().createKey(123);
    final static public DataParameter<Boolean> aC = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(124);
    final static public DataParameter<Boolean> aZ = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(125);
    final static public DataParameter<String> aU = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.STRING).getSerializer().createKey(126);
    final static public DataParameter<Boolean> ak = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(127);
    final static public DataParameter<Boolean> at = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(128);
    final static public DataParameter<Optional<UUID>> aL = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID).getSerializer().createKey(129);
    final static public int av = 24;
    static public double af = 69.0;
    static public List<Vector4d> aY = new ArrayList<Vector4d>();
    ItemStackHandler X = new ItemStackHandler(27);
    public String as = null;
    boolean az = false;
    int aP = 0;
    int U = 0;
    boolean a2 = false;
    int aD = 0;
    int a5 = 0;
    float S = Float.MAX_VALUE;
    static long aV = Long.MIN_VALUE;

    //What the fuck
    String[] DialogueArray = new String[]{
            "What the fuck did you just fucking say about me, you little bitch? I'll have you know I graduated top of my class in the Navy Seals, and I've been involved in numerous secret raids on Al-Quaeda, and I have over 300 confirmed kills. I am trained in gorilla warfare and I'm the top sniper in the entire US armed forces. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Earth, mark my fucking words. You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of spies across the USA and your IP is being traced right now so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your life. You're fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that's just with my bare hands. Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the United States Marine Corps and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little \"clever\" comment was about to bring down upon you, maybe you would have held your fucking tongue. But you couldn't, you didn't, and now you're paying the price, you goddamn idiot. I will shit fury all over you and you will drown in it. You're fucking dead, kiddo.",
            "suck my iron cock you worthless piece of shit!",
            "you'll die a fucking virgin!",
            "not even Johnny sins would wanna stick his cock up ur ass",
            "fuck you with ur borderline illegal fetishes!",
            "ur cum tastes terrible!",
            "I've always faked my orgasms when having sex with you!",
            "Not even Jenny would fuck you for 6 diamonds!",
            "U look like u'd use a shovel to mine diamonds, fucking idiot!",
            "Why tf does ur cock smell like my asshole???",
            "do all of us a favor and hit [ALT]+[F4]!",
            "I'm about to say the N word!",
            "you are under attack retard",
            "Eat my ass!",
            "my tongue is longer than ur fucking dick bitch!",
            "Ligma titties!",
            "touch some grass bitch!"
    };

    IBlockState R = null;
    IBlockState aX = null;
    BlockPos aF = null;
    boolean ao = true;
    Vec3d aS = Vec3d.ZERO;
    BlockPos aM = null;
    BlockPos aI = null;
    int ai = 0;
    int Z = 0;
    int aK = 0;
    int a0 = 0;
    boolean ax = false;
    BlockPos ap = null;
    int ab = 0;
    int aR = 24;
    int W = 0;
    ItemStack ad = null;
    public boolean aA = false;
    int V = -1;
    boolean a4 = true;
    boolean aT = false;
    public boolean Q = false;
    int aN = 0;

    public KoboldEntity(World world) {
        super(world);
        this.setSize(0.5f, 0.99f);
    }

    KoboldEntity(World world, UUID uUID, float f) {
        this(world);
        this.entityDataManager.set(aL, Optional.of(uUID));
        this.entityDataManager.set(aE, f);
    }

    public static KoboldEntity a(World world, UUID uUID) {
        float f = KoboldEntity.float_j();
        return KoboldEntity.a(world, uUID, f);
    }

    public static KoboldEntity a(World world, UUID uUID, float f) {
        af = 10.0 - (double)f * 25.0;
        return new KoboldEntity(world, uUID, f);
    }

    @Override
    protected String generateAppearanceDNA(StringBuilder dnaBuilder) {
        KoboldEntity.appendRandomGeneExclusive(dnaBuilder, 8);
        KoboldEntity.appendRandomGeneExclusive(dnaBuilder, 3);
        KoboldEntity.appendGaussianBodyGene(dnaBuilder);
        KoboldEntity.appendGaussianBodyGene(dnaBuilder);
        KoboldEntity.appendPaddedNumber(dnaBuilder, 2);
        KoboldEntity.appendPaddedNumber(dnaBuilder, 2);
        KoboldEntity.appendPaddedNumber(dnaBuilder, 1);
        KoboldEntity.appendPaddedNumber(dnaBuilder, 1);
        return dnaBuilder.toString();
    }

    @Override
    public ArrayList<Integer> getCustomPartIdList() {
        return new ArrayList<Integer>(){
            {
                this.add(101);
                this.add(EyeAndKoboldColor.values().length);
                this.add(EyeAndKoboldColor.values().length);
                this.add(8);
                this.add(3);
                this.add(101);
                this.add(101);
                this.add(3);
                this.add(3);
                this.add(4);
                this.add(2);
            }
        };
    }

    @Override
    public ArrayList<Integer> L() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(Math.round(this.entityDataManager.get(aE).floatValue() * 100.0f / 0.25f));
        arrayList.add(EyeAndKoboldColor.indexOf(EyeAndKoboldColor.safeValueOf((String)this.entityDataManager.get(CURRENT_ACTION))));
        arrayList.add(EyeAndKoboldColor.indexOf(EyeAndKoboldColor.safeValueOf((Vec3i)this.entityDataManager.get(ACTION_TARGET_POS))));
        return arrayList;
    }

    @Override
    public void setCustomPartList(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        block5: for (int i = 0; i < list.size(); ++i) {
            int n = list.get(i);
            switch (i) {
                case 0: {
                    this.entityDataManager.set(aE, Float.valueOf((float)n / 100.0f * 0.25f));
                    continue block5;
                }
                case 1: {
                    String string = (String)this.entityDataManager.get(CURRENT_ACTION);
                    String string2 = EyeAndKoboldColor.values()[n].toString();
                    if (!string2.equals(string)) {
                        this.aA = true;
                    }
                    this.entityDataManager.set(CURRENT_ACTION, string2);
                    continue block5;
                }
                case 2: {
                    this.entityDataManager.set(ACTION_TARGET_POS, new BlockPos(EyeAndKoboldColor.values()[n].getMainColor()));
                    continue block5;
                }
                default: {
                    KoboldEntity.appendPaddedNumber2(stringBuilder, n);
                }
            }
        }
        this.entityDataManager.set(APPEARANCE_DNA, stringBuilder.toString());
        KoboldRenderer.clearBoneColors();
    }

    void void_m() {
        if (this.customPartsData == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        block5: for (int i = 0; i < this.customPartsData.size(); ++i) {
            Map.Entry entry = (Map.Entry)this.customPartsData.get(i);
            int n = (Integer)((Map.Entry)entry.getValue()).getValue();
            switch (i) {
                case 0: {
                    this.entityDataManager.set(aE, Float.valueOf((float)n / 100.0f * 0.25f));
                    continue block5;
                }
                case 1: {
                    this.entityDataManager.set(CURRENT_ACTION, EyeAndKoboldColor.values()[n].toString());
                    continue block5;
                }
                case 2: {
                    this.entityDataManager.set(ACTION_TARGET_POS, new BlockPos(EyeAndKoboldColor.values()[n].getMainColor()));
                    continue block5;
                }
                default: {
                    KoboldEntity.appendPaddedNumber2(stringBuilder, n);
                }
            }
        }
        this.entityDataManager.set(APPEARANCE_DNA, stringBuilder.toString());
        KoboldRenderer.clearBoneColors();
    }

    @Override
    public Point2D getModelPartByIndex(int index) {
        switch (index) {
            case 0: {
                return new Point2D(160, 0);
            }
            case 1: {
                return new Point2D(180, 0);
            }
            case 2: {
                return new Point2D(200, 0);
            }
            case 3: {
                return new Point2D(220, 0);
            }
            case 4: {
                return new Point2D(227, 20);
            }
            case 5: {
                return new Point2D(140, 40);
            }
            case 6: {
                return new Point2D(160, 40);
            }
            case 7: {
                return new Point2D(180, 40);
            }
            case 8: {
                return new Point2D(227, 40);
            }
            case 9: {
                return new Point2D(0, 130);
            }
            case 10: {
                return new Point2D(20, 130);
            }
        }
        return Point2D.ZERO;
    }

    @Override
    public String getGirlName() {
        return this.entityDataManager.get(KOBOLD_NAME);
    }

    @Override
    public float getScaleFactor() {
        return 0.2f - (0.25f - this.entityDataManager.get(aE).floatValue());
    }

    @Override
    public float getEyeHeight() {
        return 0.94f;
    }

    public static float float_j() {
        return (float)(Math.random() * 0.25);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.values()[this.getRNG().nextInt(EyeAndKoboldColor.values().length)];
        this.entityDataManager.register(ACTION_TARGET_POS, new BlockPos(eyeAndKoboldColor_.getMainColor()));
        this.entityDataManager.register(CURRENT_ACTION, COLOR.name());
        this.entityDataManager.register(aL, Optional.absent());
        this.entityDataManager.register(aE, Float.valueOf(0.0f));
        this.entityDataManager.register(KOBOLD_NAME, KoboldNames.values()[this.getRNG().nextInt(KoboldNames.values().length)].toString());
        this.entityDataManager.register(aC, false);
        this.entityDataManager.register(aZ, false);
        this.entityDataManager.register(aU, "null");
        this.entityDataManager.register(ak, false);
        this.entityDataManager.register(at, false);
    }

    @Override
    protected void initEntityAI() {
        this.watchClosestGirlGoal = new WatchClosestGirlGoal(this, EntityPlayer.class, 3.0f, 1.0f);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAITempt((EntityCreature)this, 0.4, false, new HashSet<Item>(TEMPTATION_ITEMS)));
        this.tasks.addTask(3, new DoorInteractAIGoal(this));
        this.tasks.addTask(5, this.watchClosestGirlGoal);
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return 0.45f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(af);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0);
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected boolean processInteract(EntityPlayer entityPlayer, EnumHand enumHand) {
        if (this.getInteractionPlayerUUID() != null) {
            return false;
        }
        ItemStack itemStack = entityPlayer.getHeldItem(EnumHand.MAIN_HAND);
        if (!itemStack.getItem().equals(Items.NAME_TAG)) {
            itemStack = entityPlayer.getHeldItem(EnumHand.OFF_HAND);
        }
        if (itemStack.getItem().equals(Items.NAME_TAG) && entityPlayer.getPersistentID().toString().equals(this.entityDataManager.get(MASTER))) {
            this.entityDataManager.set(KOBOLD_NAME, itemStack.getDisplayName());
            itemStack.shrink(1);
            return true;
        }
        if (this.entityDataManager.get(aC).booleanValue()) {
            return false;
        }
        if (this.getCurrentAction() == Action.SLEEP) {
            return false;
        }
        ItemStack itemStack2 = entityPlayer.getHeldItem(EnumHand.MAIN_HAND);
        if (itemStack2.getItem() != DragonStaffItem.DRAGON_STAFF) {
            itemStack2 = entityPlayer.getHeldItem(EnumHand.OFF_HAND);
        }
        if (!this.hasMaster() && itemStack2.getItem() == DragonStaffItem.DRAGON_STAFF) {
            if (!this.world.isRemote) {
                return true;
            }
            Optional<UUID> optional = this.entityDataManager.get(aL);
            if (!optional.isPresent()) {
                return true;
            }
            if (!aY.isEmpty()) {
                return true;
            }
            this.m((UUID)optional.get());
            return true;
        }
        if (this.hasMaster() && itemStack2.getItem() == DragonStaffItem.DRAGON_STAFF && ((String)this.entityDataManager.get(MASTER)).equals(entityPlayer.getPersistentID().toString())) {
            entityPlayer.openGui(Main.instance, 1, this.world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
            return true;
        }
        if (this.world.isRemote) {
            if (this.hasMaster() && ((String)this.entityDataManager.get(MASTER)).equals(entityPlayer.getPersistentID().toString())) {
                this.a(SoundsHandler.GIRLS_KOBOLD_MASTER);
            }
            this.openInteractionMenu(entityPlayer);
        } else {
            this.setInteractionPlayerUUID(entityPlayer.getPersistentID());
            this.getNavigator().clearPath();
            this.setYawRotation((float)(Math.atan2(this.posZ - entityPlayer.posZ, this.posX - entityPlayer.posX) * 57.29577951308232 + 90.0));
            this.setTargetPosition(new Vec3d(this.posX, Math.floor(this.posY), this.posZ));
            this.entityDataManager.set(IS_ANCHORED, true);
            this.setCurrentAction(Action.NULL);
        }
        return true;
    }

    @SideOnly(value=Side.CLIENT)
    void m(UUID uUID) {
        Minecraft.getMinecraft().displayGuiScreen(new g7_class352(uUID));
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public boolean openInteractionMenu(EntityPlayer player) {
        if (this.hasMaster() && player.getPersistentID().toString().equals(this.entityDataManager.get(MASTER))) {
            Minecraft.getMinecraft().displayGuiScreen(new FighterUI(this, player, new String[]{"anal", "oral", "mating"}, null, false));
            return true;
        }
        if (this.getActivePotionEffect(HornyPotion.HORNY_POTION) != null) {
            Minecraft.getMinecraft().displayGuiScreen(new FighterUI(this, player, new String[]{"anal", "oral"}, null, false));
            return true;
        }
        Minecraft.getMinecraft().displayGuiScreen(new FighterUI(this, player, new String[]{"anal", "oral"}, new ItemStack[]{new ItemStack(Items.GOLD_INGOT, 3), new ItemStack(Items.IRON_PICKAXE)}, false));
        return true;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void ac() {
        if (this.az) {
            this.az = false;
            return;
        }
        this.setInteractionPlayerUUID((UUID)null);
        this.changeDataParameterFromClient("shouldbeattargetpos", "false");
    }

    @Override
    public void resetCameraAndPhysics() {
        this.Q = false;
        super.resetCameraAndPhysics();
    }

    protected void a(boolean bl, UUID uUID) {
        super.triggerActionSync(bl, true, uUID);
        HandlePlayerMovement.setMovementLock(false);
    }

    @Override
    public void doAction(String string, UUID uUID) {
        this.az = true;
        if ("oral".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", Action.STARTBLOWJOB.toString());
            this.a(true, uUID);
        }
        if ("anal".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", Action.KOBOLD_ANAL_START.toString());
            this.a(true, uUID);
        }
        if ("mating".equals(string)) {
            this.changeDataParameterFromClient("animationFollowUp", Action.MATING_PRESS_START.toString());
            this.a(true, uUID);
        }
    }

    @Override
    public void setDismounted() {
        this.a2 = true;
        this.entityDataManager.set(IS_ANCHORED, false);
    }

    @Override
    protected void onDataWatcherUpdate() {
        KoboldRenderer.clearBoneColors();
    }

    boolean boolean_g() {
        if (!this.a2) {
            return false;
        }
        ++this.aD;
        this.noClip = false;
        this.setNoGravity(false);
        if (this.aD > 40) {
            this.a2 = false;
            this.aD = 0;
            EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
            this.setYawRotation(entityPlayer.rotationYaw + 180.0f);
            this.entityDataManager.set(IS_ANCHORED, true);
            entityPlayer.noClip = true;
            entityPlayer.setNoGravity(true);
            this.noClip = true;
            this.setNoGravity(true);
            this.getNavigator().clearPath();
            this.U();
            return true;
        }
        this.rotationYaw = this.getYawRotation().floatValue();
        this.setNoGravity(false);
        Vec3d vec3d = ReferenceAndRotationHelper.a(this.getPositionVector(), this.getTargetPosition(), 40 - this.aD);
        this.setPosition(vec3d.x, vec3d.y, vec3d.z);
        this.setCurrentAction(Action.NULL);
        Optional<UUID> optional = this.entityDataManager.get(aL);
        if (!optional.isPresent()) {
            return true;
        }
        Collection<KoboldTaskInfo> collection = KoboldManager.getTribeTasks((UUID)optional.get());
        if (collection == null) {
            return true;
        }
        for (KoboldTaskInfo bs_class972 : collection) {
            bs_class972.removeWorker(this);
        }
        return true;
    }

    void o(UUID uUID) {
        if (this.V == -1) {
            return;
        }
        if (++this.V < 132) {
            return;
        }
        this.V = -1;
        if (this.getCurrentAction() != Action.MATING_PRESS_CUM) {
            return;
        }
        UUID uUID2 = this.getInteractionPlayerUUID();
        if (uUID2 == null) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID2);
        if (entityPlayer == null) {
            return;
        }
        EyeAndKoboldColor eyeAndKoboldColor_ = KoboldManager.getTribeColor(uUID);
        ItemStack itemStack = new ItemStack(KoboldEggItem.KOBOLD_EGG, 1, eyeAndKoboldColor_.getWoolMeta());
        NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
        if (nBTTagCompound == null) {
            nBTTagCompound = new NBTTagCompound();
        }
        nBTTagCompound.setString("tribeID", uUID.toString());
        nBTTagCompound.setString("tribeColor", eyeAndKoboldColor_.toString());
        itemStack.setTagCompound(nBTTagCompound);
        entityPlayer.inventory.addItemStackToInventory(itemStack);
    }

    @Override
    public void updateAITasks() {
        //Object object;
        super.updateAITasks();
        this.ax = false;
        Optional<UUID> optional = this.entityDataManager.get(aL);
        if (optional.isPresent()) {
            this.o((UUID)optional.get());
            KoboldManager.updateLeaderIfDead((UUID)optional.get());
            EntityPlayer object = this.getMasterPlayer();
            if (object != null) {
                KoboldManager.setTribeMaster((UUID)optional.get(), object.getPersistentID());
            }
        }
        if (this.boolean_g()) {
            return;
        }
        if (this.getInteractionPlayerUUID() != null) {
            return;
        }
        if (!this.entityDataManager.get(aC).booleanValue()) {
            if (this.getHealth() != this.getMaxHealth() && ++this.a5 >= 100) {
                this.setHealth(this.getHealth() + 2.0f);
                this.a5 = 0;
                PackageHandler.INSTANCE.sendToAllTracking((IMessage)new SpawnParticle(this.girlID(), EnumParticleTypes.HEART.getParticleName()), (Entity)this);
            }
        } else {
            this.a5 = 0;
        }
        if (!((Boolean)this.entityDataManager.get(IS_ANCHORED)).booleanValue()) {
            this.setNoGravity(false);
        }
        if (!optional.isPresent()) {
            return;
        }
        --this.aP;
        if (this.getCurrentAction() == Action.ATTACK) {
            this.getNavigator().clearPath();
            this.rotationYaw = this.getYawRotation().floatValue();
            this.rotationYawHead = this.getYawRotation().floatValue();
            ++this.U;
            if (22 == this.U) {
                this.u_();
            }
            if (32 == this.U) {
                HashSet<EntityLivingBase> object = KoboldManager.getTribeTargets(optional.get());
                HashSet<EntityLivingBase> hashSet = new HashSet<>();
                for (EntityLivingBase entityLivingBase : object) {
                    if (entityLivingBase.getDistance(this) > 2.0f) continue;
                    entityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0f);
                    if (!entityLivingBase.isDead) continue;
                    hashSet.add(entityLivingBase);
                }
                for (EntityLivingBase entityLivingBase : hashSet) {
                    KoboldManager.removeTribeTarget((UUID)optional.get(), entityLivingBase);
                }
            }
            if (84 <= this.U) {
                this.setCurrentAction(Action.NULL);
                this.entityDataManager.set(IS_ANCHORED, false);
                this.U = 0;
            }
            return;
        }
        this.entityDataManager.set(aC, this.c((UUID)optional.get(), false));
        this.entityDataManager.set(aZ, KoboldManager.isTribeLeader((UUID)optional.get(), this));
        this.entityDataManager.set(ak, KoboldManager.isTribeAlerted((UUID)optional.get()));
        this.void_d();
        this.void_h();
        this.watchClosestGirlGoal.isWatching = this.boolean_o();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.void_t();
        this.void_v();
        this.void_q();
        this.void_w();
        this.void_m();
    }

    void void_w() {
        if (!this.world.isRemote) {
            return;
        }
        if (this.world.getTotalWorldTime() - 300L < aV) {
            return;
        }
        if (!this.hasMaster()) {
            return;
        }
        if (this.getCurrentAction() != Action.NULL) {
            return;
        }
        if (!"".equals(this.entityDataManager.get(GIRL_HAND_STATES))) {
            return;
        }
        if (this.entityDataManager.get(ak).booleanValue()) {
            return;
        }
        String string = (String)this.entityDataManager.get(MASTER);
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
        if (entityPlayer == null) {
            this.S = Float.MAX_VALUE;
            return;
        }
        if (!entityPlayer.getPersistentID().toString().equals(string)) {
            return;
        }
        float f = this.getDistance(entityPlayer);
        if (f < 2.0f && this.S > 2.0f) {
            this.b(SoundsHandler.random(SoundsHandler.GIRLS_KOBOLD_HEYMASTER));
            this.sendLocalClientMessage("Hey master!");
            aV = this.world.getTotalWorldTime();
        }
        this.S = f;
    }

    void void_q() {
        if (!this.world.isRemote) {
            return;
        }
        if (this.getCurrentAction() == Action.SLEEP) {
            return;
        }
        if (!this.entityDataManager.get(ak).booleanValue()) {
            return;
        }
        if (!this.hasMaster()) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(UUID.fromString((String)this.entityDataManager.get(MASTER)));
        if (entityPlayer == null) {
            return;
        }
        this.void_b(entityPlayer);
    }

    void void_t() {
        if (this.entityDataManager.get(aC).booleanValue()) {
            return;
        }
        if (this.hasMaster()) {
            return;
        }
        Optional<UUID> optional = this.entityDataManager.get(aL);
        if (!optional.isPresent()) {
            return;
        }
        for (EntityPlayer entityPlayer : this.world.playerEntities) {
            double d;
            double d2 = d = entityPlayer.getPositionVector().distanceTo(this.getPositionVector());
            if (!this.world.isRemote) {
                for (KoboldEntity object : KoboldManager.getTribeMembersList((UUID)optional.get())) {
                    double d3 = entityPlayer.getPositionVector().distanceTo(object.getPositionVector());
                    if (!(d3 < d2)) continue;
                    d2 = d3;
                }
            }
            if (d2 > 10.0) continue;
            if (entityPlayer.getHeldItem(EnumHand.MAIN_HAND).getItem() != DragonStaffItem.DRAGON_STAFF && entityPlayer.getHeldItem(EnumHand.OFF_HAND).getItem() != DragonStaffItem.DRAGON_STAFF) {
                return;
            }
            PathNavigate pathNavigate = this.getNavigator();
            pathNavigate.clearPath();
            if (this.world.isRemote) {
                this.void_b(entityPlayer);
            } else if (d > 2.0) {
                BlockPos blockPos = this.c(entityPlayer.getPosition());
                pathNavigate.tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.35f);
            }
            return;
        }
    }

    @Override
    protected void U() {
        boolean bl;
        String string = this.entityDataManager.get(GirlEntity.GIRL_HAND_STATES);
        boolean bl2 = this.getActivePotionEffect(HornyPotion.HORNY_POTION) != null;
        boolean bl3 = false;
        if (this.hasMaster()) {
            bl3 = ((String)this.entityDataManager.get(MASTER)).equals(this.getInteractionPlayerUUID().toString());
        }
        boolean bl4 = bl = !bl2 && !bl3;
        if (string.equals(Action.STARTBLOWJOB.toString())) {
            if (!bl || this.getCurrentAction() == Action.PAYMENT) {
                this.setCurrentAction(Action.STARTBLOWJOB);
            } else {
                this.setCurrentAction(Action.PAYMENT);
            }
        }
        if (string.equals(Action.KOBOLD_ANAL_START.toString())) {
            if (!bl || this.getCurrentAction() == Action.PAYMENT) {
                this.setCurrentAction(Action.KOBOLD_ANAL_START);
            } else {
                this.setCurrentAction(Action.PAYMENT);
            }
        }
        if (string.equals(Action.MATING_PRESS_START.toString())) {
            this.setCurrentAction(Action.MATING_PRESS_START);
        }
    }

    void void_v() {
        if (!this.world.isRemote) {
            return;
        }
        UUID uUID = this.getInteractionPlayerUUID();
        if (uUID == null) {
            return;
        }
        if (!((Boolean)this.entityDataManager.get(IS_ANCHORED)).booleanValue()) {
            return;
        }
        if (this.getCurrentAction() != Action.NULL) {
            return;
        }
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID);
        if (entityPlayer == null) {
            return;
        }
        this.void_b(entityPlayer);
    }

    void void_b(EntityPlayer entityPlayer) {
        PlayerGirl ei_class2512 = PlayerGirl.getUUIDHashtable(entityPlayer.getPersistentID());
        Vec3d vec3d = new Vec3d(entityPlayer.posX, entityPlayer.posY + (double)(ei_class2512 == null ? entityPlayer.eyeHeight : ei_class2512.getEyeHeight()), entityPlayer.posZ);
        Vec3d vec3d2 = new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        double d = vec3d2.distanceTo(vec3d);
        double d2 = vec3d.y - vec3d2.y;
        this.rotationPitch = (float)(-(Math.sin(d2 / d) * 57.29577951308232));
    }

    // TODO / clash
    void u_() {
    }

    @CheckReturnValue
    boolean boolean_o() {
        if (this.getCurrentAction() != Action.NULL) {
            return false;
        }
        if (Math.abs(this.motionX) + Math.abs(this.motionZ) > 0.01) {
            return false;
        }
        return !this.IsBlockedByCeiling();
    }

    void void_d() {
        Optional<UUID> optional = this.entityDataManager.get(aL);
        if (!optional.isPresent()) {
            return;
        }
        UUID uUID = (UUID)optional.get();
        if (!this.entityDataManager.get(aC) && KoboldManager.isTribeAlerted(uUID)) {
            if (!this.hasMaster()) {
                return;
            }
            EntityPlayer entityPlayer = this.getMasterPlayer();
            if (entityPlayer == null) {
                return;
            }
            for (KoboldTaskInfo bs_class972 : KoboldManager.getTribeTasks(uUID)) {
                if (!bs_class972.hasWorker(this)) continue;
                bs_class972.removeWorker(this);
                this.setCurrentAction(Action.NULL);
                this.entityDataManager.set(IS_ANCHORED, false);
            }
            this.noClip = false;
            this.setNoGravity(false);
            PathNavigate pathNavigate = this.getNavigator();
            double d = this.getPositionVector().distanceTo(entityPlayer.getPositionVector());
            if (d > 2.0) {
                pathNavigate.tryMoveToEntityLiving(entityPlayer, this.a(entityPlayer, d));
                this.applyCustomPathNodeVelocity();
                if (d > 15.0) {
                    this.void_c(entityPlayer);
                }
            }
        } else if (KoboldManager.isTribeLeader(uUID, this)) {
            this.void_b(uUID);
        } else {
            this.n(uUID);
        }
    }

    protected double a(EntityPlayer entityPlayer, double d) {
        double d2 = entityPlayer.isSprinting() ? 0.7 : 0.35;
        double d3 = Math.floor(d / 5.0) * 0.3;
        d2 += d3;
        if (this.isInWater()) {
            d2 *= 60.0;
        }
        return d2;
    }

    void s(UUID uUID) {
        BlockPos blockPos = KoboldManager.getTribeHomePos(uUID);
        if (blockPos == null) {
            return;
        }
        if (this.aX != null) {
            this.world.setBlockState(blockPos, this.aX);
        }
        if (this.R != null) {
            this.world.setBlockState(blockPos.add(0, -1, 0), this.R);
        }
    }

    void void_b(UUID uUID) {
        TribeState fm_class3192;
        if (this.d______JustUseAiToDeobfuscate(uUID)) { // TODO clash below
            return;
        }
        if (!this.hasMaster() && KoboldManager.hasAssignedMaster(uUID)) {
            this.getNavigator().clearPath();
            this.aM = null;
            return;
        }
        TribeState fm_class3193 = KoboldManager.getTribeState(uUID);
        if (fm_class3193 != (fm_class3192 = this.com_trolmastercard_sexmod_fm_class319_p())) {
            KoboldManager.setTribeState(uUID, fm_class3192);
            switch (fm_class3192) {
                case REST: {
                    this.p(uUID);
                    KoboldManager.setTribeHomePos(uUID, (BlockPos) null);
                    this.sendGirlChatMessage("okay resting time owo");
                    break;
                }
                case ACTIVE: {
                    this.s(uUID);
                    this.q(uUID);
                }
            }
        }
        switch (fm_class3192) {
            case ACTIVE: {
                this.aF = null;
                this.void_c(uUID);
                break;
            }
            case REST: {
                this.l(uUID);
            }
        }
    }

    void p(UUID uUID) {
        Collection<KoboldTaskInfo> collection = KoboldManager.getTribeTasks(uUID);
        if (collection == null) {
            return;
        }
        for (KoboldTaskInfo bs_class972 : collection) {
            bs_class972.resetAllWorkers();
        }
    }

    void q(UUID uUID) {
        if (!this.hasMaster()) {
            return;
        }
        List<KoboldEntity> list = KoboldManager.getTribeMembersList(uUID);
        for (KoboldEntity ff_class3082 : list) {
            KoboldManager.removeBedForKobold(ff_class3082);
            if (ff_class3082.getInteractionPlayerUUID() != null) continue;
            ff_class3082.noClip = false;
            ff_class3082.setNoGravity(false);
            ff_class3082.getDataManager().set(IS_ANCHORED, false);
            ff_class3082.setCurrentAction(Action.NULL);
        }
    }

    void l(UUID uUID) {
        Collection<KoboldTaskInfo> collection = KoboldManager.getTribeTasks(uUID);
        if (collection != null) {
            for (KoboldTaskInfo bs_class972 : collection) {
                bs_class972.removeWorker(this);
            }
        }
        if (this.hasMaster()) {
            this.void_i(uUID);
        } else {
            this.void_a(uUID);
        }
    }

    void void_i(UUID uUID) {
        BlockPos[] blockPosArray = KoboldManager.getBedForKobold(this);
        if (blockPosArray != null) {
            Vec3d vec3d = new Vec3d((float)blockPosArray[0].getX() + 0.5f, (double)blockPosArray[0].getY() + 0.5625, (float)blockPosArray[0].getZ() + 0.5f);
            Vec3d vec3d2 = new Vec3d((float)blockPosArray[1].getX() + 0.5f, (double)blockPosArray[1].getY() + 0.5625, (float)blockPosArray[1].getZ() + 0.5f);
            boolean bl = vec3d.subtract((Vec3d)vec3d2).x == 0.0;
            Vec3d vec3d3 = ReferenceAndRotationHelper.LerpVec3d(vec3d, vec3d2, 0.5);
            this.entityDataManager.set(IS_ANCHORED, true);
            this.setTargetPosition(vec3d3);
            this.setYawRotation(bl ? 0.0f : 90.0f);
            this.noClip = true;
            this.setNoGravity(true);
            return;
        }
        HashSet<BlockPos> hashSet = KoboldManager.getTribeBeds(uUID);
        Vec3i vec3i = null;
        if (hashSet == null) {
            return;
        }
        for (BlockPos blockPos : hashSet) {
            IBlockState iBlockState = this.world.getBlockState(blockPos);
            boolean bl = false;
            for (Map.Entry entry : iBlockState.getProperties().entrySet()) {
                if (!(entry.getKey() instanceof PropertyBool)) continue;
                bl = (Boolean)entry.getValue();
                break;
            }
            if (bl || KoboldManager.isBedAssigned(blockPos)) continue;
            if (vec3i == null) {
                vec3i = blockPos;
                continue;
            }
            if (!(this.getDistanceSq((BlockPos)vec3i) > this.getDistanceSq(blockPos))) continue;
            vec3i = blockPos;
        }
        if (vec3i == null) {
            return;
        }
        if (vec3i.getDistance((int)this.posX, (int)this.posY, (int)this.posZ) > 2.0) {
            if (Math.abs(((BlockPos)vec3i).subtract(this.getPosition()).getY()) > 4) {
                this.b(((BlockPos)vec3i).add(0, 1, 0));
            } else {
                BlockPos blockPos = this.c((BlockPos)vec3i);
                this.getNavigator().tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.35f);
                if (this.getNavigator().getPath() == null) {
                    this.b(((BlockPos)vec3i).add(0, 1, 0));
                }
            }
            return;
        }
        KoboldManager.assignBedToKobold(this, (BlockPos)vec3i);
        this.setCurrentAction(Action.SLEEP);
    }

    void void_a(UUID uUID) {
        BlockPos blockPos = KoboldManager.getTribeHomePos(uUID);
        if (blockPos == null && KoboldManager.isTribeLeader(uUID, this)) {
            BlockPos blockPos2 = this.getPosition().add(1, 0, 0);
            this.R = this.world.getBlockState(blockPos2.add(0, -1, 0));
            this.aX = this.world.getBlockState(blockPos2);
            this.world.setBlockState(blockPos2.add(0, -1, 0), Blocks.NETHERRACK.getDefaultState());
            this.world.setBlockState(blockPos2, SexFire.FIRE.getDefaultState());
            KoboldManager.setTribeHomePos(uUID, blockPos2);
        }
        if (blockPos == null) {
            return;
        }
        if (this.aF == null) {
            this.aF = blockPos.add((this.getRNG().nextBoolean() ? 1 : -1) * (this.getRNG().nextInt(2) + 1), 0, (this.getRNG().nextBoolean() ? 1 : -1) * (this.getRNG().nextInt(2) + 1));
        }
        this.getNavigator().tryMoveToXYZ(this.aF.getX(), this.aF.getY(), this.aF.getZ(), 0.35f);
        this.applyCustomPathNodeVelocity();
    }

    void void_c(UUID uUID) {
        if (this.hasMaster()) {
            KoboldManager.setTribeHomePos(uUID, (BlockPos) null);
            this.g_(uUID);
            return;
        }
        Collection<KoboldTaskInfo> collection = KoboldManager.getTribeTasks(uUID);
        if (collection == null) {
            return;
        }
        if (this.ao) {
            this.aM = null;
            this.b(uUID, collection);
        } else {
            this.a(uUID, collection);
        }
    }

    void b(UUID uUID, Collection<KoboldTaskInfo> collection) {
        if (collection.isEmpty()) {
            this.ao = false;
            this.r(uUID);
            this.sendGirlChatMessage("Lets go somewhere else");
        }
    }

    void a(UUID uUID, Collection<KoboldTaskInfo> collection) {
        BlockPos blockPos = KoboldManager.getTribeHomePos(uUID);
        if (blockPos == null) {
            this.r(uUID);
            return;
        }
        if (this.ticksExisted % 40 == 0) {
            if (this.aS.equals(this.getPositionVector())) {
                this.r(uUID);
                this.aM = null;
            }
            this.aS = this.getPositionVector();
        }
        if (this.aM == null || this.aM.getDistance((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0) {
            this.aM = this.t(uUID);
        }
        this.getNavigator().tryMoveToXYZ(this.aM.getX(), this.aM.getY(), this.aM.getZ(), 0.35f);
        this.applyCustomPathNodeVelocity();
        if (Math.sqrt(this.getPosition().distanceSq(blockPos)) > 5.0) {
            return;
        }
        this.ao = true;
        this.sendGirlChatMessage("Time to work bitches!");
        int n = KoboldManager.getTribeMemberCount(uUID);
        for (int i = 1; i < n; ++i) {
            this.c(uUID, collection);
        }
        KoboldManager.setTribeHomePos(uUID, (BlockPos) null);
    }

    protected void void_c(EntityPlayer entityPlayer) {
        BlockPos blockPos;
        int n = 0;
        do {
            blockPos = entityPlayer.getPosition().add(ReferenceAndRotationHelper.RANDOM.nextInt(10), 0, ReferenceAndRotationHelper.RANDOM.nextInt(10));
        } while (++n < 20 && !this.attemptTeleport(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        if (n == 20) {
            this.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        }
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }

    BlockPos t(UUID uUID) {
        BlockPos blockPos = KoboldManager.getTribeHomePos(uUID);
        if (blockPos == null) {
            return BlockPos.ORIGIN;
        }
        return this.c(blockPos);
    }

    BlockPos c(BlockPos blockPos) {
        BlockPos blockPos2 = this.getPosition();
        BlockPos blockPos3 = blockPos.subtract(blockPos2);
        if (Math.abs(blockPos3.getX()) + Math.abs(blockPos3.getZ()) < 20) {
            return blockPos;
        }
        double d = Math.min(Math.abs(blockPos3.getX()), Math.abs(blockPos3.getZ()));
        double d2 = Math.max(Math.abs(blockPos3.getX()), Math.abs(blockPos3.getZ()));
        double d3 = d / (d2 + d);
        int n = (int)((double)((blockPos3.getX() > 0 ? 1 : -1) * 20) * (d == (double)Math.abs(blockPos3.getX()) ? d3 : 1.0 - d3));
        int n2 = (int)((double)((blockPos3.getZ() > 0 ? 1 : -1) * 20) * (d == (double)Math.abs(blockPos3.getZ()) ? d3 : 1.0 - d3));
        BlockPos blockPos4 = this.getPosition().add(n, 0, n2);
        blockPos4 = new BlockPos(blockPos4.getX(), WorldUtils.getHeightAt(this.world, blockPos4.getX(), blockPos4.getZ()) + 1, blockPos4.getZ());
        return blockPos4;
    }

    void r(UUID uUID) {
        BlockPos blockPos;
        int n = 0;
        do {
            blockPos = this.getPosition();
            blockPos = blockPos.add((50 + this.getRNG().nextInt(50)) * (this.getRNG().nextBoolean() ? 1 : -1), 0, (50 + this.getRNG().nextInt(50)) * (this.getRNG().nextBoolean() ? 1 : -1));
        } while (((blockPos = new BlockPos(blockPos.getX(), WorldUtils.getHeightAt(this.world, blockPos.getX(), blockPos.getZ()), blockPos.getZ())).getY() <= 0 || !this.getNavigator().canEntityStandOnPos(blockPos)) && ++n < 100);
        KoboldManager.setTribeHomePos(uUID, blockPos);
    }

    void c(UUID uUID, Collection<KoboldTaskInfo> collection) {
        List<BlockPos> list = this.a(this.getPosition(), BlockLog.class, 30, 4, null);
        BlockPos blockPos = null;
        for (BlockPos blockPos2 : list) {
            Block block = this.world.getBlockState(blockPos2.down()).getBlock();
            if (block instanceof BlockLog || block == Blocks.AIR) continue;
            boolean bl = false;
            for (KoboldTaskInfo bs_class972 : collection) {
                if (!bs_class972.containsBlock(blockPos2)) continue;
                bl = true;
                break;
            }
            if (bl) continue;
            blockPos = blockPos2;
            break;
        }
        if (blockPos == null) {
            return;
        }
        KoboldTaskInfo.createTreeFellingTask(this.world, blockPos, uUID);
        this.sendGirlChatMessage("Someone, go fall this tree!");
    }

    @CheckReturnValue
    TribeState com_trolmastercard_sexmod_fm_class319_p() {
        long l = this.world.getWorldTime();
        if (l < 12000L) {
            return TribeState.ACTIVE;
        }
        return TribeState.REST;
    }

    // TODO / dup clash with 'List<...> GirlEntity::d()'
    //  TODO rename to d___...
    @CheckReturnValue
    boolean d______JustUseAiToDeobfuscate(UUID uUID) {
        return this.c(uUID, true);
    }

    @CheckReturnValue
    boolean c(UUID uUID, boolean bl) {
        //Optional<UUID> optional;
        HashSet<EntityLivingBase> hashSet = KoboldManager.getTribeTargets(uUID);
        KoboldEntity ff_class3082 = KoboldManager.getTribeLeader(uUID);
        if (ff_class3082 == null) {
            return false;
        }
        for (KoboldEntity object2 : this.world.getEntitiesWithinAABB(KoboldEntity.class, new AxisAlignedBB(ff_class3082.posX - 30.0, ff_class3082.posY - 30.0, ff_class3082.posZ - 30.0, ff_class3082.posX + 30.0, ff_class3082.posY + 30.0, ff_class3082.posZ + 30.0))) {
            if (!this.canEntityBeSeen(object2) || object2.hasMaster() && this.hasMaster()) continue;
            Optional<UUID> optional = object2.getDataManager().get(aL);
            if (!optional.isPresent()) {
                hashSet.add(object2);
                continue;
            }
            if (((UUID)optional.get()).equals(uUID)) continue;
            hashSet.add(object2);
        }
        EntityLivingBase object3 = null;
        ArrayList<EntityLivingBase> arrayList = new ArrayList<EntityLivingBase>();
        for (EntityLivingBase f : hashSet) {
            if (f.isDead) {
                arrayList.add(f);
                continue;
            }
            if (ff_class3082.getDistance(f) > 30.0f || object3 != null && !(this.getDistance((Entity)object3) > this.getDistance(f))) continue;
            object3 = f;
        }
        for (EntityLivingBase f : arrayList) {
            KoboldManager.removeTribeTarget(uUID, f);
        }
        if (object3 == null) {
            return false;
        }
        if (!bl) {
            return true;
        }
        if (this.getCurrentAction() != Action.ATTACK) {
            this.entityDataManager.set(IS_ANCHORED, false);
            this.setCurrentAction(Action.NULL);
        }
        BlockPos blockPos = this.c((object3).getPosition());
        this.getNavigator().tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.7);
        this.applyCustomPathNodeVelocity();
        if (this.getDistance((Entity)object3) > 1.5f) {
            return true;
        }
        if (this.aP > 0) {
            return true;
        }
        float f = (float)(Math.atan2(this.posZ - ((EntityLivingBase)object3).posZ, this.posX - ((EntityLivingBase)object3).posX) * 57.29577951308232 + 90.0);
        this.setYawRotation(f);
        this.setCurrentAction(Action.ATTACK);
        this.aP = 84;
        return true;
    }

    void n(UUID uUID) {
        if (this.d______JustUseAiToDeobfuscate(uUID)) {
            return;
        }
        TribeState fm_class3192 = KoboldManager.getTribeState(uUID);
        switch (fm_class3192) {
            case REST: {
                this.l(uUID);
                break;
            }
            case ACTIVE: {
                this.aF = null;
                this.h_(uUID); // void
            }
        }
    }

    // TODO clash upstream
    void h_(UUID uUID) {
        BlockPos blockPos = KoboldManager.getTribeHomePos(uUID);
        if (blockPos == null) {
            this.aM = null;
            this.g_(uUID);
            return;
        }
        KoboldEntity ff_class3082 = KoboldManager.getTribeLeader(uUID);
        if (KoboldManager.hasAssignedMaster(uUID)) {
            this.getNavigator().clearPath();
            this.aM = null;
            return;
        }
        if (ff_class3082 == null) {
            System.out.println("leader of tribe " + uUID + " is null");
            return;
        }
        if (ff_class3082.getDistance(this) > 20.0f) {
            this.setPosition(ff_class3082.posX, ff_class3082.posY, ff_class3082.posZ);
            this.aM = null;
        }
        if (this.ticksExisted % 40 == 0) {
            if (this.aS.equals(this.getPositionVector())) {
                this.aM = this.t(uUID);
            }
            this.aS = this.getPositionVector();
        }
        if (this.aM == null || this.aM.getDistance((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0) {
            this.aM = this.t(uUID);
        }
        this.getNavigator().tryMoveToXYZ(this.aM.getX(), this.aM.getY(), this.aM.getZ(), 0.35f);
        this.applyCustomPathNodeVelocity();
    }

    // TODO clashes
    void g_(UUID uUID) {
        if (this.getInteractionPlayerUUID() != null) {
            return;
        }
        Collection<KoboldTaskInfo> collection = KoboldManager.getTribeTasks(uUID);
        if (collection == null) {
            return;
        }
        KoboldTaskInfo bs_class972 = null;
        for (KoboldTaskInfo bs_class973 : collection) {
            if (!bs_class973.hasWorker(this)) continue;
            bs_class972 = bs_class973;
            break;
        }
        if (bs_class972 == null) {
            for (KoboldTaskInfo bs_class973 : collection) {
                if (this.hasMaster() && !this.c(uUID, bs_class973)) continue;
                if (!this.a(bs_class973)) {
                    this.ax = true;
                    continue;
                }
                if (!bs_class973.assignWorker(this)) continue;
                bs_class972 = bs_class973;
                this.aI = null;
                if (bs_class973.getTaskType() == KoboldTaskInfo.KoboldTask.FALL_TREE) {
                    this.sendGirlChatMessage("Ima fall this tree owo");
                    break;
                }
                this.sendGirlChatMessage("Ima go mine uwu");
                this.b(bs_class973.getOriginPos());
                this.world.setBlockState(bs_class973.getOriginPos(), Blocks.AIR.getDefaultState());
                break;
            }
        }
        if (bs_class972 == null) {
            this.u(uUID);
            return;
        }
        if (bs_class972.getTaskType() == KoboldTaskInfo.KoboldTask.FALL_TREE) {
            this.a(uUID, bs_class972.getOriginPos(), bs_class972);
        }
        if (bs_class972.getTaskType() == KoboldTaskInfo.KoboldTask.MINE) {
            this.b(uUID, bs_class972);
        }
    }

    void b(BlockPos blockPos) {
        PackageHandler.INSTANCE.sendToAllTracking((IMessage)new SpawnParticle(this.girlID(), EnumParticleTypes.PORTAL.getParticleName(), 30), new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 30.0));
        this.setPosition(0.5f + (float)blockPos.getX(), blockPos.getY(), 0.5f + (float)blockPos.getZ());
        PackageHandler.INSTANCE.sendToAllTracking((IMessage)new SpawnParticle(this.girlID(), EnumParticleTypes.PORTAL.getParticleName(), 30), new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 30.0));
    }

    void b(UUID uUID, KoboldTaskInfo bs_class972) {
        if (this.getCurrentAction() != Action.MINE) {
            this.a(uUID, bs_class972);
            return;
        }
        --this.Z;
        --this.ai;
        if (this.ai == 0) {
            //Object object;
            IBlockState iBlockState = this.world.getBlockState(this.aI.up());
            if (!(iBlockState.getBlock() instanceof BlockFalling)) {
                bs_class972.removeBlocks(this.aI);
                EntityPlayer object = this.getMasterPlayer();
                if (object != null) {
                    PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(this.aI, false), (EntityPlayerMP)object);
                }
            }
            IBlockState object = this.world.getBlockState(this.aI);
            this.b(new ItemStack(object.getBlock().getItemDropped((IBlockState)object, this.getRNG(), 0), 1, object.getBlock().damageDropped((IBlockState)object)));
            this.world.destroyBlock(this.aI, false);
        }
        if (this.Z <= 0) {
            this.Z = 100;
            this.ai = 24;
            this.setCurrentAction(Action.NULL);
        }
    }

    void a(UUID uUID, KoboldTaskInfo task) {
        PathNavigate pathNavigate = this.getNavigator();
        if (this.aI == null || !task.getTargetBlocks().contains(this.aI)) {
            BlockPos blockPos;
            this.aI = this.a(task, uUID);
            if (this.aI == null) {
                boolean bl = task.getTargetBlocks().isEmpty();
                HashSet<BlockPos> hashSet = KoboldManager.removeTaskAndGetBlocks(uUID, task);
                UUID uUID2 = KoboldManager.getTribeMasterUUID(uUID);
                if (uUID2 == null) {
                    return;
                }
                EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(uUID2);
                if (entityPlayer == null) {
                    return;
                }
                if (!bl) {
                    entityPlayer.sendMessage(new TextComponentString(String.format("<%s> It's impossible to mine here...", this.getGirlName())));
                }
                PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(hashSet, false), (EntityPlayerMP)entityPlayer);
                return;
            }
            if (Math.abs(this.getPosition().getY() - task.getOriginPos().getY()) > 3) {
                blockPos = task.getOriginPos().add(task.getFacing().getOpposite().getDirectionVec());
                this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                this.b(blockPos);
            }
            blockPos = this.aI.add(task.getFacing().getOpposite().getDirectionVec());
            pathNavigate.tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.35f);
            return;
        }
        IBlockState iBlockState = this.world.getBlockState(this.aI);
        if (!this.boolean_a(new ItemStack(iBlockState.getBlock().getItemDropped(iBlockState, ReferenceAndRotationHelper.RANDOM, 0)))) {
            this.ax = true;
            this.b(uUID, true);
            return;
        }
        if (this.motionX != 0.0 || this.motionZ != 0.0 || !this.onGround || this.getDistance(this.aI.getX(), this.aI.getY(), this.aI.getZ()) > 3.0 || ++this.aK < 10) {
            BlockPos blockPos = this.aI.add(task.getFacing().getOpposite().getDirectionVec());
            pathNavigate.tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.35f);
            return;
        }
        pathNavigate.clearPath();
        this.aK = 0;
        this.setCurrentAction(Action.MINE);
        this.rotationYaw = this.rotationYawHead = (float)(Math.atan2(this.posZ - (double)this.aI.getZ(), this.posX - (double)this.aI.getX()) * 57.29577951308232 + 90.0);
        this.entityDataManager.set(at, false);
    }

    /*
     * WARNING - void declaration
     */
    /*
    @Deprecated
    BlockPos a___(bs_class97 bs_class972, UUID uUID) {
        //void var14_33;
        //Object object;
        //Object object2;
        BlockPos blockPos;
        HashSet<BlockPos> hashSet = bs_class972.g();
        EnumFacing enumFacing = bs_class972.f();
        ArrayList<BlockPos> arrayList2 = new ArrayList<BlockPos>();
        Integer n = null;
        if (hashSet.isEmpty()) {
            return null;
        }
        for (BlockPos arrayList3 : hashSet) {
            switch (enumFacing) {
                case NORTH: {
                    if (n != null && arrayList3.getZ() < n) break;
                    n = arrayList3.getZ();
                    arrayList2.add(arrayList3);
                    break;
                }
                case SOUTH: {
                    if (n != null && arrayList3.getZ() > n) break;
                    n = arrayList3.getZ();
                    arrayList2.add(arrayList3);
                    break;
                }
                case EAST: {
                    if (n != null && arrayList3.getX() > n) break;
                    n = arrayList3.getX();
                    arrayList2.add(arrayList3);
                    break;
                }
                case WEST: {
                    if (n != null && arrayList3.getX() < n) break;
                    n = arrayList3.getX();
                    arrayList2.add(arrayList3);
                }
            }
        }
        ArrayList arrayList4 = new ArrayList();
        for (BlockPos blockPos2 : arrayList2) {
            if ((enumFacing == EnumFacing.NORTH || enumFacing == EnumFacing.SOUTH) && blockPos2.getZ() == n.intValue()) {
                arrayList4.add(blockPos2);
            }
            if (enumFacing != EnumFacing.EAST && enumFacing != EnumFacing.WEST || blockPos2.getX() != n.intValue()) continue;
            arrayList4.add(blockPos2);
        }
        if (arrayList4.isEmpty()) {
            return null;
        }
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        EnumFacing enumFacing2 = bs_class972.f();
        BlockPos blockPos3 = bs_class972.b();
        if (enumFacing2.getAxis() == EnumFacing.Axis.Z) {
            blockPos = new BlockPos(blockPos3.getX(), blockPos3.getY(), ((BlockPos)arrayList4.get(0)).getZ());
            blockPos = enumFacing2 == EnumFacing.NORTH ? blockPos.north() : blockPos.south();
            arrayList.add(blockPos.down());
            arrayList.add(blockPos.down().east());
            arrayList.add(blockPos.down().west());
            arrayList.add(blockPos);
            arrayList.add(blockPos.up());
            arrayList.add(blockPos.up().up());
            arrayList.add(blockPos.up().up().up());
            arrayList.add(blockPos.west());
            arrayList.add(blockPos.west().up());
            arrayList.add(blockPos.west().up().up());
            arrayList.add(blockPos.west().up().up().up());
            arrayList.add(blockPos.west().west());
            arrayList.add(blockPos.west().west().up());
            arrayList.add(blockPos.west().west().up().up());
            arrayList.add(blockPos.east());
            arrayList.add(blockPos.east().up());
            arrayList.add(blockPos.east().up().up());
            arrayList.add(blockPos.east().up().up().up());
            arrayList.add(blockPos.east().east());
            arrayList.add(blockPos.east().east().up());
            arrayList.add(blockPos.east().east().up().up());
        } else {
            blockPos = new BlockPos(((BlockPos)arrayList4.get(0)).getX(), blockPos3.getY(), blockPos3.getZ());
            blockPos = enumFacing2 == EnumFacing.EAST ? blockPos.east() : blockPos.west();
            arrayList.add(blockPos.down());
            arrayList.add(blockPos.down().north());
            arrayList.add(blockPos.down().south());
            arrayList.add(blockPos);
            arrayList.add(blockPos.up());
            arrayList.add(blockPos.up().up());
            arrayList.add(blockPos.up().up().up());
            arrayList.add(blockPos.south());
            arrayList.add(blockPos.south().up());
            arrayList.add(blockPos.south().up().up());
            arrayList.add(blockPos.south().up().up().up());
            arrayList.add(blockPos.south().south());
            arrayList.add(blockPos.south().south().up());
            arrayList.add(blockPos.south().south().up().up());
            arrayList.add(blockPos.north());
            arrayList.add(blockPos.north().up());
            arrayList.add(blockPos.north().up().up());
            arrayList.add(blockPos.north().up().up().up());
            arrayList.add(blockPos.north().north());
            arrayList.add(blockPos.north().north().up());
            arrayList.add(blockPos.north().north().up().up());
        }
        HashSet<BlockPos> hashSet2 = new HashSet<BlockPos>();
        for (BlockPos blockPos4 : arrayList) {
            if (!this.world.getBlockState(blockPos4).getMaterial().isLiquid()) continue;
            this.world.setBlockState(blockPos4, Blocks.COBBLESTONE.getDefaultState(), 2);
            if (!arrayList4.contains(blockPos4)) continue;
            hashSet2.add(blockPos4);
        }
        if (!hashSet2.isEmpty()) {
            bs_class972.a(hashSet2);
            EntityPlayer object2 = this.net_minecraft_entity_player_EntityPlayer_z();
            if (object2 != null) {
                ge_class363.b.sendTo((IMessage)new SendBlocks(hashSet2, true), (EntityPlayerMP)object2);
            }
        }
        arrayList.clear();
        arrayList.add(blockPos.down());
        if (enumFacing2.getAxis() == EnumFacing.Axis.Z) {
            arrayList.add(blockPos.down().west());
            arrayList.add(blockPos.down().east());
        } else {
            arrayList.add(blockPos.down().north());
            arrayList.add(blockPos.down().south());
        }
        for (BlockPos blockPos5 : arrayList) {
            if (!this.world.getBlockState(blockPos5).getBlock().isPassable(this.world, blockPos5)) continue;
            this.world.setBlockState(blockPos5, Blocks.COBBLESTONE.getDefaultState());
        }
        HashSet<BlockPos> object2 = new HashSet();
        Iterator iterator = arrayList4.iterator();
        while (iterator.hasNext()) {
            BlockPos object = (BlockPos)iterator.next();
            Block block = this.world.getBlockState((BlockPos)object).getBlock();
            if (block != Blocks.AIR) continue;
            ((HashSet)object2).add(object);
        }
        if (!((HashSet)object2).isEmpty()) {
            arrayList4.removeAll((Collection<?>)object2);
            bs_class972.b((HashSet<BlockPos>)object2);
            UUID uUID2 = ax_class48.b(uUID);
            if (uUID2 != null && (object = this.world.getPlayerEntityByUUID(uUID2)) != null) {
                ge_class363.b.sendTo((IMessage)new SendBlocks((HashSet<BlockPos>)object2, false), (EntityPlayerMP)object);
            }
        }
        if (arrayList4.isEmpty()) {
            return this.a(bs_class972, uUID);
        }
        Object var14_23 = null;
        List<KoboldEntity> object = bs_class972.c();
        for (int i = 0; i < object.size(); ++i) {
            BlockPos blockPos6;
            if (((KoboldEntity)object.get(i)).getEntityId() != this.getEntityId()) continue;
            if (i == 0) {
                BlockPos blockPos7;
                BlockPos blockPos8 = this.a(arrayList4, -1, bs_class972.f(), bs_class972.b());
                if (blockPos8 != null || (blockPos7 = this.a(arrayList4, 0, bs_class972.f(), bs_class972.b())) != null) break;
                BlockPos blockPos9 = this.a(arrayList4, 1, bs_class972.f(), bs_class972.b());
                break;
            }
            if (i == 1) {
                BlockPos blockPos10;
                BlockPos blockPos11 = this.a(arrayList4, 1, bs_class972.f(), bs_class972.b());
                if (blockPos11 != null || (blockPos10 = this.a(arrayList4, 0, bs_class972.f(), bs_class972.b())) != null) break;
                BlockPos blockPos12 = this.a(arrayList4, -1, bs_class972.f(), bs_class972.b());
                break;
            }
            if (i != 2) continue;
            BlockPos blockPos13 = this.a(arrayList4, 0, bs_class972.f(), bs_class972.b());
            if (blockPos13 != null || (blockPos6 = this.a(arrayList4, 1, bs_class972.f(), bs_class972.b())) != null) break;
            BlockPos blockPos14 = this.a(arrayList4, -1, bs_class972.f(), bs_class972.b());
            break;
        }
        return var14_33;
    }*/

    // TODO rewrite/migrate
    BlockPos a(KoboldTaskInfo var1, UUID var2) {
        HashSet<BlockPos> var3 = var1.getTargetBlocks();
        EnumFacing var4 = var1.getFacing();
        ArrayList<BlockPos> var5 = new ArrayList<>();
        Integer var6 = null;
        if (var3.isEmpty()) {
            return null;
        } else {
            for(BlockPos var8 : var3) {
                switch (var4) {
                    case NORTH:
                        if (var6 == null || var8.getZ() >= var6) {
                            var6 = var8.getZ();
                            var5.add(var8);
                        }
                        break;
                    case SOUTH:
                        if (var6 == null || var8.getZ() <= var6) {
                            var6 = var8.getZ();
                            var5.add(var8);
                        }
                        break;
                    case EAST:
                        if (var6 == null || var8.getX() <= var6) {
                            var6 = var8.getX();
                            var5.add(var8);
                        }
                        break;
                    case WEST:
                        if (var6 == null || var8.getX() >= var6) {
                            var6 = var8.getX();
                            var5.add(var8);
                        }
                }
            }

            ArrayList<BlockPos> var17 = new ArrayList<>();

            for(BlockPos var9 : var5) {
                if ((var4 == EnumFacing.NORTH || var4 == EnumFacing.SOUTH) && var9.getZ() == var6) {
                    var17.add(var9);
                }

                if ((var4 == EnumFacing.EAST || var4 == EnumFacing.WEST) && var9.getX() == var6) {
                    var17.add(var9);
                }
            }

            if (var17.isEmpty()) {
                return null;
            } else {
                ArrayList<BlockPos> var19 = new ArrayList();
                EnumFacing var20 = var1.getFacing();
                BlockPos var10 = var1.getOriginPos();
                BlockPos var21;
                if (var20.getAxis() == EnumFacing.Axis.Z) {
                    var21 = new BlockPos(var10.getX(), var10.getY(), ((BlockPos)var17.get(0)).getZ());
                    if (var20 == EnumFacing.NORTH) {
                        var21 = var21.north();
                    } else {
                        var21 = var21.south();
                    }

                    var19.add(var21.down());
                    var19.add(var21.down().east());
                    var19.add(var21.down().west());
                    var19.add(var21);
                    var19.add(var21.up());
                    var19.add(var21.up().up());
                    var19.add(var21.up().up().up());
                    var19.add(var21.west());
                    var19.add(var21.west().up());
                    var19.add(var21.west().up().up());
                    var19.add(var21.west().up().up().up());
                    var19.add(var21.west().west());
                    var19.add(var21.west().west().up());
                    var19.add(var21.west().west().up().up());
                    var19.add(var21.east());
                    var19.add(var21.east().up());
                    var19.add(var21.east().up().up());
                    var19.add(var21.east().up().up().up());
                    var19.add(var21.east().east());
                    var19.add(var21.east().east().up());
                    var19.add(var21.east().east().up().up());
                } else {
                    var21 = new BlockPos(((BlockPos)var17.get(0)).getX(), var10.getY(), var10.getZ());
                    if (var20 == EnumFacing.EAST) {
                        var21 = var21.east();
                    } else {
                        var21 = var21.west();
                    }

                    var19.add(var21.down());
                    var19.add(var21.down().north());
                    var19.add(var21.down().south());
                    var19.add(var21);
                    var19.add(var21.up());
                    var19.add(var21.up().up());
                    var19.add(var21.up().up().up());
                    var19.add(var21.south());
                    var19.add(var21.south().up());
                    var19.add(var21.south().up().up());
                    var19.add(var21.south().up().up().up());
                    var19.add(var21.south().south());
                    var19.add(var21.south().south().up());
                    var19.add(var21.south().south().up().up());
                    var19.add(var21.north());
                    var19.add(var21.north().up());
                    var19.add(var21.north().up().up());
                    var19.add(var21.north().up().up().up());
                    var19.add(var21.north().north());
                    var19.add(var21.north().north().up());
                    var19.add(var21.north().north().up().up());
                }

                HashSet<BlockPos> var12 = new HashSet<>();

                for(BlockPos var14 : var19) {
                    if (this.world.getBlockState(var14).getMaterial().isLiquid()) {
                        this.world.setBlockState(var14, Blocks.COBBLESTONE.getDefaultState(), 2);
                        if (var17.contains(var14)) {
                            var12.add(var14);
                        }
                    }
                }

                if (!var12.isEmpty()) {
                    var1.addAllBlocks(var12);
                    EntityPlayer var23 = this.getMasterPlayer(); // TODO???
                    if (var23 != null) {
                        PackageHandler.INSTANCE.sendTo(new SendBlocks(var12, true), (EntityPlayerMP)var23);
                    }
                }

                var19.clear();
                var19.add(var21.down());
                if (var20.getAxis() == EnumFacing.Axis.Z) {
                    var19.add(var21.down().west());
                    var19.add(var21.down().east());
                } else {
                    var19.add(var21.down().north());
                    var19.add(var21.down().south());
                }

                for(BlockPos var26 : var19) {
                    if (this.world.getBlockState(var26).getBlock().isPassable(this.world, var26)) {
                        this.world.setBlockState(var26, Blocks.COBBLESTONE.getDefaultState());
                    }
                }

                HashSet<BlockPos> var25 = new HashSet<>();

                for(BlockPos var15 : var17) {
                    Block var16 = this.world.getBlockState(var15).getBlock();
                    if (var16 == Blocks.AIR) {
                        var25.add(var15);
                    }
                }

                if (!var25.isEmpty()) {
                    var17.removeAll(var25);
                    var1.removeAllBlocks(var25);
                    UUID var28 = KoboldManager.getTribeMasterUUID(var2);
                    if (var28 != null) {
                        EntityPlayer var30 = this.world.getPlayerEntityByUUID(var28);
                        if (var30 != null) {
                            PackageHandler.INSTANCE.sendTo(new SendBlocks(var25, false), (EntityPlayerMP)var30);
                        }
                    }
                }

                if (var17.isEmpty()) {
                    return this.a(var1, var2);
                } else {
                    BlockPos var29 = null;
                    List<KoboldEntity> var31 = var1.getAssignedWorkers();

                    for(int var32 = 0; var32 < var31.size(); ++var32) {
                        // TODO 308
                        if ((var31.get(var32)).getEntityId() == this.getEntityId()) {
                            if (var32 == 0) {
                                var29 = this.a(var17, -1, var1.getFacing(), var1.getOriginPos());
                                if (var29 == null) {
                                    var29 = this.a(var17, 0, var1.getFacing(), var1.getOriginPos());
                                    if (var29 == null) {
                                        var29 = this.a(var17, 1, var1.getFacing(), var1.getOriginPos());
                                    }
                                }
                                break;
                            }

                            if (var32 == 1) {
                                var29 = this.a(var17, 1, var1.getFacing(), var1.getOriginPos());
                                if (var29 == null) {
                                    var29 = this.a(var17, 0, var1.getFacing(), var1.getOriginPos());
                                    if (var29 == null) {
                                        var29 = this.a(var17, -1, var1.getFacing(), var1.getOriginPos());
                                    }
                                }
                                break;
                            }

                            if (var32 == 2) {
                                var29 = this.a(var17, 0, var1.getFacing(), var1.getOriginPos());
                                if (var29 == null) {
                                    var29 = this.a(var17, 1, var1.getFacing(), var1.getOriginPos());
                                    if (var29 == null) {
                                        var29 = this.a(var17, -1, var1.getFacing(), var1.getOriginPos());
                                    }
                                }
                                break;
                            }
                        }
                    }

                    return var29;
                }
            }
        }
    }

    @Nullable
    BlockPos a(List<BlockPos> list, int n, EnumFacing enumFacing, BlockPos blockPos) {
        BlockPos blockPos2;
        int n2;
        if (list.isEmpty()) {
            return null;
        }
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        ArrayList<BlockPos> arrayList2 = new ArrayList<BlockPos>();
        ArrayList<BlockPos> arrayList3 = new ArrayList<BlockPos>();
        int n3 = n2 = enumFacing == EnumFacing.SOUTH || enumFacing == EnumFacing.WEST ? -1 : 1;
        if (enumFacing.getAxis() == EnumFacing.Axis.Z) {
            blockPos2 = new BlockPos(blockPos.getX(), blockPos.getY(), list.get(0).getZ());
            arrayList3.add(blockPos2);
            arrayList3.add(blockPos2.up());
            arrayList3.add(blockPos2.up().up());
            arrayList3.add(blockPos2.west());
            arrayList3.add(blockPos2.west().up());
            arrayList3.add(blockPos2.west().up().up());
            arrayList3.add(blockPos2.east());
            arrayList3.add(blockPos2.east().up());
            arrayList3.add(blockPos2.east().up().up());
            if (n == 0) {
                for (BlockPos blockPos3 : arrayList3) {
                    arrayList2.add(blockPos3.east(2));
                    arrayList2.add(blockPos3.east(-2));
                }
                for (BlockPos blockPos3 : list) {
                    if (arrayList2.contains(blockPos3)) continue;
                    arrayList.add(blockPos3);
                }
            } else {
                for (BlockPos blockPos3 : arrayList3) {
                    arrayList2.add(blockPos3.east(n2 * 2 * n));
                }
                for (BlockPos blockPos3 : arrayList2) {
                    if (!list.contains(blockPos3)) continue;
                    arrayList.add(blockPos3);
                }
            }
        }
        if (enumFacing.getAxis() == EnumFacing.Axis.X) {
            blockPos2 = new BlockPos(list.get(0).getX(), blockPos.getY(), blockPos.getZ());
            arrayList3.add(blockPos2);
            arrayList3.add(blockPos2.up());
            arrayList3.add(blockPos2.up().up());
            arrayList3.add(blockPos2.north());
            arrayList3.add(blockPos2.north().up());
            arrayList3.add(blockPos2.north().up().up());
            arrayList3.add(blockPos2.south());
            arrayList3.add(blockPos2.south().up());
            arrayList3.add(blockPos2.south().up().up());
            if (n == 0) {
                for (BlockPos blockPos3 : arrayList3) {
                    arrayList2.add(blockPos3.south(2));
                    arrayList2.add(blockPos3.south(-2));
                }
                for (BlockPos blockPos3 : list) {
                    if (arrayList2.contains(blockPos3)) continue;
                    arrayList.add(blockPos3);
                }
            } else {
                for (BlockPos blockPos3 : arrayList3) {
                    arrayList2.add(blockPos3.south(n2 * 2 * n));
                }
                for (BlockPos blockPos3 : arrayList2) {
                    if (!list.contains(blockPos3)) continue;
                    arrayList.add(blockPos3);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (BlockPos)arrayList.get(this.getRNG().nextInt(arrayList.size()));
    }

    void u(UUID uUID) {
        if (this.b(uUID, false)) {
            return;
        }
        this.void_e();
    }

    void void_e() {
        EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 15.0);
        if (this.hasMaster() && entityPlayer != null && entityPlayer.getDistance(this) < 2.0f && ((String)this.entityDataManager.get(MASTER)).equals(entityPlayer.getPersistentID().toString())) {
            this.getNavigator().clearPath();
            return;
        }
        if (this.ap == null || this.getDistance(this.ap.getX(), this.ap.getY(), this.ap.getZ()) > this.double_n() || this.ab > 100) {
            int n = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(5);
            int n2 = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(5);
            int n3 = WorldUtils.getHeightAt(this.world, this.getPosition().getX() + n, this.getPosition().getZ() + n2);
            this.ap = new BlockPos(this.getPosition().getX() + n, n3, this.getPosition().getZ() + n2);
            this.ab = 0;
        }
        if (Math.sqrt(this.ap.distanceSq(this.getPosition())) > 2.0) {
            this.getNavigator().tryMoveToXYZ(this.ap.getX(), this.ap.getY(), this.ap.getZ(), 0.35f);
            this.applyCustomPathNodeVelocity();
        } else {
            ++this.ab;
        }
    }

    double double_n() {
        return Math.sqrt(800.0);
    }

    boolean b(UUID uUID, boolean bl) {
        if (this.boolean_f()) {
            return false;
        }
        if (this.a(uUID, bl)) {
            this.a0 = 0;
            return true;
        }
        if (--this.a0 >= 0 || !this.ax) {
            return false;
        }
        this.a0 = 300;
        EntityPlayer entityPlayer = this.world.getPlayerEntityByUUID(UUID.fromString((String)this.entityDataManager.get(MASTER)));
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.valueOf((String)this.entityDataManager.get(CURRENT_ACTION));
        if (entityPlayer != null) {
            entityPlayer.sendStatusMessage(new TextComponentString((Object)((Object) eyeAndKoboldColor_.getTextColor()) + this.getGirlName() + "s " + (Object)((Object)TextFormatting.WHITE) + "inventory is full and there are either no chests to put her items in or said chests are full as well"), false);
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    /*
    @Deprecated
    boolean a__(UUID uUID, boolean bl) {
        Object object;
        int n;
        Object object2;
        BlockPos blockPos2;
        HashSet<BlockPos> hashSet = ax_class48.q(uUID);
        if (hashSet == null) {
            return false;
        }
        Vec3i vec3i = null;
        for (BlockPos blockPos2 : hashSet) {
            TileEntityChest tileEntityChest = (TileEntityChest)this.world.getTileEntity(blockPos2);
            object2 = tileEntityChest.getSingleChestHandler();
            n = 0;
            for (int i = 0; i < this.X.getSlots(); ++i) {
                ItemStack itemStack = this.X.getStackInSlot(i);
                if (itemStack.isEmpty()) continue;
                for (int j = 0; j < object2.getSlots(); ++j) {
                    ItemStack itemStack2 = object2.insertItem(j, itemStack, true);
                    if (itemStack2.getCount() == itemStack.getCount()) continue;
                    n = 1;
                    break;
                }
                if (n != 0) break;
            }
            if (n == 0) continue;
            if (vec3i == null) {
                vec3i = blockPos2;
                continue;
            }
            if (!(this.getDistanceSq((BlockPos)vec3i) > this.getDistanceSq(blockPos2))) continue;
            vec3i = blockPos2;
        }
        if (vec3i == null) {
            return false;
        }
        if (this.getDistance(vec3i.getX(), vec3i.getY(), vec3i.getZ()) < 2.0) {
            object = (TileEntityChest)this.world.getTileEntity((BlockPos)vec3i);
            blockPos2 = object.getSingleChestHandler();
            block3: for (int i = 0; i < this.X.getSlots(); ++i) {
                object2 = this.X.getStackInSlot(i);
                if (((ItemStack)object2).isEmpty()) continue;
                for (n = 0; n < blockPos2.getSlots(); ++n) {
                    ItemStack itemStack = blockPos2.insertItem(n, (ItemStack)object2, false);
                    if (itemStack.getCount() <= 0) {
                        this.X.setStackInSlot(i, ItemStack.EMPTY);
                        continue block3;
                    }
                    this.X.setStackInSlot(i, itemStack);
                    object2 = itemStack;
                }
            }
            this.world.playSound(null, (BlockPos)vec3i, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            return true;
        }
        if (Math.abs(vec3i.getY() - this.getPosition().getY()) > 4) {
            if (!bl) return false;
            this.b((BlockPos)vec3i);
            return true;
        } else {
            object = this.getNavigator();
            blockPos2 = this.c((BlockPos)vec3i);
            ((PathNavigate)object).tryMoveToXYZ(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), 0.35f);
            if (((PathNavigate)object).getPath() != null) return true;
            if (!bl) return false;
            this.b((BlockPos)vec3i);
        }
        return true;
    }*/

    @CheckReturnValue
    boolean a(UUID var1, boolean var2) {
        HashSet<BlockPos> var3 = KoboldManager.getTribeChests(var1);
        if (var3 == null) {
            return false;
        } else {
            BlockPos var4 = null;

            for(BlockPos var6 : var3) {
                TileEntityChest var7 = (TileEntityChest)this.world.getTileEntity(var6);
                IItemHandler var8 = var7.getSingleChestHandler();
                boolean var9 = false;

                for(int var10 = 0; var10 < this.X.getSlots(); ++var10) {
                    ItemStack var11 = this.X.getStackInSlot(var10);
                    if (!var11.isEmpty()) {
                        for(int var12 = 0; var12 < var8.getSlots(); ++var12) {
                            ItemStack var13 = var8.insertItem(var12, var11, true);
                            if (var13.getCount() != var11.getCount()) {
                                var9 = true;
                                break;
                            }
                        }

                        if (var9) {
                            break;
                        }
                    }
                }

                if (var9) {
                    if (var4 == null) {
                        var4 = var6;
                    } else if (this.getDistanceSq(var4) > this.getDistanceSq(var6)) {
                        var4 = var6;
                    }
                }
            }

            if (var4 == null) {
                return false;
            } else if (!(this.getDistance((double)var4.getX(), (double)var4.getY(), (double)var4.getZ()) < 2.0)) {
                if (Math.abs(var4.getY() - this.getPosition().getY()) > 4) {
                    if (!var2) {
                        return false;
                    }

                    this.b(var4);
                } else {
                    PathNavigate var15 = this.getNavigator();
                    BlockPos var17 = this.c(var4);
                    var15.tryMoveToXYZ((double)var17.getX(), (double)var17.getY(), (double)var17.getZ(), 0.3499999940395355);
                    if (var15.getPath() == null) {
                        if (!var2) {
                            return false;
                        }

                        this.b(var4);
                    }
                }

                return true;
            } else {
                TileEntityChest var14 = (TileEntityChest)this.world.getTileEntity(var4);
                IItemHandler var16 = var14.getSingleChestHandler();

                for(int var18 = 0; var18 < this.X.getSlots(); ++var18) {
                    ItemStack var19 = this.X.getStackInSlot(var18);
                    if (!var19.isEmpty()) {
                        for(int var20 = 0; var20 < var16.getSlots(); ++var20) {
                            ItemStack var21 = var16.insertItem(var20, var19, false);
                            if (var21.getCount() <= 0) {
                                this.X.setStackInSlot(var18, ItemStack.EMPTY);
                                break;
                            }

                            this.X.setStackInSlot(var18, var21);
                            var19 = var21;
                        }
                    }
                }

                this.world.playSound((EntityPlayer)null, var4, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
    }

    @CheckReturnValue
    boolean c(UUID uUID, KoboldTaskInfo bs_class972) {
        List<KoboldEntity> list = KoboldManager.getTribeMembersList(uUID);
        Collection<KoboldTaskInfo> collection = KoboldManager.getTribeTasks(uUID);
        KoboldEntity ff_class3082 = null;
        Vec3d vec3d = new Vec3d(bs_class972.getOriginPos().getX(), bs_class972.getOriginPos().getY(), bs_class972.getOriginPos().getZ());
        for (KoboldEntity ff_class3083 : list) {
            boolean bl = false;
            for (KoboldTaskInfo bs_class973 : collection) {
                if (!bs_class973.hasWorker(ff_class3083)) continue;
                bl = true;
                break;
            }
            if (bl || ff_class3083.getInteractionPlayerUUID() != null) continue;
            if (ff_class3082 == null) {
                ff_class3082 = ff_class3083;
                continue;
            }
            if (!(ff_class3082.getPositionVector().distanceTo(vec3d) > ff_class3083.getPositionVector().distanceTo(vec3d))) continue;
            ff_class3082 = ff_class3083;
        }
        return this.equals(ff_class3082);
    }

    void a(UUID uUID, KoboldTaskInfo bs_class972, BlockPos blockPos) {
        if (this.ad == null) {
            this.aR = 24;
            this.W = 0;
            this.setCurrentAction(Action.NULL);
            this.entityDataManager.set(IS_ANCHORED, false);
            EntityPlayer entityPlayer = this.getMasterPlayer();
            HashSet<BlockPos> hashSet = bs_class972.getTargetBlocks();
            if (entityPlayer != null && !hashSet.isEmpty()) {
                PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(hashSet, false), (EntityPlayerMP)entityPlayer);
            }
            KoboldManager.removeWorkerTask(uUID, this);
            return;
        }
        switch (this.ad.getMetadata()) {
            case 3: 
            case 5: {
                this.world.setBlockState(blockPos, Blocks.SAPLING.getStateForPlacement(this.world, blockPos, EnumFacing.NORTH, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
                this.world.setBlockState(blockPos.north(), Blocks.SAPLING.getStateForPlacement(this.world, blockPos.north(), EnumFacing.NORTH, blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1, this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
                this.world.setBlockState(blockPos.west(), Blocks.SAPLING.getStateForPlacement(this.world, blockPos.west(), EnumFacing.NORTH, blockPos.getX() + 1, blockPos.getY(), blockPos.getZ(), this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
                this.world.setBlockState(blockPos.north().west(), Blocks.SAPLING.getStateForPlacement(this.world, blockPos.north().west(), EnumFacing.NORTH, blockPos.getX() + 1, blockPos.getY(), blockPos.getZ() + 1, this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
                break;
            }
            default: {
                this.world.setBlockState(blockPos, Blocks.SAPLING.getStateForPlacement(this.world, blockPos, EnumFacing.NORTH, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
            }
        }
        this.aR = 24;
        this.W = 0;
        this.ad = null;
        this.setCurrentAction(Action.NULL);
        this.setAnchored(false);
        EntityPlayer entityPlayer = this.getMasterPlayer();
        HashSet<BlockPos> hashSet = bs_class972.getTargetBlocks();
        if (entityPlayer != null && !hashSet.isEmpty()) {
            PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(hashSet, false), (EntityPlayerMP)entityPlayer);
        }
        KoboldManager.removeWorkerTask(uUID, this);
    }

    void a(UUID uUID, BlockPos blockPos, KoboldTaskInfo bs_class972) {
        if (this.getCurrentAction() != Action.MINE) {
            this.a(blockPos, uUID);
            return;
        }
        --this.W;
        if (this.W > 0) {
            return;
        }
        if (this.W == 0) {
            PackageHandler.INSTANCE.sendToAllAround((IMessage)new ResetController(this.girlID()), this.getTargetNetworkPoint());
        }
        if (this.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
            this.a(uUID, bs_class972, blockPos);
            return;
        }
        --this.aR;
        if (this.aR >= 0) {
            return;
        }
        this.aR = 24;
        this.W = 78;
        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
        EntityPlayer entityPlayer = this.getMasterPlayer();
        for (BlockPos object2 : bs_class972.getTargetBlocks()) {
            if (this.world.getBlockState(object2).getBlock() == Blocks.AIR) {
                hashSet.add(object2);
                continue;
            }
            if (object2.getX() == blockPos.getX() && object2.getZ() == blockPos.getZ()) continue;
            try {
                ItemStack blockPos2 = this.world.getBlockState(object2).getBlock().getItem(this.world, blockPos, this.world.getBlockState(blockPos));
                if (blockPos2.getItem() != Items.AIR) {
                    this.b(blockPos2);
                }
            } catch (IllegalArgumentException i) {
                Main.LOGGER.error("Couldn't get an item out of the block that a kobold just destroyed when falling a tree. As a result, the block wasn't added into the kobolds inventory. If you see this message, pls tell trol about it and send her the following stacktrace. Do you maybe remember what block the kobold just removed? Stacktrace follwing:");
                Main.LOGGER.warn("block in question: " + this.world.getBlockState(object2).getBlock().getTranslationKey());
                Main.LOGGER.error(i.getMessage());
            }
            this.ad = this.net_minecraft_item_ItemStack_a(object2);
            this.world.destroyBlock(object2, false);
            bs_class972.removeBlocks(object2);
            bs_class972.removeAllBlocks(hashSet);
            hashSet.add(object2);
            if (entityPlayer != null) {
                PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(hashSet, false), (EntityPlayerMP)entityPlayer);
            }
            return;
        }
        try {
            ItemStack itemStack = this.world.getBlockState(blockPos).getBlock().getItem(this.world, blockPos, this.world.getBlockState(blockPos));
            if (itemStack.getItem() != Items.AIR) {
                this.b(itemStack);
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            Main.LOGGER.error("Couldn't get an item out of the block that a kobold just destroyed when falling a tree. As a result, the block wasn't added into the kobolds inventory. If you see this message, pls tell trol about it and send her the following stacktrace. Do you maybe remember what block the kobold just removed? Stacktrace follwing:");
            Main.LOGGER.warn("block in question: " + this.world.getBlockState(blockPos).getBlock().getTranslationKey());
            Main.LOGGER.error(illegalArgumentException.getMessage());
        }
        this.ad = this.net_minecraft_item_ItemStack_a(blockPos);
        this.world.destroyBlock(blockPos, false);
        int n = 0;
        for (BlockPos hashSet2 : bs_class972.getTargetBlocks()) {
            if (!(this.world.getBlockState(hashSet2).getBlock() instanceof BlockLog)) continue;
            ++n;
        }
        HashSet<BlockPos> hashSet2 = new HashSet<BlockPos>();
        for (int i = 0; i < n; ++i) {
            hashSet2.add(blockPos.add(0, i, 0));
        }
        HashSet<BlockPos> hashSet3 = new HashSet<BlockPos>();
        for (BlockPos blockPos2 : bs_class972.getTargetBlocks()) {
            if (hashSet2.contains(blockPos2)) continue;
            hashSet3.add(blockPos2);
        }
        if (!hashSet3.isEmpty() && entityPlayer != null) {
            PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(hashSet3, false), (EntityPlayerMP)entityPlayer);
        }
        int n2 = 1;
        while (true) {
            BlockPos blockPos2;
            blockPos2 = blockPos.add(0, n2, 0);
            IBlockState iBlockState = this.world.getBlockState(blockPos2);
            if (this.world.getBlockState(blockPos2).getBlock() instanceof BlockLog) {
                this.world.destroyBlock(blockPos2, false);
                EntityFallingBlock entityFallingBlock = new EntityFallingBlock(this.world, (double)blockPos2.getX() + 0.5, blockPos2.getY(), (double)blockPos2.getZ() + 0.5, iBlockState);
                entityFallingBlock.fallTime = 1;
                this.world.spawnEntity(entityFallingBlock);
            }
            if (!bs_class972.getTargetBlocks().contains(blockPos2)) break;
            ++n2;
        }
    }

    ItemStack net_minecraft_item_ItemStack_a(BlockPos blockPos) {
        ItemStack itemStack;
        try {
            itemStack = this.world.getBlockState(blockPos).getBlock().getItem(this.world, blockPos, this.world.getBlockState(blockPos));
        } catch (IllegalArgumentException illegalArgumentException) {
            Main.LOGGER.error("Couldn't turn a wooden block into an item to get its meta data. " +
                    "As a result the kobold is just gonna plant a oak saplinig instead. " +
                    "If you see this message, pls tell trol about it and send her the following stacktrace. " +
                    "Do you maybe remember what block the kobold just removed? Stacktrace follwing:");
            Main.LOGGER.warn("block in question: " + this.world.getBlockState(blockPos).getBlock().getTranslationKey());
            Main.LOGGER.error(illegalArgumentException.getMessage());
            return new ItemStack(Blocks.SAPLING, 1, 0);
        }
        int n = ItemBlock.getIdFromItem(itemStack.getItem());
        int n2 = itemStack.getItem().getMetadata(itemStack);
        if (n == 17 && n2 == 1) {
            return new ItemStack(Blocks.SAPLING, 1, 1);
        }
        if (n == 17 && n2 == 2) {
            return new ItemStack(Blocks.SAPLING, 1, 2);
        }
        if (n == 17 && n2 == 3) {
            return new ItemStack(Blocks.SAPLING, 1, 3);
        }
        if (n == 162 && n2 == 0) {
            return new ItemStack(Blocks.SAPLING, 1, 4);
        }
        if (n == 162 && n2 == 1) {
            return new ItemStack(Blocks.SAPLING, 1, 5);
        }
        return new ItemStack(Blocks.SAPLING, 1, 0);
    }

    void a(BlockPos blockPos, UUID uUID) {
        Object object;
        Vec3i vec3i = null;
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        if (this.world.getBlockState(blockPos.north().down()).isFullCube() && !this.world.getBlockState(blockPos.north()).isFullBlock()) {
            arrayList.add(blockPos.north());
        }
        if (this.world.getBlockState(blockPos.east().down()).isFullCube() && !this.world.getBlockState(blockPos.east()).isFullBlock()) {
            arrayList.add(blockPos.east());
        }
        if (this.world.getBlockState(blockPos.south().down()).isFullCube() && !this.world.getBlockState(blockPos.south()).isFullBlock()) {
            arrayList.add(blockPos.south());
        }
        if (this.world.getBlockState(blockPos.west().down()).isFullCube() && !this.world.getBlockState(blockPos.west()).isFullBlock()) {
            arrayList.add(blockPos.west());
        }
        for (BlockPos blockPos2 : arrayList) {
            if (vec3i == null) {
                vec3i = blockPos2;
                continue;
            }
            double d = new Vec3d((float)vec3i.getX() + 0.5f, vec3i.getY(), (float)vec3i.getZ() + 0.5f).distanceTo(this.getPositionVector());
            double d2 = new Vec3d((float)blockPos2.getX() + 0.5f, blockPos2.getY(), (float)blockPos2.getZ() + 0.5f).distanceTo(this.getPositionVector());
            if (!(d2 < d)) continue;
            vec3i = blockPos2;
        }
        if (vec3i == null) {
            KoboldManager.removeWorkerTask(uUID, this);
            object = this.getMasterPlayer();
            if (object == null) {
                return;
            }
            ((EntityPlayer)object).sendStatusMessage(new TextComponentString("Your kobolds cannot fall this tree because it starts underground"), true);
            return;
        }
        if (this.getPosition().getDistance(vec3i.getX(), vec3i.getY(), vec3i.getZ()) > 1.0) {
            if (Math.abs(this.getPosition().getY() - vec3i.getY()) > 4) {
                this.b((BlockPos)vec3i);
                return;
            }
            object = this.c((BlockPos)vec3i);
            this.getNavigator().tryMoveToXYZ((double)((Vec3i)object).getX() + 0.5, ((Vec3i)object).getY(), (double)((Vec3i)object).getZ() + 0.5, 0.35);
            this.applyCustomPathNodeVelocity();
            return;
        }
        float f = 0.0f;
        if (((BlockPos)vec3i).subtract(blockPos).equals(new BlockPos(0, 0, -1))) {
            f = 0.0f;
        }
        if (((BlockPos)vec3i).subtract(blockPos).equals(new BlockPos(1, 0, 0))) {
            f = 90.0f;
        }
        if (((BlockPos)vec3i).subtract(blockPos).equals(new BlockPos(0, 0, 1))) {
            f = 180.0f;
        }
        if (((BlockPos)vec3i).subtract(blockPos).equals(new BlockPos(-1, 0, 0))) {
            f = -90.0f;
        }
        this.setTargetPosition(new Vec3d((double)vec3i.getX() + 0.5, vec3i.getY(), (double)vec3i.getZ() + 0.5));
        this.setYawRotation(f);
        this.entityDataManager.set(IS_ANCHORED, true);
        this.entityDataManager.set(at, true);
        this.setCurrentAction(Action.MINE);
        this.world.destroyBlock(((BlockPos)vec3i).up(), false);
    }

    void void_h() {
        if (this.aA) {
            return;
        }
        Optional<UUID> optional = this.entityDataManager.get(aL);
        if (!optional.isPresent()) {
            return;
        }
        this.entityDataManager.set(CURRENT_ACTION, KoboldManager.getTribeColor((UUID)optional.get()).toString());
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() == Action.MATING_PRESS_CUM && (action == Action.MATING_PRESS_SOFT || action == Action.MATING_PRESS_HARD)) {
            return;
        }
        if (this.getCurrentAction() == Action.KOBOLD_ANAL_CUM && (action == Action.KOBOLD_ANAL_SLOW || action == Action.KOBOLD_ANAL_FAST)) {
            return;
        }
        if (this.getCurrentAction() == Action.CUMBLOWJOB && (action == Action.SUCKBLOWJOB || action == Action.THRUSTBLOWJOB)) {
            return;
        }
        if (action == Action.MATING_PRESS_CUM) {
            this.V = 0;
        }
        super.setCurrentAction(action);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        EntityPlayer player;
        super.onDeath(damageSource);
        if (this.world.isRemote) {
            return;
        }
        Optional<UUID> optional = this.entityDataManager.get(aL);
        if (!optional.isPresent()) {
            return;
        }
        UUID uUID = (UUID)optional.get();
        KoboldManager.removeMemberFromTribe(uUID, this);
        if (this.hasMaster() && (player = this.world.getPlayerEntityByUUID(UUID.fromString((String)this.getDataManager().get(MASTER)))) != null) {
            player.sendMessage(new TextComponentString(String.format("%s%s%s has perished %suwu", new Object[]{TextFormatting.RED, this.getGirlName(), TextFormatting.WHITE, TextFormatting.RED})));
        }
    }

    @Override
    protected Action getNextAction(Action action) {
        if (action == Action.SUCKBLOWJOB_BLINK) {
            return Action.THRUSTBLOWJOB;
        }
        if (action == Action.KOBOLD_ANAL_SLOW) {
            return Action.KOBOLD_ANAL_FAST;
        }
        return null;
    }

    @Override
    protected Action getCumAction(Action action) {
        if (action == Action.THRUSTBLOWJOB || action == Action.SUCKBLOWJOB_BLINK) {
            return Action.CUMBLOWJOB;
        }
        if (action == Action.KOBOLD_ANAL_SLOW || action == Action.KOBOLD_ANAL_FAST) {
            return Action.KOBOLD_ANAL_CUM;
        }
        if (action == Action.MATING_PRESS_HARD || action == Action.MATING_PRESS_SOFT) {
            return Action.MATING_PRESS_CUM;
        }
        return null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setFloat("body_size", this.entityDataManager.get(aE).floatValue());
        nbt.setInteger("eyeColorX", ((BlockPos)this.entityDataManager.get(ACTION_TARGET_POS)).getX());
        nbt.setInteger("eyeColorY", ((BlockPos)this.entityDataManager.get(ACTION_TARGET_POS)).getY());
        nbt.setInteger("eyeColorZ", ((BlockPos)this.entityDataManager.get(ACTION_TARGET_POS)).getZ());
        nbt.setString("model", (String)this.entityDataManager.get(APPEARANCE_DNA));
        nbt.setString("name", this.entityDataManager.get(KOBOLD_NAME));
        nbt.setString("master", (String)this.entityDataManager.get(MASTER));
        nbt.setTag("inventory", this.X.serializeNBT());
        nbt.setString("bodyColor", (String)this.entityDataManager.get(CURRENT_ACTION));
        nbt.setBoolean("editedColorManually", this.aA);
        Optional<UUID> optional = this.entityDataManager.get(aL);
        if (optional.isPresent()) {
            nbt.setUniqueId("tribeId", (UUID)optional.get());
            nbt.setBoolean("isLeader", KoboldManager.isTribeLeader((UUID)optional.get(), this));
            nbt.setString("tribeName", this.entityDataManager.get(aU));
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        BlockPos blockPos;
        super.readEntityFromNBT(nbt);
        String string = nbt.getString("model");
        if (!"".equals(string)) {
            this.entityDataManager.set(APPEARANCE_DNA, string);
        }
        if (!BlockPos.ORIGIN.equals(blockPos = new BlockPos(nbt.getInteger("eyeColorX"), nbt.getInteger("eyeColorY"), nbt.getInteger("eyeColorZ")))) {
            this.entityDataManager.set(ACTION_TARGET_POS, blockPos);
        }
        this.entityDataManager.set(aE, nbt.getFloat("body_size"));
        this.entityDataManager.set(KOBOLD_NAME, nbt.getString("name"));
        this.entityDataManager.set(MASTER, nbt.getString("master"));
        this.X.deserializeNBT(nbt.getCompoundTag("inventory"));
        String string2 = nbt.getString("bodyColor");
        if (!string2.isEmpty()) {
            this.entityDataManager.set(CURRENT_ACTION, nbt.getString("bodyColor"));
        }
        this.aA = nbt.getBoolean("editedColorManually");
        //if (uUID != null && !this.isDead) {
        UUID tribeId = nbt.getUniqueId("tribeId");
        if (nbt.hasUniqueId("tribeId") && tribeId != null && !this.isDead) {
            if (tribeId.getLeastSignificantBits() == 0 || tribeId.getMostSignificantBits() == 0) {
                // TODO tribeId return a 00000... UUID when missing... super weird
                return;
            }
            this.entityDataManager.set(aL, Optional.of(tribeId));
            if (!KoboldManager.doesTribeExist(tribeId)) {
                KoboldManager.createTribe(tribeId, EyeAndKoboldColor.valueOf((String)this.entityDataManager.get(CURRENT_ACTION)));
            }
            KoboldManager.addMemberToTribe(tribeId, this);
            if (nbt.getBoolean("isLeader")) {
                KoboldManager.setTribeLeader(tribeId, this);
            }
            this.entityDataManager.set(aU, nbt.getString("tribeName"));
        }
    }

    @Override
    public boolean IsBlockedByCeiling() {
        if (this.isLocallyRegistered()) {
            return false;
        }
        Block block = this.world.getBlockState(this.getPosition().add(0, 1, 0)).getBlock();
        return !block.isPassable(this.world, this.getPosition().add(0, 1, 0));
    }

    boolean boolean_f() {
        for (int i = 0; i < this.X.getSlots(); ++i) {
            if (this.X.getStackInSlot(i).isEmpty()) continue;
            return false;
        }
        return true;
    }

    boolean a(KoboldTaskInfo bs_class972) {
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        for (BlockPos blockPos : bs_class972.getTargetBlocks()) {
            IBlockState iBlockState = this.world.getBlockState(blockPos);
            ItemStack itemStack = iBlockState.getBlock().getItem(this.world, blockPos, iBlockState);
            arrayList.add(itemStack);
        }
        return this.boolean_a((List<ItemStack>)arrayList);
    }

    boolean boolean_a(ItemStack itemStack) {
        return this.a(this.X, itemStack, true, false);
    }

    boolean boolean_a(List<ItemStack> list) {
        ItemStackHandler itemStackHandler = new ItemStackHandler(this.X.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
            itemStackHandler.setStackInSlot(i, this.X.getStackInSlot(i));
        }
        for (ItemStack itemStack : list) {
            if (this.a(itemStackHandler, itemStack, true, false)) continue;
            return false;
        }
        return true;
    }

    boolean b(ItemStack itemStack) {
        return this.a(this.X, itemStack, false, true);
    }

    boolean a(ItemStackHandler itemStackHandler, ItemStack itemStack, boolean bl, boolean bl2) {
        ItemStack itemStack2;
        int n;
        for (n = 0; n < itemStackHandler.getSlots(); ++n) {
            itemStack2 = itemStackHandler.getStackInSlot(n);
            if (itemStack2.getItem() != itemStack.getItem() || itemStack2.getMetadata() != itemStack.getMetadata()) continue;
            int n2 = itemStack2.getMaxStackSize();
            if (n2 > itemStack.getCount() + itemStack2.getCount()) {
                if (!bl) {
                    itemStack2.setCount(itemStack2.getCount() + itemStack.getCount());
                }
                return true;
            }
            int n3 = n2 - itemStack2.getCount();
            itemStack2.setCount(n2);
            itemStack.setCount(itemStack.getCount() - n3);
        }
        for (n = 0; n < itemStackHandler.getSlots(); ++n) {
            itemStack2 = itemStackHandler.getStackInSlot(n);
            if (itemStack2.getItem() != Items.AIR) continue;
            if (!bl) {
                itemStackHandler.setStackInSlot(n, itemStack);
            }
            return true;
        }
        if (bl) {
            return false;
        }
        if (!bl2) {
            return false;
        }
        EntityItem entityItem = new EntityItem(this.world);
        entityItem.setItem(itemStack);
        entityItem.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(entityItem);
        return false;
    }

    void b(SoundEvent soundEvent, float f) {
        float f2 = 0.25f - this.entityDataManager.get(aE).floatValue();
        double d = f2 / 0.25f;
        float f3 = (float) ReferenceAndRotationHelper.LerpDouble((double)0.9f, (double)1.1f, d);
        this.PlaySoundAtPosition(soundEvent, f, f3);
    }

    void b(SoundEvent soundEvent) {
        this.b(soundEvent, 1.0f);
    }

    void a(SoundEvent[] soundEventArray) {
        this.b(soundEventArray, 1.0f);
    }

    void b(SoundEvent[] soundEventArray, float f) {
        this.b(soundEventArray[this.getRNG().nextInt(soundEventArray.length)], f);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        float f = 0.25f - this.getDataManager().get(aE).floatValue();
        GeckoLibCache.getInstance().parser.setValue("size", f);
        block5 : switch (event.getController().getName()) {
            case "eyes": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.kobold.null", true, event);
                    break;
                }
                this.createAnimation("animation.kobold.blink", true, event);
                break;
            }
            case "movement": {
                if (this.getCurrentAction() != Action.NULL) {
                    this.createAnimation("animation.kobold.null", true, event);
                    break;
                }
                if (this.isRiding()) {
                    this.createAnimation("animation.kobold.sit", true, event);
                    break;
                }
                double d = Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ);
                if (!((Boolean)this.entityDataManager.get(IS_ANCHORED)).booleanValue() && d > 0.0) {
                    if (this.onGround && Math.abs(Math.abs(this.prevPosY) - Math.abs(this.posY)) < (double)0.1f) {
                        this.rotationYaw = this.rotationYawHead;
                        double d2 = 1.0 + (double)(f * 2.0f);
                        this.movementController.setAnimationSpeed(d2);
                        if (this.IsBlockedByCeiling()) {
                            this.createAnimation("animation.kobold.crouch_walk", true, event);
                            break;
                        }
                        if (this.entityDataManager.get(aC).booleanValue()) {
                            this.createAnimation("animation.kobold.run_armed", true, event);
                            break;
                        }
                        if (d > (double)0.2f) {
                            this.createAnimation("animation.kobold.run", true, event);
                            break;
                        }
                        this.createAnimation("animation.kobold.walk", true, event);
                        break;
                    }
                    this.createAnimation("animation.kobold.fly", true, event);
                    break;
                }
                if (this.IsBlockedByCeiling()) {
                    this.createAnimation("animation.kobold.crouch_idle", true, event);
                    break;
                }
                this.createAnimation(this.entityDataManager.get(aC) != false ? "animation.kobold.idle_armed" : "animation.kobold.idle", true, event);
                break;
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.kobold.null", true, event);
                        break block5;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.kobold.attack", false, event);
                        break block5;
                    }
                    case RIDE: 
                    case SIT: {
                        this.createAnimation("animation.kobold.sit", true, event);
                        break block5;
                    }
                    case MINE: {
                        this.createAnimation("animation.kobold.fall_tree", true, event);
                        break block5;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.kobold.paymentBackpack", true, event);
                        break block5;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobStart", false, event);
                        break block5;
                    }
                    case SUCKBLOWJOB_BLINK: {
                        String string = this.a4 ? "R" : "L";
                        String string2 = this.aT ? "Switch" : "";
                        this.createAnimation("animation.kobold.blowjobSlow" + string + string2, true, event);
                        break block5;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobFast", true, event);
                        break block5;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobCum", false, event);
                        break block5;
                    }
                    case KOBOLD_ANAL_START: {
                        this.createAnimation("animation.kobold.analStart", false, event);
                        break block5;
                    }
                    case KOBOLD_ANAL_SLOW: {
                        this.createAnimation("animation.kobold.analSoft", true, event);
                        break block5;
                    }
                    case KOBOLD_ANAL_FAST: {
                        this.createAnimation("animation.kobold.analHard", true, event);
                        break block5;
                    }
                    case KOBOLD_ANAL_CUM: {
                        this.createAnimation("animation.kobold.analCum", true, event);
                        break block5;
                    }
                    case SLEEP: {
                        this.createAnimation("animation.kobold.sleep", true, event);
                        break block5;
                    }
                    case MATING_PRESS_START: {
                        this.createAnimation("animation.kobold.mating_press_start", false, event);
                        break block5;
                    }
                    case MATING_PRESS_SOFT: {
                        this.createAnimation("animation.kobold.mating_press_soft", true, event);
                        break block5;
                    }
                    case MATING_PRESS_HARD: {
                        this.createAnimation("animation.kobold.mating_press_hard", true, event);
                        break block5;
                    }
                    case MATING_PRESS_CUM: {
                        this.createAnimation("animation.kobold.mating_press_cum", true, event);
                    }
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    @SideOnly(value=Side.CLIENT)
    public void registerControllers(AnimationData data) {
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        AnimationController.ISoundListener iSoundListener = soundKeyframeEvent -> {
            switch (soundKeyframeEvent.sound) {
                case "attackSound": {
                    this.PlaySound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                    break;
                }
                case "paymentMSG1": {
                    this.a(this.getInteractionPlayerUUID(), "I'd like to use ur services owo");
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "plob": {
                    this.PlaySound(SoundsHandler.MISC_PLOB, new int[0]);
                    break;
                }
                case "blackScreen": {
                    if (!this.isControlledByLocalPlayer()) break;
                    BlackScreenUI.run();
                    break;
                }
                case "paymentDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.U();
                    break;
                }
                case "blowjobStartMSG1": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = VectorMath.rotateByYaw(new Vec3d(0.0, 0.625 - (double)entityPlayerSP.getEyeHeight(), -1.0), this.getYawRotation().floatValue() + 180.0f);
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(vec3d), this.getYawRotation().floatValue() + 180.0f, 0.0f));
                    break;
                }
                case "blowjobStartMSG2": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = VectorMath.rotateByYaw(new Vec3d(0.5, 0.5 - (double)entityPlayerSP.getEyeHeight(), -0.6875), this.getYawRotation().floatValue() + 180.0f);
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(vec3d), this.getYawRotation().floatValue() + 180.0f - 40.0f, 0.0f));
                    break;
                }
                case "lipsound": {
                    if (this.getRNG().nextBoolean()) {
                        this.PlaySound(SoundsHandler.GIRLS_ALLIE_LIPSOUND, 1.5f);
                    } else {
                        this.PlaySound(SoundsHandler.GIRLS_JENNY_LIPSOUND, 1.5f);
                    }
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "touch": {
                    this.PlaySound(SoundsHandler.MISC_TOUCH, new int[0]);
                    break;
                }
                case "blowjobStartDone": {
                    this.setCurrentAction(Action.SUCKBLOWJOB_BLINK);
                    this.aT = false;
                    this.a4 = true;
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "switch": {
                    this.aT = this.getRNG().nextBoolean();
                    this.actionController.clearAnimationCache();
                    break;
                }
                case "endSwitch": {
                    this.aT = false;
                    this.a4 = !this.a4;
                    this.actionController.clearAnimationCache();
                    break;
                }
                case "blowjobFastDone": {
                    if (!this.isControlledByLocalPlayer() || HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.SUCKBLOWJOB_BLINK);
                    break;
                }
                case "cumLoud": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 3.0f);
                    break;
                }
                case "cumQuiet": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 1.5f);
                    break;
                }
                case "analCumDone": 
                case "blowjobCumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                    SexUI.hide();
                    break;
                }
                case "analStartDone": {
                    this.setCurrentAction(Action.KOBOLD_ANAL_SLOW);
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.showUI();
                    break;
                }
                case "analStartCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = VectorMath.rotateByYaw(new Vec3d(0.0, 0.5625 - (double)entityPlayerSP.getEyeHeight(), 0.5625), this.getYawRotation().floatValue() + 180.0f);
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(this.getInteractionPlayerUUID().toString(), this.getTargetPosition().add(vec3d), this.getYawRotation().floatValue(), 0.0f));
                    break;
                }
                case "pounding": {
                    this.PlaySound(SoundsHandler.MISC_POUNDING, new int[0]);
                    break;
                }
                case "analFastRapid": {
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    if (this.getCurrentAction() == Action.KOBOLD_ANAL_FAST) {
                        this.actionController.tickOffset = 0.0;
                    }
                    this.setCurrentAction(Action.KOBOLD_ANAL_FAST);
                    break;
                }
                case "analDone": {
                    if (this.getCurrentAction() != Action.KOBOLD_ANAL_FAST) break;
                    this.setCurrentAction(Action.KOBOLD_ANAL_SLOW);
                    break;
                }
                case "analHard": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.04f);
                    break;
                }
                case "analSoft": {
                    if (!this.isControlledByLocalPlayer()) break;
                    SexUI.addCumPercentage(0.02f);
                    break;
                }
                case "cum": {
                    this.PlaySound(SoundsHandler.MISC_SMALLINSERTS, 2.0f);
                    break;
                }
                case "giggle": {
                    this.a(SoundsHandler.GIRLS_KOBOLD_GIGGLE);
                    break;
                }
                case "moan": {
                    this.a(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "moanMating": {
                    --this.aN;
                    if (this.aN > 0) break;
                    this.aN = 3;
                    this.a(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "analHardMSG1": {
                    --this.aN;
                    if (this.aN > 0) break;
                    this.aN = 4;
                    this.a(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "orgasm": {
                    this.a(SoundsHandler.GIRLS_KOBOLD_ORGASM);
                    break;
                }
                case "breath": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_LIGHTBREATHING, 0.5f);
                    break;
                }
                case "haa": {
                    this.b(SoundsHandler.GIRLS_KOBOLD_HAA, 0.7f);
                    break;
                }
                case "interested": {
                    this.a(SoundsHandler.GIRLS_KOBOLD_INTERESTED);
                    break;
                }
                case "yep": {
                    this.a(SoundsHandler.GIRLS_KOBOLD_YEP);
                    break;
                }
                case "bjmoan": {
                    this.b(SoundsHandler.random(SoundsHandler.GIRLS_KOBOLD_BJMOAN));
                    break;
                }
                case "blowjobStartbreath": {
                    int n = this.getRNG().nextInt(3);
                    this.b(SoundsHandler.GIRLS_KOBOLD_LIGHTBREATHING[n]);
                    break;
                }
                case "matingCam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = new Vec3d(0.0, 0.4375 - (double)entityPlayerSP.eyeHeight, -0.6875);
                    vec3d = VectorMath.rotateByYaw(vec3d, this.getYawRotation().floatValue() + 180.0f);
                    vec3d = vec3d.add(this.getTargetPosition());
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, this.getYawRotation().floatValue() + 180.0f, 10.0f));
                    break;
                }
                case "mating_press_startDone": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.showUI();
                    }
                }
                case "mating_press_hardDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.setCurrentAction(Action.MATING_PRESS_SOFT);
                    break;
                }
                case "mating_press_softReady": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.setCurrentAction(Action.MATING_PRESS_HARD);
                    break;
                }
                case "mating_press_hardReady": {
                    if (this.isControlledByLocalPlayer()) {
                        SexUI.addCumPercentage(0.04f);
                    }
                    if (!this.isControlledByLocalPlayer() || !HandlePlayerMovement.isThrusting) break;
                    this.resetAnimationControllerOffset();
                    break;
                }
                case "mating_cum_cam": {
                    if (!this.isControlledByLocalPlayer()) break;
                    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
                    Vec3d vec3d = new Vec3d(0.0, 1.1875 - (double)entityPlayerSP.eyeHeight, 0.125);
                    vec3d = VectorMath.rotateByYaw(vec3d, this.getYawRotation() + 180.0f);
                    vec3d = vec3d.add(this.getTargetPosition());
                    PackageHandler.INSTANCE.sendToServer((IMessage)new TeleportPlayer(entityPlayerSP.getPersistentID().toString(), vec3d, this.getYawRotation().floatValue() + 180.0f, 70.0f));
                    break;
                }
                case "cumMsg": {
                    this.sendLocalClientMessage("I.. hope I am satisfying you sir");
                    this.b(SoundsHandler.GIRLS_KOBOLD_SAD[this.getRNG().nextInt(1)]);
                    break;
                }
                case "renderEgg": {
                    this.Q = true;
                    this.PlaySound(SoundsHandler.MISC_PLOB, 0.5f);
                    break;
                }
                case "mating_press_cumDone": {
                    if (!this.isControlledByLocalPlayer()) break;
                    this.resetCameraAndPhysics();
                }
            }
        };
        this.movementController.transitionLengthTicks = 10.0;
        this.actionController.registerSoundListener(iSoundListener);
        data.addAnimationController(this.actionController);
        data.addAnimationController(this.movementController);
        data.addAnimationController(this.eyesController);
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        if (n >= this.X.getSlots()) {
            return ItemStack.EMPTY;
        }
        return this.X.getStackInSlot(n);
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        return this.X.extractItem(n, n2, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        return this.X.extractItem(n, this.X.getStackInSlot(n).getCount(), false);
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.X.setStackInSlot(n, itemStack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getField(int n) {
        return n;
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }

    public static class c_inner311 {
        int a = 0;

        @SubscribeEvent
        public void a(LivingDeathEvent livingDeathEvent) {
            if (livingDeathEvent.getEntityLiving() instanceof KoboldEntity) {
                KoboldEntity ff_class3082 = (KoboldEntity)livingDeathEvent.getEntityLiving();
                if (ff_class3082.world.isRemote) {
                    return;
                }
                for (int i = 0; i < ff_class3082.X.getSlots(); ++i) {
                    ItemStack itemStack = ff_class3082.X.getStackInSlot(i);
                    if (itemStack.getItem() == Items.AIR) continue;
                    ff_class3082.dropItem(itemStack.getItem(), itemStack.getCount());
                }
            }
        }

        @SubscribeEvent
        public void b(LivingHurtEvent livingHurtEvent) {
            EntityPlayer entityPlayer;
            Entity entity = livingHurtEvent.getEntity();
            World world = entity.getEntityWorld();
            if (world.isRemote) {
                return;
            }
            if (!(entity instanceof KoboldEntity)) {
                return;
            }
            KoboldEntity ff_class3082 = (KoboldEntity)entity;
            Optional<UUID> optional = ff_class3082.getDataManager().get(aL);
            if (!optional.isPresent()) {
                return;
            }
            Entity entity2 = livingHurtEvent.getSource().getTrueSource();
            if (entity2 == null) {
                return;
            }
            if (!(entity2 instanceof EntityLivingBase)) {
                return;
            }
            if (entity2 instanceof EntityPlayer) {
                entityPlayer = (EntityPlayer)entity2;
                if (entityPlayer.capabilities.isCreativeMode) {
                    return;
                }
                if (entityPlayer.equals(ff_class3082.getMasterPlayer())) {
                    return;
                }
            }
            if ((entityPlayer = ff_class3082.getMasterPlayer()) != null) {
                entityPlayer.sendStatusMessage(new TextComponentString((Object)((Object)TextFormatting.RED) + "Your Tribe is under Attack!"), true);
            }
            KoboldManager.addTribeTarget((UUID)optional.get(), (EntityLivingBase)entity2);
        }

        @SubscribeEvent
        public void a(WorldEvent.Unload unload) {
            for (GirlEntity em_class2582 : GirlEntity.getGirlEntityList()) {
                KoboldEntity ff_class3082;
                Optional<UUID> optional;
                if (!(em_class2582 instanceof KoboldEntity) || !(optional = (ff_class3082 = (KoboldEntity) em_class2582).getDataManager().get(aL)).isPresent() || !KoboldManager.isTribeLeader((UUID) optional.get(), ff_class3082))
                    continue;
                ff_class3082.s((UUID) optional.get());
            }
        }

        @SubscribeEvent
        public void a(LivingHurtEvent livingHurtEvent) {
            if (livingHurtEvent.getSource() != DamageSource.IN_WALL) {
                return;
            }
            Entity entity = livingHurtEvent.getEntity();
            if (entity instanceof KoboldEntity) {
                entity.setPosition(entity.posX, entity.posY + 1.0, entity.posZ);
                livingHurtEvent.setCanceled(true);
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void a(TickEvent.ClientTickEvent clientTickEvent) {
            WorldClient worldClient = Minecraft.getMinecraft().world;
            if (worldClient == null) {
                return;
            }
            if (++this.a % 20 == 0) {
                PackageHandler.INSTANCE.sendToServer((IMessage)new GetTribeUIValues());
            }
        }
    }
}

