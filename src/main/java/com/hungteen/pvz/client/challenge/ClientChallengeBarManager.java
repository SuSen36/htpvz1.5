package com.hungteen.pvz.client.challenge;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ClientChallengeBarManager {

	private static final Map<Integer, BarData> BARS = new HashMap<>();

	public static void update(int challengeId, UUID challengeBarId, ResourceLocation resource, int totalWaves, int currentWave, BitSet bigWaves, BitSet givenUpWaves, boolean bossChallenge) {
		BARS.put(challengeId, new BarData(challengeBarId, resource, totalWaves, currentWave, (BitSet) bigWaves.clone(), (BitSet) givenUpWaves.clone(), bossChallenge));
	}

	@Nullable
	public static BarData getByBarUuid(UUID challengeBarId) {
		for (BarData data : BARS.values()) {
			if (data.barUuid.equals(challengeBarId)) {
				return data;
			}
		}
		return null;
	}

	public static void remove(int challengeId) {
		BARS.remove(challengeId);
	}

	public static void clear() {
		BARS.clear();
	}

	public static class BarData {

		private final UUID barUuid;
		private final ResourceLocation resource;
		private final int totalWaves;
		private final int currentWave;
		private final BitSet bigWaves;
		private final BitSet givenUpWaves;
		private final boolean bossChallenge;

		public BarData(UUID barUuid, ResourceLocation resource, int totalWaves, int currentWave, BitSet bigWaves, BitSet givenUpWaves, boolean bossChallenge) {
			this.barUuid = barUuid;
			this.resource = resource;
			this.totalWaves = totalWaves;
			this.currentWave = currentWave;
			this.bigWaves = bigWaves;
			this.givenUpWaves = givenUpWaves;
			this.bossChallenge = bossChallenge;
		}

		public ResourceLocation getResource() {
			return this.resource;
		}

		public int getTotalWaves() {
			return this.totalWaves;
		}

		public int getCurrentWave() {
			return this.currentWave;
		}

		public boolean isBigWave(int wave) {
			return this.bigWaves.get(wave);
		}

		public boolean isGivenUp(int wave) {
			return this.givenUpWaves.get(wave);
		}

		public boolean isBossChallenge() {
			return this.bossChallenge;
		}
	}
}
