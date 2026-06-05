# AFKTape

Client-side Fabric mod for Minecraft 26.1.x.

## Supported Versions

The published jar is built against Minecraft 26.1.2 and declares support for:

- Minecraft 26.1
- Minecraft 26.1.1
- Minecraft 26.1.2

## Build

```powershell
.\gradlew.bat build
```

The built jar is written to:

```text
build/libs/afktape-1.0.0.jar
```

## Run Clients

```powershell
.\gradlew.bat runClient26_1
.\gradlew.bat runClient26_1_1
.\gradlew.bat runClient26_1_2
```

## Requirements

- Gradle is provided by the wrapper.
- The Gradle JVM must be Java 21 or newer.
- Compilation uses a Java 25 toolchain.
