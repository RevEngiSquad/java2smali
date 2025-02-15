# java2smali

Fork of https://github.com/izgzhen/java2smali.git, uses `d8` instead of `dx`.

Compile:

    ./gradlew build

Run (in Unix system):

    java -jar java2smali.jar ./examples/example/Example.java

Then you should be able to find `*.smali` files in the `example/` directory.

Another example run with imports:

    APP_CLASSPATH=./examples java -jar java2smali.jar ./examples/example2/Example2.java

Another example with android imports:

    APP_CLASSPATH=~/Library/Android/sdk/platforms/android-28/android.jar java -jar java2smali.jar ./examples/example3/Example3.java

sometimes default `android.jar` is not enough i.e, missing some classes, you can specify your own `android.jar` or give [this](https://github.com/RevEngiSquad/cfr/releases/download/0.153-SNAPSHOT/rt.jar) a try which includes almost all classes.

    APP_CLASSPATH=rt.jar java -jar java2smali.jar ./examples/example3/Example3.java

Note that `Example2.java` import `example.Example`, so you must specify its class path directory.

### Related

https://github.com/Belluxx/Java2Smali.git is another similar project but have issues with single file input.

https://github.com/ollide/intellij-java2smali is a IntelliJ plugin.

https://r8.googlesource.com/r8/+/1.0.21/tools/java2smali.sh is a bash script
that doesn't work off-the-shelf.

The following steps might work as well, as long as the `*.java` file
does not have a package namespace.

```
javac HelloWorld.java
dx --dex --output=classes.dex HelloWorld.class
baksmali classes.dex
```
