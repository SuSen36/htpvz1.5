package com.hungteen.pvz.common.advancement;

import com.hungteen.pvz.common.advancement.trigger.*;
import com.hungteen.pvz.utils.StringUtil;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class AdvancementHandler {

	public static final PlayerTrigger BOWLING_STRIKE = new PlayerTrigger(StringUtil.prefix("strike"));

	public static void init() {
		CriteriaTriggers.register(SunAmountTrigger.INSTANCE);
		CriteriaTriggers.register(TreeLevelTrigger.INSTANCE);
		CriteriaTriggers.register(EntityEffectAmountTrigger.INSTANCE);
		CriteriaTriggers.register(PlayerPlacePAZTrigger.INSTANCE);
		CriteriaTriggers.register(CharmZombieTrigger.INSTANCE);
		CriteriaTriggers.register(PlantLevelTrigger.INSTANCE);
		CriteriaTriggers.register(MoneyTrigger.INSTANCE);
		CriteriaTriggers.register(PlantSuperTrigger.INSTANCE);
		CriteriaTriggers.register(ChallengeTrigger.INSTANCE);
		CriteriaTriggers.register(InvasionTrigger.INSTANCE);
		CriteriaTriggers.register(InvasionMissionTrigger.INSTANCE);
		CriteriaTriggers.register(PVZTradeTrigger.INSTANCE);
		CriteriaTriggers.register(SlotMachineTrigger.INSTANCE);
		CriteriaTriggers.register(BOWLING_STRIKE);
	}
	
}
