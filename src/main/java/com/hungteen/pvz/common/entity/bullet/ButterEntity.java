package com.hungteen.pvz.common.entity.bullet;

import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.plant.arma.KernelPultEntity;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class ButterEntity extends PultBulletEntity {

	public ButterEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}
	
	public ButterEntity(Level worldIn, LivingEntity shooter) {
		super(EntityRegister.BUTTER.get(), worldIn, shooter);
	}

	protected void dealDamage(Entity target) {
		PVZEntityDamageSource source = PVZEntityDamageSource.butter(this, this.getThrower());
		if(this.getThrower() instanceof KernelPultEntity) {
			source.addEffect(((KernelPultEntity) this.getThrower()).getButterEffect());
		}
		target.hurt(source, this.attackDamage);
	}

}