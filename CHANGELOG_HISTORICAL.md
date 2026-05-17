# v0.1.4

## ✍️ Developer Notes
- For help or other questions, concerns, etc. check out our Discord server: https://discord.gg/wp7mvmbkVb

## 🐞 Fixes
- Fixed acid damage configuration missing from properties file.
- Fixed acid dealing damage causing the game to crash.
- [Gigeresque] Fixed gigeresque facehuggers targeting avp aliens.
- [Gigeresque] Fixed avp aliens being affected by gigeresque's DNA disintegration effect.

# v0.1.3

## ✍️ Developer Notes
- For help or other questions, concerns, etc. check out our Discord server: https://discord.gg/wp7mvmbkVb

## ✨ What's New
- AzureLib is no longer required.
- BLib 0.2.0+ is now required.

## 🐞 Fixes
- Fixed aberrant chitin not burning in fire or lava.
- Fixed aberrant resin not burning in fire or lava.
- Fixed plated aberrant chitin not burning in fire or lava.
- Fixed aberrant aliens spawning uncontrollably when avp human module is not present.
- Fixed irradiated aliens spawning uncontrollably when avp human module is not present.

# v0.1.2

## ✍️ Developer Notes
- For help or other questions, concerns, etc. check out our Discord server: https://discord.gg/wp7mvmbkVb

## 🐞 Fixes
- Fixed default alien spawn weights being far too low now that aliens share the 'MONSTERS' mob category with other mobs.
  - For this fix to apply, you will have to delete your config file for `avp_alien`!

# v0.1.1

## ✍️ Developer Notes
- For help or other questions, concerns, etc. check out our Discord server: https://discord.gg/wp7mvmbkVb

## ✨ What's New
- AzureLib 3.1.3 is now required.
- [Fabric] Fabric Loader 0.18.4 is now required.
- [NeoForge] NeoForge 21.1.217 is now required.

# v0.1.0

## ✍️ Developer Notes
- For help or other questions, concerns, etc. check out our Discord server: https://discord.gg/wp7mvmbkVb

## ✨ What's New
- Added predalien.
- Added predalien adolescent.
- Added predalien chestburster.
- Added new textures for all alien spawn egg items.
    - Thanks to Danlogo for contributing the textures!

## ♻️ Changes
- Irradiated chitin armor now glows.
- Plated irradiated chitin armor now glows.
- Queens now move 60% faster than their base speed when chasing targets on land (was previously 10% faster).
- Queens now move 60% faster than their base speed when chasing targets in water (was previously 100% faster).
- Alien spawn eggs in the Alien Spawn Eggs creative mode tab are now sorted based on their lifecycle.
- Updated praetorian model + textures.

## 🐞 Fixes
- Fixed royal ovomorphs using incorrect textures.
- Fixed AVP aliens taking damage from Gigeresque acid.
- Fixed a crash occurring when Gigeresque's surgery kit item is used to remove an AVP chestburster.

## 🛠 Data Pack
- Added `#avp_alien:predalien_adolescents` entity type tag.
- Added `#avp_alien:predalien_chestbursters` entity type tag.
- Added `#avp_alien:predaliens` entity type tag.