package com.hungteen.pvz.common.entity.plant.arma;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.bullet.PultBulletEntity;
import com.hungteen.pvz.common.entity.bullet.itembullet.CabbageEntity;
import com.hungteen.pvz.common.entity.bullet.itembullet.IceCabbageEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantPultEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class CabbagePultEntity extends PlantPultEntity {

	private static final EntityDataAccessor<Boolean> ICE_CABBAGE = SynchedEntityData.defineId(CabbagePultEntity.class, EntityDataSerializers.BOOLEAN);

	public CabbagePultEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ICE_CABBAGE, false);
	}

	@Override
	public void startSuperMode(boolean first) {
		super.startSuperMode(first);
		if(SkillTypes.getSkillLevel(this.getSkills(), SkillTypes.COLESLAW) > 0) {
			this.setIceCabbage(true);
		}
	}
	
	@Override
	protected PultBulletEntity createBullet() {
		if(this.isIceCabbage()) {
			return new IceCabbageEntity(level, this);
		}
		return new CabbageEntity(level, this);
	}

	public boolean isIceCabbage() {
		return this.entityData.get(ICE_CABBAGE);
	}

	public void setIceCabbage(boolean ice) {
		this.entityData.set(ICE_CABBAGE, ice);
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_CABBAGE_DAMAGE);
	}

	@Override
	public float getSuperDamage() {
		return this.getAttackDamage() + 20;
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.CABBAGE_PULT;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("ice_cabbage")) {
			this.setIceCabbage(compound.getBoolean("ice_cabbage"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("ice_cabbage", this.isIceCabbage());
	}

}