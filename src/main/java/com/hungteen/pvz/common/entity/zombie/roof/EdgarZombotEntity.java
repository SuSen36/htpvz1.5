package com.hungteen.pvz.common.entity.zombie.roof;

import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.misc.DestroyCarEntity;
import com.hungteen.pvz.common.entity.misc.ElementBallEntity;
import com.hungteen.pvz.common.entity.misc.drop.JewelEntity;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import com.hungteen.pvz.common.impl.zombie.RoofZombies;
import com.hungteen.pvz.common.impl.zombie.ZombieType;
import com.hungteen.pvz.utils.AnimationUtil;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.MathUtil;
import com.hungteen.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class EdgarZombotEntity extends ZombotEntity {

    private static final float UP_PIVOT_Y = - 45;
    private static final float UP_PIVOT_Z = - 1;
    private static final float ARM_OFF_X = 30;
    private static final float ARM_OFF_Y = - 70;
    private static final float ARM_OFF_Z = 4;
    private static final float HAND_OFF_X = 2;
    private static final float HAND_OFF_Y = 85;
    private static final float HAND_OFF_Z = - 3;
    private static final float HEAD_OFF_Y = - 80;
    private static final float HEAD_OFF_Z = - 6;
    private static final float MOUTH_TIP_DOWN = 30;
    private static final float MOUTH_TIP_FRONT = 26;
    private static final float SHOOT_BODY_ANGLE = 90;
    private static final float SHOOT_MOUTH_ANGLE = 30;
    private static final float STEAL_ARM_ANGLE = - 45;
    private static final float SPAWN_BODY_ANGLE = 50;
    private static final float SPAWN_ARM_ANGLE = - 55;
    private static final float THROW_ARM_ANGLE = - 65;

    protected int throwCarTick;
    protected int spawnZombieTick;

    public EdgarZombotEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        this.refreshCountCD = 10;
        this.maxZombieSurround = 60;
        this.maxPlantSurround = 45;
        this.kickRange = 6;
        this.throwCarTick = 0;
        this.spawnZombieTick = this.getSpawnZombieCD();
        this.noCulling = true;
        this.setIsWholeBody();
    }

    @Override
    protected void registerAttackGoals() {
        super.registerAttackGoals();
        this.goalSelector.addGoal(3, new EdgarThrowCarGoal(this));
        this.goalSelector.addGoal(6, new EdgarSpawnZombieGoal(this));
    }

    @Override
    protected void spawnSpecialDrops() {
        final int playerCnt = this.bossInfo.getPlayers().size();
        for (int i = 0; i < 4 + 3 * playerCnt; ++i) {
            JewelEntity jewel = EntityRegister.JEWEL.get().create(level);
            EntityUtil.onEntityRandomPosSpawn(level, jewel, blockPosition().above(5), 4);
        }
    }

    @Override
    public int getBossStage() {
        final float percent = this.bossInfo.getProgress();
        return percent > 4F / 5 ? 1 :
                percent > 3F / 5 ? 2 :
                        percent > 2F / 5 ? 3 :
                                percent > 1F / 5 ? 4 : 5;
    }

    public EntityDimensions getDimensions(Pose poseIn) {
        return EntityDimensions.scalable(2F, 9F);
    }

    @Override
    public int getShootBallCD() {
        return this.getBossStage() < 2 ? 700 : this.getBossStage() < 4 ? 500 : 300;
    }

    @Override
    public int getStealPlantCD() {
        return this.getBossStage() < 3 ? 600 : 400;
    }

    // 原作火球 _ground 轨道每动画帧位移 18.5 像素，anim_role 速率 2 帧/秒，即 37 像素/秒；80 像素/格、1 格合 2 米、20 tick/秒换算为 0.04625 米/tick，取 4/3
    @Override
    public float getElementBallSpeed() {
        return 0.0617F;
    }

    @Override
    public float getEatDamage() {
        return ZombieUtil.LITTLE_HIGH;
    }

    @Override
    public float getInnerLife() {
        return 5000;
    }

    @Override
    public ZombieType getZombieType() {
        return RoofZombies.EDGAR_ZOMBOT;
    }

    public int getThrowCarCD() {
        return 800;
    }

    public int getSpawnZombieCD() {
        final int stage = this.getBossStage();
        final int base = stage <= 1 ? 100 : stage == 2 ? 80 : 40;
        return base + MathUtil.getRandomInRange(this.getRandom(), 10);
    }

    /**
     * Skill : throw destroy car to random target.
     * {@link EdgarThrowCarGoal#tick()}
     */
    public void throwDestroyCar() {
        final float range = 50;
        final List<LivingEntity> list = EntityUtil.getTargetableLivings(this, EntityUtil.getEntityAABB(this, range, range));
        if (! list.isEmpty()) {
            final Vec3 handPos = this.getHandPos(this.getThrowArmRot());
            final int pos = this.getRandom().nextInt(list.size());
            DestroyCarEntity car = EntityRegister.DESTROY_CAR.get().create(this.level);
            if (car != null) {
                car.summonByOwner(this);
                car.setPos(handPos.x, handPos.y, handPos.z);
                car.shootPultBullet(list.get(pos));
                level.addFreshEntity(car);
            }
        }

        this.throwCarTick = this.getThrowCarCD() + MathUtil.getRandomInRange(this.getRandom(), 160);
        this.spawnZombieTick = 100;
    }

    protected void startHoldingZombie() {
        this.getSummonZombie().ifPresent(entity -> {
            if (entity instanceof PVZZombieEntity zombie && ! this.level.isClientSide) {
                ZombieUtil.copySummonZombieData(this, zombie);
                zombie.finalizeSpawn((ServerLevel) this.level, this.level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.SPAWNER, null, null);
                zombie.setNoAi(true);
                final Vec3 holdPos = this.getHeldZombiePos();
                zombie.absMoveTo(holdPos.x, holdPos.y, holdPos.z, this.yBodyRot, 0);
                zombie.startRiding(this, true);
                this.level.addFreshEntity(zombie);
            }
        });
    }

    protected void releaseHeldZombie() {
        this.getPassengers().forEach(rider -> {
            if (rider instanceof PVZZombieEntity zombie) {
                zombie.unRide();
                zombie.setNoAi(false);
            }
        });
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public void positionRider(Entity rider) {
        if (this.hasPassenger(rider)) {
            final Vec3 pos = this.getHeldZombiePos();
            rider.setPos(pos.x, pos.y + rider.getMyRidingOffset(), pos.z);
            rider.setYRot(this.yBodyRot);
            rider.yRotO = this.yBodyRot;
            if (rider instanceof Mob mob) {
                mob.yBodyRot = this.yBodyRot;
                mob.yBodyRotO = this.yBodyRot;
            }
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        final Vec3 pos = this.getHeldZombiePos();
        return new Vec3(pos.x, pos.y + passenger.getMyRidingOffset(), pos.z);
    }


    public float getSpawnBodyRot() {
        if (this.getRobotState() != EdgarStates.SPAWN) {
            return 0;
        }
        final int total = this.getAnimSpawnCD();
        final int now = this.getAttackTime();
        final int front = total * 3 / 5;
        if (now < front) {
            return AnimationUtil.getUp(now, front, SPAWN_BODY_ANGLE);
        }
        return AnimationUtil.getDown(now - front, total - front, SPAWN_BODY_ANGLE);
    }

    public float getSpawnArmRot() {
        if (this.getRobotState() != EdgarStates.SPAWN) {
            return 0;
        }
        final int total = this.getAnimSpawnCD();
        final int now = this.getAttackTime();
        final int front = total * 3 / 5;
        if (now < front) {
            return AnimationUtil.getUp(now, front, SPAWN_ARM_ANGLE);
        }
        return AnimationUtil.getDown(now - front, total - front, SPAWN_ARM_ANGLE);
    }

    public float getStealArmRot() {
        if (this.getRobotState() != EdgarStates.STEAL) {
            return 0;
        }
        final int total = this.getAnimStealCD();
        final int now = this.getAttackTime();
        final int front = total * 2 / 5;
        if (now < front) {
            return AnimationUtil.getUp(now, front, STEAL_ARM_ANGLE);
        }
        return AnimationUtil.getDown(now - front, total - front, STEAL_ARM_ANGLE);
    }

    // Zombie_boss.reanim 的 anim_RV_1 段落车事件在归一化进度 0.65
    public int getThrowReleaseTick() {
        return this.getAnimThrowCD() * 13 / 20;
    }

    public float getThrowArmRot() {
        if (this.getRobotState() != EdgarStates.CAR) {
            return 0;
        }
        final int total = this.getAnimThrowCD();
        final int now = this.getAttackTime();
        final int front = this.getThrowReleaseTick();
        if (now < front) {
            return AnimationUtil.getUp(now, front, THROW_ARM_ANGLE);
        }
        return AnimationUtil.getDown(now - front, total - front, THROW_ARM_ANGLE);
    }

    @Override
    protected BlockPos getBungeeSpawnPos() {
        return new BlockPos(this.getHandPos(this.getStealArmRot()));
    }

    @Override
    public Vec3 getBungeeAnchorPos() {
        return this.getHandPos(this.getStealArmRot());
    }

    private Vec3 getHandPos(float armRot) {
        final float cosArm = Mth.cos(armRot);
        final float sinArm = Mth.sin(armRot);
        final float px = (ARM_OFF_X + HAND_OFF_X) / 16.0F;
        final float py = (UP_PIVOT_Y + ARM_OFF_Y + HAND_OFF_Y * cosArm - HAND_OFF_Z * sinArm) / 16.0F;
        final float pz = (UP_PIVOT_Z + ARM_OFF_Z + HAND_OFF_Y * sinArm + HAND_OFF_Z * cosArm) / 16.0F;
        final float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        final float cosYaw = Mth.cos(yaw);
        final float sinYaw = Mth.sin(yaw);
        final float scale = this.getZombieType().getRenderScale();
        final double x = this.getX() + scale * (px * cosYaw + pz * sinYaw);
        final double y = this.getY() + scale * (1.501F - py);
        final double z = this.getZ() + scale * (px * sinYaw - pz * cosYaw);
        return new Vec3(x, y, z);
    }


    // Zombie_boss.reanim 的 anim_head_attack 段为 40 帧、12fps，出球事件在归一化进度 0.42
    @Override
    public int getAnimShootCD() {
        return 67;
    }

    @Override
    public int getShootFireTick() {
        return 28;
    }

    public float getShootBodyRot() {
        if (this.getRobotState() != EdgarStates.FLAME && this.getRobotState() != EdgarStates.ICE) {
            return 0;
        }
        final int total = this.getAnimShootCD();
        final int now = this.getAttackTime();
        final int front = this.getShootFireTick();
        if (now < front) {
            return AnimationUtil.getUp(now, front, SHOOT_BODY_ANGLE);
        }
        return AnimationUtil.getDown(now - front, total - front, SHOOT_BODY_ANGLE);
    }

    public float getShootMouthRot() {
        if (this.getRobotState() != EdgarStates.FLAME && this.getRobotState() != EdgarStates.ICE) {
            return 0;
        }
        final int total = this.getAnimShootCD();
        final int now = this.getAttackTime();
        final int front = this.getShootFireTick();
        if (now < front) {
            return AnimationUtil.getUp(now, front, SHOOT_MOUTH_ANGLE);
        }
        return AnimationUtil.getDown(now - front, total - front, SHOOT_MOUTH_ANGLE);
    }

    @Override
    protected Vec3 getElementBallSpawnPos() {
        final float bodyRot = this.getShootBodyRot();
        final float cosBody = Mth.cos(bodyRot);
        final float sinBody = Mth.sin(bodyRot);
        final float headY = HEAD_OFF_Y * cosBody - HEAD_OFF_Z * sinBody;
        final float headZ = HEAD_OFF_Y * sinBody + HEAD_OFF_Z * cosBody;
        final float py = (UP_PIVOT_Y + headY + MOUTH_TIP_DOWN) / 16.0F;
        final float pz = (UP_PIVOT_Z + headZ - MOUTH_TIP_FRONT) / 16.0F;
        final float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        final float cosYaw = Mth.cos(yaw);
        final float sinYaw = Mth.sin(yaw);
        final float scale = this.getZombieType().getRenderScale();
        final double x = this.getX() + scale * (pz * sinYaw);
        final double y = this.getY() + scale * (1.501F - py);
        final double z = this.getZ() - scale * (pz * cosYaw);
        return new Vec3(x, y, z);
    }

    @Override
    protected Vec3 getElementBallDirection(ElementBallEntity ball) {
        return this.calculateViewVector(0F, this.yHeadRot);
    }

    private Vec3 getHeldZombiePos() {
        final float bodyRot = this.getSpawnBodyRot();
        final float armRot = bodyRot + this.getSpawnArmRot();
        final float cosBody = Mth.cos(bodyRot);
        final float sinBody = Mth.sin(bodyRot);
        final float cosArm = Mth.cos(armRot);
        final float sinArm = Mth.sin(armRot);
        final float px = (ARM_OFF_X + HAND_OFF_X) / 16.0F;
        final float py = (UP_PIVOT_Y + ARM_OFF_Y * cosBody - ARM_OFF_Z * sinBody + HAND_OFF_Y * cosArm - HAND_OFF_Z * sinArm) / 16.0F;
        final float pz = (UP_PIVOT_Z + ARM_OFF_Y * sinBody + ARM_OFF_Z * cosBody + HAND_OFF_Y * sinArm + HAND_OFF_Z * cosArm) / 16.0F;
        final float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        final float cosYaw = Mth.cos(yaw);
        final float sinYaw = Mth.sin(yaw);
        final float scale = this.getZombieType().getRenderScale();
        final double x = this.getX() + scale * (px * cosYaw + pz * sinYaw);
        final double y = this.getY() + scale * (1.501F - py);
        final double z = this.getZ() + scale * (px * sinYaw - pz * cosYaw);
        return new Vec3(x, y, z);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        this.releaseHeldZombie();
        super.remove(reason);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("zomboss_throw_car_tick")) {
            this.throwCarTick = compound.getInt("zomboss_throw_car_tick");
        }
        if (compound.contains("zomboss_spawn_cd")) {
            this.spawnZombieTick = compound.getInt("zomboss_spawn_cd");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("zomboss_throw_car_tick", this.throwCarTick);
        compound.putInt("zomboss_spawn_cd", this.spawnZombieTick);
    }

    protected static class EdgarThrowCarGoal extends Goal {

        private final EdgarZombotEntity edgarRobot;

        public EdgarThrowCarGoal(EdgarZombotEntity edgarRobot){
            this.edgarRobot = edgarRobot;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.edgarRobot.getRobotState() == EdgarStates.CAR) {
                return true;
            }
            if(-- this.edgarRobot.throwCarTick > 0){
                return false;
            }
            return this.edgarRobot.getTarget() != null && this.edgarRobot.getRobotState() == EdgarStates.NORMAL && this.edgarRobot.bossInfo.getProgress() < 1.0F / 2 && this.edgarRobot.nearbyPlantCount > 8;
        }

        @Override
        public boolean canContinueToUse() {
            return this.edgarRobot.getRobotState() == EdgarStates.CAR;
        }

        @Override
        public void start() {
            if (this.edgarRobot.getRobotState() == EdgarStates.NORMAL) {
                this.edgarRobot.setRobotState(EdgarStates.CAR);
                this.edgarRobot.setAttackTime(0);
            }
        }

        @Override
        public void tick() {
            final LivingEntity target = this.edgarRobot.getTarget();
            if (target != null) {
                this.edgarRobot.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
            }
            this.edgarRobot.setAttackTime(this.edgarRobot.getAttackTime() + 1);
            final int throwTime = this.edgarRobot.getAttackTime();
            if(throwTime == this.edgarRobot.getThrowReleaseTick()){
                this.edgarRobot.throwDestroyCar();
            } else if(throwTime >= this.edgarRobot.getAnimThrowCD()){
                this.edgarRobot.setRobotState(EdgarStates.NORMAL);
            }
        }

        @Override
        public void stop() {
            this.edgarRobot.setAttackTime(0);
        }
    }

    protected static class EdgarSpawnZombieGoal extends Goal {

        private final EdgarZombotEntity edgarRobot;

        public EdgarSpawnZombieGoal(EdgarZombotEntity edgarRobot){
            this.edgarRobot = edgarRobot;
        }

        @Override
        public boolean canUse() {
            if (this.edgarRobot.getRobotState() == EdgarStates.SPAWN) {
                return true;
            }
            if(-- this.edgarRobot.spawnZombieTick > 0){
                return false;
            }
            return this.edgarRobot.getTarget() != null && this.edgarRobot.getRobotState() == EdgarStates.NORMAL && this.edgarRobot.nearbyZombieCount < this.edgarRobot.maxZombieSurround;
        }

        @Override
        public boolean canContinueToUse() {
            return this.edgarRobot.getRobotState() == EdgarStates.SPAWN;
        }

        @Override
        public void start() {
            if (this.edgarRobot.getRobotState() == EdgarStates.NORMAL) {
                this.edgarRobot.setRobotState(EdgarStates.SPAWN);
                this.edgarRobot.setAttackTime(0);
                this.edgarRobot.startHoldingZombie();
            }
        }

        @Override
        public void tick() {
            //goalSelector keeps running while frozen; hold attackTime so the spawn waits until thaw.
            if (this.edgarRobot.canNormalUpdate()) {
                this.edgarRobot.setAttackTime(this.edgarRobot.getAttackTime() + 1);
                final int spawnTime = this.edgarRobot.getAttackTime();
                if(spawnTime == this.edgarRobot.getAnimSpawnCD() * 3 / 5){
                    this.edgarRobot.releaseHeldZombie();
                } else if(spawnTime >= this.edgarRobot.getAnimSpawnCD()){
                    this.edgarRobot.spawnZombieTick = this.edgarRobot.getSpawnZombieCD();
                    this.edgarRobot.setRobotState(EdgarStates.NORMAL);
                }
            }
        }

        @Override
        public void stop() {
            this.edgarRobot.setAttackTime(0);
            this.edgarRobot.releaseHeldZombie();
        }
    }

}