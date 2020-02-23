package com.tirgei.dexterpermissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {

    private val CONTACTS_PERMISSION_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!contactsPermissionGranted()) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                // Explain to the user why you need this permission. After the user sees why you need it,
                // try to request it again
            } else {
                requestContactsPermission()
            }
        }

        PermissionUtils.requestContactsPermission(this, object : PermissionsCallback {
            override fun onPermissionRequest(granted: Boolean) {
                if (granted) {
                    // User has granted permission
                } else {
                    // User has denied permission
                }
            }
        })

    }

    // Return true if user has granted permission else false
    private fun contactsPermissionGranted(): Boolean {
        return when (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)) {
            PackageManager.PERMISSION_GRANTED -> true
            else -> false
        }
    }

    // Request for permission
    private fun requestContactsPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    // User has granted permission
                    // Proceed and read list of contacts
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    // User previously denied the request, proceed and ask them again

                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    // User has rejected request
                }
            } )
            .check()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CONTACTS_PERMISSION_REQUEST -> {

                // If user cancels request no results are returned
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User has granted access, we can now read list of contacts

                } else {
                    // User has rejected request, request them again?
                }

            }

            else -> {
                // Handle other permission requests
            }
        }

    }
}
