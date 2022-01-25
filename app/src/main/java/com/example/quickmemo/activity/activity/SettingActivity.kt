package com.example.quickmemo.activity.activity

import android.content.DialogInterface
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.example.quickmemo.R
import com.example.quickmemo.activity.util.BiometricManager
import com.example.quickmemo.activity.util.Pref
import com.example.quickmemo.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity(), Pref.OnDataChanged {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPreference()

        binding.apply {
            chkBiometric.setOnClickListener {
                if((it as CompoundButton).isChecked) {
                    AlertDialog.Builder(this@SettingActivity)
                        .setMessage("생체인식을 사용하시겠습니까?")
                        .setPositiveButton("사용") { _, _ ->
                            if(BiometricManager.canUseFingerPrint(this@SettingActivity)) {
                                BiometricManager.getPrompt(
                                    this@SettingActivity,
                                    object : BiometricPrompt.AuthenticationCallback() {
                                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                            super.onAuthenticationSucceeded(result)
                                            Pref.getInstance(this@SettingActivity)?.setValueWithCallback(Pref.PREF_BIOMETRIC, true, this@SettingActivity)
                                        }
                                    })
                                    .authenticate(BiometricManager.getPromptInfo())
                            }else { chkBiometric.run { isChecked = !isChecked } }
                        }
                        .setNegativeButton(getString(R.string.str_cancel_button_text)) { _, _ ->
                            chkBiometric.run { isChecked = !isChecked }
                        }
                        .setCancelable(false)
                        .show()
                }
                else {
                    AlertDialog.Builder(this@SettingActivity)
                        .setMessage(getString(R.string.str_request_biometric_remove))
                        .setPositiveButton("해제") { _, _ ->
                            BiometricManager.getPrompt(
                                this@SettingActivity,
                                object : BiometricPrompt.AuthenticationCallback() {
                                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                        super.onAuthenticationSucceeded(result)
                                        Pref.getInstance(this@SettingActivity)?.removeValueWithCallback(Pref.PREF_BIOMETRIC, this@SettingActivity)
                                    }
                                })
                                .authenticate(
                                    BiometricManager.getPromptInfo(
                                        "생체 인식",
                                        "생체 인식을 진행해주세요"
                                    )
                                )
                        }
                        .setNegativeButton(getString(R.string.str_cancel_button_text)) { dialogInterface: DialogInterface, i: Int ->
                            chkBiometric.run { isChecked = !isChecked }
                        }
                        .setCancelable(false)
                        .show()
                }
            }
            chkBiometric.setOnCheckedChangeListener { _, checked ->
                // no-op
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun checkPreference() {
        Pref.getInstance(this)?.let {
            binding.chkBiometric.isChecked = it.getBoolean(Pref.PREF_BIOMETRIC)
        }
    }

    override fun onDataChanged(id: String?) {
        Toast.makeText(this, getString(R.string.str_biometric_add_success), Toast.LENGTH_SHORT).show()
    }

    override fun onDateDeleted(id: String?) {
        Toast.makeText(this, getString(R.string.str_biometric_remove_success), Toast.LENGTH_SHORT).show()
    }
}