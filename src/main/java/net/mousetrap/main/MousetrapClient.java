package net.mousetrap.main;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MousetrapClient implements ClientModInitializer {

    private static boolean cursorBlocked;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (cursorBlocked) fixState(client);
        });
    }

    private static void fixState(MinecraftClient client) {
        cursorBlocked = false;
        double x = (double) client.getWindow().getWidth() / 2;
        double y = (double) client.getWindow().getHeight() / 2;
        ((MouseAccess) client.mouse).updateCursorPos(client.getWindow().getHandle(), x, y);
        GLFW.glfwSetInputMode(client.getWindow().getHandle(), InputUtil.GLFW_CURSOR, InputUtil.GLFW_CURSOR_NORMAL);
        GLFW.glfwSetCursorPos(client.getWindow().getHandle(), x, y);
        GLFW.glfwSetInputMode(client.getWindow().getHandle(), InputUtil.GLFW_CURSOR, InputUtil.GLFW_CURSOR_DISABLED);
    }

    public static void blockCursor(boolean value) {
        MousetrapClient.cursorBlocked = value;
    }

    public static boolean isCursorUnlocked() {
        return !cursorBlocked;
    }
}
