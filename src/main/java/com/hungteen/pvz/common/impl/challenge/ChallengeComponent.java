package com.hungteen.pvz.common.impl.challenge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.raid.*;
import com.hungteen.pvz.common.datapack.ChallengeTagTypeLoader;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.world.challenge.ChallengeManager;
import com.hungteen.pvz.utils.others.WeightList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.Map.Entry;

public class ChallengeComponent implements IChallengeComponent {

	public static final String NAME = "default";
	private static final int DEFAULT_SEED_WEIGHT = 100;
	private static final int DEFAULT_PREPARE_TICK = 20;
	private final List<IWaveComponent> waves = new ArrayList<>();
	private final List<IRewardComponent> rewards = new ArrayList<>();
	private final Set<String> tags = new HashSet<>();
	private final Set<String> dimensions = new HashSet<>();
	private final List<String> authors = new ArrayList<>();
	private final List<Pair<MutableComponent, Integer>> messages = new ArrayList<>();
	private IPlacementComponent placement;
	private ISpawnComponent bossSpawn;
	private Component title = Component.translatable("challenge.pvz.title");
	private Component winTitle = Component.translatable("challenge.pvz.win_title");
	private Component lossTitle = Component.translatable("challenge.pvz.loss_title");
	private BossEvent.BossBarColor barColor = BossEvent.BossBarColor.WHITE;
	private SoundEvent preSound = SoundRegister.READY.get();
	private SoundEvent waveSound = SoundRegister.HUGE_WAVE.get();
	private SoundEvent winSound = SoundRegister.WIN_MUSIC.get();
	private SoundEvent lossSound = SoundRegister.LOSE_MUSIC.get();
	private SoundEvent bgmSound = SoundRegister.CHALLENGE_BGM.get();
	private int prepareTick;
	private int winTick;
	private int lossTick;
	/* for trade */
	private boolean canTrade;
	private int tradePrice;
	private int tradeWeight;
	/* misc */
	private boolean showRound;
	private int recommendLevel;
	private boolean shouldCloseToCenter;
	//0 表示未配置
	private int initialSun = 0;
	private int sunLimit = 9999;
	/* seed rain */
	private WeightList<ItemStack> seedPool;
	
