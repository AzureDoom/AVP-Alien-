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

## ✨ What's New
- Added an advancement for leading a raid to a hive of a different xenomorph variant.
- Added an advancement for being hunted by raids from two different xenomorph variants at the same time.
- Added an advancement for defeating a xenomorph raid.
- Added an advancement for killing a harbinger.
- Added an advancement for destroying a xenomorph lineage.
- Added blood loss mob effect.
- Added raw scourge jelly item.
- Added scourge jelly block.
- Added scourge mob effect.
- Added a new set of xenomorphs... the SCOURGE xenomorphs:
  - Added chrysalis.
    - Can roll around in a ball to cross distances quickly.
    - While rolled into a ball, is immune to any projectile attacks except fire and explosives.
  - Added razor claw.
    - Applies a temporarily debuff effect called 'Blood Loss' to entities.
      - When entities are attacked while Blood Loss is active, their maximum health gets reduced by whatever damage they take.
      - For example, if your health is 20/20 and you get attacked for 2 damage, your health is now 18/18 instead of 18/20.
  - Added carrier.
    - Facehuggers can latch onto carriers. While latched on, facehuggers are protected from damage.
    - If attacking a viable host and carrying a facehugger, throws a facehugger at the host to infect it.
    - On death, all carried facehuggers launch from its body in random direction.
  - Added ravager.
    - Basic attacks ignore armor.
    - Has a one-hit kill attack that, if landed, kills anything smaller than the ravager (including players).
  - Added harbinger.
    - Deadliest of the scourge xenomorphs.
    - Has a passive effect that buffs nearby xenomorphs to have 15% attack speed and 15% movement speed.
    - Invulnerable to nearly all damage, including caseless and heavy bullets.
    - Can be damaged with fire.
      - Fire allows the harbinger to be damaged by heavier firepower (caseless/heavy bullets).
    - Can be damaged with explosives.
    - Has a 2nd phase when severely damaged:
      - Tendrils become damaged enough that the harbinger can fling acid around everywhere.
      - Moves 10% faster.
  - All scourge xenomorphs also have respective variant forms (aberrant, nether and irradiated).

## ♻️ Changes
- Updated praetorian model, textures and animations.
- Updated predalien model, textures and animations.
- Updated spitter model and textures.
- Updated warrior model and textures.
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
- Added `#avp_alien:carriers` entity type tag.
- Added `#avp_alien:chrysalises` entity type tag.
- Added `#avp_alien:harbingers` entity type tag.
- Added `#avp_alien:ravagers` entity type tag.
- Added `#avp_alien:razor_claws` entity type tag.
- Added `#avp_alien:scourge_aliens` entity type tag.
- Removed `#minecraft:is_end` biome tag from `#avp_alien:has_xenomorphs` biome tag.
