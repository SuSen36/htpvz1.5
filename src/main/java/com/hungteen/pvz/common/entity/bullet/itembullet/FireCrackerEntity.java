package com.hungteen.pvz.common.entity.bullet.itembullet;

import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.item.ItemRegister;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireCrackerEntity extends PVZItemBulletEntity{

	private static final float SPEED = 1.5F;
	protected Entity target = null;
	
	public FireCrackerEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
		this.setNoGravity(false);
	}
	
	public FireCrackerEntity(Level worldIn, LivingEntity owner) {
		super(EntityRegister.FIRE_CRACKER.get(), worldIn, owner);
		this.setNoGravity(false);
	}
	
	@Override
	public void tick() {
		super.tick();
		if(! level.isClientSide() && EntityUtil.isEntityValid(target)) {
			this.shoot(this.target);
		}
	}

	public void shoot(Vec3 vec) {
		this.setDeltaMovement(vec.scale(SPEED));
	}
	
	public void shoot(Entity target) {
		this.target = target;
		Vec3 vec = target.position().subtract(this.position()).normalize();
		this.shoot(vec);
	}
	
	@Override
	protected void onImpact(HitResult result) {
		boolean flag = false;
		if (result.getType() == HitResult.Type.ENTITY) {
			Entity target = ((EntityHitResult) result).getEntity();
			if (this.shouldHit(target)) {
				target.invulnerableTime = 0;
				this.dealDamage(target); // attack 
				flag = true;
			}
		}
		this.level.broadcastEntityEvent(this, (byte) 3);
		if (flag) {
			this.discard();
		} else if(! this.checkLive(result)) {
			this.dealDamage(null);
			this.discard();
		}
	}
	
	private void dealDamage(Entity target) {
		if(! level.isClientSide()) {
			EntityUtil.playSound(this, SoundRegister.POTATO_MINE.get());
		    float range = 3F;
		    EntityUtil.getTargetableEntities(this.getOwnerOrSelf(), EntityUtil.getEntityAABB(this, range, range)).forEach((entity) -> {
			    entity.hurt(PVZEntityDamageSource.explode(this, this.getThrower()), this.getAttackDamage());
		    });
		    for(int i = 0;i < 3; ++ i) {
			    EntityUtil.spawnParticle(this, ParticleTypes.EXPLOSION);
		    }
		} 
	}
	
	@Override
	protected int getMaxLiveTick() {
		return 50;
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("target_entity_id")) {
			this.target = level.getEntity(compound.getInt("target_entity_id"));
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if(this.target != null) {
			compound.putInt("target_entity_id", this.target.getId());
		}
	}
	
	@Override
	public ItemStack getItem() {
		return new ItemStack(ItemRegister.FIRE_CRACKER.get());
	}

}