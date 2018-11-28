package com.jgchk.hotelhavoc.ui.game

import android.arch.lifecycle.Transformations
import com.google.gson.Gson
import com.jgchk.hotelhavoc.core.platform.BaseViewModel
import com.jgchk.hotelhavoc.model.GameController
import com.jgchk.hotelhavoc.model.MessageController
import com.jgchk.hotelhavoc.model.RoomController
import com.jgchk.hotelhavoc.model.ingredients.Ingredient
import javax.inject.Inject

class GameViewModel
@Inject constructor(
    private val gameController: GameController,
    private val roomController: RoomController,
    private val gson: Gson,
    private val messageController: MessageController
) : BaseViewModel() {

    val score = gameController.score
    val hand = Transformations.map(gameController.hand) { OrderConverter.ingredientsToOrderView(it) }
    val beam = Transformations.map(gameController.hand) { createBeamIngredientString(it) }
    val orders = Transformations.map(gameController.orders) { OrderConverter.ordersToOrderViews(it) }
    val action = Transformations.map(gameController.action) { ActionConverter.actionToActionView(it) }
    val actionProgress = gameController.progress

    val join = roomController.joinEvent
    val leave = roomController.leaveEvent
    val wait = roomController.waitEvent
    val start = roomController.startEvent

    val messages = messageController.messages

    fun createBeamIngredientString(ingredients: List<Ingredient>): String = gson.toJson(ingredients)

    fun onNfcScan(tag: String) = gameController.onNfcScan(tag)

    fun onBeamIngredient() = gameController.onBeamIngredient()

    fun trashHand() = gameController.trashHand()

    fun createRoom() = gameController.createRoom()

    fun setParticipantId(playerId: String) = gameController.setParticipantId(playerId)

}