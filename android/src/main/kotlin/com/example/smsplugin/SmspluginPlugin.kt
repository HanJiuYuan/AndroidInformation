package com.example.smsplugin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.Telephony
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import android.Manifest
import android.app.DownloadManager
import android.location.LocationListener
import android.os.*
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import kotlin.concurrent.thread
import android.os.BatteryManager
import android.content.IntentFilter
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

class SmspluginPlugin: FlutterPlugin, MethodCallHandler,LocationListener, ActivityAware {
  private lateinit var channel: MethodChannel
  private lateinit var context: Context
  private var locationManager: LocationManager? = null
  private var location: Location? = null
  private var activity: Activity? = null
  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "my_plugin")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  @RequiresApi(Build.VERSION_CODES.KITKAT)
  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    val a = activity!!
    when (call.method) {
      "getSmsList" -> {
        val smsList = getSmsList()
        result.success(smsList)
      }
      "getCallLogList" -> {
        val callLogList = getCallLogList()
        result.success(callLogList)
      }
      "getContacts" -> {
        val contacts = getContacts()
        result.success(contacts)
      }
      "getInstalledApps" -> {
        val installedApps = getInstalledApps()
        result.success(installedApps)
      }
      "getUpdateTime" -> {
        val deviceInfo = getUpdateTime()
        result.success(deviceInfo)
      }
      "getDownloadedFilesCount" -> {
        val deviceInfo = getDownloadedFilesCount()
        result.success(deviceInfo)
      }
      "getAlVideoPhotograph" -> {
        val deviceInfo = getAlVideoPhotograph()
        result.success(deviceInfo)
      }
      "getGaId" -> thread {
        try {
          val id = AdvertisingIdClient.getAdvertisingIdInfo(a).id
          a.runOnUiThread {
            result.success(id)
          }
        } catch (e: Exception) {
          a.runOnUiThread {
            e.javaClass.canonicalName?.let { result.success("null") }
          }
        }
      }
      "getRomInfo" -> {
        val deviceInfo = getRomInfo()
        result.success(deviceInfo)
      }
      "getLocation" -> {
        val deviceInfo = getLocation()
        result.success(deviceInfo)
      }
      "getBattery"->{
        val deviceInfo = getBattery()
        result.success(deviceInfo)
      }
      "getRamInfo"->{
        val deviceInfo = getRamInfo()
        result.success(deviceInfo)
      }
      "isEmulator"->{
        val deviceInfo = isEmulator()
        result.success(deviceInfo)
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  @SuppressLint("Range")
  private fun getSmsList(): List<Map<String, Any>> {
    val smsList = mutableListOf<Map<String, Any>>()
    val resolver: ContentResolver = context.contentResolver
    val uri = Telephony.Sms.CONTENT_URI
    val projection = arrayOf(
      Telephony.Sms._ID,
      Telephony.Sms.ADDRESS,
      Telephony.Sms.BODY,
      Telephony.Sms.DATE,
      Telephony.Sms.TYPE,
      Telephony.Sms.READ
    )
    val cursor: Cursor? = resolver.query(uri, projection, null, null, null)
    if (cursor != null && cursor.moveToFirst()) {
      do {
        val id = cursor.getLong(cursor.getColumnIndex(Telephony.Sms._ID))
        val address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS))
        val body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY))
        val date = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE))
        val type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE))
        var read = cursor.getString(cursor.getColumnIndex(Telephony.Sms.READ))
        val sms = mapOf(
          "id" to id,
          "address" to address,
          "body" to body,
          "date" to date,
          "type" to type,
          "read" to read
        )
        smsList.add(sms)
      } while (cursor.moveToNext())
      cursor.close()
    }
    return smsList
  }

  @SuppressLint("Range")
  private fun getCallLogList(): List<Map<String, Any>> {
    val callLogList = mutableListOf<Map<String, Any>>()
    val resolver: ContentResolver = context.contentResolver
    val uri = CallLog.Calls.CONTENT_URI
    val projection = arrayOf(
      CallLog.Calls._ID,
      CallLog.Calls.NUMBER,
      CallLog.Calls.DATE,
      CallLog.Calls.DURATION,
      CallLog.Calls.TYPE,
      CallLog.Calls.CACHED_NAME,
    )
    val cursor: Cursor? = resolver.query(uri, projection, null, null, null)
    if (cursor != null && cursor.moveToFirst()) {
      do {
        val id = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID))
        val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
        val date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
        val duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
        var type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))
        val name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
        // 判断通话类型
        if (type == CallLog.Calls.INCOMING_TYPE) {
          // 呼入
          type=1
        } else if (type == CallLog.Calls.OUTGOING_TYPE) {
          // 呼出
          type=2
        }
        val callLog = mapOf(
          "id" to id,
          "number" to number,
          "date" to date,
          "duration" to duration,
          "type" to type,
          "name" to name
        )
        callLogList.add(callLog)
      } while (cursor.moveToNext())
      cursor.close()
    }
    return callLogList
  }

  @SuppressLint("Range")
  private fun getContacts(): List<Map<String, Any>> {
    val contacts = mutableListOf<Map<String, Any>>()
    val contentResolver: ContentResolver = context.contentResolver
    val cursor: Cursor? = contentResolver.query(
      ContactsContract.Contacts.CONTENT_URI,
      null,
      null,
      null,
      null
    )

    if (cursor != null && cursor.moveToFirst()) {
      do {
        val contact = mutableMapOf<String, Any>()
        val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
        contact["name"] = name

        val phoneCursor: Cursor? = contentResolver.query(
          ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
          null,
          ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
          arrayOf(id),
          null
        )

        if (phoneCursor != null && phoneCursor.moveToFirst()) {
          val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
          contact["phoneNumber"] = phoneNumber
        }

        phoneCursor?.close()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val creationDate = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP))
        val creationDateMillis = creationDate.toLongOrNull() ?: 0
        val creationDateFormatted = dateFormat.format(Date(creationDateMillis))
        contact["creationDate"] = creationDateMillis

        val modificationDate = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP))
        val modificationDateMillis = modificationDate.toLongOrNull() ?: 0
        val modificationDateFormatted = dateFormat.format(Date(modificationDateMillis))
        contact["modificationDate"] = modificationDateMillis

        contacts.add(contact)
      } while (cursor.moveToNext())
    }

    cursor?.close()

    return contacts
  }


  @SuppressLint("Range")
  //获取应用列表
  private fun getInstalledApps(): List<Map<String, Any>>  {
    val installedApps = mutableListOf<Map<String, Any>>()
    val packageManager = context.packageManager
    val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
    for (packageInfo in packages) {
      val appInfo = packageInfo.applicationInfo
      val app = mutableMapOf<String, Any>()
      app["appType"] = appInfo.flags and ApplicationInfo.FLAG_SYSTEM
      app["name"] = appInfo.loadLabel(packageManager).toString()
      app["packageName"] = appInfo.packageName
      app["versionName"] = packageInfo.versionName
      app["versionCode"] = packageInfo.versionCode
      app["updateTime"] = packageInfo.lastUpdateTime
      app["installTime"] = packageInfo.firstInstallTime
      app["appSize"] = getAppSize(packageName = appInfo.packageName,context)
      installedApps.add(app)
    }
    return installedApps
  }

  //获取文件内存大小
  private fun getAppSize(packageName: String, context: Context): String {
    val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
    val appSize = File(packageInfo.applicationInfo.publicSourceDir).length()
    return formatFileSize(appSize)
  }

  ///计算文件内存大小
  private fun formatFileSize(size: Long): String {
    if (size <= 0) {
      return "0B"
    }
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (kotlin.math.log10(size.toDouble()) / kotlin.math.log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
  }

  @SuppressLint("Range")
  //获取上次开机时间
  private fun getUpdateTime(): Long {
    return SystemClock.elapsedRealtime()
  }

  @SuppressLint("Range")
  //获取下载文件数量
  private fun getDownloadedFilesCount(): Int {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query()
    query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)
    val cursor = downloadManager.query(query)
    return  cursor.count
  }
  //获取手机内部视频以及照片
  private fun getAlVideoPhotograph(): Map<String, Any> {
    val photos = ArrayList<String>()
    val resolver: ContentResolver = context.contentResolver
    val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor? = resolver.query(uri, projection, null, null, null)
    if (cursor != null) {
      while (cursor.moveToNext()) {
        val path: String = cursor.getString(0)
        photos.add(path)
      }
      cursor.close()
    }
    var count = 0
    val videoProjection = arrayOf(
      MediaStore.Video.Media._ID,
      MediaStore.Video.Media.DISPLAY_NAME,
      MediaStore.Video.Media.DATE_ADDED,
      MediaStore.Video.Media.DATA,
      MediaStore.Video.Media.MIME_TYPE
    )
    val videoSelection = "${MediaStore.Video.Media.MIME_TYPE}=? or ${MediaStore.Video.Media.MIME_TYPE}=?"
    val videoSelectionArgs = arrayOf("video/mp4", "video/3gp")
    val videoCursor = resolver.query(
      MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
      videoProjection,
      videoSelection,
      videoSelectionArgs,
      null
    )
    videoCursor?.let {
      count+= it.count
      it.close()
    }
    return mapOf(
      "photos" to photos,
      "count" to count
    )
  }
  @SuppressLint("Range")
  private fun getRomInfo(): List<Map<String, Any>> {
    val installedApps = mutableListOf<Map<String, Any>>()
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)
    val map = mutableMapOf<String, Long>()
    map["totalMem"] = memoryInfo.totalMem
    map["availMem"] = memoryInfo.availMem
    map["threshold"] = memoryInfo.threshold
    installedApps.add(map)
    return installedApps
  }
  @SuppressLint("Range")
  private fun getLocation(): Location? {
    locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    val isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

    if (!isGPSEnabled && !isNetworkEnabled) {
      return null
    } else {
      if (ActivityCompat.checkSelfPermission(
          context,
          Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
          context,
          Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        return null
      }
      if (isNetworkEnabled) {
        locationManager?.requestLocationUpdates(
          LocationManager.NETWORK_PROVIDER,
          0L,
          0f,
          this
        )
        location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
      }
      if (isGPSEnabled) {
        locationManager?.requestLocationUpdates(
          LocationManager.GPS_PROVIDER,
          0L,
          0f,
          this
        )
        location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
      }
    }
    return location
  }
  //获取内部储存
  private fun getRamInfo(): Map<String, Long> {
    val stat = StatFs(Environment.getDataDirectory().path)
    val blockSize = stat.blockSizeLong
    val totalBlocks = stat.blockCountLong
    val availableBlocks = stat.availableBlocksLong
    val freeBlocks = totalBlocks - availableBlocks
    val availableSize = availableBlocks * blockSize
    val freeSize = freeBlocks * blockSize
    return mapOf(
      "availableSize" to availableSize,
      "freeSize" to freeSize
    )
  }
  //获取电池状态
  private fun getBattery(): Map<String, Any>  {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    var isCharging = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    return mapOf(
      "batteryLevel" to batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY),
      "pct" to (level * 100 / scale.toFloat()).toInt(),
      "isCharging" to isCharging
    )
  }
  private fun isEmulator(): Boolean {
    return Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86")
  }

  override fun onLocationChanged(location: Location) {
    this.location = location
  }

  @Deprecated("Deprecated in Java")
  override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {}

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {}
  override fun onDetachedFromActivity() {}


}

