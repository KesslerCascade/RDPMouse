package kesslercascade.rdpmouse.neoforge;

import kesslercascade.rdpmouse.RDPMouse;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(RDPMouse.MOD_ID)
public final class RDPMouseNeoForge {
    public RDPMouseNeoForge(IEventBus modBus) {
        RDPMouse.init();
    }
}
