package addon.dailymission.listener

import addon.dailymission.logic.MissionProgressProcessor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.RecipeChoice
import kotlin.collections.iterator

class CraftMissionListener(
    private val processor: MissionProgressProcessor
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onCraft(e: CraftItemEvent) {
        val player = e.whoClicked as? Player ?: return
        val result = e.currentItem ?: return
        val amount = estimateCraftAmount(e, result)
        processor.onCraft(player, result, amount)
    }

    private fun estimateCraftAmount(e: CraftItemEvent, result: ItemStack): Int {
        val base = result.amount
        if (!e.isShiftClick) return base
        val inv = e.inventory as? CraftingInventory ?: return base
        val maxCrafts = maxCrafts(inv, e.recipe) ?: 1
        return base * maxCrafts
    }

    private fun maxCrafts(inv: CraftingInventory, recipe: Recipe?): Int? {
        val matrix = inv.matrix
        if (recipe is ShapedRecipe) {
            val counts = mutableMapOf<Char, Int>()
            for (row in recipe.shape) {
                for (ch in row.toCharArray()) {
                    if (ch == ' ') continue
                    counts[ch] = (counts[ch] ?: 0) + 1
                }
            }
            var max = Int.MAX_VALUE
            for ((ch, need) in counts) {
                val choice = recipe.choiceMap[ch] ?: continue
                val available = countMatching(matrix, choice)
                max = minOf(max, available / need)
            }
            return max
        }

        if (recipe is ShapelessRecipe) {
            val choices = recipe.choiceList
            val counts = mutableMapOf<RecipeChoice, Int>()
            for (choice in choices) {
                counts[choice] = (counts[choice] ?: 0) + 1
            }
            var max = Int.MAX_VALUE
            for ((choice, need) in counts) {
                val available = countMatching(matrix, choice)
                max = minOf(max, available / need)
            }
            return max
        }

        return null
    }

    private fun countMatching(matrix: Array<ItemStack?>, choice: RecipeChoice): Int {
        var total = 0
        for (item in matrix) {
            if (item == null) continue
            if (choice.test(item)) {
                total += item.amount
            }
        }
        return total
    }
}
