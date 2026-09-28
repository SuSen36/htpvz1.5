package com.hungteen.pvz.common.item.spawn.card;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.types.ICoolDown;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.api.types.ISkillType;
import com.hungteen.pvz.common.advancement.trigger.PlayerPlacePAZTrigger;
import com.hungteen.pvz.common.block.BlockRegister;
import com.hungteen.pvz.common.enchantment.EnchantmentRegister;
import com.hungteen.pvz.common.enchantment.card.BandageEnchantment;
import com.hungteen.pvz.common.enchantment.card.ImmediateCDEnchantment;
import com.hungteen.pvz.common.enchantment.card.plantcard.BreakOutEnchantment;
import com.hungteen.pvz.common.enchantment.card.plantcard.DenselyPlantEnchantment;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.plant.magic.ImitaterEntity;
import com.hungteen.pvz.common.event.events.PlantConditionMatchingEvent;
import com.hungteen.pvz.common.event.events.PlantResourceEvent;
import com.hungteen.pvz.common.event.events.SummonCardUseEvent;
import com.hungteen.pvz.common.impl.CoolDowns;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.OtherPlants;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.impl.plant.PlantType;
import com.hungteen.pvz.common.item.PVZItemGroups;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.potion.EffectRegister;
import com.hungteen.pvz.common.world.challenge.Challenge;
import com.hungteen.pvz.common.world.challenge.ChallengeManager;
import com.hungteen.pvz.utils.ConfigUtil;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.PlayerUtil;
import com.hungteen.pvz.utils.enums.Resources;
import com.hungteen.pvz.utils.others.WeightList;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Normal plant type : place on top face of block.
 * Outer plant type : place on plant entity. 
 * Pumpkin heal type : heal pumpkin outer plant. 
 * Defender plant heal type : Heal Defender Plant. 
 * Upgrade plant type : base plant upgrade. 
 * CatTail upgrade type : cattail upgrade on lilypad. 
 * CoffeeBean place type : coffee bean ride on plant entity. 
 * GraveBuster place type : gravebuster ride on tombstone. 
 * Place block in water type : lilypad place in water. 
 * Place block on ground type : flower pot place on ground. 
 */
public class PlantCardItem extends SummonCardItem {

	private static final Set<IPlantType> TOOL_TIP_TYPES = new HashSet<>(Arrays.asList(
			PVZPlants.DOOM_SHROOM, OtherPlants.GOLD_LEAF
	));
	/**
	 * 挑战绑定标记：体验卡归属挑战的 bar uuid，无此标记即无限制。
	 */
	public static final String CHALLENGE_TAG = "challenge_uuid";
	public final IPlantType plantType;

	public PlantCardItem(IPlantType plant, boolean isFragment) {
		super(plant, isFragment);
		this.plantType = plant;
	}
	
	public PlantCardItem(Properties properties, IPlantType plant, boolean isFragment) {
		super(properties, plant, isFragment);
		this.plantType = plant;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {
		if(this.allowedIn(group)) {
			if(group == PVZItemGroups.PVZ_PLANT_CARD) {
				// insert sort.
				while(list.size() < PlantType.getPlants().size() * 2){
					list.add(new ItemStack(this));
				}
				final int pos = this.isEnjoyCard ? this.plantType.getId() + PlantType.getPlants().size() : this.plantType.getId();
				list.set(pos, new ItemStack(this));
			} else{
				list.add(new ItemStack(this));
			}
		}
	}

	/**
	 * only consider placement in water. <br>
	 * 1. check cool down. <br>
	 * 2. check can place in water. <br>
	 * 3. place water plants. <br>
	 */
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
		final ItemStack heldStack = getHeldStack(player.getItemInHand(handIn));
		if(world.isClientSide) {
			return InteractionResultHolder.success(heldStack);
		}
		/* check cool down */
		if(player.getCooldowns().isOnCooldown(heldStack.getItem())) {
			this.notifyPlayerAndCD(player, heldStack, PlacementHints.ON_COOL_DOWN, heldStack.getHoverName());
			return InteractionResultHolder.fail(heldStack);
		}
		/* do ray check */
		final BlockHitResult result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
		if (result.getType() == HitResult.Type.BLOCK) {
			final BlockPos waterPos = result.getBlockPos();
			/* can not place here */
			if(world.getFluidState(waterPos).getType() != Fluids.WATER || ! this.plantType.isWaterPlant()) {
				this.notifyPlayerAndCD(player, heldStack, this.plantType.isWaterPlant() ? PlacementHints.CAN_ONLY_PLANT_IN_WATER : PlacementHints.CANT_PLANT_IN_WATER, getPlantName(this.plantType));
				return InteractionResultHolder.fail(heldStack);
		    }
			if(! world.isEmptyBlock(waterPos.above())) {
				this.notifyPlayerAndCD(player, heldStack, PlacementHints.NO_ENOUGH_PLACE, 0);
				return InteractionResultHolder.fail(heldStack);
			}
			final MutableComponent plantResult = plantOnBlock(player, heldStack, world, waterPos, null);
			if (plantResult == null) {
				return InteractionResultHolder.success(heldStack);
			}
			player.displayClientMessage(plantResult, true);
			PlayerUtil.playClientSound(player, SoundRegister.NO.get());
			return InteractionResultHolder.fail(heldStack);
		} else {
			return InteractionResultHolder.pass(heldStack);
		}
	}
	
