package kesslercascade.rdpmouse;

public final class RDPMouseState {
    private RDPMouseState() {}

    public static volatile boolean enabled = false;

    public static final double UNSET = Double.MIN_VALUE;

    public static volatile double lastX = UNSET;
    public static volatile double lastY = UNSET;

    /** Camera pan deltas injected by keyboard pan keys each tick. */
    public static volatile double panDX = 0;
    public static volatile double panDY = 0;

    public static void reset() {
        lastX = UNSET;
        lastY = UNSET;
    }
}
