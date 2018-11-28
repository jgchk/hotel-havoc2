package com.jgchk.hotelhavoc.model

import android.arch.lifecycle.MutableLiveData
import com.jgchk.hotelhavoc.core.extension.Event
import com.jgchk.hotelhavoc.ui.game.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageController
@Inject constructor() {

    val messages = MutableLiveData<Event<Message>>()

    fun sendMessage(message: Message) {
        messages.value = Event(message)
    }

}