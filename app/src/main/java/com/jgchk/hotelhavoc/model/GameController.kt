package com.jgchk.hotelhavoc.model

import android.util.Log
import com.jgchk.hotelhavoc.model.actions.OnCompleteListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameController
@Inject constructor(
    private val scoreController: ScoreController,
    private val orderController: OrderController,
    private val handController: HandController,
    private val actionController: ActionController,
    private val nfcController: NfcController,
    private val roomController: RoomController
) : OnCompleteListener {

    override fun onProgressUpdate(progress: Int) {}

    override fun onComplete() {
        Log.d(TAG, "Action complete!")
        handController.doAction()
    }

    val score = scoreController.scoreData
    val hand = handController.handData
    val orders = orderController.ordersData
    val action = actionController.actionData
    val progress = actionController.progressData

    init {
        actionController.onCompleteListener = this
    }

    fun onNfcScan(tag: String) = nfcController.onNfcScan(tag)

    fun onBeamIngredient() = handController.onBeamIngredient()

    fun trashHand() {
        handController.trash()
        actionController.stopAction()
    }

    fun createRoom() = roomController.join()

    fun setParticipantId(playerId: String) = roomController.setParticipantId_(playerId)

    companion object {
        private val TAG = GameController::class.simpleName
    }

}