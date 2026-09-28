package com.hungteen.pvz.client.render.entity.zombie.roof;

import com.hungteen.pvz.api.enums.BodyType;
import com.hungteen.pvz.client.model.entity.zombie.roof.EdgarZombotModel;
import com.hungteen.pvz.client.particle.ModelPartParticle;
import com.hungteen.pvz.client.render.entity.zombie.PVZZombieRender;
import com.hungteen.pvz.common.entity.zombie.roof.EdgarZombotEntity;
import com.hungteen.pvz.utils.MathUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class EdgarZombotRender<T extends EdgarZombotEntity> extends PVZZombieRender<T> {

	public EdgarZombotRender(EntityRendererProvider.Context context) {
		super(context, new EdgarZombotModel<>(context.bakeLayer(EdgarZombotModel.LAYER)), 0);
	}

	//完全冻结时原版会按 tickCount 给渲染偏航叠加 cos 周期抖动，僵王冰冻只保留冰块覆盖、本体保持静止。
	@Override
	protected boolean isShaking(T zombie) {
		return false;
	}

	@Override
	protected void onFallBody(ModelPartParticle body, T zombie, BodyType type, Optional<Vec3> damageSourcePos) {
		body.scale(this.getScaleByEntity(zombie));
		body.updateInfo(zombie, type);
		body.setPos(zombie.position().x, zombie.position().y + zombie.getBbHeight() / 2, zombie.position().z);
		final float dx = MathUtil.getRandomFloat(zombie.getRandom());
		final float dy = zombie.getRandom().nextFloat();
		final float dz = MathUtil.getRandomFloat(zombie.getRandom());
		body.speed(new Vec3(dx, dy, dz));
		body.bodyYRot = (float) (Math.atan2(dx, dz) * (180.0D / Math.PI));
	}

}