# 🛩️ About

**Uplift** is a **Fabric 1.21+** mod.

It adds a new enchantment for the elytra, which lets you use a variation of the creative mode flying in survival.
My goal for this mod is to add a new "vanilla-like" way to build in an easier manner.

# ⚙️ Features

### How to get the enchantment
You can get the enchantment by trading with a librarian. The librarian will have a chance to sell the Uplift enchant at level 1. It will cost 22 emeralds for now.


### Balancing
To counteract the huge benefit of creative flying I want to implement some drawbacks.
Possibly increasing the amount of rockets consumed or damaging the elytra.

Right now, it will use one rocket (from anywhere in the players inventory) when initiating flight and every 600 ticks (30 seconds) it will use the next. If no rockets are found in the players inventory the flight gets cancelled.


### Settings
In the controls you can set the "Toggle Uplift Flight Mode" setting to any key you wish.

> The default key for toggling the flight mode is `C`.


### In Progress
- [x] Remove sprint from upflight
- [ ] Calculate rocket usage based on distance AND time
- [ ] Don't reset timer on land so last "charge" can be used fully
- [ ] Randomize VillagerTradePrice by a small amount