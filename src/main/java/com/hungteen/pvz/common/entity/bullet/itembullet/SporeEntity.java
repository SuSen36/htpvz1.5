package com.hungteen.pvz.common.entity.bullet.itembullet;

import com.hungteen.pvz.client.particle.ParticleRegister;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.plant.toxic.PuffShroomEntity;
import com.hungteen.pvz.common.entity.plant.toxic.SeaShroomEntity;
import com.hungteen.pvz.common.item.ItemRegister;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.utils.WorldUtil;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SporeEntity extends PVZItemBulletEntity{

	public SporeEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}
	
	public SporeEntity(Level worldIn, LivingEntity living) {
		super(EntityRegister.SPORE.get(), worldIn, living);
	}

	@Override
	public void tick() {
		super.tick();
		if(level.isClientSide()) {
			for(int i = 0; i < 3; ++i) {
				WorldUtil.spawnRandomSpeedParticle(level, ParticleRegister.SPORE.get(), this.position(), 0);
	        }
		}
	}
	
	@Override
	protected int getMaxLiveTick() {
		if(this.getThrower() instanceof PuffShroomEntity || this.getThrower() instanceof SeaShroomEntity) {
			return 10;
		}
		return 24;
	}
	
	@Override
	public ItemStack getItem() {
		return new ItemStack(ItemRegister.SPORE.get());
	}

	@Override
	protected void onImpact(HitResult result) {
		boolean flag = false;
		if (result.getType() == HitResult.Type.ENTITY) {
			Entity target = ((EntityHitResult) result).getEntity();
			if (this.shouldHit(target)) {
				target.invulnerableTime = 0;
				this.dealSporeDamage(target); // attack 
				flag = true;
			}
		}
		this.level.broadcastEntityEvent(this, (byte) 3);
		if (flag || !this.checkLive(result)) {
this.discard();
		}
	}
	
	private void dealSporeDamage(Entity target) {
		target.hurt(PVZEntityDamageSource.spore(this, this.getThrower()), this.attackDamage);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.0012f;
	}

}