package com.hungteen.pvz.client.challenge;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientConveyorBeltManager {

	private static final List<ItemStack> CARDS = new ArrayList<>();
	private static final List<Long> ENTRY_TICKS = new ArrayList<>();
	private static int selected = 0;
	private static boolean active = false;

	public static void update(List<ItemStack> cards, long[] entryTicks) {
		CARDS.clear();
		CARDS.addAll(cards);
		ENTRY_TICKS.clear();
		for(long tick : entryTicks) {
			ENTRY_TICKS.add(tick);
		}
		//服务端可能已取走选中位置之后的卡，选中下标需收回有效范围
		selected = Mth.clamp(selected, 0, Math.max(0, CARDS.size() - 1));
		active = true;
	}

	public static void remove() {
		CARDS.clear();
		ENTRY_TICKS.clear();
		selected = 0;
		active = false;
	}

	public static boolean isActive() {
		return active;
	}

	public static int getSize() {
		return CARDS.size();
	}

	public static ItemStack getCard(int index) {
		return CARDS.get(index);
	}

	public static long getEntryTick(int index) {
		return ENTRY_TICKS.get(index);
	}

	public static int getSelected() {
		return selected;
	}

	/**
	 * 传送带激活时上下键在带上循环切换选中的卡。
	 */
	public static void changeSelection(int offset) {
		if(! CARDS.isEmpty()) {
			selected = (selected + offset + CARDS.size()) % CARDS.size();
		}
	}
}
