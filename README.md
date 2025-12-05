![Uplift](src/main/resources/assets/uplift/uplift_banner.jpg)

# 🛩️ About

**Uplift** is a **Fabric 1.21.10** mod.

It adds a new enchantment for the elytra, which lets you use a variation of the creative mode flying in survival.
My goal for this mod is to add a new "vanilla-like" way to build in an easier manner.

You can get the enchantment by trading with a librarian. The librarian will have a 12.5% chance to sell the Uplift enchant at level 1 for 14-30 emeralds.


# ⚖️ Balancing
The `Uplift Flight Mode` will use one rocket (from anywhere in the players inventory) when initiating flight (double-jump) and every 20 seconds it will use the next. If no rockets are found in the players inventory the flight gets cancelled and the elytra will activate. 
The elytra will take 1 durability damage every 6 seconds, but scales with the unbreaking enchantment.
Currently the player takes no fall damage with an uplift enchanted elytra equipped.

# ⚙️ Settings
In the controls you can set the `Toggle Uplift Flight Mode` setting to any key you wish.

> The default key for toggling the flight mode is `U`.


# ⚒️ In Progress

- [x] Remove sprint from uplift flight mode
- [x] Damage elytra durability 
- [x] Disable upliftFlightMode on init 
- [x] Activate elytra when upflight mode gets deactivated and player in air 
- [x] Don't reset timer on land so last charge can be used fully 
- [x] Randomize villagerTradePrice by a small amount
- [ ] ~~Calculate rocket usage based on distance AND time~~
- [ ] ~~Replace bookTrade instead of replacing paper or bookshelf~~