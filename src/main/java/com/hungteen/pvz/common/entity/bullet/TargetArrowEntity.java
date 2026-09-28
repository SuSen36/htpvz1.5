package com.hungteen.pvz.common.entity.bullet;

import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.zombie.roof.BungeeZombieEntity;
import com.hungteen.pvz.common.entity.zombie.roof.BungeeZombieEntity.BungeeTypes;
import com.hungteen.pvz.common.item.ItemRegister;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class TargetArrowEntity extends AbstractArrow {

	public TargetArrowEntity(EntityType<? extends AbstractArrow> type, Level worldIn) {
		super(type, worldIn);
	}
	
	public TargetArrowEntity(Level worldIn, LivingEntity living) {
		super(EntityRegister.TARGET_ARROW.get(), living, worldIn);
	}
	
	@Override
	public void tick() {
		if(! level.isClientSide()) {
			if(! EntityUtil.isEntityValid(this.getOwner())) { // shooter died
				this.discard();
				return ;
			} else {
				if(this.getOwner() instanceof BungeeZombieEntity bungee) {
                    if(EntityUtil.isEntityValid(bungee.getStealTarget())) {
						this.shoot(bungee.getStealTarget());
					} else {
						this.discard();
						return ;
					}
				}
			}
		}
		super.tick();
	}
	
	@Override
	protected void onHitEntity(EntityHitResult result) {
		if(result.getEntity() instanceof LivingEntity && this.getOwner() instanceof Player) {// summon bungee
			if(! BungeeZombieEntity.canBungeeSteal(result.getEntity())) {
				super.onHitEntity(result);
				return ;
			}
			BungeeZombieEntity zombie = EntityRegister.BUNGEE_ZOMBIE.get().create(level);
			zombie.setBungeeType(BungeeTypes.HELP);
			zombie.setCharmed(true);
			zombie.setStealTarget((LivingEntity) result.getEntity());
			EntityUtil.onEntitySpawn(level, zombie, blockPosition().above(20));
			super.onHitEntity(result);
this.discard();
		}
	}
	
	public void shoot(LivingEntity target) {
		Vec3 speed = target.position().subtract(this.position()).normalize();
		double multi = 1.3D;
		this.setDeltaMovement(speed.multiply(multi, multi, multi));
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(ItemRegister.TARGET_ARROW.get());
	}
	
	@Override
	public byte getPierceLevel() {
		return 0;
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}