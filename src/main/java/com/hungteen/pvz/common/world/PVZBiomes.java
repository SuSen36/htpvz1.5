package com.hungteen.pvz.common.world;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.common.block.BlockRegister;
import com.hungteen.pvz.common.world.feature.PVZFeatures;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PVZBiomes {

	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, PVZMod.MOD_ID);

	public static final RegistryObject<Biome> ZEN_GARDEN = BIOMES.register("zen_garden", PVZBiomes::zenGarden);

	public static Holder<PlacedFeature> NUT_TREE_PF = null;
	public static Holder<PlacedFeature> CHOMPER_PATCH_PF = null;
	private static boolean features;

	private static void checkFeatures() {
		if (features) {
			return;
		}
		CHOMPER_PATCH_PF = PlacementUtils.register("pvz:chomper_patch", PVZFeatures.CHOMPER_PATCH,
				InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), RarityFilter.onAverageOnceEvery(48));
		NUT_TREE_PF = PlacementUtils.register("pvz:nut_tree", PVZFeatures.NUT_TREE,
				VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.05F, 1), BlockRegister.NUT_SAPLING.get()));
		// 路灯花是唯一跨群系共享的 pvz 特征，插入位置必须处处一致：只能由 biome modifier 追加（含 pvz:zen_garden），不能在 zen_garden 里内联 addFeature，
		// 否则它在禅境花园位于 vegetal_decoration 中部、在沼泽却由修饰器追加于该步末尾，同一特征两种相对顺序会让 FeatureSorter 抛 Feature order cycle found
		PlacementUtils.register("pvz:plantern", PVZFeatures.PLANTERN,
				RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		features = true;
	}

	private static Biome zenGarden() {
		checkFeatures();

		MobSpawnSettings.Builder mobSpawnBuilder = new MobSpawnSettings.Builder();

		BiomeGenerationSettings.Builder biomeGenBuilder = new BiomeGenerationSettings.Builder();
		BiomeDefaultFeatures.addDefaultCarversAndLakes(biomeGenBuilder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(biomeGenBuilder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(biomeGenBuilder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenBuilder);
		BiomeDefaultFeatures.addDefaultSprings(biomeGenBuilder);
		BiomeDefaultFeatures.addPlainGrass(biomeGenBuilder);
		biomeGenBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUNFLOWER);
		BiomeDefaultFeatures.addPlainVegetation(biomeGenBuilder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomeGenBuilder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenBuilder);
		BiomeDefaultFeatures.addSurfaceFreezing(biomeGenBuilder);
		biomeGenBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CHOMPER_PATCH_PF);
		biomeGenBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NUT_TREE_PF);

		return new Biome.BiomeBuilder()
				.precipitation(Biome.Precipitation.RAIN)
				.temperature(0.8F)
				.downfall(0.4F)
				.specialEffects(new BiomeSpecialEffects.Builder()
						.fogColor(12638463)
						.waterColor(4159204)
						.waterFogColor(329011)
						.skyColor(7907327)
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
						.build())
				.mobSpawnSettings(mobSpawnBuilder.build())
				.generationSettings(biomeGenBuilder.build())
				.build();
	}
}
