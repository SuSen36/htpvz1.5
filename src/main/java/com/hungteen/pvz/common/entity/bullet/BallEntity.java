package com.hungteen.pvz.common.entity.bullet;

import com.hungteen.pvz.common.misc.PVZEntityDamageSource;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BallEntity extends PultBulletEntity {

	public BallEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void dealDamage(Entity target) {
		target.hurt(PVZEntityDamageSource.ball(this, this.getOwnerOrSelf()), this.getAttackDamage());
	}

}