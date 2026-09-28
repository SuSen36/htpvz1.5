package com.hungteen.pvz.common.event;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.events.PlayerLevelChangeEvent;
import com.hungteen.pvz.common.capability.CapabilityHandler;
import com.hungteen.pvz.common.capability.player.PlayerDataManager;
import com.hungteen.pvz.common.datapack.PVZDataPackManager;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.event.events.PlantConditionMatchingEvent;
import com.hungteen.pvz.common.event.events.SummonCardUseEvent;
import com.hungteen.pvz.common.event.handler.PlayerEventHandler;
import com.hungteen.pvz.common.item.spawn.card.PlantCardItem;
import com.hungteen.pvz.common.item.spawn.card.SummonCardItem;
import com.hungteen.pvz.common.item.tool.plant.BowlingGloveItem;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.world.challenge.Challenge;
import com.hungteen.pvz.common.world.challenge.ChallengeManager;
import com.hungteen.pvz.common.world.invasion.InvasionManager;
import com.hungteen.pvz.compat.CompatUtil;
import com.hungteen.pvz.utils.PlayerUtil;
import com.hungteen.pvz.utils.enums.Resources;
import com.hungteen.pvz.utils.others.WeightList;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid=PVZMod.MOD_ID)
public class PVZPlayerEvents {

	@SubscribeEvent
	public static void tickPlayer(TickEvent.PlayerTickEvent ev) {
		if(! ev.player.level.isClientSide) {
			if (ev.player.tickCount < 2) {
				PlayerUtil.getOptManager(ev.player).ifPresent(PlayerDataManager::loadSummonCardCDs);
			}
			ev.player.getCapability(CapabilityHandler.PLAYER_DATA_CAPABILITY).ifPresent((l) -> {
				if (l.getPlayerData().getOtherStats().playSoundTick > 0) {
					--l.getPlayerData().getOtherStats().playSoundTick;
				}
			});
		}
		PVZMod.PROXY.climbUp();
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent ev) {
		if (! ev.getEntity().level.isClientSide) {
			PlayerEventHandler.onPlayerLogin(ev.getEntity());
			InvasionManager.addPlayer(ev.getEntity());
			PlayerEventHandler.unLockPAZs(ev.getEntity());
			//sync to client data pack.
			PVZDataPackManager.sendSyncPacketsTo(ev.getEntity());
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent ev) {
		if (! ev.getEntity().level.isClientSide) {
			PlayerEventHandler.onPlayerLogout(ev.getEntity());
			InvasionManager.removePlayer(ev.getEntity());
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone ev) {
		PlayerEventHandler.clonePlayerData(ev.getOriginal(), ev.getEntity(), ev.isWasDeath());
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent ev) {
		if(! ev.getEntity().level.isClientSide) {
			PlayerUtil.getOptManager(ev.getEntity()).ifPresent(l -> l.syncToClient());
		}
	}
	
	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent ev) {
		if(! ev.getEntity().level.isClientSide) {
			PlayerUtil.getOptManager(ev.getEntity()).ifPresent(l -> l.syncToClient());
		}
	}
	
	@SubscribeEvent
	public static void onPlayerInteractSpec(PlayerInteractEvent.EntityInteractSpecific ev) {
		if(ev.getHand() == InteractionHand.MAIN_HAND) {
			if(! ev.getLevel().isClientSide){
				PlayerEventHandler.quickRemoveByPlayer(ev.getEntity(), ev.getTarget(), ev.getEntity().getMainHandItem());
				PlayerEventHandler.makeSuperMode(ev.getEntity(), ev.getTarget(), ev.getEntity().getMainHandItem());
			}
			if(ev.getTarget() instanceof PVZPlantEntity && ev.getItemStack().getItem() instanceof ShovelItem) {
				ev.setCanceled(true);
				ev.setCancellationResult(InteractionResult.SUCCESS);
			}
		}
		BowlingGloveItem.onPickUp(ev);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void banBucket(PlayerInteractEvent.EntityInteractSpecific ev) {
		if(! CompatUtil.canBucketEntity(ev.getEntity().level, ev.getTarget(), ev.getItemStack())){
			ev.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTreeLevelUp(PlayerLevelChangeEvent ev) {
		if (!ev.getEntity().level.isClientSide && ev.isLevelUp()) {
			PlayerEventHandler.unLockPAZs(ev.getEntity());
			PlayerUtil.playClientSound(ev.getEntity(), SoundRegister.PLANT_GROW.get());
			PlayerUtil.addResource(ev.getEntity(), Resources.LOTTERY_CHANCE, 3);
		}
	}
	
	@SubscribeEvent
	public static void onSummonCardUse(SummonCardUseEvent ev) {
	}
	
	@SubscribeEvent
	public static void onPlantConditionMatching(PlantConditionMatchingEvent ev) {
		if(ev.phase == PlantConditionMatchingEvent.Phase.PRE && ev.event != null && ! ev.isCanceled()
				&& ev.event.getEntity().level instanceof ServerLevel serverLevel) {
			final Player player = ev.event.getEntity();
			final ItemStack heldStack = ev.event.seedPacket;
			/* challenge bound cards can only be planted inside their own challenge */
			final UUID challengeUuid = PlantCardItem.getChallengeUuid(heldStack);
			if(challengeUuid != null && ! ChallengeManager.isPlayerInChallengeRange(serverLevel, challengeUuid, player)) {
				ev.setCanceled(true);
				ev.result = SummonCardItem.PlacementHints.CHALLENGE_ONLY.getTextByArg(ChatFormatting.RED, 0);
			} else if(player instanceof ServerPlayer serverPlayer) {
				/* when the level configures a seed pool, only pool entries can be planted */
				final Challenge playerChallenge = ChallengeManager.getPlayerChallenge(serverPlayer);
				if(playerChallenge != null) {
					final WeightList<ItemStack> levelSeedPool = playerChallenge.getRaidComponent().getSeedPool();
					if(levelSeedPool != null) {
						boolean allowed = false;
						for(ItemStack poolEntry : levelSeedPool.getItemList()) {
							if(poolEntry.getItem() == heldStack.getItem()) {
								allowed = true;
								break;
							}
						}
						if(! allowed) {
							ev.setCanceled(true);
							ev.result = SummonCardItem.PlacementHints.SEED_POOL_ONLY.getTextByArg(ChatFormatting.RED, 0);
						}
					}
				}
			}
		}
	}
	
}