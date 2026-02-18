package addon.dailymission.gui

import addon.dailymission.mission.MissionDifficulty
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class MissionMainHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}

class MissionDetailHolder(val difficulty: MissionDifficulty) : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}
