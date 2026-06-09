package kesslercascade.rdpmouse.mixin;

import com.mojang.blaze3d.platform.Window;
import kesslercascade.rdpmouse.RDPMouseCursor;
import kesslercascade.rdpmouse.RDPMouseState;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Shadow private double accumulatedDX;
    @Shadow private double accumulatedDY;
    @Shadow private boolean mouseGrabbed;

    @Unique private GLFWCursorPosCallback rdpmouse$vanillaCallback;
    @Unique private long rdpmouse$window;

    @Inject(method = "setup", at = @At("RETURN"))
    private void rdpmouse$onSetup(Window window, CallbackInfo ci) {
        rdpmouse$window = window.handle();
        rdpmouse$vanillaCallback = GLFW.glfwSetCursorPosCallback(rdpmouse$window, (win, x, y) -> {
            if (RDPMouseState.enabled && mouseGrabbed) {
                if (RDPMouseState.lastX == RDPMouseState.UNSET) {
                    RDPMouseState.lastX = x;
                    RDPMouseState.lastY = y;
                } else {
                    double dx = x - RDPMouseState.lastX;
                    double dy = y - RDPMouseState.lastY;
                    int[] w = new int[1], h = new int[1];
                    GLFW.glfwGetWindowSize(win, w, h);
                    if (Math.abs(dx) < h[0] / 6.0 && Math.abs(dy) < h[0] / 6.0) {
                        accumulatedDX += dx;
                        accumulatedDY += dy;
                    }
                    RDPMouseState.lastX = x;
                    RDPMouseState.lastY = y;
                }
            } else if (rdpmouse$vanillaCallback != null) {
                rdpmouse$vanillaCallback.invoke(win, x, y);
            }
        });
    }

    @Inject(method = "grabMouse", at = @At("RETURN"))
    private void rdpmouse$onGrabMouse(CallbackInfo ci) {
        if (rdpmouse$window != 0L && RDPMouseState.enabled) {
            RDPMouseState.reset();
            GLFW.glfwSetInputMode(rdpmouse$window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
            RDPMouseCursor.clipCursor(rdpmouse$window);
        }
    }

    @Inject(method = "releaseMouse", at = @At("HEAD"))
    private void rdpmouse$onReleaseMouse(CallbackInfo ci) {
        if (!mouseGrabbed) return;
        RDPMouseState.reset();
        if (rdpmouse$window != 0L && RDPMouseState.enabled) {
            RDPMouseCursor.releaseClip();
        }
    }

    @Inject(method = "handleAccumulatedMovement", at = @At("HEAD"))
    private void rdpmouse$onTick(CallbackInfo ci) {
        if (RDPMouseState.panDX != 0 || RDPMouseState.panDY != 0) {
            accumulatedDX += RDPMouseState.panDX;
            accumulatedDY += RDPMouseState.panDY;
            RDPMouseState.panDX = 0;
            RDPMouseState.panDY = 0;
        }
    }
}
