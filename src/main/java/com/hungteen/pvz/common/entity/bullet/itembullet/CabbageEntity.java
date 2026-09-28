package com.hungteen.pvz.common.entity.bullet.itembullet;

import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.bullet.PultBulletEntity;
import com.hungteen.pvz.common.item.ItemRegister;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CabbageEntity extends PultBulletEntity implements ItemSupplier {

	public CabbageEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}
	
	public CabbageEntity(Level worldIn, LivingEntity shooter) {
		super(EntityRegister.CABBAGE.get(), worldIn, shooter);
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(ItemRegister.CABBAGE.get());
	}

	protected void dealDamage(Entity target) {
		target.hurt(PVZEntityDamageSource.cabbage(this, this.getThrower()), this.getAttackDamage());
	}

}