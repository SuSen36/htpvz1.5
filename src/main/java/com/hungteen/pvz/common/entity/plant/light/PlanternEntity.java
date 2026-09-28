package com.hungteen.pvz.common.entity.plant.light;

import com.hungteen.pvz.api.interfaces.IAlmanacEntry;
import com.hungteen.pvz.api.interfaces.ILightEffect;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.effect.OriginEffectEntity;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.potion.EffectRegister;
import com.hungteen.pvz.common.world.challenge.ChallengeManager;
import com.hungteen.pvz.utils.EffectUtil;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.enums.Colors;
import com.hungteen.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class PlanternEntity extends PVZPlantEntity implements ILightEffect {

	//ref htpvz2 OfferBrightnessGoal: refresh every 10 ticks, effect lasts 30 ticks.
	private static final int EFFECT_CD = 10;
	private static final int LIGHT_EYE_TIME = 30;
	//ref htpvz2: base range is XZ +/-8, Y +/-3, more light range skill enlarges it.
	private static final double LIGHT_RANGE = 8.0D;
	private static final double LIGHT_HEIGHT = 3.0D;
	private static final double RANGE_BONUS_PER_LEVEL = 4.0D;
	private static final double HEIGHT_BONUS_PER_LEVEL = 1.0D;

	public PlanternEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}
	
	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if(! level.isClientSide()) {
			if(this.getExistTick() % EFFECT_CD == 0) {
				this.giveLightToPlayers();
			}
		}
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec3d, InteractionHand hand) {
		if(hand == InteractionHand.MAIN_HAND && player.getItemInHand(hand).isEmpty() && this.isInSuperState()){
			if(! this.level.isClientSide()){
				OriginEffectEntity.create(this.level, this.blockPosition().above(), Colors.YELLOW);
                
				this.displayAllRaider();

				EntityUtil.playSound(this, this.getSpawnSound().get());
			}
			return InteractionResult.SUCCESS;
		}
		return super.interactAt(player, vec3d, hand);
	}

	/**
	 * {@link #normalPlantTick()}
	 */
	private void giveLightToPlayers() {
		final float range = this.getEffectRange();
		final float height = this.getEffectHeight();
		//ref htpvz2: AABB centers on entity position.
		final AABB aabb = new AABB(this.getX() - range, this.getY() - height, this.getZ() - range,
				this.getX() + range, this.getY() + height, this.getZ() + range);
		final int nightVisionTime = this.getNightVisionTime();
		//ref htpvz2: grant light eye to itself and every teammate in range.
		this.addEffect(this.getLightEyeEffect(), this);
		EntityUtil.getFriendlyLivings(this, aabb).forEach(entity -> {
			entity.addEffect(this.getLightEyeEffect(), this);
			if(nightVisionTime > 0){
				entity.addEffect(EffectUtil.viewEffect(MobEffects.NIGHT_VISION, nightVisionTime, 0), this);
			}
		});
		//ref htpvz2: bright light removes enemies' invisibility.
		this.level.getEntitiesOfClass(LivingEntity.class, aabb, target ->
				! EntityUtil.isFriendly(this, target) && target.hasEffect(MobEffects.INVISIBILITY)
		).forEach(target -> target.removeEffect(MobEffects.INVISIBILITY));
	}

	private void displayAllRaider(){
		if(this.level instanceof ServerLevel){
			ChallengeManager.getChallengeNearBy((ServerLevel) this.level, this.blockPosition()).ifPresent(challenge -> {
				challenge.getRaiders().forEach(raider -> {
					if(raider instanceof LivingEntity) {
						((LivingEntity) raider).addEffect(EffectUtil.viewEffect(MobEffects.GLOWING, 200, 0));
					} else {
						raider.setGlowingTag(true);
					}
				});
			});
		}
	}

	public float getEffectRange(){
		return (float) (LIGHT_RANGE + SkillTypes.getSkillLevel(this.getSkills(), SkillTypes.MORE_LIGHT_RANGE) * RANGE_BONUS_PER_LEVEL);
	}

	public float getEffectHeight(){
		return (float) (LIGHT_HEIGHT + SkillTypes.getSkillLevel(this.getSkills(), SkillTypes.MORE_LIGHT_RANGE) * HEIGHT_BONUS_PER_LEVEL);
	}

	//night vision skill only acts as the unlock gate, its duration equals the light eye time.
	public int getNightVisionTime(){
		return SkillTypes.getSkillLevel(this.getSkills(), SkillTypes.NIGHT_VISION) > 0 ? LIGHT_EYE_TIME : 0;
	}

	@Override
	public MobEffectInstance getLightEyeEffect() {
		return EffectUtil.effect(EffectRegister.LIGHT_EYE_EFFECT.get(), this.getLightEyeTime(), 0);
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.EFFECT_TIME, this.getLightEyeTime()));
	}

	public int getLightEyeTime() {
		return LIGHT_EYE_TIME;
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	@Override
	public Optional<SoundEvent> getSpawnSound() {
		return Optional.ofNullable(SoundRegister.PLANTERN.get());
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.PLANTERN;
	}
	
}