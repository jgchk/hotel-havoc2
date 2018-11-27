package com.jgchk.hotelhavoc.model.actions

import android.util.Log
import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.core.exception.Failure
import com.jgchk.hotelhavoc.core.interactor.UseCase
import com.jgchk.hotelhavoc.network.AccelerationReading
import com.jgchk.hotelhavoc.network.ChopReading
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.math.abs

class Chop
@Inject constructor(
    private val chopReading: ChopReading,
    onCompleteListener: OnCompleteListener
) : Action(onCompleteListener) {

    override val name = "chop"
    override val drawable = R.drawable.ic_chop

    companion object {
        private const val INCREMENT = 500L
        private const val TIMEOUT = 60 * 1000L
        private const val PROGRESS_INCREMENT = 20
        private const val READING_DIFFERENCE = 100
        private val TAG = Chop::class.simpleName
    }

    private var run = false
    private var lastReading: Double = 0.0

    override fun start() {

        run = true
        resetProgress()

        val stopTime = System.currentTimeMillis() + TIMEOUT

        fixedRateTimer(name, false, 0L, INCREMENT) {
            if (System.currentTimeMillis() >= stopTime)
                run = false

            if (run) {
                chopReading.invoke(UseCase.None()) {
                    it.either(::handleFailure, ::handleChopReading)
                }
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

    private fun handleChopReading(reading: AccelerationReading) {
        Log.d(TAG, "Sensor reading: $reading")
        val magnitude = reading.threeAxis.magnitude()
        val difference = abs(magnitude - lastReading)
        Log.d(TAG, "Difference: $difference")
        if (difference >= READING_DIFFERENCE) {
            addProgress(PROGRESS_INCREMENT)
        }
        lastReading = magnitude
    }

}