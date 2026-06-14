package dev.hytalemodding;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import dev.hytalemodding.commands.ExampleCommand;
import dev.hytalemodding.commands.QrCommand;
import dev.hytalemodding.config.QrhyrConfig;
import dev.hytalemodding.events.ExampleEvent;
import dev.hytalemodding.events.QrEvent;

import javax.annotation.Nonnull;

public class Qrhyr extends JavaPlugin {

    private static Config<QrhyrConfig> config = null;

    public Qrhyr(@Nonnull JavaPluginInit init) {
        super(init);
        config = this.withConfig("qrhyr_config", QrhyrConfig.CODEC);
    }

    @Override
    protected void setup() {
        config.save();
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getCommandRegistry().registerCommand(new QrCommand("qr", "http://hytale.com/"));
        if (getConfig().get().isEnabledWelcomeMessage()) {
            this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);
        }
        if (getConfig().get().isEnabledQrCodes()) {
            this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, QrEvent::onPlayerReady);
        }
    }

    public static Config<QrhyrConfig> getConfig() {
        return config;
    }
}