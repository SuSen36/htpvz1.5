package com.hungteen.pvz.common.entity.plant.explosion;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.client.particle.ParticleRegister;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.misc.DoomFixerEntity;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.plant.assist.FlowerPotEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantBomberEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class DoomShroomEntity extends PlantBomberEntity {

	public static final float MAX_EXPLOSION_LEVEL = 500;
	
	public DoomShroomEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if(! this.level.isClientSide()) {
			if(this.getAttackTime() == this.getReadyTime() - 2) {
				DoomFixerEntity fixer = EntityRegister.DOOM_FIXER.get().create(level);
                if (fixer != null) {
                    EntityUtil.onEntitySpawn(level, fixer, this.blockPosition());
                }
            }
		}
	}
	
	@Override
	public void startBomb(boolean server) {
		if(server) {
			//deal damage to targets.
			final float range = this.getExplodeRange();
			final AABB aabb = EntityUtil.getEntityAABB(this, range, range);
			EntityUtil.getWholeTargetableEntities(this, aabb).forEach(target -> {
				if(target instanceof EnderDragon) {//make ender_dragon can be damaged by doom shroom.
					target.hurt(((EntityDamageSource)DamageSource.mobAttack(this)).setThorns().setExplosion(), this.getExplodeDamage() * 2);
				} else {
					target.hurt(PVZEntityDamageSource.explode(this), this.getExplodeDamage());
				}
			});
			PVZPlantEntity.clearLadders(this, aabb);
			//the carrying flower pot is the rider vehicle, which is excluded from targetable entities.
			if(this.getVehicle() instanceof FlowerPotEntity flowerPot) {
				flowerPot.hurt(PVZEntityDamageSource.explode(this), this.getExplodeDamage());
			}
			EntityUtil.playSound(this, SoundRegister.DOOM_SHROOM.get());
			//destroy block and spawn drops
			if(net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
				this.destroyBlocks();
			}
		} else {
			for(int i = 0; i < 300; ++ i) {
				WorldUtil.spawnRandomSpeedParticle(level, ParticleRegister.SPORE.get(), this.position().add(0, 1, 0), 0.8F);
			}
		}
	}

	protected void destroyBlocks() {
		final List<BlockPos> destroyedList = new ArrayList<>();

		final int len = 1;
		for (int i = -len; i <= len; ++i) {
			for (int j = -len; j <= len; ++j) {
				for (int k = -2; k < 0; ++k) {
					final BlockPos pos = this.blockPosition().offset(i, k, j);
					final BlockState state = level.getBlockState(pos);
					if (! state.isAir() && state.getBlock().getExplosionResistance() <= MAX_EXPLOSION_LEVEL) {
						destroyedList.add(pos.immutable());
					}
				}
			}
		}

		final int range = 6;
		for (int h = 0; h <= range + 8; ++h) {
			for (int i = -range; i <= range; ++i) {
				for (int j = -range; j <= range; ++j) {
					if (new Vec3(i, h - 5, j).lengthSqr() <= range * range) {
						final BlockPos pos = this.blockPosition().offset(i, h, j);
						final BlockState state = level.getBlockState(pos);
						if (! state.isAir() && state.getBlock().getExplosionResistance() <= MAX_EXPLOSION_LEVEL) {
							destroyedList.add(pos.immutable());
						}
					}
				}
			}
		}

		//feed custom shape into vanilla explosion so its finalizeExplosion handles loot drops exactly like vanilla.
		final Explosion explosion = new Explosion(this.level, this, this.getX(), this.getY(), this.getZ(), this.getExplodeRange(), false, Explosion.BlockInteraction.DESTROY, destroyedList);
		explosion.finalizeExplosion(false);
	}

	@Override
	public float getExplodeDamage() {
		return this.getSkillValue(SkillTypes.HIGH_EXPLODE_DAMAGE);
	}

	@Override
	public float getExplodeRange(){
		return 10.5F;
	}
	
	@Override
	public int getReadyTime() {
		return 50;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.DOOM_SHROOM;
	}

}