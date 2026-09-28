package com.hungteen.pvz.client.events;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.client.ClientProxy;
import com.hungteen.pvz.client.KeyBindRegister;
import com.hungteen.pvz.client.capability.level.ConveyorBeltCapability;
import com.hungteen.pvz.common.entity.plant.explosion.CobCannonEntity;
import com.hungteen.pvz.common.network.PVZPacketHandler;
import com.hungteen.pvz.common.network.toserver.ConveyorTakePacket;
import com.hungteen.pvz.common.network.toserver.EntityInteractPacket;
import com.hungteen.pvz.utils.ConfigUtil;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PVZMod.MOD_ID, value = Dist.CLIENT)
public class PVZInputEvents {

	private static final int SWITCH_NUM = 4;
	public static boolean ShowOverlay = true;
	public static int CurrentResourcePos = 0;
	
	@SubscribeEvent
	public static void onKeyDown(InputEvent.Key ev) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.isWindowActive() && ClientProxy.MC.player != null) {
			/* change display of resource overlay */
			if(KeyBindRegister.SHOW_OVERLAY.consumeClick()) {
				ShowOverlay = ! ShowOverlay;
			}
			
			/* change resource to display */
			if(KeyBindRegister.LEFT_TOGGLE.consumeClick()) {
				changeToggle(- 1);
			}
			if(KeyBindRegister.RIGHT_TOGGLE.consumeClick()) {
				changeToggle(1);
			}
			
			/* switch the selected card on conveyor belt and take it */
			//背包与创造背包设了 passEvents，界面打开时 KeyMapping 仍会记录点击，不排除会误取卡
			final ConveyorBeltCapability belt = ConveyorBeltCapability.getCurrent();
			if(belt != null && belt.isActive() && mc.screen == null) {
				if(KeyBindRegister.UP_TOGGLE.consumeClick()) {
					belt.changeSelection(- 1);
				}
				if(KeyBindRegister.DOWN_TOGGLE.consumeClick()) {
					belt.changeSelection(1);
				}
				if(KeyBindRegister.TAKE_CARD.consumeClick()) {
					PVZPacketHandler.sendToServer(new ConveyorTakePacket(belt.getSelected()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onMouseDown(InputEvent.MouseButton.Pre ev) {
		if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
			if(ClientProxy.MC.player.getVehicle() instanceof CobCannonEntity cob) {
                if(ClientProxy.MC.player.getMainHandItem().isEmpty() && cob.getCornNum() > 0 && ClientProxy.MC.options.keyUse.consumeClick()) {
				    PVZPacketHandler.CHANNEL.sendToServer(new EntityInteractPacket(cob.getId(), 0, 0));
				}
			}
		}
	}
	
	@SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent ev) {
    }
	
	/**
	 * {@link #onKeyDown(InputEvent.Key)}
	 */
	private static void changeToggle(int offset) {
		int result = (CurrentResourcePos + offset + SWITCH_NUM) % SWITCH_NUM;
		int cnt = 0;//avoid endless loop.
		while(! checkCurrentPos(result)) {
			result = (result + offset + 3) % 3;
			if(++ cnt >= SWITCH_NUM + 1) {
				break;
			}
		}
		CurrentResourcePos = result;
	}
	
	private static boolean checkCurrentPos(int pos) {
		return pos == 0 ? ConfigUtil.renderSunBar() :
			pos == 1 ? ConfigUtil.renderMoneyBar() :
			pos == 2 ? ConfigUtil.renderGemBar() :
            pos == 3 && ConfigUtil.renderTreeLevel();
	}
	
}