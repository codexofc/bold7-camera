package com.light.lightcamera

import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.google.zxing.qrcode.QRCodeWriter
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.nio.ByteBuffer

class BarcodeReaderTest {

    // 3 wide, 2 high, rows padded to 4 bytes: 1 2 3 / 4 5 6.
    private val plane = ByteBuffer.wrap(byteArrayOf(1, 2, 3, 0, 4, 5, 6, 0))

    @Test
    fun upright_turnsThePlaneClockwiseAndDropsThePadding() {
        assertArrayEquals(byteArrayOf(1, 2, 3, 4, 5, 6), BarcodeReader.upright(plane, 4, 3, 2, 0))
        assertArrayEquals(byteArrayOf(4, 1, 5, 2, 6, 3), BarcodeReader.upright(plane, 4, 3, 2, 90))
        assertArrayEquals(byteArrayOf(6, 5, 4, 3, 2, 1), BarcodeReader.upright(plane, 4, 3, 2, 180))
        assertArrayEquals(byteArrayOf(3, 6, 2, 5, 1, 4), BarcodeReader.upright(plane, 4, 3, 2, 270))
    }

    @Test
    fun read_findsAQrCodeInAFrame() {
        val frame = frame(QRCodeWriter().encode("https://example.org", BarcodeFormat.QR_CODE, 200, 200), 640, 480)
        assertEquals("https://example.org", BarcodeReader().read(frame, 640, 640, 480, 90))
    }

    @Test
    fun read_findsABarcodeDrawnUprightOnAFrameTheSensorTurned() {
        // A barcode upright on the screen lies on its side in the sensor's frame (rotation 90):
        // its bars run along the frame's rows, which ZXing reads only once the frame is upright.
        val code = Code128Writer().encode("BOLD7", BarcodeFormat.CODE_128, 300, 80)
        val sideways = ByteBuffer.wrap(ByteArray(640 * 480) { -1 })
        for (y in 0 until code.height) for (x in 0 until code.width) {
            // Turned 90 degrees, the frame's (row r, column c) shows at upright (row c, column
            // 479 - r); the code goes to upright (row y + 280, column x + 90).
            if (code.get(x, y)) sideways.put((479 - x - 90) * 640 + (y + 280), 0)
        }
        assertEquals("BOLD7", BarcodeReader().read(sideways, 640, 640, 480, 90))
    }

    @Test
    fun read_givesNullOnAnEmptyFrame() {
        assertNull(BarcodeReader().read(ByteBuffer.wrap(ByteArray(640 * 480) { -1 }), 640, 640, 480, 90))
    }

    private fun frame(code: com.google.zxing.common.BitMatrix, width: Int, height: Int): ByteBuffer {
        val bytes = ByteArray(width * height) { -1 }
        for (y in 0 until code.height) for (x in 0 until code.width) {
            if (code.get(x, y)) bytes[(y + 100) * width + x + 200] = 0
        }
        return ByteBuffer.wrap(bytes)
    }
}
