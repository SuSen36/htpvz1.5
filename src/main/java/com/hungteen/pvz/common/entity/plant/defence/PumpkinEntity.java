package com.hungteen.pvz.common.entity.plant.defence;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.plant.base.PlantDefenderEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class PumpkinEntity extends PlantDefenderEntity{

	public PumpkinEntity(EntityType<? extends PathfinderMob> p_i48575_1_, Level p_i48575_2_) {
		super(p_i48575_1_, p_i48575_2_);
		this.canCollideWithPlant = false;
		this.isImmuneToWeak = true;
	}

	@Override
	public boolean canAttract(LivingEntity entity) {
		return this.getPassengers().isEmpty();
	}

	@Override
	public float getLife() {
		return this.getSkillValue(SkillTypes.PUMPKIN_MORE_LIFE);
	}

	@Override
	public float getSuperLife() {
		return 800;
	}

	@Override
	public boolean canHoldPlant() {
		return true;
	}

	@Override
	public boolean canPlantOnMe(IPlantType type) {
		return super.canPlantOnMe(type) && !type.isWaterPlant();
	}

	@Override
	public double getPassengersRidingOffset() {
		return 0.025D;
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.PUMPKIN;
	}
}