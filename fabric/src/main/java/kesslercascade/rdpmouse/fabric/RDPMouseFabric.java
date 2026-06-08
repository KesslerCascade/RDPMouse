package kesslercascade.rdpmouse.fabric;

import kesslercascade.rdpmouse.RDPMouse;
import kesslercascade.rdpmouse.RDPMouseClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public final class RDPMouseFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RDPMouse.init();
        KeyBindingHelper.registerKeyBinding(RDPMouseClient.TOGGLE_KEY);
        KeyBindingHelper.registerKeyBinding(RDPMouseClient.FREE_MOUSE_KEY);
        KeyBindingHelper.registerKeyBinding(RDPMouseClient.PAN_LEFT);
        KeyBindingHelper.registerKeyBinding(RDPMouseClient.PAN_RIGHT);
        KeyBindingHelper.registerKeyBinding(RDPMouseClient.PAN_UP);
        KeyBindingHelper.registerKeyBinding(RDPMouseClient.PAN_DOWN);
        ClientTickEvents.END_CLIENT_TICK.register(RDPMouseClient.INSTANCE::onClientTick);
    }
}
