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

import com.trolmastercard.sexmod.Main;
import com.trolmastercard.sexmod.Packets.*;
import com.trolmastercard.sexmod.blocks.SexFire;
import com.trolmastercard.sexmod.companion.DoorInteractAIGoal;
import com.trolmastercard.sexmod.companion.fighter.WatchClosestGirlGoal;
import com.trolmastercard.sexmod.events.HandlePlayerMovement;
import com.trolmastercard.sexmod.gender_change.hornypotion.HornyPotion;
import com.trolmastercard.sexmod.girls.Kobold.DragonStaff.DragonStaffItem;
import com.trolmastercard.sexmod.girls.Kobold.KoboldEgg.KoboldEggItem;
import com.trolmastercard.sexmod.girls.base.AbstractNpcOnlyEntity;
import com.trolmastercard.sexmod.girls.base.Action;
import com.trolmastercard.sexmod.girls.base.GirlEntity;
import com.trolmastercard.sexmod.girls.base.PlayerGirl.PlayerGirl;
import com.trolmastercard.sexmod.gui.Menu.FighterUI;
import com.trolmastercard.sexmod.gui.Sex.SexUI;
import com.trolmastercard.sexmod.gui.Sex.BlackScreenUI;
import com.trolmastercard.sexmod.gui.TribeNameScreen;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
    final static public DataParameter<Float> SIZE = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.FLOAT).getSerializer().createKey(122);
    final static public DataParameter<String> KOBOLD_NAME = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.STRING).getSerializer().createKey(123);
    final static public DataParameter<Boolean> aC = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(124);
    final static public DataParameter<Boolean> aZ = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(125);
    final static public DataParameter<String> TRIBE_NAME = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.STRING).getSerializer().createKey(126);
    final static public DataParameter<Boolean> ak = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(127);
    final static public DataParameter<Boolean> at = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.BOOLEAN).getSerializer().createKey(128);
    final static public DataParameter<Optional<UUID>> TRIBE_ID = EntityDataManager.createKey(KoboldEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID).getSerializer().createKey(129);
    final static public int av = 24;
    static public double MAX_HEALTH = 69.0;
    static public List<Vector4d> aY = new ArrayList<Vector4d>();
    ItemStackHandler inventory = new ItemStackHandler(27);
    public String as = null;
    boolean az = false;
    int aP = 0;
    int animationTicks = 0;
    boolean a2 = false;
    int aD = 0;
    int a5 = 0;
    float nearestDistance = Float.MAX_VALUE;
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

    IBlockState blockBelowState = null;
    IBlockState aX = null;
    BlockPos aF = null;
    boolean ao = true;
    Vec3d aS = Vec3d.ZERO;
    BlockPos aM = null;
    BlockPos aI = null;
    int ai = 0;
    int taskTimer = 0;
    int aK = 0;
    int a0 = 0;
    boolean ax = false;
    BlockPos ap = null;
    int ab = 0;
    int aR = 24;
    int cooldownTicks = 0;
    ItemStack ad = null;
    public boolean editedColorManually = false;
    int actionCooldown = -1;
    boolean a4 = true;
    boolean aT = false;
    public boolean isRenderEgg = false;
    int aN = 0;

    public KoboldEntity(World world) {
        super(world);
        this.setSize(0.5f, 0.99f);
    }

    KoboldEntity(World world, UUID tribeId, float size) {
        this(world);
        this.entityDataManager.set(TRIBE_ID, Optional.of(tribeId));
        this.entityDataManager.set(SIZE, size);
    }

    public static KoboldEntity createKobold(World world, UUID uUID) {
        float throwDelay = KoboldEntity.getRandomThrowDelay();
        return KoboldEntity.createKoboldWithSpeed(world, uUID, throwDelay);
    }

    public static KoboldEntity createKoboldWithSpeed(World world, UUID tribeId, float throwDelay) {
        MAX_HEALTH = 10.0 - (double)throwDelay * 25.0;
        return new KoboldEntity(world, tribeId, throwDelay);
    }

    @Override
    protected String generateAppearanceDNA(StringBuilder builder) {
        KoboldEntity.appendPaddedLetter(builder, 8);
        KoboldEntity.appendPaddedLetter(builder, 3);
        KoboldEntity.appendRandomGene(builder);
        KoboldEntity.appendRandomGene(builder);
        KoboldEntity.appendPaddedNumber(builder, 2);
        KoboldEntity.appendPaddedNumber(builder, 2);
        KoboldEntity.appendPaddedNumber(builder, 1);
        KoboldEntity.appendPaddedNumber(builder, 1);
        return builder.toString();
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
    public ArrayList<Integer> getBasePartIdList() {
        ArrayList<Integer> parts = new ArrayList<Integer>();
        parts.add(Math.round(this.entityDataManager.get(SIZE) * 100.0f / 0.25f));
        parts.add(EyeAndKoboldColor.indexOf(EyeAndKoboldColor.safeValueOf(this.entityDataManager.get(CURRENT_ACTION))));
        parts.add(EyeAndKoboldColor.indexOf(EyeAndKoboldColor.safeValueOf(this.entityDataManager.get(ACTION_TARGET_POS))));
        return parts;
    }

    @Override
    public void setCustomPartList(List<Integer> parts) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < parts.size(); ++i) {
            int partId = parts.get(i);
            switch (i) {
                case 0: {
                    this.entityDataManager.set(SIZE, (float) partId / 100.0f * 0.25f);
                    break;
                }
                case 1: {
                    String currentColor = this.entityDataManager.get(CURRENT_ACTION);
                    String newColor = EyeAndKoboldColor.values()[partId].toString();
                    if (!newColor.equals(currentColor)) {
                        this.editedColorManually = true;
                    }
                    this.entityDataManager.set(CURRENT_ACTION, newColor);
                    break;
                }
                case 2: {
                    this.entityDataManager.set(ACTION_TARGET_POS, new BlockPos(EyeAndKoboldColor.values()[partId].getMainColor()));
                    break;
                }
                default: {
                    KoboldEntity.appendPaddedNumberWithFixedValue(builder, partId);
                }
            }
        }
        this.entityDataManager.set(APPEARANCE_DNA, builder.toString());
        KoboldRenderer.clearBoneColors();
    }

    void updateModelCodeDNA() {
        if (this.customPartsData != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < this.customPartsData.size(); ++i) {
                Map.Entry entry = this.customPartsData.get(i);
                int partId = (Integer) ((Map.Entry) entry.getValue()).getValue();
                switch (i) {
                    case 0: {
                        this.entityDataManager.set(SIZE, (float) partId / 100.0f * 0.25f);
                        break;
                    }
                    case 1: {
                        this.entityDataManager.set(CURRENT_ACTION, EyeAndKoboldColor.values()[partId].toString());
                        break;
                    }
                    case 2: {
                        this.entityDataManager.set(ACTION_TARGET_POS, new BlockPos(EyeAndKoboldColor.values()[partId].getMainColor()));
                        break;
                    }
                    default: {
                        appendPaddedNumberWithFixedValue(stringBuilder, partId);
                    }
                }
            }
            this.entityDataManager.set(APPEARANCE_DNA, stringBuilder.toString());
            KoboldRenderer.clearBoneColors();
        }
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
            default:
                return Point2D.ZERO;
        }
    }

    @Override
    public String getGirlName() {
        return this.entityDataManager.get(KOBOLD_NAME);
    }

    @Override
    public float getScaleFactor() {
        return 0.2f - (0.25f - this.entityDataManager.get(SIZE));
    }

    @Override
    public float getEyeHeight() {
        return 0.94f;
    }

    public static float getRandomThrowDelay() {
        return (float)(Math.random() * 0.25);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        EyeAndKoboldColor eyeAndKoboldColor_ = EyeAndKoboldColor.values()[this.getRNG().nextInt(EyeAndKoboldColor.values().length)];
        this.entityDataManager.register(ACTION_TARGET_POS, new BlockPos(eyeAndKoboldColor_.getMainColor()));
        this.entityDataManager.register(CURRENT_ACTION, COLOR.name());
        this.entityDataManager.register(TRIBE_ID, Optional.absent());
        this.entityDataManager.register(SIZE, 0.0f);
        this.entityDataManager.register(KOBOLD_NAME, KoboldNames.values()[this.getRNG().nextInt(KoboldNames.values().length)].toString());
        this.entityDataManager.register(aC, false);
        this.entityDataManager.register(aZ, false);
        this.entityDataManager.register(TRIBE_NAME, "null");
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0);
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (this.getInteractionPlayerUUID() != null) {
            return false;
        }

        ItemStack nameTagStack = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!nameTagStack.getItem().equals(Items.NAME_TAG)) {
            nameTagStack = player.getHeldItem(EnumHand.OFF_HAND);
        }

        if (nameTagStack.getItem().equals(Items.NAME_TAG) && player.getPersistentID().toString().equals(this.entityDataManager.get(MASTER))) {
            this.entityDataManager.set(KOBOLD_NAME, nameTagStack.getDisplayName());
            nameTagStack.shrink(1);
            return true;
        }

        if (this.entityDataManager.get(aC)) {
            return false;
        }
        if (this.getCurrentAction() == Action.SLEEP) {
            return false;
        }

        ItemStack staffStack = player.getHeldItem(EnumHand.MAIN_HAND);
        if (staffStack.getItem() != DragonStaffItem.DRAGON_STAFF) {
            staffStack = player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (!this.hasMaster() && staffStack.getItem() == DragonStaffItem.DRAGON_STAFF) {
            if (!this.world.isRemote) {
                return true;
            }
            Optional<UUID> tribeIdOpt = this.entityDataManager.get(TRIBE_ID);
            if (!tribeIdOpt.isPresent()) {
                return true;
            }
            if (!aY.isEmpty()) {
                return true;
            }
            this.openTribeNameScreen((UUID)tribeIdOpt.get());
            return true;
        }

        if (this.hasMaster() && staffStack.getItem() == DragonStaffItem.DRAGON_STAFF && ((String)this.entityDataManager.get(MASTER)).equals(player.getPersistentID().toString())) {
            player.openGui(Main.instance, 1, this.world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
            return true;
        }
        if (this.world.isRemote) {
            if (this.hasMaster() && ((String)this.entityDataManager.get(MASTER)).equals(player.getPersistentID().toString())) {
                this.playRandomSounds(SoundsHandler.GIRLS_KOBOLD_MASTER);
            }
            this.openInteractionMenu(player);
        } else {
            this.setInteractionPlayerUUID(player.getPersistentID());
            this.getNavigator().clearPath();
            this.setYawRotation((float)(Math.atan2(this.posZ - player.posZ, this.posX - player.posX) * 57.29577951308232 + 90.0));
            this.setTargetPosition(new Vec3d(this.posX, Math.floor(this.posY), this.posZ));
            this.entityDataManager.set(IS_ANCHORED, true);
            this.setCurrentAction(Action.NULL);
        }
        return true;
    }

    @SideOnly(value=Side.CLIENT)
    void openTribeNameScreen(UUID tribeId) {
        Minecraft.getMinecraft().displayGuiScreen(new TribeNameScreen(tribeId));
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
        } else {
            this.setInteractionPlayerUUID((UUID) null);
            this.changeDataParameterFromClient("shouldbeattargetpos", "false");
        }
    }

    @Override
    public void resetCameraAndPhysics() {
        this.isRenderEgg = false;
        super.resetCameraAndPhysics();
    }

    protected void triggerActionSync(boolean flag, UUID uuid) {
        super.triggerActionSync(flag, true, uuid);
        HandlePlayerMovement.setMovementLock(false);
    }

    @Override
    public void doAction(String action, UUID uuid) {
        this.az = true;
        if ("oral".equals(action)) {
            this.changeDataParameterFromClient("animationFollowUp", Action.STARTBLOWJOB.toString());
            this.triggerActionSync(true, uuid);
        }
        if ("anal".equals(action)) {
            this.changeDataParameterFromClient("animationFollowUp", Action.KOBOLD_ANAL_START.toString());
            this.triggerActionSync(true, uuid);
        }
        if ("mating".equals(action)) {
            this.changeDataParameterFromClient("animationFollowUp", Action.MATING_PRESS_START.toString());
            this.triggerActionSync(true, uuid);
        }
    }

    @Override
    public void setDismounted() {
        this.a2 = true;
        this.entityDataManager.set(IS_ANCHORED, false);
    }

    @Override
    protected void clearBoneColors() {
        KoboldRenderer.clearBoneColors();
    }

    boolean isSitting() {
        if (!this.a2) {
            return false;
        }
        ++this.aD;
        this.noClip = false;
        this.setNoGravity(false);
        if (this.aD > 40) {
            this.a2 = false;
            this.aD = 0;
            EntityPlayer player = this.world.getPlayerEntityByUUID(this.getInteractionPlayerUUID());
            this.setYawRotation(player.rotationYaw + 180.0f);
            this.entityDataManager.set(IS_ANCHORED, true);
            player.noClip = true;
            player.setNoGravity(true);
            this.noClip = true;
            this.setNoGravity(true);
            this.getNavigator().clearPath();
            this.U();
            return true;
        }
        this.rotationYaw = this.getYawRotation();
        this.setNoGravity(false);
        Vec3d pos = ReferenceAndRotationHelper.lerpVec3d(this.getPositionVector(), this.getTargetPosition(), 40 - this.aD);
        this.setPosition(pos.x, pos.y, pos.z);
        this.setCurrentAction(Action.NULL);
        Optional<UUID> tribeIdOpt = this.entityDataManager.get(TRIBE_ID);
        if (!tribeIdOpt.isPresent()) {
            return true;
        }
        Collection<KoboldTask> tasks = KoboldManager.getTribeTasks((UUID)tribeIdOpt.get());
        if (tasks == null) {
            return true;
        }
        for (KoboldTask task : tasks) {
            task.removeWorker(this);
        }
        return true;
    }

    void handleActionCooldown(UUID tribeId) {
        if (this.actionCooldown != -1) {
            if (++this.actionCooldown >= 132) {
                this.actionCooldown = -1;
                if (this.getCurrentAction() == Action.MATING_PRESS_CUM) {
                    UUID uuid = this.getInteractionPlayerUUID();
                    if (uuid != null) {
                        EntityPlayer player = this.world.getPlayerEntityByUUID(uuid);
                        if (player != null) {
                            EyeAndKoboldColor color = KoboldManager.getTribeColor(tribeId);
                            ItemStack eggStack = new ItemStack(KoboldEggItem.KOBOLD_EGG, 1, color.getWoolMeta());
                            NBTTagCompound nbt = eggStack.getTagCompound();
                            if (nbt == null) {
                                nbt = new NBTTagCompound();
                            }
                            nbt.setString("tribeID", tribeId.toString());
                            nbt.setString("tribeColor", color.toString());
                            eggStack.setTagCompound(nbt);
                            player.inventory.addItemStackToInventory(eggStack);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateAITasks() {
        //Object object;
        super.updateAITasks();
        this.ax = false;
        Optional<UUID> tribeIdOpt = this.entityDataManager.get(TRIBE_ID);
        if (tribeIdOpt.isPresent()) {
            this.handleActionCooldown((UUID)tribeIdOpt.get());
            KoboldManager.updateLeaderIfDead((UUID)tribeIdOpt.get());
            EntityPlayer master = this.getMasterPlayer();
            if (master != null) {
                KoboldManager.setTribeMaster((UUID)tribeIdOpt.get(), master.getPersistentID());
            }
        }

        if (!this.isSitting()) {
            if (this.getInteractionPlayerUUID() == null) {
                if (!this.entityDataManager.get(aC)) {
                    if (this.getHealth() != this.getMaxHealth() && ++this.a5 >= 100) {
                        this.setHealth(this.getHealth() + 2.0f);
                        this.a5 = 0;
                        PackageHandler.INSTANCE.sendToAllTracking(new SpawnParticle(this.girlID(), EnumParticleTypes.HEART.getParticleName()), this);
                    }
                } else {
                    this.a5 = 0;
                }

                if (!this.entityDataManager.get(IS_ANCHORED)) {
                    this.setNoGravity(false);
                }

                if (tribeIdOpt.isPresent()) {
                    --this.aP;
                    if (this.getCurrentAction() == Action.ATTACK) {
                        this.getNavigator().clearPath();
                        this.rotationYaw = this.getYawRotation();
                        this.rotationYawHead = this.getYawRotation();
                        ++this.animationTicks;

                        if (22 == this.animationTicks) {
                            this.onTickEmpty();
                        }
                        if (32 == this.animationTicks) {
                            HashSet<EntityLivingBase> targets = KoboldManager.getTribeTargets(tribeIdOpt.get());
                            HashSet<EntityLivingBase> toRemove = new HashSet<>();
                            for (EntityLivingBase target : targets) {
                                if (!(target.getDistance(this) > 2.0f)) {
                                    target.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0f);
                                    if (target.isDead) {
                                        toRemove.add(target);
                                    }
                                }
                            }

                            for (EntityLivingBase target : toRemove) {
                                KoboldManager.removeCombatant(tribeIdOpt.get(), target);
                            }
                        }
                        if (84 <= this.animationTicks) {
                            this.setCurrentAction(Action.NULL);
                            this.entityDataManager.set(IS_ANCHORED, false);
                            this.animationTicks = 0;
                        }
                        return;
                    }
                    this.entityDataManager.set(aC, this.handleTribeCombat(tribeIdOpt.get(), false));
                    this.entityDataManager.set(aZ, KoboldManager.isTribeMember(tribeIdOpt.get(), this));
                    this.entityDataManager.set(ak, KoboldManager.isTribeAlerted(tribeIdOpt.get()));
                    this.handleMasterPresence();
                    this.handleModelSync();
                    this.watchClosestGirlGoal.isWatching = this.isIdle();
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.handleTribeState();
        this.handleInteraction();
        this.handleIdleState();
        this.handleSleepState();
        this.updateModelCodeDNA();
    }

    void handleSleepState() {
        if (this.world.isRemote) {
            if (this.world.getTotalWorldTime() - 300L >= aV) {
                if (this.hasMaster()) {
                    if (this.getCurrentAction() == Action.NULL) {
                        if (this.entityDataManager.get(GIRL_HAND_STATES).isEmpty()) {
                            if (!this.entityDataManager.get(ak)) {
                                String string = this.entityDataManager.get(MASTER);
                                EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 10.0);
                                if (entityPlayer == null) {
                                    this.nearestDistance = Float.MAX_VALUE;
                                } else if (entityPlayer.getPersistentID().toString().equals(string)) {
                                    float dist = this.getDistance(entityPlayer);
                                    if (dist < 2.0f && this.nearestDistance > 2.0f) {
                                        this.playSound(SoundsHandler.random(SoundsHandler.GIRLS_KOBOLD_HEYMASTER));
                                        this.sendLocalClientMessage("Hey master!");
                                        aV = this.world.getTotalWorldTime();
                                    }
                                    this.nearestDistance = dist;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void handleIdleState() {
        if (this.world.isRemote) {
            if (this.getCurrentAction() != Action.SLEEP) {
                if (this.entityDataManager.get(ak)) {
                    if (this.hasMaster()) {
                        EntityPlayer player = this.world.getPlayerEntityByUUID(UUID.fromString((String) this.entityDataManager.get(MASTER)));
                        if (player != null) {
                            this.handleKoboldOwner(player);
                        }
                    }
                }
            }
        }
    }

    void handleTribeState() {
        if (!this.entityDataManager.get(aC)) {
            if (!this.hasMaster()) {
                Optional<UUID> tribeIdOpt = this.entityDataManager.get(TRIBE_ID);
                if (tribeIdOpt.isPresent()) {
                    for (EntityPlayer player : this.world.playerEntities) {
                        double dist = player.getPositionVector().distanceTo(this.getPositionVector());
                        double closestDist = dist;
                        if (!this.world.isRemote) {
                            for (KoboldEntity kobold : KoboldManager.getTribeMembersList(tribeIdOpt.get())) {
                                double koboldDist = player.getPositionVector().distanceTo(kobold.getPositionVector());
                                if (koboldDist < closestDist) {
                                    closestDist = koboldDist;
                                }
                            }
                        }
                        if (!(closestDist > 10.0)) {
                            if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == DragonStaffItem.DRAGON_STAFF || player.getHeldItem(EnumHand.OFF_HAND).getItem() == DragonStaffItem.DRAGON_STAFF) {
                                PathNavigate navigator = this.getNavigator();
                                navigator.clearPath();
                                if (this.world.isRemote) {
                                    this.handleKoboldOwner(player);
                                } else if (dist > 2.0) {
                                    BlockPos standPos = this.findStandPos(player.getPosition());
                                    navigator.tryMoveToXYZ(standPos.getX(), standPos.getY(), standPos.getZ(), 0.35f);
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void U() {
        //boolean condition;
        String handState = this.entityDataManager.get(GirlEntity.GIRL_HAND_STATES);
        boolean hasHornyPotion = this.getActivePotionEffect(HornyPotion.HORNY_POTION) != null;
        boolean isMasterNear = false;
        if (this.hasMaster()) {
            isMasterNear = this.entityDataManager.get(MASTER).equals(this.getInteractionPlayerUUID().toString());
        }

        boolean condition = !hasHornyPotion && !isMasterNear;
        if (handState.equals(Action.STARTBLOWJOB.toString())) {
            if (!condition || this.getCurrentAction() == Action.PAYMENT) {
                this.setCurrentAction(Action.STARTBLOWJOB);
            } else {
                this.setCurrentAction(Action.PAYMENT);
            }
        }

        if (handState.equals(Action.KOBOLD_ANAL_START.toString())) {
            if (!condition || this.getCurrentAction() == Action.PAYMENT) {
                this.setCurrentAction(Action.KOBOLD_ANAL_START);
            } else {
                this.setCurrentAction(Action.PAYMENT);
            }
        }
        if (handState.equals(Action.MATING_PRESS_START.toString())) {
            this.setCurrentAction(Action.MATING_PRESS_START);
        }
    }

    void handleInteraction() {
        if (this.world.isRemote) {
            UUID uuid = this.getInteractionPlayerUUID();
            if (uuid != null) {
                if (this.entityDataManager.get(IS_ANCHORED)) {
                    if (this.getCurrentAction() == Action.NULL) {
                        EntityPlayer player = this.world.getPlayerEntityByUUID(uuid);
                        if (player != null) {
                            this.handleKoboldOwner(player);
                        }
                    }
                }
            }
        }
    }

    void handleKoboldOwner(EntityPlayer player) {
        PlayerGirl playerGirl = PlayerGirl.getUUIDHashtable(player.getPersistentID());
        Vec3d headPos = new Vec3d(player.posX, player.posY + (double)(playerGirl == null ? player.eyeHeight : playerGirl.getEyeHeight()), player.posZ);
        Vec3d EyePos = new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        double dist = EyePos.distanceTo(headPos);
        double heightDiff = headPos.y - EyePos.y;
        this.rotationPitch = (float)(-(Math.sin(heightDiff / dist) * 57.29577951308232));
    }

    void onTickEmpty() {
    }

    @CheckReturnValue
    boolean isIdle() {
        if (this.getCurrentAction() != Action.NULL || Math.abs(this.motionX) + Math.abs(this.motionZ) > 0.01) {
            return false;
        } else {
            return !this.IsBlockedByCeiling();
        }
    }

    void handleMasterPresence() {
        Optional<UUID> tribeIdOpt = this.entityDataManager.get(TRIBE_ID);
        if (tribeIdOpt.isPresent()) {
            UUID uUID = tribeIdOpt.get();
            if (!this.entityDataManager.get(aC) && KoboldManager.isTribeAlerted(uUID)) {
                if (this.hasMaster()) {
                    EntityPlayer player = this.getMasterPlayer();
                    if (player != null) {
                        for (KoboldTask task : KoboldManager.getTribeTasks(uUID)) {
                            if (task.hasWorker(this)) {
                                task.removeWorker(this);
                                this.setCurrentAction(Action.NULL);
                                this.entityDataManager.set(IS_ANCHORED, false);
                            }
                        }
                        this.noClip = false;
                        this.setNoGravity(false);
                        PathNavigate navigator = this.getNavigator();
                        double dist = this.getPositionVector().distanceTo(player.getPositionVector());
                        if (dist > 2.0) {
                            navigator.tryMoveToEntityLiving(player, this.getKickDistance(player, dist));
                            this.tickPathVelocity();
                            if (dist > 15.0) {
                                this.handlePlayerDismount(player);
                            }
                        }
                    }
                }
            } else if (KoboldManager.isTribeMember(uUID, this)) {
                this.handleTribeRequest(uUID);
            } else {
                this.handleTribeJoin(uUID);
            }
        }
    }

    protected double getKickDistance(EntityPlayer player, double dist) {
        double kickDistance = player.isSprinting() ? 0.7 : 0.35;
        double extra = Math.floor(dist / 5.0) * 0.3;
        kickDistance += extra;
        if (this.isInWater()) {
            kickDistance *= 60.0;
        }

        return kickDistance;
    }

    void teleportToHome(UUID uUID) {
        BlockPos homePos = KoboldManager.getTribeHomePos(uUID);
        if (homePos != null) {
            if (this.aX != null) {
                this.world.setBlockState(homePos, this.aX);
            }

            if (this.blockBelowState != null) {
                this.world.setBlockState(homePos.add(0, -1, 0), this.blockBelowState);
            }
        } else {
            return;
        }
    }

    void handleTribeRequest(UUID uUID) {

        if (!this.isTribeTaskDone(uUID)) {
            if (!this.hasMaster() && KoboldManager.hasAssignedMaster(uUID)) {
                this.getNavigator().clearPath();
                this.aM = null;
            } else {
                TribeState newState = this.getTribeStateForTime();
                TribeState currentState = KoboldManager.getTribeState(uUID);

                if (currentState != newState) {
                    KoboldManager.setTribeState(uUID, newState);
                    switch (newState) {
                        case REST: {
                            this.handleTaskAssign(uUID);
                            KoboldManager.setTribeHomePos(uUID, (BlockPos) null);
                            this.sendGirlChatMessage("okay resting time owo");
                            break;
                        }
                        case ACTIVE: {
                            this.teleportToHome(uUID);
                            this.handleMemberSync(uUID);
                        }
                    }
                }

                switch (newState) {
                    case ACTIVE: {
                        this.aF = null;
                        this.handleHomeRelease(uUID);
                        break;
                    }
                    case REST: {
                        this.handleTaskRequest(uUID);
                    }
                }
            }
        }
    }

    void handleTaskAssign(UUID uUID) {
        Collection<KoboldTask> tasks = KoboldManager.getTribeTasks(uUID);
        if (tasks != null) {
            for (KoboldTask task : tasks) {
                task.resetAllWorkers();
            }
        }
    }

    void handleMemberSync(UUID uUID) {
        if (this.hasMaster()) {
            List<KoboldEntity> members = KoboldManager.getTribeMembersList(uUID);
            for (KoboldEntity kobold : members) {
                KoboldManager.removeBedForKobold(kobold);
                if (kobold.getInteractionPlayerUUID() == null) {
                    kobold.noClip = false;
                    kobold.setNoGravity(false);
                    kobold.getDataManager().set(IS_ANCHORED, false);
                    kobold.setCurrentAction(Action.NULL);
                }
            }
        }
    }

    void handleTaskRequest(UUID uUID) {
        Collection<KoboldTask> tasks = KoboldManager.getTribeTasks(uUID);
        if (tasks != null) {
            for (KoboldTask task : tasks) {
                task.removeWorker(this);
            }
        }
        if (this.hasMaster()) {
            this.handleBedRequest(uUID);
        } else {
            this.handleHomeRequest(uUID);
        }
    }

    void handleBedRequest(UUID uUID) {
        BlockPos[] bedPositions = KoboldManager.getBedForKobold(this);
        if (bedPositions != null) {
            Vec3d headVec = new Vec3d((float) bedPositions[0].getX() + 0.5f, (double) bedPositions[0].getY() + 0.5625, (float) bedPositions[0].getZ() + 0.5f);
            Vec3d footVec = new Vec3d((float) bedPositions[1].getX() + 0.5f, (double) bedPositions[1].getY() + 0.5625, (float) bedPositions[1].getZ() + 0.5f);
            boolean isVertical = headVec.subtract((Vec3d) footVec).x == 0.0;
            Vec3d midVec = ReferenceAndRotationHelper.LerpVec3d(headVec, footVec, 0.5);
            this.entityDataManager.set(IS_ANCHORED, true);
            this.setTargetPosition(midVec);
            this.setYawRotation(isVertical ? 0.0f : 90.0f);
            this.noClip = true;
            this.setNoGravity(true);
        } else {
            HashSet<BlockPos> beds = KoboldManager.getTribeBeds(uUID);
            BlockPos chosenBed = null;
            if (beds != null) {
                for (BlockPos bed : beds) {
                    IBlockState state = this.world.getBlockState(bed);
                    boolean occupied = false;

                    for (Map.Entry Property : state.getProperties().entrySet()) {
                        if (Property.getKey() instanceof PropertyBool) {
                            occupied = (Boolean) Property.getValue();
                            break;
                        }
                    }
                    if (!occupied && !KoboldManager.isBedAssigned(bed)) {
                        if (chosenBed == null) {
                            chosenBed = bed;
                        } else if (this.getDistanceSq(chosenBed) > this.getDistanceSq(bed)) {
                            chosenBed = bed;
                        }
                    }
                }
                if (chosenBed != null) {
                    if (chosenBed.getDistance((int) this.posX, (int) this.posY, (int) this.posZ) > 2.0) {
                        if (Math.abs(chosenBed.subtract(this.getPosition()).getY()) > 4) {
                            this.syncTribeBlocks(chosenBed.add(0, 1, 0));
                        } else {
                            BlockPos standPos = this.findStandPos(chosenBed);
                            this.getNavigator().tryMoveToXYZ(standPos.getX(), standPos.getY(), standPos.getZ(), 0.35f);
                            if (this.getNavigator().getPath() == null) {
                                this.syncTribeBlocks(chosenBed.add(0, 1, 0));
                            }
                        }
                    } else {
                        KoboldManager.assignBedToKobold(this, chosenBed);
                        this.setCurrentAction(Action.SLEEP);
                    }
                }
            }
        }
    }

    void handleHomeRequest(UUID uUID) {
        BlockPos homePos = KoboldManager.getTribeHomePos(uUID);
        if (homePos == null && KoboldManager.isTribeMember(uUID, this)) {
            BlockPos pos = this.getPosition().add(1, 0, 0);
            this.blockBelowState = this.world.getBlockState(pos.add(0, -1, 0));
            this.aX = this.world.getBlockState(pos);
            this.world.setBlockState(pos.add(0, -1, 0), Blocks.NETHERRACK.getDefaultState());
            this.world.setBlockState(pos, SexFire.FIRE.getDefaultState());
            KoboldManager.setTribeHomePos(uUID, pos);
        }
        if (homePos != null) {
            if (this.aF == null) {
                this.aF = homePos.add(
                        (this.getRNG().nextBoolean() ? 1 : -1) * (this.getRNG().nextInt(2) + 1),
                        0,
                        (this.getRNG().nextBoolean() ? 1 : -1) * (this.getRNG().nextInt(2) + 1)
                );
            }
            this.getNavigator().tryMoveToXYZ(this.aF.getX(), this.aF.getY(), this.aF.getZ(), 0.35f);
            this.tickPathVelocity();
        }
    }

    void handleHomeRelease(UUID uUID) {
        if (this.hasMaster()) {
            KoboldManager.setTribeHomePos(uUID, (BlockPos) null);
            this.handleTaskFollow(uUID);
        } else {
            Collection<KoboldTask> tasks = KoboldManager.getTribeTasks(uUID);
            if (tasks != null) {
                if (this.ao) {
                    this.aM = null;
                    this.handleTribeTasks(uUID, tasks);
                } else {
                    this.handleTribeTasksInit(uUID, tasks);
                }
            }
        }
    }

    void handleTribeTasks(UUID uUID, Collection<KoboldTask> tasks) {
        if (tasks.isEmpty()) {
            this.ao = false;
            this.checkTribeHome(uUID);
            this.sendGirlChatMessage("Lets go somewhere else");
        }
    }

    void handleTribeTasksInit(UUID uUID, Collection<KoboldTask> tasks) {
        BlockPos homePos = KoboldManager.getTribeHomePos(uUID);
        if (homePos == null) {
            this.checkTribeHome(uUID);
        } else {
            if (this.ticksExisted % 40 == 0) {
                if (this.aS.equals(this.getPositionVector())) {
                    this.checkTribeHome(uUID);
                    this.aM = null;
                }
                this.aS = this.getPositionVector();
            }

            if (this.aM == null || this.aM.getDistance((int) this.posX, (int) this.posY, (int) this.posZ) < 4.0) {
                this.aM = this.getTribeHomePos(uUID);
            }

            this.getNavigator().tryMoveToXYZ(this.aM.getX(), this.aM.getY(), this.aM.getZ(), 0.35f);
            this.tickPathVelocity();
            if (!(Math.sqrt(this.getPosition().distanceSq(homePos)) > 5.0)) {
                this.ao = true;
                this.sendGirlChatMessage("Time to work bitches!");
                int memberCount = KoboldManager.getTribeMemberCount(uUID);

                for (int i = 1; i < memberCount; ++i) {
                    this.findConnectedLogs(uUID, tasks);
                }
                KoboldManager.setTribeHomePos(uUID, null);
            }
        }
    }

    protected void handlePlayerDismount(EntityPlayer player) {
        BlockPos teleportPos;
        int attempts = 0;

        do {
            teleportPos = player.getPosition().add(
                    ReferenceAndRotationHelper.RANDOM.nextInt(10),
                    0,
                    ReferenceAndRotationHelper.RANDOM.nextInt(10));

        } while (++attempts < 20 && !this.attemptTeleport(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ()));

        if (attempts == 20) {
            this.setPosition(player.posX, player.posY, player.posZ);
        }

        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }

    BlockPos getTribeHomePos(UUID uUID) {
        BlockPos homePos = KoboldManager.getTribeHomePos(uUID);
        return homePos == null ? BlockPos.ORIGIN : this.findStandPos(homePos);
    }

    BlockPos findStandPos(BlockPos pos) {
        BlockPos standPos = this.getPosition();
        BlockPos delta = pos.subtract(standPos);
        if (Math.abs(delta.getX()) + Math.abs(delta.getZ()) < 20) {
            return pos;
        }

        double minAxis = Math.min(Math.abs(delta.getX()), Math.abs(delta.getZ()));
        double maxAxis = Math.max(Math.abs(delta.getX()), Math.abs(delta.getZ()));
        double ratio = minAxis / (maxAxis + minAxis);

        int xOffset = (int)((double)((delta.getX() > 0 ? 1 : -1) * 20) * (minAxis == (double)Math.abs(delta.getX()) ? ratio : 1.0 - ratio));
        int zOffset = (int)((double)((delta.getZ() > 0 ? 1 : -1) * 20) * (minAxis == (double)Math.abs(delta.getZ()) ? ratio : 1.0 - ratio));
        BlockPos candidate = this.getPosition().add(xOffset, 0, zOffset);
        candidate = new BlockPos(candidate.getX(), WorldUtils.getHeightAt(this.world, candidate.getX(), candidate.getZ()) + 1, candidate.getZ());
        return candidate;
    }

    void checkTribeHome(UUID uUID) {
        BlockPos homePos;
        int attempts = 0;

        do {
            homePos = this.getPosition();
            homePos = homePos.add((50 + this.getRNG().nextInt(50)) * (this.getRNG().nextBoolean() ? 1 : -1), 0, (50 + this.getRNG().nextInt(50)) * (this.getRNG().nextBoolean() ? 1 : -1));
        } while (((homePos = new BlockPos(homePos.getX(), WorldUtils.getHeightAt(this.world, homePos.getX(), homePos.getZ()), homePos.getZ())).getY() <= 0 || !this.getNavigator().canEntityStandOnPos(homePos)) && ++attempts < 100);

        KoboldManager.setTribeHomePos(uUID, homePos);
    }

    void findConnectedLogs(UUID tribeId, Collection<KoboldTask> tasks) {
        List<BlockPos> logs = this.findBlocksInRadius(this.getPosition(), BlockLog.class, 30, 4, null);
        BlockPos connectedLog = null;

        for (BlockPos log : logs) {
            Block blockBelow = this.world.getBlockState(log.down()).getBlock();
            if (!(blockBelow instanceof BlockLog) && blockBelow != Blocks.AIR) {
                boolean connected = false;
                for (KoboldTask task : tasks) {
                    if (task.containsBlock(log)) {
                        connected = true;
                        break;
                    }
                }
                if (!connected) {
                    connectedLog = log;
                    break;
                }
            }
        }
        if (connectedLog != null) {
            KoboldTask.findConnectedBlocks(this.world, connectedLog, tribeId);
            this.sendGirlChatMessage("Someone, go fall this tree!");
        }
    }

    @CheckReturnValue
    TribeState getTribeStateForTime() {
        long time = this.world.getWorldTime();
        return time < 12000L ? TribeState.ACTIVE : TribeState.REST;
    }

    // nottodo / dup clash with 'List<...> GirlEntity::d()'
    //  DONE rename to d___...

    //d______JustUseAiToDeobfuscate

    @CheckReturnValue
    boolean isTribeTaskDone(UUID uUID) {
        return this.handleTribeCombat(uUID, true);
    }

    @CheckReturnValue
    boolean handleTribeCombat(UUID tribeId, boolean isLeader) {
        //Optional<UUID> optional;
        HashSet<EntityLivingBase> targets = KoboldManager.getTribeTargets(tribeId);
        KoboldEntity leader = KoboldManager.getTribeLeader(tribeId);
        if (leader == null) {
            return false;
        }

        for (KoboldEntity kobold : this.world.getEntitiesWithinAABB(KoboldEntity.class, new AxisAlignedBB(leader.posX - 30.0, leader.posY - 30.0, leader.posZ - 30.0, leader.posX + 30.0, leader.posY + 30.0, leader.posZ + 30.0))) {
            if (this.canEntityBeSeen(kobold) && (!kobold.hasMaster() || !this.hasMaster())) {
                Optional<UUID> tribeIdOpt = kobold.getDataManager().get(TRIBE_ID);
                if (!tribeIdOpt.isPresent()) {
                    targets.add(kobold);
                } else if (!tribeIdOpt.get().equals(tribeId)) {
                    targets.add(kobold);
                }
            }
        }

        EntityLivingBase closestTarget = null;
        ArrayList<EntityLivingBase> deadTargets = new ArrayList<EntityLivingBase>();

        for (EntityLivingBase target : targets) {
            if (target.isDead) {
                deadTargets.add(target);
            } else if (!(leader.getDistance(target) > 30.0f) && (closestTarget == null || this.getDistance(closestTarget) > this.getDistance(target))) {
                closestTarget = target;
            }
        }

        for (EntityLivingBase target : deadTargets) {
            KoboldManager.removeCombatant(tribeId, target);
        }

        if (closestTarget == null) {
            return false;
        }
        if (!isLeader) {
            return true;
        }

        if (this.getCurrentAction() != Action.ATTACK) {
            this.entityDataManager.set(IS_ANCHORED, false);
            this.setCurrentAction(Action.NULL);
        }

        BlockPos standPos = this.findStandPos((closestTarget).getPosition());
        this.getNavigator().tryMoveToXYZ(standPos.getX(), standPos.getY(), standPos.getZ(), 0.7);

        this.tickPathVelocity();
        if (this.getDistance(closestTarget) > 1.5f || this.aP > 0) {
            return true;
        }

        float yaw = (float)(Math.atan2(this.posZ - closestTarget.posZ, this.posX - closestTarget.posX) * 57.29577951308232 + 90.0);
        this.setYawRotation(yaw);
        this.setCurrentAction(Action.ATTACK);
        this.aP = 84;
        return true;
    }

    void handleTribeJoin(UUID tribeId) {
        if (this.isTribeTaskDone(tribeId)) {
            return;
        }
        TribeState state = KoboldManager.getTribeState(tribeId);
        switch (state) {
            case REST: {
                this.handleTaskRequest(tribeId);
                break;
            }
            case ACTIVE: {
                this.aF = null;
                this.handleHomeTeleport(tribeId); // void
            }
        }
    }

    void handleHomeTeleport(UUID uUID) {
        BlockPos pos = KoboldManager.getTribeHomePos(uUID);
        if (pos == null) {
            this.aM = null;
            this.handleTaskFollow(uUID);
        } else {
            KoboldEntity leader = KoboldManager.getTribeLeader(uUID);
            if (KoboldManager.hasAssignedMaster(uUID)) {
                this.getNavigator().clearPath();
                this.aM = null;
                //return;
            } else if (leader == null) {
                System.out.println("leader of tribe " + uUID + " is null");
            } else {
                if (leader.getDistance(this) > 20.0f) {
                    this.setPosition(leader.posX, leader.posY, leader.posZ);
                    this.aM = null;
                }

                if (this.ticksExisted % 40 == 0) {
                    if (this.aS.equals(this.getPositionVector())) {
                        this.aM = this.getTribeHomePos(uUID);
                    }
                    this.aS = this.getPositionVector();
                }

                if (this.aM == null || this.aM.getDistance((int) this.posX, (int) this.posY, (int) this.posZ) < 4.0) {
                    this.aM = this.getTribeHomePos(uUID);
                }

                this.getNavigator().tryMoveToXYZ(this.aM.getX(), this.aM.getY(), this.aM.getZ(), 0.35f);
                this.tickPathVelocity();
            }
        }
    }

    void handleTaskFollow(UUID tribeId) {
        if (this.getInteractionPlayerUUID() == null) {
            Collection<KoboldTask> tasks = KoboldManager.getTribeTasks(tribeId);
            if (tasks != null) {
                KoboldTask assignedTask = null;

                for (KoboldTask task : tasks) {
                    if (!task.hasWorker(this)) continue;
                    assignedTask = task;
                    break;
                }

                if (assignedTask == null) {
                    for (KoboldTask task : tasks) {
                        if (!this.hasMaster() || this.assignTaskToKobold(tribeId, task)) {
                            if (!this.canAssignTask(task)) {
                                this.ax = true;
                            }
                            else if (task.addWorker(this)) {
                                assignedTask = task;
                                this.aI = null;
                                if (task.getTaskType() == KoboldTask.KoboldTasks.FALL_TREE) {
                                    this.sendGirlChatMessage("Ima fall this tree owo");
                                } else {
                                    this.sendGirlChatMessage("Ima go mine uwu");
                                    this.syncTribeBlocks(task.getOriginPos());
                                    this.world.setBlockState(task.getOriginPos(), Blocks.AIR.getDefaultState());
                                }
                                break;
                            }
                        }
                    }
                }

                if (assignedTask == null) {
                    this.handleNearbyPlayerTick(tribeId);
                } else {
                    if (assignedTask.getTaskType() == KoboldTask.KoboldTasks.FALL_TREE) {
                        this.startMiningTask(tribeId, assignedTask.getOriginPos(), assignedTask);
                    }
                    if (assignedTask.getTaskType() == KoboldTask.KoboldTasks.MINE) {
                        this.handleTribeTasks(tribeId, assignedTask);
                    }
                }
            }
        }
    }

    void syncTribeBlocks(BlockPos blockPos) {
        PackageHandler.INSTANCE.sendToAllTracking(new SpawnParticle(this.girlID(), EnumParticleTypes.PORTAL.getParticleName(), 30), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 30.0));
        this.setPosition(0.5f + (float)blockPos.getX(), blockPos.getY(), 0.5f + (float)blockPos.getZ());
        PackageHandler.INSTANCE.sendToAllTracking(new SpawnParticle(this.girlID(), EnumParticleTypes.PORTAL.getParticleName(), 30), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 30.0));
    }

    void handleTribeTasks(UUID tribeId, KoboldTask task) {
        if (this.getCurrentAction() != Action.MINE) {
            this.handleTaskNavigation(tribeId, task);
        } else {
            --this.taskTimer;
            --this.ai;
            if (this.ai == 0) {
                //Object object;
                IBlockState fallingState = this.world.getBlockState(this.aI.up());
                if (!(fallingState.getBlock() instanceof BlockFalling)) {
                    task.removeBlocks(this.aI);
                    EntityPlayer master = this.getMasterPlayer();
                    if (master != null) {
                        PackageHandler.INSTANCE.sendTo(new SendBlocks(this.aI, false), (EntityPlayerMP) master);
                    }
                }
                IBlockState blockState = this.world.getBlockState(this.aI);
                this.canExtractItem(new ItemStack(blockState.getBlock().getItemDropped(blockState, this.getRNG(), 0), 1, blockState.getBlock().damageDropped(blockState)));
                this.world.destroyBlock(this.aI, false);
            }
            if (this.taskTimer <= 0) {
                this.taskTimer = 100;
                this.ai = 24;
                this.setCurrentAction(Action.NULL);
            }
        }
    }

    void handleTaskNavigation(UUID uUID, KoboldTask task) {
        PathNavigate navigator = this.getNavigator();

        if (this.aI != null && task.getTargetBlocks().contains(this.aI)) {
            IBlockState blockState = this.world.getBlockState(this.aI);
            if (!this.canInsertItem(new ItemStack(blockState.getBlock().getItemDropped(blockState, ReferenceAndRotationHelper.RANDOM, 0)))) {
                this.ax = true;
                this.canStoreInventory(uUID, true);
            } else if (this.motionX != 0.0 || this.motionZ != 0.0 || !this.onGround || this.getDistance(this.aI.getX(), this.aI.getY(), this.aI.getZ()) > 3.0 || ++this.aK < 10) {
                BlockPos pos = this.aI.add(task.getFacing().getOpposite().getDirectionVec());
                navigator.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 0.35f);
            } else {
                navigator.clearPath();
                this.aK = 0;
                this.setCurrentAction(Action.MINE);
                this.rotationYaw = this.rotationYawHead = (float) (Math.atan2(this.posZ - (double) this.aI.getZ(), this.posX - (double) this.aI.getX()) * 57.29577951308232 + 90.0);
                this.entityDataManager.set(at, false);
            }

        } else {
            BlockPos blockPos;
            this.aI = this.executeMiningTask(task, uUID);
            if (this.aI == null) {
                boolean noTargets = task.getTargetBlocks().isEmpty();
                HashSet<BlockPos> blocks = KoboldManager.removeTaskAndGetBlocks(uUID, task);
                UUID tribeId = KoboldManager.getTribeMasterUUID(uUID);
                if (tribeId != null) {
                    EntityPlayer player = this.world.getPlayerEntityByUUID(tribeId);
                    if (player != null) {
                        if (!noTargets) {
                            player.sendMessage(new TextComponentString(String.format("<%s> It's impossible to mine here...", this.getGirlName())));
                        }
                        PackageHandler.INSTANCE.sendTo(new SendBlocks(blocks, false), (EntityPlayerMP) player);
                        //return;
                    }
                }
            } else {
                if (Math.abs(this.getPosition().getY() - task.getOriginPos().getY()) > 3) {
                    blockPos = task.getOriginPos().add(task.getFacing().getOpposite().getDirectionVec());
                    this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                    this.syncTribeBlocks(blockPos);
                }
                blockPos = this.aI.add(task.getFacing().getOpposite().getDirectionVec());
                navigator.tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.35f);
            }
        }
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

    BlockPos executeMiningTask(KoboldTask task, UUID tribeId) {
        HashSet<BlockPos> miningTargets = task.getTargetBlocks();
        EnumFacing facing = task.getFacing();
        ArrayList<BlockPos> row = new ArrayList<>();
        Integer maxZ = null;
        if (miningTargets.isEmpty()) {
            return null;
        } else {
            for(BlockPos target : miningTargets) {
                switch (facing) {
                    case NORTH:
                        if (maxZ == null || target.getZ() >= maxZ) {
                            maxZ = target.getZ();
                            row.add(target);
                        }
                        break;
                    case SOUTH:
                        if (maxZ == null || target.getZ() <= maxZ) {
                            maxZ = target.getZ();
                            row.add(target);
                        }
                        break;
                    case EAST:
                        if (maxZ == null || target.getX() <= maxZ) {
                            maxZ = target.getX();
                            row.add(target);
                        }
                        break;
                    case WEST:
                        if (maxZ == null || target.getX() >= maxZ) {
                            maxZ = target.getX();
                            row.add(target);
                        }
                }
            }

            ArrayList<BlockPos> column = new ArrayList<>();

            for(BlockPos pos : row) {
                if ((facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) && pos.getZ() == maxZ) {
                    column.add(pos);
                }

                if ((facing == EnumFacing.EAST || facing == EnumFacing.WEST) && pos.getX() == maxZ) {
                    column.add(pos);
                }
            }

            if (column.isEmpty()) {
                return null;
            } else {
                ArrayList<BlockPos> positions = new ArrayList();
                EnumFacing taskFacing = task.getFacing();
                BlockPos targetPos = task.getOriginPos();
                BlockPos minePos;

                if (taskFacing.getAxis() == EnumFacing.Axis.Z) {
                    minePos = new BlockPos(targetPos.getX(), targetPos.getY(), column.get(0).getZ());
                    if (taskFacing == EnumFacing.NORTH) {
                        minePos = minePos.north();
                    } else {
                        minePos = minePos.south();
                    }

                    positions.add(minePos.down());
                    positions.add(minePos.down().east());
                    positions.add(minePos.down().west());
                    positions.add(minePos);
                    positions.add(minePos.up());
                    positions.add(minePos.up().up());
                    positions.add(minePos.up().up().up());
                    positions.add(minePos.west());
                    positions.add(minePos.west().up());
                    positions.add(minePos.west().up().up());
                    positions.add(minePos.west().up().up().up());
                    positions.add(minePos.west().west());
                    positions.add(minePos.west().west().up());
                    positions.add(minePos.west().west().up().up());
                    positions.add(minePos.east());
                    positions.add(minePos.east().up());
                    positions.add(minePos.east().up().up());
                    positions.add(minePos.east().up().up().up());
                    positions.add(minePos.east().east());
                    positions.add(minePos.east().east().up());
                    positions.add(minePos.east().east().up().up());
                } else {
                    minePos = new BlockPos(column.get(0).getX(), targetPos.getY(), targetPos.getZ());
                    if (taskFacing == EnumFacing.EAST) {
                        minePos = minePos.east();
                    } else {
                        minePos = minePos.west();
                    }

                    positions.add(minePos.down());
                    positions.add(minePos.down().north());
                    positions.add(minePos.down().south());
                    positions.add(minePos);
                    positions.add(minePos.up());
                    positions.add(minePos.up().up());
                    positions.add(minePos.up().up().up());
                    positions.add(minePos.south());
                    positions.add(minePos.south().up());
                    positions.add(minePos.south().up().up());
                    positions.add(minePos.south().up().up().up());
                    positions.add(minePos.south().south());
                    positions.add(minePos.south().south().up());
                    positions.add(minePos.south().south().up().up());
                    positions.add(minePos.north());
                    positions.add(minePos.north().up());
                    positions.add(minePos.north().up().up());
                    positions.add(minePos.north().up().up().up());
                    positions.add(minePos.north().north());
                    positions.add(minePos.north().north().up());
                    positions.add(minePos.north().north().up().up());
                }

                HashSet<BlockPos> liquidBlocks = new HashSet<>();

                for(BlockPos pos : positions) {
                    if (this.world.getBlockState(pos).getMaterial().isLiquid()) {
                        this.world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), 2);
                        if (column.contains(pos)) {
                            liquidBlocks.add(pos);
                        }
                    }
                }

                if (!liquidBlocks.isEmpty()) {
                    task.addAllBlocks(liquidBlocks);
                    EntityPlayer master = this.getMasterPlayer();
                    if (master != null) {
                        PackageHandler.INSTANCE.sendTo(new SendBlocks(liquidBlocks, true), (EntityPlayerMP)master);
                    }
                }

                positions.clear();
                positions.add(minePos.down());
                if (taskFacing.getAxis() == EnumFacing.Axis.Z) {
                    positions.add(minePos.down().west());
                    positions.add(minePos.down().east());
                } else {
                    positions.add(minePos.down().north());
                    positions.add(minePos.down().south());
                }

                for(BlockPos pos : positions) {
                    if (this.world.getBlockState(pos).getBlock().isPassable(this.world, pos)) {
                        this.world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
                    }
                }

                HashSet<BlockPos> airBlocks = new HashSet<>();

                for(BlockPos pos : column) {
                    Block block = this.world.getBlockState(pos).getBlock();
                    if (block == Blocks.AIR) {
                        airBlocks.add(pos);
                    }
                }

                if (!airBlocks.isEmpty()) {
                    column.removeAll(airBlocks);
                    task.removeAllBlocks(airBlocks);
                    UUID tribeUuid = KoboldManager.getTribeMasterUUID(tribeId);
                    if (tribeUuid != null) {
                        EntityPlayer player = this.world.getPlayerEntityByUUID(tribeUuid);
                        if (player != null) {
                            PackageHandler.INSTANCE.sendTo(new SendBlocks(airBlocks, false), (EntityPlayerMP)player);
                        }
                    }
                }

                if (column.isEmpty()) {
                    return this.executeMiningTask(task, tribeId);
                } else {
                    BlockPos workerMinePos = null;
                    List<KoboldEntity> workers = task.getAssignedWorkers();

                    for(int i = 0; i < workers.size(); ++i) {
                        if ((workers.get(i)).getEntityId() == this.getEntityId()) {
                            if (i == 0) {
                                workerMinePos = this.findTaskBlock(column, -1, task.getFacing(), task.getOriginPos());
                                if (workerMinePos == null) {
                                    workerMinePos = this.findTaskBlock(column, 0, task.getFacing(), task.getOriginPos());
                                    if (workerMinePos == null) {
                                        workerMinePos = this.findTaskBlock(column, 1, task.getFacing(), task.getOriginPos());
                                    }
                                }
                                break;
                            }

                            if (i == 1) {
                                workerMinePos = this.findTaskBlock(column, 1, task.getFacing(), task.getOriginPos());
                                if (workerMinePos == null) {
                                    workerMinePos = this.findTaskBlock(column, 0, task.getFacing(), task.getOriginPos());
                                    if (workerMinePos == null) {
                                        workerMinePos = this.findTaskBlock(column, -1, task.getFacing(), task.getOriginPos());
                                    }
                                }
                                break;
                            }

                            if (i == 2) {
                                workerMinePos = this.findTaskBlock(column, 0, task.getFacing(), task.getOriginPos());
                                if (workerMinePos == null) {
                                    workerMinePos = this.findTaskBlock(column, 1, task.getFacing(), task.getOriginPos());
                                    if (workerMinePos == null) {
                                        workerMinePos = this.findTaskBlock(column, -1, task.getFacing(), task.getOriginPos());
                                    }
                                }
                                break;
                            }
                        }
                    }

                    return workerMinePos;
                }
            }
        }
    }

    @Nullable
    BlockPos findTaskBlock(List<BlockPos> blocks, int index, EnumFacing facing, BlockPos pos) {
        //BlockPos blockPos2;
        //int n2;

        if (blocks.isEmpty()) {
            return null;
        }

        ArrayList<BlockPos> extraBlocks = new ArrayList<BlockPos>();
        ArrayList<BlockPos> sideBlocks = new ArrayList<BlockPos>();
        ArrayList<BlockPos> rowBlocks = new ArrayList<BlockPos>();
        int dir = facing == EnumFacing.SOUTH || facing == EnumFacing.WEST ? -1 : 1;
        if (facing.getAxis() == EnumFacing.Axis.Z) {
            BlockPos rowAnchor = new BlockPos(pos.getX(), pos.getY(), blocks.get(0).getZ());
            rowBlocks.add(rowAnchor);
            rowBlocks.add(rowAnchor.up());
            rowBlocks.add(rowAnchor.up().up());
            rowBlocks.add(rowAnchor.west());
            rowBlocks.add(rowAnchor.west().up());
            rowBlocks.add(rowAnchor.west().up().up());
            rowBlocks.add(rowAnchor.east());
            rowBlocks.add(rowAnchor.east().up());
            rowBlocks.add(rowAnchor.east().up().up());
            if (index == 0) {
                for (BlockPos rowPos : rowBlocks) {
                    sideBlocks.add(rowPos.east(2));
                    sideBlocks.add(rowPos.east(-2));
                }
                for (BlockPos blockPos : blocks) {
                    if (sideBlocks.contains(blockPos)) continue;
                    extraBlocks.add(blockPos);
                }
            } else {
                for (BlockPos rowPos : rowBlocks) {
                    sideBlocks.add(rowPos.east(dir * 2 * index));
                }
                for (BlockPos sidePos : sideBlocks) {
                    if (!blocks.contains(sidePos)) continue;
                    extraBlocks.add(sidePos);
                }
            }
        }
        if (facing.getAxis() == EnumFacing.Axis.X) {
            BlockPos colAnchor = new BlockPos(blocks.get(0).getX(), pos.getY(), pos.getZ());
            rowBlocks.add(colAnchor);
            rowBlocks.add(colAnchor.up());
            rowBlocks.add(colAnchor.up().up());
            rowBlocks.add(colAnchor.north());
            rowBlocks.add(colAnchor.north().up());
            rowBlocks.add(colAnchor.north().up().up());
            rowBlocks.add(colAnchor.south());
            rowBlocks.add(colAnchor.south().up());
            rowBlocks.add(colAnchor.south().up().up());
            if (index == 0) {
                for (BlockPos colPos : rowBlocks) {
                    sideBlocks.add(colPos.south(2));
                    sideBlocks.add(colPos.south(-2));
                }
                for (BlockPos blockPos : blocks) {
                    if (sideBlocks.contains(blockPos)) continue;
                    extraBlocks.add(blockPos);
                }
            } else {
                for (BlockPos colPos : rowBlocks) {
                    sideBlocks.add(colPos.south(dir * 2 * index));
                }
                for (BlockPos sidePos : sideBlocks) {
                    if (blocks.contains(sidePos)) {
                        extraBlocks.add(sidePos);
                    }
                }
            }
        }
        if (extraBlocks.isEmpty()) {
            return null;
        }
        return extraBlocks.get(this.getRNG().nextInt(extraBlocks.size()));
    }

    void handleNearbyPlayerTick(UUID tribeId) {
        if (!this.canStoreInventory(tribeId, false)) {
            this.handleNearbyPlayer();
        }
    }

    void handleNearbyPlayer() {
        EntityPlayer player = this.world.getClosestPlayerToEntity(this, 15.0);
        if (this.hasMaster() && player != null && player.getDistance(this) < 2.0f && this.entityDataManager.get(MASTER).equals(player.getPersistentID().toString())) {
            this.getNavigator().clearPath();
        } else {
            if (this.ap == null || this.getDistance(this.ap.getX(), this.ap.getY(), this.ap.getZ()) > this.getWanderRange() || this.ab > 100) {
                int xOffset = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(5);
                int n2 = (this.getRNG().nextBoolean() ? 1 : -1) * this.getRNG().nextInt(5);
                int n3 = WorldUtils.getHeightAt(this.world, this.getPosition().getX() + xOffset, this.getPosition().getZ() + n2);
                this.ap = new BlockPos(this.getPosition().getX() + xOffset, n3, this.getPosition().getZ() + n2);
                this.ab = 0;
            }
            if (Math.sqrt(this.ap.distanceSq(this.getPosition())) > 2.0) {
                this.getNavigator().tryMoveToXYZ(this.ap.getX(), this.ap.getY(), this.ap.getZ(), 0.35f);
                this.tickPathVelocity();
            } else {
                ++this.ab;
            }
        }
    }

    double getWanderRange() {
        return Math.sqrt(800.0);
    }

    boolean canStoreInventory(UUID tribeId, boolean checkOpen) {
        if (this.hasInventoryItems()) {
            return false;
        }
        if (this.isTribeChestOpen(tribeId, checkOpen)) {
            this.a0 = 0;
            return true;
        }
        if (--this.a0 >= 0 || !this.ax) {
            return false;
        }
        this.a0 = 300;
        EntityPlayer master = this.world.getPlayerEntityByUUID(UUID.fromString(this.entityDataManager.get(MASTER)));
        EyeAndKoboldColor tribeColor = EyeAndKoboldColor.valueOf(this.entityDataManager.get(CURRENT_ACTION));
        if (master != null) {
            master.sendStatusMessage(new TextComponentString(tribeColor.getTextColor() + this.getGirlName() + "s " + TextFormatting.WHITE + "inventory is full and there are either no chests to put her items in or said chests are full as well"), false);
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
                        continue;
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
    boolean isTribeChestOpen(UUID tribeId, boolean checkOpen) {
        HashSet<BlockPos> chests = KoboldManager.getTribeChests(tribeId);
        if (chests == null) {
            return false;
        } else {
            BlockPos chosenChest = null;

            for(BlockPos chestPos : chests) {
                TileEntityChest chest = (TileEntityChest)this.world.getTileEntity(chestPos);
                IItemHandler chestHandler = chest.getSingleChestHandler();
                boolean canStore = false;

                for(int i = 0; i < this.inventory.getSlots(); ++i) {
                    ItemStack stack = this.inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        for(int slot = 0; slot < chestHandler.getSlots(); ++slot) {
                            ItemStack remaining = chestHandler.insertItem(slot, stack, true);
                            if (remaining.getCount() != stack.getCount()) {
                                canStore = true;
                                break;
                            }
                        }

                        if (canStore) {
                            break;
                        }
                    }
                }

                if (canStore) {
                    if (chosenChest == null) {
                        chosenChest = chestPos;
                    } else if (this.getDistanceSq(chosenChest) > this.getDistanceSq(chestPos)) {
                        chosenChest = chestPos;
                    }
                }
            }

            if (chosenChest == null) {
                return false;
            } else if (!(this.getDistance(chosenChest.getX(), chosenChest.getY(), chosenChest.getZ()) < 2.0)) {
                if (Math.abs(chosenChest.getY() - this.getPosition().getY()) > 4) {
                    if (!checkOpen) {
                        return false;
                    }

                    this.syncTribeBlocks(chosenChest);
                } else {
                    PathNavigate navigator = this.getNavigator();
                    BlockPos standPos = this.findStandPos(chosenChest);
                    navigator.tryMoveToXYZ(standPos.getX(), standPos.getY(), standPos.getZ(), 0.35f);
                    if (navigator.getPath() == null) {
                        if (!checkOpen) {
                            return false;
                        }

                        this.syncTribeBlocks(chosenChest);
                    }
                }

                return true;
            } else {
                TileEntityChest chest = (TileEntityChest)this.world.getTileEntity(chosenChest);
                IItemHandler chestHandler = chest.getSingleChestHandler();

                for(int i = 0; i < this.inventory.getSlots(); ++i) {
                    ItemStack stack = this.inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        for(int slot = 0; slot < chestHandler.getSlots(); ++slot) {
                            ItemStack remaining = chestHandler.insertItem(slot, stack, false);
                            if (remaining.getCount() <= 0) {
                                this.inventory.setStackInSlot(i, ItemStack.EMPTY);
                                break;
                            }

                            this.inventory.setStackInSlot(i, remaining);
                            stack = remaining;
                        }
                    }
                }

                this.world.playSound(null, chosenChest, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
    }

    @CheckReturnValue
    boolean assignTaskToKobold(UUID tribeId, KoboldTask task) {
        List<KoboldEntity> members = KoboldManager.getTribeMembersList(tribeId);
        Collection<KoboldTask> tasks = KoboldManager.getTribeTasks(tribeId);
        KoboldEntity closestKobold = null;
        Vec3d vec3d = new Vec3d(task.getOriginPos().getX(), task.getOriginPos().getY(), task.getOriginPos().getZ());
        for (KoboldEntity kobold : members) {
            boolean taskAssigned = false;
            for (KoboldTask workerTask : tasks) {
                if (!workerTask.hasWorker(kobold)) continue;
                taskAssigned = true;
                break;
            }
            if (taskAssigned || kobold.getInteractionPlayerUUID() != null) continue;
            if (closestKobold == null) {
                closestKobold = kobold;
            } else if (closestKobold.getPositionVector().distanceTo(vec3d) > kobold.getPositionVector().distanceTo(vec3d)) {
                closestKobold = kobold;
            }
        }
        return this.equals(closestKobold);
    }

    void navigateToTask(UUID tribeId, KoboldTask task, BlockPos pos) {
        if (this.ad == null) {
            this.aR = 24;
            this.cooldownTicks = 0;
            this.setCurrentAction(Action.NULL);
            this.entityDataManager.set(IS_ANCHORED, false);
            EntityPlayer master = this.getMasterPlayer();
            HashSet<BlockPos> blocks = task.getTargetBlocks();
            if (master != null && !blocks.isEmpty()) {
                PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(blocks, false), (EntityPlayerMP)master);
            }
            KoboldManager.removeWorkerTask(tribeId, this);
            return;
        }
        switch (this.ad.getMetadata()) {
            case 3: 
            case 5: {
                this.world.setBlockState(pos, Blocks.SAPLING.getStateForPlacement(this.world, pos, EnumFacing.NORTH, pos.getX(), pos.getY(), pos.getZ(), this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
                this.world.setBlockState(pos.north(), Blocks.SAPLING.getStateForPlacement(this.world, pos.north(), EnumFacing.NORTH, pos.getX(), pos.getY(), pos.getZ() + 1, this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
                this.world.setBlockState(pos.west(), Blocks.SAPLING.getStateForPlacement(this.world, pos.west(), EnumFacing.NORTH, pos.getX() + 1, pos.getY(), pos.getZ(), this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
                this.world.setBlockState(pos.north().west(), Blocks.SAPLING.getStateForPlacement(this.world, pos.north().west(), EnumFacing.NORTH, pos.getX() + 1, pos.getY(), pos.getZ() + 1, this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
                break;
            }
            default: {
                this.world.setBlockState(pos, Blocks.SAPLING.getStateForPlacement(this.world, pos, EnumFacing.NORTH, pos.getX(), pos.getY(), pos.getZ(), this.ad.getMetadata(), this, EnumHand.MAIN_HAND));
            }
        }
        this.aR = 24;
        this.cooldownTicks = 0;
        this.ad = null;
        this.setCurrentAction(Action.NULL);
        this.setAnchored(false);
        EntityPlayer master = this.getMasterPlayer();
        HashSet<BlockPos> blocks = task.getTargetBlocks();
        if (master != null && !blocks.isEmpty()) {
            PackageHandler.INSTANCE.sendTo((IMessage)new SendBlocks(blocks, false), (EntityPlayerMP)master);
        }
        KoboldManager.removeWorkerTask(tribeId, this);
    }

    void startMiningTask(UUID tribeId, BlockPos pos, KoboldTask task) {
        if (this.getCurrentAction() != Action.MINE) {
            this.mineBlockAt(pos, tribeId);
        } else {
            --this.cooldownTicks;
            if (this.cooldownTicks <= 0) {
                if (this.cooldownTicks == 0) {
                    PackageHandler.INSTANCE.sendToAllAround(new ResetController(this.girlID()), this.getTargetNetworkPoint());
                }
                if (this.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                    this.navigateToTask(tribeId, task, pos);
                } else {
                    --this.aR;
                    if (this.aR < 0) {
                        this.aR = 24;
                        this.cooldownTicks = 78;
                        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
                        EntityPlayer entityPlayer = this.getMasterPlayer();
                        for (BlockPos target : task.getTargetBlocks()) {
                            if (this.world.getBlockState(target).getBlock() == Blocks.AIR) {
                                hashSet.add(target);
                                //continue;
                            } else if (target.getX() != pos.getX() || target.getZ() != pos.getZ()) {
                                try {
                                    ItemStack dropStack = this.world.getBlockState(target).getBlock().getItem(this.world, pos, this.world.getBlockState(pos));
                                    if (dropStack.getItem() != Items.AIR) {
                                        this.canExtractItem(dropStack);
                                    }
                                } catch (IllegalArgumentException e) {
                                    Main.LOGGER.error("Couldn't get an item out of the block that a kobold just destroyed when falling a tree. As a result, the block wasn't added into the kobolds inventory. If you see this message, pls tell trol about it and send her the following stacktrace. Do you maybe remember what block the kobold just removed? Stacktrace follwing:");
                                    Main.LOGGER.warn("block in question: " + this.world.getBlockState(target).getBlock().getTranslationKey());
                                    Main.LOGGER.error(e.getMessage());
                                }

                                this.ad = this.getBlockItem(target);
                                this.world.destroyBlock(target, false);
                                task.removeBlocks(target);
                                task.removeAllBlocks(hashSet);
                                hashSet.add(target);
                                if (entityPlayer != null) {
                                    PackageHandler.INSTANCE.sendTo(new SendBlocks(hashSet, false), (EntityPlayerMP) entityPlayer);
                                }
                                return;
                            }
                        }
                        try {
                            ItemStack dropStack = this.world.getBlockState(pos).getBlock().getItem(this.world, pos, this.world.getBlockState(pos));
                            if (dropStack.getItem() != Items.AIR) {
                                this.canExtractItem(dropStack);
                            }
                        } catch (IllegalArgumentException e) {
                            Main.LOGGER.error("Couldn't get an item out of the block that a kobold just destroyed when falling a tree. As a result, the block wasn't added into the kobolds inventory. If you see this message, pls tell trol about it and send her the following stacktrace. Do you maybe remember what block the kobold just removed? Stacktrace follwing:");
                            Main.LOGGER.warn("block in question: " + this.world.getBlockState(pos).getBlock().getTranslationKey());
                            Main.LOGGER.error(e.getMessage());
                        }

                        this.ad = this.getBlockItem(pos);
                        this.world.destroyBlock(pos, false);
                        int logCount = 0;
                        for (BlockPos target : task.getTargetBlocks()) {
                            if (!(this.world.getBlockState(target).getBlock() instanceof BlockLog)) continue;
                            ++logCount;
                        }
                        HashSet<BlockPos> trunkBlocks = new HashSet<>();
                        
                        for (int i = 0; i < logCount; ++i) {
                            trunkBlocks.add(pos.add(0, i, 0));
                        }

                        HashSet<BlockPos> otherBlocks = new HashSet<>();
                        for (BlockPos target : task.getTargetBlocks()) {
                            if (trunkBlocks.contains(target)) continue;
                            otherBlocks.add(target);
                        }

                        if (!otherBlocks.isEmpty() && entityPlayer != null) {
                            PackageHandler.INSTANCE.sendTo(new SendBlocks(otherBlocks, false), (EntityPlayerMP) entityPlayer);
                        }

                        int height = 1;
                        while (true) {
                            //BlockPos blockPos2;
                            BlockPos logPos = pos.add(0, height, 0);
                            IBlockState logState = this.world.getBlockState(logPos);
                            if (this.world.getBlockState(logPos).getBlock() instanceof BlockLog) {
                                this.world.destroyBlock(logPos, false);
                                EntityFallingBlock fallingBlock = new EntityFallingBlock(this.world, (double) logPos.getX() + 0.5, logPos.getY(), (double) logPos.getZ() + 0.5, logState);
                                fallingBlock.fallTime = 1;
                                this.world.spawnEntity(fallingBlock);
                            }
                            if (!task.getTargetBlocks().contains(logPos)) break;
                            ++height;
                        }
                    }
                }
            }
        }
    }

    ItemStack getBlockItem(BlockPos blockPos) {
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
        int blockId = ItemBlock.getIdFromItem(itemStack.getItem());
        int metadata = itemStack.getItem().getMetadata(itemStack);
        if (blockId == 17 && metadata == 1) {
            return new ItemStack(Blocks.SAPLING, 1, 1);
        } else if (blockId == 17 && metadata == 2) {
            return new ItemStack(Blocks.SAPLING, 1, 2);
        } else if (blockId == 17 && metadata == 3) {
            return new ItemStack(Blocks.SAPLING, 1, 3);
        } else if (blockId == 162 && metadata == 0) {
            return new ItemStack(Blocks.SAPLING, 1, 4);
        } else if (blockId == 162 && metadata == 1) {
            return new ItemStack(Blocks.SAPLING, 1, 5);
        } else {
            return new ItemStack(Blocks.SAPLING, 1, 0);
        }
    }

    void mineBlockAt(BlockPos pos, UUID tribeId) {
        //Object object; My beloved.
        BlockPos bestBlock;
        Vec3i bestBlockVec = null;
        ArrayList<BlockPos> neighbours = new ArrayList<>();

        if (this.world.getBlockState(pos.north().down()).isFullCube() && !this.world.getBlockState(pos.north()).isFullBlock()) {
            neighbours.add(pos.north());
        }
        if (this.world.getBlockState(pos.east().down()).isFullCube() && !this.world.getBlockState(pos.east()).isFullBlock()) {
            neighbours.add(pos.east());
        }
        if (this.world.getBlockState(pos.south().down()).isFullCube() && !this.world.getBlockState(pos.south()).isFullBlock()) {
            neighbours.add(pos.south());
        }
        if (this.world.getBlockState(pos.west().down()).isFullCube() && !this.world.getBlockState(pos.west()).isFullBlock()) {
            neighbours.add(pos.west());
        }

        for (BlockPos neighbour : neighbours) {
            if (bestBlockVec == null) {
                bestBlockVec = neighbour;
            } else {
                double d = new Vec3d((float) bestBlockVec.getX() + 0.5f, bestBlockVec.getY(), (float) bestBlockVec.getZ() + 0.5f).distanceTo(this.getPositionVector());
                double d2 = new Vec3d((float) neighbour.getX() + 0.5f, neighbour.getY(), (float) neighbour.getZ() + 0.5f).distanceTo(this.getPositionVector());
                if (d2 < d) {
                    bestBlockVec = neighbour;
                }
            }
        }

        if (bestBlockVec == null) {
            KoboldManager.removeWorkerTask(tribeId, this);
            EntityPlayer master = this.getMasterPlayer();
            if (master != null) {
                master.sendStatusMessage(new TextComponentString("Your kobolds cannot fall this tree because it starts underground"), true);
                return;
            }
        }

        if (this.getPosition().getDistance(bestBlockVec.getX(), bestBlockVec.getY(), bestBlockVec.getZ()) > 1.0) {
            if (Math.abs(this.getPosition().getY() - bestBlockVec.getY()) > 4) {
                this.syncTribeBlocks((BlockPos)bestBlockVec);
                return;
            }
            bestBlock = this.findStandPos((BlockPos)bestBlockVec);
            this.getNavigator().tryMoveToXYZ((double)((Vec3i)bestBlock).getX() + 0.5, ((Vec3i)bestBlock).getY(), (double)((Vec3i)bestBlock).getZ() + 0.5, 0.35);
            this.tickPathVelocity();
            return;
        }
        float yaw = 0.0f;
        if (((BlockPos)bestBlockVec).subtract(pos).equals(new BlockPos(0, 0, -1))) {
            yaw = 0.0f;
        }
        if (((BlockPos)bestBlockVec).subtract(pos).equals(new BlockPos(1, 0, 0))) {
            yaw = 90.0f;
        }
        if (((BlockPos)bestBlockVec).subtract(pos).equals(new BlockPos(0, 0, 1))) {
            yaw = 180.0f;
        }
        if (((BlockPos)bestBlockVec).subtract(pos).equals(new BlockPos(-1, 0, 0))) {
            yaw = -90.0f;
        }

        this.setTargetPosition(new Vec3d((double)bestBlockVec.getX() + 0.5, bestBlockVec.getY(), (double)bestBlockVec.getZ() + 0.5));
        this.setYawRotation(yaw);
        this.entityDataManager.set(IS_ANCHORED, true);
        this.entityDataManager.set(at, true);
        this.setCurrentAction(Action.MINE);
        this.world.destroyBlock(((BlockPos)bestBlockVec).up(), false);
    }

    void handleModelSync() {
        if (this.editedColorManually) {
            return;
        }
        Optional<UUID> tribeIdOpt = this.entityDataManager.get(TRIBE_ID);
        if (tribeIdOpt.isPresent()) {
            this.entityDataManager.set(CURRENT_ACTION, KoboldManager.getTribeColor((UUID) tribeIdOpt.get()).toString());
        }
    }

    @Override
    public void setCurrentAction(Action action) {
        if (this.getCurrentAction() != Action.MATING_PRESS_CUM || (action != Action.MATING_PRESS_SOFT && action != Action.MATING_PRESS_HARD)) {
            if (this.getCurrentAction() != Action.KOBOLD_ANAL_CUM || (action != Action.KOBOLD_ANAL_SLOW && action != Action.KOBOLD_ANAL_FAST)) {
                if (this.getCurrentAction() != Action.CUMBLOWJOB || (action != Action.SUCKBLOWJOB && action != Action.THRUSTBLOWJOB)) {
                    if (action == Action.MATING_PRESS_CUM) {
                        this.actionCooldown = 0;
                    }
                    super.setCurrentAction(action);
                }
            }
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        EntityPlayer player;
        super.onDeath(cause);
        if (!this.world.isRemote) {
            Optional<UUID> tribeIdOpt = this.entityDataManager.get(TRIBE_ID);
            if (tribeIdOpt.isPresent()) {
                UUID uUID = tribeIdOpt.get();
                KoboldManager.removeMemberFromTribe(uUID, this);
                if (this.hasMaster() && (player = this.world.getPlayerEntityByUUID(UUID.fromString(this.getDataManager().get(MASTER)))) != null) {
                    player.sendMessage(new TextComponentString(String.format("%s%s%s has perished %suwu", TextFormatting.RED, this.getGirlName(), TextFormatting.WHITE, TextFormatting.RED)));
                }
            }
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
        nbt.setFloat("body_size", this.entityDataManager.get(SIZE));
        nbt.setInteger("eyeColorX", this.entityDataManager.get(ACTION_TARGET_POS).getX());
        nbt.setInteger("eyeColorY", this.entityDataManager.get(ACTION_TARGET_POS).getY());
        nbt.setInteger("eyeColorZ", this.entityDataManager.get(ACTION_TARGET_POS).getZ());
        nbt.setString("model", this.entityDataManager.get(APPEARANCE_DNA));
        nbt.setString("name", this.entityDataManager.get(KOBOLD_NAME));
        nbt.setString("master", this.entityDataManager.get(MASTER));
        nbt.setTag("inventory", this.inventory.serializeNBT());
        nbt.setString("bodyColor", this.entityDataManager.get(CURRENT_ACTION));
        nbt.setBoolean("editedColorManually", this.editedColorManually);
        Optional<UUID> tribeIdOpt = this.entityDataManager.get(TRIBE_ID);
        if (tribeIdOpt.isPresent()) {
            nbt.setUniqueId("tribeId", tribeIdOpt.get());
            nbt.setBoolean("isLeader", KoboldManager.isTribeMember((UUID)tribeIdOpt.get(), this));
            nbt.setString("tribeName", this.entityDataManager.get(TRIBE_NAME));
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        BlockPos blockPos;
        super.readEntityFromNBT(nbt);
        String modelCode = nbt.getString("model");

        if (!modelCode.isEmpty()) {
            this.entityDataManager.set(APPEARANCE_DNA, modelCode);
        }
        if (!BlockPos.ORIGIN.equals(blockPos = new BlockPos(nbt.getInteger("eyeColorX"), nbt.getInteger("eyeColorY"), nbt.getInteger("eyeColorZ")))) {
            this.entityDataManager.set(ACTION_TARGET_POS, blockPos);
        }
        this.entityDataManager.set(SIZE, nbt.getFloat("body_size"));
        this.entityDataManager.set(KOBOLD_NAME, nbt.getString("name"));
        this.entityDataManager.set(MASTER, nbt.getString("master"));
        this.inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        String bodyColor = nbt.getString("bodyColor");
        if (!bodyColor.isEmpty()) {
            this.entityDataManager.set(CURRENT_ACTION, nbt.getString("bodyColor"));
        }
        this.editedColorManually = nbt.getBoolean("editedColorManually");
        //if (uUID != null && !this.isDead) {
        UUID tribeId = nbt.getUniqueId("tribeId");
        if (nbt.hasUniqueId("tribeId") && tribeId != null && !this.isDead) {

//            if (tribeId.getLeastSignificantBits() == 0 || tribeId.getMostSignificantBits() == 0) {
//                // TODO tribeId return a 00000... UUID when missing... super weird
//                return;
//            }

            this.entityDataManager.set(TRIBE_ID, Optional.of(tribeId));
            if (!KoboldManager.doesTribeExist(tribeId)) {
                KoboldManager.createTribe(tribeId, EyeAndKoboldColor.valueOf((String)this.entityDataManager.get(CURRENT_ACTION)));
            }

            KoboldManager.addTribeMember(tribeId, this);
            if (nbt.getBoolean("isLeader")) {
                KoboldManager.setTribeLeader(tribeId, this);
            }
            this.entityDataManager.set(TRIBE_NAME, nbt.getString("tribeName"));
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

    boolean hasInventoryItems() {
        for (int i = 0; i < this.inventory.getSlots(); ++i) {
            if (this.inventory.getStackInSlot(i).isEmpty()) continue;
            return false;
        }
        return true;
    }

    boolean canAssignTask(KoboldTask task) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        for (BlockPos pos : task.getTargetBlocks()) {
            IBlockState state = this.world.getBlockState(pos);
            ItemStack drop = state.getBlock().getItem(this.world, pos, state);
            drops.add(drop);
        }
        return this.canStoreItems(drops);
    }

    boolean canInsertItem(ItemStack stack) {
        return this.canInsertItemStack(this.inventory, stack, true, false);
    }

    boolean canStoreItems(List<ItemStack> stacks) {
        ItemStackHandler simulated = new ItemStackHandler(this.inventory.getSlots());
        for (int i = 0; i < simulated.getSlots(); ++i) {
            simulated.setStackInSlot(i, this.inventory.getStackInSlot(i));
        }
        for (ItemStack stack : stacks) {
            if (!this.canInsertItemStack(simulated, stack, true, false)) {
                return false;
            }
        }
        return true;
    }

    boolean canExtractItem(ItemStack stack) {
        return this.canInsertItemStack(this.inventory, stack, false, true);
    }

    boolean canInsertItemStack(ItemStackHandler handler, ItemStack stack, boolean simulate, boolean extract) {
        //ItemStack itemStack2;
        //int n;
        for (int slot = 0; slot < handler.getSlots(); ++slot) {
            ItemStack existing = handler.getStackInSlot(slot);
            if (existing.getItem() == stack.getItem() && existing.getMetadata() == stack.getMetadata()) {
                int maxStack = existing.getMaxStackSize();
                if (maxStack > stack.getCount() + existing.getCount()) {
                    if (!simulate) {
                        existing.setCount(existing.getCount() + stack.getCount());
                    }
                    return true;
                }

                int space = maxStack - existing.getCount();
                existing.setCount(maxStack);
                stack.setCount(stack.getCount() - space);
            }
        }

        for (int slot = 0; slot < handler.getSlots(); ++slot) {
            ItemStack itemStack2 = handler.getStackInSlot(slot);
            if (itemStack2.getItem() != Items.AIR) continue;
            if (!simulate) {
                handler.setStackInSlot(slot, stack);
            }
            return true;
        }
        if (simulate || !extract) {
            return false;
        }
        EntityItem entityItem = new EntityItem(this.world);
        entityItem.setItem(stack);
        entityItem.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(entityItem);
        return false;
    }

    void playSoundAtVolume(SoundEvent sound, float volume) {
        float shrink = 0.25f - this.entityDataManager.get(SIZE);
        double progress = shrink / 0.25f;
        float pitch = (float) ReferenceAndRotationHelper.LerpDouble(0.9f, 1.1f, progress);
        this.PlaySoundAtPosition(sound, volume, pitch);
    }

    void playSound(SoundEvent sound) {
        this.playSoundAtVolume(sound, 1.0f);
    }

    void playRandomSounds(SoundEvent[] sounds) {
        this.playRandomSound(sounds, 1.0f);
    }

    void playRandomSound(SoundEvent[] sounds, float volume) {
        this.playSoundAtVolume(sounds[this.getRNG().nextInt(sounds.length)], volume);
    }

    @Override
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.world instanceof FakeWorld) {
            return PlayState.STOP;
        }
        if (this.actionController == null) {
            this.initAnimationControllers();
        }
        float shrink = 0.25f - this.getDataManager().get(SIZE);
        GeckoLibCache.getInstance().parser.setValue("size", shrink);
        switch (event.getController().getName()) {
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
                } else if (this.isRiding()) {
                    this.createAnimation("animation.kobold.sit", true, event);
                } else {
                    double moved = Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ);
                    if (!(Boolean) this.entityDataManager.get(IS_ANCHORED) && moved > 0.0) {
                        if (this.onGround && Math.abs(Math.abs(this.prevPosY) - Math.abs(this.posY)) < (double) 0.1f) {
                            this.rotationYaw = this.rotationYawHead;
                            double d2 = 1.0 + (double) (shrink * 2.0f);
                            this.movementController.setAnimationSpeed(d2);
                            if (this.IsBlockedByCeiling()) {
                                this.createAnimation("animation.kobold.crouch_walk", true, event);
                                break;
                            }
                            if (this.entityDataManager.get(aC)) {
                                this.createAnimation("animation.kobold.run_armed", true, event);
                                break;
                            }
                            if (moved > (double) 0.2f) {
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
                    this.createAnimation(this.entityDataManager.get(aC) ? "animation.kobold.idle_armed" : "animation.kobold.idle", true, event);
                    break;
                }
            }
            case "action": {
                switch (this.getCurrentAction()) {
                    case NULL: {
                        this.createAnimation("animation.kobold.null", true, event);
                        break;
                    }
                    case ATTACK: {
                        this.createAnimation("animation.kobold.attack", false, event);
                        break;
                    }
                    case RIDE: 
                    case SIT: {
                        this.createAnimation("animation.kobold.sit", true, event);
                        break;
                    }
                    case MINE: {
                        this.createAnimation("animation.kobold.fall_tree", true, event);
                        break;
                    }
                    case PAYMENT: {
                        this.createAnimation("animation.kobold.paymentBackpack", true, event);
                        break;
                    }
                    case STARTBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobStart", false, event);
                        break;
                    }
                    case SUCKBLOWJOB_BLINK: {
                        String string = this.a4 ? "R" : "L";
                        String string2 = this.aT ? "Switch" : "";
                        this.createAnimation("animation.kobold.blowjobSlow" + string + string2, true, event);
                        break;
                    }
                    case THRUSTBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobFast", true, event);
                        break;
                    }
                    case CUMBLOWJOB: {
                        this.createAnimation("animation.kobold.blowjobCum", false, event);
                        break;
                    }
                    case KOBOLD_ANAL_START: {
                        this.createAnimation("animation.kobold.analStart", false, event);
                        break;
                    }
                    case KOBOLD_ANAL_SLOW: {
                        this.createAnimation("animation.kobold.analSoft", true, event);
                        break;
                    }
                    case KOBOLD_ANAL_FAST: {
                        this.createAnimation("animation.kobold.analHard", true, event);
                        break;
                    }
                    case KOBOLD_ANAL_CUM: {
                        this.createAnimation("animation.kobold.analCum", true, event);
                        break;
                    }
                    case SLEEP: {
                        this.createAnimation("animation.kobold.sleep", true, event);
                        break;
                    }
                    case MATING_PRESS_START: {
                        this.createAnimation("animation.kobold.mating_press_start", false, event);
                        break;
                    }
                    case MATING_PRESS_SOFT: {
                        this.createAnimation("animation.kobold.mating_press_soft", true, event);
                        break;
                    }
                    case MATING_PRESS_HARD: {
                        this.createAnimation("animation.kobold.mating_press_hard", true, event);
                        break;
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
                    this.playRandomSounds(SoundsHandler.GIRLS_KOBOLD_GIGGLE);
                    break;
                }
                case "moan": {
                    this.playRandomSounds(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "moanMating": {
                    --this.aN;
                    if (this.aN > 0) break;
                    this.aN = 3;
                    this.playRandomSounds(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "analHardMSG1": {
                    --this.aN;
                    if (this.aN > 0) break;
                    this.aN = 4;
                    this.playRandomSounds(SoundsHandler.GIRLS_KOBOLD_MOAN);
                    break;
                }
                case "orgasm": {
                    this.playRandomSounds(SoundsHandler.GIRLS_KOBOLD_ORGASM);
                    break;
                }
                case "breath": {
                    this.playRandomSound(SoundsHandler.GIRLS_KOBOLD_LIGHTBREATHING, 0.5f);
                    break;
                }
                case "haa": {
                    this.playRandomSound(SoundsHandler.GIRLS_KOBOLD_HAA, 0.7f);
                    break;
                }
                case "interested": {
                    this.playRandomSounds(SoundsHandler.GIRLS_KOBOLD_INTERESTED);
                    break;
                }
                case "yep": {
                    this.playRandomSounds(SoundsHandler.GIRLS_KOBOLD_YEP);
                    break;
                }
                case "bjmoan": {
                    this.playSound(SoundsHandler.random(SoundsHandler.GIRLS_KOBOLD_BJMOAN));
                    break;
                }
                case "blowjobStartbreath": {
                    int n = this.getRNG().nextInt(3);
                    this.playSound(SoundsHandler.GIRLS_KOBOLD_LIGHTBREATHING[n]);
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
                    this.playSound(SoundsHandler.GIRLS_KOBOLD_SAD[this.getRNG().nextInt(1)]);
                    break;
                }
                case "renderEgg": {
                    this.isRenderEgg = true;
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
    public ItemStack getStackInSlot(int slot) {
        if (slot >= this.inventory.getSlots()) {
            return ItemStack.EMPTY;
        }
        return this.inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return this.inventory.extractItem(slot, amount, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return this.inventory.extractItem(slot, this.inventory.getStackInSlot(slot).getCount(), false);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.inventory.setStackInSlot(slot, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack stack) {
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

    public static class EventHandler {
        int tickCounter = 0;

        @SubscribeEvent
        public void onLivingDeath(LivingDeathEvent event) {
            if (event.getEntityLiving() instanceof KoboldEntity) {
                KoboldEntity kobold = (KoboldEntity)event.getEntityLiving();
                if (!kobold.world.isRemote) {
                    for (int i = 0; i < kobold.inventory.getSlots(); ++i) {
                        ItemStack itemStack = kobold.inventory.getStackInSlot(i);
                        if (itemStack.getItem() == Items.AIR) continue;
                        kobold.dropItem(itemStack.getItem(), itemStack.getCount());
                    }
                }
            }
        }

        @SubscribeEvent
        public void onLivingHurtPlayer(LivingHurtEvent event) {
            EntityPlayer player;
            Entity entity = event.getEntity();
            World world = entity.getEntityWorld();
            if (!world.isRemote) {
                if (entity instanceof KoboldEntity) {
                    KoboldEntity kobold = (KoboldEntity) entity;
                    Optional<UUID> optional = kobold.getDataManager().get(TRIBE_ID);
                    if (optional.isPresent()) {
                        Entity attacker = event.getSource().getTrueSource();
                        if (attacker != null) {
                            if (attacker instanceof EntityLivingBase) {
                                if (attacker instanceof EntityPlayer) {
                                    player = (EntityPlayer) attacker;
                                    if (!player.capabilities.isCreativeMode) {
                                        if (player.equals(kobold.getMasterPlayer())) {
                                            return;
                                        }
                                    }
                                }

                                if ((player = kobold.getMasterPlayer()) != null) {
                                    player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "Your Tribe is under Attack!"), true);
                                }
                                KoboldManager.addTribeTarget(optional.get(), (EntityLivingBase) attacker);
                            }
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public void onWorldUnload(WorldEvent.Unload event) {
            for (GirlEntity girl : GirlEntity.getGirlEntityList()) {
                KoboldEntity kobold;
                Optional<UUID> tribeOptUuid;
                if (girl instanceof KoboldEntity && (tribeOptUuid = (kobold = (KoboldEntity) girl).getDataManager().get(TRIBE_ID)).isPresent() && KoboldManager.isTribeMember((UUID) tribeOptUuid.get(), kobold)) {
                    kobold.teleportToHome((UUID) tribeOptUuid.get());
                }
            }
        }

        @SubscribeEvent
        public void onLivingHurtCancel(LivingHurtEvent event) {
            if (event.getSource() == DamageSource.IN_WALL) {
                Entity entity = event.getEntity();
                if (entity instanceof KoboldEntity) {
                    entity.setPosition(entity.posX, entity.posY + 1.0, entity.posZ);
                    event.setCanceled(true);
                }
            }
        }

        @SideOnly(value=Side.CLIENT)
        @SubscribeEvent
        public void onClientTick(TickEvent.ClientTickEvent event) {
            WorldClient world = Minecraft.getMinecraft().world;
            if (world != null) {
                if (++this.tickCounter % 20 == 0) {
                    PackageHandler.INSTANCE.sendToServer(new GetTribeUIValues());
                }
            }
        }
    }
}

