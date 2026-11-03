# Ender Launcher — Upgrade Plan

This plan separates implemented changes from work that requires broader engineering or external infrastructure.

## Implemented in the current feature branch

- Ender identity and package ID are configured for the Android app.
- The default launcher accent is violet.
- Custom skin and cape imports validate PNG format and supported Minecraft texture dimensions before replacing the active file.
- Imports are staged to a temporary file, limited to 4 MiB, and moved into place only after validation.
- Android CI now caches Gradle and the Android NDK and enables Gradle build cache/parallel execution.

## Version creation and installation

Use the official Zalith Launcher 2 implementation as a technical reference, not as a blind file copy. The version picker should be based on the official Minecraft version manifest and the existing `MinecraftVersions` / `MinecraftOfficialContentManager` data layer already present in this codebase.

Target behavior:
- Release, snapshot, old beta/alpha and April Fools filters.
- Search by exact version ID.
- Pull-to-refresh with visible loading, empty and network-error states.
- Stable keys and lazy rendering for long version lists.
- Version installation through the launcher's existing download/task pipeline, with progress, retry and dependency validation.
- Preserve existing installed profiles and their independent game directories.

The Zalith Launcher 2 source is GPL-3.0. Any code ported from it must retain required copyright/license notices and compatible licensing; prefer adapting concepts and existing shared APIs over copying large UI files.

## Performance and stability backlog

- Profile startup, version-list refresh, skin preview, download throughput and game launch before optimizing.
- Keep file/network work off the Compose main thread.
- Cache manifest and metadata with explicit freshness rules; avoid duplicate concurrent downloads.
- Use lazy lists, stable item keys and lifecycle-aware state collection.
- Validate downloaded files before marking tasks complete; retry transient failures without duplicating progress.
- Add crash-safe import/export and clear error messages.
- Build arm64 debug and release variants and run lint/unit checks before publishing.
- Keep APK size and ZIP size as separate measured outputs; do not claim a 25 MiB package until the produced artifact is measured.

## Public multiplayer server creation (not implemented yet)

A player-hosted Java server reachable by friends over the public internet cannot be guaranteed by removing the LAN/VPN feature alone. Mobile networks and many home routers use carrier-grade NAT or block inbound connections.

A reliable no-VPN flow needs a public relay or hosted server backend:
1. Player chooses Java version, server software, memory limit and world.
2. Ender requests a temporary server instance from a managed backend.
3. Backend starts the server and allocates a public address/port.
4. Friends join using that address; the owner can stop the instance and manage access.
5. Add resource limits, automatic shutdown, abuse protection and clear cost/availability information.

This requires a separately deployed backend and hosting budget/credentials. Until that exists, the app must not display a fake public address or claim that friends can join from anywhere.

## Skin and cape scope

- Local custom PNG skin and cape import is supported by the wardrobe UI.
- This does not automatically upload a cape to Mojang/Microsoft or make it visible on official servers.
- Official account cape changes require a supported account/provider API; local preview and local files are separate from account-wide entitlement.

## Release gate

Do not publish an APK as stable until the CI build succeeds, the APK installs, startup and game launch are tested on a real arm64 Android device, and the skin/cape flows are exercised with valid and invalid PNGs.
