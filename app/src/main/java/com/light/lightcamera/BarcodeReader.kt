package com.light.lightcamera

import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.ReaderException
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

/** Reads a barcode (QR and the others ZXing knows) from a camera frame. One per thread. */
class BarcodeReader {
    private val reader = MultiFormatReader()

    /**
     * The text of the barcode in the luminance plane [y] (the Y plane of a YUV_420_888 frame,
     * [rowStride] bytes a row), turned upright by [rotation] degrees clockwise first, as the
     * screen shows it: ZXing reads one-dimensional codes along the rows. Null when none is found.
     */
    fun read(y: ByteBuffer, rowStride: Int, width: Int, height: Int, rotation: Int): String? {
        val upright = upright(y, rowStride, width, height, rotation)
        val (w, h) = if (rotation % 180 == 0) width to height else height to width
        val bitmap = BinaryBitmap(HybridBinarizer(PlanarYUVLuminanceSource(upright, w, h, 0, 0, w, h, false)))
        return try {
            reader.decodeWithState(bitmap).text
        } catch (e: ReaderException) {
            null
        } finally {
            reader.reset()
        }
    }

    companion object {
        /** The plane as width x height packed bytes, turned [rotation] degrees clockwise. */
        fun upright(y: ByteBuffer, rowStride: Int, width: Int, height: Int, rotation: Int): ByteArray {
            val out = ByteArray(width * height)
            for (r in 0 until height) {
                for (c in 0 until width) {
                    val v = y.get(r * rowStride + c)
                    when (rotation) {
                        90 -> out[c * height + (height - 1 - r)] = v
                        180 -> out[(height - 1 - r) * width + (width - 1 - c)] = v
                        270 -> out[(width - 1 - c) * height + r] = v
                        else -> out[r * width + c] = v
                    }
                }
            }
            return out
        }
    }
}
