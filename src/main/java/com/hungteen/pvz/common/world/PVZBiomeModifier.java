package com.hungteen.pvz.common.world;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.utils.StringUtil;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PVZBiomeModifier implements BiomeModifier {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, PVZMod.MOD_ID);
    public static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = BIOME_MODIFIER.register("mob_spawns", () -> Codec.unit(PVZBiomeModifier::new));

    public static final TagKey<Biome> UNABLE_MOOBLOOM_SPAWNING = TagKey.create(Registry.BIOME_REGISTRY, StringUtil.prefix("unable_moobloom_spawning"));
    public static final TagKey<Biome> EXTRA_MOOBLOOM_SPAWNING = TagKey.create(Registry.BIOME_REGISTRY, StringUtil.prefix("extra_moobloom_spawning"));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            if (!biome.is(UNABLE_MOOBLOOM_SPAWNING)) {
                if (PVZBiomes.ZEN_GARDEN.getKey() != null && biome.is(PVZBiomes.ZEN_GARDEN.getKey())) {
                    addMooBloomSpawn(builder, 5);
                } else if (biome.is(Biomes.SUNFLOWER_PLAINS)) {
                    addMooBloomSpawn(builder, 50);
                } else if (builder.getMobSpawnSettings().getEntityTypes().contains(EntityType.COW)) {
                    addMooBloomSpawn(builder, biome.is(EXTRA_MOOBLOOM_SPAWNING) ? 15 : 5);
                }
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    private static void addMooBloomSpawn(ModifiableBiomeInfo.BiomeInfo.Builder builder, int weight) {
        builder.getMobSpawnSettings().getSpawner(MobCategory.CREATURE)
                .add(new MobSpawnSettings.SpawnerData(EntityRegister.MOOBLOOM.get(), weight, 1, 2));
    }
}
