package com.example.phoneauthentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class Verify : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var timerExt: CountDownTimerExt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        val repeat = findViewById<Button>(R.id.repeatBtn)
        val timer = findViewById<TextView>(R.id.tv_timer)

        timerExt = object : CountDownTimerExt(TIMER_DURATION, TIMER_INTERVAL) {
            override fun onTimerTick(millisUntilFinished: Long) {
                Log.d("TAG", "onTimerTick $millisUntilFinished")
                timer.text = (millisUntilFinished / TIMER_INTERVAL).toString()
            }

            override fun onTimerFinish() {
                Log.d("TAG", "onTimerFinish")
                timer.text = "0"
            }
        }

        repeat.setOnClickListener {
            timerExt.start()
        }


        auth = FirebaseAuth.getInstance()

        val storedVerificationId = intent.getStringExtra("storedVerificationId")

        val verify = findViewById<Button>(R.id.verifyBtn)
        val otpGiven = findViewById<EditText>(R.id.id_otp)

        verify.setOnClickListener {
            val otp = otpGiven.text.toString().trim()
            if (otp.isNotEmpty()) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this, "Введите код полностью", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(applicationContext, Home::class.java))
                    finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Неверный код, попробуйте еще раз", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    companion object {
        const val TIMER_DURATION = 120000L
        const val TIMER_INTERVAL = 1000L
    }
}
