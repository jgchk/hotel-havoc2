package com.jgchk.hotelhavoc.model

import android.arch.lifecycle.MutableLiveData
import com.jgchk.hotelhavoc.core.extension.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageController
@Inject constructor() {

    val messages = MutableLiveData<Event<String>>()

    fun sendMessage(message: String) {
        messages.value = Event(message)
    }

}