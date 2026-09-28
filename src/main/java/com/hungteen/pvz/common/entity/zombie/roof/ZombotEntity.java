package com.hungteen.pvz.common.entity.zombie.roof;

import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.misc.drop.JewelEntity;
import com.hungteen.pvz.common.entity.zombie.base.AbstractEdgarZombotEntity;
import com.hungteen.pvz.common.impl.zombie.RoofZombies;
import com.hungteen.pvz.common.impl.zombie.ZombieType;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.MathUtil;
import com.hungteen.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ZombotEntity extends AbstractEdgarZombotEntity {

    private static final EntityDataAccessor<BlockPos> ORIGIN_POS = SynchedEntityData.defineId(ZombotEntity.class, EntityDataSerializers.BLOCK_POS);

    public ZombotEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        this.refreshCountCD = 10;
        this.maxZombieSurround = 40;
        this.maxPlantSurround = 50;
        this.kickRange = 6;
        this.setIsWholeBody();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ORIGIN_POS, BlockPos.ZERO);
    }

    @Override
    public void kill() {
        super.kill();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void zombieTick() {
        super.zombieTick();
        if (!level.isClientSide()) {
            if (this.getOriginPos() == BlockPos.ZERO) {
                this.setOriginPos(this.blockPosition());
            } else {
                if (MathUtil.getPosDisToVec(getOriginPos(), position()) >= 10) {
                    final int range = 4;
                    for (int i = -range; i <= range; ++i) {
                        for (int j = -range; j <= range; ++j) {
                            final BlockPos tmp = getOriginPos().offset(i, -1, j);
                            if (level.getBlockState(tmp).isAir()) {
                                level.setBlockAndUpdate(tmp, Blocks.GRASS_BLOCK.defaultBlockState());
                            }
                            for (int k = 0; k <= 10; ++k) {
                                level.setBlockAndUpdate(getOriginPos().offset(i, k, j), Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                    this.setPos(getOriginPos().getX(), getOriginPos().getY() + 1, getOriginPos().getZ());
                }
            }
        }
    }

    @Override
    public int getBossStage() {
        final float percent = this.bossInfo.getProgress();
        return percent > 3F / 4 ? 1 :
                percent > 2F / 4 ? 2 :
                        percent > 1F / 4 ? 3 : 4;
    }

    @Override
    protected void spawnSpecialDrops() {
        final int playerCnt = this.bossInfo.getPlayers().size();
        for (int i = 0; i < 2 + 2 * playerCnt; ++i) {
            JewelEntity jewel = EntityRegister.JEWEL.get().create(level);
            EntityUtil.onEntityRandomPosSpawn(level, jewel, blockPosition().above(5), 4);
        }
    }

    public EntityDimensions getDimensions(Pose poseIn) {
        return EntityDimensions.scalable(2F, 7.5F);
    }

    @Override
    public float getWalkSpeed() {
        return 0;
    }

    @Override
    public float getEatDamage() {
        return ZombieUtil.NORMAL_DAMAGE;
    }

    @Override
    public float getLife() {
        return 1000;
    }

    @Override
    public float getInnerLife() {
        return 3000;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("origin_pos")) {
            CompoundTag nbt = compound.getCompound("origin_pos");
            this.setOriginPos(new BlockPos(nbt.getInt("origin_pos_x"), nbt.getInt("origin_pos_y"), nbt.getInt("origin_pos_z")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("origin_pos_x", this.getOriginPos().getX());
        nbt.putInt("origin_pos_y", this.getOriginPos().getY());
        nbt.putInt("origin_pos_z", this.getOriginPos().getZ());
        compound.put("origin_pos", nbt);
    }

    public BlockPos getOriginPos() {
        return this.entityData.get(ORIGIN_POS);
    }

    public void setOriginPos(BlockPos pos) {
        this.entityData.set(ORIGIN_POS, pos);
    }

    @Override
    public ZombieType getZombieType() {
        return RoofZombies.ZOMBOT;
    }

}