	/**
	 * only consider common plants that can place on suitable ground.
	 * not include imitater, outer plants, water plants, 
	 */
	@Override
	public InteractionResult useOn(UseOnContext context) {
		final Level world = context.getLevel();
		final Player player = context.getPlayer();
		final net.minecraft.world.InteractionHand hand = context.getHand();
		final ItemStack heldStack = context.getItemInHand();
		final ItemStack plantStack = getPlantStack(context.getItemInHand());
		final PlantCardItem cardItem = (PlantCardItem) plantStack.getItem();
		final IPlantType plantType = cardItem.plantType;
		final BlockPos pos = context.getClickedPos();
		if(world.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		if(plantType == null) {
			PVZMod.LOGGER.error("Plant Card Use : Error Card !");
			return InteractionResult.FAIL;
		}
		/* check cool down */
        if (player != null && player.getCooldowns().isOnCooldown(heldStack.getItem())) {
            this.notifyPlayerAndCD(player, heldStack, PlacementHints.ON_COOL_DOWN, heldStack.getHoverName());
            return InteractionResult.FAIL;
        }

        /* check water plants, cat tail is upgraded onto lily pad instead of placed in fluid directly */
		if(plantType.isWaterPlant() && plantType != PVZPlants.CAT_TAIL) {
			return this.use(world, player, hand).getResult();
		}
		final MutableComponent plantResult = plantOnBlock(player, heldStack, world, pos, context.getClickedFace());
		if (plantResult == null) {
			return InteractionResult.SUCCESS;
		}
		player.displayClientMessage(plantResult, true);
		PlayerUtil.playClientSound(player, SoundRegister.NO.get());
		return InteractionResult.FAIL;
	}
	
	private static Component getPlantName(IPlantType plantType) {
		return plantType.getEntityType().map(entityType -> entityType.getDescription()).orElse(Component.empty());
	}

	/**
	 * plant card on a block position with pre/post condition events. <br>
	 * the plant entity is pre-created and discarded when condition checks fail,
	 * so post handlers (e.g. shell startup) can replace the failed planting with a container card.
	 * @param direction null means planting inside fluid, otherwise the clicked face of the block.
	 * @return null for successful planting, otherwise the fail reason.
	 */
	@Nullable
	public static MutableComponent plantOnBlock(Player player, ItemStack heldStack, Level level, BlockPos clickPos, @Nullable Direction direction) {
		if(! (level instanceof ServerLevel serverLevel)) {
			return null;
		}
		final ItemStack plantStack = getPlantStack(heldStack);
		if(! (plantStack.getItem() instanceof PlantCardItem cardItem) || ! (heldStack.getItem() instanceof PlantCardItem costCard)) {
			return Component.empty();
		}
		final IPlantType plantType = cardItem.plantType;
		final boolean isImitater = heldStack.getItem() instanceof ImitaterCardItem;
		/* check cool down */
		if(player.getCooldowns().isOnCooldown(heldStack.getItem())) {
			return PlacementHints.ON_COOL_DOWN.getTextByArg(ChatFormatting.RED, heldStack.getHoverName());
		}
		/* check position */
		final boolean inWater = direction == null;
		BlockPos spawnPos = clickPos;
		MutableComponent positionError = null;
		if(inWater) {
			spawnPos = clickPos.above();
			/* 猫尾草只能种在睡莲上，不能直接种进流体 */
			if(! plantType.isWaterPlant() || plantType == PVZPlants.CAT_TAIL) {
				positionError = PlacementHints.CANT_PLANT_IN_WATER.getTextByArg(ChatFormatting.RED, getPlantName(plantType));
			} else if(level.getFluidState(clickPos).getType() != Fluids.WATER) {
				positionError = PlacementHints.CAN_ONLY_PLANT_IN_WATER.getTextByArg(ChatFormatting.RED, getPlantName(plantType));
			} else if(! level.isEmptyBlock(spawnPos)) {
				positionError = PlacementHints.NO_ENOUGH_PLACE.getTextByArg(ChatFormatting.RED, 0);
			}
		} else if(! level.isEmptyBlock(clickPos.above())) {
			positionError = PlacementHints.NO_ENOUGH_PLACE.getTextByArg(ChatFormatting.RED, 0);
		} else if(plantType.getUpgradeFrom().isPresent()) {
			positionError = PlacementHints.UPGRADE_ONLY.getTextByArg(ChatFormatting.RED, 0);
		} else if(! plantType.getPlacement().canPlaceOnBlock(level.getBlockState(clickPos).getBlock())) {
			positionError = PlacementHints.CANT_PLANT_ON.getTextByArg(ChatFormatting.RED, getPlantName(plantType), level.getBlockState(clickPos).getBlock().getName());
		} else if(! level.getBlockState(clickPos).getCollisionShape(level, clickPos).isEmpty()) {
			spawnPos = clickPos.relative(Direction.UP);
		}
		if(positionError == null && ! level.getEntitiesOfClass(PVZPlantEntity.class, new AABB(spawnPos)).isEmpty()) {
			positionError = PlacementHints.NO_ENOUGH_PLACE.getTextByArg(ChatFormatting.RED, 0);
		}
		/* pre-create entity without adding to world */
		final IPlantType entityPlantType = isImitater ? PVZPlants.IMITATER : plantType;
		if(entityPlantType.getEntityType().isEmpty()) {
			PVZMod.LOGGER.error("Plant Card : Summon wrong plant entity !");
			return PlacementHints.GROUND.getTextByArg(ChatFormatting.RED, 0);
		}
		final Mob spawnedMob = entityPlantType.getEntityType().get().create(serverLevel, plantStack.getTag(),
				plantStack.hasCustomHoverName() ? plantStack.getHoverName() : null, player,
				spawnPos, MobSpawnType.SPAWN_EGG, true, true);
		if(! (spawnedMob instanceof PVZPlantEntity plantEntity)) {
			PVZMod.LOGGER.error("Plant Card : No such plant entity !");
			if(spawnedMob != null) {
				spawnedMob.discard();
			}
			return PlacementHints.GROUND.getTextByArg(ChatFormatting.RED, 0);
		}
		/* create(...,true,true) settles on block collision tops; fluid has none, so water
		 * plants end up at the fluid-cell bottom. Lift them onto the surface, matching
		 * htpvz2 customPositionSafe: clickPos.y + fluid height */
		if(inWater) {
			plantEntity.moveTo(clickPos.getX() + 0.5D,
					clickPos.getY() + level.getFluidState(clickPos).getHeight(level, clickPos),
					clickPos.getZ() + 0.5D);
		}
		if(plantEntity instanceof ImitaterEntity imitater) {
			imitater.setImitateCard(plantStack.copy());
			imitater.setDirection(player.getDirection().getOpposite());
		}
		plantEntity.onSpawnedByPlayer(player, cardItem.getBasisSunCost(plantStack));
		enchantPlantEntityByCard(plantEntity, plantStack);
		/* fire resource event */
		final int sunCost = cardItem.getCardSunCost(player, plantStack);
		final int coolDown = ImmediateCDEnchantment.canImmediateCD(plantStack, player.getRandom()) ? 20 : getPlantCardCD(player, plantStack, cardItem);
		final PlantResourceEvent.CheckPlantConditionEvent resourceEvent = new PlantResourceEvent.CheckPlantConditionEvent(
				player, heldStack, plantEntity, Resources.SUN_NUM, sunCost, coolDown);
		MinecraftForge.EVENT_BUS.post(resourceEvent);
		/* pre condition */
		final PlantConditionMatchingEvent.OnBlock preEvent = new PlantConditionMatchingEvent.OnBlock(
				plantEntity, resourceEvent, null, level, clickPos, direction, true, PlantConditionMatchingEvent.Phase.PRE);
		MinecraftForge.EVENT_BUS.post(preEvent);
		if(preEvent.isCanceled()) {
			plantEntity.discard();
			return preEvent.result != null ? preEvent.result : PlacementHints.GROUND.getTextByArg(ChatFormatting.RED, 0);
		}
		/* check lock */
		if(! costCard.isEnjoyCard && PlayerUtil.isPAZLocked(player, costCard.plantType) && ConfigUtil.needUnlockToPlant() && ! player.isCreative()) {
			plantEntity.discard();
			return PlacementHints.LOCKED.getTextByArg(ChatFormatting.RED, costCard.plantType.getRequiredLevel());
		}
		/* challenge bound check：挑战绑定体验卡只能在其对应挑战范围内种植 */
		final UUID challengeUuid = getChallengeUuid(heldStack);
		if(challengeUuid != null && ! ChallengeManager.isPlayerInChallengeRange(serverLevel, challengeUuid, player)) {
			plantEntity.discard();
			return PlacementHints.CHALLENGE_ONLY.getTextByArg(ChatFormatting.RED, 0);
		}
		/* level seed pool：关卡配置了种子池时，只允许种植池内植物 */
		final Challenge playerChallenge = player instanceof ServerPlayer serverPlayer ? ChallengeManager.getPlayerChallenge(serverPlayer) : null;
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
					plantEntity.discard();
					return PlacementHints.SEED_POOL_ONLY.getTextByArg(ChatFormatting.RED, 0);
				}
			}
		}
		/* check sun */
		if(resourceEvent.cost > PlayerUtil.getResource(player, Resources.SUN_NUM) && ! player.isCreative()) {
			plantEntity.discard();
			if(sunCost == cardItem.getBasisSunCost(plantStack)) {
				return PlacementHints.NO_ENOUGH_RESOURCE.getTextByArg(ChatFormatting.RED, Resources.SUN_NUM.getText());
			}
			return PlacementHints.MULTIPLE_SUN.getTextByArg(ChatFormatting.RED, resourceEvent.cost);
		}
		/* post condition */
		final PlantConditionMatchingEvent.OnBlock postEvent = new PlantConditionMatchingEvent.OnBlock(
				plantEntity, resourceEvent, positionError, level, clickPos, direction, true, PlantConditionMatchingEvent.Phase.POST);
		MinecraftForge.EVENT_BUS.post(postEvent);
		if(postEvent.result != null) {
			plantEntity.discard();
			return postEvent.result;
		}
		/* consume sun */
		if(! player.isCreative()) {
			PlayerUtil.addResource(player, Resources.SUN_NUM, - resourceEvent.cost);
		}
		/* riding relation may be established by post handlers before entity joins world;
		 * shell startup may have already added it as a container's passenger */
		if(! plantEntity.isRemoved() && serverLevel.getEntity(plantEntity.getUUID()) == null) {
			serverLevel.addFreshEntityWithPassengers(plantEntity);
		}
		/* handle cd and misc */
		MinecraftForge.EVENT_BUS.post(new SummonCardUseEvent(player, heldStack, plantStack));
		if(PlayerUtil.isPlayerSurvival(player)) {
			if(cardItem.isEnjoyCard) {
				heldStack.shrink(1);
			} else {
				PlayerUtil.setItemStackCD(player, heldStack, resourceEvent.coolDown);
			}
		} else {
			player.getCooldowns().addCooldown(heldStack.getItem(), 10);
		}
		if(player instanceof ServerPlayer serverPlayer) {
			if(plantType.getUpgradeFrom().isPresent()) {
				PlayerPlacePAZTrigger.INSTANCE.trigger(serverPlayer, PlayerPlacePAZTrigger.PlaceTypes.UPGRADE.toString().toLowerCase(), plantType.getIdentity());
			} else {
				PlayerPlacePAZTrigger.INSTANCE.trigger(serverPlayer, PlayerPlacePAZTrigger.PlaceTypes.PLANT.toString().toLowerCase(), plantType.getIdentity());
			}
		}
		player.awardStat(Stats.ITEM_USED.get(cardItem));
		return null;
	}

	/**
	 * check sunCost and spawn plantEntity.
	 */
	public static boolean checkSunAndSummonPlant(Player player, ItemStack heldStack, ItemStack plantStack, PlantCardItem cardItem, BlockPos pos, Consumer<PVZPlantEntity> consumer) {
		final IPlantType plantType = cardItem.plantType;
			/* handle imitater card */
		if(heldStack.getItem() instanceof ImitaterCardItem){
			//*0.6.4 separately checkSunAndCD to fix imitator cd calculation bug.
			if (checkSunAndCD(player, (ImitaterCardItem) heldStack.getItem(), plantStack, false, p -> true)) {
				return ImitaterCardItem.summonImitater(player, heldStack, plantStack, cardItem, pos, i -> consumer.accept(i));
			}
		} else {
			if(checkSunAndCD(player, cardItem, plantStack, false, p -> true)){
				/* other plant card */
				if(! handlePlantEntity(player, plantType, plantStack, pos, plantEntity -> {
	//				/* update maxLevel and its owner */
					plantEntity.onSpawnedByPlayer(player, cardItem.getBasisSunCost(plantStack));
					/* other operations */
					consumer.accept(plantEntity);
					/* enchantment effects */
					enchantPlantEntityByCard(plantEntity, plantStack);
				})) {
					return false;
				}
				/* handle cd and misc */
				PlantCardItem.onUsePlantCard(player, heldStack, plantStack, cardItem);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@link #checkSunAndSummonPlant(Player, ItemStack, ItemStack, PlantCardItem, BlockPos, Consumer)}
	 * {@link ImitaterCardItem#summonImitater(Player, ItemStack, ItemStack, PlantCardItem, BlockPos, Consumer)}
	 */
	public static boolean handlePlantEntity(Player player, IPlantType plantType, ItemStack plantStack, BlockPos pos, Consumer<PVZPlantEntity> consumer) {
		if(plantType.getEntityType().isEmpty()) {
	        PVZMod.LOGGER.error("Plant Card : Summon wrong plant entity !");
		    return false;
	    }
	    final ServerLevel serverLevel = (ServerLevel) player.level;
	    final Mob spawnedMob = plantType.getEntityType().get().create(serverLevel, plantStack.getTag(),
	    		plantStack.hasCustomHoverName() ? plantStack.getHoverName() : null, player,
				pos, MobSpawnType.SPAWN_EGG, true, true);
    	if (! (spawnedMob instanceof PVZPlantEntity plantEntity)) {
    		PVZMod.LOGGER.error("Plant Card : No such plant entity !");
			if(spawnedMob != null) {
				spawnedMob.discard();
			}
			return false;
    	}
    	if(ForgeEventFactory.doSpecialSpawn(plantEntity, serverLevel, pos.getX(), pos.getY(), pos.getZ(), null, MobSpawnType.SPAWN_EGG)) {
    		return false;
    	}
    	/* create(...,true,true) settles on block collision tops; fluid has none, so water
    	 * plants (e.g. cattail upgraded from lily pad) must be lifted onto the surface
    	 * BEFORE joining the world, otherwise the spawn packet leaves at the fluid-cell
    	 * bottom and only a follow-up move packet corrects it. Matches card placement */
    	if(plantType.isWaterPlant() && ! serverLevel.getFluidState(pos).isEmpty()) {
    		plantEntity.moveTo(pos.getX() + 0.5D,
    				pos.getY() + serverLevel.getFluidState(pos).getHeight(serverLevel, pos),
    				pos.getZ() + 0.5D);
    	}
    	serverLevel.addFreshEntityWithPassengers(plantEntity);
    	consumer.accept(plantEntity);
		return true;
	}
	
	/**
	 * check sunCost and place plantBlock.
	 */
	public static boolean checkSunAndPlaceBlock(Player player, ItemStack heldStack, ItemStack plantStack, PlantCardItem cardItem, BlockPos pos) {
		final IPlantType plantType = cardItem.plantType;
		final BlockState state = PlantCardItem.getBlockState(player, plantType);
		if(heldStack.getItem() instanceof ImitaterCardItem) {
			//*0.6.4 separately checkSunAndCD to fix imitator cd calculation bug.
			if(checkSunAndCD(player, (ImitaterCardItem) heldStack.getItem(), plantStack, true, p -> {
				if(state == null) {
					PVZMod.LOGGER.error("Plant Card : No such plant block !");
					return false;
				}
				return true;
			})) {
				if(! ImitaterCardItem.summonImitater(player, heldStack, plantStack, cardItem, pos, (imitater) -> {})){
					return false;
				}
				if (player instanceof ServerPlayer) {
					CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, heldStack);
				}
				/* handle cd and misc */
				PlantCardItem.onUsePlantCard(player, heldStack, plantStack, (PlantCardItem) heldStack.getItem());
				return true;
			}
		} else {
			if(checkSunAndCD(player, cardItem, plantStack, true, p -> {
				if(state == null) {
					PVZMod.LOGGER.error("Plant Card : No such plant block !");
					return false;
				}
				return true;
			})) {
				handlePlantBlock(player.level, plantType, state, pos);
				if (player instanceof ServerPlayer) {
					CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, heldStack);
				}
				/* handle cd and misc */
				PlantCardItem.onUsePlantCard(player, heldStack, plantStack, (PlantCardItem) heldStack.getItem());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@link #checkSunAndPlaceBlock(Player, ItemStack, ItemStack, PlantCardItem, BlockPos)}
	 */
	public static void handlePlantBlock(Level world, IPlantType plantType, BlockState state, BlockPos pos) {
		world.setBlock(pos, state, 11);
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), plantType.isWaterPlant() ? SoundRegister.PLACE_PLANT_WATER.get() : SoundRegister.PLACE_PLANT_GROUND.get(), SoundSource.BLOCKS, 1F, 1F);
	}
	
	/**
	 * check sunCost and place plant onto a container plant (as a riding passenger).
	 */
	public static boolean checkSunAndHoldPlant(Player player, PVZPlantEntity plantEntity, PlantCardItem cardItem,
			ItemStack heldStack) {
		/* check held stack */
		if(! checkItemStackAndCD(player, heldStack, stack -> true)) {
			return false;
		}
		final ItemStack plantStack = getPlantStack(heldStack);
		final IPlantType plantType = ((PlantCardItem) plantStack.getItem()).plantType;
		/* container plant can only carry plant that it allows, and can not hold the same type;
		 * upgrade cards must grow out of their base plant via the upgrade path, never held
		 * directly onto an empty container (the post-placement shell chain uses mountPlantOn) */
		if(! plantEntity.canHoldPlant() || ! plantEntity.getPassengers().isEmpty()
				|| plantType.getUpgradeFrom().isPresent()
				|| ! plantType.canBeHold() || ! plantEntity.canPlantOnMe(plantType)) {
			return false;
		}
		return PlantCardItem.checkSunAndSummonPlant(player, heldStack, plantStack, cardItem, plantEntity.blockPosition(), plantEntity1 -> {
			if (plantEntity1 instanceof ImitaterEntity) {
				((ImitaterEntity) plantEntity1).setImitateAction(p -> p.startRiding(plantEntity));
			} else {
				plantEntity1.startRiding(plantEntity);
			}
		});
	}

	/**
	 * check sun cost and summon pumpkin that wraps the plant (as a riding vehicle).
	 */
	public static boolean checkSunAndCarryPlant(Player player, PVZPlantEntity plantEntity, PlantCardItem cardItem,
			ItemStack heldStack) {
		/* check held stack */
		if(! checkItemStackAndCD(player, heldStack, stack -> ((PlantCardItem) stack.getItem()).plantType == PVZPlants.PUMPKIN)) {
			return false;
		}
		final ItemStack plantStack = getPlantStack(heldStack);
		/* 容器植物由 hold 分支承载南瓜，南瓜自身不能再套一个南瓜 */
		if(plantEntity.getPlantType() == PVZPlants.PUMPKIN || plantEntity.canHoldPlant()) {
			return false;
		}
		/* when target is riding, only a container plant that allows pumpkin can be taken over */
		if(plantEntity.getVehicle() != null && ! (plantEntity.getVehicle() instanceof PVZPlantEntity container
				&& container.canPlantOnMe(PVZPlants.PUMPKIN))) {
			return false;
		}
		return PlantCardItem.checkSunAndSummonPlant(player, heldStack, plantStack, cardItem, plantEntity.blockPosition(), plantEntity1 -> {
			final Entity vehicle = plantEntity.getVehicle();
			if (plantEntity1 instanceof ImitaterEntity) {
				((ImitaterEntity) plantEntity1).setImitateAction(p -> {
					plantEntity.startRiding(p);
					if(vehicle instanceof PVZPlantEntity container) {
						p.startRiding(container);
					}
				});
			} else {
				plantEntity.startRiding(plantEntity1);
				if(vehicle instanceof PVZPlantEntity container) {
					plantEntity1.startRiding(container);
				}
			}
		});
	}

	/**
	 * check sunCost and heal defender plantEntity.
	 *
     */
	public static boolean checkSunAndHealPlant(Player player, PVZPlantEntity plantEntity, PlantCardItem cardItem,
			ItemStack heldStack) {
		/* check held stack */
		if(! checkItemStackAndCD(player, heldStack, stack -> true)) {
			return false;
		}
		final ItemStack plantStack = getPlantStack(heldStack);
		final IPlantType plantType = ((PlantCardItem) plantStack.getItem()).plantType;
		final float percent = BandageEnchantment.getHealPercent(plantStack);
		/* the same type */
		if(! plantType.equals(plantEntity.getPlantType())) {
			return false;
		}
		if(checkSunAndCD(player, cardItem, plantStack, true, p -> true)){
			onUsePlantCard(player, heldStack, plantStack, cardItem);
			plantEntity.onHealBy(plantType, percent);
				return true;
		}
		return false;
	}
	
	/**
	 * check sunCost and heal defender plantEntity.
	 *
     */
	public static boolean checkSunAndUpgradePlant(Player player, PVZPlantEntity plantEntity, PlantCardItem cardItem,
			ItemStack heldStack) {
		/* check held stack */
		if(! checkItemStackAndCD(player, heldStack, stack -> true)) {
			return false;
		}
		
		final ItemStack plantStack = getPlantStack(heldStack);
		final IPlantType plantType = ((PlantCardItem) plantStack.getItem()).plantType;
		if(plantType.getUpgradeFrom().isPresent() && plantEntity.getPlantType().equals(plantType.getUpgradeFrom().get())) {
            return plantEntity.canBeUpgrade(player) && PlantCardItem.checkSunAndSummonPlant(player, heldStack, plantStack, cardItem, plantEntity.blockPosition(), (plant) -> {
                /* the upgraded plant inherits the container its base rode on, or it floats above and wilts. */
                final Entity carrier = plantEntity.getVehicle();
                if (plant instanceof ImitaterEntity) {
                    ((ImitaterEntity) plant).setImitateAction((p) -> {
                        plantEntity.onPlantUpgrade(p);
                        if(carrier instanceof PVZPlantEntity carrierPlant) {
                            p.mountPlantOn(carrierPlant);
                        }
                    });
                } else {
                    plantEntity.onPlantUpgrade(plant);
                    if(carrier instanceof PVZPlantEntity carrierPlant) {
                        plant.mountPlantOn(carrierPlant);
                    }
                }
            });
		}
		return false;
	}
	
	/**
	 * check sunCost and heal defender plantEntity.
	 *
     */
	public static boolean checkSunAndInteractEntity(Player player, Entity entity, PlantCardItem cardItem,
			ItemStack heldStack, Predicate<IPlantType> pre, Consumer<PVZPlantEntity> con) {
		/* check held stack */
		if(! checkItemStackAndCD(player, heldStack, stack -> true)) {
			return false;
		}
		final ItemStack plantStack = getPlantStack(heldStack);
		final IPlantType plantType = ((PlantCardItem) plantStack.getItem()).plantType;
		if(! pre.test(plantType)) {
			return false;
		}
        return PlantCardItem.checkSunAndSummonPlant(player, heldStack, plantStack, cardItem, entity.blockPosition(), plantEntity -> {
            if (plantEntity instanceof ImitaterEntity) {
                ((ImitaterEntity) plantEntity).setImitateAction(p -> con.accept(p));
            } else {
                con.accept(plantEntity);
            }
        });
    }
	
	/**
	 * check card cd and sun cost, and other predicates, finally consume sun.
	 */
	private static boolean checkSunAndCD(Player player, PlantCardItem cardItem, ItemStack stack, boolean ignore, Predicate<Player> pre) {
		/* check cool down */
		if(player.getCooldowns().isOnCooldown(cardItem)) {
			cardItem.notifyPlayerAndCD(player, stack, PlacementHints.ON_COOL_DOWN, stack.getHoverName());
			return false;
		}
		/* check lock */
		if(! cardItem.isEnjoyCard && (PlayerUtil.isPAZLocked(player, cardItem.plantType) && ConfigUtil.needUnlockToPlant() && !player.isCreative())) {
			cardItem.notifyPlayerAndCD(player, stack, PlacementHints.LOCKED, cardItem.plantType.getRequiredLevel());
			return false;
		}
		/* whether consider surrounding plants number */
		final int sunCost = ignore ? cardItem.getBasisSunCost(stack) : cardItem.getCardSunCost(player, stack);
		/* check sun */
		if(sunCost > PlayerUtil.getResource(player, Resources.SUN_NUM) && !player.isCreative()) {
			if (sunCost == cardItem.getBasisSunCost(stack)) {
				cardItem.notifyPlayerAndCD(player, stack, PlacementHints.NO_ENOUGH_RESOURCE, Resources.SUN_NUM.getText());
			}
			else {
				cardItem.notifyPlayerAndCD(player, stack, PlacementHints.MULTIPLE_SUN, sunCost);
			}
			return false;
		}
		if(pre.test(player)) {
			if (! player.isCreative()){
				PlayerUtil.addResource(player, Resources.SUN_NUM, - sunCost);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * does imitater has a correct card.
	 */
	private static boolean checkItemStackAndCD(Player player, ItemStack heldStack, Predicate<ItemStack> predicate) {
		ItemStack stack = heldStack;
		if(heldStack.getItem() instanceof ImitaterCardItem) {
			stack = getPlantStack(heldStack);
		}
		return stack.getItem() instanceof PlantCardItem && ! player.getCooldowns().isOnCooldown(stack.getItem()) && predicate.test(stack);
	}
	
	/**
	 */
	public static void enchantPlantEntityByCard(PVZPlantEntity plantEntity, ItemStack stack) {
		/* check break out enchantment */
		BreakOutEnchantment.checkAndBreakOut(plantEntity, stack);
		/* check charm enchantment */
		if(EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.CHARM.get(), stack) > 0) {
			plantEntity.onCharmedBy(null);
		}
	}

	/**
	 * deal with cd and misc.
	 */
    public static void onUsePlantCard(Player player, ItemStack heldStack, ItemStack plantStack, PlantCardItem item) {
		MinecraftForge.EVENT_BUS.post(new SummonCardUseEvent(player, heldStack, plantStack));
		if(PlayerUtil.isPlayerSurvival(player)) {
			if(item.isEnjoyCard) {
				heldStack.shrink(1);
			} else {
				handlePlantCardCoolDown(player, heldStack, plantStack, item);
			}
		} else {
			player.getCooldowns().addCooldown(heldStack.getItem(), 10);
		}
		if(player instanceof ServerPlayer) {
			if(item.plantType.getUpgradeFrom().isPresent()){
				PlayerPlacePAZTrigger.INSTANCE.trigger((ServerPlayer) player, PlayerPlacePAZTrigger.PlaceTypes.UPGRADE.toString().toLowerCase(), item.plantType.getIdentity());
			} else{
				PlayerPlacePAZTrigger.INSTANCE.trigger((ServerPlayer) player, PlayerPlacePAZTrigger.PlaceTypes.PLANT.toString().toLowerCase(), item.plantType.getIdentity());
			}
		}
		player.awardStat(Stats.ITEM_USED.get(item));
	}
    
	/**
	 * set PlantCard Item cool down.
	 */
	public static void handlePlantCardCoolDown(Player player, ItemStack heldStack, ItemStack plantStack, PlantCardItem item) {
		/* handle immediate cool down enchantment */
		if(ImmediateCDEnchantment.canImmediateCD(plantStack, player.getRandom())) {
			PlayerUtil.setItemStackCD(player, heldStack, 20);
		} else {
			PlayerUtil.setItemStackCD(player, heldStack, getPlantCardCD(player, plantStack, item));
		}
	}
	
	@Nullable
	public static BlockState getBlockState(Player player, IPlantType plant) {
		return plant == PVZPlants.LILY_PAD ? BlockRegister.LILY_PAD.get().getStateForPlacement(player) :
			   plant == PVZPlants.FLOWER_POT ? BlockRegister.FLOWER_POT.get().getStateForPlacement(player) :
			   null;
	}
	
	@Nullable
	public static BlockState getBlockState(Direction direction, IPlantType plant) {
		return plant == PVZPlants.LILY_PAD ? BlockRegister.LILY_PAD.get().getStateForPlacement(direction) :
			   plant == PVZPlants.FLOWER_POT ? BlockRegister.FLOWER_POT.get().getStateForPlacement(direction) :
			   null;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(Component.translatable("tooltip.pvz.plant_card_info").withStyle(ChatFormatting.GREEN));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		//挑战绑定体验卡：红色下划线标注，只能在该挑战内种植
		if(getChallengeUuid(stack) != null) {
			tooltip.add(Component.translatable("tooltip.pvz.challenge_bound").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.UNDERLINE));
		}
		final PlantCardItem item = (PlantCardItem) stack.getItem();
		if(item != null) {
		    final IPlantType plant = item.plantType;
	    	if(plant.getUpgradeFrom().isPresent()) {
	    	    if(plant == PVZPlants.COB_CANNON) {
	    		    tooltip.add(Component.translatable("tooltip.pvz.cob_cannon_card").withStyle(ChatFormatting.RED));
	    	    }  else {
	    		    tooltip.add(Component.translatable("tooltip.pvz.upgrade_card").append(plant.getUpgradeFrom().get().getText().withStyle(ChatFormatting.UNDERLINE)).withStyle(ChatFormatting.RED));
	    	    }
	    	} else if(plant == PVZPlants.CAT_TAIL) {
	    		tooltip.add(Component.translatable("tooltip.pvz.upgrade_card").append(PVZPlants.LILY_PAD.getText().withStyle(ChatFormatting.UNDERLINE)).withStyle(ChatFormatting.RED));
	    	}
		    /* misc */
		    if(TOOL_TIP_TYPES.contains(plant)) {
			    tooltip.add(Component.translatable("tooltip.pvz." + plant.toString().toLowerCase() + "_card").withStyle(ChatFormatting.DARK_RED));
		    }
		}
	}
	
	/**
	 * get the final cost when placing plant.
	 */
	public int getCardSunCost(Player player, ItemStack stack) {
		final int range = 30;
		final long count = EntityUtil.getFriendlyLivings(player, EntityUtil.getEntityAABB(player, range, range))
		    .stream().filter(entity -> {
				return entity instanceof PVZPlantEntity
						&& ((PVZPlantEntity) entity).getPlantType().countInLimit()
						&& ((PVZPlantEntity) entity).getOwnerUUID().isPresent()
						&& ((PVZPlantEntity) entity).getOwnerUUID().get().equals(player.getUUID());
			}).count() + (this.plantType.countInLimit() ? 1 : 0);

		final long multipy = Math.max(0, (count - ConfigUtil.getLimitPlantCount() - DenselyPlantEnchantment.getExtraPlantNum(stack) + 4) / 5);
		return (int) Math.min(100000L, this.getBasisSunCost(stack) * (1L << multipy));
	}
	
	/**
	 * the cost of plant card without consider range plants count.
	 */
	public int getBasisSunCost(ItemStack stack) {
		//挑战绑定体验卡种植无需阳光
		if(getChallengeUuid(stack) != null) {
			return 0;
		}
		//less sun skill boost.
		final int level = SkillTypes.getSkillLevel(stack, SkillTypes.LESS_SUN);
		int vary = (int) SkillTypes.LESS_SUN.getValueAt(level);
		// Extra skill sun cost.
		if(stack.getOrCreateTag().contains(SkillTypes.SKILL_TAG)){
			final CompoundTag nbt = stack.getOrCreateTag().getCompound(SkillTypes.SKILL_TAG);
			for (String key : nbt.getAllKeys()) {
				final ISkillType type = SkillTypes.getSkillType(key);
				if(type != null && nbt.getInt(key) > 0){
					vary -= type.getExtraSun();
				}
			}
		}

		if(stack.getItem() instanceof PlantCardItem) {
			IPlantType plantType = ((PlantCardItem) stack.getItem()).plantType;
			return Math.max(plantType.getSunCost() - vary, 0);
		}
		return 1;
	}
	
	/**
	 * the cd of plant card without consider other condition.
	 */
	public ICoolDown getBasisCoolDown(ItemStack stack) {
		if(stack.getItem() instanceof SummonCardItem) {
			return ((SummonCardItem) stack.getItem()).type.getCoolDown();
		}
		return CoolDowns.DEFAULT;
	}
	
	/**
	 * get cool down for current plant card.
	 */
	private static int getPlantCardCD(Player player, ItemStack stack, PlantCardItem item) {
		final int level = SkillTypes.getSkillLevel(stack, SkillTypes.FAST_CD);
		int cd = item.getBasisCoolDown(stack).getCD(level);

		if (player.hasEffect(EffectRegister.EXCITE_EFFECT.get())) {
			int lvl = player.getEffect(EffectRegister.EXCITE_EFFECT.get()).getAmplifier();
			float mult = Math.max(0, 0.9f - 0.1f * lvl);
			cd = (int) Math.floor(cd * mult);
		}
		return cd;
	}
	
	private static ItemStack getHeldStack(ItemStack stack) {
		return ImitaterCardItem.getDoubleStack(stack).getFirst();
	}
	
	private static ItemStack getPlantStack(ItemStack stack) {
		return ImitaterCardItem.getDoubleStack(stack).getSecond();
	}

	/**
	 * 体验卡绑定的挑战 bar uuid，未绑定或无此标记返回 null。
	 */
	@Nullable
	public static UUID getChallengeUuid(ItemStack stack) {
		if(stack.hasTag() && stack.getTag().contains(CHALLENGE_TAG)) {
			try {
				return UUID.fromString(stack.getTag().getString(CHALLENGE_TAG));
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		return null;
	}

	public static void setChallengeUuid(ItemStack stack, UUID uuid) {
		stack.getOrCreateTag().putString(CHALLENGE_TAG, uuid.toString());
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if(! level.isClientSide && entity instanceof ServerPlayer player) {
			final UUID challengeUuid = getChallengeUuid(stack);
			final Challenge challenge = ChallengeManager.getPlayerChallenge(player);
			//存活期与卡包锁定一致：玩家须仍处于这张卡绑定的那场挑战中
			if(challengeUuid != null && (challenge == null || ! challenge.getBarUuid().equals(challengeUuid))) {
				stack.shrink(stack.getCount());
			}
		}
	}
	
	@Override
	public int getEnchantmentValue() {
		return plantType.getRank().getEnchantPoint();
	}
	
}