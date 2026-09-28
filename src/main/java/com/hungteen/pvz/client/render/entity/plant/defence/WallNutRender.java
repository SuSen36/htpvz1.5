package com.hungteen.pvz.client.render.entity.plant.defence;

import com.hungteen.pvz.client.model.entity.plant.defence.WallNutModel;
import com.hungteen.pvz.client.render.entity.plant.PVZPlantRender;
import com.hungteen.pvz.client.render.layer.component.WallNutArmorLayer;
import com.hungteen.pvz.common.entity.plant.defence.WallNutEntity;
import com.hungteen.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WallNutRender extends PVZPlantRender<WallNutEntity>{

	private final ResourceLocation TEX1 = StringUtil.prefix("textures/entity/plant/defence/wall_nut.png");
	private final ResourceLocation TEX2 = StringUtil.prefix("textures/entity/plant/defence/wall_nut_1.png");
	private final ResourceLocation TEX3 = StringUtil.prefix("textures/entity/plant/defence/wall_nut_2.png");
	//滚动绕坚果自身横轴，轴心取模型中心高度
	private static final float BOWLING_PIVOT_Y = 0.55F;
	private static final float BOWLING_ROLL_DEGREE = 15.0F;

	public WallNutRender(EntityRendererProvider.Context context) {
		super(context, new WallNutModel<>(context.bakeLayer(WallNutModel.LAYER)), 0.55f);
	}

	@Override
	protected void setupRotations(WallNutEntity entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
		if(entity.isBowling()) {
			/* 滚动必须排在朝向之前：顺序颠倒后旋转会落在世界轴上，横向滚动的坚果会立着打转 */
			poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot())));
			poseStack.translate(0.0D, BOWLING_PIVOT_Y, 0.0D);
			poseStack.mulPose(Vector3f.XP.rotationDegrees((entity.tickCount + partialTicks) * BOWLING_ROLL_DEGREE));
			poseStack.translate(0.0D, - BOWLING_PIVOT_Y, 0.0D);
		} else {
			super.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
		}
	}

	@Override
	protected void addPlantLayers() {
		super.addPlantLayers();
		this.addLayer(new WallNutArmorLayer(this));
	}

	@Override
	public ResourceLocation getTextureLocation(WallNutEntity entity) {
		final double percent = entity.getHealth() / entity.getMaxHealth();
		return percent > 2 / 3F ? TEX1 : percent > 1 / 3F ? TEX2 : TEX3;
	}
}