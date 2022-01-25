package com.example.quickmemo.activity.util

import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.hardware.fingerprint.FingerprintManager
import android.provider.Settings
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.FragmentActivity
import com.example.quickmemo.R

object BiometricManager {
    fun canUseFingerPrint(context: Context) : Boolean {
        val bioManager = BiometricManager.from(context)
        if(!(context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager).isDeviceSecure) {
            Logger.e("Not use device lock")
            Toast.makeText(context, "기기잠금을 사용해야 생체 인식 사용이 가능합니다.", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
            return false
        }
        when(bioManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> { // 생체 인식 사용가능
                Logger.e("Can use biometric")
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> { // 기기에서 생체 인식을 지원하지 않음
                Logger.e("Not available on this device")
                Toast.makeText(context, context.getString(R.string.str_biometric_error_no_hardware), Toast.LENGTH_SHORT).show()
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> { // 생체 인식을 현재 사용할 수 없음
                Logger.e("Biometric features are currently unavailable ")
                Toast.makeText(context, context.getString(R.string.str_biometric_error_hw_unavailable), Toast.LENGTH_SHORT).show()
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> { // 생체 인식을 설정하지 않음
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                context.startActivity(enrollIntent)
                return false
            }
        }
        return false
    }
    fun getPrompt(activity: FragmentActivity, callback: BiometricPrompt.AuthenticationCallback?) : BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        return BiometricPrompt(
            activity,
            executor,
            callback ?: object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(activity, "생체인식 성공", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(activity, "생체 인식이 필요합니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(activity, "생체인식 실패", Toast.LENGTH_SHORT).show()
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