package com.hungteen.pvz.common.entity.plant;

import com.hungteen.pvz.api.enums.MetalTypes;
import com.hungteen.pvz.api.enums.PVZGroupType;
import com.hungteen.pvz.api.interfaces.IAlmanacEntry;
import com.hungteen.pvz.api.paz.IPlantEntity;
import com.hungteen.pvz.api.paz.IPlantInfo;
import com.hungteen.pvz.api.types.*;
import com.hungteen.pvz.client.particle.ParticleRegister;
import com.hungteen.pvz.common.advancement.trigger.PlantSuperTrigger;
import com.hungteen.pvz.common.entity.AbstractPAZEntity;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.ai.goal.PVZLookRandomlyGoal;
import com.hungteen.pvz.common.entity.misc.drop.SunEntity;
import com.hungteen.pvz.common.entity.plant.defence.PumpkinEntity;
import com.hungteen.pvz.common.entity.plant.enforce.SquashEntity;
import com.hungteen.pvz.common.entity.plant.explosion.DoomShroomEntity;
import com.hungteen.pvz.common.entity.plant.light.GoldLeafEntity;
import com.hungteen.pvz.common.entity.plant.spear.SpikeWeedEntity;
import com.hungteen.pvz.common.entity.zombie.grass.TombStoneEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.item.spawn.card.PlantCardItem;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.potion.EffectRegister;
import com.hungteen.pvz.utils.AlgorithmUtil;
import com.hungteen.pvz.utils.ConfigUtil;
import com.hungteen.pvz.utils.EffectUtil;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.enums.PAZAlmanacs;
import com.hungteen.pvz.utils.interfaces.ICanAttract;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class PVZPlantEntity extends AbstractPAZEntity implements IPlantEntity, IPlantCarrier {

	private static final EntityDataAccessor<Integer> SUPER_TIME = SynchedEntityData.defineId(PVZPlantEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ATTACK_TIME = SynchedEntityData.defineId(PVZPlantEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> GOLD_TIME = SynchedEntityData.defineId(PVZPlantEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BOOST_TIME = SynchedEntityData.defineId(PVZPlantEntity.class, EntityDataSerializers.INT);
	//plant states flags.
	protected static final int LADDER_FLAG = 0;
	protected static final int CHARM_FLAG = 1;
	protected static final int SLEEP_FLAG = 2;
	protected static final int SUPER_FLAG = 3;
	//handle plant weak, place on wrong block.
	private static final int PLANT_WEAK_CD = 10;
	protected boolean isImmuneToWeak = false;
	protected int weakTime = 0;
	//handle plant collide with other plants.
	public boolean canCollideWithPlant = true;
	//handle plant sleep.
	public int sleepTime = 0;
	//handle plant itself.
    protected IPlantInfo innerPlant;
	protected boolean canBeRemove = true;
	protected boolean canHelpAttack = true;
	protected boolean root = true;
	@Nullable
	private UUID challengeUuid;

	public PVZPlantEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.innerPlant = new PlantInfo(this.getPlantType());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(SUPER_TIME, 0);
		entityData.define(ATTACK_TIME, 0);
		entityData.define(GOLD_TIME, 0);
		entityData.define(BOOST_TIME, 0);
	}

	/**
	 * {@link EntityRegister#addEntityAttributes(EntityAttributeCreationEvent)}
	 */
	public static AttributeSupplier createPlantAttributes() {
		return AbstractPAZEntity.createPAZAttributes()
				.add(Attributes.MAX_HEALTH, 20)
				.add(Attributes.FOLLOW_RANGE, 30.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1)
				.add(Attributes.MOVEMENT_SPEED, 0)
				.build();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new PVZLookRandomlyGoal(this));
	}
	
	/**
	 * spawned by player.
	 */
	@Override
	public void onSpawnedByPlayer(@Nullable Player player, int sunCost) {
		super.onSpawnedByPlayer(player, sunCost);
		this.getPlantInfo().ifPresent(info -> {
			info.setSunCost(sunCost);
		});
	}

	@Override
	public void tick(){
		if(! this.level.isClientSide() && this.onGround && this.getVehicle() == null && !this.root) {
			this.setDeltaMovement(new Vec3(0,this.getDeltaMovement().y,0));
			//*0.6.4 prevent plants from getting knocked back. May this cause problems?
		}
		super.tick();
	}

	@Override
	public void pazTick() {
		super.pazTick();
		this.level.getProfiler().push("PVZ Plant Tick");
		this.plantTick();
		this.level.getProfiler().pop();

		if (this.canNormalUpdate()) {
			this.level.getProfiler().push("PVZ Normal Plant Tick");
			this.normalPlantTick();
			this.level.getProfiler().pop();
		}

		if (!level.isClientSide()){
			getOwnerPlayer().ifPresent(p -> {
				Vec3 self = this.position();
				BlockPos owner = ((ServerPlayer) p).getRespawnPosition() == null ? new BlockPos(0, 0, 0): ((ServerPlayer) p).getRespawnPosition();
				this.canDespawn = this.tickCount > ConfigUtil.getPlantMinimumTick() &&
						owner.distToCenterSqr(self) > ConfigUtil.despawnOwnedEntityRange() * ConfigUtil.despawnOwnedEntityRange();
			});
		}
	}
	
	/**
	 * check can run {@link #normalPlantTick()} or not.
	 */
	@Override
	public boolean canNormalUpdate() {
		return ! this.hasMetal() && !this.isPlantSleeping() && super.canNormalUpdate();
	}

	/**
	 * plant tick.
	 * {@link #aiStep()}
	 */
	protected void plantTick() {
		/* check plant wilt. */
		if (!this.level.isClientSide()) {
			if (this.shouldWilt() && this.weakTime <= 0) {
				this.weakTime = PLANT_WEAK_CD;
				this.hurt(PVZEntityDamageSource.PLANT_WILT, EntityUtil.getMaxHealthDamage(this, 0.35F));
			}
			this.weakTime = Math.max(0, this.weakTime - 1);
		}
		// super mode or boost time or sleep time
		if (!this.level.isClientSide()) {
			//handle super mode.
			this.setSuperTime(Math.max(0, this.getSuperTime() - 1));
			//handle boost mode(no use for currrent version).
			this.setBoostTime(Math.max(0, this.getBoostTime() - 1));
			//handler plant's sleep.
			if (this.shouldPlantRegularSleep()) {
				this.sleepTime = this.sleepTime <= 1 ? this.sleepTime + 1 : this.sleepTime - 1;
			} else {
				this.sleepTime = this.sleepTime <= -1 ? this.sleepTime + 1 : this.sleepTime - 1;
			}
			if(! this.isPlantSleeping() && this.sleepTime > 0) {
				this.setPlantSleeping(true);
			}
			if(this.isPlantSleeping() && this.sleepTime <= 0) {
				this.setPlantSleeping(false);
			}
		}
		// spawn sleep particle
		if (level.isClientSide() && this.isPlantSleeping() && this.tickCount % 20 == 0) {
			EntityUtil.spawnSpeedParticle(this, ParticleRegister.SLEEP.get(), 0.05F);
		}
		// lock the x and z of plant
		if (this.shouldLockXZ()) {
			if (this.getVehicle() == null) {
				BlockPos pos = this.blockPosition();
				this.setPos(pos.getX() + 0.5, this.getY(), pos.getZ() + 0.5);
			}
		}
		if (!level.isClientSide()) {//set float on water.
			if (this.getPlantType().isWaterPlant() && this.isInWater()) {
				Vec3 vec = this.getDeltaMovement();
				double speedY = Math.min(vec.y, 0.05D);
				this.setDeltaMovement(vec.x, speedY, vec.z);
			}
		}
	}

	/**
	 * tick when plant is normal state.
	 * (not be frozen or butter and so on).
	 * {@link #aiStep()}
	 */
	protected void normalPlantTick() {
		/* tick when plant is place on gold tile, and produce sun */
		if (!this.level.isClientSide() && this.getGoldTime() < GoldLeafEntity.GOLD_GEN_CD) {
			Block block = this.level.getBlockState(this.blockPosition().below()).getBlock();
			int lvl = GoldLeafEntity.getBlockGoldLevel(block);
			if (lvl <= 0) {//not gole tile.
				this.setGoldTime(0);
				return;
			}
			this.setGoldTime(this.getGoldTime() + 1);
			if (this.getGoldTime() >= GoldLeafEntity.GOLD_GEN_CD) {
				this.setGoldTime(0);
				SunEntity sun = EntityRegister.SUN.get().create(level);
                if (sun != null) {
                    sun.setAmount(GoldLeafEntity.getGoldGenAmount(lvl));
                }
                EntityUtil.onEntityRandomPosSpawn(level, sun, blockPosition(), 2);
				EntityUtil.playSound(this, SoundEvents.EXPERIENCE_ORB_PICKUP);
			}
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(
                Pair.of(PAZAlmanacs.HEALTH, this.getSkillValue(SkillTypes.PLANT_MORE_LIFE))
        );
	}

	/**
	 * check if the plant can stand on the current position.
	 * {@link #plantTick()}
	 */
	public boolean shouldWilt() {
		if (! this.isImmuneToWeak() && this.getVehicle() == null) {//fit check condition and is allowed to wilt.
			if(this.getPlantType().isWaterPlant()) {//on ground, not in water.
				return this.isOnGround() && ! this.isInWater() && ! this.level.getFluidState(blockPosition()).getType().is(FluidTags.WATER);
			}
			if(this.isInWaterOrBubble()) {//can not stay in water.
				return true;
			}
		    final BlockPos pos = Math.abs(this.getY() - this.blockPosition().getY()) <= 0.01D ? this.blockPosition().below() : this.blockPosition();
		    return ! this.getPlantType().getPlacement().canPlaceOnBlock(level.getBlockState(pos).getBlock());
		}
		return false;
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return ! this.hasMetal();
	}

	@Override
	public boolean canBeAttractedBy(ICanAttract defender) {
		return true;
	}

	@Override
	public boolean canOuterDefend(DamageSource source) {
		//pumpkin can not defend parabola damage.
		if(source instanceof PVZEntityDamageSource && ((PVZEntityDamageSource) source).isParabola()){
			return false;
		}
		return super.canOuterDefend(source);
	}

	@Override
	public void attractBy(ICanAttract defender) {
		if(defender instanceof LivingEntity){
			this.setTarget((LivingEntity) defender);
		}
	}

	/**
	 * use for shrooms' sleep ,need check for later coffee bean update.
	 * {@link #plantTick()}
	 */
	protected boolean shouldPlantRegularSleep() {
		if (this.getPlantType().isShroomPlant()) {
			return level.isDay();
		}
		return false;
	}
	
	/**
	 * check can zombie add effect.
	 * {@link EntityUtil#addPotionEffect(Entity, MobEffectInstance)}
	 */
	public void checkAndAddPotionEffect(MobEffectInstance effect) {
		if (effect.getEffect() == EffectRegister.FROZEN_EFFECT.get() && !this.canBeFrozen()) {
			return;
		}
		if (effect.getEffect() == EffectRegister.BUTTER_EFFECT.get() && !this.canBeButtered()) {
			return;
		}
		this.addEffect(effect);
	}
	
	/**
	 * {@link DoomShroomEntity#startBomb(boolean)}
	 */
	public static void clearLadders(LivingEntity entity, AABB aabb) {
		entity.level.getEntitiesOfClass(PVZPlantEntity.class, aabb, target -> {
			return target.hasMetal() && ! EntityUtil.checkCanEntityBeAttack(entity, target);
		}).forEach(plant -> {
			plant.setMetal(false);
		});
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		entityIn.invulnerableTime = 0;
		return super.doHurtTarget(entityIn);
	}

	/**
	 * lock the movement of plant.
	 * {@link #plantTick()}
	 */
	protected boolean shouldLockXZ() {
		return true;
	}

	@Override
	public void push(Entity entityIn) {
		if (!this.isPassengerOfSameVehicle(entityIn)) {
			if (!entityIn.noPhysics && !this.noPhysics) {
				double d0 = entityIn.getX() - this.getX();
				double d1 = entityIn.getZ() - this.getZ();
				double d2 = Mth.absMax(d0, d1);
				if (d2 >= 0.009999999776482582D) {// collide from out to in,add velocity to out
					d2 = Mth.sqrt((float) d2);
					d0 = d0 / d2;
					d1 = d1 / d2;
					double d3 = 1.0D / d2;
					if (d3 > 1.0D) {
						d3 = 1.0D;
					}
					d0 = d0 * d3;
					d1 = d1 * d3;
					d0 = d0 * 0.05000000074505806D;
					d1 = d1 * 0.05000000074505806D;
					if (!entityIn.isVehicle()) {
						entityIn.push(d0, 0.0D, d1);
					}
				} else {
					if (this instanceof PVZPlantEntity && entityIn instanceof PVZPlantEntity
							&& !EntityUtil.canTargetEntity(this, entityIn)) {
						if (this.tickCount >= entityIn.tickCount) {
							this.hurt(PVZEntityDamageSource.PLANT_WILT, EntityUtil.getMaxHealthDamage(this, 0.5F));
						}
					}
				}
			}
		}
	}

	@Override
	protected void pushEntities() {
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
		if (!list.isEmpty()) {
			int i = this.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
			if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
				int j = 0;
                for (LivingEntity living : list) {
                    if (!living.isPassenger()) {
                        ++j;
                    }
                }
				if (j > i - 1) {
					this.hurt(DamageSource.CRAMMING, 6.0F);
				}
			}
            for (LivingEntity target : list) {
                if (!this.is(target) && shouldCollideWithEntity(target)) {// can collide with
                    this.doPush(target);
                }
            }
		}
	}

	/**
	 * common plants collide with common plants, mobs who target them, tombstone.
	 * {@link #pushEntities()}
	 */
	protected boolean shouldCollideWithEntity(LivingEntity target) {
		if (target instanceof PVZPlantEntity) {
			if (!this.canCollideWithPlant || !((PVZPlantEntity) target).canCollideWithPlant) {
				return false;
			}
			if (target instanceof SquashEntity) {
				return !EntityUtil.canTargetEntity(this, target);
			}
			if (target instanceof SpikeWeedEntity) {
				return !EntityUtil.canTargetEntity(this, target);
			}
			return true;
		}
		if (target instanceof Mob) {
			if (((Mob) target).getTarget() == this) {
				return true;
			}
            return target instanceof TombStoneEntity;
		}
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return this.isPlantImmuneTo(source) && source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer() && ! source.equals(PVZEntityDamageSource.PLANT_WILT);
	}

	/**
	 * {@link #isInvulnerableTo(DamageSource)}
	 */
	public boolean isPlantImmuneTo(DamageSource source) {
		return this.isPlantInSuperMode();
	}

	@Override
	public boolean canBreatheUnderwater() {
		return this.getPlantType().isWaterPlant();
	}
	
	/**
	 * {@link EntityUtil#canHelpAttackOthers(Entity)}
	 */
	public boolean canHelpAttack() {
		return this.canHelpAttack;
	}

	@Override
	protected float getLife() {
		return this.getSkillValue(SkillTypes.PLANT_MORE_LIFE);
	}
	
	/**
	 * on charmed by entity.
	 */
	@Override
	public void onCharmedBy(LivingEntity entity) {
		if(this.canBeCharmed()) {
			this.setCharmed(! this.isCharmed());
		}
	}
	
	/**
	 * use to start plant super mode.
	 */
	public void startSuperMode(boolean first) {
		this.setSuperTime(this.getSuperTimeLength());
		this.heal(this.getMaxHealth());
		this.setInSuperState(true);
		if (first) {
			Player player = EntityUtil.getEntityOwner(level, this);
			if (player instanceof ServerPlayer) {
				PlantSuperTrigger.INSTANCE.trigger((ServerPlayer) player, this);
			}
		}
	}

	/**
	 * {@link PlantCardItem#checkSunAndHealPlant(Player, PVZPlantEntity, PlantCardItem, ItemStack)}
	 */
	public void onHealBy(IPlantType plantType, float percent) {
		this.heal(this.getLife() * percent);
		this.addEffect(EffectUtil.viewEffect(MobEffects.REGENERATION, 60, 0));
		this.getSpawnSound().ifPresent(s -> EntityUtil.playSound(this, s));
	}


	public void onPlantUpgrade(PVZPlantEntity plantEntity) {
		// keep rotation of plant
		plantEntity.setYRot(this.getYRot());
		plantEntity.yBodyRot = this.yBodyRot;
		plantEntity.yHeadRot = this.yHeadRot;
		// keep sleep of plant
		plantEntity.sleepTime = this.sleepTime;
		// remove old plant itself
		this.discard();
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		/* pumpkin vehicle receives the damage aimed at the plant it wraps, by attack direction. */
		if(this.getVehicle() instanceof PumpkinEntity pumpkin) {
			final Entity direct = source.getDirectEntity();
			if(direct != null && ! source.isBypassArmor()) {
				final Vec3 pos = direct.position().subtract(this.position());
				if(pos.y < 0 || pos.y * pos.y / (pos.x * pos.x + pos.z * pos.z) < 3) {
					return pumpkin.hurt(source, amount);
				}
			}
		}
		return super.hurt(source, amount);
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec3d, InteractionHand hand) {
		final ItemStack stack = player.getItemInHand(hand);
		if (! level.isClientSide()) {
			if (stack.getItem() instanceof PlantCardItem item) {// plant card right click plant entity
                /* short-circuit order defines the interaction priority: heal > upgrade > carry(takeover) > hold. */
                if(PlantCardItem.checkSunAndHealPlant(player, this, item, stack)
                		|| PlantCardItem.checkSunAndUpgradePlant(player, this, item, stack)
                		|| PlantCardItem.checkSunAndCarryPlant(player, this, item, stack)
                		|| PlantCardItem.checkSunAndHoldPlant(player, this, item, stack)) {
					return InteractionResult.SUCCESS;
				}
			}
		} else if (stack.getItem() instanceof PlantCardItem) {
			return InteractionResult.SUCCESS;
		}
		return super.interactAt(player, vec3d, hand);
	}

	/**
	 * whether this plant can serve as a container that other plants ride on.
	 * e.g. LilyPad & FlowerPot, or shroom plants that carry coffee bean.
	 */
	public boolean canHoldPlant() {
		return this.getPlantType().isShroomPlant() && this.isPlantSleeping();
	}

	/**
	 * whether the given plant type can be placed onto this plant (as a riding passenger).
	 * sleeping shroom plant only carries coffee bean, container plant can not hold the plant of same type.
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean canPlantOnMe(IPlantType type) {
		if(this.getPlantType().isShroomPlant()) {
			return this.isPlantSleeping() && type == PVZPlants.COFFEE_BEAN;
		}
		final ICardPlacement carryPlacement = this.getCarryPlacement();
		return type != this.getPlantType() && type != PVZPlants.COFFEE_BEAN
				&& (carryPlacement == null || carryPlacement.canPlantOn(type.getPlacement()));
	}

	/**
	 * plant type used when this pre-created plant checks whether it can ride a container;
	 * imitater answers with the type of the card it mimics, since it has not transformed yet.
	 */
	protected IPlantType getRidePlantType() {
		return this.getPlantType();
	}

	/**
	 * try to mount this plant onto a container plant while both are pre-created and not added to world.
	 * @return true when the riding relation is established.
	 */
	public boolean mountPlantOn(PVZPlantEntity container) {
		final IPlantType rideType = this.getRidePlantType();
		if(container.canHoldPlant() && container.getPassengers().isEmpty()
				&& rideType.canBeHold() && container.canPlantOnMe(rideType)) {
			this.moveTo(container.getX(), container.getY() + container.getPassengersRidingOffset(),
					container.getZ(), container.getYRot(), 0.0F);
			this.startRiding(container);
			return true;
		}
		return false;
	}

	/* misc get */
	
	public boolean canBeUpgrade(Player player) {
		return this.getPlantType().getUpgradeTo().isPresent();
	}
	
	public boolean isPlantInSuperMode() {
		return this.getSuperTime() > 0;
	}

	private boolean hasSuperMode() {
		return this.getSuperTimeLength() > 0;
	}

	public boolean isPlantInBoost() {
		return this.getBoostTime() > 0;
	}

	/**
	 * check can start super mode currently.
	 */
	public boolean canStartSuperMode() {
		return this.canNormalUpdate() && this.hasSuperMode() && !this.isPlantInSuperMode();
	}

	public int getSuperTimeLength(){
		return 0;
	}
	
    /**
     * {@link #plantTick()}
     */
	public Optional<Player> getOwnerPlayer() {
		if(! this.hasOwner()) {
			return Optional.empty();
		}
		if(! EntityUtil.isEntityValid(this.ownerPlayer)) {
			this.ownerPlayer = this.level.getPlayerByUUID(this.getOwnerUUID().get());
		}
		return Optional.ofNullable(this.ownerPlayer);
	}
	
	@Override
	public PVZGroupType getEntityGroupType() {
		return this.isCharmed() ? PVZGroupType.ZOMBIES : PVZGroupType.PLANTS;
	}
	
	public boolean hasOwner() {
		return this.getOwnerUUID().isPresent();
	}

	@Override
	public Optional<SoundEvent> getSpawnSound() {
		return Optional.of(this.getPlantType().isWaterPlant() ? SoundRegister.PLACE_PLANT_WATER.get() : SoundRegister.PLACE_PLANT_GROUND.get());
	}

	/* data */
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("plant_super_time", this.getSuperTime());
		compound.putInt("plant_attack_time", this.getAttackTime());
		compound.putInt("plant_gold_time", this.getGoldTime());
		compound.putInt("plant_boost_time", this.getBoostTime());
		compound.putInt("plant_sleep_time", this.sleepTime);
		PlantInfo.write(this.innerPlant, compound, "inner_plant_info");
		compound.putBoolean("immune_to_weak", this.isImmuneToWeak);
		if(this.challengeUuid != null) {
			compound.putUUID(PlantCardItem.CHALLENGE_TAG, this.challengeUuid);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("plant_super_time")) {
			this.setSuperTime(compound.getInt("plant_super_time"));
		}
		if (compound.contains("plant_attack_time")) {
			this.setAttackTime(compound.getInt("plant_attack_time"));
		}
		if (compound.contains("plant_gold_time")) {
			this.setGoldTime(compound.getInt("plant_gold_time"));
		}
		if (compound.contains("plant_boost_time")) {
			this.setBoostTime(compound.getInt("plant_boost_time"));
		}
		if(compound.contains("plant_sleep_time")) {
			this.sleepTime = compound.getInt("plant_sleep_time");
		}
		PlantInfo.read(this.innerPlant, compound, "inner_plant_info");
		if (compound.contains("immune_to_weak")) {
			this.isImmuneToWeak = compound.getBoolean("immune_to_weak");
		}
		if(compound.hasUUID(PlantCardItem.CHALLENGE_TAG)) {
			this.challengeUuid = compound.getUUID(PlantCardItem.CHALLENGE_TAG);
		}
		if (compound.contains("plant_state")) {
			this.setPAZState(compound.getInt("plant_state"));
		}
	}

	/* getter setter */

	public Optional<IPlantInfo> getPlantInfo() {
		return Optional.ofNullable(this.innerPlant);
	}

	@Nullable
	public UUID getChallengeUuid() {
		return this.challengeUuid;
	}

	public void setChallengeUuid(UUID challengeUuid) {
		this.challengeUuid = challengeUuid;
	}

	public void setImmuneToWeak(boolean is) {
		this.isImmuneToWeak = is;
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isImmuneToWeak() {
		return this.isImmuneToWeak;
	}

	public int getBoostTime() {
		return entityData.get(BOOST_TIME);
	}

	public void setBoostTime(int time) {
		entityData.set(BOOST_TIME, time);
	}

	public int getGoldTime() {
		return entityData.get(GOLD_TIME);
	}

	public void setGoldTime(int cd) {
		entityData.set(GOLD_TIME, cd);
	}

	public int getAttackTime() {
		return entityData.get(ATTACK_TIME);
	}

	public void setAttackTime(int cd) {
		entityData.set(ATTACK_TIME, cd);
	}

	public void setSuperTime(int time) {
		entityData.set(SUPER_TIME, time);
	}

	public int getSuperTime() {
		return entityData.get(SUPER_TIME);
	}

	@Override
	public boolean hasMetal() {
		return AlgorithmUtil.BitOperator.hasBitOne(this.getPAZState(), LADDER_FLAG);
	}

	public void setMetal(boolean flag) {
		this.setPAZState(AlgorithmUtil.BitOperator.setBit(this.getPAZState(), LADDER_FLAG, flag));
	}
	
	@Override
	public boolean isCharmed() {
		return AlgorithmUtil.BitOperator.hasBitOne(this.getPAZState(), CHARM_FLAG);
	}

	public void setCharmed(boolean flag) {
		this.setPAZState(AlgorithmUtil.BitOperator.setBit(this.getPAZState(), CHARM_FLAG, flag));
	}
	
	public boolean isPlantSleeping() {
		return AlgorithmUtil.BitOperator.hasBitOne(this.getPAZState(), SLEEP_FLAG);
	}
	
	public void setPlantSleeping(boolean flag) {
		this.setPAZState(AlgorithmUtil.BitOperator.setBit(this.getPAZState(), SLEEP_FLAG, flag));
	}

	public boolean isInSuperState() {
		return AlgorithmUtil.BitOperator.hasBitOne(this.getPAZState(), SUPER_FLAG);
	}

	public void setInSuperState(boolean flag) {
		this.setPAZState(AlgorithmUtil.BitOperator.setBit(this.getPAZState(), SUPER_FLAG, flag));
	}
	
	@Override
	public void increaseMetal() {
		this.setMetal(true);
	}

	@Override
	public void decreaseMetal() {
		this.setMetal(false);
	}

	@Override
	public MetalTypes getMetalType() {
		return MetalTypes.LADDER;
	}

	public IEssenceType getPlantEssenceType() {
		return this.getPlantType().getEssence();
	}

	@Override
	public IPAZType getPAZType() {
		return this.getPlantType();
	}

	/**
	 * creative mode middle-click pick on plant entity gives its summon card.
	 * {@link net.minecraft.client.Minecraft#pickBlock()}
	 */
	@Nullable
	@Override
	public ItemStack getPickResult() {
		return this.getPlantType().getSummonCard()
				.map(Item::getDefaultInstance)
				.orElse(null);
	}

	/**
	 * match entity with plant type.
	 */
	public abstract IPlantType getPlantType();

}