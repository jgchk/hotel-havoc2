package com.jgchk.hotelhavoc.model.orders

import com.jgchk.hotelhavoc.model.ingredients.Ingredient
import kotlin.random.Random

abstract class Order {

    abstract val name: String
    abstract val requiredIngredients: List<Ingredient>
    abstract val score: Int
    val turnInRoom = Random.nextInt(1, 6)

    fun matchesIngredients(ingredients: List<Ingredient>): Boolean {
        if (ingredients.size != requiredIngredients.size) return false
        if (ingredients.toSet() != requiredIngredients.toSet()) return false
        return true
    }

    companion object {
        val orders = listOf(Hamburger, Milkshake, Fries)
    }

}