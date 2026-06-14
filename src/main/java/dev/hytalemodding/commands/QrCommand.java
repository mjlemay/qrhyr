package dev.hytalemodding.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.DefaultArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.pages.QrPage;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class QrCommand extends AbstractCommand {

    private final DefaultArg<String> urlArg;

    public QrCommand(String name, String defaultUrl) {
        super(name, "Opens a custom page displaying a QR code for a URL");
        // Declares an optional URL argument so `/qr` (uses the default) and
        // `/qr <url>` both parse. Without this the command accepts 0 arguments,
        // so passing a URL fails with "Expected: 0, actual: 1".
        this.urlArg = withDefaultArg(
                "url",
                "The URL to encode in the QR code",
                ArgTypes.GREEDY_STRING,
                defaultUrl,
                defaultUrl);
    }

    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {
        if (!context.isPlayer()) {
            context.sendMessage(Message.raw("This command can only be used by a player."));
            return CompletableFuture.completedFuture(null);
        }

        String url = context.get(urlArg);

        Ref<EntityStore> ref = context.senderAsPlayerRef();
        Store<EntityStore> store = ref.getStore();

        // execute() runs on an async ForkJoinPool worker, but the entity store
        // and page manager must be touched on the world thread that owns them
        // (otherwise store.assertThread() throws "Assert not in thread!").
        // World implements Executor, so hop onto it before accessing the store.
        World world = store.getExternalData().getWorld();
        world.execute(() -> {
            if (!ref.isValid()) {
                return;
            }
            Player player = store.getComponent(ref, Player.getComponentType());
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());

            if (player != null && playerRef != null) {
                QrPage page = new QrPage(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, url);
                player.getPageManager().openCustomPage(ref, store, page);
            }
        });

        return CompletableFuture.completedFuture(null);
    }

}
