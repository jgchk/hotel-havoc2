package com.jgchk.hotelhavoc.model.actions

import android.util.Log
import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.core.exception.Failure
import com.jgchk.hotelhavoc.core.interactor.UseCase
import com.jgchk.hotelhavoc.network.BlendReading
import com.jgchk.hotelhavoc.network.PowerReading
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class Blend
@Inject constructor(
    private val blendReading: BlendReading,
    onCompleteListener: OnCompleteListener
) : Action(onCompleteListener) {

    override val name = "blend"
    override val drawable = R.drawable.ic_blender

    companion object {
        private const val INCREMENT = 500L
        private const val TIMEOUT = 60 * 1000L
        private const val PROGRESS_INCREMENT = 15
        private val TAG = Blend::class.simpleName
    }

    private var run = false

    override fun start() {

        run = true
        resetProgress()

        val stopTime = System.currentTimeMillis() + TIMEOUT

        fixedRateTimer(name, false, 0L, INCREMENT) {
            if (System.currentTimeMillis() >= stopTime)
                run = false

            if (run) {
                blendReading.invoke(UseCase.None()) { it.either(::handleFailure, ::handleBlendReading) }
            } else {
                cancel()
            }
        }
    }

    override fun stop() {
        run = false
    }

    private fun handleFailure(failure: Failure) {
        Log.e(TAG, "Failed to get sensor reading: $failure")
    }

    private fun handleBlendReading(reading: PowerReading) {
        Log.d(TAG, "Sensor reading: $reading")
        if (reading.power > 0) {
            addProgress(PROGRESS_INCREMENT)
        }
    }

}