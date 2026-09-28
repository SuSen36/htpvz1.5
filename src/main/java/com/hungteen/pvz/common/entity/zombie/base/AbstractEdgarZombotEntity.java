package com.hungteen.pvz.common.entity.zombie.base;

import com.hungteen.pvz.api.types.IZombieType;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.ai.goal.target.PVZNearestTargetGoal;
import com.hungteen.pvz.common.entity.misc.ElementBallEntity;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import com.hungteen.pvz.common.entity.zombie.roof.BungeeZombieEntity;
import com.hungteen.pvz.common.impl.zombie.*;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.*;
import com.hungteen.pvz.utils.others.EntityList;
import com.hungteen.pvz.utils.others.WeightList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * @program: pvzmod-1.16.5
 * @author: HungTeen
 * @create: 2022-02-04 11:32
 **/
public abstract class AbstractEdgarZombotEntity extends AbstractBossZombieEntity {

    private static final EntityDataAccessor<Integer> STATES = SynchedEntityData.defineId(AbstractEdgarZombotEntity.class, EntityDataSerializers.INT);
    private static final WeightList<ZombieType> ZOMBIES_1 = new WeightList<>();
    private static final WeightList<ZombieType> ZOMBIES_2 = new WeightList<>();
    private static final WeightList<ZombieType> ZOMBIES_3 = new WeightList<>();
    private static final WeightList<ZombieType> ZOMBIES_4 = new WeightList<>();
    private static final WeightList<ZombieType> ZOMBIES_5 = new WeightList<>();
    protected int shootBallTick;
    protected int stealPlantTick;
    private final EntityList<BungeeZombieEntity> stealBungees = new EntityList<>();

