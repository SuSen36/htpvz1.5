package com.hungteen.pvz.common.entity.plant.assist;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.impl.plant.OtherPlants;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

/**
 * Container that holds any plant riding on it.
 */
public class FlowerPotEntity extends PVZPlantEntity {

	public FlowerPotEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.isImmuneToWeak = true;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.FLOWER_POT;
	}

	@Override
	public boolean canHoldPlant() {
		return true;
	}

	@Override
	public boolean canPlantOnMe(IPlantType type) {
		return super.canPlantOnMe(type) && !type.isWaterPlant() && type != PVZPlants.SPIKE_WEED
				&& type != PVZPlants.SPIKE_ROCK && type != OtherPlants.GOLD_LEAF;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public double getPassengersRidingOffset() {
		return 0.4D;
	}

	@Override
	public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
		return true;
	}

	@Override
	public boolean rideableUnderWater() {
		return true;
	}
}