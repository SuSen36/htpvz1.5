package com.hungteen.pvz.client.model.entity.misc;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.common.entity.misc.DestroyCarEntity;
import com.hungteen.pvz.common.entity.npc.PennyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
public class DestroyCarModel extends EntityModel<DestroyCarEntity> {
	public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(PVZMod.MOD_ID, "destroy_car"), "main");

	private final ModelPart car;
	private final ModelPart head;
	private final ModelPart bone10;
	private final ModelPart bone;
	private final ModelPart light;
	private final ModelPart bone9;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bone6;
	private final ModelPart bone5;
	private final ModelPart body;
	private final ModelPart top;
	private final ModelPart tail;
	private final ModelPart tyres;
	private final ModelPart tyre_1;
	private final ModelPart bone14;
	private final ModelPart bone12;
	private final ModelPart bone13;
	private final ModelPart bone15;
	private final ModelPart bone16;
	private final ModelPart bone17;
	private final ModelPart tyre_2;
	private final ModelPart bone8;
	private final ModelPart bone11;
	private final ModelPart bone18;
	private final ModelPart bone19;
	private final ModelPart bone20;
	private final ModelPart bone21;
	private final ModelPart tyre_3;
	private final ModelPart bone22;
	private final ModelPart bone23;
	private final ModelPart bone24;
	private final ModelPart bone25;
	private final ModelPart bone26;
	private final ModelPart bone27;
	private final ModelPart tyre_4;
	private final ModelPart bone28;
	private final ModelPart bone29;
	private final ModelPart bone30;
	private final ModelPart bone31;
	private final ModelPart bone32;
	private final ModelPart bone33;

	public DestroyCarModel(ModelPart root) {
		this.car = root.getChild("car");
		this.head = this.car.getChild("head");
		this.bone10 = this.head.getChild("bone10");
		this.bone = this.bone10.getChild("bone");
		this.light = this.bone10.getChild("light");
		this.bone9 = this.head.getChild("bone9");
		this.bone2 = this.bone9.getChild("bone2");
		this.bone3 = this.bone9.getChild("bone3");
		this.bone4 = this.head.getChild("bone4");
		this.bone6 = this.bone4.getChild("bone6");
		this.bone5 = this.bone4.getChild("bone5");
		this.body = this.car.getChild("body");
		this.top = this.body.getChild("top");
		this.tail = this.car.getChild("tail");
		this.tyres = this.car.getChild("tyres");
		this.tyre_1 = this.tyres.getChild("tyre_1");
		this.bone14 = this.tyre_1.getChild("bone14");
		this.bone12 = this.bone14.getChild("bone12");
		this.bone13 = this.bone14.getChild("bone13");
		this.bone15 = this.tyre_1.getChild("bone15");
		this.bone16 = this.bone15.getChild("bone16");
		this.bone17 = this.bone15.getChild("bone17");
		this.tyre_2 = this.tyres.getChild("tyre_2");
		this.bone8 = this.tyre_2.getChild("bone8");
		this.bone11 = this.bone8.getChild("bone11");
		this.bone18 = this.bone8.getChild("bone18");
		this.bone19 = this.tyre_2.getChild("bone19");
		this.bone20 = this.bone19.getChild("bone20");
		this.bone21 = this.bone19.getChild("bone21");
		this.tyre_3 = this.tyres.getChild("tyre_3");
		this.bone22 = this.tyre_3.getChild("bone22");
		this.bone23 = this.bone22.getChild("bone23");
		this.bone24 = this.bone22.getChild("bone24");
		this.bone25 = this.tyre_3.getChild("bone25");
		this.bone26 = this.bone25.getChild("bone26");
		this.bone27 = this.bone25.getChild("bone27");
		this.tyre_4 = this.tyres.getChild("tyre_4");
		this.bone28 = this.tyre_4.getChild("bone28");
		this.bone29 = this.bone28.getChild("bone29");
		this.bone30 = this.bone28.getChild("bone30");
		this.bone31 = this.tyre_4.getChild("bone31");
		this.bone32 = this.bone31.getChild("bone32");
		this.bone33 = this.bone31.getChild("bone33");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition car = partdefinition.addOrReplaceChild("car", CubeListBuilder.create().texOffs(2, 2).addBox(-7.5F, -21.0F, -10.0F, 15.0F, 18.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = car.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 50).addBox(-16.5F, 1.25F, -17.0F, 15.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -7.0F, -6.0F));

		PartDefinition bone10 = head.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(45, 50).addBox(-16.5F, -27.0F, -14.0F, 15.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 5.0F));

		PartDefinition bone = bone10.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(61, 60).addBox(-15.0F, 4.6002F, -6.8003F, 14.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -32.0F, -16.0F, 0.9275F, 0.0F, 0.0F));

		PartDefinition light = bone10.addOrReplaceChild("light", CubeListBuilder.create().texOffs(34, 92).addBox(-11.0F, -27.5F, -1.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -12.0F));

		PartDefinition bone9 = head.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(35, 66).addBox(-13.5F, -10.0F, -8.0F, 9.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition bone2 = bone9.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(93, 27).addBox(-1.422F, 5.0F, 4.6991F, 1.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -15.0F, -12.0F, 0.0F, 0.6458F, 0.0F));

		PartDefinition bone3 = bone9.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(-1, 45).addBox(-2.7725F, 5.0F, 7.1063F, 1.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -15.0F, -12.0F, 0.0F, -0.6458F, 0.0F));

		PartDefinition bone4 = head.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 67).addBox(-14.5F, -4.0F, -15.0F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(62, 16).addBox(-5.5F, -4.0F, -15.0F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.5F, -1.75F, -17.0F, 7.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone6 = bone4.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(62, -1).addBox(-3.0F, -0.1906F, -9.4614F, 7.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.5F, -5.2098F, -8.1923F, 0.3944F, 0.0F, 0.0F));

		PartDefinition body = car.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(9.0F, -7.0F, -6.0F));

		PartDefinition top = body.addOrReplaceChild("top", CubeListBuilder.create().texOffs(62, 11).addBox(-15.5F, -32.0F, 50.0F, 13.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -29.0F));

		PartDefinition tail = car.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 12).addBox(-16.0F, -12.0F, 58.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -5.0F, -38.0F));

		PartDefinition tyres = car.addOrReplaceChild("tyres", CubeListBuilder.create(), PartPose.offset(9.0F, 0.0F, 11.0F));

		PartDefinition tyre_1 = tyres.addOrReplaceChild("tyre_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -29.0F));

		PartDefinition bone14 = tyre_1.addOrReplaceChild("bone14", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -68.0F));

		PartDefinition bone12 = bone14.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(24, 90).addBox(-2.5F, 6.0F, 68.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(10, 90).addBox(-2.5F, 2.7574F, 68.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone13 = bone14.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(50, 89).addBox(-2.5F, -71.8787F, 14.8787F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(60, 89).addBox(-2.5F, -75.1213F, 14.8787F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bone15 = tyre_1.addOrReplaceChild("bone15", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -12.0F, -70.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone16 = bone15.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(0, 89).addBox(-2.5F, -45.0122F, 54.9828F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(88, 67).addBox(-2.5F, -48.2548F, 54.9828F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone17 = bone15.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(83, 88).addBox(-2.5F, -58.8614F, -36.1335F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(86, 0).addBox(-2.5F, -62.1041F, -36.1335F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition tyre_2 = tyres.addOrReplaceChild("tyre_2", CubeListBuilder.create(), PartPose.offset(-15.0F, 0.0F, -29.0F));

		PartDefinition bone8 = tyre_2.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -68.0F));

		PartDefinition bone11 = bone8.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(17, 86).addBox(-2.5F, 6.0F, 68.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(85, 50).addBox(-2.5F, 2.7574F, 68.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone18 = bone8.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(73, 85).addBox(-2.5F, -71.8787F, 14.8787F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(43, 85).addBox(-2.5F, -75.1213F, 14.8787F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bone19 = tyre_2.addOrReplaceChild("bone19", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -12.0F, -70.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone20 = bone19.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(33, 85).addBox(-2.5F, -45.0122F, 54.9828F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(84, 20).addBox(-2.5F, -48.2548F, 54.9828F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone21 = bone19.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(63, 82).addBox(-2.5F, -58.8614F, -36.1335F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(53, 82).addBox(-2.5F, -62.1041F, -36.1335F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition tyre_3 = tyres.addOrReplaceChild("tyre_3", CubeListBuilder.create(), PartPose.offset(-15.0F, 0.0F, -5.0F));

		PartDefinition bone22 = tyre_3.addOrReplaceChild("bone22", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -68.0F));

		PartDefinition bone23 = bone22.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(10, 82).addBox(-2.5F, 6.0F, 68.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 82).addBox(-2.5F, 2.7574F, 68.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone24 = bone22.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(81, 74).addBox(-2.5F, -71.8787F, 14.8787F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(81, 81).addBox(-2.5F, -75.1213F, 14.8787F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bone25 = tyre_3.addOrReplaceChild("bone25", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -12.0F, -70.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone26 = bone25.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(78, 67).addBox(-2.5F, -45.0122F, 54.9828F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(71, 78).addBox(-2.5F, -48.2548F, 54.9828F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone27 = bone25.addOrReplaceChild("bone27", CubeListBuilder.create().texOffs(46, 78).addBox(-2.5F, -58.8614F, -36.1335F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(36, 78).addBox(-2.5F, -62.1041F, -36.1335F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition tyre_4 = tyres.addOrReplaceChild("tyre_4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -5.0F));

		PartDefinition bone28 = tyre_4.addOrReplaceChild("bone28", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -68.0F));

		PartDefinition bone29 = bone28.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(75, 16).addBox(-2.5F, 6.0F, 68.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(61, 75).addBox(-2.5F, 2.7574F, 68.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone30 = bone28.addOrReplaceChild("bone30", CubeListBuilder.create().texOffs(71, 71).addBox(-2.5F, -71.8787F, 14.8787F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(64, 67).addBox(-2.5F, -75.1213F, 14.8787F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bone31 = tyre_4.addOrReplaceChild("bone31", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -12.0F, -70.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone32 = bone31.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(62, 0).addBox(-2.5F, -45.0122F, 54.9828F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(20, 25).addBox(-2.5F, -48.2548F, 54.9828F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone33 = bone31.addOrReplaceChild("bone33", CubeListBuilder.create().texOffs(10, 25).addBox(-2.5F, -58.8614F, -36.1335F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 25).addBox(-2.5F, -62.1041F, -36.1335F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void setupAnim(DestroyCarEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		car.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}