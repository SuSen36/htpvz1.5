package com.hungteen.pvz.common.entity.plant.flame;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.misc.ElementBallEntity;
import com.hungteen.pvz.common.entity.misc.ElementBallEntity.ElementTypes;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantBomberEntity;
import com.hungteen.pvz.common.entity.zombie.zombotany.JalapenoZombieEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;

public class JalapenoEntity extends PlantBomberEntity{

	public JalapenoEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void startBomb(boolean server) {
		final float range = this.getExplodeRange();
		if(server) {
			//deal damage.
			fireTarget(this, range, 1F);
		    fireTarget(this, 1F, range);
			EntityUtil.playSound(this, SoundRegister.JALAPENO.get());
		}
		clearSnowAndSpawnFlame(this, (int) range);
	}
	
	/**
	 * jalapeno fire mobs.
	 * {@link #startBomb(boolean)}
	 */
	public static void fireTarget(LivingEntity entity, float dx, float dz) {
		final AABB aabb = new AABB(entity.position().add(dx, 1, dz), entity.position().add(- dx, - 1, - dz));
		for(Entity target : EntityUtil.getWholeTargetableEntities(entity, aabb)) {
			if(target instanceof ElementBallEntity elementBall && elementBall.getElementBallType() == ElementTypes.ICE) {
				target.discard();
				continue;
			}
			float damage = 0;
			if(entity instanceof JalapenoEntity jalapeno) {
				damage = jalapeno.getExplodeDamage();
			} else if(entity instanceof JalapenoZombieEntity) {
				if(target instanceof LivingEntity living) {
					damage = EntityUtil.getMaxHealthDamage(living, 2);
				} else {
					damage = 100F;
				}
			}
			target.hurt(PVZEntityDamageSource.causeFlameDamage(entity, entity).setExplosion(), damage);
		}
		PVZPlantEntity.clearLadders(entity, aabb);
	}
	
	/**
	 * spawn flame particle and clear snow.
	 * {@link #startBomb(boolean)}
	 */
	public static void clearSnowAndSpawnFlame(LivingEntity entity, int range) {
		final boolean flag = ForgeEventFactory.getMobGriefingEvent(entity.level, entity);
		for(int i = - range; i <= range; ++ i) {
			spawnFlame(entity, i, 0);
			spawnFlame(entity, 0, i);
			if(flag) {
				for(int j = - 1; j <= 1; ++ j) {
					clearSnow(entity, i, j);
					clearSnow(entity, j, i);
				}
			}
		}
	}
	
	/**
	 * spawn flame particle.
	 */
	private static void spawnFlame(LivingEntity entity, int dx, int dz) {
		if(entity.level.isClientSide()) {
			for(int i = 0; i < 20; ++ i) {
				WorldUtil.spawnRandomSpeedParticle(entity.level, ParticleTypes.FLAME, entity.position().add(dx, 0, dz), 0.1F);
			}
		}
	}
	
	/**
	 * clear snow around.
	 */
	private static void clearSnow(LivingEntity entity, int dx, int dz) {
		if(! entity.level.isClientSide()) {
		    final BlockPos pos = entity.blockPosition().offset(dx, 0, dz);
		    if(entity.level.getBlockState(pos).getBlock() == Blocks.SNOW || entity.level.getBlockState(pos).getBlock() == Blocks.SNOW_BLOCK) {
			    entity.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		    }
		}
	}

	@Override
	public float getExplodeRange() {
		return 20;
	}

	@Override
	public float getExplodeDamage() {
		return this.getSkillValue(SkillTypes.NORMAL_BOMB_DAMAGE);
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.7f, 1.5f);
	}

	@Override
	public int getReadyTime() {
		return 20;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.JALAPENO;
	}

}