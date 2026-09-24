package com.hungteen.pvz.client;

import com.hungteen.pvz.PVZMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = PVZMod.MOD_ID)
public class KeyBindRegister {

	public static final KeyMapping SHOW_OVERLAY = new KeyMapping("key.pvz.show_overlay", 261, "key.categories.pvz");
	public static final KeyMapping UP_TOGGLE = new KeyMapping("key.pvz.up_toggle", 265, "key.categories.pvz");
	public static final KeyMapping DOWN_TOGGLE = new KeyMapping("key.pvz.down_toggle", 264, "key.categories.pvz");
	public static final KeyMapping LEFT_TOGGLE = new KeyMapping("key.pvz.left_toggle", 263, "key.categories.pvz");
	public static final KeyMapping RIGHT_TOGGLE = new KeyMapping("key.pvz.right_toggle", 262, "key.categories.pvz");
	public static final KeyMapping TAKE_CARD = new KeyMapping("key.pvz.take_card", 79, "key.categories.pvz");
//	public static final KeyMapping SHIFT = new KeyMapping("key.pvz.shift", 340, "key.categories.pvz");
	
	@SubscribeEvent
	public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
		event.register(SHOW_OVERLAY);
		event.register(UP_TOGGLE);
		event.register(DOWN_TOGGLE);
		event.register(LEFT_TOGGLE);
		event.register(RIGHT_TOGGLE);
		event.register(TAKE_CARD);
	}
	
	/**
	 * {@link ClientProxy#setUpClient()}
	 */
	public static void init() {
	}
	
}