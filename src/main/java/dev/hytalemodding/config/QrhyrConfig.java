package dev.hytalemodding.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class QrhyrConfig {

    public static final BuilderCodec<QrhyrConfig> CODEC = BuilderCodec.builder(QrhyrConfig.class, QrhyrConfig::new)
            .append(
                new KeyedCodec<>("EnableWelcomeMessage", Codec.BOOLEAN),
                (exConfig, aBoolean, extraInfo) -> exConfig.enabledWelcomeMessage = aBoolean,
                (exConfig, extraInfo) -> exConfig.enabledWelcomeMessage
            )
            .add()
            .append(
                new KeyedCodec<>("EnableQrCodes", Codec.BOOLEAN),
                (exConfig, aBoolean, extraInfo) -> exConfig.enabledQrCodes = aBoolean,
                (exConfig, extraInfo) -> exConfig.enabledQrCodes
            )
            .add()
            .build();

    private boolean enabledWelcomeMessage;
    private boolean enabledQrCodes;

    private QrhyrConfig() {}

    public boolean isEnabledWelcomeMessage() {
        return enabledWelcomeMessage;
    }
    public boolean isEnabledQrCodes() {
        return enabledQrCodes;
    }
}
