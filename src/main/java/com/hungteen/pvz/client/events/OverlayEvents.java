package com.hungteen.pvz.client.events;

import com.hungteen.pvz.PVZConfig;
import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.client.events.handler.PVZOverlayHandler;
import com.hungteen.pvz.common.entity.plant.explosion.CobCannonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PVZMod.MOD_ID, value = Dist.CLIENT)
public class OverlayEvents {

	private static final Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public static void onPostRenderOverlay(RenderGuiEvent.Post ev) {
		final PoseStack stack = ev.getPoseStack();
		final int width = ev.getWindow().getGuiScaledWidth();
		final int height = ev.getWindow().getGuiScaledHeight();
		if (! canRender() || mc.options.hideGui) {
			return;
		}
		/* no opened gui */
		if (mc.screen == null && PVZInputEvents.ShowOverlay) {
			/* render resources on left upper corner */
			if(! mc.options.renderDebug) {
				PVZOverlayHandler.renderResources(stack, width, height);
			}

			/* render conveyor belt on left side */
			PVZOverlayHandler.renderConveyorBelt(stack, width, height);

			/* render plant food on left lower corner */
			if (PVZConfig.CLIENT_CONFIG.OverlaySettings.RenderPlantFoodBar.get()) {
				PVZOverlayHandler.renderPlantFood(stack, width, height);
			}

			/* render invasion bar on right lower corner */
			if(PVZConfig.CLIENT_CONFIG.OverlaySettings.RenderInvasionProgress.get()) {
				PVZOverlayHandler.renderInvasionProgress(stack, width, height);
				PVZOverlayHandler.renderMission(stack, width, height);
			}

			if(mc.player.getVehicle() instanceof CobCannonEntity cob) {
                if(cob.getCornNum() > 0) {
					PVZOverlayHandler.renderTargetAim(stack, width, height);
				}
				PVZOverlayHandler.renderCobReload(stack, width, height);
			}

			/* render card slots on left side */
//			PVZOverlayHandler.drawCardInventory(mc.player, stack, width, height);
		}
	}

	private static boolean canRender() {
		return mc.player != null && ! mc.player.isSpectator();
	}
	
}