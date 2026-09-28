package com.hungteen.pvz.client.render.entity.zombie.roof;

import com.hungteen.pvz.api.enums.BodyType;
import com.hungteen.pvz.client.model.entity.zombie.roof.ZombotModel;
import com.hungteen.pvz.client.particle.ModelPartParticle;
import com.hungteen.pvz.client.render.entity.zombie.PVZZombieRender;
import com.hungteen.pvz.common.entity.zombie.base.AbstractEdgarZombotEntity;
import com.hungteen.pvz.utils.MathUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ZombotRender<T extends AbstractEdgarZombotEntity> extends PVZZombieRender<T> {

	public ZombotRender(EntityRendererProvider.Context context) {
		super(context, new ZombotModel<>(context.bakeLayer(ZombotModel.LAYER)), 0);
	}

	@Override
	protected void onFallBody(ModelPartParticle body, T zombie, BodyType type, Optional<Vec3> damageSourcePos) {
		body.scale(this.getScaleByEntity(zombie));
		//ref AbstractEdgarZombotEntity.onFallBody：直接updateInfo+身体中心出生+随机三轴速度，存活默认60。
		body.updateInfo(zombie, type);
		body.setPos(zombie.position().x, zombie.position().y + zombie.getBbHeight() / 2, zombie.position().z);
		final float dx = MathUtil.getRandomFloat(zombie.getRandom());
		final float dy = zombie.getRandom().nextFloat();
		final float dz = MathUtil.getRandomFloat(zombie.getRandom());
		body.speed(new Vec3(dx, dy, dz));
		//ref ZombieDropBodyEntity.lerpMotion：部位朝向按各自抛飞方向(渲染为yRot+180)校正，镜像实体首帧旋转。
		body.bodyYRot = (float) (Math.atan2(dx, dz) * (180.0D / Math.PI));
	}

}