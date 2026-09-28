package com.hungteen.pvz.common.entity.misc;

import com.hungteen.pvz.PVZConfig;
import com.hungteen.pvz.common.entity.AbstractOwnerEntity;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.plant.flame.JalapenoEntity;
import com.hungteen.pvz.common.entity.plant.ice.IceShroomEntity;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ElementBallEntity extends AbstractOwnerEntity {

	private static final EntityDataAccessor<Integer> ELEMENTS = SynchedEntityData.defineId(ElementBallEntity.class, EntityDataSerializers.INT);
	protected float speed = 0.25F;
	private int removeTick = 0;

	public ElementBallEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
		this.maxUpStep = 2.0F;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ELEMENTS, ElementTypes.FLAME.ordinal());
	}

	/**
	 * {@link IceShroomEntity#startBomb(boolean)}
	 * {@link JalapenoEntity#startBomb(boolean)}
	 */
	public static void killElementBalls(LivingEntity attacker, float range, ElementTypes type) {
		killElementBalls(attacker, EntityUtil.getEntityAABB(attacker, range, range), type);
	}

	public static void killElementBalls(LivingEntity attacker, AABB aabb, ElementTypes type) {
		attacker.level.getEntitiesOfClass(ElementBallEntity.class, aabb, target -> {
			return target.getElementBallType() == type && EntityUtil.checkCanEntityBeAttack(attacker, target);
		}).forEach(Entity::discard);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickMove();
		this.tickCollision();
		if(! level.isClientSide()) {
			if(this.tickCount >= PVZConfig.COMMON_CONFIG.EntitySettings.EntityLiveTick.ElementBallLiveTick.get()) {
				this.discard();
				return ;
			}
			if(this.getOwner() == null){
				++ this.removeTick;
				if(this.removeTick >= 15){
					this.discard();
				}
			} else{
				this.removeTick = 0;
			}
		}
	}

	public void shoot(Vec3 direction){
		this.setDeltaMovement(direction.normalize().scale(this.speed));
	}

	private void tickCollision() {
		if(! level.isClientSide() && this.tickCount % 10 == 0) {
			EntityUtil.getTargetableEntities(this, this.getBoundingBox().inflate(1F)).forEach(target -> {
				if(target instanceof PVZPlantEntity) {
					target.hurt(this.getAttackSource(), EntityUtil.getCurrentMaxHealth((PVZPlantEntity) target));
				} else if(target instanceof PVZZombieEntity) {
					target.hurt(this.getAttackSource(), EntityUtil.getCurrentMaxHealth((PVZZombieEntity) target));
				} else {
					target.hurt(PVZEntityDamageSource.causeCrushDamage(this), 5);
					target.setDeltaMovement(target.position().subtract(this.position()).normalize().scale(this.speed));
				}
			});
		}
	}

	private PVZEntityDamageSource getAttackSource() {
		if(this.getElementBallType() == ElementTypes.FLAME) {
			return PVZEntityDamageSource.causeFlameDamage(this, this.getOwner());
		}
		return PVZEntityDamageSource.causeIceDamage(this, this.getOwner());
	}

	public void setSpeed(float speed){
		this.speed = speed;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("element_ball_type")) {
			this.setElementBallType(ElementTypes.values()[compound.getInt("element_ball_type")]);
		}
		if(compound.contains("element_speed")) {
			this.speed = compound.getFloat("element_speed");
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("element_ball_type", this.getElementBallType().ordinal());
		compound.putFloat("element_speed", this.speed);
	}

	public void setElementBallType(ElementTypes type) {
		this.entityData.set(ELEMENTS, type.ordinal());
	}

	public ElementTypes getElementBallType() {
		return ElementTypes.values()[this.entityData.get(ELEMENTS)];
	}

	public enum ElementTypes {
		FLAME,
		ICE,
	}

}