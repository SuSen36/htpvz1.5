package com.hungteen.pvz.common.network.toclient;

import com.hungteen.pvz.client.challenge.ClientConveyorBeltManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ConveyorBeltPacket {

	private final List<ItemStack> cards;
	private final long[] entryTicks;
	private final boolean remove;

	public ConveyorBeltPacket(List<ItemStack> cards, long[] entryTicks) {
		this.cards = cards;
		this.entryTicks = entryTicks;
		this.remove = false;
	}

	private ConveyorBeltPacket() {
		this.cards = new ArrayList<>();
		this.entryTicks = new long[0];
		this.remove = true;
	}

	public static ConveyorBeltPacket remove() {
		return new ConveyorBeltPacket();
	}

	public ConveyorBeltPacket(FriendlyByteBuf buf) {
		this.remove = buf.readBoolean();
		if(this.remove) {
			this.cards = new ArrayList<>();
			this.entryTicks = new long[0];
		} else {
			final int size = buf.readVarInt();
			this.cards = new ArrayList<>(size);
			for(int i = 0; i < size; ++ i) {
				this.cards.add(buf.readItem());
			}
			this.entryTicks = buf.readLongArray();
		}
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(this.remove);
		if(! this.remove) {
			buf.writeVarInt(this.cards.size());
			this.cards.forEach(buf::writeItem);
			buf.writeLongArray(this.entryTicks);
		}
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if(this.remove) {
				ClientConveyorBeltManager.remove();
			} else {
				ClientConveyorBeltManager.update(this.cards, this.entryTicks);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
