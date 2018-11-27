package com.jgchk.hotelhavoc

import com.jgchk.hotelhavoc.model.OrderController
import com.jgchk.hotelhavoc.model.OrderGenerator
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class OrderControllerTest {

    @Test
    fun addRandomOrder_isCorrect() {
        val orderController = OrderController(OrderGenerator())
        val size = orderController.orders.size
        orderController.addRandomOrder()
        assertTrue(orderController.orders.size == size + 1)
    }

    @Test
    fun initOrders_isCorrect() {
        val orderController = OrderController(OrderGenerator())
        assertTrue(orderController.orders.size == OrderController.MAX_ORDERS)
    }
}
