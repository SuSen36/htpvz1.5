package com.hungteen.pvz.common.entity.plant.defence;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.advancement.AdvancementHandler;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.misc.drop.CoinEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantDefenderEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.world.challenge.Challenge;
import com.hungteen.pvz.common.world.challenge.ChallengeManager;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class WallNutEntity extends PlantDefenderEntity{

	private static final EntityDataAccessor<Boolean> BOWLING = SynchedEntityData.defineId(WallNutEntity.class, EntityDataSerializers.BOOLEAN);
	private static final double BOWLING_SPEED = 0.3D;
	private static final float BOWLING_SIDE_ANGLE = 45.0F;
	private static final float BOWLING_DAMAGE = 180.0F;
	private static final int BOWLING_SWITCH_CD = 10;
	private static final int BOWLING_LIFE = 180;
	private static final int BOWLING_WALL_LIMIT = 4;
	private static final int BOWLING_STRIKE_HIT = 5;

	private Direction bowlingForward = Direction.SOUTH;
	//0为直行，±1为左右两道斜行
	private int bowlingSide = 0;
	private int bowlingCD = 0;
	private int bowlingLife = BOWLING_LIFE;
	private int wallHitCount = 0;
	private int hitCount = 0;

	public WallNutEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(BOWLING, false);
	}

	@Override
	public void onSpawnedByPlayer(Player player, int sunCost) {
		super.onSpawnedByPlayer(player, sunCost);
		if(! this.level.isClientSide() && this.canBowling() && this.isInBowlingChallenge()) {
			this.setBowling(true);
			this.bowlingForward = player.getDirection();
			this.setYRot(this.bowlingForward.toYRot());
			EntityUtil.playSound(this, SoundRegister.BOWLING.get());
		}
	}

	@Override
	protected void plantTick() {
		super.plantTick();
		if(! this.level.isClientSide() && this.isBowling()) {
			this.tickBowling();
		}
	}

	private void tickBowling() {
		/* 滚出挑战范围或挑战结束即消失，否则会一直滚下去 */
		if(-- this.bowlingLife <= 0 || ! this.isInBowlingChallenge()) {
			this.discard();
			return;
		}
		/* 每 tick 按前进角重算速度：摩擦减速与上 tick 残余速度都不参与路径 */
		final float yaw = this.bowlingForward.toYRot() + this.bowlingSide * BOWLING_SIDE_ANGLE;
		final float rad = yaw * Mth.DEG_TO_RAD;
		this.setYRot(yaw);
		this.setDeltaMovement(- Mth.sin(rad) * BOWLING_SPEED, this.getDeltaMovement().y(), Mth.cos(rad) * BOWLING_SPEED);
		if(this.bowlingCD > 0) {
			-- this.bowlingCD;
			return;
		}
		final List<LivingEntity> targets = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D),
				target -> target != this && EntityUtil.canTargetEntity(this, target));
		if(! targets.isEmpty()) {
			this.hitBowlingTargets(targets);
		} else if(this.horizontalCollision) {
			if(++ this.wallHitCount >= BOWLING_WALL_LIMIT) {
				this.discard();
			} else {
				this.changeBowlingSide();
			}
		}
	}

	private void hitBowlingTargets(List<LivingEntity> targets) {
		++ this.hitCount;
		for(int i = 1; i < this.hitCount; ++ i) {
			final CoinEntity coin = EntityRegister.COIN.get().create(this.level);
			if(coin != null) {
				coin.setAmount(1);
				EntityUtil.onEntityRandomPosSpawn(this.level, coin, this.blockPosition(), 1);
			}
		}
		EntityUtil.playSound(this, SoundRegister.BOWLING_HIT.get());
		if(this.hitCount >= BOWLING_STRIKE_HIT && this.getOwnerPlayer().orElse(null) instanceof ServerPlayer player) {
			AdvancementHandler.BOWLING_STRIKE.trigger(player);
		}
		for(LivingEntity target : targets) {
			target.hurt(PVZEntityDamageSource.normal(this).setCount(this.hitCount), BOWLING_DAMAGE);
		}
		this.changeBowlingSide();
	}

	private void changeBowlingSide() {
		this.bowlingSide = this.bowlingSide == 0 ? (this.random.nextBoolean() ? 1 : - 1) : - this.bowlingSide;
		this.bowlingCD = BOWLING_SWITCH_CD;
	}

	private boolean isInBowlingChallenge() {
		if(this.level instanceof ServerLevel server) {
			return ChallengeManager.getChallengeNearBy(server, this.blockPosition())
					.map(Challenge::hasBowling).orElse(false);
		}
		return false;
	}

	private void setBowling(boolean flag) {
		this.entityData.set(BOWLING, flag);
		this.setImmuneToWeak(flag);
		this.canCollideWithPlant = ! flag;
	}

	public boolean isBowling() {
		return this.entityData.get(BOWLING);
	}

	public boolean canBowling() {
		return true;
	}

	@Override
	protected boolean shouldLockXZ() {
		return ! this.isBowling();
	}

	@Override
	public float getLife() {
		return this.getSkillValue(SkillTypes.NUT_MORE_LIFE);
	}
	
	@Override
	public float getSuperLife() {
		return 400;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.WALL_NUT;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("bowling", this.isBowling());
		compound.putInt("bowling_forward", this.bowlingForward.get3DDataValue());
		compound.putInt("bowling_side", this.bowlingSide);
		compound.putInt("bowling_life", this.bowlingLife);
		compound.putInt("bowling_wall_count", this.wallHitCount);
		compound.putInt("bowling_hit_count", this.hitCount);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("bowling")) {
			this.setBowling(compound.getBoolean("bowling"));
		}
		if(compound.contains("bowling_forward")) {
			this.bowlingForward = Direction.from3DDataValue(compound.getInt("bowling_forward"));
		}
		if(compound.contains("bowling_side")) {
			this.bowlingSide = compound.getInt("bowling_side");
		}
		if(compound.contains("bowling_life")) {
			this.bowlingLife = compound.getInt("bowling_life");
		}
		if(compound.contains("bowling_wall_count")) {
			this.wallHitCount = compound.getInt("bowling_wall_count");
		}
		if(compound.contains("bowling_hit_count")) {
			this.hitCount = compound.getInt("bowling_hit_count");
		}
	}

}