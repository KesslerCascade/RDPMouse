package kesslercascade.rdpmouse.neoforge.client;

import kesslercascade.rdpmouse.RDPMouse;
import kesslercascade.rdpmouse.RDPMouseClient;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = RDPMouse.MOD_ID, dist = Dist.CLIENT)
public final class RDPMouseNeoForgeClient {

    public RDPMouseNeoForgeClient(IEventBus modBus) {
        modBus.addListener(this::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(RDPMouseClient.TOGGLE_KEY);
        event.register(RDPMouseClient.FREE_MOUSE_KEY);
        event.register(RDPMouseClient.PAN_LEFT);
        event.register(RDPMouseClient.PAN_RIGHT);
        event.register(RDPMouseClient.PAN_UP);
        event.register(RDPMouseClient.PAN_DOWN);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        RDPMouseClient.INSTANCE.onClientTick(Minecraft.getInstance());
    }
}
