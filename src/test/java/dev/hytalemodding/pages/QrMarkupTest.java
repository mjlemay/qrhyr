package dev.hytalemodding.pages;

import io.nayuki.qrcodegen.QrCode;
import org.junit.jupiter.api.Test;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QrMarkupTest {

    private static final int MAX_PANEL = 380;

    @Test
    void rendersOneRowGroupPerPaddedMatrixRow() {
        QrCode qr = QrCode.encodeText("https://example.com", QrCode.Ecc.MEDIUM);
        int total = qr.size + 8; // 4-module quiet zone on each side
        String markup = QrMarkup.render(qr, MAX_PANEL);
        assertEquals(total, countOccurrences(markup, "LayoutMode: Left"));
    }

    @Test
    void blackAreaEqualsDarkModuleCountTimesModuleSize() {
        QrCode qr = QrCode.encodeText("HELLO", QrCode.Ecc.MEDIUM);
        int total = qr.size + 8;
        int module = Math.max(2, MAX_PANEL / total);

        int darkModules = 0;
        for (int y = 0; y < qr.size; y++) {
            for (int x = 0; x < qr.size; x++) {
                if (qr.getModule(x, y)) darkModules++;
            }
        }

        String markup = QrMarkup.render(qr, MAX_PANEL);
        assertEquals(darkModules * module, sumBlackRunWidths(markup));
    }

    @Test
    void bracesAreBalanced() {
        QrCode qr = QrCode.encodeText("https://hytale.com/", QrCode.Ecc.MEDIUM);
        String markup = QrMarkup.render(qr, MAX_PANEL);
        assertEquals(countOccurrences(markup, "{"), countOccurrences(markup, "}"));
    }

    private static int countOccurrences(String s, String sub) {
        int c = 0, i = 0;
        while ((i = s.indexOf(sub, i)) >= 0) { c++; i += sub.length(); }
        return c;
    }

    private static int sumBlackRunWidths(String markup) {
        Matcher m = Pattern.compile("Width: (\\d+)\\); Background: #000000;").matcher(markup);
        int sum = 0;
        while (m.find()) sum += Integer.parseInt(m.group(1));
        return sum;
    }
}
