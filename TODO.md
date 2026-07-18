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
- When selecting kobold in horny potion, it crashes `if (a_inner492 == null) {
            System.out.println("tribe of UUID " + uUID.toString() + " not found uwu");
            return new HashMap<UUID, BlockPos>();
        }`

This is a known bug found by palkaline, where kobold UUIDs are messed up

- other stuff