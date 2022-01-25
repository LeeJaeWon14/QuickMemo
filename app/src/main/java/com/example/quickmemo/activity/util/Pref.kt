package com.example.quickmemo.activity.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class Pref(private val context : Context) {
    interface OnDataChanged {
        fun onDataChanged(id : String?)
        fun onDateDeleted(id: String?)
    }
    private val PREF_NAME = "PrefOfLee"
    private var preference : SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREF_BIOMETRIC = "BIOMETRIC USE"

        private var instance : Pref? =null
        @Synchronized
        fun getInstance(context: Context) : Pref? {
            if(instance == null)
                instance = Pref(context)

            return instance
        }
    }

    fun getString(id: String?) : String? {
        return preference.getString(id, "")
    }

    fun getBoolean(id: String?) : Boolean {
        return preference.getBoolean(id, false)
    }

    fun setValue(id: String?, value: String) : Boolean {
        return preference.edit()
            .putString(id, value)
            .commit()
    }

    fun setValue(id: String?, value: Boolean) : Boolean {
        return preference.edit()
            .putBoolean(id, value)
            .commit()
    }

    fun removeValue(id: String?) : Boolean {
        return preference.edit()
            .remove(id)
            .commit()
    }

    fun setValueWithCallback(id: String?, value: String, listener: OnDataChanged) : Boolean {
        if(setValue(id, value)) {
            Handler(Looper.getMainLooper()).post(Runnable { listener.onDataChanged(id) })
            Logger.e("SharedPreference >> setValue Success")
            return true
        }
        else {
            if(listener is Activity)
                Toast.makeText(listener, "Preference Error!", Toast.LENGTH_SHORT).show()
            Logger.e("SharedPreference >> setValue Failure")
            return false
        }
    }

    fun setValueWithCallback(id: String?, value: Boolean, listener: OnDataChanged) : Boolean {
        if(setValue(id, value)) {
            Handler(Looper.getMainLooper()).post(Runnable { listener.onDataChanged(id) })
            Logger.e("SharedPreference >> setValue Success")
            return true
        }
        else {
            if(listener is Activity)
                Toast.makeText(listener, "Preference Error!", Toast.LENGTH_SHORT).show()
            Logger.e("SharedPreference >> setValue Failure")
            return false
        }
    }

    fun removeValueWithCallback(id: String?, listener: OnDataChanged) : Boolean {
        if(removeValue(id)) {
            Handler(Looper.getMainLooper()).post(Runnable { listener.onDateDeleted(id) })
            Logger.e("SharedPreference >> removeValue Success")
            return true
        }
        else {
            if(listener is Activity)
                Toast.makeText(listener, "Delete Error!", Toast.LENGTH_SHORT).show()
            Logger.e("SharedPreference >> removeValue Failure")
            return false
        }
    }
}