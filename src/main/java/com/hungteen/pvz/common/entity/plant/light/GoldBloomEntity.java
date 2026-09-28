package com.hungteen.pvz.common.entity.plant.light;

import com.hungteen.pvz.api.interfaces.IAlmanacEntry;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.misc.drop.SunEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantBomberEntity;
import com.hungteen.pvz.common.impl.plant.OtherPlants;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

import java.util.List;

public class GoldBloomEntity extends PlantBomberEntity {

	public GoldBloomEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.hasBombAlamancs = false;
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if(! this.level.isClientSide()) {
			final int time = this.getAttackTime();
			if(time == 16) {
				EntityUtil.playSound(this, SoundRegister.GOLD_BLOOM_PRODUCE.get());
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(50), 0.3F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(25), 0.3F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(5), 0.4F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(5), 0.4F);
			} else if(time == 29) {
				EntityUtil.playSound(this, SoundRegister.GOLD_BLOOM_PRODUCE.get());
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(50), 0.3F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(25), 0.3F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(5), 0.4F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(5), 0.4F);
			} else if(time == 40) {
				EntityUtil.playSound(this, SoundRegister.GOLD_BLOOM_PRODUCE.get());
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(50), 0.3F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(25), 0.3F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(25), 0.4F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(15), 0.4F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(15), 0.4F);
				SunEntity.dropSunRandomly(level, this.blockPosition().above(), getSunPieceAmount(10), 0.4F);
			}
		}
	}

	/**
	 * night yields 2/3 of the day amount per sun piece, rounded to a multiple of 5
	 * because dropped sun entities only carry values on that grid.
	 */
	private int getSunPieceAmount(int dayAmount) {
		if(this.level.isDay()) {
			return dayAmount;
		}
		return (int) Math.round(dayAmount * 2.0 / 3.0 / 5.0) * 5;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.GEN_SUN_AMOUNT, 310));
	}

	@Override
	protected void startBomb(boolean server) {
	}

	@Override
	public int getReadyTime() {
		return 80;
	}

	@Override
	public IPlantType getPlantType() {
		return OtherPlants.GOLD_BLOOM;
	}

}
