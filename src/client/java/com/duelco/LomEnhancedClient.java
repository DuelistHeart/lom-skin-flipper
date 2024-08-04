package com.duelco;

import com.duelco.config.LevelUpMessageConfig;
import com.duelco.config.ModConfig;
import com.duelco.config.SkinFlipperConfig;
import com.duelco.handlers.SkinFlipperHandler;
import com.duelco.handlers.SlashMeContinuesHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LomEnhancedClient implements ClientModInitializer {
	private static KeyBinding skinFlipperToggleKeybind;
	public static final Logger LOGGER = LoggerFactory.getLogger("lom-enhanced-client");
	public static ModConfig config;

	@Override
	public void onInitializeClient() {
		AutoConfig.register(
				ModConfig.class,
				PartitioningSerializer.wrap(JanksonConfigSerializer::new)
		);

		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

//		AutoConfig.register(LevelUpMessageConfig.class, GsonConfigSerializer::new);
//		AutoConfig.register(SkinFlipperConfig.class, GsonConfigSerializer::new);
		registerKeybinds();
		SlashMeContinuesHandler.register();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (skinFlipperToggleKeybind.wasPressed()) {
				SkinFlipperHandler.execute();
			}
		});
	}

	private void registerKeybinds() {
		skinFlipperToggleKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.duelco.flipskin", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_K, // The keycode of the key
				"category.duelco.skinflipper" // The translation key of the keybinding's category.
		));
	}
}