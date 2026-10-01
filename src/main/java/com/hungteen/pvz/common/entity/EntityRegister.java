package com.hungteen.pvz.common.entity;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.api.types.IZombieType;
import com.hungteen.pvz.common.entity.bullet.*;
import com.hungteen.pvz.common.entity.bullet.itembullet.*;
import com.hungteen.pvz.common.entity.creature.FoodieZombieEntity;
import com.hungteen.pvz.common.entity.creature.MooBloom;
import com.hungteen.pvz.common.entity.effect.OriginEffectEntity;
import com.hungteen.pvz.common.entity.misc.*;
import com.hungteen.pvz.common.entity.misc.bowling.ExplosionBowlingEntity;
import com.hungteen.pvz.common.entity.misc.bowling.GiantNutBowlingEntity;
import com.hungteen.pvz.common.entity.misc.bowling.WallNutBowlingEntity;
import com.hungteen.pvz.common.entity.misc.drop.*;
import com.hungteen.pvz.common.entity.npc.CrazyDaveEntity;
import com.hungteen.pvz.common.entity.npc.PennyEntity;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.plant.appease.*;
import com.hungteen.pvz.common.entity.plant.arma.ButterPultEntity;
import com.hungteen.pvz.common.entity.plant.arma.CabbagePultEntity;
import com.hungteen.pvz.common.entity.plant.arma.KernelPultEntity;
import com.hungteen.pvz.common.entity.plant.arma.MelonPultEntity;
import com.hungteen.pvz.common.entity.plant.assist.*;
import com.hungteen.pvz.common.entity.plant.defence.GarlicEntity;
import com.hungteen.pvz.common.entity.plant.defence.PumpkinEntity;
import com.hungteen.pvz.common.entity.plant.defence.TallNutEntity;
import com.hungteen.pvz.common.entity.plant.defence.WallNutEntity;
import com.hungteen.pvz.common.entity.plant.enforce.*;
import com.hungteen.pvz.common.entity.plant.explosion.*;
import com.hungteen.pvz.common.entity.plant.flame.JalapenoEntity;
import com.hungteen.pvz.common.entity.plant.flame.TorchWoodEntity;
import com.hungteen.pvz.common.entity.plant.ice.IceShroomEntity;
import com.hungteen.pvz.common.entity.plant.ice.IcebergLettuceEntity;
import com.hungteen.pvz.common.entity.plant.ice.SnowPeaEntity;
import com.hungteen.pvz.common.entity.plant.ice.WinterMelonEntity;
import com.hungteen.pvz.common.entity.plant.light.*;
import com.hungteen.pvz.common.entity.plant.magic.*;
import com.hungteen.pvz.common.entity.plant.spear.CactusEntity;
import com.hungteen.pvz.common.entity.plant.spear.CatTailEntity;
import com.hungteen.pvz.common.entity.plant.spear.SpikeRockEntity;
import com.hungteen.pvz.common.entity.plant.spear.SpikeWeedEntity;
import com.hungteen.pvz.common.entity.plant.toxic.*;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import com.hungteen.pvz.common.entity.zombie.custom.GigaTombStoneEntity;
import com.hungteen.pvz.common.entity.zombie.custom.LavaZombieEntity;
import com.hungteen.pvz.common.entity.zombie.grass.*;
import com.hungteen.pvz.common.entity.zombie.other.*;
import com.hungteen.pvz.common.entity.zombie.part.PVZZombiePartEntity;
import com.hungteen.pvz.common.entity.zombie.pool.*;
import com.hungteen.pvz.common.entity.zombie.roof.*;
import com.hungteen.pvz.common.entity.zombie.zombotany.*;
import com.hungteen.pvz.common.impl.plant.PlantType;
import com.hungteen.pvz.common.impl.zombie.ZombieType;
import com.hungteen.pvz.utils.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = PVZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegister {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =  DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PVZMod.MOD_ID);

	/**
	 * drops
	 */
	public static final RegistryObject<EntityType<SunEntity>> SUN = registerEntityType(SunEntity::new, "sun", MobCategory.AMBIENT);
	public static final RegistryObject<EntityType<CoinEntity>> COIN = registerEntityType(CoinEntity::new, "coin", MobCategory.MISC);
	public static final RegistryObject<EntityType<JewelEntity>> JEWEL = registerEntityType(JewelEntity::new, "jewel", MobCategory.MISC, 0.9f, 0.9f);
	public static final RegistryObject<EntityType<EnergyEntity>> ENERGY = registerEntityType(EnergyEntity::new, "energy", MobCategory.MISC, 0.9f, 2f);
	public static final RegistryObject<EntityType<GiftBoxEntity>> GIFT_BOX = registerEntityType(GiftBoxEntity::new, "gift_box", MobCategory.MISC, 0.9f, 1f);
	public static final RegistryObject<EntityType<FallenStar>> FALLEN_STAR = registerEntityType(FallenStar::new, "fallen_star", MobCategory.MISC, 0.4f, 0.4f);
	public static final RegistryObject<EntityType<SeedPacketEntity>> SEED_PACKET = registerEntityType(SeedPacketEntity::new, "seed_packet", MobCategory.MISC, 0.5f, 0.5f);

	/**
	 * bullets
	 */
	public static final RegistryObject<EntityType<PeaEntity>> PEA = registerEntityType(PeaEntity::new, "pea", MobCategory.MISC);
	public static final RegistryObject<EntityType<PotatoEntity>> POTATO = registerEntityType(PotatoEntity::new, "potato", MobCategory.MISC);
	public static final RegistryObject<EntityType<SporeEntity>> SPORE = registerEntityType(SporeEntity::new, "spore", MobCategory.MISC, 0.25f, 0.25f);
	public static final RegistryObject<EntityType<FumeEntity>> FUME = registerEntityType(FumeEntity::new, "fume", MobCategory.MISC, 0.25f, 0.25f);
	public static final RegistryObject<EntityType<MetalItemEntity>> METAL = registerEntityType(MetalItemEntity::new, "metal", MobCategory.MISC, 0.2f, 0.2f);
	public static final RegistryObject<EntityType<ThornEntity>> THORN = registerEntityType(ThornEntity::new, "thorn", MobCategory.MISC, 0.2f, 0.2f);
	public static final RegistryObject<EntityType<StarEntity>> STAR = registerEntityType(StarEntity::new, "star", MobCategory.MISC);
	public static final RegistryObject<EntityType<NutEntity>> NUT = registerEntityType(NutEntity::new, "nut", MobCategory.MISC);
	public static final RegistryObject<EntityType<CabbageEntity>> CABBAGE = registerEntityType(CabbageEntity::new, "cabbage", MobCategory.MISC, 0.5f, 0.5f);
	public static final RegistryObject<EntityType<IceCabbageEntity>> ICE_CABBAGE = registerEntityType(IceCabbageEntity::new, "ice_cabbage", MobCategory.MISC, 0.5f, 0.5f);
	public static final RegistryObject<EntityType<KernelEntity>> KERNEL = registerEntityType(KernelEntity::new, "kernel", MobCategory.MISC, 0.4f, 0.4f);
	public static final RegistryObject<EntityType<ButterEntity>> BUTTER = registerEntityType(ButterEntity::new, "butter", MobCategory.MISC, 0.6f, 0.6f);
	public static final RegistryObject<EntityType<TargetArrowEntity>> TARGET_ARROW = registerEntityType(TargetArrowEntity::new, "target_arrow", MobCategory.MISC, 0.5f, 0.5f);
	public static final RegistryObject<EntityType<MelonEntity>> MELON = registerEntityType(MelonEntity::new, "melon", MobCategory.MISC, 0.6f, 0.6f);
	public static final RegistryObject<EntityType<FireCrackerEntity>> FIRE_CRACKER = registerEntityType(FireCrackerEntity::new, "fire_cracker", MobCategory.MISC, 0.5f, 0.5f);
	public static final RegistryObject<EntityType<BallEntity>> BALL = registerEntityType(BallEntity::new, "ball", MobCategory.MISC, 0.4f, 0.4f);
	public static final RegistryObject<EntityType<CornEntity>> CORN = registerEntityType(CornEntity::new, "corn", MobCategory.MISC, 1f, 1f);
	public static final RegistryObject<EntityType<SeedArrowEntity>> SEED_ARROW = ENTITY_TYPES.register("seed_arrow",
			() -> EntityType.Builder.<SeedArrowEntity>of(SeedArrowEntity::new, MobCategory.MISC).sized(0.2F, 0.2F)
					.clientTrackingRange(4).updateInterval(20).build(StringUtil.prefix("seed_arrow").toString()));
	public static final RegistryObject<EntityType<OriginArrowEntity>> ORIGIN_ARROW = ENTITY_TYPES.register("origin_arrow",
			() -> EntityType.Builder.<OriginArrowEntity>of(OriginArrowEntity::new, MobCategory.MISC).sized(0.2F, 0.2F)
					.clientTrackingRange(4).updateInterval(20).build(StringUtil.prefix("origin_arrow").toString()));
	public static final RegistryObject<EntityType<ThrownFogInBottle>> FOG_IN_BOTTLE = ENTITY_TYPES.register("fog_in_bottle",
			() -> EntityType.Builder.<ThrownFogInBottle>of(ThrownFogInBottle::new, MobCategory.MISC).sized(0.25F, 0.25F)
					.clientTrackingRange(4).updateInterval(20).build(StringUtil.prefix("fog_in_bottle").toString()));

	/**
	 * effects
	 */
	public static final RegistryObject<EntityType<OriginEffectEntity>> ORIGIN_EFFECT = registerEntityType(OriginEffectEntity::new, "origin_effect", MobCategory.MISC);
	public static final RegistryObject<EntityType<DoomFixerEntity>> DOOM_FIXER = registerEntityType(DoomFixerEntity::new, "doom_fixer", MobCategory.MISC);

	/**
	 * misc
	 */
	public static final RegistryObject<EntityType<SmallChomperEntity>> SMALL_CHOMPER = registerEntityType(SmallChomperEntity::new, "small_chomper", MobCategory.MISC, 0.4f, 0.5f);
	public static final RegistryObject<EntityType<BobsleCarEntity>> BOBSLE_CAR = registerEntityType(BobsleCarEntity::new, "bobsle_car", MobCategory.MISC, 1.25f, 1.4f);
	public static final RegistryObject<EntityType<PVZZombiePartEntity>> ZOMBIE_PART = registerEntityType(PVZZombiePartEntity::new, "zombie_part", MobCategory.MISC);
	public static final RegistryObject<EntityType<ZombieHandEntity>> ZOMBIE_HAND = registerEntityType(ZombieHandEntity::new, "zombie_hand", MobCategory.MISC, 0.4f, 0.5f);
	public static final RegistryObject<EntityType<WallNutBowlingEntity>> WALL_NUT_BOWLING = registerEntityType(WallNutBowlingEntity::new, "wall_nut_bowling", MobCategory.MISC, 0.95f, 1f);
	public static final RegistryObject<EntityType<ExplosionBowlingEntity>> EXPLOSION_BOWLING = registerEntityType(ExplosionBowlingEntity::new, "explosion_bowling", MobCategory.MISC, 0.95f, 1f);
	public static final RegistryObject<EntityType<GiantNutBowlingEntity>> GIANT_NUT_BOWLING = registerEntityType(GiantNutBowlingEntity::new, "giant_nut_bowling", MobCategory.MISC, 2.5f, 2.5f);
	public static final RegistryObject<EntityType<LawnMowerEntity>> LAWN_MOWER = registerEntityType(LawnMowerEntity::new, "lawn_mower", MobCategory.MISC, 0.8f, 0.8f);
	public static final RegistryObject<EntityType<FireCrackersEntity>> FIRE_CRACKERS = registerEntityType(FireCrackersEntity::new, "fire_crackers", MobCategory.MISC);
	public static final RegistryObject<EntityType<ElementBallEntity>> ELEMENT_BALL = registerEntityType(ElementBallEntity::new, "element_ball", MobCategory.MISC, 3f, 3f);
	public static final RegistryObject<EntityType<DestroyCarEntity>> DESTROY_CAR = registerEntityType(DestroyCarEntity::new, "destroy_car", MobCategory.MISC, 2F, 2F);
	public static final RegistryObject<EntityType<GardenRakeEntity>> GARDEN_RAKE = registerEntityType(GardenRakeEntity::new, "garden_rake", MobCategory.MISC, 0.9f, 0.8f);

	/**
	 * animals
	 */
	public static final RegistryObject<EntityType<FoodieZombieEntity>> FOODIE_ZOMBIE = registerEntityType(FoodieZombieEntity::new, "foodie_zombie", MobCategory.WATER_CREATURE);
	public static final RegistryObject<EntityType<MooBloom>> MOOBLOOM = registerEntityType(MooBloom::new, "moo_bloom", MobCategory.CREATURE, 0.9f, 1.4f);

	/**
	 * npc
	 */
	public static final RegistryObject<EntityType<CrazyDaveEntity>> CRAZY_DAVE = registerEntityType(CrazyDaveEntity::new, "crazy_dave", MobCategory.CREATURE, 0.9f, 2.4f);
	public static final RegistryObject<EntityType<PennyEntity>> PANNEY = registerEntityType(PennyEntity::new, "panney", MobCategory.CREATURE, 1.8f, 2f);

	/**
	 * zombies
	 */
	public static final RegistryObject<EntityType<NormalZombieEntity>> NORMAL_ZOMBIE = registerZombieEntityType(NormalZombieEntity::new, "normal_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<FlagZombieEntity>> FLAG_ZOMBIE = registerZombieEntityType(FlagZombieEntity::new, "flag_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<ConeHeadZombieEntity>> CONEHEAD_ZOMBIE = registerZombieEntityType(ConeHeadZombieEntity::new, "conehead_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<PoleZombieEntity>> POLE_ZOMBIE = registerZombieEntityType(PoleZombieEntity::new, "pole_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<BucketHeadZombieEntity>> BUCKETHEAD_ZOMBIE = registerZombieEntityType(BucketHeadZombieEntity::new, "buckethead_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<SnorkelZombieEntity>> SNORKEL_ZOMBIE = registerZombieEntityType(SnorkelZombieEntity::new, "snorkel_zombie", 0.7f, 1.9f);
	public static final RegistryObject<EntityType<ZomboniEntity>> ZOMBONI = registerZombieEntityType(ZomboniEntity::new, "zomboni", 0.8f, 2.3f);
	public static final RegistryObject<EntityType<BobsleTeamEntity>> BOBSLE_TEAM = registerZombieEntityType(BobsleTeamEntity::new, "bobsle_team", 1.25f, 1.4f);
	public static final RegistryObject<EntityType<BobsleZombieEntity>> BOBSLE_ZOMBIE = registerZombieEntityType(BobsleZombieEntity::new, "bobsle_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<ZombieDolphinEntity>> ZOMBIE_DOLPHIN = registerZombieEntityType(ZombieDolphinEntity::new, "zombie_dolphin", 1.0f, 0.7f);
	public static final RegistryObject<EntityType<DolphinRiderEntity>> DOLPHIN_RIDER = registerZombieEntityType(DolphinRiderEntity::new, "dolphin_rider", 0.7f, 1.6f);
	public static final RegistryObject<EntityType<DolphinRiderZombieEntity>> DOLPHIN_RIDER_ZOMBIE = registerZombieEntityType(DolphinRiderZombieEntity::new, "dolphin_rider_zombie", 0.7f, 1.9f);
	public static final RegistryObject<EntityType<LavaZombieEntity>> LAVA_ZOMBIE = registerZombieEntityType(LavaZombieEntity::new, "lava_zombie", 0.7f, 1.9f);
	public static final RegistryObject<EntityType<NewspaperZombieEntity>> NEWSPAPER_ZOMBIE = registerZombieEntityType(NewspaperZombieEntity::new, "newspaper_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<TombStoneEntity>> TOMB_STONE = registerZombieEntityType(TombStoneEntity::new, "tomb_stone", 0.8f, 1.6f);
	public static final RegistryObject<EntityType<ScreenDoorZombieEntity>> SCREENDOOR_ZOMBIE = registerZombieEntityType(ScreenDoorZombieEntity::new, "screendoor_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<FootballZombieEntity>> FOOTBALL_ZOMBIE = registerZombieEntityType(FootballZombieEntity::new, "football_zombie", 0.8f, 2.4f);
	public static final RegistryObject<EntityType<DancingZombieEntity>> DANCING_ZOMBIE = registerZombieEntityType(DancingZombieEntity::new, "dancing_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<BackupDancerEntity>> BACKUP_DANCER = registerZombieEntityType(BackupDancerEntity::new, "backup_dancer", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<GigaFootballZombieEntity>> GIGA_FOOTBALL_ZOMBIE = registerZombieEntityType(GigaFootballZombieEntity::new, "giga_football_zombie", 0.8f, 2.4f);
	public static final RegistryObject<EntityType<PumpkinZombieEntity>> PUMPKIN_ZOMBIE = registerZombieEntityType(PumpkinZombieEntity::new, "pumpkin_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<TrickZombieEntity>> TRICK_ZOMBIE = registerZombieEntityType(TrickZombieEntity::new, "trick_zombie", 0.6f, 1.2f);
	public static final RegistryObject<EntityType<CoffinEntity>> COFFIN = registerZombieEntityType(CoffinEntity::new, "coffin", 2.0f, 2.0f);
	public static final RegistryObject<EntityType<MournerZombieEntity>> MOURNER_ZOMBIE = registerZombieEntityType(MournerZombieEntity::new, "mourner_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<NobleZombieEntity>> NOBLE_ZOMBIE = registerZombieEntityType(NobleZombieEntity::new, "noble_zombie", 0.8f, 1.9f);
	public static final RegistryObject<EntityType<SundayEditionZombieEntity>> SUNDAY_EDITION_ZOMBIE = registerZombieEntityType(SundayEditionZombieEntity::new, "sunday_edition_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<JackInBoxZombieEntity>> JACK_IN_BOX_ZOMBIE = registerZombieEntityType(JackInBoxZombieEntity::new, "jack_in_box_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<PogoZombieEntity>> POGO_ZOMBIE = registerZombieEntityType(PogoZombieEntity::new, "pogo_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<YetiZombieEntity>> YETI_ZOMBIE = registerZombieEntityType(YetiZombieEntity::new, "yeti_zombie", 1.0f, 2.6f);
	public static final RegistryObject<EntityType<DiggerZombieEntity>> DIGGER_ZOMBIE = registerZombieEntityType(DiggerZombieEntity::new, "digger_zombie", 0.8f, 0.2f);
	public static final RegistryObject<EntityType<BalloonZombieEntity>> BALLOON_ZOMBIE = registerZombieEntityType(BalloonZombieEntity::new, "balloon_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<RaZombieEntity>> RA_ZOMBIE = registerZombieEntityType(RaZombieEntity::new, "ra_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<BungeeZombieEntity>> BUNGEE_ZOMBIE = registerZombieEntityType(BungeeZombieEntity::new, "bungee_zombie", 1.2f, 2.0f);
	public static final RegistryObject<EntityType<LadderZombieEntity>> LADDER_ZOMBIE = registerZombieEntityType(LadderZombieEntity::new, "ladder_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<CatapultZombieEntity>> CATAPULT_ZOMBIE = registerZombieEntityType(CatapultZombieEntity::new, "catapult_zombie", 0.8f, 2.0f);
	public static final RegistryObject<EntityType<GargantuarEntity>> GARGANTUAR = registerZombieEntityType(GargantuarEntity::new, "gargantuar", 0.8f, 4.0f);
	public static final RegistryObject<EntityType<ImpEntity>> IMP = registerZombieEntityType(ImpEntity::new, "imp", 0.6f, 1.2f);
	public static final RegistryObject<EntityType<GigaGargantuarEntity>> GIGA_GARGANTUAR = registerZombieEntityType(GigaGargantuarEntity::new, "giga_gargantuar", 0.8f, 4.0f);
	public static final RegistryObject<EntityType<ZombotEntity>> ZOMBOT = registerZombieEntityType(ZombotEntity::new, "zombot", 2.0f, 7.5f);
	public static final RegistryObject<EntityType<EdgarZombotEntity>> EDGAR_ZOMBOT = registerZombieEntityType(EdgarZombotEntity::new, "edgar_zombot", 3f, 10.0f);
	public static final RegistryObject<EntityType<PeaShooterZombieEntity>> PEASHOOTER_ZOMBIE = registerZombieEntityType(PeaShooterZombieEntity::new, "peashooter_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<GatlingPeaZombieEntity>> GATLINGPEA_ZOMBIE = registerZombieEntityType(GatlingPeaZombieEntity::new, "gatlingpea_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<SquashZombieEntity>> SQUASH_ZOMBIE = registerZombieEntityType(SquashZombieEntity::new, "squash_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<JalapenoZombieEntity>> JALAPENO_ZOMBIE = registerZombieEntityType(JalapenoZombieEntity::new, "jalapeno_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<WallNutZombieEntity>> WALLNUT_ZOMBIE = registerZombieEntityType(WallNutZombieEntity::new, "wallnut_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<TallNutZombieEntity>> TALLNUT_ZOMBIE = registerZombieEntityType(TallNutZombieEntity::new, "tallnut_zombie", 0.8f, 1.98f);
	public static final RegistryObject<EntityType<GigaTombStoneEntity>> GIGA_TOMB_STONE = registerZombieEntityType(GigaTombStoneEntity::new, "giga_tomb_stone", 0.8f, 1.6f);

	/**
	 * plants
	 */
	public static final RegistryObject<EntityType<PeaShooterEntity>> PEA_SHOOTER = registerPlantEntityType(PeaShooterEntity::new, "pea_shooter", 0.7f, 1.3f);
	public static final RegistryObject<EntityType<SunFlowerEntity>> SUN_FLOWER = registerPlantEntityType(SunFlowerEntity::new, "sun_flower", 0.8f, 1.3f);
	public static final RegistryObject<EntityType<CherryBombEntity>> CHERRY_BOMB = registerPlantEntityType(CherryBombEntity::new, "cherry_bomb", 0.9f, 1.0f);
	public static final RegistryObject<EntityType<WallNutEntity>> WALL_NUT = registerPlantEntityType(WallNutEntity::new, "wall_nut", 0.9f, 1.1f);
	public static final RegistryObject<EntityType<PotatoMineEntity>> POTATO_MINE = registerPlantEntityType(PotatoMineEntity::new, "potato_mine", 0.6f, 0.4f);
	public static final RegistryObject<EntityType<SnowPeaEntity>> SNOW_PEA = registerPlantEntityType(SnowPeaEntity::new, "snow_pea", 0.7f, 1.3f);
	public static final RegistryObject<EntityType<ChomperEntity>> CHOMPER = registerPlantEntityType(ChomperEntity::new, "chomper", 0.9f, 1.9f);
	public static final RegistryObject<EntityType<RepeaterEntity>> REPEATER = registerPlantEntityType(RepeaterEntity::new, "repeater", 0.7f, 1.3f);
	public static final RegistryObject<EntityType<LilyPadEntity>> LILY_PAD = registerPlantEntityType(LilyPadEntity::new, "lily_pad", 0.875f, 0.2f);
	public static final RegistryObject<EntityType<SquashEntity>> SQUASH = registerPlantEntityType(SquashEntity::new, "squash", 0.9f, 1.5f);
	public static final RegistryObject<EntityType<ThreePeaterEntity>> THREE_PEATER = registerPlantEntityType(ThreePeaterEntity::new, "three_peater", 0.9f, 1.7f);
	public static final RegistryObject<EntityType<TangleKelpEntity>> TANGLE_KELP = registerPlantEntityType(TangleKelpEntity::new, "tangle_kelp", 0.6f, 1.0f);
	public static final RegistryObject<EntityType<JalapenoEntity>> JALAPENO = registerPlantEntityType(JalapenoEntity::new, "jalapeno", 0.7f, 1.5f);
	public static final RegistryObject<EntityType<SpikeWeedEntity>> SPIKE_WEED = registerPlantEntityType(SpikeWeedEntity::new, "spike_weed", 0.95f, 0.4f);
	public static final RegistryObject<EntityType<TorchWoodEntity>> TORCH_WOOD = registerPlantEntityType(TorchWoodEntity::new, "torch_wood", 0.95f, 1.5f);
	public static final RegistryObject<EntityType<TallNutEntity>> TALL_NUT = registerPlantEntityType(TallNutEntity::new, "tall_nut", 0.9f, 1.9f);
	public static final RegistryObject<EntityType<PuffShroomEntity>> PUFF_SHROOM = registerPlantEntityType(PuffShroomEntity::new, "puff_shroom", 0.5f, 0.5f);
	public static final RegistryObject<EntityType<SunShroomEntity>> SUN_SHROOM = registerPlantEntityType(SunShroomEntity::new, "sun_shroom", 0.4f, 0.4f);
	public static final RegistryObject<EntityType<FumeShroomEntity>> FUME_SHROOM = registerPlantEntityType(FumeShroomEntity::new, "fume_shroom", 0.8f, 1.25f);
	public static final RegistryObject<EntityType<GraveBusterEntity>> GRAVE_BUSTER = registerPlantEntityType(GraveBusterEntity::new, "grave_buster", 1.0f, 1.6f);
	public static final RegistryObject<EntityType<HypnoShroomEntity>> HYPNO_SHROOM = registerPlantEntityType(HypnoShroomEntity::new, "hypno_shroom", 0.7f, 1.45f);
	public static final RegistryObject<EntityType<ScaredyShroomEntity>> SCAREDY_SHROOM = registerPlantEntityType(ScaredyShroomEntity::new, "scaredy_shroom", 0.6f, 1.6f);
	public static final RegistryObject<EntityType<IceShroomEntity>> ICE_SHROOM = registerPlantEntityType(IceShroomEntity::new, "ice_shroom", 0.85f, 1.35f);
	public static final RegistryObject<EntityType<DoomShroomEntity>> DOOM_SHROOM = registerPlantEntityType(DoomShroomEntity::new, "doom_shroom", 0.8f, 1.5f);
	public static final RegistryObject<EntityType<SeaShroomEntity>> SEA_SHROOM = registerPlantEntityType(SeaShroomEntity::new, "sea_shroom", 0.5f, 0.8f);
	public static final RegistryObject<EntityType<SplitPeaEntity>> SPLIT_PEA = registerPlantEntityType(SplitPeaEntity::new, "split_pea", 0.7f, 1.3f);
	public static final RegistryObject<EntityType<CoffeeBeanEntity>> COFFEE_BEAN = registerPlantEntityType(CoffeeBeanEntity::new, "coffee_bean", 0.6f, 0.8f);
	public static final RegistryObject<EntityType<MariGoldEntity>> MARIGOLD = registerPlantEntityType(MariGoldEntity::new, "marigold", 0.8f, 1.6f);
	public static final RegistryObject<EntityType<GatlingPeaEntity>> GATLING_PEA = registerPlantEntityType(GatlingPeaEntity::new, "gatling_pea", 0.7f, 1.3f);
	public static final RegistryObject<EntityType<TwinSunFlowerEntity>> TWIN_SUNFLOWER = registerPlantEntityType(TwinSunFlowerEntity::new, "twin_sunflower", 0.9f, 1.3f);
	public static final RegistryObject<EntityType<PumpkinEntity>> PUMPKIN = registerPlantEntityType(PumpkinEntity::new, "pumpkin", 1.0f, 0.5f);
	public static final RegistryObject<EntityType<PlanternEntity>> PLANTERN = registerPlantEntityType(PlanternEntity::new, "plantern", 0.75f, 1.7f);
	public static final RegistryObject<EntityType<MagnetShroomEntity>> MAGNET_SHROOM = registerPlantEntityType(MagnetShroomEntity::new, "magnet_shroom", 0.5f, 1.3f);
	public static final RegistryObject<EntityType<CatTailEntity>> CAT_TAIL = registerPlantEntityType(CatTailEntity::new, "cat_tail", 0.8f, 0.6f);
	public static final RegistryObject<EntityType<StrangeCatEntity>> STRANGE_CAT = registerPlantEntityType(StrangeCatEntity::new, "strange_cat", 0.8f, 1.0f);
	public static final RegistryObject<EntityType<StarFruitEntity>> STARFRUIT = registerPlantEntityType(StarFruitEntity::new, "starfruit", 0.9f, 0.5f);
	public static final RegistryObject<EntityType<AngelStarFruitEntity>> ANGEL_STARFRUIT = registerPlantEntityType(AngelStarFruitEntity::new, "angel_starfruit", 0.9f, 0.5f);
	public static final RegistryObject<EntityType<CactusEntity>> CACTUS = registerPlantEntityType(CactusEntity::new, "cactus", 0.8f, 2.0f);
	public static final RegistryObject<EntityType<BloverEntity>> BLOVER = registerPlantEntityType(BloverEntity::new, "blover", 0.5f, 1.5f);
	public static final RegistryObject<EntityType<GloomShroomEntity>> GLOOM_SHROOM = registerPlantEntityType(GloomShroomEntity::new, "gloom_shroom", 0.9f, 0.8f);
	public static final RegistryObject<EntityType<GoldMagnetEntity>> GOLD_MAGNET = registerPlantEntityType(GoldMagnetEntity::new, "gold_magnet", 0.5f, 1.3f);
	public static final RegistryObject<EntityType<GoldLeafEntity>> GOLD_LEAF = registerPlantEntityType(GoldLeafEntity::new, "gold_leaf", 0.6f, 1.0f);
	public static final RegistryObject<EntityType<GoldBloomEntity>> GOLD_BLOOM = registerPlantEntityType(GoldBloomEntity::new, "gold_bloom", 0.6f, 0.6f);
	public static final RegistryObject<EntityType<FlowerPotEntity>> FLOWER_POT = registerPlantEntityType(FlowerPotEntity::new, "flower_pot", 0.75f, 0.43f);
	public static final RegistryObject<EntityType<CabbagePultEntity>> CABBAGE_PULT = registerPlantEntityType(CabbagePultEntity::new, "cabbage_pult", 0.8f, 1.0f);
	public static final RegistryObject<EntityType<KernelPultEntity>> KERNEL_PULT = registerPlantEntityType(KernelPultEntity::new, "kernel_pult", 0.8f, 1.0f);
	public static final RegistryObject<EntityType<ButterPultEntity>> BUTTER_PULT = registerPlantEntityType(ButterPultEntity::new, "butter_pult", 0.8f, 1.0f);
	public static final RegistryObject<EntityType<GarlicEntity>> GARLIC = registerPlantEntityType(GarlicEntity::new, "garlic", 0.8f, 1.2f);
	public static final RegistryObject<EntityType<UmbrellaLeafEntity>> UMBRELLA_LEAF = registerPlantEntityType(UmbrellaLeafEntity::new, "umbrella_leaf", 0.7f, 1.2f);
	public static final RegistryObject<EntityType<MelonPultEntity>> MELON_PULT = registerPlantEntityType(MelonPultEntity::new, "melon_pult", 0.9f, 1.0f);
	public static final RegistryObject<EntityType<WinterMelonEntity>> WINTER_MELON = registerPlantEntityType(WinterMelonEntity::new, "winter_melon", 0.9f, 1.0f);
	public static final RegistryObject<EntityType<BambooLordEntity>> BAMBOO_LORD = registerPlantEntityType(BambooLordEntity::new, "bamboo_lord", 0.6f, 1.0f);
	public static final RegistryObject<EntityType<IcebergLettuceEntity>> ICEBERG_LETTUCE = registerPlantEntityType(IcebergLettuceEntity::new, "iceberg_lettuce", 0.6f, 0.6f);
	public static final RegistryObject<EntityType<SpikeRockEntity>> SPIKE_ROCK = registerPlantEntityType(SpikeRockEntity::new, "spike_rock", 0.95f, 0.4f);
	public static final RegistryObject<EntityType<BonkChoyEntity>> BONK_CHOY = registerPlantEntityType(BonkChoyEntity::new, "bonk_choy", 0.6f, 0.9f);
	public static final RegistryObject<EntityType<ImitaterEntity>> IMITATER = registerPlantEntityType(ImitaterEntity::new, "imitater", 0.7f, 1.25f);
	public static final RegistryObject<EntityType<CobCannonEntity>> COB_CANNON = registerPlantEntityType(CobCannonEntity::new, "cob_cannon", 1.25f, 1.0f);
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
		// Renderer registration has been moved to ClientRegister#onRegisterRenderers
		// via the Forge EntityRenderersEvent.RegisterRenderers MOD-bus event (1.19).
		// The event subscribes on client-only dist (same @Mod.EventBusSubscriber already covers Dist).
	}
	
	@SubscribeEvent
	public static void addEntityAttributes(EntityAttributeCreationEvent ev) {
		PlantType.initPlants();
    	ZombieType.initZombies();
		PlantType.postInitPlants();
		ZombieType.postInitZombies();
		//init all plants' attributes.
		for(IPlantType p : PlantType.getPlants()) {
			p.getEntityType().ifPresent(obj -> {
		        ev.put(obj, PVZPlantEntity.createPlantAttributes());
			});
		}
		//init all zombies' attributes.
		for(IZombieType z : ZombieType.getZombies()) {
			z.getEntityType().ifPresent(obj -> {
				ev.put(obj, PVZZombieEntity.createZombieAttributes());
			});
		}
		Arrays.asList(
				CRAZY_DAVE.get(),
				FOODIE_ZOMBIE.get()
		).forEach(obj -> {
			ev.put(obj, PathfinderMob.createMobAttributes().build());
		});
		ev.put(PANNEY.get(),PennyEntity.createPennyAttributes());
		ev.put(MOOBLOOM.get(),MooBloom.createAttributes().build());
	}

	private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(EntityFactory<T> factory, String name, MobCategory classification){
		return ENTITY_TYPES.register(name, () -> {return EntityType.Builder.of(factory, classification).build(StringUtil.prefix(name).toString());});
	}
	
	private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(EntityFactory<T> factory, String name, MobCategory classification, float w, float h){
		return ENTITY_TYPES.register(name, () -> {return EntityType.Builder.of(factory, classification).sized(w, h).build(StringUtil.prefix(name).toString());});
	}

	private static <T extends PVZZombieEntity> RegistryObject<EntityType<T>> registerZombieEntityType(EntityFactory<T> factory, String name, float w, float h){
		return ENTITY_TYPES.register(name, () -> {return EntityType.Builder.of(factory, PVZMobCategorys.PVZ_ZOMBIE).sized(w, h).build(StringUtil.prefix(name).toString());});
	}

	private static <T extends PVZPlantEntity> RegistryObject<EntityType<T>> registerPlantEntityType(EntityFactory<T> factory, String name, float w, float h){
		return ENTITY_TYPES.register(name, () -> {return EntityType.Builder.of(factory, PVZMobCategorys.PVZ_PLANT).sized(w, h).build(StringUtil.prefix(name).toString());});
	}
	
}