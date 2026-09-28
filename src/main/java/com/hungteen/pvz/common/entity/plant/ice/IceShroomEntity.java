package com.hungteen.pvz.common.entity.plant.ice;

import com.hungteen.pvz.api.interfaces.IAlmanacEntry;
import com.hungteen.pvz.api.interfaces.IIceEffect;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.client.particle.ParticleRegister;
import com.hungteen.pvz.common.advancement.trigger.EntityEffectAmountTrigger;
import com.hungteen.pvz.common.entity.misc.ElementBallEntity;
import com.hungteen.pvz.common.entity.misc.ElementBallEntity.ElementTypes;
import com.hungteen.pvz.common.entity.plant.base.PlantBomberEntity;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.potion.EffectRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IceShroomEntity extends PlantBomberEntity implements IIceEffect{

	public IceShroomEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void startBomb(boolean server) {
		if(server) {
			//frozen enemies.
			final float len = this.getExplodeRange();
			final AABB aabb = EntityUtil.getEntityAABB(this, len, len);
			int cnt = 0;
			for(LivingEntity entity : EntityUtil.getTargetableLivings(this, aabb)) {
				 PVZEntityDamageSource source = PVZEntityDamageSource.causeIceDamage(this, this);
				 this.getFrozenEffect().ifPresent(source::addEffect);
				 entity.hurt(source, this.getExplodeDamage());
				 if(EntityUtil.isEntityFrozen(entity)) {
					 ++ cnt;
				 }
			}
			EntityUtil.playSound(this, SoundRegister.FROZEN.get());
			//trigger advancement.
			final Player player = EntityUtil.getEntityOwner(level, this);
			if(player instanceof ServerPlayer) {
				EntityEffectAmountTrigger.INSTANCE.trigger((ServerPlayer) player, this, cnt);
			}
			//kill flame ball.
			ElementBallEntity.killElementBalls(this, 40, ElementTypes.FLAME);
		} else {
			for(int i = 0;i < 3; ++ i) {
		        this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
	 	    }
		    for(int i = 0; i < 15; ++ i) {
			    this.level.addParticle(ParticleRegister.SNOW_FLOWER.get(), this.getX(), this.getY(), this.getZ(), (this.getRandom().nextFloat() - 0.5f) / 4, this.getRandom().nextFloat() / 5, (this.getRandom().nextFloat() - 0.5f) / 4);
		    }
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.COLD_LEVEL, this.getColdLvl()),
				Pair.of(PAZAlmanacs.COLD_TIME, this.getColdTick()),
				Pair.of(PAZAlmanacs.FROZEN_LEVEL, this.getFrozenLvl()),
				Pair.of(PAZAlmanacs.FROZEN_TIME, this.getFrozenTick())
		));
	}

	@Override
	public int getReadyTime() {
		return 20;
	}

	@Override
	public float getExplodeRange(){
		return 20;
	}
	
	public float getExplodeDamage() {
		return 0.1F;
	}
	
	public int getColdLvl() {
		return 1;
	}
	
	public int getColdTick() {
		return 0;
	}

	public int getFrozenLvl() {
		return 0;
	}
	
	public int getFrozenTick() {
		return 100;
	}
	
	@Override
	public Optional<MobEffectInstance> getFrozenEffect() {
		return Optional.ofNullable(new MobEffectInstance(EffectRegister.FROZEN_EFFECT.get(), this.getFrozenTick(), this.getFrozenLvl(), false, false));
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.ICE_SHROOM;
	}

}