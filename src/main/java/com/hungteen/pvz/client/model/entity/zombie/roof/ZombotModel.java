package com.hungteen.pvz.client.model.entity.zombie.roof;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.interfaces.IBodyEntity;
import com.hungteen.pvz.api.paz.IZombieModel;
import com.hungteen.pvz.common.entity.zombie.base.AbstractEdgarZombotEntity;
import com.hungteen.pvz.utils.AnimationUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
public class ZombotModel<T extends AbstractEdgarZombotEntity> extends EntityModel<T> implements IZombieModel<T>{
	public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(PVZMod.MOD_ID, "zombot"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart left_shoo;
	private final ModelPart right_leg;
	private final ModelPart right_shoo;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart pipe1;
	private final ModelPart pipe2;
	private final ModelPart head;
	private final ModelPart mouse;
	private final ModelPart lace;
	private final ModelPart red_eyes;
	private final ModelPart yellow_eyes;
	private final ModelPart blue_eyes;
	private final ModelPart left_arm;
	private final ModelPart right_arm;

	public ZombotModel(ModelPart root) {
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.left_shoo = this.left_leg.getChild("left_shoo");
		this.right_leg = this.total.getChild("right_leg");
		this.right_shoo = this.right_leg.getChild("right_shoo");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.pipe1 = this.body.getChild("pipe1");
		this.pipe2 = this.body.getChild("pipe2");
		this.head = this.up.getChild("head");
		this.mouse = this.head.getChild("mouse");
		this.lace = this.head.getChild("lace");
		this.red_eyes = this.head.getChild("red_eyes");
		this.yellow_eyes = this.head.getChild("yellow_eyes");
		this.blue_eyes = this.head.getChild("blue_eyes");
		this.left_arm = this.up.getChild("left_arm");
		this.right_arm = this.up.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
	
		PartDefinition total_pd = partdefinition.addOrReplaceChild("total",
			CubeListBuilder.create(),
			PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition left_leg_pd = total_pd.addOrReplaceChild("left_leg",
			CubeListBuilder.create()
				.texOffs(155, 211).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 33.0F, 10.0F)
				.texOffs(202, 220).addBox(-5.0F, 12.0F, -4.0F, 10.0F, 4.0F, 1.0F),
			PartPose.offset(6.0F, -41.0F, 0.0F));
		PartDefinition left_shoo_pd = left_leg_pd.addOrReplaceChild("left_shoo",
			CubeListBuilder.create()
				.texOffs(198, 230).addBox(0.0F, -8.0F, -8.0F, 12.0F, 8.0F, 16.0F),
			PartPose.offset(-6.0F, 41.0F, 0.0F));
		PartDefinition right_leg_pd = total_pd.addOrReplaceChild("right_leg",
			CubeListBuilder.create()
				.texOffs(154, 164).addBox(-4.0F, 0.0F, -3.0F, 10.0F, 33.0F, 10.0F)
				.texOffs(230, 220).addBox(-4.0F, 12.0F, -4.0F, 10.0F, 4.0F, 1.0F),
			PartPose.offset(-7.0F, -41.0F, 0.0F));
		PartDefinition right_shoo_pd = right_leg_pd.addOrReplaceChild("right_shoo",
			CubeListBuilder.create()
				.texOffs(198, 187).addBox(-12.0F, -8.0F, -8.0F, 12.0F, 8.0F, 16.0F),
			PartPose.offset(7.0F, 41.0F, 0.0F));
		PartDefinition up_pd = total_pd.addOrReplaceChild("up",
			CubeListBuilder.create(),
			PartPose.offset(0.0F, -41.0F, 0.0F));
		PartDefinition body_pd = up_pd.addOrReplaceChild("body",
			CubeListBuilder.create()
				.texOffs(186, 2).addBox(-11.0F, -40.0F, -4.0F, 22.0F, 40.0F, 12.0F),
			PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition pipe1_pd = body_pd.addOrReplaceChild("pipe1",
			CubeListBuilder.create()
				.texOffs(200, 170).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 10.0F)
				.texOffs(245, 166).addBox(-2.0F, -2.0F, 10.0F, 2.0F, 16.0F, 2.0F),
			PartPose.offsetAndRotation(9.0F, -39.0F, 6.0F, -0.6109F, 0.0F, 0.0F));
		PartDefinition pipe2_pd = body_pd.addOrReplaceChild("pipe2",
			CubeListBuilder.create()
				.texOffs(226, 150).addBox(-18.0F, -2.0F, 0.0F, 2.0F, 2.0F, 10.0F)
				.texOffs(210, 146).addBox(-18.0F, -2.0F, 10.0F, 2.0F, 16.0F, 2.0F),
			PartPose.offsetAndRotation(9.0F, -39.0F, 6.0F, -0.6109F, 0.0F, 0.0F));
		PartDefinition head_pd = up_pd.addOrReplaceChild("head",
			CubeListBuilder.create()
				.texOffs(188, 58).addBox(-8.0F, -9.0F, -16.0F, 16.0F, 13.0F, 16.0F)
				.texOffs(195, 118).addBox(2.0F, -13.0F, -13.0F, 2.0F, 4.0F, 2.0F)
				.texOffs(195, 134).addBox(-4.0F, -13.0F, -13.0F, 2.0F, 4.0F, 2.0F)
				.texOffs(172, 97).addBox(8.0F, -4.0F, -9.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(171, 77).addBox(9.0F, -5.0F, -10.0F, 2.0F, 3.0F, 3.0F)
				.texOffs(176, 103).addBox(-9.0F, -4.0F, -9.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(154, 98).addBox(-11.0F, -5.0F, -10.0F, 2.0F, 3.0F, 3.0F),
			PartPose.offset(0.0F, -40.0F, -3.0F));
		PartDefinition mouse_pd = head_pd.addOrReplaceChild("mouse",
			CubeListBuilder.create()
				.texOffs(186, 92).addBox(-8.0F, 0.0F, -15.0F, 16.0F, 3.0F, 16.0F)
				.texOffs(134, 6).addBox(-8.0F, -2.0F, -3.0F, 16.0F, 2.0F, 4.0F)
				.texOffs(0, 0).addBox(-1.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(1.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(4.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-2.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-5.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-7.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-8.0F, -2.0F, -13.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-8.0F, -2.0F, -9.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-8.0F, -2.0F, -6.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(7.0F, -2.0F, -7.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(7.0F, -2.0F, -12.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-1.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(2.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(6.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-5.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-8.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-8.0F, -1.0F, -11.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(-8.0F, -1.0F, -8.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(7.0F, -1.0F, -12.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(7.0F, -1.0F, -8.0F, 1.0F, 1.0F, 1.0F)
				.texOffs(0, 0).addBox(7.0F, -1.0F, -5.0F, 1.0F, 1.0F, 1.0F),
			PartPose.offset(0.0F, 6.0F, -1.0F));
		PartDefinition lace_pd = head_pd.addOrReplaceChild("lace",
			CubeListBuilder.create()
				.texOffs(151, 151).addBox(-11.0F, -3.0F, 0.0F, 22.0F, 3.0F, 3.0F)
				.texOffs(155, 126).addBox(-11.0F, -3.0F, -11.0F, 3.0F, 3.0F, 11.0F)
				.texOffs(152, 108).addBox(8.0F, -3.0F, -11.0F, 3.0F, 3.0F, 11.0F)
				.texOffs(102, 247).addBox(-11.0F, -3.0F, -14.0F, 22.0F, 3.0F, 3.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 1.1345F, 0.0F, 0.0F));
		PartDefinition red_eyes_pd = head_pd.addOrReplaceChild("red_eyes",
			CubeListBuilder.create()
				.texOffs(86, 249).addBox(-5.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F)
				.texOffs(89, 241).addBox(4.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F),
			PartPose.offset(-2.0F, -1.0F, -17.0F));
		PartDefinition yellow_eyes_pd = head_pd.addOrReplaceChild("yellow_eyes",
			CubeListBuilder.create()
				.texOffs(92, 231).addBox(-5.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F)
				.texOffs(94, 222).addBox(4.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F),
			PartPose.offset(-2.0F, -1.0F, -17.0F));
		PartDefinition blue_eyes_pd = head_pd.addOrReplaceChild("blue_eyes",
			CubeListBuilder.create()
				.texOffs(92, 202).addBox(-5.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F)
				.texOffs(91, 212).addBox(4.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F),
			PartPose.offset(-2.0F, -1.0F, -17.0F));
		PartDefinition left_arm_pd = up_pd.addOrReplaceChild("left_arm",
			CubeListBuilder.create()
				.texOffs(108, 180).addBox(-4.0F, -5.0F, -5.0F, 10.0F, 48.0F, 10.0F),
			PartPose.offset(15.0F, -35.0F, 2.0F));
		PartDefinition right_arm_pd = up_pd.addOrReplaceChild("right_arm",
			CubeListBuilder.create()
				.texOffs(106, 112).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 48.0F, 10.0F),
			PartPose.offset(-16.0F, -35.0F, 2.0F));
		return LayerDefinition.create(meshdefinition, 256, 256);
	}


	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.red_eyes.visible = (entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.FLAME);
		this.blue_eyes.visible = (entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.ICE);
		this.yellow_eyes.visible = (! this.red_eyes.visible && ! this.blue_eyes.visible);
		if(entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.FLAME || entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.ICE) {
			this.mouse.xRot = AnimationUtil.getUp(entity.getAttackTime(), entity.getAnimShootCD(), 30);
			this.right_arm.xRot = 0;
			this.left_arm.xRot = 0;
		} else if(entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.STEAL) {
			this.left_arm.xRot = AnimationUtil.getUpDown(entity.getAttackTime(), entity.getAnimStealCD(), - 120);
			this.right_arm.xRot = AnimationUtil.getUpDown(entity.getAttackTime(), entity.getAnimStealCD(), - 120);
			this.mouse.xRot = 0;
		} else {
			this.left_arm.xRot = 0;
			this.right_arm.xRot = 0;
			this.mouse.xRot = 0;
		}
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		total.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch) {
		
	}

	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, VertexConsumer buffer, int packedLight,
			int packedOverlay) {
		this.setAllInvis();
		switch (entity.getBodyType()) {
		case LEFT_LEG:{
			this.left_leg.visible = true;
			this.left_leg.setPos(0, 24, 0);
			this.left_leg.render(stack, buffer, packedLight, packedOverlay);
			break;
		}
		case RIGHT_LEG:{
			this.right_leg.visible = true;
			this.right_leg.setPos(0, 24, 0);
			this.right_leg.render(stack, buffer, packedLight, packedOverlay);
			break;
		}
		case LEFT_HAND:{
			this.left_arm.visible = true;
			this.left_arm.setPos(0, 24, 0);
			this.left_arm.render(stack, buffer, packedLight, packedOverlay);
			break;
		}
		case RIGHT_HAND:{
			this.right_arm.visible = true;
			this.right_arm.setPos(0, 24, 0);
			this.right_leg.render(stack, buffer, packedLight, packedOverlay);
		}
		case BODY:{
			this.body.visible = true;
			this.body.setPos(0, 24, 0);
			this.body.render(stack, buffer, packedLight, packedOverlay);
			break;
		}
		case HEAD:{
			this.head.visible = true;
			this.head.setPos(0, 24, 0);
			this.head.render(stack, buffer, packedLight, packedOverlay);
		}
		default:
			break;
		}
	}
	
	public void setAllInvis() {
		this.left_leg.visible = false;
		this.right_leg.visible = false;
		this.left_arm.visible = false;
		this.right_arm.visible = false;
		this.head.visible = false;
		this.body.visible = false;
	}

	@Override
	public EntityModel<T> getZombieModel() {
		return this;
	}
}