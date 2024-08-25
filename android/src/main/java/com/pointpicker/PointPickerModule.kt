package com.pointpicker

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.content.Context
import android.content.pm.PackageManager
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.database.Cursor
import android.provider.MediaStore
import java.io.File


class PointPickerModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
 private val context: Context = reactContext
  override fun getName(): String {
    return NAME
  }


@ReactMethod
fun getColorFromImage(imagePath: String, x: Int, y: Int, promise: Promise) {
    val activity = currentActivity
    if (activity == null) {
        promise.reject("Activity error", "Current activity is null")
        return
    }

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        promise.reject("Permission error", "Read external storage permission not granted")
        return
    }

    try {
        // Strip the "file://" prefix if it exists
        // val uri: Uri = Uri.parse(imagePath)
        // val filePath: String? = uri.path
          val filePath = if (imagePath.startsWith("file://")) {
            imagePath.substring(7)
        } else {
            imagePath
        }
          Log.d("PointPickerModule", "Processed file path: $filePath")
            // Check if the file exists
        val file = File(filePath)
        if (!file.exists()) {
            promise.reject("File error", "File does not exist at path: $filePath")
            return
        }

        // Optionally add a small delay to ensure the file is fully written
        Thread.sleep(100)

        // Decode the bitmap from the file path
        val bitmap = BitmapFactory.decodeFile(filePath)

        if (bitmap == null) {
            promise.reject("Bitmap error", "Failed to decode image from file path: $filePath")
            return
        }
       Log.d("PointPickerModule", "Bitmap dimensions: ${bitmap.width}x${bitmap.height}")
        if (x < 0 || y < 0 || x >= bitmap.width || y >= bitmap.height) {
            promise.reject("Coordinate error", "Coordinates are out of bounds")
            return
        }

        // Get the pixel color from the bitmap at the specified coordinates
        val pixelColor = bitmap.getPixel(x, y)
        val red = (pixelColor shr 16) and 0xff
        val green = (pixelColor shr 8) and 0xff
        val blue = pixelColor and 0xff
        val hexColor = String.format("#%02x%02x%02x", red, green, blue)

        // Resolve the promise with the hex color
        promise.resolve(hexColor)

    } catch (e: Exception) {
        // Reject the promise with the error
        promise.reject("Color extraction error", e)
    }
}


    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(uri, proj, null, null, null)
            val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(column_index ?: -1)
        } finally {
            cursor?.close()
        }
    }


  companion object {
    const val NAME = "PointPicker"
  }
}
