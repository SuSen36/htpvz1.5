package com.hungteen.pvz.common.network;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.common.network.toclient.*;
import com.hungteen.pvz.common.network.toserver.ClickButtonPacket;
import com.hungteen.pvz.common.network.toserver.ConveyorTakePacket;
import com.hungteen.pvz.common.network.toserver.EntityInteractPacket;
import com.hungteen.pvz.common.network.toserver.PVZMouseScrollPacket;
import com.hungteen.pvz.common.network.toserver.UpdateMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PVZPacketHandler {

	private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(PVZMod.MOD_ID + ":networking");
	private static final String PROTOCOL_VERSION = "1.0";
	private static int id = 0;

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(CHANNEL_NAME)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.simpleChannel();

	public static void init() {
		id = 0;
		CHANNEL.registerMessage(id++, PlayerStatsPacket.class, PlayerStatsPacket::encode, PlayerStatsPacket::new, PlayerStatsPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, OpenGuiPacket.class, OpenGuiPacket::encode, OpenGuiPacket::new, OpenGuiPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, ClickButtonPacket.class, ClickButtonPacket::encode, ClickButtonPacket::new, ClickButtonPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, PlaySoundPacket.class, PlaySoundPacket::encode, PlaySoundPacket::new, PlaySoundPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, OtherStatsPacket.class, OtherStatsPacket::encode, OtherStatsPacket::new, OtherStatsPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, UpdateMotionPacket.class, UpdateMotionPacket::encode, UpdateMotionPacket::new, UpdateMotionPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, EntityInteractPacket.class, EntityInteractPacket::encode, EntityInteractPacket::new, EntityInteractPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, PVZMouseScrollPacket.class, PVZMouseScrollPacket::encode, PVZMouseScrollPacket::new, PVZMouseScrollPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, CardInventoryPacket.class, CardInventoryPacket::encode, CardInventoryPacket::new, CardInventoryPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, PAZStatsPacket.class, PAZStatsPacket::encode, PAZStatsPacket::new, PAZStatsPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, DatapackPacket.class, DatapackPacket::encode, DatapackPacket::new, DatapackPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, PVZFogPacket.class, PVZFogPacket::encode, PVZFogPacket::new, PVZFogPacket::handle);
		CHANNEL.registerMessage(id++, ChallengeBarPacket.class, ChallengeBarPacket::encode, ChallengeBarPacket::new, ChallengeBarPacket::handle);
		CHANNEL.registerMessage(id++, SunLimitPacket.class, SunLimitPacket::encode, SunLimitPacket::new, SunLimitPacket.Handler::onMessage);
		CHANNEL.registerMessage(id++, ConveyorBeltPacket.class, ConveyorBeltPacket::encode, ConveyorBeltPacket::new, ConveyorBeltPacket::handle);
		CHANNEL.registerMessage(id++, ConveyorTakePacket.class, ConveyorTakePacket::encode, ConveyorTakePacket::new, ConveyorTakePacket.Handler::onMessage);
	}

	public static <MSG> void sendToServer(MSG msg) {
		CHANNEL.sendToServer(msg);
	}

	public static <MSG> void sendToClient(ServerPlayer serverPlayer, MSG msg) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
	}

	public static <MSG> void sendToNearByClient(Level level, Vec3 vec, double dis, MSG msg) {
		CHANNEL.send(PacketDistributor.NEAR.with(() ->
				new PacketDistributor.TargetPoint(vec.x, vec.y, vec.z, dis, level.dimension())), msg);
	}

	public static <MSG> void sendToLevel(Level level, MSG msg) {
		CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), msg);
	}

	public static <MSG> void sendToPlayers(MSG msg) {
		CHANNEL.send(PacketDistributor.ALL.noArg(), msg);
	}
}