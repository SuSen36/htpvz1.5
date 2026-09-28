package com.hungteen.pvz.common.entity.plant.flame;

import com.hungteen.pvz.api.interfaces.IAlmanacEntry;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.bullet.itembullet.PeaEntity;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

import java.util.List;

public class TorchWoodEntity extends PVZPlantEntity {

	private static final EntityDataAccessor<Integer> FLAME_TYPE = SynchedEntityData.defineId(TorchWoodEntity.class, EntityDataSerializers.INT);
	
	public TorchWoodEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(FLAME_TYPE, FlameTypes.YELLOW.ordinal());
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if(!level.isClientSide()) {
			this.heatPeas();
		}
	}
	
	/**
	 * {@link #normalPlantTick()}
	 */
	public void heatPeas() {
		final float range = this.getHeatRange();
		level.getEntitiesOfClass(PeaEntity.class, EntityUtil.getEntityAABB(this, range, range)).forEach(pea -> {
			pea.heatBy(this);
		});
	}
	
	@Override
	public void startSuperMode(boolean first) {
		super.startSuperMode(first);
		this.setFlameType(FlameTypes.BLUE);
	}
	
	@Override
	public boolean canStartSuperMode() {
		return super.canStartSuperMode() && this.getFlameType() == FlameTypes.YELLOW;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.HEAT_PEA_RANGE, this.getHeatRange()));
	}

	/**
	 * {@link #heatPeas()}
	 */
	public float getHeatRange() {
		return this.getSkillValue(SkillTypes.HEAT_PEA_RANGE);
	}

	@Override
	protected float getLife() {
		return this.getSkillValue(SkillTypes.WOOD_MORE_LIFE);
	}

	@Override
	public int getSuperTimeLength() {
		/* 非零时长才能被 canStartSuperMode 放行 */
		return 20;
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("flame_type")) {
			this.setFlameType(FlameTypes.values()[compound.getInt("flame_type")]);
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("flame_type", this.getFlameType().ordinal());
	}
	
	public FlameTypes getFlameType() {
		return FlameTypes.values()[entityData.get(FLAME_TYPE)];
	}
	
	public void setFlameType(FlameTypes type) {
		entityData.set(FLAME_TYPE, type.ordinal());
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TORCH_WOOD;
	}
	
	public enum FlameTypes{
		YELLOW,
		BLUE,
		PURPLE
	}

}