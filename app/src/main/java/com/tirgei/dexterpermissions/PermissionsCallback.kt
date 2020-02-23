package com.tirgei.dexterpermissions

interface PermissionsCallback {

    // Pass request granted status i.e true or false
    fun onPermissionRequest(granted: Boolean)

}