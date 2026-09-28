package com.hungteen.pvz.client.render.entity.misc;

import com.hungteen.pvz.client.model.entity.misc.DestroyCarModel;
import com.hungteen.pvz.client.render.entity.PVZEntityRender;
import com.hungteen.pvz.common.entity.misc.DestroyCarEntity;
import com.hungteen.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DestroyCarRender extends PVZEntityRender<DestroyCarEntity> {

	public DestroyCarRender(EntityRendererProvider.Context context) {
		super(context, new DestroyCarModel(context.bakeLayer(DestroyCarModel.LAYER)));
	}

	@Override
	protected float getScaleByEntity(DestroyCarEntity entity) {
		return 1.0F;
	}

	@Override
	public ResourceLocation getTextureLocation(DestroyCarEntity entity) {
		return StringUtil.prefix("textures/entity/misc/destroy_car.png");
	}
}