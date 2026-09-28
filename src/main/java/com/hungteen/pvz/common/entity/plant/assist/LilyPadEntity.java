package com.hungteen.pvz.common.entity.plant.assist;

import com.hungteen.pvz.api.types.ICardPlacement;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.impl.Placements;
import com.hungteen.pvz.common.impl.plant.OtherPlants;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;

import java.util.List;

/**
 * Leaf that floats on water and carries other plants riding on it.
 */
public class LilyPadEntity extends PVZPlantEntity {

	public LilyPadEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.root = false;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new FloatGoal(this));
	}

	/* eye height is below 0.4, so the vanilla jump threshold is 0: FloatGoal hops
	 * while the feet are submerged, and once they reach the surface they are
	 * hard-pinned after vanilla movement, on both sides, keeping the final position
	 * constant with no vertical move packets or re-entry splashes */
	@Override
	public void tick() {
		super.tick();
		if(this.getVehicle() == null) {
			final BlockPos fluidPos = this.blockPosition();
			final FluidState fluidState = this.level.getFluidState(fluidPos);
			if(fluidState.is(FluidTags.WATER)
					&& this.getFluidHeight(FluidTags.WATER) <= this.getFluidJumpThreshold()) {
				final double surfaceY = fluidPos.getY() + fluidState.getHeight(this.level, fluidPos);
				if(this.getY() != surfaceY) {
					this.setPos(this.getX(), surfaceY, this.getZ());
				}
				if(this.getDeltaMovement().y != 0.0D) {
					this.setDeltaMovement(this.getDeltaMovement().x, 0.0D, this.getDeltaMovement().z);
				}
			}
		}
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.LILY_PAD;
	}

	@Override
	public MobType getMobType() {
		return MobType.WATER;
	}

	@Override
	public boolean canHoldPlant() {
		return true;
	}

	@Override
	public ICardPlacement getCarryPlacement() {
		return Placements.COMMON;
	}

	@Override
	public boolean canPlantOnMe(IPlantType type) {
		return super.canPlantOnMe(type) && !type.isWaterPlant() && type != PVZPlants.SPIKE_WEED
				&& type != PVZPlants.SPIKE_ROCK && type != PVZPlants.POTATO_MINE
				&& type != OtherPlants.GOLD_LEAF;
	}

	@Override
	protected float getWaterSlowDown() {
		return 1F;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return true;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public double getPassengersRidingOffset() {
		return 0.2D;
	}

	@Override
	public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
		return true;
	}

	@Override
	public boolean rideableUnderWater() {
		return true;
	}

	@Override
	public void remove(RemovalReason reason) {
		List.copyOf(this.getPassengers()).forEach(Entity::discard);
		super.remove(reason);
	}
}