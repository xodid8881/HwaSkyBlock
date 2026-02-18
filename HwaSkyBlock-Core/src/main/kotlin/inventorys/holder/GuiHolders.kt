package org.hwabaeg.hwaskyblock.inventorys.holder

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class BuyGuiHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}

class GlobalFragGuiHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}

class GlobalUseGuiHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}

class MenuGuiHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}

class SettingGuiHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}

class SharerGuiHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}

class SharerUseGuiHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}

class GeyserMenuGuiHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}
