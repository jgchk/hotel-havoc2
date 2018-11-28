package com.jgchk.hotelhavoc.ui.game

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.android.gms.games.RealTimeMultiplayerClient
import com.google.android.gms.games.multiplayer.realtime.Room
import com.google.android.gms.games.multiplayer.realtime.RoomConfig
import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.core.extension.Event
import com.jgchk.hotelhavoc.core.extension.observe
import com.jgchk.hotelhavoc.core.extension.viewModel
import com.jgchk.hotelhavoc.core.platform.BaseFragment
import com.jgchk.hotelhavoc.model.RoomController
import com.jgchk.hotelhavoc.model.RoomData
import kotlinx.android.synthetic.main.fragment_game.*
import javax.inject.Inject


class GameFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_game

    private lateinit var gameViewModel: GameViewModel
    @Inject lateinit var ordersAdapter: OrdersAdapter
    private lateinit var multiplayerClient: RealTimeMultiplayerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        gameViewModel = viewModel(viewModelFactory) {
            observe(hand, ::onHandUpdate)
            observe(orders, ::onOrdersUpdate)
            observe(score, ::onScoreUpdate)
            observe(beam, ::onBeamUpdate)
            observe(action, ::onActionUpdate)
            observe(actionProgress, ::onActionProgressUpdate)
            observe(leave, ::onLeaveEvent)
            observe(join, ::onJoinEvent)
            observe(wait, ::onWaitEvent)
            observe(start, ::onStartEvent)
            observe(messages, ::onSendMessage)
        }

        multiplayerClient =
                Games.getRealTimeMultiplayerClient(context!!, GoogleSignIn.getLastSignedInAccount(context)!!)
        gameViewModel.createRoom()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trash_btn.setOnClickListener { gameViewModel.trashHand() }
        orders_view.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        orders_view.adapter = ordersAdapter
    }

    private fun onHandUpdate(orderView: OrderView?) {
        if (orderView == null) {
            ingredient_view.setImageDrawable(null)
            trash_btn.visibility = View.GONE
        } else {
            playSound(R.raw.succ)
            ingredient_view.setImageDrawable(makeHandDrawable(orderView))
            trash_btn.visibility = View.VISIBLE
        }
    }

    private fun makeHandDrawable(orderView: OrderView): Drawable {
        val layerDrawable = LayerDrawable(orderView.drawables.map { resources.getDrawable(it) }.toTypedArray())

        if (orderView.drawables.toSet() == setOf(R.drawable.ic_milk, R.drawable.ic_ice_cream)) {
            layerDrawable.setLayerInset(0, 0, 0, layerDrawable.getDrawable(1).intrinsicWidth, 0)
            layerDrawable.setLayerInset(1, layerDrawable.getDrawable(0).intrinsicWidth, 0, 0, 0)
        } else {
            for (i in 0 until layerDrawable.numberOfLayers) {
                layerDrawable.setLayerInset(i, 0, (layerDrawable.numberOfLayers - i) * 200, 0, i * 200)
            }
        }
        return layerDrawable
    }

    private fun onOrdersUpdate(orders: List<OrderView>?) {
        ordersAdapter.collection = orders.orEmpty()
    }

    private fun onScoreUpdate(score: Int?) {
        val scoreString = (score ?: 0).toString()
        score_view.text = scoreString
    }

    private fun onBeamUpdate(beamMessage: String?) {
        if (beamMessage == null) return
        (activity as GameActivity).setBeamMessage(beamMessage)
    }

    private fun onActionUpdate(actionView: ActionView?) {
        if (actionView == null) return

        val drawable = actionView.drawable?.let { resources.getDrawable(it) }
        action_img.setImageDrawable(drawable)

        action_layout.visibility =
                if (drawable == null) View.GONE
                else View.VISIBLE
    }

    private fun onActionProgressUpdate(progress: Int?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            action_progress.setProgress(progress ?: 0, true)
        } else {
            action_progress.progress = progress ?: 0
        }

        if (progress == 100) {
            playSound(R.raw.ding)
        }
    }

    private fun playSound(sound: Int) {
        val mediaPlayer = MediaPlayer.create(context, sound)
        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
        mediaPlayer.start()
    }

    private fun onLeaveEvent(event: Event<RoomData>?) {
        event?.getContentIfNotHandled()?.let {
            leaveRoom(it.roomConfig, it.room.roomId)
            activity?.finish()
        }
    }

    private fun leaveRoom(roomConfig: RoomConfig, roomId: String) {
        multiplayerClient.leave(roomConfig, roomId)
    }

    private fun onJoinEvent(event: Event<RoomConfig>?) {
        event?.getContentIfNotHandled()?.let {
            multiplayerClient.create(it)
        }
    }

    private fun onWaitEvent(event: Event<Room>?) {
        event?.getContentIfNotHandled()?.let {
            showWaitingRoom(it)
        }
    }

    private fun showWaitingRoom(room: Room) {
        multiplayerClient
            .getWaitingRoomIntent(room, RoomController.MAX_PLAYERS)
            .addOnSuccessListener { startActivityForResult(it, RC_WAITING_ROOM) }
    }

    private fun onStartEvent(event: Event<Boolean>?) {
        event?.getContentIfNotHandled()?.let {
            getParticipantId()
        }
    }

    fun getParticipantId() {
        Games.getPlayersClient(context!!, GoogleSignIn.getLastSignedInAccount(context)!!)
            .currentPlayerId
            .addOnSuccessListener { gameViewModel.setParticipantId(it) }
            .addOnFailureListener { Log.e(TAG, "Failed to get participant id") }
    }

    fun onSendMessage(event: Event<Message>?) {
        event?.getContentIfNotHandled()?.let {
            Log.d(TAG, "Send: ${it.message}")
            sendToAllReliably(it.message.toByteArray(), it.room, it.myParticipantId)
        }
    }

    private val pendingMessages = mutableSetOf<Int>()

    private fun sendToAllReliably(message: ByteArray, room: Room, myParticipantId: String) {
        room.participantIds.forEach {
            if (it != myParticipantId) {
                sendMessage(message, room, it)
            }
        }
    }

    private fun sendMessage(message: ByteArray, room: Room, participantId: String) {
        Log.d(TAG, "Send to $participantId")
        multiplayerClient.sendReliableMessage(
            message,
            room.roomId,
            participantId
        ) { _, tokenId, _ ->
            pendingMessages.remove(tokenId)
        }.addOnCompleteListener {
            pendingMessages.add(it.result!!)
        }
    }

    fun onNfcScan(tag: String) {
        vibrate()
        gameViewModel.onNfcScan(tag)
    }

    fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.getSystemService(Vibrator::class.java)?.vibrate(
                VibrationEffect.createOneShot(
                    150,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            getSystemService(context!!, Vibrator::class.java)?.vibrate(150)
        }
    }

    fun onBeamIngredient() = gameViewModel.onBeamIngredient()

    companion object {
        private val TAG = GameFragment::class.simpleName
        private const val RC_WAITING_ROOM = 15623
    }

}