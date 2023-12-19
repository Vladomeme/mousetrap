package net.mousetrap.main.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow
    private boolean cursorLocked;
    @Final @Shadow
    private MinecraftClient client;
    @Shadow
    private boolean hasResolutionChanged;

    @Inject(method = "lockCursor", at = @At(value = "HEAD"), cancellable = true)
    private void lockCursor(CallbackInfo ci) {
        if (!this.client.isWindowFocused()) {
            return;
        }
        if (this.cursorLocked) {
            return;
        }
        if (!MinecraftClient.IS_SYSTEM_MAC) {
            KeyBinding.updatePressedStates();
        }
        this.cursorLocked = true;
        GLFW.glfwSetInputMode(this.client.getWindow().getHandle(), InputUtil.GLFW_CURSOR, InputUtil.GLFW_CURSOR_DISABLED);
        this.client.setScreen(null);
        this.client.attackCooldown = 10000;
        this.hasResolutionChanged = true;
        ci.cancel();
    }

    @Inject(method = "unlockCursor", at = @At(value = "HEAD"), cancellable = true)
    private void unlockCursor(CallbackInfo ci) {
        if (!this.cursorLocked) {
            return;
        }
        this.cursorLocked = false;
        GLFW.glfwSetInputMode(this.client.getWindow().getHandle(), InputUtil.GLFW_CURSOR, InputUtil.GLFW_CURSOR_NORMAL);
        ci.cancel();
    }
}
