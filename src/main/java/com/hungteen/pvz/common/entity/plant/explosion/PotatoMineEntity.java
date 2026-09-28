package com.hungteen.pvz.common.entity.plant.explosion;

import com.hungteen.pvz.api.interfaces.IAlmanacEntry;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.client.model.entity.plant.explosion.PotatoMineModel;
import com.hungteen.pvz.client.particle.ParticleRegister;
import com.hungteen.pvz.common.entity.bullet.itembullet.PotatoEntity;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantCloserEntity;
import com.hungteen.pvz.common.entity.zombie.pool.DiggerZombieEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.potion.EffectRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.MathUtil;
import com.hungteen.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PotatoMineEntity extends PlantCloserEntity{

	public static final int RISING_ANIM_CD = 20;

	private static final EntityDataAccessor<Boolean> POISONOUS = SynchedEntityData.defineId(PotatoMineEntity.class, EntityDataSerializers.BOOLEAN);
	
	public PotatoMineEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(POISONOUS, false);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if(! this.level.isClientSide()) {
			if(this.getExistTick() == this.getPrepareCD() - RISING_ANIM_CD + 1) {
				EntityUtil.playSound(this, SoundRegister.DIRT_RISE.get());
			}
		} else {
			if(this.isRisingFromDirt()) {
				final BlockPos groundPos = this.getOnPos();
				for(int i = 0; i < 3; ++ i) {
					this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.level.getBlockState(groundPos)).setPos(groundPos),
							this.getX() + (this.getRandom().nextDouble() - 0.5D),
							this.getY() + 0.1D,
							this.getZ() + (this.getRandom().nextDouble() - 0.5D),
							(this.getRandom().nextDouble() - 0.5D) * 6.0D,
							2D,
							(this.getRandom().nextDouble() - 0.5D) * 4.0D);
				}
			}
			if(this.isPoisonous() && this.getRandom().nextBoolean()) {
				final int color = EffectRegister.PHYTOTOXIN_EFFECT.get().getColor();
				final double r = (double) (color >> 16 & 255) / 255.0D;
				final double g = (double) (color >> 8 & 255) / 255.0D;
				final double b = (double) (color & 255) / 255.0D;
				this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.getRandom().nextFloat() * 0.6F - 0.3F,
						this.getY() + this.getRandom().nextFloat() - 0.3F, this.getZ() + this.getRandom().nextFloat() * 0.6F - 0.3F, r, g, b);
			}
		}
	}

	@Override
	public void pazTick() {
		super.pazTick();
		if(! this.level.isClientSide()) {
			if(this.hasEffect(MobEffects.POISON) || this.hasEffect(EffectRegister.PHYTOTOXIN_EFFECT.get())) {
				this.setPoisonous(true);
				this.removeEffect(MobEffects.POISON);
				this.removeEffect(EffectRegister.PHYTOTOXIN_EFFECT.get());
			}
		}
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.EXPLODE_DAMAGE, this.getExplodeDamage()),
				Pair.of(PAZAlmanacs.EXPLODE_DAMAGE, this.getExplodeRange()),
				Pair.of(PAZAlmanacs.PREPARE_CD, this.getPrepareCD())
		));
	}

	@Override
	public void performAttack(LivingEntity target1) {
		if(! this.level.isClientSide()) {
			final float range = 1.6F;
			final AABB aabb = EntityUtil.getEntityAABB(this, range, range);
			EntityUtil.getWholeTargetableEntities(this, aabb).forEach(target -> {
				target.hurt(PVZEntityDamageSource.explode(this), this.getExplodeDamage());
			});
			PVZPlantEntity.clearLadders(this, aabb);
			EntityUtil.playSound(this, SoundRegister.POTATO_MINE.get());
			((ServerLevel) this.level).sendParticles(ParticleRegister.MASHED_POTATO.get(),
					this.getX() + this.getRandom().nextFloat() * 0.5 - 0.25,
					this.getY() + this.getRandom().nextFloat() * 0.5 + 0.25,
					this.getZ() + this.getRandom().nextFloat() * 0.5 - 0.25,
					20, 0.5, 0.5, 0.5, 0.1);
			if(this.isPoisonous()) {
				this.spawnPoisonCloud();
			}
			this.discard();
		}
	}
	
	private void spawnPoisonCloud() {
		AreaEffectCloud cloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
		cloud.setRadius(2F);
		cloud.setDuration(400);
		cloud.setWaitTime(0);
		cloud.setOwner(this);
		cloud.addEffect(new MobEffectInstance(EffectRegister.PHYTOTOXIN_EFFECT.get(), 400));
		this.level.addFreshEntity(cloud);
	}
	
	@Override
	public boolean canPAZTarget(Entity target) {
		if(target instanceof DiggerZombieEntity) {
			return true;
		}
		return super.canPAZTarget(target);
	}
	
	@Override
	public boolean canStartSuperMode() {
		return super.canStartSuperMode() && this.getAttackTime() <= 0;
	}
	
	@Override
	public void startSuperMode(boolean first) {
		super.startSuperMode(first);
		shootPotatos();
		if(! this.isRisingFromDirt() && ! this.isMineReady()) {
			this.setRisingFromDirt();
		}
	}

	@Override
	public void finalizeSpawn(CompoundTag tag) {
		super.finalizeSpawn(tag);
		if(! this.level.isClientSide()) {
			/* 技能只决定出生时是否毒化，此后毒化状态独立于技能表存在 */
			final boolean bySkill = SkillTypes.getSkillLevel(this.getSkills(), SkillTypes.POISON_ENRICHMENT) > 0;
			final boolean byRandom = this.getRandom().nextFloat() <= 0.02F;
			if(bySkill || byRandom) {
				this.setPoisonous(true);
			}
		}
	}

	/**
	 * shoot some potato to the sky
	 */
	protected void shootPotatos() {
		int num = this.getShootNum();
		for(int i = 0; i < num; ++ i) {
			/* 只朝附近可种植土豆雷的地面投掷，避免土豆落在不可种植方块上白炸 */
			final Optional<BlockPos> targetPos = this.getRandomPlantableGround();
			if(targetPos.isEmpty()) {
				continue;
			}
			PotatoEntity potato = new PotatoEntity(level, this);
			potato.setPoisonous(this.isPoisonous());
			potato.setPos(this.getX(), this.getY() + 1, this.getZ());
			potato.shootTo(targetPos.get());
		    potato.summonByOwner(this);
		    this.level.addFreshEntity(potato);
		}
	}

	private Optional<BlockPos> getRandomPlantableGround() {
		for(int i = 0; i < 8; ++ i) {
			final BlockPos offset = MathUtil.getRandomRangePos(this.getRandom(), 2, 16);
			final BlockPos groundPos = this.blockPosition().offset(offset.getX(), -1, offset.getZ());
			if(this.level.isEmptyBlock(groundPos.above())
					&& this.level.getEntitiesOfClass(PVZPlantEntity.class, new AABB(groundPos.above())).isEmpty()
					&& PVZPlants.POTATO_MINE.getPlacement().canPlaceOnBlock(this.level.getBlockState(groundPos).getBlock())) {
				return Optional.of(groundPos);
			}
		}
		return Optional.empty();
	}
	
	public int getShootNum() {
		return 3;
	}

	public float getExplodeRange(){
		return 1.8F;
	}

	public float getExplodeDamage(){
		final float damage = this.getSkillValue(SkillTypes.NORMAL_BOMB_DAMAGE);
		return this.isPoisonous() ? damage * 0.75F : damage;
	}

	public int getPrepareCD(){
		return (int) this.getSkillValue(SkillTypes.MINE_FAST_PREPARE);
	}

	public boolean isPoisonous() {
		return this.entityData.get(POISONOUS);
	}

	public void setPoisonous(boolean poisonous) {
		this.entityData.set(POISONOUS, poisonous);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("poisonous")) {
			this.setPoisonous(compound.getBoolean("poisonous"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("poisonous", this.isPoisonous());
	}

	/**
	 */
	public boolean isMineReady() {
		return this.getExistTick() > this.getPrepareCD();
	}
	
	/**
	 */
	public boolean isRisingFromDirt() {
		return this.getExistTick() >= this.getPrepareCD() - RISING_ANIM_CD && this.getExistTick() <= this.getPrepareCD();
	}
	
	/**
	 *
     */
	public void setRisingFromDirt() {
		this.setExistTick(this.getPrepareCD() - RISING_ANIM_CD - 2);
	}
	
	@Override
	protected boolean canBeImmuneToEnforce(Entity entity) {
		return super.canBeImmuneToEnforce(entity) && (this.isMineReady() || this.isRisingFromDirt());
	}
	
	@Override
	public boolean canCheckDistance() {
		return this.isMineReady();
	}
	
	/**
	 * use to change Signal String's color.
	 * {@link PotatoMineModel#setupAnim(PotatoMineEntity, float, float, float, float, float)}
	 */
	public int getSignChangeCD(){
		return this.isMineReady() ? 10 : 20;
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.POTATO_MINE;
	}

}