	@Override
	public boolean readJson(JsonObject json) {
		/* titles */
		{
			final Component text = Component.Serializer.fromJson(json.get("title"));
		    if(text != null) {
			    this.title = text;
		    }
		}
		{
			final Component text = Component.Serializer.fromJson(json.get("win_title"));
		    if(text != null) {
			    this.winTitle = text;
		    }
		}
		{
			final Component text = Component.Serializer.fromJson(json.get("loss_title"));
		    if(text != null) {
			    this.lossTitle = text;
		    }
		}
		/* authors */
		{
			final JsonArray array = GsonHelper.getAsJsonArray(json, "authors", new JsonArray());
			if(array != null) {
				for(int i = 0; i < array.size(); ++ i) {
					final JsonElement e = array.get(i);
					if(e.isJsonPrimitive()) {
						this.authors.add(e.getAsString());
					}
				}
			}
		}
		/* tags */
	{
		final JsonArray array = GsonHelper.getAsJsonArray(json, "tags", new JsonArray());
		if(array != null) {
			for(int i = 0; i < array.size(); ++ i) {
				final JsonElement e = array.get(i);
				if(e.isJsonPrimitive()) {
					this.tags.addAll(ChallengeTagTypeLoader.resolveToken(e.getAsString()));
				}
			}
		}
	}
		/* dimensions */
		{
			final JsonArray array = GsonHelper.getAsJsonArray(json, "dimensions", new JsonArray());
			if(array != null) {
				for(int i = 0; i < array.size(); ++ i) {
					final JsonElement e = array.get(i);
					if(e.isJsonPrimitive()) {
						this.dimensions.add(e.getAsString());
					}
				}
			}
		}
		/* raid cd */
		{
		    this.prepareTick = GsonHelper.getAsInt(json, "prepare_tick", DEFAULT_PREPARE_TICK);
		    this.winTick = GsonHelper.getAsInt(json, "win_tick", 400);
		    this.lossTick = GsonHelper.getAsInt(json, "loss_tick", 200);
		}
		/* bar color */
		{
			this.barColor = BossEvent.BossBarColor.byName(GsonHelper.getAsString(json, "bar_color", "red"));
		}
		{/* trade */
			this.canTrade = GsonHelper.getAsBoolean(json, "can_trade", true);
			this.tradePrice = GsonHelper.getAsInt(json, "trade_price", 100);
			this.tradeWeight = GsonHelper.getAsInt(json, "trade_weight", 100);
		}
		{/* misc */
			this.showRound = GsonHelper.getAsBoolean(json, "show_round", true);
			this.recommendLevel = GsonHelper.getAsInt(json, "recommend_level", 1);
			this.shouldCloseToCenter = GsonHelper.getAsBoolean(json, "close_to_center", true);
			this.initialSun = GsonHelper.getAsInt(json, "initial_sun", 0);
			this.sunLimit = GsonHelper.getAsInt(json, "sun_limit", 9999);
		}
		/* sounds */
		{
			JsonObject obj = GsonHelper.getAsJsonObject(json, "sounds", null);
			if(obj != null) {
				{
					final SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(GsonHelper.getAsString(obj, "pre_sound", "")));
					if(sound != null){
						this.preSound = sound;
					}
				}
				{
					final SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(GsonHelper.getAsString(obj, "wave_sound", "")));
					if(sound != null){
						this.waveSound = sound;
					}
				}
				{
					final SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(GsonHelper.getAsString(obj, "win_sound", "")));
					if(sound != null){
						this.winSound = sound;
					}
				}
				{
					final SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(GsonHelper.getAsString(obj, "loss_sound", "")));
					if(sound != null){
						this.lossSound = sound;
					}
				}
				{
					final SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(GsonHelper.getAsString(obj, "bgm_sound", "")));
					if(sound != null){
						this.bgmSound = sound;
					}
				}
			}
		}
		/* spawn placement */
		{
			this.placement = ChallengeManager.readPlacement(json, true);
		}
	    /* boss spawn */
	    {
		    final JsonObject obj = GsonHelper.getAsJsonObject(json, "boss", null);
		    if(obj != null) {
			    final ISpawnComponent spawn = ChallengeManager.getSpawnComponent(SpawnComponent.NAME);
			    if(spawn.readJson(obj)) {
				    this.bossSpawn = spawn;
			    }
		    }
	    }
		/* waves */
		JsonArray jsonWaves = GsonHelper.getAsJsonArray(json, "waves", new JsonArray());
		if(jsonWaves != null) {
			for(int i = 0; i < jsonWaves.size(); ++ i) {
			    JsonObject obj = jsonWaves.get(i).getAsJsonObject();
			    if(obj != null) {
			    	String type = GsonHelper.getAsString(obj, "type", "");
		            IWaveComponent wave = ChallengeManager.getWaveComponent(type);
		            if(! wave.readJson(obj)) {
		            	return false;
		            }
			        this.waves.add(wave);
			    }
			}
		}
	    if(this.waves.isEmpty() && this.bossSpawn == null) {
		    throw new JsonSyntaxException("Wave list cannot be empty");
	    }

	    /* rewards */
	    {
	    	JsonObject obj = GsonHelper.getAsJsonObject(json, "rewards", null);
		    if(obj != null && ! obj.entrySet().isEmpty()) {
		       for(Entry<String, JsonElement> entry : obj.entrySet()) {
		  		    final IRewardComponent tmp = ChallengeManager.getRewardComponent(entry.getKey());
		    	    if(tmp != null) {
		    		    tmp.readJson(entry.getValue());
		    		    this.rewards.add(tmp);
		    	    } else {
		    		    PVZMod.LOGGER.warn("PlacementModifier Component : Read Spawn PlacementModifier Wrongly");
		    	    }
		   	    }
		    }
	    }
	    
	    /* optional seed pool：内联条目，缺省则不限制种植也不下种子雨 */
    {
	    final JsonArray array = GsonHelper.getAsJsonArray(json, "seed_pool", null);
	    if(array != null) {
		    final WeightList<ItemStack> pool = new WeightList<>();
		    for(JsonElement element : array) {
			    if(element.isJsonObject()) {
				    final JsonObject entry = element.getAsJsonObject();
				    final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(entry, "item", "")));
				    if(item == null) {
					    throw new JsonSyntaxException("seed pool item cannot be empty or wrong format");
				    }
				    pool.addItem(new ItemStack(item), Math.max(0, GsonHelper.getAsInt(entry, "weight", DEFAULT_SEED_WEIGHT)));
			    }
		    }
		    if(! pool.isEmpty()) {
			    this.seedPool = pool;
		    }
	    }
    }

    return true;
}
	
	@Override
	public List<ISpawnComponent> getSpawns(int wavePos) {
		return this.waves.get(this.wavePos(wavePos)).getSpawns();
	}

	@Override
	public List<IWaveComponent> getWaves() {
		return waves;
	}

	@Override
	public ISpawnComponent getBossSpawn() {
		return this.bossSpawn;
	}

	@Override
	public List<IRewardComponent> getRewards() {
		return this.rewards;
	}
	
	@Override
	public List<String> getAuthors() {
		return this.authors;
	}
	
	@Override
	public boolean hasTag(String tag) {
		return this.tags.contains(tag);
	}

	@Override
	public boolean isSuitableDimension(ResourceKey<Level> type) {
		return this.dimensions.isEmpty() || this.dimensions.contains(type.location().toString());
	}

	@Override
	public int getPrepareCD(int wavePos) {
		return this.waves.isEmpty() ? this.prepareTick : this.waves.get(this.wavePos(wavePos)).getPrepareCD();
	}

	@Override
	public int getTotalWaveCount() {
		return this.waves.size();
	}
	
	@Override
	public int getWinTick() {
		return this.winTick;
	}
	
	@Override
	public int getLossTick() {
		return this.lossTick;
	}

	@Override
	public SoundEvent getPrepareSound() {
		return this.preSound;
	}

	@Override
	public SoundEvent getStartWaveSound() {
		return this.waveSound;
	}

	@Override
	public SoundEvent getWinSound() {
		return this.winSound;
	}

	@Override
	public SoundEvent getLossSound() {
		return this.lossSound;
	}

	@Override
	public SoundEvent getBgmSound() {
		return this.bgmSound;
	}

	@Override
	public Optional<Music> getBgmMusic() {
		//参数对齐 Musics.END_BOSS：挑战 BGM 由原版 MusicManager 无缝循环播放
		return Optional.of(new Music(this.bgmSound, 0, 0, true));
	}
	
	@Override
	public IPlacementComponent getPlacement(int wavePos) {
		final IPlacementComponent p = this.waves.get(this.wavePos(wavePos)).getPlacement();
		return p == null ? this.placement : p;
	}
	
	@Override
	public Component getTitle() {
		return this.title;
	}
	
	@Override
	public Component getWinTitle() {
		return this.winTitle;
	}
	
	@Override
	public Component getLossTitle() {
		return this.lossTitle;
	}
	
	@Override
	public BossEvent.BossBarColor getBarColor() {
		return this.barColor;
	}
	
	private int wavePos(int pos) {
		return Mth.clamp(pos, 0, this.waves.size() - 1);
	}

	@Override
	public MutableComponent getChallengeName(){
		final ResourceLocation resourceLocation = ChallengeManager.getResourceByChallenge(this);
		return Component.translatable("challenge." + resourceLocation.getNamespace() + "." + resourceLocation.getPath() + ".name");
	}

	@Override
	public void setMessages(List<Pair<MutableComponent, Integer>> list) {
		this.messages.clear();
		list.forEach(p -> this.messages.add(p));
	}

	@Override
	public List<Pair<MutableComponent, Integer>> getMessages(){
		return Collections.unmodifiableList(this.messages);
	}

	@Override
	public int getRecommendLevel() {
		return recommendLevel;
	}

	@Override
	public int getInitialSun() {
		return this.initialSun;
	}

	@Override
	public int getSunLimit() {
		return this.sunLimit;
	}

	@Override
	public boolean canTrade() {
		return canTrade;
	}

	@Override
	public int getTradeWeight() {
		return tradeWeight;
	}

	@Override
	public int getTradePrice() {
		return tradePrice;
	}

	@Override
	public boolean showRoundTitle() {
		return this.showRound;
	}

	@Override
	public boolean shouldCloseToCenter() {
		return this.shouldCloseToCenter;
	}

	@Override
	public WeightList<ItemStack> getSeedPool() {
		return this.seedPool;
	}

}