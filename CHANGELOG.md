# v0.1.5

## ✍️ Developer Notes
- For help or other questions, concerns, etc. check out our Discord server: https://discord.gg/wp7mvmbkVb

## ♻️ Changes
- Updated praetorian model, textures and animations.
- Updated predalien model, textures and animations.
- Updated chitin models and textures.
- Updated plated chitin models and textures.
- Only ovomorphs and boilers now respond to vibrations, rather than all aliens.
- Only boilers now investigate vibrations and become aggressive from repeated disturbances.

## 🐞 Fixes
- Fixed ovipositors suffocating.
- Fixed facehugger lungs expanding even when not on a host.
- Fixed gigeresque acid melting through chitin and resin blocks.
- Fixed the mod interfering with enderman spawns in The End.
  - Xenomorphs were added to The End's spawn pool on the off chance that they spread to The End. However because Minecraft uses a weighted spawn system, xenomorphs being introduced decreased the chances of endermen spawning. To fix this issue, we've had to remove natural xenomorph spawns from The End's spawn pool.

## 🛠 Data Pack
- Removed `#minecraft:is_end` biome tag from `#avp_alien:has_xenomorphs` biome tag.