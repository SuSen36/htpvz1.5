package com.hungteen.pvz.common.entity.bullet;

import com.hungteen.pvz.api.paz.IPAZEntity;
import com.hungteen.pvz.client.particle.ParticleRegister;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class MelonEntity extends PultBulletEntity {

	private static final EntityDataAccessor<Integer> MELON_STATE = SynchedEntityData.defineId(MelonEntity.class, EntityDataSerializers.INT);
	private static final int CHILL_FROZEN_TICK = 400;
	private Entity attackEntity = null;
	
	public MelonEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}

	public MelonEntity(Level worldIn, LivingEntity shooter) {
		super(EntityRegister.MELON.get(), worldIn, shooter);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(MELON_STATE, MelonStates.NORMAL.ordinal());
	}
	
	@Override
	protected void dealDamage(Entity target) {
		if(this.getMelonState() == MelonStates.ICE) {
			PVZEntityDamageSource source = PVZEntityDamageSource.winterMelon(this, this.getThrower());
			target.hurt(source, this.getAttackDamage());
			if(target.canFreeze() && (!(target instanceof IPAZEntity) || ((IPAZEntity) target).canBeCold()) && target.getTicksFrozen() < CHILL_FROZEN_TICK) {
				target.setTicksFrozen(CHILL_FROZEN_TICK);
			}
		} else{
			target.hurt(PVZEntityDamageSource.melon(this, this.getThrower()), this.getAttackDamage());
		}
		this.attackEntity = target;
		this.dealSplashDamage();
	}
	
	@Override
	protected void onHitBlock() {
		this.dealSplashDamage();
	}
	
	/**
	 * {@link #onHitBlock()}
	 * {@link #dealDamage(Entity)}
	 */
	public void dealSplashDamage() {
		final float range = 3F;
		EntityUtil.getTargetableEntities(this.getOwnerOrSelf(), EntityUtil.getEntityAABB(this, range, range)).forEach(entity -> {
			if(! entity.is(attackEntity) && this.shouldHit(entity)) {
				if(this.getMelonState() == MelonStates.ICE) {
					PVZEntityDamageSource source = PVZEntityDamageSource.winterMelon(this, this.getThrower());
					entity.hurt(source, this.getAttackDamage() / 2);
					if(entity.canFreeze() && (!(entity instanceof IPAZEntity) || ((IPAZEntity) entity).canBeCold()) && entity.getTicksFrozen() < CHILL_FROZEN_TICK) {
						entity.setTicksFrozen(CHILL_FROZEN_TICK);
					}
				} else {
					PVZEntityDamageSource source = PVZEntityDamageSource.melon(this, this.getThrower());
				    entity.hurt(source, this.getAttackDamage() / 2);
				}
			}
		});
		for(int i = 0; i < 10; ++ i) {
			EntityUtil.spawnParticle(this, (this.getMelonState() == MelonStates.ICE ? ParticleRegister.FROZEN_MELON_SLICE.get() : ParticleRegister.MELON_SLICE.get()));
		}
		EntityUtil.playSound(this, SoundRegister.MELON_HIT.get());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("melon_state")) {
			this.setMelonState(MelonStates.values()[compound.getInt("melon_state")]);
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("melon_state", this.getMelonState().ordinal());
	}
	
	public void setMelonState(MelonStates type) {
		this.entityData.set(MELON_STATE, type.ordinal());
	}
	
	public MelonStates getMelonState() {
		return MelonStates.values()[this.entityData.get(MELON_STATE)];
	}
	
	public enum MelonStates {
		NORMAL,
		ICE,
	}
	
}