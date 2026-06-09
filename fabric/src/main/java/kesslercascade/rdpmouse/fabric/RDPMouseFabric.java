package kesslercascade.rdpmouse.fabric;

import kesslercascade.rdpmouse.RDPMouse;
import kesslercascade.rdpmouse.RDPMouseClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public final class RDPMouseFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RDPMouse.init();
        KeyMappingHelper.registerKeyMapping(RDPMouseClient.TOGGLE_KEY);
        KeyMappingHelper.registerKeyMapping(RDPMouseClient.FREE_MOUSE_KEY);
        KeyMappingHelper.registerKeyMapping(RDPMouseClient.PAN_LEFT);
        KeyMappingHelper.registerKeyMapping(RDPMouseClient.PAN_RIGHT);
        KeyMappingHelper.registerKeyMapping(RDPMouseClient.PAN_UP);
        KeyMappingHelper.registerKeyMapping(RDPMouseClient.PAN_DOWN);
        ClientTickEvents.END_CLIENT_TICK.register(RDPMouseClient.INSTANCE::onClientTick);
    }
}
