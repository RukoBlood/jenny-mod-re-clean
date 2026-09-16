# Obfuscation.

Fapcraft uses Zelix KlassMaster obfuscation. Although ZKM can make even more obfuscation like strings, integers and more, Schnurri and trol used only names and flow obfuscation.


Ordered by severity

## ZKM transformations
- exception pass-and-return wrapping
    ```java
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
    ```java
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
    ```java
    int a = 5 + z;
    // some code ...
    catch { // dangling catch without prior try
        throw a.b(new RuntimeException());
    }
    ```
- impossible control flow (ZKM Heavy-duty protection)
    ```java 
    block71: {
        block73: {
            block75: {
                // some code...
                *** goto label69 *** // impossible to track
            }
        }
    }
    ```
  Killer feature of ZKM. You can't handle goto spaghetti manually, but things like ZKM-FlowDeobf and similar tools are designed specifically to deobfuscate flow.

- local reuse
    ```java 
    Object z = new Integer(5);
    // some code...
    z = new HashMap<Integer, Double>();
    ```
- duplicate identifiers
    ```java
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
- clashing signature (IntelliJ IDEA handles clashes great.)
    ```java 
    int a(){}
    
    float a(){}
    
    a a(){}
    ```

## Decompilation bugs

- Inner Classes stripping.
    ```java
    final ClassName this$0;
    static Minecraft access$300(ClassName className) {
        return className.mc;
    }
    ```
  This is not ZKM obfuscation. It's a CFR bug.
  - try-with-resources reconstruction.
    ```java
    try {
      FileWriter writer = new FileWriter(file); //or other things
      Throwable throwable = null;
      try {
          //some code, usually foreach loop with writing things
      } catch (Throwable caughtThrowable) {
          throwable = caughtThrowable;
          throw caughtThrowable;
      } finally {
          if (throwable != null) {
              try {
                  writer.close();
              } catch (Throwable suppressed) {
                  throwable.addSuppressed(suppressed);
              }
          } else {
              writer.close();
          }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    ```
    CFR may incorrectly decompile a try-with-resources statement into an expanded `try/catch/finally` structure with `Throwable` variables, explicit `close()` calls, and `addSuppressed()`. 
    This can be reconstructed back into the original try-with-resources syntax.