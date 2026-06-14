package dev.hytalemodding.pages;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.nayuki.qrcodegen.DataTooLongException;
import io.nayuki.qrcodegen.QrCode;

import javax.annotation.Nonnull;

public class QrPage extends BasicCustomUIPage {

    /** Must match the #QrContainer size in Pages/QrPage.ui so the grid fits centered. */
    private static final int MAX_PANEL_PX = 380;

    private final String url;

    public QrPage(@Nonnull PlayerRef playerRef, @Nonnull CustomPageLifetime lifetime, String url) {
        super(playerRef, lifetime);
        this.url = url != null ? url : "http://hytale.com/";
    }

    @Override
    public void build(UICommandBuilder uiCommandBuilder) {
        // 1) Create the shell (incl. #QrContainer and #Url) BEFORE selecting into it.
        uiCommandBuilder.append("Pages/QrPage.ui");

        // 2) Generate the QR and inject it. Any failure here must NOT escape build(),
        //    or the client disconnects — fall back to a message in the URL label.
        try {
            QrCode qr = QrCode.encodeText(url, QrCode.Ecc.MEDIUM);
            uiCommandBuilder.appendInline("#QrContainer", QrMarkup.render(qr, MAX_PANEL_PX));
            uiCommandBuilder.set("#Url.Text", url);
        } catch (DataTooLongException e) {
            uiCommandBuilder.set("#Url.Text", "URL too long to encode");
        } catch (RuntimeException e) {
            uiCommandBuilder.set("#Url.Text", "Could not generate QR code");
        }
    }
}
