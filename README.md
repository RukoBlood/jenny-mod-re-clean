**18+ Alert! Bad code Alert!** 

**This project is a fork of https://github.com/palkaline/jenny-mod-re**
---
# Jenny mod reverse engineering (And Cleaning up) (TMC 1.1.0 version)

Created by Palkaline, edited by me. Currently i rename come classes, because half of it are still uses reverse engineered gibberish.

## Fortunate news.
One guy (RealCrystalNight) fully reverse engineered Jenny mod's code.

Go check it out: https://github.com/ReverseEngineeringEnthusiasts/Jenny-Mod-Fapcraft

Also found out that This mod was obfuscated with Zelix KlassMaster 13
## About

Initial goals of this project
- Reverse engineer the extreme obfuscation that has been applied jar-wide
- FOSS!
- Clean up code

## Building and/or running
gradlew build
if successful - gradlew RunClient

## Obfuscation

Fapcraft uses Zelix KlassMaster obfuscation.

Ordered by severity
- exception pass-and-return wrapping
    ```
    try {
        // code ...
    } catch (RuntimeException runtimeException) {
        // see here... exceptions are wrapped, passed, and returned (identity)
        throw a(new RuntimeException(runtimeException));
    }
    
    public static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
    ```

- exception table mashing
    ```
    try {
        try {
            try {
                // ... real code
            } catch (RuntimeException runtimeException) {
                throw a(new RuntimeException(runtimeException));
            }
            // code ... or fallthrough
        } catch (RuntimeException runtimeException) {
            throw a(new RuntimeException(runtimeException));
        }
        // code ... or fallthrough
    } catch (RuntimeException runtimeException) {
        throw a(new RuntimeException(runtimeException));
    }
    ```
- dangling catch blocks
    ```
    int a = 5 + z;
    // some code ...
    catch { // dangling catch without prior try
        throw a.b(new RuntimeException());
    }
    ```
- impossible control flow
    ```
    block71: {
        block73: {
            block75: {
                // some code...
                *** goto label69 *** // impossible to track
            }
        }
    }
    ```
- local reuse
    ```
    Object z = new Integer(5);
    // some code...
    z = new HashMap<Integer, Double>();
    ```
- duplicate identifiers
    ```
    class a {
        int a;
        Aa a;
        A a();
        public class a {
        
        }
    }
    ```
- synthetic(s) <sub>deserves a whole subtopic of its own</sub>
  - generic "fill-all" wrappers
  - inner class helpers
- generic stripping
  - `class A<B>` --> `class A`, across classes, methods, params...
- clashing signature
    ``` 
    int a()
    
    float a()
    
    a a()
    ```
- Other CFR failures not included above...

## Regex

Useful patterns below:

- Generic stripped onMessage repopulation:
  - Find: `((?:public|protected|private)?\s*IMessage\s+onMessage\s*\(\s*)IMessage(\s+\w+\s*,\s*MessageContext\s+\w+\s*\)\s*\{\s*return\s+[\w.]+\s*\(\s*\(\s*)([\w.]+)(\s*\)\s*\w+\s*,\s*\w+\s*\)\s*;\s*\})`
  - Replace: `@Override\n$1$3$2$3$4`
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

## Bugs (Palkaline)

I 110% guarantee there are bugs that were created during the deobfuscation process. Many steps are likely to result in many bugs, with each step compounding bugs before them.

The most likely bugs are:
- Dangling overrides I failed to rename 
  - a to doRender, getResourceLocation, getTexture, getModelFileLocation
  - so, these might fallback to defaults from the parent class.
- Referencing incorrect method, same signature.
  - Static method resolution is strange to me... you learn something new!

##Bugs (ME)

Current state of the mod - я сломаль


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
- Other things I've probably forgotten... have been commented out though.

## TODO (Palkaline)

- fix broken refactors
  - see above...
- add buttplug.io support and/or proper keyframing to animations to resolve motion
  - see TODOs (search 'toy' etc...)
  - I wrote a tiny proof-of-concept (not pictured in this project) that I managed to get working, 
  but it requires manual copying / pasting across each entity. I hate the design of this project.
  I feel like there is a better supported way to implement this... again, see comments...

## TODO (ME)
- Fix girls bugs
- Refactor something
- 
  
## References

- See: https://github.com/hfgufjkgjkfbg/Minecraft-Sex-Mod-Jenny-1.12.2-Forge
