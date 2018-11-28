package com.jgchk.hotelhavoc.model

import android.arch.lifecycle.MutableLiveData
import com.jgchk.hotelhavoc.model.ingredients.Ingredient
import com.jgchk.hotelhavoc.model.orders.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderController
@Inject constructor(
    private val scoreController: ScoreController
) {

    companion object {
        const val MAX_ORDERS = 3
    }

    val ordersData = MutableLiveData<List<Order>>()

    var orders
        get() = ordersData.value!!
        private set(value) {
            ordersData.value = value
        }

    init {
        orders = emptyList()
        initOrders()
    }

    fun initOrders(): List<Order> {
        orders = OrderGenerator.randomOrders(MAX_ORDERS)
        return orders
    }

    fun addRandomOrder() {
        orders = orders.plus(OrderGenerator.randomOrder())
    }

    fun turnIn(ingredients: List<Ingredient>, roomNum: Int): Boolean {
        val order = matchingOrder(ingredients, roomNum) ?: return false

        scoreController.addScore(order.score)
        orders = orders.minus(order)
        addRandomOrder()
        return true
    }

    private fun matchingOrder(ingredients: List<Ingredient>, roomNum: Int): Order? {
        return orders.find { it.turnInRoom == roomNum && it.matchesIngredients(ingredients) }
    }

    fun setOrdersFromMessage(ordersFromMessage: List<Order>) {
        orders = ordersFromMessage
    }

}