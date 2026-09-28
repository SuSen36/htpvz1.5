package com.hungteen.pvz.client.render.entity.zombie.roof;

import com.hungteen.pvz.client.model.entity.zombie.roof.BungeeZombieModel;
import com.hungteen.pvz.client.render.entity.zombie.PVZZombieRender;
import com.hungteen.pvz.common.entity.zombie.roof.BungeeZombieEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BungeeZombieRender extends PVZZombieRender<BungeeZombieEntity> {

	public BungeeZombieRender(EntityRendererProvider.Context context) {
		super(context, new BungeeZombieModel(context.bakeLayer(BungeeZombieModel.LAYER)), 0);
	}

	@Override
	public void render(BungeeZombieEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn,
			MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		this.renderLine(entityIn, partialTicks, matrixStackIn, bufferIn);
	}

	private void renderLine(BungeeZombieEntity entityLivingIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn) {
		matrixStackIn.pushPose();

		final Vec3 anchor = entityLivingIn.getAnchorPos();
		double d6 = anchor.x;
		double d7 = anchor.y;
		double d8 = anchor.z;

		double d9 = (double)(Mth.lerp(partialTicks, entityLivingIn.yBodyRotO, entityLivingIn.yBodyRot) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
		Vec3 vec31 = entityLivingIn.getLeashOffset();
		double d1 = Math.cos(d9) * vec31.z + Math.sin(d9) * vec31.x;
		double d2 = Math.sin(d9) * vec31.z - Math.cos(d9) * vec31.x;

		double d10 = Mth.lerp(partialTicks, entityLivingIn.xo, entityLivingIn.getX()) + d1;
		double d11 = Mth.lerp(partialTicks, entityLivingIn.yo, entityLivingIn.getY()) + vec31.y;
		double d12 = Mth.lerp(partialTicks, entityLivingIn.zo, entityLivingIn.getZ()) + d2;

		matrixStackIn.translate(d1, vec31.y, d2);

		float f = (float)(d6 - d10);
		float f1 = (float)(d7 - d11);
		float f2 = (float)(d8 - d12);

		VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.leash());
		Matrix4f matrix4f = matrixStackIn.last().pose();

		float f4 = Mth.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
		float f5 = f2 * f4;
		float f6 = f * f4;

		BlockPos blockpos = new BlockPos(entityLivingIn.getEyePosition(partialTicks));
		BlockPos anchorPos = new BlockPos(anchor);

		int i = this.getBlockLightLevel(entityLivingIn, blockpos);
		int j = entityLivingIn.level.getBrightness(LightLayer.BLOCK, anchorPos);
		int k = entityLivingIn.level.getBrightness(LightLayer.SKY, blockpos);
		int l = entityLivingIn.level.getBrightness(LightLayer.SKY, anchorPos);

		for(int i1 = 0; i1 <= 24; ++i1) {
			addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
		}

		for(int j1 = 24; j1 >= 0; --j1) {
			addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
		}
		matrixStackIn.popPose();
	}

	private static void addVertexPair(VertexConsumer buffer, Matrix4f pose, float x, float y, float z,
	                                  int blockLight1, int blockLight2, int skyLight1, int skyLight2,
	                                  float p_174317_, float p_174318_, float p_174319_, float p_174320_,
	                                  int index, boolean reverse) {
		float f = (float)index / 24.0F;
		int i = (int)Mth.lerp(f, (float)blockLight1, (float)blockLight2);
		int j = (int)Mth.lerp(f, (float)skyLight1, (float)skyLight2);
		int k = LightTexture.pack(i, j);

		float f1 = index % 2 == (reverse ? 1 : 0) ? 0.7F : 1.0F;
		float f2 = 0.5F * f1;
		float f3 = 0.4F * f1;
		float f4 = 0.3F * f1;

		float f5 = x * f;
		float f6 = y > 0.0F ? y * f * f : y - y * (1.0F - f) * (1.0F - f);
		float f7 = z * f;

		buffer.vertex(pose, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
		buffer.vertex(pose, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
	}
}