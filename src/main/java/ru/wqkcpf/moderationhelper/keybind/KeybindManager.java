package ru.wqkcpf.moderationhelper.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import ru.wqkcpf.moderationhelper.ModerationHelperClient;

public class KeybindManager {
    private KeyBinding statsKey;
    private KeyBinding stopObsKey;

    private static final KeyBinding.Category CATEGORY =
            KeyBinding.Category.create(Identifier.of(ModerationHelperClient.MOD_ID, "main"));

    public void register() {
        statsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.moderation_helper_gui.open_stats",
                GLFW.GLFW_KEY_H,
                CATEGORY
        ));

        stopObsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.moderation_helper_gui.stop_obs",
                GLFW.GLFW_KEY_G,
                CATEGORY
        ));
    }

    public void tick(MinecraftClient client) {
        while (statsKey.wasPressed()) {
            ModerationHelperClient.openStatsScreen();
        }

        while (stopObsKey.wasPressed()) {
            // Если открыт чат, G не должен останавливать запись OBS.
            if (ModerationHelperClient.shouldIgnoreStopKeyBecauseChatOpen(client)) {
                continue;
            }

            ModerationHelperClient.stopCheckRecording("клавиша G");
        }
    }
}
