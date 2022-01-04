package com.example.quickmemo.activity.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.quickmemo.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            chkBiometric.setOnCheckedChangeListener { compoundButton, checked ->
                if(checked) {
                    AlertDialog.Builder(this@SettingActivity)
                        .setMessage("생체인식을 사용하시겠습니까?")
                        .setPositiveButton("사용") { _, _ ->
                            val executor = ContextCompat.getMainExecutor(this@SettingActivity)
                            val biometricPrompt = BiometricPrompt(
                                this@SettingActivity,
                                executor,
                                object : BiometricPrompt.AuthenticationCallback() {
                                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                        super.onAuthenticationSucceeded(result)
                                        Toast.makeText(this@SettingActivity, "지문인식 성공", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onAuthenticationError(
                                        errorCode: Int,
                                        errString: CharSequence
                                    ) {
                                        super.onAuthenticationError(errorCode, errString)
                                        Toast.makeText(this@SettingActivity, "onAuthenticationError", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onAuthenticationFailed() {
                                        super.onAuthenticationFailed()
                                        Toast.makeText(this@SettingActivity, "onAuthenticationFailed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )

                            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                                .setTitle("생체 인식")
                                .setSubtitle("지문 인증을 해주세요")
                                .setNegativeButtonText("취소")
                                .setDeviceCredentialAllowed(false)
                                .build()
                        }
                        .setNegativeButton("취소", null)
                        .show()
                }
                else {
                    AlertDialog.Builder(this@SettingActivity)
                        .setMessage("생체인식을 해제하시겠습니까?")
                        .setPositiveButton("해제", null)
                        .setNegativeButton("취소", null)
                        .show()
                }
            }
        }
    }
}