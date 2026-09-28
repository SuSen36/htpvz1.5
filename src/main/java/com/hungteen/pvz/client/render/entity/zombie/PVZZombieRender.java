package com.hungteen.pvz.client.render.entity.zombie;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.enums.BodyType;
import com.hungteen.pvz.client.ClientProxy;
import com.hungteen.pvz.client.particle.ModelPartParticle;
import com.hungteen.pvz.client.render.entity.PVZCreatureRender;
import com.hungteen.pvz.client.render.layer.fullskin.*;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import com.hungteen.pvz.common.entity.zombie.base.AbstractEdgarZombotEntity;
import com.hungteen.pvz.common.potion.EffectRegister;
import com.hungteen.pvz.utils.ConfigUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public abstract class PVZZombieRender <T extends PVZZombieEntity> extends PVZCreatureRender<T> {

	public PVZZombieRender(EntityRendererProvider.Context context, EntityModel<T> entityModelIn, float shadowSizeIn) {
		super(context, entityModelIn, shadowSizeIn);
		this.addZombieLayers();
	}

	@Override
	protected boolean isBodyVisible(T livingEntityIn) {
		return super.isBodyVisible(livingEntityIn) || PVZMod.PROXY.getPlayer().hasEffect(EffectRegister.LIGHT_EYE_EFFECT.get());
	}

	protected void addZombieLayers() {
		this.addLayer(new EnergyLayer<>(this));
		this.addLayer(new CharmLayer<>(this));
		this.addLayer(new ColdLayer<>(this));
		this.addLayer(new SunLightLayer<>(this));
		this.addLayer(new SunLayer<>(this));
	}

	@Override
	public void render(T zombie, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		//htpvz2式本地检测：掉手/掉头/死亡由渲染器直接生成粒子，不发包；一次性标志防重复
		if (ConfigUtil.enableZombieDropParts() && ! ClientProxy.MC.isPaused()) {
			if (zombie.renderHand && ! zombie.hasHand() && zombie.canLostHand()) {
				zombie.renderHand = false;
				this.createBodyParticle(zombie, BodyType.HAND, Optional.empty());
			}
			if (zombie.renderHead && ! zombie.hasHead() && zombie.canLostHead()) {
				zombie.renderHead = false;
				this.createBodyParticle(zombie, BodyType.HEAD, Optional.empty());
			}
			if (zombie.renderBody && zombie.isDeadOrDying()) {
				zombie.renderBody = false;
				if (zombie instanceof AbstractEdgarZombotEntity) {//僵王六部位一次抛落
					List.of(BodyType.HEAD, BodyType.BODY, BodyType.LEFT_HAND, BodyType.RIGHT_HAND, BodyType.LEFT_LEG, BodyType.RIGHT_LEG)
							.forEach(type -> this.createBodyParticle(zombie, type, Optional.empty()));
				} else {
					this.createBodyParticle(zombie, BodyType.BODY, Optional.empty());
				}
			}
		}
		super.render(zombie, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}
	
	@Override
	public Vec3 getTranslateVec(T entity) {
		float height = this.getOffsetRisingHeight();
		float downOffset = entity.getAnimTime() < 0 ? (- entity.getAnimTime() * 1.0f / PVZZombieEntity.RISING_CD) * height : 0;
		return new Vec3(0, downOffset, 0);
	}
	
	@Override
	protected float getScaleByEntity(T entity) {
		final float sz = entity.getZombieType().getRenderScale();
		return entity.isMiniZombie() ? sz * PVZZombieEntity.MINI_SCALE : sz;
	}

	/**
	 * spawn a body part particle when the body part is dropped.
	 * called by renderer local detect(htpvz2式不发包)。
	 */
	public void createBodyParticle(T zombie, BodyType type, Optional<Vec3> damageSourcePos) {
		ModelPartParticle body = new ModelPartParticle((ClientLevel) zombie.level, zombie.position());
		//抛落风格按僵尸由各渲染器子类分派（镜像1.16.5僵尸子类onFallBody）。
		this.onFallBody(body, zombie, type, damageSourcePos);
		ClientProxy.MC.particleEngine.add(body);
	}

	/**
	 * 客户端抛落分派，镜像1.16.5僵尸实体 onFallBody + setBodyStates：默认死亡整体存活40；
	 * 僵王/整车由各自渲染器子类override，巨人经setBodyStates延命到DEATH_ANIM_CD。
	 */
	protected void onFallBody(ModelPartParticle body, T zombie, BodyType type, Optional<Vec3> damageSourcePos) {
		body.scale(this.getScaleByEntity(zombie));
		body.droppedByOwner(zombie, type, damageSourcePos);
		if (type == BodyType.BODY) {
			//ref PVZZombieEntity.onFallBody：死亡整体默认存活40（断手断头维持60）。
			body.setMaxLiveTick(40);
			this.setBodyStates(body, zombie);
		}
	}

	/**
	 * 镜像PVZZombieEntity.setBodyStates：默认空实现，巨人子类override延命。
	 */
	protected void setBodyStates(ModelPartParticle body, T zombie) {
	}

	protected float getOffsetRisingHeight() {
		return 1.6f;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return entity.getZombieType().getRenderResource();
	}

}