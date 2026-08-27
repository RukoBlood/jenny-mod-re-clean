package com.trolmastercard.sexmod.util;

import net.minecraft.util.math.Vec3d;
import java.util.Random;

public class Reference {
    public static final String MOD_ID = "sexmod"; //unused
    final static public String NAME = "Fapcraft"; //unused
    final static public String VERSION = "1.1.0"; //unused
    final static public String CLIENT = "com.trolmastercard.sexmod.proxy.ClientProxy"; //unused
    final static public String COMMON = "com.trolmastercard.sexmod.proxy.CommonProxy"; //unused
    final static public Random RANDOM = new Random(); //used everywhere
    static public int EDITOR_ID = 0;
    static public int BUTTON_ID = 0;
    final static public int ENTITY_JENNY_UNUSED = 4674237; //unured
    final static public int ENTITY_ELLIE_UNUSED = 6281823; //unused
    static public Vec3d cameraPosCurrent = Vec3d.ZERO;
    static public Vec3d cameraPosPrevious = Vec3d.ZERO;
}
