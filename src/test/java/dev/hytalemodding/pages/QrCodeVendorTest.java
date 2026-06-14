package dev.hytalemodding.pages;

import io.nayuki.qrcodegen.QrCode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QrCodeVendorTest {

    @Test
    void encodesTextToAMatrix() {
        QrCode qr = QrCode.encodeText("https://example.com", QrCode.Ecc.MEDIUM);
        assertEquals(25, qr.size);            // version 2 for this input
        assertTrue(qr.getModule(0, 0));       // top-left finder pattern is dark
    }
}
