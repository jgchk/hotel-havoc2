package com.jgchk.hotelhavoc.model

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.games.GamesCallbackStatusCodes
import com.google.android.gms.games.multiplayer.Participant
import com.google.android.gms.games.multiplayer.realtime.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jgchk.hotelhavoc.core.extension.Event
import com.jgchk.hotelhavoc.model.orders.Order
import com.jgchk.hotelhavoc.ui.game.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomController
@Inject constructor(
    private val orderController: OrderController,
    private val gson: Gson,
    private val messageController: MessageController,
    private val scoreController: ScoreController
) {

    val joinEvent = MutableLiveData<Event<RoomConfig>>()
    val waitEvent = MutableLiveData<Event<Room>>()
    val leaveEvent = MutableLiveData<Event<RoomData>>()
    val startEvent = MutableLiveData<Event<Boolean>>()

    var room: Room? = null
        private set
    var participantId: String? = null
    var hostId: String? = null

    val roomStatusUpdateCallback = object : RoomStatusUpdateCallback() {
        override fun onRoomConnecting(room: Room?) {
            Log.d(TAG, "Connecting to room $room")
        }

        override fun onP2PConnected(participantId: String) {
            Log.d(TAG, "Participant $participantId connected")
        }

        override fun onDisconnectedFromRoom(room: Room?) {
            Log.d(TAG, "Disconnected from room")
            leave()
        }

        override fun onPeerDeclined(room: Room?, peers: MutableList<String>) {
            Log.d(TAG, "Peer $peers declined")
            if (shouldCancelGame()) {
                leave()
            }
        }

        override fun onPeersConnected(room: Room?, peers: MutableList<String>) {
            Log.d(TAG, "Peer $peers connected")
        }

        override fun onPeerInvitedToRoom(room: Room?, peers: MutableList<String>) {
            Log.d(TAG, "Peer $peers invited to room")
        }

        override fun onPeerLeft(room: Room?, peers: MutableList<String>) {
            Log.d(TAG, "Peer $peers left room")
        }

        override fun onRoomAutoMatching(room: Room?) {
            Log.d(TAG, "Automatching for room $room")
        }

        override fun onPeerJoined(room: Room?, peers: MutableList<String>) {
            Log.d(TAG, "Players $peers have joined!")
        }

        override fun onConnectedToRoom(room: Room?) {
            Log.d(TAG, "Connected to room $room")
        }

        override fun onPeersDisconnected(room: Room?, peers: MutableList<String>) {
            Log.d(TAG, "Peers $peers disconnected")
            if (shouldCancelGame()) {
                leave()
            }
        }

        override fun onP2PDisconnected(participantId: String) {
            Log.d(TAG, "Participant $participantId disconnected")
        }

    }

    private fun shouldCancelGame(): Boolean {
        return connectedPlayers().size < MIN_PLAYERS
    }

    private fun connectedPlayers(): List<Participant> {
        return room?.participants?.filter { it.isConnectedToRoom } ?: listOf()
    }

    fun join() {
        joinEvent.value = Event(roomConfig)
    }

    fun leave() {
        leaveEvent.value = Event(RoomData(room!!, roomConfig))
    }

    fun wait_() {
        waitEvent.value = Event(room!!)
    }

    fun start() {
        startEvent.value = Event(true)
    }

    val roomUpdateCallback = object : RoomUpdateCallback() {
        override fun onJoinedRoom(code: Int, room: Room?) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room ${room.roomId} joined.")
            } else {
                Log.w(TAG, "Error joining mRoom: $code")
            }
        }

        override fun onLeftRoom(code: Int, roomId: String) {
            Log.d(TAG, "Left mRoom $roomId")
        }

        override fun onRoomCreated(code: Int, room: Room?) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room ${room.roomId} created.")
                this@RoomController.room = room
                wait_()
            } else {
                Log.w(TAG, "Error creating mRoom: $code")
            }
        }

        override fun onRoomConnected(code: Int, room: Room?) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room ${room.roomId} connected.")
                this@RoomController.room = room
                start()
            } else {
                Log.w(TAG, "Error connecting to mRoom: $code")
            }
        }
    }

    val realTimeMessageListener = OnRealTimeMessageReceivedListener {
        val message = String(it.messageData)
        Log.d(TAG, "Received: $message")
        processMessage(message)
    }

    private fun processMessage(message: String) {
        val parts = message.split('|').map { it.trim() }
        if (parts.isEmpty()) return
        if (parts[0] == "host") {
            val hostId = parts[1]
            Log.d(TAG, "$participantId, $hostId")
            if (participantId == hostId) {
                hostOrders()
            }
        } else if (parts[0] == "orders") {
            orderController.setOrdersFromMessage(processOrdersString(parts[1]))
        } else if (parts[0] == "score") {
            scoreController.setScoreFromMessage(parts[1].toInt())
        }
    }

    private fun makeOrdersString(orders: List<Order>): String {
        return gson.toJson(orders, object : TypeToken<List<Order>>() {}.type)
    }

    private fun processOrdersString(ordersString: String): List<Order> {
        return gson.fromJson(ordersString, object : TypeToken<List<Order>>() {}.type)
    }

    fun setParticipantId_(playerId: String) {
        participantId = room!!.getParticipantId(playerId)
        determineHost()
    }

    fun determineHost() {
        val participantIds = connectedPlayers().map { it.participantId }
        val hostChooserId = participantIds.sorted().first()
        if (participantId == hostChooserId) {
            hostId = participantIds.random()
            if (participantId == hostId) {
                hostOrders()
            } else {
                messageController.sendMessage(
                    Message(
                        "host | $hostId",
                        room!!,
                        participantId!!
                    )
                )
            }
        }
    }

    fun hostOrders() {
        orderController.initOrders()
        sendOrders()
    }

    fun sendOrders() {
        messageController.sendMessage(
            Message(
                "orders | ${makeOrdersString(orderController.orders)}",
                room!!,
                participantId!!
            )
        )
    }

    fun sendScore() {
        messageController.sendMessage(
            Message(
                "score | ${scoreController.score}",
                room!!,
                participantId!!
            )
        )
    }

    val roomConfig: RoomConfig = RoomConfig.builder(roomUpdateCallback)
        .setOnMessageReceivedListener(realTimeMessageListener)
        .setRoomStatusUpdateCallback(roomStatusUpdateCallback)
        .setAutoMatchCriteria(RoomConfig.createAutoMatchCriteria(1, MAX_PLAYERS - 1, 0x0))
        .build()

    companion object {
        private val TAG = RoomController::class.simpleName
        private const val MIN_PLAYERS = 2
        const val MAX_PLAYERS = 8
    }

}