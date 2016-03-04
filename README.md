# jamaica-gradle-test
Minimal reproducible example demonstrating classloading problems when running JUnit tests from Gradle with JamaicaVM.

Tool versions
---
* JamaicaVM 6.4-beta3 (build 9129), Host: Linux x86-64, Target: PikeOS
* Gradle 2.11
* JUnit 4.12

Steps to reproduce (on Linux x86-64)
---

* Clone repository
* Execute build and run unit tests: ```./gradlew clean test```


Problem description
---

The unit test execution task fails with an exception (apparently related to classloading):

    ** ERROR: initialization failed: Java exception: java.lang.ExceptionInInitializerError: null
    java/lang/ExceptionInInitializerError
        java/lang/Class.desiredAssertionStatus()Z [sourceline:2897]
        java/lang/System.<clinit>()V [sourceline:77]
    Caused by: java/lang/NullPointerException
        java/lang/ClassLoader.defineClass(Ljava/lang/String;[BIILjava/security/ProtectionDomain;)Ljava/lang/Class; [sourceline:1166]
        java/security/SecureClassLoader.defineClass(Ljava/lang/String;[BIILjava/security/CodeSource;)Ljava/lang/Class; [sourceline:142]
        java/net/URLClassLoader.defineClass(Ljava/lang/String;Lsun/misc/Resource;)Ljava/lang/Class; [sourceline:4294967295]
        java/net/URLClassLoader.access$000(Ljava/net/URLClassLoader;Ljava/lang/String;Lsun/misc/Resource;)Ljava/lang/Class; [sourceline:73]
        java/net/URLClassLoader$1.run()Ljava/lang/Class; [sourceline:4294967295]
        java/security/AccessController.doPrivileged(Ljava/security/PrivilegedExceptionAction;Ljava/security/AccessControlContext;)Ljava/lang/Object; [sourceline:4294967295]
        java/net/URLClassLoader.findClass(Ljava/lang/String;)Ljava/lang/Class; [sourceline:4294967295]
        java/lang/ClassLoader$SystemClassLoader.findClass(Ljava/lang/String;)Ljava/lang/Class; [sourceline:4294967295]
        java/lang/ClassLoader.loadClassHelper(Ljava/lang/String;Z)Ljava/lang/Class; [sourceline:4294967295]
        java/lang/ClassLoader.loadClass(Ljava/lang/String;Z)Ljava/lang/Class; [sourceline:4294967295]
        java/lang/ClassLoader$SystemClassLoader.loadClass(Ljava/lang/String;Z)Ljava/lang/Class; [sourceline:4294967295]
        java/lang/ClassLoader.loadClass(Ljava/lang/String;)Ljava/lang/Class; [sourceline:4294967295]
        java/lang/ClassLoader.<clinit>()V [sourceline:236]
        java/lang/Class.desiredAssertionStatus()Z [sourceline:2897]
        java/lang/System.<clinit>()V [sourceline:77]
    ** ERROR: shutdown failed: Java exception: java.lang.InternalError: getRuntime() called before java.lang.Runtime is initialized
    java/lang/InternalError: getRuntime() called before java.lang.Runtime is initialized
        java/lang/Runtime.getRuntime()Ljava/lang/Runtime; [sourceline:317]
        java/lang/Thread.exitVM()V [sourceline:1694]

The possible origin of this exception is
[Gradle's main worker](https://github.com/gradle/gradle/blob/master/subprojects/core/src/main/groovy/org/gradle/process/internal/launcher/GradleWorkerMain.java),
responsible for launching the unit test task from within the main Gradle JVM instance.

Notes
---
The build script assumes that the JamaicaVM compiler and VM binaries reside in the ```/usr/local/jamaica/bin``` directory.
If your local installation uses a different installation path, you change the directory names in the ```gradle.properties``` file.
