package net.Mystery2099.mystmultitoolmod.config.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyModifier;

public class KeyBinds {
    public static final String CATEGORY_MULTITOOLS = "key.categories.mystmultitoolmod";
    public static final KeyMapping RESET_KEY = createKeyMapping("reset", InputConstants.Type.KEYSYM, InputConstants.KEY_R);
    public static final KeyMapping PREVIOUS_MODE_KEY = createKeyMapping("PREVIOUS", InputConstants.Type.KEYSYM, InputConstants.KEY_LEFT);
    public static final KeyMapping NEXT_MODE_KEY = createKeyMapping("next", InputConstants.Type.KEYSYM, InputConstants.KEY_RIGHT);


    private static KeyMapping createKeyMapping(String name, InputConstants.Type keyType, int keyCode) {
        return new KeyMapping("key.mystmultitoolmod."+name, IsHoldingMultiToolKeyConflictContext.INSTANCE, keyType, keyCode, KeyBinds.CATEGORY_MULTITOOLS);
    }

    public static void registerKeys() {
        ClientRegistry.registerKeyBinding(RESET_KEY);
        ClientRegistry.registerKeyBinding(PREVIOUS_MODE_KEY);
        ClientRegistry.registerKeyBinding(NEXT_MODE_KEY);
    }
}