    static {
        {//stage 1 zombies.
            ZOMBIES_1.addItem(GrassZombies.NORMAL_ZOMBIE, 100);
            ZOMBIES_1.addItem(GrassZombies.CONEHEAD_ZOMBIE, 30);
            ZOMBIES_1.addItem(GrassZombies.BUCKETHEAD_ZOMBIE, 30);
            ZOMBIES_1.addItem(GrassZombies.POLE_ZOMBIE, 30);
            ZOMBIES_1.addItem(PoolZombies.JACK_IN_BOX_ZOMBIE, 30);
            ZOMBIES_1.addItem(RoofZombies.LADDER_ZOMBIE, 30);
            ZOMBIES_1.addItem(PoolZombies.POGO_ZOMBIE, 30);
            ZOMBIES_1.addItem(GrassZombies.NEWSPAPER_ZOMBIE, 30);
            ZOMBIES_1.addItem(GrassZombies.SCREENDOOR_ZOMBIE, 30);
        }
        {//stage 2 zombies.
            ZOMBIES_2.addItem(GrassZombies.CONEHEAD_ZOMBIE, 100);
            ZOMBIES_2.addItem(GrassZombies.BUCKETHEAD_ZOMBIE, 30);
            ZOMBIES_2.addItem(GrassZombies.FOOTBALL_ZOMBIE, 30);
            ZOMBIES_2.addItem(GrassZombies.POLE_ZOMBIE, 30);
            ZOMBIES_2.addItem(PoolZombies.JACK_IN_BOX_ZOMBIE, 30);
            ZOMBIES_2.addItem(RoofZombies.LADDER_ZOMBIE, 30);
            ZOMBIES_2.addItem(PoolZombies.ZOMBONI, 30);
            ZOMBIES_2.addItem(RoofZombies.CATAPULT_ZOMBIE, 30);
            ZOMBIES_2.addItem(PoolZombies.POGO_ZOMBIE, 30);
            ZOMBIES_2.addItem(GrassZombies.NEWSPAPER_ZOMBIE, 30);
            ZOMBIES_2.addItem(GrassZombies.SCREENDOOR_ZOMBIE, 30);
            ZOMBIES_2.addItem(RoofZombies.GARGANTUAR, 30);
        }
        {//stage 3 zombies.
            ZOMBIES_3.addItem(GrassZombies.BUCKETHEAD_ZOMBIE, 100);
            ZOMBIES_3.addItem(GrassZombies.FOOTBALL_ZOMBIE, 30);
            ZOMBIES_3.addItem(GrassZombies.POLE_ZOMBIE, 30);
            ZOMBIES_3.addItem(PoolZombies.JACK_IN_BOX_ZOMBIE, 30);
            ZOMBIES_3.addItem(RoofZombies.LADDER_ZOMBIE, 30);
            ZOMBIES_3.addItem(PoolZombies.ZOMBONI, 30);
            ZOMBIES_3.addItem(RoofZombies.CATAPULT_ZOMBIE, 30);
            ZOMBIES_3.addItem(PoolZombies.POGO_ZOMBIE, 30);
            ZOMBIES_3.addItem(GrassZombies.NEWSPAPER_ZOMBIE, 30);
            ZOMBIES_3.addItem(GrassZombies.SCREENDOOR_ZOMBIE, 30);
            ZOMBIES_3.addItem(RoofZombies.GARGANTUAR, 30);
        }
        {//stage 4 zombies.
            ZOMBIES_4.addItem(GrassZombies.CONEHEAD_ZOMBIE, 100);
            ZOMBIES_4.addItem(GrassZombies.BUCKETHEAD_ZOMBIE, 100);
            ZOMBIES_4.addItem(GrassZombies.FOOTBALL_ZOMBIE, 100);
            ZOMBIES_4.addItem(GrassZombies.POLE_ZOMBIE, 100);
            ZOMBIES_4.addItem(PoolZombies.JACK_IN_BOX_ZOMBIE, 100);
            ZOMBIES_4.addItem(RoofZombies.LADDER_ZOMBIE, 100);
            ZOMBIES_4.addItem(PoolZombies.ZOMBONI, 100);
            ZOMBIES_4.addItem(RoofZombies.CATAPULT_ZOMBIE, 100);
            ZOMBIES_4.addItem(PoolZombies.POGO_ZOMBIE, 100);
            ZOMBIES_4.addItem(GrassZombies.NEWSPAPER_ZOMBIE, 100);
            ZOMBIES_4.addItem(GrassZombies.SCREENDOOR_ZOMBIE, 100);
            ZOMBIES_4.addItem(RoofZombies.GARGANTUAR, 100);
        }
        {//stage 5 zombies.
            ZOMBIES_5.addItem(GrassZombies.CONEHEAD_ZOMBIE, 100);
            ZOMBIES_5.addItem(GrassZombies.BUCKETHEAD_ZOMBIE, 100);
            ZOMBIES_5.addItem(GrassZombies.FOOTBALL_ZOMBIE, 100);
            ZOMBIES_5.addItem(GrassZombies.POLE_ZOMBIE, 100);
            ZOMBIES_5.addItem(PoolZombies.JACK_IN_BOX_ZOMBIE, 100);
            ZOMBIES_5.addItem(RoofZombies.LADDER_ZOMBIE, 100);
            ZOMBIES_5.addItem(PoolZombies.ZOMBONI, 100);
            ZOMBIES_5.addItem(RoofZombies.CATAPULT_ZOMBIE, 100);
            ZOMBIES_5.addItem(PoolZombies.POGO_ZOMBIE, 100);
            ZOMBIES_5.addItem(GrassZombies.NEWSPAPER_ZOMBIE, 100);
            ZOMBIES_5.addItem(GrassZombies.SCREENDOOR_ZOMBIE, 100);
            ZOMBIES_5.addItem(RoofZombies.GARGANTUAR, 100);
        }
    }

