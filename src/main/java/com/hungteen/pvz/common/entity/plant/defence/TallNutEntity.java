package com.hungteen.pvz.common.entity.plant.defence;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class TallNutEntity extends WallNutEntity{

	public TallNutEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public boolean canBowling() {
		return false;
	}

	@Override
	public float getSuperLife() {
		return 800;
	}

	@Override
	public int getArmor() {
		return 15;
	}

	@Override
	public int getArmorToughness() {
		return 10;
	}

	@Override
	public float getAttractRange() {
		return 3.5F;
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TALL_NUT;
	}
	
}