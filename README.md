**18+ Alert! Bad code Alert!** 

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
After you fully compiled mod with assets, install it as default minecraft mod. 

Don't forget to additionally install geckolib. You can download it from curseforge/'rinth, or get it directly from libs folder.


## Obfuscation
Moved to Obfuscation.md (check docs)
## Regex

Useful patterns below:
- Finding calls to arbitrary method, ie `ad`:
  - `(void |\.)ad\(\)`
- <details>
  <summary>Finding censored words:</summary>
  
    ```
    cock|pussy|dick|sex|trol|(?<!cl)ass|anal|butt|booty|sex|jenny|girl|nude|bee|ellie|slime|galath|fap
    ```
  </details>

No longer needed: 
- Exception wrapping obfuscation (pass-and-return)
    - Find: `throw ([\w\d]+)\.([\w\d]+)\(([\w\d]+)\);`
    - Replace: `throw $3;`
- Generic stripped onMessage repopulation:
  - Find: `((?:public|protected|private)?\s*IMessage\s+onMessage\s*\(\s*)IMessage(\s+\w+\s*,\s*MessageContext\s+\w+\s*\)\s*\{\s*return\s+[\w.]+\s*\(\s*\(\s*)([\w.]+)(\s*\)\s*\w+\s*,\s*\w+\s*\)\s*;\s*\})`
  - Replace: `@Override\n$1$3$2$3$4`

## Bugs (Palkaline)

I 110% guarantee there are bugs that were created during the deobfuscation process. Many steps are likely to result in many bugs, with each step compounding bugs before them.

The most likely bugs are:
- Dangling overrides I failed to rename 
  - a to doRender, getResourceLocation, getTexture, getModelFileLocation (done!)
  - so, these might fallback to defaults from the parent class.
- Referencing incorrect method, same signature.
  - Static method resolution is strange to me... you learn something new!

## Bugs (ME)

Current state of the mod - still broken stuff.

But some bugs got fixed


## Modifications from source jar

- I have edited some code portions that I believe are a bit anti-user:
  - The mod .jar and cwd deletion in /mods when age check fails
    - Filesystem manipulation seems dangerous to me
    - I find this check screen to be quite a bit ugly (javax...), I swear there used to 
    be an embedded in-game age-ask GUI button(s)... maybe I am too old...
- Commenting out of broken code:
  - An example is in the Kobold Damage Listener
    - It prints out tribe stats when a hit is registered. This however seems
    broken in its current state.
      - RukoBlood: fixed!
- Other things I've probably forgotten... have been commented out though.

## TODO (Palkaline)

- fix broken refactors
  - see above...
- add buttplug.io support and/or proper keyframing to animations to resolve motion
  - see TODOs (search 'toy' etc...) //RukoBlood: there is no buttplug TODOs GEEG.
  - I wrote a tiny proof-of-concept (not pictured in this project) that I managed to get working, 
  but it requires manual copying / pasting across each entity. I hate the design of this project.
  I feel like there is a better supported way to implement this... again, see comments...

## TODO (ME)
- Fix girls bugs, including critical ones
- Rename methods and fields in classes
- Fix code that palkaline didn't fixed, because he abandoned this project.
  
## References

- See: https://github.com/hfgufjkgjkfbg/Minecraft-Sex-Mod-Jenny-1.12.2-Forge
- See: https://github.com/ReverseEngineeringEnthusiasts/Jenny-Mod-Fapcraft
