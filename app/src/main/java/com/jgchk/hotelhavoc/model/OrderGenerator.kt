package com.jgchk.hotelhavoc.model

import com.jgchk.hotelhavoc.model.orders.Order

object OrderGenerator {

    fun randomOrders(num: Int): List<Order> {
        return (0 until num).map { randomOrder() }
    }

    fun randomOrder(): Order {
        val order = Order.orders.random()
        return order.random()
    }

}