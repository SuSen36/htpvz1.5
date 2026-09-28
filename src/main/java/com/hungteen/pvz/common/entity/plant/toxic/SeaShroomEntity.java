package com.hungteen.pvz.common.entity.plant.toxic;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;

public class SeaShroomEntity extends PuffShroomEntity {

	public SeaShroomEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new FloatGoal(this));
	}
	
	@Override
	public double getMaxShootAngle() {
		return 5;
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.SEA_SHROOM;
	}

}