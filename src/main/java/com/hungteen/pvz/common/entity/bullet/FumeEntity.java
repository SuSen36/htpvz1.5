package com.hungteen.pvz.common.entity.bullet;

import com.hungteen.pvz.client.particle.ParticleRegister;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.bullet.itembullet.PVZItemBulletEntity;
import com.hungteen.pvz.common.entity.plant.toxic.GloomShroomEntity;
import com.hungteen.pvz.common.item.ItemRegister;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.utils.WorldUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FumeEntity extends PVZItemBulletEntity{

	private int knockback = 0;
	
	public FumeEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}
	
	public FumeEntity(Level worldIn, LivingEntity living) {
		super(EntityRegister.FUME.get(), worldIn, living);
	}
	
	@Override
	public void tick() {
		super.tick();
		if(level.isClientSide()) {
			final int cnt = Math.max(2, Math.min(5, this.getMaxLiveTick() / this.tickCount));
			for(int i = 0; i < cnt; ++ i) {
				WorldUtil.spawnRandomSpeedParticle(level, ParticleRegister.FUME.get(), this.position(), 0.05F);
	        }
		}
	}
	
	@Override
	protected int getMaxLiveTick() {
		if(this.getThrower() instanceof GloomShroomEntity) {
			return 3;
		}
		return 10;
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
				this.dealFumeDamage(target); // attack 
				if(this.hitEntities == null) {
					this.hitEntities = new IntOpenHashSet();
				}
				this.addHitEntity(target);
			}
		}
		this.level.broadcastEntityEvent(this, (byte) 3);
		if (flag || !this.checkLive(result)) {
this.discard();
		}
	}
	
	@Override
	protected boolean checkLive(HitResult result) {
		if(result.getType() == HitResult.Type.BLOCK) {
    		Block block = level.getBlockState(((BlockHitResult)result).getBlockPos()).getBlock();
            return block instanceof BushBlock;
        }
    	return true;
	}
	
	private void dealFumeDamage(Entity target) {
		target.hurt(PVZEntityDamageSource.fume(this, this.getThrower()), this.attackDamage);
		if(!level.isClientSide() && this.knockback > 0) {
			Vec3 speed = target.getDeltaMovement();
			Vec3 now = this.getDeltaMovement();
			int lvl = this.knockback;
			target.setDeltaMovement(speed.add(now).multiply(lvl, lvl, lvl));
		}
	}
	
	public void setKnockback(int lvl) {
		this.knockback = lvl;
	}
	
	public int getKnockback() {
		return this.knockback;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.002f;
	}

}