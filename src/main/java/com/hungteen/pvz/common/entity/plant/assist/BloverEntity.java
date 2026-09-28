package com.hungteen.pvz.common.entity.plant.assist;

import com.hungteen.pvz.api.interfaces.IAlmanacEntry;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.capability.level.PVZFogCapability;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.zombie.pool.BalloonZombieEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.network.PVZFogPacket;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BloverEntity extends PVZPlantEntity {

	public BloverEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}
	
	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if(! level.isClientSide()) {
			if(this.getExistTick() == 10) {
			    this.blow();
			} else if(this.getExistTick() > 60) {
				this.discard();
			}
		}
	}
	
	public void blow() {
		if(! this.level.isClientSide()) {
			final float len = this.getBlowRange();
			//按三叶草面朝方向统一吹飞
			final float yaw = this.getYRot() * ((float) Math.PI / 180F);
			final Vec3 faceVec = new Vec3(- Mth.sin(yaw), 0, Mth.cos(yaw));
			//deal damage.
			EntityUtil.getWholeTargetableEntities(this, EntityUtil.getEntityAABB(this, len, len)).forEach(target -> {
				if(EntityUtil.isEntityInSky(target)) {
					target.hurt(PVZEntityDamageSource.normal(this).setMustHurt(), this.getAttackDamage());
					final Vec3 speed = target.getDeltaMovement();
					final double lvl = this.getForceLevel() * 2.5F;
					final Vec3 delta = faceVec.scale(lvl);
					target.setDeltaMovement(speed.x + delta.x, speed.y, speed.z + delta.z);
				}
			});
			//全屏驱雾，被驱散的雾按原作24秒后重新蔓延
			this.level.getCapability(PVZFogCapability.CAP).ifPresent(cap -> {
				for(UUID uuid : cap.fogs.keySet()) {
					PVZFogCapability.modifyFogFeatures(this.level, uuid, PVZFogPacket.ModifyType.REMOVE, 0);
				}
			});
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.ATTACK_RANGE, this.getBlowRange())
		));
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		if(entity instanceof BalloonZombieEntity) {
			return true;
		}
		return super.canPAZTarget(entity);
	}
	
	@Override
	public Optional<SoundEvent> getSpawnSound() {
		return Optional.ofNullable(SoundRegister.BLOVER.get());
	}
	
	public float getAttackDamage(){
		return this.getSkillValue(SkillTypes.BLOW_STRENGTH);
	}
	
	public int getForceLevel() {
		return 2;
	}

	public float getBlowRange(){
		return 30;
	}

	public int getReadyTime() {
		return 40;
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.BLOVER;
	}

}