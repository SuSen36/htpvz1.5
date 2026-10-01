package com.hungteen.pvz.client.challenge;

import com.hungteen.pvz.utils.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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

	public static void update(int challengeId, UUID challengeBarId, ResourceLocation resource, int totalWaves, int currentWave, BitSet bigWaves, BitSet givenUpWaves, boolean bossChallenge, BlockPos center, long dayTimeOverride, float rainLevelOverride, float thunderLevelOverride) {
		BARS.put(challengeId, new BarData(challengeBarId, resource, totalWaves, currentWave, (BitSet) bigWaves.clone(), (BitSet) givenUpWaves.clone(), bossChallenge, center, dayTimeOverride, rainLevelOverride, thunderLevelOverride));
	}

	public static long getLocalPlayerDayTimeOverride() {
		final BarData data = findLocalPlayerBar();
		return data == null ? -1L : data.dayTimeOverride;
	}

	public static float getLocalPlayerRainLevelOverride() {
		final BarData data = findLocalPlayerBar();
		return data == null ? -1.0F : data.rainLevelOverride;
	}

	public static float getLocalPlayerThunderLevelOverride() {
		final BarData data = findLocalPlayerBar();
		return data == null ? -1.0F : data.thunderLevelOverride;
	}

	@Nullable
	private static BarData findLocalPlayerBar() {
		BarData result = null;
		final Player player = Minecraft.getInstance().player;
		if(player != null) {
			final int range = ConfigUtil.getRaidRange();
			for (BarData data : BARS.values()) {
				if(Math.abs(player.getX() - data.center.getX()) <= range
						&& Math.abs(player.getY() - data.center.getY()) <= range
						&& Math.abs(player.getZ() - data.center.getZ()) <= range) {
					result = data;
					break;
				}
			}
		}
		return result;
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
		private final BlockPos center;
		private final long dayTimeOverride;
		private final float rainLevelOverride;
		private final float thunderLevelOverride;

		public BarData(UUID barUuid, ResourceLocation resource, int totalWaves, int currentWave, BitSet bigWaves, BitSet givenUpWaves, boolean bossChallenge, BlockPos center, long dayTimeOverride, float rainLevelOverride, float thunderLevelOverride) {
			this.barUuid = barUuid;
			this.resource = resource;
			this.totalWaves = totalWaves;
			this.currentWave = currentWave;
			this.bigWaves = bigWaves;
			this.givenUpWaves = givenUpWaves;
			this.bossChallenge = bossChallenge;
			this.center = center;
			this.dayTimeOverride = dayTimeOverride;
			this.rainLevelOverride = rainLevelOverride;
			this.thunderLevelOverride = thunderLevelOverride;
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