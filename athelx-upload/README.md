# FramePilot — Fabric 1.21.11

A small client-side performance diagnostics starter mod for Minecraft Java 1.21.11.
It measures recent frame time and displays a compact FPS/frame-time overlay. It does
not claim to force a 60/40 GPU/CPU split or magically move Minecraft's game logic to
the GPU. This first version is deliberately conservative; optimization changes should
be based on measurements.

## Requirements
- Minecraft Java Edition 1.21.11
- Java Development Kit (JDK) 21
- Fabric Loader and Fabric API for 1.21.11
- Gradle (or add the official Gradle wrapper from a trusted Gradle installation)

## Build on Windows
Open PowerShell in this folder and run:

    gradle build

The output JAR should appear in `build/libs/`. This source project has not been
compiled in this environment, so treat the first build as a verification step.
If Gradle reports a dependency/version resolution error, check the official Fabric
version page and update `gradle.properties` / the Loom plugin version.

## Install
Copy the built `framepilot-0.1.0.jar` to `%appdata%\.minecraft\mods`.
Install Fabric Loader and the matching Fabric API first.

## What this version does
- Tracks frame time over a rolling sample window.
- Shows an on-screen diagnostic overlay toggled with F8.
- Adds very little work: samples a timestamp per rendered frame and computes stats
  periodically rather than allocating objects every frame.

## What it does not do yet
- It does not offload world simulation, entities, or chunk generation to the GPU.
- It does not promise higher FPS on every computer.
- It does not change graphics settings automatically.

Next steps should be driven by profiling results and compatibility testing.


## Simplified Windows build
Double-click `BUILD.bat`. It checks for Java and Gradle, then runs the build. It does not install software automatically. If either tool is missing, follow the official link printed in the window.
