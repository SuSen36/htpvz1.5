package com.hungteen.pvz.client.capability.level;

import com.hungteen.pvz.PVZMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端传送带状态挂在客户端世界上，随世界对象一起生灭。
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = PVZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ConveyorBeltCapability implements ICapabilityProvider {

	public static final Capability<ConveyorBeltCapability> CAP = CapabilityManager.get(new CapabilityToken<>(){});

	private final List<ItemStack> cards = new ArrayList<>();
	private final List<Long> entryTicks = new ArrayList<>();
	private boolean active = false;
	private int selected = 0;

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(ConveyorBeltCapability.class);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return cap == CAP ? LazyOptional.of(() -> (T) this) : LazyOptional.empty();
	}

	@Nullable
	public static ConveyorBeltCapability getCurrent() {
		final Level level = Minecraft.getInstance().level;
		return level == null ? null : level.getCapability(CAP).orElse(null);
	}

	public void update(List<ItemStack> newCards, long[] newEntryTicks) {
		this.cards.clear();
		this.cards.addAll(newCards);
		this.entryTicks.clear();
		for(long tick : newEntryTicks) {
			this.entryTicks.add(tick);
		}
		//服务端可能已取走选中位置之后的卡，选中下标需收回有效范围
		this.selected = Mth.clamp(this.selected, 0, Math.max(0, this.cards.size() - 1));
		this.active = true;
	}

	public void clear() {
		this.cards.clear();
		this.entryTicks.clear();
		this.selected = 0;
		this.active = false;
	}

	public boolean isActive() {
		return this.active;
	}

	public int getSize() {
		return this.cards.size();
	}

	public ItemStack getCard(int index) {
		return this.cards.get(index);
	}

	public long getEntryTick(int index) {
		return this.entryTicks.get(index);
	}

	public int getSelected() {
		return this.selected;
	}

	/**
	 * 传送带激活时上下键在带上循环切换选中的卡。
	 */
	public void changeSelection(int offset) {
		if(! this.cards.isEmpty()) {
			this.selected = (this.selected + offset + this.cards.size()) % this.cards.size();
		}
	}

	@Mod.EventBusSubscriber(modid = PVZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
	private static final class ForgeEvents {

		@SubscribeEvent
		public static void attachLevelCapabilities(AttachCapabilitiesEvent<Level> event) {
			//只能挂客户端世界：集成服务端的 ServerLevel 共用同一条事件，挂上去会跟着存档走
			if(event.getObject() instanceof ClientLevel) {
				event.addCapability(new ResourceLocation(PVZMod.MOD_ID, "conveyor_belt"), new ConveyorBeltCapability());
			}
		}
	}
}