# v0.1.5

## ✍️ Developer Notes
- For help or other questions, concerns, etc. check out our Discord server: https://discord.gg/wp7mvmbkVb

## ☢️ Breaking Changes
- Rewrote the hive system from the ground up to use BLib's faction system:
  - BLib factions are treated as global entities. As a result, hives are no longer bound by dimensions. A drone that is a member of a hive will still be part of its hive even if it crosses over into the Nether, End, etc.
  - BLib's faction system offers certain performance improvements for querying information about entities (such as what faction members are loaded in the world).
  - This rewrite should also fix a bug where a hive can have multiple queens.
  - BLib factions support subfactions - this opens the path for more advanced hive mechanics down the road (super hives, empresses, intra-hive and inter-hive fighting, etc.).
  - BLib factions support reputation. This allows for complex relationships between hives and other factions in the future (hives <-> cultists? ;)), but as of right now reputation is unused. However, addon developers are now able to make use of that mechanic if they wanted to!
  - Removed hive debugging code. The debugging code was outdated with the rewrite and has therefore been removed, including its properties in the alien properties file.

## ♻️ Changes
- Updated praetorian model, textures and animations.
- Updated predalien model, textures and animations.
- Updated chitin models and textures.
- Updated plated chitin models and textures.
- Only ovomorphs and boilers now respond to vibrations, rather than all aliens.
- Only boilers now investigate vibrations and become aggressive from repeated disturbances.

## 🐞 Fixes
- Fixed ovipositors suffocating.
- Fixed facehugger lungs expanding/contracting even when not on a host.
- Fixed gigeresque acid melting through chitin and resin blocks.
- Fixed boilers being added to hive reserves on after exploding.
- Fixed aliens having their old forms added to reserves when evolving into a newer form.
  - This may have resulted in larger-than-expected hives, which increased hive combat difficulty unnecessarily.
- Fixed aliens being added to reserves whenever other mods call `discard()` on them.
  - This may fix bugs with certain mods like `Mob Capturing Tool` or similar.
- Fixed the mod interfering with enderman spawns in The End.
  - Xenomorphs were added to The End's spawn pool on the off chance that they spread to The End. However because Minecraft uses a weighted spawn system, xenomorphs being introduced decreased the chances of endermen spawning. To fix this issue, we've had to remove natural xenomorph spawns from The End's spawn pool.

## 🛠 Data Pack
- Removed `#minecraft:is_end` biome tag from `#avp_alien:has_xenomorphs` biome tag.