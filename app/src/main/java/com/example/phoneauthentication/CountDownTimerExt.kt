package com.example.phoneauthentication

import android.os.CountDownTimer

abstract class CountDownTimerExt(mMillisInFuture: Long, var mInterval: Long) {

    private lateinit var countDownTimer: CountDownTimer
    private var remainingTime: Long = 0


    init {
        this.remainingTime = mMillisInFuture
    }

    @Synchronized
    fun start() {
        countDownTimer = object : CountDownTimer(remainingTime, mInterval) {
            override fun onFinish() {
                onTimerFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                onTimerTick(millisUntilFinished)
            }

        }.apply {
            start()
        }
    }

    abstract fun onTimerTick(millisUntilFinished: Long)
    abstract fun onTimerFinish()
}