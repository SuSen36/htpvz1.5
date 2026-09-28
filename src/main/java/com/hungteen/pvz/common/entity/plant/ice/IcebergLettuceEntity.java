package com.hungteen.pvz.common.entity.plant.ice;

import com.hungteen.pvz.api.interfaces.IIceEffect;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.advancement.trigger.EntityEffectAmountTrigger;
import com.hungteen.pvz.common.entity.plant.base.PlantCloserEntity;
import com.hungteen.pvz.common.impl.plant.OtherPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.potion.EffectRegister;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class IcebergLettuceEntity extends PlantCloserEntity implements IIceEffect {

	private static final int FROZEN_TICK = 200;
	
	public IcebergLettuceEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void performAttack(LivingEntity target) {
		for(int i = 0; i < 2; ++ i) {
			EntityUtil.spawnParticle(this, ParticleTypes.EXPLOSION);
		}
		EntityUtil.playSound(this, SoundRegister.FROZEN.get());
		this.dealDamageTo(target);
		this.discard();
	}
	
	@Override
	public void startSuperMode(boolean first) {
		super.startSuperMode(first);
		final float range = this.getSuperRange();
		for(int i = 0; i < 2; ++ i) {
			EntityUtil.spawnParticle(this, ParticleTypes.EXPLOSION);
		}
		EntityUtil.playSound(this, SoundRegister.FROZEN.get());
		int cnt = 0;
		for(Entity target : EntityUtil.getTargetableEntities(this, EntityUtil.getEntityAABB(this, range, range))) {
			this.dealDamageTo(target);
			if(target instanceof LivingEntity && EntityUtil.isEntityFrozen((LivingEntity) target)) {
				++ cnt;
			}
		}
        Player player = EntityUtil.getEntityOwner(level, this);
		if(player instanceof ServerPlayer) {
			EntityEffectAmountTrigger.INSTANCE.trigger((ServerPlayer) player, this, cnt);
		}
	}
	
	private void dealDamageTo(Entity target) {
		PVZEntityDamageSource source = PVZEntityDamageSource.causeIceDamage(this, this);
		this.getFrozenEffect().ifPresent(source::addEffect);
		target.hurt(source, 0.001F);
	}

    @Override
	public Optional<MobEffectInstance> getFrozenEffect() {
    	return Optional.of(new MobEffectInstance(EffectRegister.FROZEN_EFFECT.get(), FROZEN_TICK, 1, false, false));
	}

    public float getSuperRange() {
		return 20;
    }
    
	@Override
	public IPlantType getPlantType() {
		return OtherPlants.ICEBERG_LETTUCE;
	}

}