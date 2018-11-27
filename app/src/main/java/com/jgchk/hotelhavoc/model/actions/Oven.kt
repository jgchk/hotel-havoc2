package com.jgchk.hotelhavoc.model.actions

import android.util.Log
import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.core.exception.Failure
import com.jgchk.hotelhavoc.core.interactor.UseCase
import com.jgchk.hotelhavoc.network.DoorReading
import com.jgchk.hotelhavoc.network.OvenReading
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class Oven
@Inject constructor(private val ovenReading: OvenReading, onCompleteListener: OnCompleteListener) :
    Action(onCompleteListener) {

    override val name = "oven"
    override val drawable = R.drawable.ic_oven

    companion object {
        private const val INCREMENT = 500L
        private const val TIMEOUT = 60 * 1000L
        private val STEPS = listOf("open", "closed", "waitEvent")
        private const val PROGRESS_INCREMENT = 10
        private val TAG = Oven::class.simpleName
    }

    private var run = false
    private var currentStep = 0

    override fun start() {

        run = true
        currentStep = 0
        resetProgress()

        val stopTime = System.currentTimeMillis() + TIMEOUT

        fixedRateTimer(name, false, 0L, INCREMENT) {
            if (System.currentTimeMillis() >= stopTime)
                run = false

            if (run) {
                ovenReading.invoke(UseCase.None()) { it.either(::handleFailure, ::handleOvenReading) }
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

    private fun handleOvenReading(reading: DoorReading) {
        Log.d(TAG, "Sensor reading: $reading")
        Log.d(TAG, "Step: $currentStep, ${STEPS[currentStep]}")
        if (reading.status == STEPS[currentStep]) {
            Log.d(TAG, "Next step")
            currentStep++
        } else if (STEPS[currentStep] == "waitEvent") {
            addProgress(PROGRESS_INCREMENT)
        }
    }

}