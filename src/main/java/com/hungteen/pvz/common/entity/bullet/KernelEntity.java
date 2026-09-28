package com.hungteen.pvz.common.entity.bullet;

import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class KernelEntity extends PultBulletEntity {

	public KernelEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}
	
	public KernelEntity(Level worldIn, LivingEntity shooter) {
		super(EntityRegister.KERNEL.get(), worldIn, shooter);
	}

	protected void dealDamage(Entity target) {
		target.hurt(PVZEntityDamageSource.kernel(this, this.getThrower()), this.getAttackDamage() / 2F);
	}

}