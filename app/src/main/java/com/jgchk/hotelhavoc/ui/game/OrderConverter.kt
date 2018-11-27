package com.jgchk.hotelhavoc.ui.game

import com.jgchk.hotelhavoc.model.ingredients.Ingredient
import com.jgchk.hotelhavoc.model.orders.Order

object OrderConverter {

    fun ingredientsToOrderView(ingredients: List<Ingredient>): OrderView {
        val drawables = ingredientsToDrawables(ingredients)
        val strings = ingredientsToStrings(ingredients)
        return OrderView(drawables, strings, 0)

    }

    private fun ingredientsToDrawables(ingredients: List<Ingredient>): List<Int> {
        val unorderedDrawables = mutableListOf<IntArray>()
        ingredients.forEach { ingredient ->
            ingredient.drawables().forEach {
                unorderedDrawables.add(intArrayOf(it.key, it.value))
            }
        }
        unorderedDrawables.sortBy { it[1] }
        return unorderedDrawables.map { it[0] }.toList()
    }

    private fun ingredientsToStrings(ingredients: List<Ingredient>): List<String> {
        return ingredients.map { it.name }
    }

    fun ordersToOrderViews(orders: List<Order>): List<OrderView> {
        return orders.map {
            val drawables = ingredientsToDrawables(it.requiredIngredients)
            val strings = ingredientsToStrings(it.requiredIngredients)
            OrderView(drawables, strings, it.turnInRoom + 200)
        }
    }
}