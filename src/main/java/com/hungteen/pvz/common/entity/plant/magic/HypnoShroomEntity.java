package com.hungteen.pvz.common.entity.plant.magic;

import com.hungteen.pvz.api.interfaces.ICanBeAttracted;
import com.hungteen.pvz.api.interfaces.ICanBeCharmed;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.ai.goal.misc.PlantAttractGoal;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import com.hungteen.pvz.common.entity.zombie.roof.GargantuarEntity;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.interfaces.ICanAttract;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class HypnoShroomEntity extends PVZPlantEntity implements ICanAttract {

    public HypnoShroomEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PlantAttractGoal(this, this, 25));
    }

    @Override
    public boolean canAttract(LivingEntity target) {
        if (target instanceof ICanBeAttracted && !((ICanBeAttracted) target).canBeAttractedBy(this)) {
            return false;
        }
        if (!this.getSensing().hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Mob mob) {
            return !(mob.getTarget() instanceof HypnoShroomEntity);
        }
        return false;
    }

    @Override
    public void attract(LivingEntity target) {
        if (target instanceof Mob) {
            ((Mob) target).setTarget(this);
        }
    }

    @Override
    public float getAttractRange() {
        return 2.5F;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level.isClientSide() && this.canNormalUpdate()) {
            if (source instanceof PVZEntityDamageSource && ((PVZEntityDamageSource) source).isEatDamage()) {
                if (this.isPlantInSuperMode()) {
                    source.getEntity();
                    source.getEntity().remove(RemovalReason.DISCARDED);
                    GargantuarEntity gar = EntityRegister.GARGANTUAR.get().create(level);
                    EntityUtil.onEntitySpawn(level, gar, source.getEntity().blockPosition());
                    gar.setZombieType(PVZZombieEntity.VariantType.NORMAL);
                    gar.setHealth(gar.getMaxHealth() * this.getSummonHealth());
                    gar.setCharmed(!this.isCharmed());
                } else {
                    if (source.getEntity() instanceof ICanBeCharmed) {
						((ICanBeCharmed) source.getEntity()).onCharmedBy(this);
                    }
                }
				EntityUtil.playSound(this, SoundRegister.HYPNO.get());
            }
        }
    }


    @Override
    public float getLife() {
        return 20;
    }

    /**
     * the current health of gargangtuar when summoning.
     */
    public float getSummonHealth() {
        return 0.7F;
    }

    @Override
    public boolean isPlantImmuneTo(DamageSource source) {
        return false;
    }

    @Override
    public int getSuperTimeLength() {
        return 2400;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.HYPNO_SHROOM;
    }

}