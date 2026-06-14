package dev.hytalemodding.pages;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class QrPage extends BasicCustomUIPage {

    private final String url;

    public QrPage(@Nonnull PlayerRef playerRef, @Nonnull CustomPageLifetime lifetime, String url) {
        super(playerRef, lifetime);
        this.url = url != null ? url : "http://hytale.com/";
    }

    @Override
    public void build(UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("Pages/QrPage.ui");
        uiCommandBuilder.set("#Url.Text", url);
    }
}