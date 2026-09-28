package com.hungteen.pvz.common.entity.bullet.itembullet;

import com.hungteen.pvz.api.paz.IPAZEntity;
import com.hungteen.pvz.common.entity.AbstractPAZEntity;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.bullet.PultBulletEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.potion.EffectRegister;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class IceCabbageEntity extends PultBulletEntity {

	//ice pea slow tick.
	private static final int CHILL_FROZEN_TICK = 400;
	private static final int FREEZE_TICK = 60;

	public IceCabbageEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}

	public IceCabbageEntity(Level worldIn, LivingEntity shooter) {
		super(EntityRegister.ICE_CABBAGE.get(), worldIn, shooter);
	}

	@Override
	protected void dealDamage(Entity target) {
		final PVZEntityDamageSource source = PVZEntityDamageSource.iceCabbage(this, this.getThrower());
		float chance = 0F;
		if(this.getThrower() instanceof AbstractPAZEntity) {
			chance = ((AbstractPAZEntity) this.getThrower()).getSkillValue(SkillTypes.COLESLAW);
		}
		if(chance > 0 && this.level.getRandom().nextFloat() < chance) {
			source.addEffect(new MobEffectInstance(EffectRegister.FROZEN_EFFECT.get(), FREEZE_TICK, 0, false, false));
		}
		target.hurt(source, this.getAttackDamage());
		target.clearFire();
		if(target.canFreeze() && (! (target instanceof IPAZEntity) || ((IPAZEntity) target).canBeCold()) && target.getTicksFrozen() < CHILL_FROZEN_TICK) {
			target.setTicksFrozen(CHILL_FROZEN_TICK);
		}
	}

}