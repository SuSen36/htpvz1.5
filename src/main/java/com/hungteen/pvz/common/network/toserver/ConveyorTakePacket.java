package com.hungteen.pvz.common.network.toserver;

import com.hungteen.pvz.common.world.challenge.Challenge;
import com.hungteen.pvz.common.world.challenge.ChallengeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ConveyorTakePacket {

	private final int index;

	public ConveyorTakePacket(int index) {
		this.index = index;
	}

	public ConveyorTakePacket(FriendlyByteBuf buffer) {
		this.index = buffer.readVarInt();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeVarInt(this.index);
	}

	public static class Handler {
		public static void onMessage(ConveyorTakePacket message, Supplier<NetworkEvent.Context> ctx) {
			final ServerPlayer player = ctx.get().getSender();
			ctx.get().enqueueWork(() -> {
				if(player != null) {
					final Challenge challenge = ChallengeManager.getPlayerChallenge(player);
					if(challenge != null) {
						challenge.takeConveyorCard(player, message.index);
					}
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
