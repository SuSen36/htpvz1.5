package com.hungteen.pvz.client.events.handler;

import com.hungteen.pvz.client.ClientProxy;
import com.hungteen.pvz.client.capability.level.ConveyorBeltCapability;
import com.hungteen.pvz.client.events.OverlayEvents;
import com.hungteen.pvz.client.events.PVZInputEvents;
import com.hungteen.pvz.common.capability.player.PlayerDataManager;
import com.hungteen.pvz.common.entity.plant.explosion.CobCannonEntity;
import com.hungteen.pvz.common.world.invasion.InvasionManager;
import com.hungteen.pvz.common.world.invasion.MissionManager;
import com.hungteen.pvz.utils.ConfigUtil;
import com.hungteen.pvz.utils.MathUtil;
import com.hungteen.pvz.utils.PlayerUtil;
import com.hungteen.pvz.utils.StringUtil;
import com.hungteen.pvz.utils.enums.Colors;
import com.hungteen.pvz.utils.enums.Resources;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PVZOverlayHandler {

	private static final ResourceLocation RESOURCE = StringUtil.prefix("textures/gui/overlay/resources.png");
	private static final ResourceLocation FOG = StringUtil.prefix("textures/gui/overlay/fog.png");
	private static final ResourceLocation INVASION = StringUtil.prefix("textures/gui/overlay/invasion.png");
	private static final ResourceLocation TARGET = StringUtil.prefix("textures/gui/overlay/target.png");
	private static final ResourceLocation CONVEYOR = StringUtil.prefix("textures/gui/overlay/conveyor.png");
	private static final int SUN_BAR_W1 = 157;
	private static final int SUN_BAR_H1 = 32;
	private static final int SUN_BAR_W2 = 122;
	private static final int EACH_W = 26;
	private static final int SLOT_SIDE = 22;
	private static final int SLOT_DELAY_CD = 20;
	private static final int TEX_SZ = 256;
	private static final int SlotDelay = 0;
	/* 传送带贴图 64×64：整块带子占 x0-21/y0-42，带面 x3-18，中段 y4-39 为 36px 循环段 */
	private static final int CONVEYOR_TEX_SZ = 64;
	private static final int CONVEYOR_BELT_X = 0;
	private static final int CONVEYOR_BELT_Y = 26;
	private static final int CONVEYOR_BELT_W = 22;
	private static final int CONVEYOR_HEAD_H = 4;
	private static final int CONVEYOR_TAIL_H = 3;
	private static final int CONVEYOR_BAND_V = 4;
	private static final int CONVEYOR_BAND_H = 36;
	private static final int CONVEYOR_CARD_X = CONVEYOR_BELT_X + 3;
	private static final int CONVEYOR_CARD_SIZE = 16;
	private static final int CONVEYOR_SPACING = 17;
	//容量需与服务端 Challenge.CONVEYOR_MAX_CARDS 保持一致（原版传送带固定 10 格）
	private static final int CONVEYOR_SLOTS = 10;
	private static final int CONVEYOR_ENTRY_Y = CONVEYOR_BELT_Y + CONVEYOR_HEAD_H + CONVEYOR_SLOTS * CONVEYOR_SPACING
			+ (CONVEYOR_SPACING - CONVEYOR_CARD_SIZE) / 2;
	private static final int CONVEYOR_HIGHLIGHT = 0xFFFFE24B;

	/**
	 * {@link OverlayEvents#onPostRenderOverlay(net.minecraftforge.client.event.RenderGuiEvent.Post)}
	 */
	public static void renderResources(PoseStack stack, int width, int height) {
		final ConveyorBeltCapability belt = ConveyorBeltCapability.getCurrent();
		//传送带关卡取卡与种植都不消耗阳光，资源栏固定切到金币
		final int pos = belt != null && belt.isActive() ? 1 : PVZInputEvents.CurrentResourcePos;
		if(pos == 0 && ConfigUtil.renderSunBar()) {
			renderSunBar(stack, width, height);
		} else if(pos == 1 && ConfigUtil.renderMoneyBar()) {
			renderMoneyBar(stack, width, height);
		} else if(pos == 2 && ConfigUtil.renderGemBar()) {
			renderGemBar(stack, width, height);
		} else if(pos == 3 && ConfigUtil.renderTreeLevel()) {
			renderTreeLevel(stack, width, height);
		}
	}

	/**
	 * {@link OverlayEvents#onPostRenderOverlay(net.minecraftforge.client.event.RenderGuiEvent.Post)}
	 */
	public static void renderPlantFood(PoseStack stack, int w, int h) {
		final int maxNum = PlayerUtil.getResource(ClientProxy.MC.player, Resources.MAX_ENERGY_NUM);
		int num = PlayerUtil.getResource(ClientProxy.MC.player, Resources.ENERGY_NUM);
		final float sz = 0.5F;
		stack.pushPose();
		RenderSystem.enableBlend();
		stack.scale(sz, sz, 1F);
		bindTexture(RESOURCE);

		/* render head */
		blitTex(stack, 0, h * 2 - SUN_BAR_H1, 0, 64, 34, SUN_BAR_H1);
		if(num > 0) {// light.
			blitTex(stack, 0, h * 2 - SUN_BAR_H1, 162, 45, SUN_BAR_H1, SUN_BAR_H1);
		}

		/* render body */
		int currentX = 35;
		for (int i = 0; i < maxNum; i++) {
			if (num -- > 0) {
				blitTex(stack, currentX, h * 2 - SUN_BAR_H1, 35, 64, 26, SUN_BAR_H1);
			} else {
				blitTex(stack, currentX, h * 2 - SUN_BAR_H1, 61, 64, 26, SUN_BAR_H1);
			}
			currentX += EACH_W;
		}

		/* render tail */
		blitTex(stack, currentX, h * 2 - SUN_BAR_H1, 153, 64, 4, SUN_BAR_H1);

		RenderSystem.disableBlend();
		stack.popPose();
	}

	public static void renderInvasionProgress(PoseStack stack, int w, int h) {
		final PlayerDataManager manager = PlayerUtil.getManager(ClientProxy.MC.player);
		final int count = manager.getInvasion().getTotalWaveCount();
		if(count == 0){
			return;
		}
		stack.pushPose();

		bindTexture(INVASION);

		final float sz = 0.7F;
		stack.scale(sz, sz, sz);

		final int WIDTH = 158, HEIGHT = 21;
		blitTex(stack, (int) (w / sz) - WIDTH, (int)(h / sz) - HEIGHT, 0, 0, WIDTH, HEIGHT);

		final int P_WIDTH = 144, P_HEIGHT = 7;
		final int dayTime = (int)((ClientProxy.MC.level.getDayTime() - InvasionManager.START_TICK  + 2 + 24000) % 24000L);
		final int barLen = MathUtil.getBarLen(dayTime, 24000, P_WIDTH);
		blitTex(stack, (int) (w / sz) - barLen - 7, (int)(h / sz) - HEIGHT + 7, 149 - barLen + 1, 31, barLen, P_HEIGHT);

		for(int i = 0; i < count; ++ i) {
			final int time = (int)((manager.getInvasion().getWaveTime(i) - InvasionManager.START_TICK  + 2 + 24000) % 24000L);
			final int waveLen = MathUtil.getBarLen(time, 24000, P_WIDTH);
			if(time > dayTime) {
				blitTex(stack, (int) (w / sz) - waveLen - 7, (int)(h / sz) - HEIGHT + 4, 1, 49, 14, 11);
			} else {
				if(manager.getInvasion().getWaveTriggered(i)) {
					blitTex(stack, (int) (w / sz) - waveLen - 7, (int)(h / sz) - HEIGHT, 1, 49, 14, 15);
				} else {
					blitTex(stack, (int) (w / sz) - waveLen - 7, (int)(h / sz) - HEIGHT, 1, 65, 14, 15);
				}
			}
		}

		blitTex(stack, (int) (w / sz) - barLen - 11, (int)(h / sz) - HEIGHT + 4, 17, 52, 15, 12);

		stack.popPose();
	}

	public static void renderMission(PoseStack stack, int w, int h) {
		final MissionManager.MissionType type = MissionManager.getPlayerMission(ClientProxy.MC.player);
		if(type == MissionManager.MissionType.EMPTY){
			return;
		}

		stack.pushPose();

		bindTexture(INVASION);

		final int WIDTH = 72, HEIGHT = 24;
		blitTex(stack, w - WIDTH, h - HEIGHT - 16, 88, 48, WIDTH, HEIGHT);

		final int stage = PlayerUtil.getResource(ClientProxy.MC.player, Resources.MISSION_STAGE);
		final int now = PlayerUtil.getResource(ClientProxy.MC.player, Resources.MISSION_VALUE);
		final int need = MissionManager.getRequireMissionValue(type, stage);
		final int barLen = MathUtil.getBarLen(now, need, 66);
		blitTex(stack, w - WIDTH + 3, h - HEIGHT + 3, 91, 75, barLen, 2);

		final String progress = now + "/" + need;

		switch(type) {
		case COLLECT_SUN:
			blitTex(stack, w - WIDTH + 3, h - HEIGHT - 14, 40, 48, 8, 8);
			break;
		case KILL:
		case INSTANT_KILL:
			blitTex(stack, w - WIDTH + 3, h - HEIGHT - 14, 48, 48, 8, 8);
			break;
		default:
			break;
		}

		StringUtil.drawScaledString(stack, ClientProxy.MC.font, Component.translatable("invasion.pvz.mission." + type.toString().toLowerCase(), need).getString(), w - WIDTH + 12, h - HEIGHT - 13, Colors.WHITE, 0.7F);
		StringUtil.drawScaledString(stack, ClientProxy.MC.font, progress, w - WIDTH + 4, h - HEIGHT - 3, Colors.WHITE, 0.6F);

		stack.popPose();
	}

	public static void renderTargetAim(PoseStack stack, int w, int h) {
		stack.pushPose();
		bindTexture(TARGET);
		final int WIDTH = 32, HEIGHT = 32;
		blitTex(stack, (w - WIDTH) / 2, (h - HEIGHT) / 2, 0, 0, WIDTH, HEIGHT);
		stack.popPose();
	}

	/**
	 * 骑行玉米炮时在经验条下方渲染装弹冷却条,UI 形式对齐 htpvz2 机枪射手的过热条。
	 * 有弹药时保持满格,弹药耗尽后按装弹进度增长。
	 * {@link OverlayEvents#onPostRenderOverlay(RenderGuiEvent.Post)}
	 */
	public static void renderCobReload(PoseStack stack, int width, int height) {
		final Player player = ClientProxy.MC.player;
		if(player != null && player.getVehicle() instanceof CobCannonEntity cob) {
			final float progress = cob.getCornNum() > 0 ? 1F : Mth.clamp(cob.getReloadTick() * 1F / cob.getPreCD(), 0F, 1F);
			final int len = (int) (182 * progress);
			final int y = height - 32 + 3;
			stack.pushPose();
			RenderSystem.enableBlend();
			bindTexture(TARGET);
			blitTex(stack, width / 2 - 91, y, 0, 32, 182, 5);
			if(len > 0) {
				blitTex(stack, width / 2 - 91, y, 0, 37, len, 5);
			}
			RenderSystem.disableBlend();
			stack.popPose();
		}
	}

	@SuppressWarnings("deprecation")
	public static void renderFog(PoseStack stack, int w, int h, float dep) {
		stack.pushPose();
		RenderSystem.enableBlend();
		bindTexture(FOG);
		RenderSystem.setShaderColor(1f, 1f, 1f, dep);

		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(0.0D, h, -90.0D).uv(0.0F, 1.0F).endVertex();
		bufferbuilder.vertex(w, h, -90.0D).uv(1.0F, 1.0F).endVertex();
		bufferbuilder.vertex(w, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
		bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
		tessellator.end();

		RenderSystem.disableBlend();
		stack.popPose();
	}

	/**
	 * 屏幕左侧贴边的竖直传送带：头 4px 与尾 3px 固定，中段 36px 按 gameTime 纵向平铺循环。
	 * 卡片滑动位移由入场时刻推算，与带面同为 1px/tick，故两者视觉同步。
	 * {@link OverlayEvents#onPostRenderOverlay(net.minecraftforge.client.event.RenderGuiEvent.Post)}
	 */
	public static void renderConveyorBelt(PoseStack stack, int width, int height) {
		final ConveyorBeltCapability belt = ConveyorBeltCapability.getCurrent();
		if(belt != null && belt.isActive()) {
			final int count = belt.getSize();
			//容量固定为槽位数，带高不随卡片数变化；末尾多留一格作新卡入场区，卡片滑入时不会越出带面
			final int beltH = CONVEYOR_HEAD_H + (CONVEYOR_SLOTS + 1) * CONVEYOR_SPACING + CONVEYOR_TAIL_H;
			final long now = ClientProxy.MC.level.getGameTime();
			final int scroll = (int) (now % CONVEYOR_BAND_H);
			final int bandTop = CONVEYOR_BELT_Y + CONVEYOR_HEAD_H;
			final int bandBottom = CONVEYOR_BELT_Y + beltH - CONVEYOR_TAIL_H;

			stack.pushPose();
			RenderSystem.enableBlend();
			bindTexture(CONVEYOR);
			for(int top = bandTop - scroll; top < bandBottom; top += CONVEYOR_BAND_H) {
				final int from = Math.max(top, bandTop);
				final int to = Math.min(top + CONVEYOR_BAND_H, bandBottom);
				if(to > from) {
					blitTex64(stack, CONVEYOR_BELT_X, from, 0, CONVEYOR_BAND_V + (from - top), CONVEYOR_BELT_W, to - from);
				}
			}
			blitTex64(stack, CONVEYOR_BELT_X, CONVEYOR_BELT_Y, 0, 0, CONVEYOR_BELT_W, CONVEYOR_HEAD_H);
			blitTex64(stack, CONVEYOR_BELT_X, CONVEYOR_BELT_Y + beltH - CONVEYOR_TAIL_H, 0, CONVEYOR_BAND_V + CONVEYOR_BAND_H, CONVEYOR_BELT_W, CONVEYOR_TAIL_H);
			RenderSystem.disableBlend();
			stack.popPose();

			final int selected = belt.getSelected();
			for(int i = 0; i < count; ++ i) {
				final int slotY = CONVEYOR_BELT_Y + CONVEYOR_HEAD_H + i * CONVEYOR_SPACING
						+ (CONVEYOR_SPACING - CONVEYOR_CARD_SIZE) / 2;
				final int cardY = Mth.clamp(CONVEYOR_ENTRY_Y - (int) (now - belt.getEntryTick(i)), slotY, CONVEYOR_ENTRY_Y);
				if(i == selected) {
					GuiComponent.fill(stack, CONVEYOR_CARD_X - 1, cardY - 1, CONVEYOR_CARD_X + CONVEYOR_CARD_SIZE + 1, cardY, CONVEYOR_HIGHLIGHT);
					GuiComponent.fill(stack, CONVEYOR_CARD_X - 1, cardY + CONVEYOR_CARD_SIZE, CONVEYOR_CARD_X + CONVEYOR_CARD_SIZE + 1, cardY + CONVEYOR_CARD_SIZE + 1, CONVEYOR_HIGHLIGHT);
					GuiComponent.fill(stack, CONVEYOR_CARD_X - 1, cardY, CONVEYOR_CARD_X, cardY + CONVEYOR_CARD_SIZE, CONVEYOR_HIGHLIGHT);
					GuiComponent.fill(stack, CONVEYOR_CARD_X + CONVEYOR_CARD_SIZE, cardY, CONVEYOR_CARD_X + CONVEYOR_CARD_SIZE + 1, cardY + CONVEYOR_CARD_SIZE, CONVEYOR_HIGHLIGHT);
				}
				ClientProxy.MC.getItemRenderer().renderAndDecorateItem(belt.getCard(i), CONVEYOR_CARD_X, cardY, 0);
			}
		}
	}

	/**
	 * {@link #renderResources(PoseStack, int, int)}
	 */
	private static void renderSunBar(PoseStack stack, int width, int height) {
		final int max = PlayerUtil.getSunLimit(ClientProxy.MC.player);
		final int now = PlayerUtil.getResource(ClientProxy.MC.player, Resources.SUN_NUM);
		final int len = MathUtil.getBarLen(now, max, SUN_BAR_W2);
		final float sz = 0.7F;
		stack.pushPose();
		RenderSystem.enableBlend();
		stack.scale(sz, sz, 1F);
		bindTexture(RESOURCE);

		blitTex(stack, 0, 0, 0, 0, SUN_BAR_W1, SUN_BAR_H1);
		blitTex(stack, 0, 0, 0, 32, 32 + len, SUN_BAR_H1);

		StringUtil.drawCenteredScaledString(stack, ClientProxy.MC.font, now + "", 95 + 1, 5 + 1, 6698496, 1.5f);
		StringUtil.drawCenteredScaledString(stack, ClientProxy.MC.font, now + "", 95, 5, Colors.WHITE, 1.5f);

		RenderSystem.disableBlend();
		stack.popPose();
	}

	/**
	 * {@link #renderResources(PoseStack, int, int)}
	 */
	public static void renderMoneyBar(PoseStack stack, int width, int height) {
		final int now = PlayerUtil.getResource(ClientProxy.MC.player, Resources.MONEY);
		final float sz = 0.7F;
		stack.pushPose();
		RenderSystem.enableBlend();
		stack.scale(sz, sz, 1F);
		bindTexture(RESOURCE);

		blitTex(stack, 0, 0, 0, 96, SUN_BAR_W1, SUN_BAR_H1);

		StringUtil.drawCenteredScaledString(stack, ClientProxy.MC.font, now + "", 95 + 1, 5 + 1, 3610880, 1.5f);
		StringUtil.drawCenteredScaledString(stack, ClientProxy.MC.font, now + "", 95, 5, Colors.WHITE, 1.5f);

		RenderSystem.disableBlend();
		stack.popPose();
	}

	/**
	 * {@link #renderResources(PoseStack, int, int)}
	 */
	public static void renderGemBar(PoseStack stack, int width, int height) {
		final int now = PlayerUtil.getResource(ClientProxy.MC.player, Resources.GEM_NUM);
		final float sz = 0.7F;
		stack.pushPose();
		RenderSystem.enableBlend();
		stack.scale(sz, sz, 1F);
		bindTexture(RESOURCE);

		blitTex(stack, 0, 0, 0, 128, SUN_BAR_W1, SUN_BAR_H1);
		StringUtil.drawCenteredScaledString(stack, ClientProxy.MC.font, now + "", 95 + 1, 5, 46545, 1.5f);
		StringUtil.drawCenteredScaledString(stack, ClientProxy.MC.font, now + "", 95, 5, Colors.WHITE, 1.5f);

		RenderSystem.disableBlend();
		stack.popPose();
	}

	/**
	 * {@link #renderResources(PoseStack, int, int)}
	 */
	private static void renderTreeLevel(PoseStack stack, int width, int height) {
		final int level = PlayerUtil.getResource(ClientProxy.MC.player, Resources.TREE_LVL);
		final int max = PlayerUtil.getPlayerLevelUpXp(level);
		final int now = PlayerUtil.getResource(ClientProxy.MC.player, Resources.TREE_XP);
		final int len = MathUtil.getBarLen(now, max, 120);
		final float sz = 0.7F;
		stack.pushPose();
		RenderSystem.enableBlend();
		stack.scale(sz, sz, 1F);
		bindTexture(RESOURCE);

		blitTex(stack, 0, 0, 0, 160, 157, 34);
		blitTex(stack, 34, 3, 34, 194, len, 8);

		StringUtil.drawCenteredScaledString(stack, ClientProxy.MC.font, "Lv." + level, 52, 18, Colors.WHITE, 1f);

		RenderSystem.disableBlend();
		stack.popPose();
	}

	/**
	 * 所有 overlay 贴图尺寸 256×256（fog.png 128×128 不走本路径）。
	 */
	private static void blitTex(PoseStack stack, int x, int y, int u, int v, int w, int h) {
		GuiComponent.blit(stack, x, y, 0, (float) u, (float) v, w, h, TEX_SZ, TEX_SZ);
	}

	private static void blitTex64(PoseStack stack, int x, int y, int u, int v, int w, int h) {
		GuiComponent.blit(stack, x, y, 0, (float) u, (float) v, w, h, CONVEYOR_TEX_SZ, CONVEYOR_TEX_SZ);
	}

	private static void bindTexture(ResourceLocation texture) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, texture);
	}

}