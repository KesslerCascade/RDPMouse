package kesslercascade.rdpmouse;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class RDPMouseClient {

    public static final RDPMouseClient INSTANCE = new RDPMouseClient();

    public static final KeyMapping.Category RDPMOUSE_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("rdpmouse", "category"));

    public static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "key.rdpmouse.toggle",
            GLFW.GLFW_KEY_F8,
            RDPMOUSE_CATEGORY
    );

    public static final KeyMapping FREE_MOUSE_KEY = new KeyMapping(
            "key.rdpmouse.freemouse",
            GLFW.GLFW_KEY_LEFT_ALT,
            RDPMOUSE_CATEGORY
    );

    public static final KeyMapping PAN_LEFT = new KeyMapping(
            "key.rdpmouse.pan_left",
            GLFW.GLFW_KEY_LEFT,
            RDPMOUSE_CATEGORY
    );

    public static final KeyMapping PAN_RIGHT = new KeyMapping(
            "key.rdpmouse.pan_right",
            GLFW.GLFW_KEY_RIGHT,
            RDPMOUSE_CATEGORY
    );

    public static final KeyMapping PAN_UP = new KeyMapping(
            "key.rdpmouse.pan_up",
            GLFW.GLFW_KEY_UP,
            RDPMOUSE_CATEGORY
    );

    public static final KeyMapping PAN_DOWN = new KeyMapping(
            "key.rdpmouse.pan_down",
            GLFW.GLFW_KEY_DOWN,
            RDPMOUSE_CATEGORY
    );

    private boolean wasFreeMouse = false;
    private boolean releasedForFree = false;

    private int panLeftTicks, panRightTicks, panUpTicks, panDownTicks;

    private RDPMouseClient() {}

    private static double panSpeed(int ticks) {
        return Math.min(2.0 + ticks * 0.5, 25.0);
    }

    public void onClientTick(Minecraft mc) {
        // Toggle
        while (TOGGLE_KEY.consumeClick()) {
            RDPMouseState.enabled = !RDPMouseState.enabled;
            RDPMouseState.reset();

            long window = mc.getWindow().handle();
            if (mc.mouseHandler.isMouseGrabbed()) {
                if (RDPMouseState.enabled) {
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
                    RDPMouseCursor.clipCursor(window);
                } else {
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                    RDPMouseCursor.releaseClip();
                }
            }

            if (mc.player != null) {
                String key = RDPMouseState.enabled ? "rdpmouse.status.on" : "rdpmouse.status.off";
                mc.player.displayClientMessage(Component.translatable(key), true);
            }
        }

        // Free mouse (hold to release grab)
        boolean holdingFree = FREE_MOUSE_KEY.isDown();
        if (holdingFree && !wasFreeMouse) {
            if (mc.mouseHandler.isMouseGrabbed()) {
                mc.mouseHandler.releaseMouse();
                releasedForFree = true;
            }
        } else if (!holdingFree && wasFreeMouse && releasedForFree) {
            mc.mouseHandler.grabMouse();
            releasedForFree = false;
        }
        wasFreeMouse = holdingFree;

        // Keyboard pan — divide by the vanilla sensitivity factor so pan speed is
        // independent of the mouse sensitivity setting, then apply 2.5x multiplier.
        // Vanilla applies: rotation = accumulatedDX * (sens*0.6+0.2)^3 * 8
        // We pre-divide so that multiplication cancels out.
        double sens = mc.options.sensitivity().get();
        double d = sens * 0.6 + 0.2;
        double sensitivityFactor = d * d * d * 8.0;

        if (PAN_LEFT.isDown()) {
            RDPMouseState.panDX -= panSpeed(panLeftTicks++) * 2.5 / sensitivityFactor;
        } else {
            panLeftTicks = 0;
        }
        if (PAN_RIGHT.isDown()) {
            RDPMouseState.panDX += panSpeed(panRightTicks++) * 2.5 / sensitivityFactor;
        } else {
            panRightTicks = 0;
        }
        if (PAN_UP.isDown()) {
            RDPMouseState.panDY -= panSpeed(panUpTicks++) * 2.5 / sensitivityFactor;
        } else {
            panUpTicks = 0;
        }
        if (PAN_DOWN.isDown()) {
            RDPMouseState.panDY += panSpeed(panDownTicks++) * 2.5 / sensitivityFactor;
        } else {
            panDownTicks = 0;
        }
    }
}
