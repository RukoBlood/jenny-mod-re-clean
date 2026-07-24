# Bugs (Old)

- Likely usages through that fail to inherit from their proper parent
  - CFR did a good job including at least an @Override to make things 1000% easier.

- Galath fight, lunge move softlocking player

- ~~Jenny model not correctly culling out steve during normal~~

# Bugs (NEW)
- SpawnEnergyBallParticles and SpawnEnergyBallParticlesAlt are placed randomly due to crash of an IDE (Thanks Giga IDE)
- Softlocks with some of the characters (bed related)
- PlayerSlime culling works incorrectly
- Slime girl only does sloppy toppy
- When selecting kobold in horny potion, it crashes
  ```
  if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashMap<UUID, BlockPos>();
        }
  ```

  
now it's:


```
public static HashMap<UUID, BlockPos> getUnloadedMembersMap(UUID uUID, World world)
{
        KoboldSavedData.KoboldTribe tribe = tribesMap.get(uUID);
        if (tribe == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashMap<UUID, BlockPos>();
        }
        HashMap<UUID, BlockPos> unloadedMap = tribe.unloadedMembers;
        ArrayList<UUID> missingUUIDs = new ArrayList<UUID>();

        for (Map.Entry<UUID, BlockPos> entry : unloadedMap.entrySet()) {
            BlockPos pos = entry.getValue();
            UUID koboldUUID = entry.getKey();
            if (!world.isAreaLoaded(pos, 5)) continue;

            AxisAlignedBB checkArea = new AxisAlignedBB(pos.subtract(new Vec3i(-3, -3, -3)), pos.add(3, 3, 3));
            List<KoboldEntity> nearbyKobolds = world.getEntitiesWithinAABB(KoboldEntity.class, checkArea);
            boolean isFound = false;
            for (KoboldEntity kobold : nearbyKobolds) {
                if (!koboldUUID.equals(kobold.girlID())) continue;
                isFound = true;
                break;
            }
            if (isFound) continue;
            missingUUIDs.add(koboldUUID);
        }
        tribe.unloadedMembers = unloadedMap;
        return unloadedMap;
    }
```

This is a known bug found by palkaline, where kobold UUIDs are messed up
P.S: Caused by minecraft in IDE generating random names and uuid, meaning that tribes crashing the game while take damage

-BiaRenderer crashes game

- other stuff
