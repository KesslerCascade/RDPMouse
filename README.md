# RDPMouse

Minecraft uses raw relative mouse input, which Windows blocks over Remote Desktop. The result is a camera that spins uncontrollably the moment you try to look around. This has been a known bug since 2016 - [MC-107122](https://bugs.mojang.com/browse/MC-107122) and [MC-126875](https://bugs.mojang.com/browse/MC-126875) (open since 2018) - with no fix from Mojang.

If you've ever wanted to check in on a running game from another room, or do mod development and testing remotely on a machine with a better GPU, this mod makes that possible.

## How it works

RDPMouse replaces raw mouse input with absolute cursor position tracking, which RDP does support. It's not perfect. RDP has inherent latency and the cursor can reach the edge of the window and lose focus, but the game becomes controllable.

## Usage

Press **F8** to toggle RDP Mode on or off. A message will confirm the current state.

When RDP Mode is active:
- Mouse movement controls the camera normally (within the window bounds)
- If the camera stops turning, the cursor has hit the edge of the window. Hold **Alt** to release the cursor, recenter it, then release Alt to resume
- **Arrow keys** can also be used to turn the camera, as an alternative to the mouse

**Tip:** Turn mouse sensitivity up significantly in Minecraft's settings. RDP compresses cursor movement, so higher sensitivity helps compensate.

## Key Bindings

| Key | Action |
|-----|--------|
| F8 | Toggle RDP Mode |
| Alt (hold) | Release cursor to recenter |
| Arrow keys | Pan camera |

All bindings can be changed in Options → Controls → RDP Mouse.

## Compatibility

Available for both Fabric and NeoForge.

This mod is Windows-only in any practical sense. The underlying issue is specific to how Windows RDP handles mouse input. It will load on Linux and Mac but won't do anything useful there.
