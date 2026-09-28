package com.hungteen.pvz.common.entity.plant.light;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class TwinSunFlowerEntity extends SunFlowerEntity{

	public TwinSunFlowerEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void genSomething() {
		this.genSun(this.getSunAmount(), 2);
	}

	@Override
	public int getSunAmount() {
		return 50;
	}

	@Override
	public int getSuperSunAmount() {
		return 750;
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TWIN_SUNFLOWER;
	}
	
}