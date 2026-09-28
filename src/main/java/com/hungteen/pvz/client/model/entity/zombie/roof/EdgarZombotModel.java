package com.hungteen.pvz.client.model.entity.zombie.roof;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.interfaces.IBodyEntity;
import com.hungteen.pvz.api.paz.IZombieModel;
import com.hungteen.pvz.common.entity.zombie.base.AbstractEdgarZombotEntity;
import com.hungteen.pvz.common.entity.zombie.roof.EdgarZombotEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
public class EdgarZombotModel<T extends EdgarZombotEntity> extends EntityModel<T> implements IZombieModel<T>{
	public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(PVZMod.MOD_ID, "edgar_zombot"), "main");

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
	private final ModelPart open_light;
	private final ModelPart cloes_light;
	private final ModelPart mouse;
	private final ModelPart lace;
	private final ModelPart yellow_eyes;
	private final ModelPart blue_eyes;
	private final ModelPart red_eyes;
	private final ModelPart cockpit;
	private final ModelPart left_arm;
	private final ModelPart car;
	private final ModelPart head2;
	private final ModelPart bone10;
	private final ModelPart bone;
	private final ModelPart light;
	private final ModelPart bone9;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bone6;
	private final ModelPart bone5;
	private final ModelPart body2;
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
	private final ModelPart right_arm;

	public EdgarZombotModel(ModelPart root) {
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
		this.open_light = this.head.getChild("open_light");
		this.cloes_light = this.head.getChild("cloes_light");
		this.mouse = this.head.getChild("mouse");
		this.lace = this.head.getChild("lace");
		this.yellow_eyes = this.head.getChild("yellow_eyes");
		this.blue_eyes = this.head.getChild("blue_eyes");
		this.red_eyes = this.head.getChild("red_eyes");
		this.cockpit = this.head.getChild("cockpit");
		this.left_arm = this.up.getChild("left_arm");
		this.car = this.left_arm.getChild("car");
		this.head2 = this.car.getChild("head2");
		this.bone10 = this.head2.getChild("bone10");
		this.bone = this.bone10.getChild("bone");
		this.light = this.bone10.getChild("light");
		this.bone9 = this.head2.getChild("bone9");
		this.bone2 = this.bone9.getChild("bone2");
		this.bone3 = this.bone9.getChild("bone3");
		this.bone4 = this.head2.getChild("bone4");
		this.bone6 = this.bone4.getChild("bone6");
		this.bone5 = this.bone4.getChild("bone5");
		this.body2 = this.car.getChild("body2");
		this.top = this.body2.getChild("top");
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
		this.right_arm = this.up.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left_leg = total.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(309, 421).addBox(-16.0F, 21.0F, -6.0F, 20.0F, 45.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(404, 440).addBox(-16.0F, 40.0F, -8.0F, 20.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(18.0F, -82.0F, 0.0F));

		PartDefinition left_shoo = left_leg.addOrReplaceChild("left_shoo", CubeListBuilder.create().texOffs(396, 460).addBox(0.0F, -16.0F, -16.0F, 24.0F, 16.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(-18.0F, 82.0F, 0.0F));

		PartDefinition right_leg = total.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(308, 328).addBox(-2.0F, 21.0F, -6.0F, 20.0F, 45.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(460, 440).addBox(-2.0F, 39.0F, -8.0F, 20.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-20.0F, -82.0F, 0.0F));

		PartDefinition right_shoo = right_leg.addOrReplaceChild("right_shoo", CubeListBuilder.create().texOffs(396, 373).addBox(-24.0F, -16.0F, -16.0F, 24.0F, 16.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(20.0F, 82.0F, 0.0F));

		PartDefinition up = total.addOrReplaceChild("up", CubeListBuilder.create(), PartPose.offset(0.0F, -69.0F, -1.0F));

		PartDefinition body = up.addOrReplaceChild("body", CubeListBuilder.create().texOffs(372, 4).addBox(-22.0F, -86.0F, -8.0F, 44.0F, 79.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(171, 170).addBox(-20.0F, -7.0F, -8.0F, 40.0F, 14.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition pipe1 = body.addOrReplaceChild("pipe1", CubeListBuilder.create().texOffs(400, 340).addBox(-4.0F, -2.8029F, -16.577F, 4.0F, 4.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(489, 332).addBox(-4.0F, -2.8029F, 3.423F, 4.0F, 32.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(489, 332).addBox(-4.0F, -2.8029F, -20.577F, 4.0F, 32.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, -84.0F, 28.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition pipe2 = body.addOrReplaceChild("pipe2", CubeListBuilder.create().texOffs(452, 300).addBox(-36.0F, -2.8029F, -16.577F, 4.0F, 4.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(420, 292).addBox(-36.0F, -2.8029F, 3.423F, 4.0F, 32.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(420, 292).addBox(-36.0F, -2.8029F, -20.577F, 4.0F, 32.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, -84.0F, 27.2F, -0.6109F, 0.0F, 0.0F));

		PartDefinition head = up.addOrReplaceChild("head", CubeListBuilder.create().texOffs(376, 124).addBox(-16.0F, -10.0F, -32.0F, 32.0F, 18.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(342, 193).addBox(16.0F, -8.0F, -18.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(350, 205).addBox(-20.0F, -8.0F, -18.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -80.0F, -6.0F));

		PartDefinition open_light = head.addOrReplaceChild("open_light", CubeListBuilder.create().texOffs(307, 196).addBox(19.8F, -10.0F, -20.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(307, 196).addBox(-25.0F, -10.0F, -20.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cloes_light = head.addOrReplaceChild("cloes_light", CubeListBuilder.create().texOffs(340, 176).addBox(19.8F, -10.0F, -20.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(307, 176).addBox(-25.0F, -10.0F, -20.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition mouse = head.addOrReplaceChild("mouse", CubeListBuilder.create().texOffs(372, 184).addBox(-16.0F, 0.0F, -30.0F, 32.0F, 6.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(268, 12).addBox(-16.0F, -4.0F, -6.0F, 32.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(2.0F, -4.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(8.0F, -4.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.0F, -4.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.0F, -4.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-14.0F, -4.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, -4.0F, -26.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, -4.0F, -18.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, -4.0F, -12.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(14.0F, -4.0F, -14.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(14.0F, -4.0F, -24.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.0F, -2.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(4.0F, -2.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(12.0F, -2.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-10.0F, -2.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, -2.0F, -30.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, -2.0F, -22.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, -2.0F, -16.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(14.0F, -2.0F, -24.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(14.0F, -2.0F, -16.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(14.0F, -2.0F, -10.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, -2.0F));

		PartDefinition lace = head.addOrReplaceChild("lace", CubeListBuilder.create().texOffs(301, 301).addBox(-22.0F, -3.0F, 11.0F, 44.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(309, 252).addBox(-22.0F, -3.0F, -11.0F, 6.0F, 6.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(304, 216).addBox(16.0F, -3.0F, -11.0F, 6.0F, 6.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(204, 493).addBox(-22.0F, -3.0F, -17.0F, 44.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.8487F, -1.7448F, 1.309F, 0.0F, 0.0F));

		PartDefinition yellow_eyes = head.addOrReplaceChild("yellow_eyes", CubeListBuilder.create().texOffs(184, 461).addBox(-10.0F, -10.0F, 1.8F, 10.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(188, 444).addBox(8.0F, -10.0F, 1.8F, 10.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -2.0F, -34.0F));

		PartDefinition blue_eyes = head.addOrReplaceChild("blue_eyes", CubeListBuilder.create().texOffs(184, 404).addBox(-10.0F, -10.0F, 1.8F, 10.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(181, 424).addBox(8.0F, -10.0F, 1.8F, 10.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -2.0F, -34.0F));

		PartDefinition red_eyes = head.addOrReplaceChild("red_eyes", CubeListBuilder.create().texOffs(172, 497).addBox(-10.0F, -10.0F, 1.8F, 10.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(177, 481).addBox(8.0F, -10.0F, 1.8F, 10.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -2.0F, -34.0F));

		PartDefinition cockpit = head.addOrReplaceChild("cockpit", CubeListBuilder.create().texOffs(0, 472).addBox(9.0F, 0.0F, -18.0F, 7.0F, 8.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(76, 472).addBox(-16.0F, 0.0F, -18.0F, 6.0F, 8.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(0, 447).addBox(-10.0F, 0.0F, -18.0F, 19.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(53, 447).addBox(-10.0F, 0.0F, 7.0F, 19.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(2, 1).addBox(0.9F, -2.0F, -13.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 1).addBox(-7.6F, -2.0F, -13.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 1).addBox(-4.1F, -2.0F, -13.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-0.5F, -2.0F, 7.9F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(415, 255).addBox(-1.0F, -3.6F, 7.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.5F, -2.0F, 7.9F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(415, 270).addBox(3.0F, -3.6F, 7.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(7.5F, -2.0F, 7.9F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(395, 270).addBox(7.0F, -3.6F, 7.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(395, 270).addBox(0.4F, -3.6F, -14.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(395, 255).addBox(-8.1F, -3.6F, -14.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(395, 270).addBox(-4.6F, -3.6F, -14.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, -1).addBox(6.8F, -1.0F, -16.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.6F, -1.0F, -16.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(0.4F, -1.0F, -16.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.8F, -1.0F, -16.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-6.0F, -1.0F, -16.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(9.8F, -1.0F, -9.6F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(9.8F, -1.0F, -2.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.6F, -1.0F, 9.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.4F, -1.0F, -9.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.4F, -1.0F, -5.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.4F, -1.0F, -1.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.4F, -1.0F, 2.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(9.8F, -1.0F, 4.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-8.4F, -1.0F, 9.2F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, -14.0F));

		PartDefinition left_arm = up.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(216, 360).addBox(-8.0F, -8.0F, -10.0F, 20.0F, 88.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(30.0F, -70.0F, 4.0F));

		PartDefinition car = left_arm.addOrReplaceChild("car", CubeListBuilder.create().texOffs(2, 2).addBox(-7.5F, -21.0F, -10.0F, 15.0F, 18.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 85.0F, -3.0F));

		PartDefinition head2 = car.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 50).addBox(-16.5F, 1.25F, -17.0F, 15.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -7.0F, -6.0F));

		PartDefinition bone10 = head2.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(45, 50).addBox(-16.5F, -27.0F, -14.0F, 15.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 5.0F));

		PartDefinition bone = bone10.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(61, 60).addBox(-15.0F, 4.6002F, -6.8003F, 14.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -32.0F, -16.0F, 0.9275F, 0.0F, 0.0F));

		PartDefinition light = bone10.addOrReplaceChild("light", CubeListBuilder.create().texOffs(34, 92).addBox(-11.0F, -27.5F, -1.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -12.0F));

		PartDefinition bone9 = head2.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(35, 66).addBox(-13.5F, -10.0F, -8.0F, 9.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition bone2 = bone9.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(93, 27).addBox(-1.422F, 5.0F, 4.6991F, 1.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -15.0F, -12.0F, 0.0F, 0.6458F, 0.0F));

		PartDefinition bone3 = bone9.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(-1, 45).addBox(-2.7725F, 5.0F, 7.1063F, 1.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -15.0F, -12.0F, 0.0F, -0.6458F, 0.0F));

		PartDefinition bone4 = head2.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 67).addBox(-14.5F, -4.0F, -15.0F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(62, 16).addBox(-5.5F, -4.0F, -15.0F, 2.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-12.5F, -1.75F, -17.0F, 7.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone6 = bone4.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(62, 0).addBox(-3.0F, -0.1906F, -9.4614F, 7.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.5F, -5.2098F, -8.1923F, 0.3944F, 0.0F, 0.0F));

		PartDefinition body2 = car.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(9.0F, -7.0F, -6.0F));

		PartDefinition top = body2.addOrReplaceChild("top", CubeListBuilder.create().texOffs(62, 11).addBox(-15.5F, -32.0F, 50.0F, 13.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -29.0F));

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

		PartDefinition right_arm = up.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(212, 224).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 88.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(-32.0F, -70.0F, 4.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}


	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.red_eyes.visible = (entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.FLAME);
		this.blue_eyes.visible = (entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.ICE);
		this.yellow_eyes.visible = (! this.red_eyes.visible && ! this.blue_eyes.visible);
		this.car.visible = (entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.CAR && entity.getAttackTime() < entity.getThrowReleaseTick());
		final int lightT = 10;
		final boolean lightFlag = (entity.getExistTick() % (lightT << 1)) < lightT;
		this.open_light.visible = lightFlag;
		this.cloes_light.visible = ! lightFlag;
		this.up.xRot = 0;
		this.head.xRot = 0;
		if(entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.FLAME || entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.ICE) {
			this.up.xRot = entity.getShootBodyRot();
			this.head.xRot = - entity.getShootBodyRot();
			this.mouse.xRot = entity.getShootMouthRot();
			this.right_arm.xRot = 0;
			this.left_arm.xRot = 0;
		} else if(entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.CAR) {
			this.left_arm.xRot = entity.getThrowArmRot();
			this.right_arm.xRot = 0;
			this.mouse.xRot = 0;
		} else if(entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.SPAWN) {
			this.up.xRot = entity.getSpawnBodyRot();
			this.left_arm.xRot = entity.getSpawnArmRot();
			this.right_arm.xRot = 0;
			this.mouse.xRot = 0;
		} else if(entity.getRobotState() == AbstractEdgarZombotEntity.EdgarStates.STEAL) {
			this.left_arm.xRot = entity.getStealArmRot();
			this.right_arm.xRot = 0;
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
		this.car.visible = false;
	}

	@Override
	public EntityModel<T> getZombieModel() {
		return this;
	}
}