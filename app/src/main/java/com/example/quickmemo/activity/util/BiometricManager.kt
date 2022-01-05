package com.example.quickmemo.activity.util

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricManager {
    fun getPrompt(activity: FragmentActivity, callback: BiometricPrompt.AuthenticationCallback?) : BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        return BiometricPrompt(
            activity,
            executor,
            callback ?: object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(activity, "지문인식 성공", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(activity, "onAuthenticationError", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(activity, "지문인식 실패", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun getPromptInfo(title: String = "생체 인식", negativeButtonText: String = "취소") : BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setNegativeButtonText(negativeButtonText)
            .setDeviceCredentialAllowed(false)
            .build()
    }
}