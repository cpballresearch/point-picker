package com.pointpicker

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import android.graphics.BitmapFactory

class PointPickerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "PointPicker"
    }

    @ReactMethod
    fun getColorFromImage(imagePath: String, x: Int, y: Int, promise: Promise) {
        try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val pixelColor = bitmap.getPixel(x, y)
            val red = (pixelColor shr 16) and 0xff
            val green = (pixelColor shr 8) and 0xff
            val blue = pixelColor and 0xff
            val hexColor = String.format("#%02x%02x%02x", red, green, blue)
            promise.resolve(hexColor)
        } catch (e: Exception) {
            promise.reject("Color extraction error", e)
        }
    }
}
