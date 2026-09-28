package com.hungteen.pvz.common.entity.zombie.grass;

import com.hungteen.pvz.common.entity.plant.assist.GraveBusterEntity;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.item.spawn.card.PlantCardItem;
import com.hungteen.pvz.utils.ZombieUtil;
import com.hungteen.pvz.utils.others.WeightList;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractTombStoneEntity extends PVZZombieEntity {

	protected static final WeightList<DropType> TOMBSTONE_DROP_LIST = new WeightList<>();
	protected int lifeRange = 2000;

	public AbstractTombStoneEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.setImmuneAllEffects();
		this.setIsWholeBody();
		this.canBeMini = false;
		this.canBeStealByBungee = false;
		this.canCollideWithZombie = false;
		this.canHelpAttack = false;
	}

	@Override
	protected void registerGoals() {
		//no goals.
	}

	@Override
	public VariantType getRandomVariant() {
		return VariantType.NORMAL;
	}

	public void tick() {
		if(! this.level.isClientSide() && this.onGround && this.getVehicle() == null) {
			this.setDeltaMovement(new Vec3(0,this.getDeltaMovement().y,0));
			//*0.6.4 prevent tombstones from getting knocked back.
		}
		super.tick();
		if(! this.level.isClientSide()) {
			BlockPos pos = this.blockPosition();
			this.setPos(pos.getX() + 0.5, this.getY(), pos.getZ() + 0.5);
			if (-- lifeRange < 0 && (level.getDayTime() % 24000 < 12000 && random.nextInt(100) == 0) && level.getNearestPlayer(this, 20) == null){
this.discard();
			}//*0.6.4 to avoid tombstones from accumulating.
		}
	}
	
	@Override
	public InteractionResult interactAt(Player player, Vec3 vec3d, InteractionHand hand) {
		if (! level.isClientSide()) {
			ItemStack stack = player.getItemInHand(hand);
			if (stack.getItem() instanceof PlantCardItem item) {// plant card right click plant entity
                if(PlantCardItem.checkSunAndInteractEntity(player, this, item, stack, type -> {
					return type == PVZPlants.GRAVE_BUSTER;
				}, plantEntity -> {
					if(plantEntity instanceof GraveBusterEntity) {
						plantEntity.setTarget(this);
					}
				})) {
					
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.interactAt(player, vec3d, hand);
	}

	@Override
	public float getWalkSpeed() {
		return 0;
	}

	@Override
	public float getEatDamage() {
		return 0;
	}

	@Override
	public float getFollowRange() {
		return ZombieUtil.CLOSE_TARGET_RANGE;
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return false;
	}
	
	@Override
	protected WeightList<DropType> getDropSpecialList() {
		if(TOMBSTONE_DROP_LIST.isEmpty()){
			TOMBSTONE_DROP_LIST.addAll(super.getDropSpecialList());
			TOMBSTONE_DROP_LIST.setLeftItem(DropType.COPPER);
		}
		return TOMBSTONE_DROP_LIST;
	}

	@Override
	public float getLife() {
		return 70;
	}

	@Override
	public double getPassengersRidingOffset() {
		return 0;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}
	
}