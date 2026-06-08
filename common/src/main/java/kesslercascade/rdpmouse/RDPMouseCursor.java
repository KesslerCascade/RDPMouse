package kesslercascade.rdpmouse;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.JNI;
import org.lwjgl.system.Library;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Platform;
import org.lwjgl.system.SharedLibrary;

public final class RDPMouseCursor {
    private RDPMouseCursor() {}

    private static final long CLIP_CURSOR;

    static {
        long clip = 0L;
        if (Platform.get() == Platform.WINDOWS) {
            try {
                SharedLibrary user32 = Library.loadNative(RDPMouseCursor.class, null, "user32");
                clip = user32.getFunctionAddress("ClipCursor");
            } catch (Exception e) {
                RDPMouse.LOGGER.warn("RDPMouse: could not resolve ClipCursor", e);
            }
        }
        CLIP_CURSOR = clip;
    }

    public static void clipCursor(long window) {
        if (CLIP_CURSOR == 0L) return;
        int[] x = new int[1], y = new int[1], w = new int[1], h = new int[1];
        GLFW.glfwGetWindowPos(window, x, y);
        GLFW.glfwGetWindowSize(window, w, h);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long rect = stack.nmalloc(4, 16);
            MemoryUtil.memPutInt(rect,      x[0]);
            MemoryUtil.memPutInt(rect +  4, y[0]);
            MemoryUtil.memPutInt(rect +  8, x[0] + w[0]);
            MemoryUtil.memPutInt(rect + 12, y[0] + h[0]);
            JNI.callPP(rect, CLIP_CURSOR);
        }
    }

    public static void releaseClip() {
        if (CLIP_CURSOR == 0L) return;
        JNI.callPP(0L, CLIP_CURSOR);
    }
}
