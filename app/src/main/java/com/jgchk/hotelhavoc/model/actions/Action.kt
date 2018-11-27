package com.jgchk.hotelhavoc.model.actions

import android.util.Log

abstract class Action constructor(private val onCompleteListener: OnCompleteListener) {
    abstract val name: String
    abstract val drawable: Int

    abstract fun start()
    abstract fun stop()

    var progress = 0
    fun addProgress(amount: Int) {
        progress += amount
        onCompleteListener.onProgressUpdate(progress)
        Log.d(TAG, "Progress: $progress")
        if (progress >= 100) {
            onComplete()
        }
    }

    open fun onComplete() {
        stop()
        resetProgress()
        onCompleteListener.onComplete()
    }

    fun resetProgress() {
        progress = 0
    }

    companion object {
        private val TAG = Action::class.simpleName
    }
}