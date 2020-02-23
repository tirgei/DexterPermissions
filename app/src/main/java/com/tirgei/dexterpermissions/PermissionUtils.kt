package com.tirgei.dexterpermissions

import android.Manifest
import android.app.Activity
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

object PermissionUtils {

    fun requestContactsPermission(context: Context, callback: PermissionsCallback) {
        requestSinglePermission(context, Manifest.permission.READ_CONTACTS, callback)
    }

    fun requestStoragePermission(context: Context, callback: PermissionsCallback) {
        requestSinglePermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, callback)
    }

    fun requestCameraPermission(context: Context, callback: PermissionsCallback) {
        requestSinglePermission(context, Manifest.permission.CAMERA, callback)
    }

    private fun requestSinglePermission(context: Context, permission: String, callback: PermissionsCallback) {
        Dexter.withActivity(context as Activity) // Cast the context to Activity
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    // User has granted the permission
                    callback.onPermissionRequest(granted = true)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    // User previously denied the permission, request them again
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    // User has denied the permission
                    callback.onPermissionRequest(granted = false)
                }
            })
    }

    private fun requestPhotoCapturePermissions(context: Context, callback: PermissionsCallback) {
        Dexter.withActivity(context as Activity)
            .withPermissions(listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // Check if user has granted all
                    if (report?.areAllPermissionsGranted()!!) {
                        callback.onPermissionRequest(true)
                    } else {
                        callback.onPermissionRequest(false)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    // User has denied a permission, proceed and ask them again
                    token?.continuePermissionRequest()
                }
            })
    }

}