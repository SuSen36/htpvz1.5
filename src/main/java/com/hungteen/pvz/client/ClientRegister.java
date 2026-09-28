package com.hungteen.pvz.client;

import com.hungteen.pvz.client.model.baked.ImitaterCardBakedModel;
import com.hungteen.pvz.client.model.baked.PVZBakedModel;
import com.hungteen.pvz.client.model.entity.blockeneity.OriginModel;
import com.hungteen.pvz.client.model.entity.bullet.*;
import com.hungteen.pvz.client.model.entity.creature.FoodieZombieModel;
import com.hungteen.pvz.client.model.entity.creature.MooBloomModel;
import com.hungteen.pvz.client.model.entity.misc.*;
import com.hungteen.pvz.client.model.entity.npc.CrazyDaveModel;
import com.hungteen.pvz.client.model.entity.npc.PennyModel;
import com.hungteen.pvz.client.model.entity.plant.appease.*;
import com.hungteen.pvz.client.model.entity.plant.arma.CabbagePultModel;
import com.hungteen.pvz.client.model.entity.plant.arma.KernelPultModel;
import com.hungteen.pvz.client.model.entity.plant.arma.MelonPultModel;
import com.hungteen.pvz.client.model.entity.plant.assist.*;
import com.hungteen.pvz.client.model.entity.plant.defence.GarlicModel;
import com.hungteen.pvz.client.model.entity.plant.defence.PumpkinModel;
import com.hungteen.pvz.client.model.entity.plant.defence.TallNutModel;
import com.hungteen.pvz.client.model.entity.plant.defence.WallNutModel;
import com.hungteen.pvz.client.model.entity.plant.enforce.*;
import com.hungteen.pvz.client.model.entity.plant.explosion.*;
import com.hungteen.pvz.client.model.entity.plant.flame.JalapenoModel;
import com.hungteen.pvz.client.model.entity.plant.flame.TorchWoodModel;
import com.hungteen.pvz.client.model.entity.plant.ice.IceShroomModel;
import com.hungteen.pvz.client.model.entity.plant.ice.IcebergLettuceModel;
import com.hungteen.pvz.client.model.entity.plant.ice.SnowPeaModel;
import com.hungteen.pvz.client.model.entity.plant.ice.WinterMelonModel;
import com.hungteen.pvz.client.model.entity.plant.light.*;
import com.hungteen.pvz.client.model.entity.plant.magic.*;
import com.hungteen.pvz.client.model.entity.plant.spear.CactusModel;
import com.hungteen.pvz.client.model.entity.plant.spear.CatTailModel;
import com.hungteen.pvz.client.model.entity.plant.spear.SpikeRockModel;
import com.hungteen.pvz.client.model.entity.plant.spear.SpikeWeedModel;
import com.hungteen.pvz.client.model.entity.plant.toxic.*;
import com.hungteen.pvz.client.model.entity.zombie.grass.*;
import com.hungteen.pvz.client.model.entity.zombie.other.*;
import com.hungteen.pvz.client.model.entity.zombie.pool.*;
import com.hungteen.pvz.client.model.entity.zombie.roof.*;
import com.hungteen.pvz.client.model.entity.zombie.zombotany.*;
import com.hungteen.pvz.client.particle.*;
import com.hungteen.pvz.client.particle.bomb.CherryBombParticle;
import com.hungteen.pvz.client.particle.bomb.YellowBombParticle;
import com.hungteen.pvz.client.render.entity.bullet.*;
import com.hungteen.pvz.client.render.entity.creature.FoodieZombieRender;
import com.hungteen.pvz.client.render.entity.creature.MooBloomRender;
import com.hungteen.pvz.client.render.entity.effects.DoomFixerRender;
import com.hungteen.pvz.client.render.entity.effects.OriginEffectRender;
import com.hungteen.pvz.client.render.entity.misc.*;
import com.hungteen.pvz.client.render.entity.misc.bowling.ExplosionBowlingRender;
import com.hungteen.pvz.client.render.entity.misc.bowling.GiantNutBowlingRender;
import com.hungteen.pvz.client.render.entity.misc.bowling.WallNutBowlingRender;
import com.hungteen.pvz.client.render.entity.misc.drop.*;
import com.hungteen.pvz.client.render.entity.npc.CrazyDaveRender;
import com.hungteen.pvz.client.render.entity.npc.PennyRender;
import com.hungteen.pvz.client.render.entity.plant.appease.*;
import com.hungteen.pvz.client.render.entity.plant.arma.ButterPultRender;
import com.hungteen.pvz.client.render.entity.plant.arma.CabbagePultRender;
import com.hungteen.pvz.client.render.entity.plant.arma.KernelPultRender;
import com.hungteen.pvz.client.render.entity.plant.arma.MelonPultRender;
import com.hungteen.pvz.client.render.entity.plant.assist.*;
import com.hungteen.pvz.client.render.entity.plant.defence.GarlicRender;
import com.hungteen.pvz.client.render.entity.plant.defence.PumpkinRender;
import com.hungteen.pvz.client.render.entity.plant.defence.TallNutRender;
import com.hungteen.pvz.client.render.entity.plant.defence.WallNutRender;
import com.hungteen.pvz.client.render.entity.plant.enforce.*;
import com.hungteen.pvz.client.render.entity.plant.explosion.*;
import com.hungteen.pvz.client.render.entity.plant.flame.JalapenoRender;
import com.hungteen.pvz.client.render.entity.plant.flame.TorchWoodRender;
import com.hungteen.pvz.client.render.entity.plant.ice.IceShroomRender;
import com.hungteen.pvz.client.render.entity.plant.ice.IcebergLettuceRender;
import com.hungteen.pvz.client.render.entity.plant.ice.SnowPeaRender;
import com.hungteen.pvz.client.render.entity.plant.ice.WinterMelonRender;
import com.hungteen.pvz.client.render.entity.plant.light.*;
import com.hungteen.pvz.client.render.entity.plant.magic.*;
import com.hungteen.pvz.client.render.entity.plant.spear.CactusRender;
import com.hungteen.pvz.client.render.entity.plant.spear.CatTailRender;
import com.hungteen.pvz.client.render.entity.plant.spear.SpikeRockRender;
import com.hungteen.pvz.client.render.entity.plant.spear.SpikeWeedRender;
import com.hungteen.pvz.client.render.entity.plant.toxic.*;
import com.hungteen.pvz.client.render.entity.zombie.grass.*;
import com.hungteen.pvz.client.render.entity.zombie.other.*;
import com.hungteen.pvz.client.render.entity.zombie.pool.*;
import com.hungteen.pvz.client.render.entity.zombie.roof.*;
import com.hungteen.pvz.client.render.entity.zombie.zombotany.*;
import com.hungteen.pvz.client.render.tileentity.*;
import com.hungteen.pvz.common.block.BlockRegister;
import com.hungteen.pvz.common.blockentity.TileEntityRegister;
import com.hungteen.pvz.common.item.ItemRegister;
import com.hungteen.pvz.common.item.tool.mc.SeedCrossbowItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

