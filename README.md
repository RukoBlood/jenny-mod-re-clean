**18+ Alert! Bad code Alert!**

<p align="center">
  <img src="./img/deobf3.jpg" alt="1" width="258">
  <img src="./img/jongun-deobf.jpg" alt="2" width="247">
  <img src="img/atomov%20deobf.png" alt="3" width="258">
  <img src="img/ZKM.png" alt="4" width="258">
</p>

**This project is a fork of https://github.com/palkaline/jenny-mod-re**
---
# Jenny Mod Deobfuscation and Remapping project (Based on Trolmastercard's Fapcraft 1.1.0)

Created by Palkaline, Remapped by Rukoblood

## Fortunate news.
One guy (RealCrystalNight) fully reverse engineered Jenny mod's code.

Go check it out: https://github.com/ReverseEngineeringEnthusiasts/Jenny-Mod-Fapcraft

Also found out that This mod was obfuscated with Zelix KlassMaster 13

## About
jenny-mod-re-clean (or JMRC) is a project about a deobfuscation and remapping a one long dead mod originally made by SchnurriTV, and then trolmastercard.

Initial goals of this project:
- Reverse engineer the extreme obfuscation that has been applied jar-wide (Done! ZKM has fallen)
- FOSS! 
- Simple SDK for pasters. 
- Remap all the classes, fields and methods.
- Fix the bugs from decompiling, commented out code if needed.

## Tools, AI and mods that i used to reverse engineer this Mod

- Giga IDE CE 2025.1 
- Konloch's bytecode viewer 2.13.2
- Google Gemini (free tier, no antigravity, no ai plus/pro).
- SchnurriTV's Jenny Mod build v1.5.2 (As unobfuscated bytecode reference for class, field and methods names.)

## Building and/or running
1. Git clone this repo.
2. Open this project in IntelliJ IDEA or Giga IDE. 
3. gradlew build
4. if successful - gradlew RunClient to test bugs in dev environment


## Installation:
<p>
  <img src="img/img.png" alt="ишак">
</p>

After you fully compiled mod with assets, install it as default minecraft mod. 

Don't forget to additionally install geckolib. You can download it from curseforge/'rinth, or get it directly from libs folder.


## Obfuscation
Moved to Obfuscation.md (check docs)

## Bugs
It's broken.

It runs in dev environment, but shits itself on real client (Packets crashes and freezes the entire game)

## TODO
- Fix girls bugs, including critical ones
- Rename methods and fields in classes
- Fix code that palkaline didn't fixed, because he abandoned this project.
- Make it run on real client
  
## References

- See: https://github.com/hfgufjkgjkfbg/Minecraft-Sex-Mod-Jenny-1.12.2-Forge
- See: https://github.com/ReverseEngineeringEnthusiasts/Jenny-Mod-Fapcraft
