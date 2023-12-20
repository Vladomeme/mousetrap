package net.mousetrap.main.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.mousetrap.main.MousetrapClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "setScreen", at = @At(value = "HEAD"))
    private void setScreen$HEAD(Screen screen, CallbackInfo ci) {
        if (screen == null) {
            MousetrapClient.blockCursor(true);
        }
    }
}