package com.jgchk.hotelhavoc.model

import android.arch.lifecycle.MutableLiveData
import com.jgchk.hotelhavoc.model.actions.Action
import com.jgchk.hotelhavoc.model.actions.OnCompleteListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionController
@Inject constructor() : OnCompleteListener {

    val actionData = MutableLiveData<Action>()
    val progressData = MutableLiveData<Int>()

    private var action
        get() = actionData.value
        set(value) {
            actionData.value = value
        }

    private var progress
        get() = progressData.value
        set(value) {
            progressData.value = value
        }

    fun startAction(newAction: Action) {
        if (newAction == action) return

        action?.stop()
        action = newAction
        action?.start()
    }

    fun stopAction() {
        action?.stop()
        action = null
    }

    var onCompleteListener: OnCompleteListener? = null

    override fun onComplete() {
        action = null
        progress = 0
        onCompleteListener?.onComplete()
    }

    override fun onProgressUpdate(progress: Int) {
        this@ActionController.progress = progress
    }

    companion object {
        private val TAG = ActionController::class.simpleName
    }

}
