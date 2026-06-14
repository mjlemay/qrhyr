package dev.hytalemodding.pages;

import io.nayuki.qrcodegen.QrCode;

public final class QrMarkup {

    private static final int QUIET = 4;       // standard QR quiet zone, in modules
    private static final int MIN_MODULE = 2;  // px per module; floor for scannability

    private QrMarkup() {
    }

    public static String render(QrCode qr, int maxPanelPx) {
        int total = qr.size + 2 * QUIET;
        int module = Math.max(MIN_MODULE, maxPanelPx / total);
        int gridPx = total * module;

        StringBuilder sb = new StringBuilder(4096);
        sb.append("Group {\n");
        sb.append("    LayoutMode: Top;\n");
        sb.append("    Anchor: (Width: ").append(gridPx).append(", Height: ").append(gridPx).append(");\n");

        for (int y = 0; y < total; y++) {
            sb.append("    Group { LayoutMode: Left; Anchor: (Width: ").append(gridPx)
              .append(", Height: ").append(module).append(");");
            int x = 0;
            while (x < total) {
                boolean dark = isDark(qr, x, y);
                int run = 1;
                while (x + run < total && isDark(qr, x + run, y) == dark) {
                    run++;
                }
                int width = run * module;
                if (dark) {
                    sb.append(" Group { Anchor: (Width: ").append(width).append("); Background: #000000; }");
                } else {
                    sb.append(" Group { Anchor: (Width: ").append(width).append("); }");
                }
                x += run;
            }
            sb.append(" }\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    /** True when (x,y) in the padded grid is a dark module; the quiet-zone border is light. */
    private static boolean isDark(QrCode qr, int x, int y) {
        int mx = x - QUIET;
        int my = y - QUIET;
        if (mx < 0 || my < 0 || mx >= qr.size || my >= qr.size) {
            return false;
        }
        return qr.getModule(mx, my);
    }
}