import static com.hungteen.pvz.common.entity.EntityRegister.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegister {

	@SubscribeEvent
	public static void onModelBaked(ModelEvent.BakingCompleted ev) {
		Map<ResourceLocation, BakedModel> modelRegistry = ev.getModels();
//		{
//			Pair<BakedModel, ModelResourceLocation> now = getBakedModel(ev, ItemRegister.BOWLING_GLOVE.get());
//			BowlingGloveBakedModel tmp = new BowlingGloveBakedModel(now.getFirst());
//			modelRegistry.put(now.getSecond(), tmp);
//		}
		{
			Pair<BakedModel, ModelResourceLocation> now = getBakedModel(ev, ItemRegister.IMITATER_CARD.get());
			ImitaterCardBakedModel tmp = new ImitaterCardBakedModel(now.getFirst());
			modelRegistry.put(now.getSecond(), tmp);
		}
	}

	@SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.register(ParticleRegister.RED_BOMB.get(), CherryBombParticle.Provider::new);
        event.register(ParticleRegister.YELLOW_BOMB.get(), YellowBombParticle.Provider::new);
        event.register(ParticleRegister.DIRT_BURST_OUT.get(), DirtBurstOutParticle.Provider::new);
        event.register(ParticleRegister.SLEEP.get(), SleepParticle.Provider::new);
        event.register(ParticleRegister.SPORE.get(), SporeParticle.Provider::new);
        event.register(ParticleRegister.FUME.get(), FumeParticle.Provider::new);
        event.register(ParticleRegister.SNOW_FLOWER.get(), SnowFlowerParticle.Provider::new);
        event.register(ParticleRegister.DOOM.get(), DoomParticle.Provider::new);
        event.register(ParticleRegister.MELON_SLICE.get(), MelonSliceParticle.Provider::new);
        event.register(ParticleRegister.FROZEN_MELON_SLICE.get(), FrozenMelonSliceParticle.Provider::new);
        event.register(ParticleRegister.GREEN_SWEEP.get(), GreenSweepParticle.Provider::new);
        event.register(ParticleRegister.POP_CORN.get(), PopCornParticle.Provider::new);
        event.register(ParticleRegister.FOG.get(), FogParticle.Provider::new);
        event.register(ParticleRegister.MASHED_POTATO.get(), MashedPotatoParticle.Provider::new);
	}


	private static Pair<BakedModel, ModelResourceLocation> getBakedModel(ModelEvent.BakingCompleted ev, Item item) {
		Map<ResourceLocation, BakedModel> modelRegistry = ev.getModels();
		ModelResourceLocation location = new ModelResourceLocation(ForgeRegistries.ITEMS.getKey(item), "inventory");
		BakedModel model = modelRegistry.get(location);
		if(model == null) throw new RuntimeException("Did not find Obsidian Hidden in registry");
        else if(model instanceof PVZBakedModel) throw new RuntimeException("Tried to replaceObsidian Hidden twice");
		return Pair.of(model, location);
	}

	@SuppressWarnings("removal")
	@SubscribeEvent
	public static void reigsterRenderType(FMLClientSetupEvent ev){
		ev.enqueueWork(() -> SeedCrossbowItem.registerProperties(ItemRegister.SEED_CROSSBOW.get()));
		TileEntityRegister.bindRenderers(ev);
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.ORIGIN_BLOCK.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.PEA_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.NUT_LEAVES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.NUT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.TOXIC_SHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.PLANTERN.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.FLOWER_POT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.LILY_PAD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.CABBAGE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.BUTTER_BLOCK.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.CORN.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.PEPPER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.ESSENCE_ALTAR.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.STEEL_LADDER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.SILVER_SUNFLOWER_TROPHY.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.GOLD_SUNFLOWER_TROPHY.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockRegister.DIAMOND_SUNFLOWER_TROPHY.get(), RenderType.cutout());
		TileEntityRegister.bindRenderers(ev);
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers ev) {
		ev.registerEntityRenderer(SUN.get(), ctx -> new SunRender(ctx));
		ev.registerEntityRenderer(COIN.get(), ctx -> new CoinRender(ctx));
		ev.registerEntityRenderer(JEWEL.get(), ctx -> new JewelRender(ctx));
		ev.registerEntityRenderer(ENERGY.get(), ctx -> new EnergyRender(ctx));
		ev.registerEntityRenderer(GIFT_BOX.get(), ctx -> new GiftBoxRender(ctx));
		ev.registerEntityRenderer(FALLEN_STAR.get(), ctx -> new FallenStarRender(ctx));
		ev.registerEntityRenderer(SEED_PACKET.get(), ctx -> new SeedPacketRender(ctx));
		ev.registerEntityRenderer(PEA.get(), ctx -> new PeaRender(ctx));
		ev.registerEntityRenderer(POTATO.get(), ctx -> new PotatoRender(ctx));
		ev.registerEntityRenderer(SPORE.get(), ctx -> new SporeRender(ctx));
		ev.registerEntityRenderer(FUME.get(), ctx -> new FumeRender(ctx));
		ev.registerEntityRenderer(METAL.get(), ctx -> new MetalItemRender(ctx));
		ev.registerEntityRenderer(THORN.get(), ctx -> new ThornRender(ctx));
		ev.registerEntityRenderer(STAR.get(), ctx -> new StarRender(ctx));
		ev.registerEntityRenderer(NUT.get(), ctx -> new NutRender(ctx));
		ev.registerEntityRenderer(CABBAGE.get(), ctx -> new CabbageRender(ctx));
		ev.registerEntityRenderer(ICE_CABBAGE.get(), ctx -> new IceCabbageRender(ctx));
		ev.registerEntityRenderer(KERNEL.get(), ctx -> new KernelRender(ctx));
		ev.registerEntityRenderer(BUTTER.get(), ctx -> new ButterRender(ctx));
		ev.registerEntityRenderer(TARGET_ARROW.get(), ctx -> new TargetArrowRender(ctx));
		ev.registerEntityRenderer(MELON.get(), ctx -> new MelonRender(ctx));
		ev.registerEntityRenderer(FIRE_CRACKER.get(), ctx -> new FireCrackerRender(ctx));
		ev.registerEntityRenderer(BALL.get(), ctx -> new BallRender(ctx));
		ev.registerEntityRenderer(CORN.get(), ctx -> new CornRender(ctx));
		ev.registerEntityRenderer(FOG_IN_BOTTLE.get(), ctx -> new ThrownItemRenderer<>(ctx, 1, false));
		ev.registerEntityRenderer(ORIGIN_EFFECT.get(), ctx -> new OriginEffectRender(ctx));
		ev.registerEntityRenderer(SEED_ARROW.get(), ctx -> new SeedArrowRender(ctx));
		ev.registerEntityRenderer(ORIGIN_ARROW.get(), ctx -> new OriginArrowRender(ctx));
		ev.registerEntityRenderer(DOOM_FIXER.get(), ctx -> new DoomFixerRender(ctx));
		ev.registerEntityRenderer(SMALL_CHOMPER.get(), ctx -> new SmallChomperRender(ctx));
		ev.registerEntityRenderer(BOBSLE_CAR.get(), ctx -> new BobsleCarRender(ctx));
		ev.registerEntityRenderer(ZOMBIE_PART.get(), ctx -> new EmptyRender(ctx));
		ev.registerEntityRenderer(ZOMBIE_HAND.get(), ctx -> new ZombieHandRender(ctx));
		ev.registerEntityRenderer(WALL_NUT_BOWLING.get(), ctx -> new WallNutBowlingRender(ctx));
		ev.registerEntityRenderer(EXPLOSION_BOWLING.get(), ctx -> new ExplosionBowlingRender(ctx));
		ev.registerEntityRenderer(GIANT_NUT_BOWLING.get(), ctx -> new GiantNutBowlingRender(ctx));
		ev.registerEntityRenderer(LAWN_MOWER.get(), ctx -> new LawnMowerRender(ctx));
		ev.registerEntityRenderer(FIRE_CRACKERS.get(), ctx -> new FireCrackersRender(ctx));
		ev.registerEntityRenderer(ELEMENT_BALL.get(), ctx -> new ElementBallRender(ctx));
		ev.registerEntityRenderer(DESTROY_CAR.get(), ctx -> new DestroyCarRender(ctx));
		ev.registerEntityRenderer(GARDEN_RAKE.get(), ctx -> new GardenRakeRender(ctx));
		ev.registerEntityRenderer(FOODIE_ZOMBIE.get(), ctx -> new FoodieZombieRender(ctx));
		ev.registerEntityRenderer(MOOBLOOM.get(), ctx -> new MooBloomRender(ctx));
		ev.registerEntityRenderer(CRAZY_DAVE.get(), ctx -> new CrazyDaveRender(ctx));
		ev.registerEntityRenderer(PANNEY.get(), ctx -> new PennyRender(ctx));
		ev.registerEntityRenderer(NORMAL_ZOMBIE.get(), ctx -> new NormalZombieRender(ctx));
		ev.registerEntityRenderer(FLAG_ZOMBIE.get(), ctx -> new FlagZombieRender(ctx));
		ev.registerEntityRenderer(CONEHEAD_ZOMBIE.get(), ctx -> new ConeHeadZombieRender(ctx));
		ev.registerEntityRenderer(POLE_ZOMBIE.get(), ctx -> new PoleZombieRender(ctx));
		ev.registerEntityRenderer(BUCKETHEAD_ZOMBIE.get(), ctx -> new BucketHeadZombieRender(ctx));
		ev.registerEntityRenderer(SNORKEL_ZOMBIE.get(), ctx -> new SnorkelZombieRender(ctx));
		ev.registerEntityRenderer(ZOMBONI.get(), ctx -> new ZomboniRender(ctx));
		ev.registerEntityRenderer(BOBSLE_TEAM.get(), ctx -> new BobsleTeamRender(ctx));
		ev.registerEntityRenderer(BOBSLE_ZOMBIE.get(), ctx -> new BobsleZombieRender(ctx));
		ev.registerEntityRenderer(ZOMBIE_DOLPHIN.get(), ctx -> new ZombieDolphinRender(ctx));
		ev.registerEntityRenderer(DOLPHIN_RIDER.get(), ctx -> new DolphinRiderRender(ctx));
		ev.registerEntityRenderer(DOLPHIN_RIDER_ZOMBIE.get(), ctx -> new DolphinRiderZombieRender(ctx));
		ev.registerEntityRenderer(LAVA_ZOMBIE.get(), ctx -> new LavaZombieRender(ctx));
		ev.registerEntityRenderer(NEWSPAPER_ZOMBIE.get(), ctx -> new NewspaperZombieRender(ctx));
		ev.registerEntityRenderer(TOMB_STONE.get(), ctx -> new TombStoneRender(ctx));
		ev.registerEntityRenderer(SCREENDOOR_ZOMBIE.get(), ctx -> new ScreenDoorZombieRender(ctx));
		ev.registerEntityRenderer(FOOTBALL_ZOMBIE.get(), ctx -> new FootballZombieRender(ctx));
		ev.registerEntityRenderer(GIGA_FOOTBALL_ZOMBIE.get(), ctx -> new GigaFootballZombieRender(ctx));
		ev.registerEntityRenderer(DANCING_ZOMBIE.get(), ctx -> new DancingZombieRender(ctx));
		ev.registerEntityRenderer(BACKUP_DANCER.get(), ctx -> new BackupDancerRender(ctx));
		ev.registerEntityRenderer(PUMPKIN_ZOMBIE.get(), ctx -> new PumpkinZombieRender(ctx));
		ev.registerEntityRenderer(TRICK_ZOMBIE.get(), ctx -> new TrickZombieRender(ctx));
		ev.registerEntityRenderer(COFFIN.get(), ctx -> new CoffinRender(ctx));
		ev.registerEntityRenderer(MOURNER_ZOMBIE.get(), ctx -> new MournerZombieRender(ctx));
		ev.registerEntityRenderer(NOBLE_ZOMBIE.get(), ctx -> new NobleZombieRender(ctx));
		ev.registerEntityRenderer(SUNDAY_EDITION_ZOMBIE.get(), ctx -> new SundayEditionZombieRender(ctx));
		ev.registerEntityRenderer(JACK_IN_BOX_ZOMBIE.get(), ctx -> new JackInBoxZombieRender(ctx));
		ev.registerEntityRenderer(POGO_ZOMBIE.get(), ctx -> new PogoZombieRender(ctx));
		ev.registerEntityRenderer(YETI_ZOMBIE.get(), ctx -> new YetiZombieRender(ctx));
		ev.registerEntityRenderer(DIGGER_ZOMBIE.get(), ctx -> new DiggerZombieRender(ctx));
		ev.registerEntityRenderer(BALLOON_ZOMBIE.get(), ctx -> new BalloonZombieRender(ctx));
		ev.registerEntityRenderer(RA_ZOMBIE.get(), ctx -> new RaZombieRender(ctx));
		ev.registerEntityRenderer(BUNGEE_ZOMBIE.get(), ctx -> new BungeeZombieRender(ctx));
		ev.registerEntityRenderer(LADDER_ZOMBIE.get(), ctx -> new LadderZombieRender(ctx));
		ev.registerEntityRenderer(CATAPULT_ZOMBIE.get(), ctx -> new CatapultZombieRender(ctx));
		ev.registerEntityRenderer(GARGANTUAR.get(), ctx -> new GargantuarRender(ctx));
		ev.registerEntityRenderer(IMP.get(), ctx -> new ImpRender(ctx));
		ev.registerEntityRenderer(GIGA_GARGANTUAR.get(), ctx -> new GigaGargantuarRender(ctx));
		ev.registerEntityRenderer(ZOMBOT.get(), ctx -> new ZombotRender(ctx));
		ev.registerEntityRenderer(PEASHOOTER_ZOMBIE.get(), ctx -> new PeaShooterZombieRender(ctx));
		ev.registerEntityRenderer(GATLINGPEA_ZOMBIE.get(), ctx -> new GatlingPeaZombieRender(ctx));
		ev.registerEntityRenderer(SQUASH_ZOMBIE.get(), ctx -> new SquashZombieRender(ctx));
		ev.registerEntityRenderer(JALAPENO_ZOMBIE.get(), ctx -> new JalapenoZombieRender(ctx));
		ev.registerEntityRenderer(WALLNUT_ZOMBIE.get(), ctx -> new WallNutZombieRender(ctx));
		ev.registerEntityRenderer(TALLNUT_ZOMBIE.get(), ctx -> new TallNutZombieRender(ctx));
		ev.registerEntityRenderer(GIGA_TOMB_STONE.get(), ctx -> new GigaTombStoneRender(ctx));
		ev.registerEntityRenderer(EDGAR_ZOMBOT.get(), ctx -> new EdgarZombotRender<>(ctx));
		ev.registerEntityRenderer(PEA_SHOOTER.get(), ctx -> new PeaShooterRender(ctx));
		ev.registerEntityRenderer(SUN_FLOWER.get(), ctx -> new SunFlowerRender(ctx));
		ev.registerEntityRenderer(CHERRY_BOMB.get(), ctx -> new CherryBombRender(ctx));
		ev.registerEntityRenderer(WALL_NUT.get(), ctx -> new WallNutRender(ctx));
		ev.registerEntityRenderer(POTATO_MINE.get(), ctx -> new PotatoMineRender(ctx));
		ev.registerEntityRenderer(SNOW_PEA.get(), ctx -> new SnowPeaRender(ctx));
		ev.registerEntityRenderer(CHOMPER.get(), ctx -> new ChomperRender(ctx));
		ev.registerEntityRenderer(REPEATER.get(), ctx -> new RepeaterRender(ctx));
		ev.registerEntityRenderer(LILY_PAD.get(), ctx -> new LilyPadRender(ctx));
		ev.registerEntityRenderer(SQUASH.get(), ctx -> new SquashRender(ctx));
		ev.registerEntityRenderer(THREE_PEATER.get(), ctx -> new ThreePeaterRender(ctx));
		ev.registerEntityRenderer(TANGLE_KELP.get(), ctx -> new TangleKelpRender(ctx));
		ev.registerEntityRenderer(JALAPENO.get(), ctx -> new JalapenoRender(ctx));
		ev.registerEntityRenderer(SPIKE_WEED.get(), ctx -> new SpikeWeedRender(ctx));
		ev.registerEntityRenderer(TORCH_WOOD.get(), ctx -> new TorchWoodRender(ctx));
		ev.registerEntityRenderer(TALL_NUT.get(), ctx -> new TallNutRender(ctx));
		ev.registerEntityRenderer(PUFF_SHROOM.get(), ctx -> new PuffShroomRender(ctx));
		ev.registerEntityRenderer(SUN_SHROOM.get(), ctx -> new SunShroomRender(ctx));
		ev.registerEntityRenderer(FUME_SHROOM.get(), ctx -> new FumeShroomRender(ctx));
		ev.registerEntityRenderer(GRAVE_BUSTER.get(), ctx -> new GraveBusterRender(ctx));
		ev.registerEntityRenderer(HYPNO_SHROOM.get(), ctx -> new HypnoShroomRender(ctx));
		ev.registerEntityRenderer(SCAREDY_SHROOM.get(), ctx -> new ScaredyShroomRender(ctx));
		ev.registerEntityRenderer(ICE_SHROOM.get(), ctx -> new IceShroomRender(ctx));
		ev.registerEntityRenderer(DOOM_SHROOM.get(), ctx -> new DoomShroomRender(ctx));
		ev.registerEntityRenderer(SEA_SHROOM.get(), ctx -> new SeaShroomRender(ctx));
		ev.registerEntityRenderer(PLANTERN.get(), ctx -> new PlanternRender(ctx));
		ev.registerEntityRenderer(CACTUS.get(), ctx -> new CactusRender(ctx));
		ev.registerEntityRenderer(BLOVER.get(), ctx -> new BloverRender(ctx));
		ev.registerEntityRenderer(SPLIT_PEA.get(), ctx -> new SplitPeaRender(ctx));
		ev.registerEntityRenderer(STARFRUIT.get(), ctx -> new StarFruitRender(ctx));
		ev.registerEntityRenderer(PUMPKIN.get(), ctx -> new PumpkinRender(ctx));
		ev.registerEntityRenderer(MAGNET_SHROOM.get(), ctx -> new MagnetShroomRender(ctx));
		ev.registerEntityRenderer(CABBAGE_PULT.get(), ctx -> new CabbagePultRender(ctx));
		ev.registerEntityRenderer(FLOWER_POT.get(), ctx -> new FlowerPotRender(ctx));
		ev.registerEntityRenderer(KERNEL_PULT.get(), ctx -> new KernelPultRender(ctx));
		ev.registerEntityRenderer(GARLIC.get(), ctx -> new GarlicRender(ctx));
		ev.registerEntityRenderer(COFFEE_BEAN.get(), ctx -> new CoffeeBeanRender(ctx));
		ev.registerEntityRenderer(UMBRELLA_LEAF.get(), ctx -> new UmbrellaLeafRender(ctx));
		ev.registerEntityRenderer(MARIGOLD.get(), ctx -> new MariGoldRender(ctx));
		ev.registerEntityRenderer(MELON_PULT.get(), ctx -> new MelonPultRender(ctx));
		ev.registerEntityRenderer(GATLING_PEA.get(), ctx -> new GatlingPeaRender(ctx));
		ev.registerEntityRenderer(TWIN_SUNFLOWER.get(), ctx -> new TwinSunFlowerRender(ctx));
		ev.registerEntityRenderer(GLOOM_SHROOM.get(), ctx -> new GloomShroomRender(ctx));
		ev.registerEntityRenderer(CAT_TAIL.get(), ctx -> new CatTailRender(ctx));
		ev.registerEntityRenderer(WINTER_MELON.get(), ctx -> new WinterMelonRender(ctx));
		ev.registerEntityRenderer(GOLD_MAGNET.get(), ctx -> new GoldMagnetRender(ctx));
		ev.registerEntityRenderer(SPIKE_ROCK.get(), ctx -> new SpikeRockRender(ctx));
		ev.registerEntityRenderer(COB_CANNON.get(), ctx -> new CobCannonRender(ctx));
		ev.registerEntityRenderer(IMITATER.get(), ctx -> new ImitaterRender(ctx));
		ev.registerEntityRenderer(STRANGE_CAT.get(), ctx -> new StrangeCatRender(ctx));
		ev.registerEntityRenderer(ANGEL_STARFRUIT.get(), ctx -> new AngelStarFruitRender(ctx));
		ev.registerEntityRenderer(GOLD_LEAF.get(), ctx -> new GoldLeafRender(ctx));
		ev.registerEntityRenderer(GOLD_BLOOM.get(), ctx -> new GoldBloomRender(ctx));
		ev.registerEntityRenderer(BUTTER_PULT.get(), ctx -> new ButterPultRender(ctx));
		ev.registerEntityRenderer(BAMBOO_LORD.get(), ctx -> new BambooLordRender(ctx));
		ev.registerEntityRenderer(ICEBERG_LETTUCE.get(), ctx -> new IcebergLettuceRender(ctx));
		ev.registerEntityRenderer(BONK_CHOY.get(), ctx -> new BonkChoyRender(ctx));
		ev.registerBlockEntityRenderer(TileEntityRegister.SUNFLOWER_TROPHY.get(), ctx -> new SunFlowerTrophyTER(ctx));
		ev.registerBlockEntityRenderer(TileEntityRegister.ESSENCE_ALTAR.get(), ctx -> new EssenceAltarTER(ctx));
	}


	@SubscribeEvent
	public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions ev) {
		ev.registerLayerDefinition(BallModel.LAYER,
			BallModel::createBodyLayer);
		ev.registerLayerDefinition(CabbageModel.LAYER,
			CabbageModel::createBodyLayer);
		ev.registerLayerDefinition(CornModel.LAYER,
			CornModel::createBodyLayer);
		ev.registerLayerDefinition(KernelModel.LAYER,
			KernelModel::createBodyLayer);
		ev.registerLayerDefinition(StarModel.LAYER,
			StarModel::createBodyLayer);
		ev.registerLayerDefinition(TargetArrowModel.LAYER,
			TargetArrowModel::createBodyLayer);
		ev.registerLayerDefinition(ThornModel.LAYER,
			ThornModel::createBodyLayer);
		ev.registerLayerDefinition(FoodieZombieModel.LAYER,
			FoodieZombieModel::createBodyLayer);
		ev.registerLayerDefinition(MooBloomModel.LAYER,
			MooBloomModel::createBodyLayer);
		ev.registerLayerDefinition(BobsleCarModel.LAYER,
			BobsleCarModel::createBodyLayer);
		ev.registerLayerDefinition(DropModel.LAYER,
			DropModel::createBodyLayer);
		ev.registerLayerDefinition(FireCrackersModel.LAYER,
			FireCrackersModel::createBodyLayer);
		ev.registerLayerDefinition(GardenRakeModel.LAYER,
			GardenRakeModel::createBodyLayer);
		ev.registerLayerDefinition(GiftBoxModel.LAYER,
			GiftBoxModel::createBodyLayer);
		ev.registerLayerDefinition(LadderModel.LAYER,
			LadderModel::createBodyLayer);
		ev.registerLayerDefinition(LawnMowerModel.LAYER,
			LawnMowerModel::createBodyLayer);
		ev.registerLayerDefinition(SmallChomperModel.LAYER,
			SmallChomperModel::createBodyLayer);
		ev.registerLayerDefinition(ZombieHandModel.LAYER,
			ZombieHandModel::createBodyLayer);
		ev.registerLayerDefinition(CrazyDaveModel.LAYER,
			CrazyDaveModel::createBodyLayer);
		ev.registerLayerDefinition(PennyModel.LAYER,
			PennyModel::createBodyLayer);
		ev.registerLayerDefinition(DestroyCarModel.LAYER,
			DestroyCarModel::createBodyLayer);
		ev.registerLayerDefinition(AngelStarFruitModel.LAYER,
			AngelStarFruitModel::createBodyLayer);
		ev.registerLayerDefinition(GatlingPeaModel.LAYER,
			GatlingPeaModel::createBodyLayer);
		ev.registerLayerDefinition(PeaShooterModel.LAYER,
			PeaShooterModel::createBodyLayer);
		ev.registerLayerDefinition(RepeaterModel.LAYER,
			RepeaterModel::createBodyLayer);
		ev.registerLayerDefinition(SplitPeaModel.LAYER,
			SplitPeaModel::createBodyLayer);
		ev.registerLayerDefinition(StarFruitModel.LAYER,
			StarFruitModel::createBodyLayer);
		ev.registerLayerDefinition(ThreePeaterModel.LAYER,
			ThreePeaterModel::createBodyLayer);
		ev.registerLayerDefinition(CabbagePultModel.LAYER,
			CabbagePultModel::createBodyLayer);
		ev.registerLayerDefinition(KernelPultModel.LAYER,
			KernelPultModel::createBodyLayer);
		ev.registerLayerDefinition(MelonPultModel.LAYER,
			MelonPultModel::createBodyLayer);
		ev.registerLayerDefinition(BloverModel.LAYER,
			BloverModel::createBodyLayer);
		ev.registerLayerDefinition(FlowerPotModel.LAYER,
			FlowerPotModel::createBodyLayer);
		ev.registerLayerDefinition(GoldMagnetModel.LAYER,
			GoldMagnetModel::createBodyLayer);
		ev.registerLayerDefinition(GraveBusterModel.LAYER,
			GraveBusterModel::createBodyLayer);
		ev.registerLayerDefinition(LilyPadModel.LAYER,
			LilyPadModel::createBodyLayer);
		ev.registerLayerDefinition(MagnetShroomModel.LAYER,
			MagnetShroomModel::createBodyLayer);
		ev.registerLayerDefinition(GarlicModel.LAYER,
			GarlicModel::createBodyLayer);
		ev.registerLayerDefinition(PumpkinModel.LAYER,
			PumpkinModel::createBodyLayer);
		ev.registerLayerDefinition(TallNutModel.LAYER,
			TallNutModel::createBodyLayer);
		ev.registerLayerDefinition(WallNutModel.LAYER,
			WallNutModel::createBodyLayer);
		ev.registerLayerDefinition(BonkChoyModel.LAYER,
			BonkChoyModel::createBodyLayer);
		ev.registerLayerDefinition(ChomperModel.LAYER,
			ChomperModel::createBodyLayer);
		ev.registerLayerDefinition(SquashModel.LAYER,
			SquashModel::createBodyLayer);
		ev.registerLayerDefinition(TangleKelpModel.LAYER,
			TangleKelpModel::createBodyLayer);
		ev.registerLayerDefinition(UmbrellaLeafModel.LAYER,
			UmbrellaLeafModel::createBodyLayer);
		ev.registerLayerDefinition(BambooLordModel.LAYER,
			BambooLordModel::createBodyLayer);
		ev.registerLayerDefinition(CherryBombModel.LAYER,
			CherryBombModel::createBodyLayer);
		ev.registerLayerDefinition(CobCannonModel.LAYER,
			CobCannonModel::createBodyLayer);
		ev.registerLayerDefinition(DoomShroomModel.LAYER,
			DoomShroomModel::createBodyLayer);
		ev.registerLayerDefinition(PotatoMineModel.LAYER,
			PotatoMineModel::createBodyLayer);
		ev.registerLayerDefinition(JalapenoModel.LAYER,
			JalapenoModel::createBodyLayer);
		ev.registerLayerDefinition(TorchWoodModel.LAYER,
			TorchWoodModel::createBodyLayer);
		ev.registerLayerDefinition(IcebergLettuceModel.LAYER,
			IcebergLettuceModel::createBodyLayer);
		ev.registerLayerDefinition(IceShroomModel.LAYER,
			IceShroomModel::createBodyLayer);
		ev.registerLayerDefinition(SnowPeaModel.LAYER,
			SnowPeaModel::createBodyLayer);
		ev.registerLayerDefinition(WinterMelonModel.LAYER,
			WinterMelonModel::createBodyLayer);
		ev.registerLayerDefinition(GoldLeafModel.LAYER,
			GoldLeafModel::createBodyLayer);
		ev.registerLayerDefinition(GoldBloomModel.LAYER,
			GoldBloomModel::createBodyLayer);
		ev.registerLayerDefinition(PlanternModel.LAYER,
			PlanternModel::createBodyLayer);
		ev.registerLayerDefinition(SunFlowerModel.LAYER,
			SunFlowerModel::createBodyLayer);
		ev.registerLayerDefinition(SunShroomModel.LAYER,
			SunShroomModel::createBodyLayer);
		ev.registerLayerDefinition(TwinSunFlowerModel.LAYER,
			TwinSunFlowerModel::createBodyLayer);
		ev.registerLayerDefinition(CoffeeBeanModel.LAYER,
			CoffeeBeanModel::createBodyLayer);
		ev.registerLayerDefinition(HypnoShroomModel.LAYER,
			HypnoShroomModel::createBodyLayer);
		ev.registerLayerDefinition(ImitaterModel.LAYER,
			ImitaterModel::createBodyLayer);
		ev.registerLayerDefinition(MariGoldModel.LAYER,
			MariGoldModel::createBodyLayer);
		ev.registerLayerDefinition(StrangeCatModel.LAYER,
			StrangeCatModel::createBodyLayer);
		ev.registerLayerDefinition(CactusModel.LAYER,
			CactusModel::createBodyLayer);
		ev.registerLayerDefinition(CatTailModel.LAYER,
			CatTailModel::createBodyLayer);
		ev.registerLayerDefinition(SpikeRockModel.LAYER,
			SpikeRockModel::createBodyLayer);
		ev.registerLayerDefinition(SpikeWeedModel.LAYER,
			SpikeWeedModel::createBodyLayer);
		ev.registerLayerDefinition(FumeShroomModel.LAYER,
			FumeShroomModel::createBodyLayer);
		ev.registerLayerDefinition(GloomShroomModel.LAYER,
			GloomShroomModel::createBodyLayer);
		ev.registerLayerDefinition(PuffShroomModel.LAYER,
			PuffShroomModel::createBodyLayer);
		ev.registerLayerDefinition(ScaredyShroomModel.LAYER,
			ScaredyShroomModel::createBodyLayer);
		ev.registerLayerDefinition(SeaShroomModel.LAYER,
			SeaShroomModel::createBodyLayer);
		ev.registerLayerDefinition(OriginModel.LAYER,
			OriginModel::createBodyLayer);
		ev.registerLayerDefinition(BackupDancerModel.LAYER,
			BackupDancerModel::createBodyLayer);
		ev.registerLayerDefinition(BucketHeadZombieModel.LAYER,
			BucketHeadZombieModel::createBodyLayer);
		ev.registerLayerDefinition(ConeHeadZombieModel.LAYER,
			ConeHeadZombieModel::createBodyLayer);
		ev.registerLayerDefinition(DancingZombieModel.LAYER,
			DancingZombieModel::createBodyLayer);
		ev.registerLayerDefinition(FlagZombieModel.LAYER,
			FlagZombieModel::createBodyLayer);
		ev.registerLayerDefinition(FootballZombieModel.LAYER,
			FootballZombieModel::createBodyLayer);
		ev.registerLayerDefinition(GigaFootballZombieModel.LAYER,
			GigaFootballZombieModel::createBodyLayer);
		ev.registerLayerDefinition(NewspaperZombieModel.LAYER,
			NewspaperZombieModel::createBodyLayer);
		ev.registerLayerDefinition(NormalZombieModel.LAYER,
			NormalZombieModel::createBodyLayer);
		ev.registerLayerDefinition(PoleZombieModel.LAYER,
			PoleZombieModel::createBodyLayer);
		ev.registerLayerDefinition(ScreenDoorZombieModel.LAYER,
			ScreenDoorZombieModel::createBodyLayer);
		ev.registerLayerDefinition(SundayEditionZombieModel.LAYER,
			SundayEditionZombieModel::createBodyLayer);
		ev.registerLayerDefinition(TombStoneModel.LAYER,
			TombStoneModel::createBodyLayer);
		ev.registerLayerDefinition(CoffinModel.LAYER,
			CoffinModel::createBodyLayer);
		ev.registerLayerDefinition(MournerZombieModel.LAYER,
			MournerZombieModel::createBodyLayer);
		ev.registerLayerDefinition(NobleZombieModel.LAYER,
			NobleZombieModel::createBodyLayer);
		ev.registerLayerDefinition(RaZombieModel.LAYER,
			RaZombieModel::createBodyLayer);
		ev.registerLayerDefinition(TrickZombieModel.LAYER,
			TrickZombieModel::createBodyLayer);
		ev.registerLayerDefinition(BalloonZombieModel.LAYER,
			BalloonZombieModel::createBodyLayer);
		ev.registerLayerDefinition(BobsleTeamModel.LAYER,
			BobsleTeamModel::createBodyLayer);
		ev.registerLayerDefinition(BobsleZombieModel.LAYER,
			BobsleZombieModel::createBodyLayer);
		ev.registerLayerDefinition(DiggerZombieModel.LAYER,
			DiggerZombieModel::createBodyLayer);
		ev.registerLayerDefinition(DolphinRiderModel.LAYER,
			DolphinRiderModel::createBodyLayer);
		ev.registerLayerDefinition(DolphinRiderZombieModel.LAYER,
			DolphinRiderZombieModel::createBodyLayer);
		ev.registerLayerDefinition(DuckyTubeModel.LAYER,
			DuckyTubeModel::createBodyLayer);
		ev.registerLayerDefinition(JackInBoxZombieModel.LAYER,
			JackInBoxZombieModel::createBodyLayer);
		ev.registerLayerDefinition(LavaZombieModel.LAYER,
			LavaZombieModel::createBodyLayer);
		ev.registerLayerDefinition(PogoZombieModel.LAYER,
			PogoZombieModel::createBodyLayer);
		ev.registerLayerDefinition(SnorkelZombieModel.LAYER,
			SnorkelZombieModel::createBodyLayer);
		ev.registerLayerDefinition(YetiZombieModel.LAYER,
			YetiZombieModel::createBodyLayer);
		ev.registerLayerDefinition(ZombieDolphinModel.LAYER,
			ZombieDolphinModel::createBodyLayer);
		ev.registerLayerDefinition(ZomboniModel.LAYER,
			ZomboniModel::createBodyLayer);
		ev.registerLayerDefinition(BungeeZombieModel.LAYER,
			BungeeZombieModel::createBodyLayer);
		ev.registerLayerDefinition(CatapultZombieModel.LAYER,
			CatapultZombieModel::createBodyLayer);
		ev.registerLayerDefinition(ZombotModel.LAYER,
			ZombotModel::createBodyLayer);
		ev.registerLayerDefinition(EdgarZombotModel.LAYER,
			EdgarZombotModel::createBodyLayer);
		ev.registerLayerDefinition(GargantuarModel.LAYER,
			GargantuarModel::createBodyLayer);
		ev.registerLayerDefinition(ImpModel.LAYER,
			ImpModel::createBodyLayer);
		ev.registerLayerDefinition(LadderZombieModel.LAYER,
			LadderZombieModel::createBodyLayer);
		ev.registerLayerDefinition(GatlingPeaZombieModel.LAYER,
			GatlingPeaZombieModel::createBodyLayer);
		ev.registerLayerDefinition(JalapenoZombieModel.LAYER,
			JalapenoZombieModel::createBodyLayer);
		ev.registerLayerDefinition(PeaShooterZombieModel.LAYER,
			PeaShooterZombieModel::createBodyLayer);
		ev.registerLayerDefinition(PumpkinZombieModel.LAYER,
			PumpkinZombieModel::createBodyLayer);
		ev.registerLayerDefinition(SquashZombieModel.LAYER,
			SquashZombieModel::createBodyLayer);
		ev.registerLayerDefinition(TallNutZombieModel.LAYER,
			TallNutZombieModel::createBodyLayer);
		ev.registerLayerDefinition(WallNutZombieModel.LAYER,
			WallNutZombieModel::createBodyLayer);
	}

}