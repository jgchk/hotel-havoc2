package com.jgchk.hotelhavoc.ui.game

import com.google.android.gms.games.multiplayer.realtime.Room

data class Message(val message: String, val room: Room, val myParticipantId: String)