    public AbstractEdgarZombotEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        this.setIsWholeBody();
        this.shootBallTick = this.getShootBallCD();
        this.stealPlantTick = this.getStealPlantCD();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATES, EdgarStates.NORMAL.ordinal());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 40.0F));
        registerTargetGoals();
        registerAttackGoals();
    }

    @Override
    protected void registerAttackGoals() {
        this.goalSelector.addGoal(4, new EdgarShootBallGoal(this));
        this.goalSelector.addGoal(5, new EdgarStealPlantGoal(this));
    }

    @Override
    protected void registerTargetGoals() {
        this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, false, true, ZombieUtil.LITTLE_CLOSE_TARGET_RANGE, ZombieUtil.LITTLE_HIGH_TARGET_HEIGHT));
    }

    @Override
    public boolean canPAZTarget(Entity target) {
        return true;
    }

    //ZOMBIE_BOSS can only be chilled or frozen while its head is lowered to spit.
    @Override
    public boolean canBeCold() {
        return this.getRobotState() == EdgarStates.FLAME || this.getRobotState() == EdgarStates.ICE;
    }

    @Override
    public boolean canBeFrozen() {
        return this.getRobotState() == EdgarStates.FLAME || this.getRobotState() == EdgarStates.ICE;
    }

    @Override
    public void zombieTick() {
        super.zombieTick();
        if (! level.isClientSide()) {
            for (BungeeZombieEntity zombie : this.stealBungees) {
                if (zombie.isAlive()) {
                    zombie.setOriginPos(this.getBungeeAnchorPos());
                }
            }
        }
    }

    //ZOMBIE_BOSS is rejected unconditionally in Zombie::ApplyButter, even while its head is lowered.
    @Override
    public boolean canBeButtered() {
        return false;
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime % 20 == 1) {
            if (level.isClientSide()) {
                level.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 0, 0, 0);
                level.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY() + 5, getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected int getDeathTime() {
        return 60;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level.isClientSide()) {
            this.bossInfo.getPlayers().forEach((player) -> {
                CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(player, this, source);
            });
        }
    }

    /**
     * Skill : Shoot ElementBall To Target
     * {@link EdgarShootBallGoal#tick()}
     */
    public void shootElementBall() {
        final ElementBallEntity ball = EntityRegister.ELEMENT_BALL.get().create(level);
        ball.summonByOwner(this);
        ball.setSpeed(this.getElementBallSpeed());
        final Vec3 spawnPos = this.getElementBallSpawnPos();
        ball.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        ball.shoot(this.getElementBallDirection(ball));
        ball.setElementBallType(this.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.ICE ? ElementBallEntity.ElementTypes.ICE : ElementBallEntity.ElementTypes.FLAME);
        this.level.addFreshEntity(ball);

        this.shootBallTick = this.getShootBallCD() + MathUtil.getRandomInRange(this.getRandom(), 80);
    }

    protected Vec3 getElementBallSpawnPos() {
        return new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ());
    }

    protected Vec3 getElementBallDirection(ElementBallEntity ball) {
        final LivingEntity target = this.getTarget();
        if (target != null) {
            return target.position().subtract(ball.position());
        }
        return this.getLookAngle();
    }

    public int getShootFireTick() {
        return this.getAnimShootCD() + 1;
    }

    /**
     * Skill : Steal Target By Bungee.
     * {@link EdgarStealPlantGoal#tick()}
     */
    public void stealRandomTargets() {
        this.stealBungees.clear();
        final int cnt = this.getStealCount();
        final float range = 50;
        List<LivingEntity> list = EntityUtil.getTargetableLivings(this, EntityUtil.getEntityAABB(this, range, range));
        if (! list.isEmpty()) {
            for (int i = 0; i < cnt; ++i) {
                LivingEntity target = list.get(this.getRandom().nextInt(list.size()));
                BungeeZombieEntity zombie = EntityRegister.BUNGEE_ZOMBIE.get().create(level);
                zombie.setBungeeType(BungeeZombieEntity.BungeeTypes.STEAL);
                zombie.setStealTarget(target);
                zombie.setBungeeState(BungeeZombieEntity.BungeeStates.DOWN);
                zombie.setOriginLocked(true);
                ZombieUtil.copySummonZombieData(this, zombie);
                EntityUtil.onEntitySpawn(level, zombie, this.getBungeeSpawnPos());
                this.stealBungees.add(zombie);
            }
        }
    }

    protected boolean areStealBungeesDone() {
        int remaining = 0;
        boolean anyRising = false;
        for (BungeeZombieEntity zombie : this.stealBungees) {
            if (zombie.isAlive()) {
                if (zombie.getBungeeState() == BungeeZombieEntity.BungeeStates.UP) {
                    anyRising = true;
                } else {
                    ++ remaining;
                }
            }
        }
        return anyRising || remaining == 0;
    }

    protected BlockPos getBungeeSpawnPos() {
        return this.blockPosition().above(18);
    }

    public Vec3 getBungeeAnchorPos() {
        return Vec3.atBottomCenterOf(this.getBungeeSpawnPos());
    }

    protected Optional<? extends Mob> getSummonZombie() {
        // TODO 原作召唤分段按僵王存活时间（3500/8000/12500 更新），此处暂按剩余血量 stage，后续改为按存活时间
        final int stage = getBossStage();
        IZombieType zombieType;
        if (stage == 1) zombieType = ZOMBIES_1.getRandomItem(this.getRandom()).get();
        else if (stage == 2) zombieType = ZOMBIES_2.getRandomItem(this.getRandom()).get();
        else if (stage == 3) zombieType = ZOMBIES_3.getRandomItem(this.getRandom()).get();
        else if (stage == 4) zombieType = ZOMBIES_4.getRandomItem(this.getRandom()).get();
        else if (stage == 5) zombieType = ZOMBIES_5.getRandomItem(this.getRandom()).get();
        else {
            System.out.println("Error : Wrong Boss Stage !");
            return Optional.empty();
        }
        if (zombieType.getEntityType().isPresent()) {
            return Optional.ofNullable(zombieType.getEntityType().get().create(this.level));
        }
        return Optional.empty();
    }

    public abstract int getBossStage();

    public float getElementBallSpeed(){
        return 0.18F;
    }

    public int getStealCount(){
        return 3;
    }

    public int getAnimSpawnCD() {
        return 50;
    }

    public int getShootBallCD() {
        return 600;
    }

    public int getStealPlantCD() {
        return 400;
    }

    public int getAnimShootCD() {
        return 40;
    }

    public int getAnimThrowCD() {
        return 30;
    }

    public int getAnimStealCD() {
        return 30;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("zomboss_state")) {
            this.setRobotState(EdgarStates.values()[compound.getInt("zomboss_state")]);
        }
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        if (compound.contains("zomboss_shoot_ball_cd")) {
            this.shootBallTick = compound.getInt("zomboss_shoot_ball_cd");
        }
        if (compound.contains("zomboss_steal_cd")) {
            this.stealPlantTick = compound.getInt("zomboss_steal_cd");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("zomboss_state", this.getRobotState().ordinal());
        compound.putInt("zomboss_shoot_ball_cd", this.shootBallTick);
        compound.putInt("zomboss_steal_cd", this.stealPlantTick);
    }

    public void setRobotState(EdgarStates state) {
        this.entityData.set(STATES, state.ordinal());
    }

    public EdgarStates getRobotState() {
        return EdgarStates.values()[this.entityData.get(STATES)];
    }

    @Override
    public Optional<SoundEvent> getSpawnSound() {
        return Optional.ofNullable(SoundRegister.EDGAR_LAUGH.get());
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegister.EDGAR_EXPLOSION.get();
    }

    public enum EdgarStates {
        NORMAL,//yellow eyes
        FLAME,//red eyes
        ICE,//blue eyes
        STEAL,
        CAR,
        SPAWN,
    }

    protected static class EdgarShootBallGoal extends Goal {

        private final AbstractEdgarZombotEntity edgarRobot;

        public EdgarShootBallGoal(AbstractEdgarZombotEntity edgarRobot){
            this.edgarRobot = edgarRobot;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            final EdgarStates state = this.edgarRobot.getRobotState();
            if (state == EdgarStates.FLAME || state == EdgarStates.ICE) {
                return true;
            }
            if(-- this.edgarRobot.shootBallTick > 0){
                return false;
            }
            return this.edgarRobot.getTarget() != null && state == EdgarStates.NORMAL;
        }

        @Override
        public boolean canContinueToUse() {
            return this.edgarRobot.getRobotState() == EdgarStates.FLAME || this.edgarRobot.getRobotState() == EdgarStates.ICE;
        }

        @Override
        public void start() {
            if (this.edgarRobot.getRobotState() == EdgarStates.NORMAL) {
                this.edgarRobot.setRobotState(this.edgarRobot.getRandom().nextInt(2) == 0 ? EdgarStates.FLAME : EdgarStates.ICE);
                this.edgarRobot.bossInfo.getPlayers().forEach(p -> {
                    PlayerUtil.playClientSound(p, SoundRegister.EDGAR_SHOOT.get());
                });
                this.edgarRobot.setAttackTime(0);
            }
        }

        @Override
        public void tick() {
            //goalSelector keeps running while frozen; hold attackTime so the shot waits until thaw.
            if (this.edgarRobot.canNormalUpdate()) {
                final LivingEntity target = this.edgarRobot.getTarget();
                if (target != null) {
                    this.edgarRobot.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
                }
                this.edgarRobot.setAttackTime(this.edgarRobot.getAttackTime() + 1);
                final int shootTime = this.edgarRobot.getAttackTime();
                if(shootTime == this.edgarRobot.getShootFireTick()){
                    this.edgarRobot.shootElementBall();
                }
                if(shootTime >= this.edgarRobot.getAnimShootCD()){
                    this.edgarRobot.setRobotState(EdgarStates.NORMAL);
                }
            }
        }

        @Override
        public void stop() {
            this.edgarRobot.setAttackTime(0);
        }
    }

    protected static class EdgarStealPlantGoal extends Goal {

        private final AbstractEdgarZombotEntity edgarRobot;

        public EdgarStealPlantGoal(AbstractEdgarZombotEntity edgarRobot){
            this.edgarRobot = edgarRobot;
        }

        @Override
        public boolean canUse() {
            if (this.edgarRobot.getRobotState() == EdgarStates.STEAL) {
                return true;
            }
            if(-- this.edgarRobot.stealPlantTick > 0){
                return false;
            }
            return this.edgarRobot.getTarget() != null && this.edgarRobot.getRobotState() == EdgarStates.NORMAL && this.edgarRobot.nearbyPlantCount > 10;
        }

        @Override
        public boolean canContinueToUse() {
            return this.edgarRobot.getRobotState() == EdgarStates.STEAL;
        }

        @Override
        public void start() {
            if (this.edgarRobot.getRobotState() == EdgarStates.NORMAL) {
                this.edgarRobot.setRobotState(EdgarStates.STEAL);
                this.edgarRobot.setAttackTime(0);
            }
        }

        @Override
        public void tick() {
            //goalSelector keeps running while frozen; hold attackTime so the whole bungee sequence waits until thaw.
            if (this.edgarRobot.canNormalUpdate()) {
                final int now = this.edgarRobot.getAttackTime();
                final int front = this.edgarRobot.getAnimStealCD() * 2 / 5;
                if (now < front) {
                    this.edgarRobot.setAttackTime(now + 1);
                    if (now + 1 == front) {
                        this.edgarRobot.stealRandomTargets();
                    }
                } else if (now < this.edgarRobot.getAnimStealCD()) {
                    if (this.edgarRobot.areStealBungeesDone()) {
                        this.edgarRobot.setAttackTime(now + 1);
                    }
                } else {
                    this.edgarRobot.stealPlantTick = this.edgarRobot.getStealPlantCD() + MathUtil.getRandomInRange(this.edgarRobot.getRandom(), 120);
                    this.edgarRobot.setRobotState(EdgarStates.NORMAL);
                }
            }
        }

        @Override
        public void stop() {
            this.edgarRobot.setAttackTime(0);
        }
    }

}