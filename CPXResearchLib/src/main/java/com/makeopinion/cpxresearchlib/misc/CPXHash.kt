package com.makeopinion.cpxresearchlib.misc

import android.content.Context
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.math.BigInteger
import java.security.MessageDigest

class CPXHash {
    companion object {
        fun md5(input: String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }

        /*fun checkPlayServices(context: Context): Boolean {

            val apiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
            val resultCode: Int = apiAvailability.isGooglePlayServicesAvailable(context)
            if (resultCode != ConnectionResult.SUCCESS) {
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    Log.i("CPX", "This device is not supported: ${apiAvailability.getErrorString(resultCode)}")
                } else {
                    Log.i("CPX", "This device is not supported.")
                    //finish()
                }
                return false
            }
            Log.i("CPX", "This device is supported.")
            return true
        }*/
    }
}