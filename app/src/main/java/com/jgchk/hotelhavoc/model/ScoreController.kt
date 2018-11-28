package com.jgchk.hotelhavoc.model

import android.arch.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScoreController
@Inject constructor() {

    val scoreData = MutableLiveData<Int>()

    var score
        get() = scoreData.value!!
        private set(value) {
            scoreData.value = value
        }

    init {
        score = 0
    }

    fun addScore(amount: Int) {
        score += amount
    }

    fun setScoreFromMessage(score: Int) {
        this.score = score
    }

}