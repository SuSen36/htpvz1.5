package com.hungteen.pvz.common.world.challenge;

import com.google.common.collect.Sets;
import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.events.RaidEvent;
import com.hungteen.pvz.api.interfaces.IChallenge;
import com.hungteen.pvz.api.raid.IChallengeComponent;
import com.hungteen.pvz.api.raid.ISpawnComponent;
import com.hungteen.pvz.api.raid.IWaveComponent;
import com.hungteen.pvz.common.advancement.trigger.ChallengeTrigger;
import com.hungteen.pvz.common.capability.CapabilityHandler;
import com.hungteen.pvz.common.capability.level.PVZFogCapability;
import com.hungteen.pvz.common.entity.AbstractPAZEntity;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.ai.goal.ChallengeMoveGoal;
import com.hungteen.pvz.common.entity.misc.drop.SeedPacketEntity;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.item.spawn.card.PlantCardItem;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.common.network.PVZFogPacket;
import com.hungteen.pvz.common.network.PVZPacketHandler;
import com.hungteen.pvz.common.network.toclient.ChallengeBarPacket;
import com.hungteen.pvz.common.network.toclient.ConveyorBeltPacket;
import com.hungteen.pvz.common.world.PVZFog;import com.hungteen.pvz.utils.ConfigUtil;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.PlayerUtil;
import com.hungteen.pvz.utils.enums.Resources;
import com.hungteen.pvz.utils.others.WeightList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

public class Challenge implements IChallenge {

	private static final Component CHALLENGE_NAME_COMPONENT = Component.translatable("event.minecraft.raid");
	private static final Component CHALLENGE_WARN = Component.translatable("challenge.pvz.too_far_away").withStyle(ChatFormatting.RED);

	private static final int FOG_RECOVER_TICK = 480;
	private static final int FOG_LIFE_TICK = 1200;
	private static final double FOG_STRENGTH = 1.5D;
	private static final double FOG_RANGE = 18.0D;
	private static final String TAG_FOG = "fog";
	private static final String TAG_SEED_RAIN = "seed_rain";
	private static final String TAG_CONVEYOR = "conveyor";
	private static final String TAG_BOWLING = "bowling";
	private static final String TAG_DAY = "day";
	private static final String TAG_NIGHT = "night";
	private static final String TAG_RAIN = "rain";
	private static final String TAG_THUNDER = "thunder";
	private static final String TAG_CLEAR = "clear";
	private static final long DAY_OVERRIDE_TIME = 6000L;
	private static final long NIGHT_OVERRIDE_TIME = 18000L;
	//带上最多10张卡，首张200cs后出现，之后按带上张数分档补卡（原版数值单位为cs，换算tick需除以5）
	private static final int CONVEYOR_MAX_CARDS = 10;
	private static final int CONVEYOR_INITIAL_DELAY = 40;
	private static final int MAX_ZOMBIES_IN_WAVE = 50;
	private static final int SWITCH_INTERVAL = 5;
	//对齐pvz2D mHugeWaveCountDown：旗帜波刷怪前750cs（7.5秒）红字预警
	private static final int WAVE_WARNING_TICK = 150;
	//Board.cpp:5398 红字后25cs播吼声；Board.cpp:5341 末波刷怪后60cs播finalwave
	private static final int HUGE_WAVE_ROAR_DELAY = 5;
	private static final int FINAL_WAVE_SOUND_DELAY = 12;
	//对齐pvz2D StartReadySetPlant.reanim总时长1830ms（约37tick）：5+27+5
	private static final int READY_TITLE_STAY_TICK = 27;
	//减速与玩家伤害减半已下放至PVZZombieEntity，由僵尸实体依据isInChallengeRange()自管理
	//BGM 随 ChallengeBarPacket 下发，由客户端灾变式 looping 声音持有，关闭原版 boss 音乐机制
	private final ServerBossEvent challengeBar = (ServerBossEvent)(new ServerBossEvent(CHALLENGE_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setPlayBossMusic(false));
	private final int id;//unique specify id.
	public final ServerLevel world;
	public final ResourceLocation resource;//res to read raid component.
	protected IChallengeComponent challenge;
	protected BlockPos center;//raid center block position.
	protected Status status = Status.PREPARE;
	protected int tick = 0;
	protected int totalTime = 0;
	protected int stopTick = 0;
	protected int currentWave = 0;
	protected int waveSwitchThreshold = 0;
	protected int waveStartThreat = 0;
	//波次超时且未召唤出任何僵尸时置位，客户端对应波次显示白旗（对齐 htpvz2 Invasion.trySwitchWave）
	protected BitSet givenUpWaves = new BitSet();
	//boss 挑战开局生成的 boss 实体（对齐 pvz1 5-10）
	protected UUID bossId;
	protected Set<Entity> raiders = new HashSet<>();
	protected Set<UUID> heroes = new HashSet<>();
	private boolean firstTick = false;
	private boolean warningSent = false;
	private int finalWaveSoundDelay = 0;
	private int currentMaxLevel = 0;
	private int fogRecoverDelay = 0;
	//玩家真实阳光在进入范围时快照于此，SUN_NUM槽位在挑战期间承载挑战余额
	private final Map<UUID, SunSession> sunSessions = new HashMap<>();
	//每玩家一条独立带面，取卡与后续种植都不消耗阳光
	private final Map<UUID, ConveyorBelt> conveyorBelts = new HashMap<>();



	public Challenge(int id, ServerLevel world, ResourceLocation res, BlockPos pos) {
		this.id = id;
		this.world = world;
		this.resource = res;
		this.center = pos;
	}

	public Challenge(ServerLevel world, CompoundTag nbt) {
		this.world = world;
		this.id = nbt.getInt("challenge_id");
		this.status = Status.values()[nbt.getInt("challenge_status")];
		this.resource = new ResourceLocation(nbt.getString("challenge_resource"));
		this.tick = nbt.getInt("challenge_tick");
		this.totalTime = nbt.getInt("total_time");
		this.stopTick = nbt.getInt("stop_tick");
		this.currentWave = nbt.getInt("current_wave");
		this.waveSwitchThreshold = nbt.getInt("wave_switch_threshold");
		this.waveStartThreat = nbt.getInt("wave_start_threat");
		if(nbt.contains("given_up_waves")) {
			this.givenUpWaves = BitSet.valueOf(nbt.getLongArray("given_up_waves"));
		}
		this.firstTick = nbt.getBoolean("first_tick");
		this.warningSent = nbt.getBoolean("warning_sent");
		this.finalWaveSoundDelay = nbt.getInt("final_wave_sound_delay");
		this.fogRecoverDelay = nbt.getInt("fog_recover_delay");
		{// for raid center position.
			CompoundTag tmp = nbt.getCompound("center_pos");
			this.center = new BlockPos(tmp.getInt("pos_x"), tmp.getInt("pos_y"), tmp.getInt("pos_z"));
		}
		if(nbt.contains("boss_id")) {
			this.bossId = NbtUtils.loadUUID(nbt.get("boss_id"));
		}
		{// for raiders entity id.
			ListTag list = nbt.getList("raiders", 11);
            for (Tag tag : list) {
                final Entity entity = world.getEntity(NbtUtils.loadUUID(tag));
                if (entity != null) {
                    this.raiders.add(entity);
                }
            }
		}
		{// for heroes uuid.
			ListTag list = nbt.getList("heroes", 11);
            for (Tag tag : list) {
                this.heroes.add(NbtUtils.loadUUID(tag));
            }
		}
		if(nbt.contains("sun_sessions")) {
			ListTag list = nbt.getList("sun_sessions", 10);
			for(Tag tag : list) {
				final CompoundTag tmp = (CompoundTag) tag;
				this.sunSessions.put(NbtUtils.loadUUID(tmp.get("player_uuid")),
						new SunSession(tmp.getInt("original_sun"), tmp.getInt("challenge_sun"), tmp.getBoolean("exchanged")));
			}
		}
	}

	public void save(CompoundTag nbt) {
		nbt.putInt("challenge_id", this.id);
		nbt.putInt("challenge_status", this.status.ordinal());
		nbt.putString("challenge_resource", this.resource.toString());
		nbt.putInt("challenge_tick", this.tick);
		nbt.putInt("total_time", this.totalTime);
		nbt.putInt("stop_tick", this.stopTick);
		nbt.putInt("current_wave", this.currentWave);
		nbt.putInt("wave_switch_threshold", this.waveSwitchThreshold);
		nbt.putInt("wave_start_threat", this.waveStartThreat);
		nbt.putLongArray("given_up_waves", this.givenUpWaves.toLongArray());
		nbt.putBoolean("first_tick", this.firstTick);
		nbt.putBoolean("warning_sent", this.warningSent);
		nbt.putInt("final_wave_sound_delay", this.finalWaveSoundDelay);
		nbt.putInt("fog_recover_delay", this.fogRecoverDelay);
		{// for raid center position.
			CompoundTag tmp = new CompoundTag();
			tmp.putInt("pos_x", this.center.getX());
		    tmp.putInt("pos_y", this.center.getY());
			tmp.putInt("pos_z", this.center.getZ());
			nbt.put("center_pos", tmp);
		}
		if(this.bossId != null) {
			nbt.put("boss_id", NbtUtils.createUUID(this.bossId));
		}
		{// for raiders entity id.
			ListTag list = new ListTag();
			for(Entity entity : this.raiders) {
				list.add(NbtUtils.createUUID(entity.getUUID()));
			}
			nbt.put("raiders", list);
		}
		{// for heroes uuid.
			ListTag list = new ListTag();
			for(UUID uuid : this.heroes) {
				list.add(NbtUtils.createUUID(uuid));
			}
			nbt.put("heroes", list);
		}
		{// for isolated challenge sun sessions.
			ListTag list = new ListTag();
			this.sunSessions.forEach((uuid, session) -> {
				final Player online = this.world.getPlayerByUUID(uuid);
				if(session.exchanged && online != null) {
					session.challengeSun = PlayerUtil.getResource(online, Resources.SUN_NUM);
				}
				final CompoundTag tmp = new CompoundTag();
				tmp.put("player_uuid", NbtUtils.createUUID(uuid));
				tmp.putInt("original_sun", session.originalSun);
				tmp.putInt("challenge_sun", session.challengeSun);
				tmp.putBoolean("exchanged", session.exchanged);
				list.add(tmp);
			});
			nbt.put("sun_sessions", list);
		}
	}

	/**
	 * {@link PVZChallengeData#tick()}
	 */
	public void tick() {
		/* skip tick */
		if(this.isRemoving() || this.world.players().isEmpty()) {
			return ;
		}
		/* not allow to be peaceful */
		if(this.world.getDifficulty() == Difficulty.PEACEFUL) {
			this.remove();
			return ;
		}
		/* is raid component valid */
		if(this.getRaidComponent() == null) {
			this.remove();
			PVZMod.LOGGER.warn("Challenge Tick Error : Where is the challenge component ?");
			return ;
		}
		++ this.tick;
		++ this.totalTime;
		this.tickBar();
		if(this.isStopping()) {
			/* has stopped */
			if(++ this.stopTick >= ConfigUtil.getRaidWaitTime()) {
				this.remove();
			}
		}
		if(this.isPreparing()) {
			/* prepare state */
			final int prepareCD = this.challenge.getPrepareCD(this.currentWave);
			final IWaveComponent wave = this.getCurrentWaveComponent();
			final boolean isBigWave = wave != null && wave.isBigWave();
			if(! this.warningSent && prepareCD >= WAVE_WARNING_TICK && this.tick >= prepareCD - WAVE_WARNING_TICK && isBigWave) {
				this.warningSent = true;
				if(this.getRaidComponent().showRoundTitle()) {
					this.getPlayers().forEach(p -> {
						p.connection.send(new ClientboundSetTitlesAnimationPacket(10, WAVE_WARNING_TICK - 20, 10));
						PlayerUtil.sendSubTitleToPlayer(p, Component.translatable("challenge.pvz.huge_wave").withStyle(ChatFormatting.DARK_RED));
					});
				}
			}
			//对齐pvz2D Board.cpp:5398：红字出现25cs后播hugewave吼声
			if(this.warningSent && this.tick == prepareCD - WAVE_WARNING_TICK + HUGE_WAVE_ROAR_DELAY) {
				this.getPlayers().forEach(p -> PlayerUtil.playClientSound(p, SoundRegister.HUGE_WAVE.get()));
			}
			if(this.tick >= prepareCD) {
				if(wave != null) {
					this.waveStart();
				} else {
					this.status = Status.RUNNING;
				}
			}
		} else if(this.isRunning()) {
			/* running state, whole wave spawns at wave start, only the countdown to next wave is ticked */
			//对齐pvz2D Board.cpp:5360-5367：末波刷怪60cs后播finalwave
			if(this.finalWaveSoundDelay > 0) {
				-- this.finalWaveSoundDelay;
				if(this.finalWaveSoundDelay == 0) {
					this.getPlayers().forEach(p -> PlayerUtil.playClientSound(p, SoundRegister.FINAL_WAVE.get()));
				}
			}
			if(this.tick % SWITCH_INTERVAL == 0) {
				this.updateDifficultyLevel();
				this.updateRaiders();
				if(this.trySwitchWave()) {
					this.syncBar();
				}
				//boss 挑战：boss 死亡即胜利（对齐 pvz1 5-10 进度条打满过关）
				//boss 所在区块未加载时 getBossEntity 返回 null，须有玩家在场（区块已加载）才能把找不到 boss 当作击杀
				if(this.isBossChallenge() && this.bossId != null && ! this.getPlayers().isEmpty() && ! this.isBossAlive()) {
					this.status = Status.VICTORY;
					this.syncBar();
				}
				if(this.isVictory()) {
					this.onVictory();
					return ;
				}
			}
		} else if(this.isLoss()) {
			/* loss state */
			if(this.tick >= this.challenge.getLossTick()) {
				this.remove();
			}
		} else if(this.isVictory()) {
			/* victory state */
			if(this.tick >= this.challenge.getWinTick()) {
				this.remove();
			}
		}
		if(! this.firstTick){//first tick.
			this.firstTick = true;
			//boss 挑战开局生成 boss，血条随后按 boss 血量驱动
			if(this.isBossChallenge()) {
				this.summonBoss();
			}
			if(this.currentWave == 0){
				//首tick先于updatePlayers，立即为范围内玩家建立独立阳光，保证READY过场前HUD已是挑战余额
				this.world.getPlayers(this.validPlayer()).forEach(p -> {
					if(! ChallengeManager.isSunExchanged(p)) {
						this.enterSunExchange(p);
					}
				});
			}
			this.getPlayers().forEach(p -> {
				PlayerUtil.playClientSound(p, this.challenge.getPrepareSound());
				if(this.currentWave == 0){
					p.connection.send(new ClientboundSetTitlesAnimationPacket(5, READY_TITLE_STAY_TICK, 5));
					PlayerUtil.sendSubTitleToPlayer(p, Component.translatable("challenge.pvz.ready").withStyle(ChatFormatting.DARK_RED));
				}
			});
		}
		if(this.hasConveyorBelt() && (this.isPreparing() || this.isRunning())) {
			this.updateConveyorBelts();
		}
		this.tickFog();
	}

	/**
	 * 首tick按tag造雾；此后处理三叶草移除后的延迟重蔓延，以及自然到期/重载缺失的同tick补造。
	 */
	protected void tickFog() {
		if(this.hasFog() && ! this.isRemoving()) {
			PVZFog fog = PVZFogCapability.getFog(this.world, this.getFogUUID());
			if(fog == null && this.fogRecoverDelay > 0) {
				-- this.fogRecoverDelay;
				if(this.fogRecoverDelay == 0) {
					PVZFogCapability.addOrResetFog(this.world, this.center, FOG_LIFE_TICK, FOG_STRENGTH, FOG_RANGE, this.getFogUUID());
				}
			} else {
				//fog 存在但 lifeLeft<0：三叶草标记移除，Capability下一tick清理，按原作24秒后重新蔓延
				//fog==null 且无延迟：首tick造雾，或自然到期/重载缺失，与Capability移除同tick补造，视觉无断层
				if(fog != null && fog.lifeLeft < 0) {
					this.fogRecoverDelay = FOG_RECOVER_TICK;
				} else if(fog == null) {
					PVZFogCapability.addOrResetFog(this.world, this.center, FOG_LIFE_TICK, FOG_STRENGTH, FOG_RANGE, this.getFogUUID());
				}
			}
		}
	}

	private boolean hasFog() {
		return this.hasTag(TAG_FOG);
	}

	public boolean hasSeedRain() {
		return this.hasTag(TAG_SEED_RAIN) && this.challenge.getSeedPool() != null;
	}

	public boolean hasConveyorBelt() {
		return this.hasTag(TAG_CONVEYOR) && this.challenge.getSeedPool() != null;
	}

	public boolean hasBowling() {
		return this.hasTag(TAG_BOWLING);
	}

	public void spawnSeedPacket(BlockPos pos) {
		final WeightList<ItemStack> pool = this.challenge.getSeedPool();
		final Optional<ItemStack> card = pool.getRandomItem(this.world.random);
		if(card.isPresent()) {
			final SeedPacketEntity seedPacket = EntityRegister.SEED_PACKET.get().create(this.world);
			if(seedPacket != null) {
				seedPacket.moveTo(pos, 0.0F, 0.0F);
				//种子池条目是 WeightList 里共享的同一个 ItemStack，直接写 uuid 会把标记永久留进池子
				final ItemStack cardStack = card.get().copy();
				PlantCardItem.setChallengeUuid(cardStack, this.getBarUuid());
				seedPacket.setCardStack(cardStack);
				this.world.addFreshEntity(seedPacket);
			}
		}
	}

	/**
	 * 每玩家独立带面：每tick推进补卡倒计时，到点按权重补一张并整表同步给该玩家。
	 * 卡片滑动位置由客户端按入场gameTime推算，服务端只维护带面内容与倒计时。
	 */
	private void updateConveyorBelts() {
		this.world.getPlayers(this.validPlayer()).forEach(player -> {
			final ConveyorBelt belt = this.conveyorBelts.computeIfAbsent(player.getUUID(), uuid -> new ConveyorBelt());
			-- belt.spawnCooldown;
			if(belt.spawnCooldown <= 0) {
				//原版间隔按补卡前的张数分档：不超过4张400cs、超过4张425cs、超过6张500cs、超过8张1000cs
				final int count = belt.cards.size();
				belt.spawnCooldown = count > 8 ? 200 : count > 6 ? 100 : count > 4 ? 85 : 80;
				if(count < CONVEYOR_MAX_CARDS) {
					final WeightList<ItemStack> pool = this.challenge.getSeedPool();
					final WeightList<ItemStack> adjusted = new WeightList<>();
					int total = 0;
					if(pool != null) {
						for(int i = 0; i < pool.getLen(); ++ i) {
							final ItemStack stack = pool.getItem(i);
							int weight = pool.getWeight(i);
							//原版权重压制：带上同类型已达4张压到1、已达3张压到5、与上一张同类型减半
							if(pool.getLen() > 2) {
								int onBelt = 0;
								for(ItemStack card : belt.cards) {
									if(card.getItem() == stack.getItem()) {
										++ onBelt;
									}
								}
								if(onBelt >= 4) {
									weight = 1;
								} else if(onBelt >= 3) {
									weight = 5;
								} else if(stack.getItem() == belt.lastSeedType) {
									weight /= 2;
								}
							}
							total += weight;
							adjusted.addItem(stack, weight);
						}
					}
					//权重全为0时 getRandomItem 会以0为界取随机数，此处跳过本次补卡
					final Optional<ItemStack> card = total > 0 ? adjusted.getRandomItem(this.world.random) : Optional.empty();
					if(card.isPresent()) {
						//种子池条目是 WeightList 里共享的同一个 ItemStack，直接写 uuid 会把标记永久留进池子
						final ItemStack cardStack = card.get().copy();
						PlantCardItem.setChallengeUuid(cardStack, this.getBarUuid());
						belt.cards.add(cardStack);
						belt.entryTicks.add(this.world.getGameTime());
						belt.lastSeedType = cardStack.getItem();
						this.syncConveyorBeltTo(player);
					}
				}
			}
		});
	}

	/**
	 * 取走带上指定卡：走原版拾取语义（快捷栏优先、可堆叠），背包放不下或已持有挑战体验卡则卡片留在带上。
	 */
	public void takeConveyorCard(ServerPlayer player, int index) {
		final ConveyorBelt belt = this.conveyorBelts.get(player.getUUID());
		if(belt != null && index >= 0 && index < belt.cards.size()) {
			final ItemStack cardStack = belt.cards.get(index);
			boolean holdingCard = false;
			for(int i = 0; i < player.getInventory().getContainerSize(); ++ i) {
				if(PlantCardItem.getChallengeUuid(player.getInventory().getItem(i)) != null) {
					holdingCard = true;
					break;
				}
			}
			if(holdingCard) {
				player.displayClientMessage(Component.translatable("challenge.pvz.conveyor_holding"), true);
			} else if(player.getInventory().add(cardStack.copy())) {
				belt.cards.remove(index);
				belt.entryTicks.remove(index);
				//入场时刻刻意不重置：槽位下标前移一格，滑动余量随之自动多出一格，卡片原地续滑进空槽
				this.syncConveyorBeltTo(player);
			} else {
				player.displayClientMessage(Component.translatable("challenge.pvz.conveyor_full"), true);
			}
		}
	}

	private void syncConveyorBeltTo(ServerPlayer player) {
		final ConveyorBelt belt = this.conveyorBelts.get(player.getUUID());
		if(belt != null) {
			final long[] entryTicks = new long[belt.entryTicks.size()];
			for(int i = 0; i < entryTicks.length; ++ i) {
				entryTicks[i] = belt.entryTicks.get(i);
			}
			PVZPacketHandler.sendToClient(player, new ConveyorBeltPacket(belt.cards, entryTicks));
		}
	}

	/**
	 * {@link #tick()}
	 */
	protected void updateDifficultyLevel() {
		/* update difficulty level */
		if(this.getWorld().getDifficulty() == Difficulty.HARD){
			if(this.tick % 10 == 0){
				this.currentMaxLevel = 0;
				this.getPlayers().forEach(p -> {
					this.currentMaxLevel += PlayerUtil.getResource(p, Resources.TREE_LVL);
				});
			}
		} else {
			this.currentMaxLevel = 0;
		}
	}

	/**
	 * drop dead members.
	 * {@link #tick()}
	 */
	protected void updateRaiders() {
		this.raiders.removeIf(entity -> !entity.isAlive());
	}

	/**
	 * buy the whole roster of this wave with its points budget, equivalent to pvz2D PickZombieWaves.
	 * the flag zombie is forced ahead on a big wave, the rest are weighted picks that consume points.
	 */
	protected List<ISpawnComponent> pickWaveRoster(IWaveComponent wave, int points) {
		final List<ISpawnComponent> roster = new ArrayList<>();
		if(wave.isBigWave()) {
			for(ISpawnComponent spawn : wave.getSpawns()) {
				if(spawn.isFlag()) {
					roster.add(spawn);
					break;
				}
			}
		}
		int pointsLeft = points;
		while(pointsLeft > 0 && roster.size() < MAX_ZOMBIES_IN_WAVE) {
			final List<ISpawnComponent> candidates = new ArrayList<>();
			int allWeight = 0;
			for(ISpawnComponent spawn : wave.getSpawns()) {
				if(! spawn.isFlag() && pointsLeft >= spawn.getThreat() && (! spawn.isElite() || wave.isBigWave())
						&& (float) this.currentWave / this.challenge.getTotalWaveCount() >= spawn.getStartFrom()) {
					candidates.add(spawn);
					allWeight += spawn.getWeight();
				}
			}
			if(candidates.isEmpty() || allWeight <= 0) {
				break;
			}
			int selected = this.world.random.nextInt(allWeight);
			ISpawnComponent picked = null;
			for(ISpawnComponent spawn : candidates) {
				selected -= spawn.getWeight();
				if(selected <= 0) {
					picked = spawn;
					break;
				}
			}
			if(picked == null) {
				break;
			}
			roster.add(picked);
			pointsLeft -= picked.getThreat();
		}
		return roster;
	}

	/**
	 * summon one zombie of the picked roster and bind its threat cost and spawn wave onto the raider capability.
	 */
	protected boolean summonEntity(ISpawnComponent spawn, int wavePos) {
		final Entity entity = this.createEntity(spawn);
		if(entity == null) {
			return false;
		}
		this.raiders.add(entity);
		entity.getCapability(CapabilityHandler.RAIDER_DATA_CAPABILITY).ifPresent(cap -> {
			cap.setThreat(spawn.getThreat());
			cap.setWave(wavePos);
		});
		if(entity instanceof Mob) {
			// avoid despawn.
			((Mob) entity).setPersistenceRequired();

			//close to center goal.
			if (this.getRaidComponent().shouldCloseToCenter()) {
				((Mob) entity).goalSelector.addGoal(0, new ChallengeMoveGoal(((Mob) entity), this));
			}
		}
		if(entity instanceof AbstractPAZEntity){//init skills.
			AbstractPAZEntity.randomInitSkills((AbstractPAZEntity) entity, Math.max(0, this.currentMaxLevel - this.getRaidComponent().getRecommendLevel()));
		}
		return true;
	}

	/**
	 * copy from {@link net.minecraft.server.commands.SummonCommand}
	 */
	private Entity createEntity(ISpawnComponent spawn) {
		final BlockPos pos;
		if(spawn.getPlacement() != null) {
			pos = spawn.getPlacement().getPlacePosition(this.world, this.center);
		} else {
			// zombies walk in from 1~5 blocks outside one of the four square edges, spawn distance is global
			final int side = this.world.random.nextInt(4);
			final int outer = ConfigUtil.getRaidRange() + 1 + this.world.random.nextInt(5);
			final int along = this.world.random.nextInt(ConfigUtil.getRaidRange() * 2 + 1) - ConfigUtil.getRaidRange();
			final int spawnX = this.center.getX() + (side == 0 ? outer : side == 1 ? -outer : along);
			final int spawnZ = this.center.getZ() + (side == 2 ? outer : side == 3 ? -outer : along);
			pos = new BlockPos(spawnX, this.world.getHeight(Heightmap.Types.WORLD_SURFACE, spawnX, spawnZ), spawnZ);
		}
		final CompoundTag nbt = spawn.getNBT().copy();
		nbt.putInt("pvz_avoid_same_hash_random", this.world.random.nextInt());
		return EntityUtil.createWithNBT(this.world, spawn.getSpawnType(), nbt, pos);
	}

	/**
	 * total threat of alive raiders spawned in the given wave.
	 */
	public int getLivingMembersThreat(int wavePos) {
		int result = 0;
		for(Entity raider : this.raiders) {
			final int[] cost = new int[1];
			raider.getCapability(CapabilityHandler.RAIDER_DATA_CAPABILITY).ifPresent(cap -> {
				if(cap.getWave() == wavePos) {
					cost[0] = cap.getThreat();
				}
			});
			result += cost[0];
		}
		return result;
	}

	/**
	 * countdown driven wave switching, equivalent to pvz2D mZombieCountDown plus the health early trigger.
	 * before the minimum wait the wave holds; inside the wait window it advances once this wave loses enough
	 * threat; past the maximum wait it advances unconditionally. waves may overlap. the final wave spawns its
	 * whole roster at wave start and grants victory as soon as every raider is dead, aligned with htpvz2 which
	 * settles immediately on the last member removal instead of waiting out the remaining minimum wait.
	 */
	public boolean trySwitchWave() {
		final boolean isFinalWave = this.currentWave >= this.challenge.getTotalWaveCount() - 1;
		if(isFinalWave) {
			//boss 挑战胜利由 boss 死亡单独判定，小怪清空不结算
			if(this.raiders.isEmpty() && ! this.isBossChallenge()) {
				this.status = Status.VICTORY;
				return true;
			}
			return false;
		}
		final IWaveComponent wave = this.getCurrentWaveComponent();
		if(this.tick < wave.getMinimumWaitTime()) {
			return false;
		}
		if(this.tick < wave.getMaximumWaitTime() && this.getLivingMembersThreat(this.currentWave) > this.waveSwitchThreshold) {
			return false;
		}
		//波次超时且一只僵尸都没召唤出来（如生成点全被占用），标记放弃波次，客户端显示白旗
		if(this.waveStartThreat == 0 && this.tick > wave.getMaximumWaitTime()) {
			this.givenUpWaves.set(this.currentWave);
		}
		this.currentWave += 1;
		this.tick = 0;
		this.warningSent = false;
		this.status = Status.PREPARE;
		return true;
	}

	/**
	 * {@link #tick()}
	 */
	protected void tickBar() {
		if(this.tick % 10 == 0 && ! this.world.players().isEmpty()) {
			this.updatePlayers();
		}
		this.challengeBar.setColor(this.challenge.getBarColor());
		this.challengeBar.setName(this.getBarName());
		if(this.isPreparing() || this.isRunning()) {
			//boss 挑战进度条显示对 boss 造成的伤害比例，其余挑战沿用波次进度
			this.challengeBar.setProgress(this.isBossChallenge() ? this.getBossProgress() : this.getWaveProgress());
		} else {
			this.challengeBar.setProgress(1F);
		}
	}

	/**
	 * aligned with pvz2D Board::UpdateProgressMeter: wave slot start plus in-wave kill fraction,
	 * the previous wave end equals the next wave start so the fill never bounces back.
	 */
	private float getWaveProgress() {
		final int totalWaves = this.challenge.getTotalWaveCount();
		if(this.currentWave >= totalWaves - 1) {
			return 1.0F;
		}
		float fraction = 0.0F;
		if(this.isRunning()) {
			final int damageTarget = this.waveStartThreat - this.waveSwitchThreshold;
			if(damageTarget >= 1) {
				fraction = Mth.clamp((float) (this.waveStartThreat - this.getLivingMembersThreat(this.currentWave)) / damageTarget, 0.0F, 1.0F);
			}
		}
		return (this.currentWave + fraction) / (totalWaves - 1);
	}

	/**
	 * bar title carries the challenge id as an extra translatable arg, client side uses it to match synced wave data.
	 */
	private Component getBarName() {
		final MutableComponent name = Component.translatable("challenge." + this.resource.getNamespace() + "." + this.resource.getPath() + ".name", this.id);
		if(this.isVictory()) {
			return name.append(" - ").append(this.challenge.getWinTitle());
		} else if(this.isLoss()) {
			return name.append(" - ").append(this.challenge.getLossTitle());
		}
		return name;
	}

	/**
	 * sync wave layout and current progress to all tracked players, for the custom boss bar with flag markers.
	 */
	private void syncBar() {
		this.getPlayers().forEach(this::syncBarTo);
	}

	private void syncBarTo(ServerPlayer player) {
		final BitSet bigWaves = new BitSet();
		for(int i = 0; i < this.challenge.getTotalWaveCount(); ++ i) {
			if(this.challenge.getWaves().get(i).isBigWave()) {
				bigWaves.set(i);
			}
		}
		//BGM 随 bar 包下发：仅准备/进行态播放，终态重发本包即驱动客户端淡出，bar 本身保留到 remove
		final boolean bossChallenge = this.isBossChallenge();
		final boolean bgmPlaying = this.isPreparing() || this.isRunning();
		PVZPacketHandler.sendToClient(player, new ChallengeBarPacket(this.id, this.challengeBar.getId(), this.resource, this.challenge.getTotalWaveCount(), this.currentWave, bigWaves, this.givenUpWaves, bossChallenge, bgmPlaying, this.center, this.getDayTimeOverride(), this.getRainLevelOverride(), this.getThunderLevelOverride()));
	}

	/**
	 * 挑战唯一标识（ServerBossEvent bar id），供挑战绑定的体验卡校验。
	 */
	public UUID getBarUuid() {
		return this.challengeBar.getId();
	}

	/**
	 * player who is alive and in suitable range can be tracked.
	 */
	private Predicate<ServerPlayer> validPlayer() {
		return this::isInRange;
	}

	public boolean isInRange(Entity entity) {
		return this.isInRange(entity.getX(), entity.getY(), entity.getZ());
	}

	public boolean isInRange(double x, double y, double z) {
		final int range = ConfigUtil.getRaidRange();
		return Math.abs(x - this.center.getX()) <= range
				&& Math.abs(y - this.center.getY()) <= range
				&& Math.abs(z - this.center.getZ()) <= range;
	}

	public long getDayTimeOverride() {
		long result = -1L;
		if((this.isPreparing() || this.isRunning()) && this.getRaidComponent() != null) {
			if(this.hasTag(TAG_NIGHT)) {
				result = NIGHT_OVERRIDE_TIME;
			} else if(this.hasTag(TAG_DAY)) {
				result = DAY_OVERRIDE_TIME;
			}
		}
		return result;
	}

	public float getRainLevelOverride() {
		float result = -1.0F;
		if((this.isPreparing() || this.isRunning()) && this.getRaidComponent() != null) {
			if(this.hasTag(TAG_THUNDER) || this.hasTag(TAG_RAIN)) {
				result = 1.0F;
			} else if(this.hasTag(TAG_CLEAR)) {
				result = 0.0F;
			}
		}
		return result;
	}

	public float getThunderLevelOverride() {
		float result = -1.0F;
		if((this.isPreparing() || this.isRunning()) && this.getRaidComponent() != null) {
			if(this.hasTag(TAG_THUNDER)) {
				result = 1.0F;
			} else if(this.hasTag(TAG_RAIN) || this.hasTag(TAG_CLEAR)) {
				result = 0.0F;
			}
		}
		return result;
	}

	/**
	 * 玩家是否曾参与本挑战（heroes 记录，含离开范围后的 ChallengeWaitTime 等待移除阶段）。
	 */
	public boolean isParticipant(ServerPlayer player) {
		return this.heroes.contains(player.getUUID());
	}

	/**
	 * {@link #tickBar()}
	 */
	protected void updatePlayers() {
		final Set<ServerPlayer> oldPlayers = Sets.newHashSet(this.challengeBar.getPlayers());
		final Set<ServerPlayer> newPlayers = Sets.newHashSet(this.world.getPlayers(this.validPlayer()));

		/* add new join players */
		newPlayers.forEach(p -> {
			if(! oldPlayers.contains(p)) {
				this.challengeBar.addPlayer(p);
				this.syncBarTo(p);
				//挑战期间禁止僵尸入侵叠加：清空入侵波次与任务，入侵进度条随之消失
				PlayerUtil.getInvasion(p).disable();
				//带面为空时也要下发，玩家一进范围即显示带子；终态带面已撤，重进范围不能又补一条空带
				if(this.hasConveyorBelt() && (this.isPreparing() || this.isRunning())) {
					this.conveyorBelts.computeIfAbsent(p.getUUID(), uuid -> new ConveyorBelt());
					this.syncConveyorBeltTo(p);
				}
			}
		});

		/* remove offline players */
		oldPlayers.forEach(p -> {
			if(! newPlayers.contains(p)) {
				this.challengeBar.removePlayer(p);
				//阳光不随离开范围立即恢复，保留挑战余额至 remove() 时 releaseAllSunSessions 统一写回
				//带面内容保留，玩家回到范围时由上面的分支重新下发
				if(this.hasConveyorBelt()) {
					PVZPacketHandler.sendToClient(p, ConveyorBeltPacket.remove());
				}
			}
		});

		/* add heroes */
		this.challengeBar.getPlayers().forEach(p -> {
            this.heroes.add(p.getUUID());
		});

		/* 中途走进范围或离开后返回的玩家在此续接独立阳光；终态不再交换 */
		if(this.isPreparing() || this.isRunning()) {
			newPlayers.forEach(p -> {
				final SunSession session = this.sunSessions.get(p.getUUID());
				if((session == null || ! session.exchanged) && ! ChallengeManager.isSunExchanged(p)) {
					this.enterSunExchange(p);
				}
			});
		}

		if(this.challengeBar.getPlayers().isEmpty()){
			if(! this.isStopping()) {
				++ this.stopTick;
				this.heroes.forEach(uuid -> {
					Player player = this.world.getPlayerByUUID(uuid);
					if(player != null) {
						PlayerUtil.sendMsgTo(player, CHALLENGE_WARN);
					}
				});
			}
		} else {
			this.stopTick = 0;
		}
	}

	/**
	 * boss 挑战开局生成 boss 实体；自带 Boss 条让位给挑战 bar，隐藏标记随 nbt 生成、由实体自身读取。
	 */
	protected void summonBoss() {
		final ISpawnComponent boss = this.challenge.getBossSpawn();
		boss.getNBT().putBoolean("boss_bar_visible", false);
		final Entity entity = this.createEntity(boss);
		if(entity == null) {
			PVZMod.LOGGER.warn("Challenge Boss Summon Fail : {}", this.resource);
			return;
		}
		this.raiders.add(entity);
		this.bossId = entity.getUUID();
		if(entity instanceof Mob) {
			((Mob) entity).setPersistenceRequired();
		}
	}

	/**
	 * boss 挑战当前 boss 实体，未生成或已移除时为 null。
	 */
	public Entity getBossEntity() {
		return this.bossId == null ? null : this.world.getEntity(this.bossId);
	}

	private boolean isBossAlive() {
		final Entity boss = this.getBossEntity();
		return boss instanceof LivingEntity living && living.isAlive();
	}

	/**
	 * 对齐 pvz2D Board::UpdateProgressMeter：进度条 = 对 boss 已造成伤害比例（总量含内防护盾），boss 死亡时打满。
	 */
	private float getBossProgress() {
		final Entity boss = this.getBossEntity();
		float progress = 1.0F;
		if(boss instanceof AbstractPAZEntity pazEntity && boss.isAlive() && pazEntity.getCurrentMaxHealth() > 0) {
			progress = Mth.clamp((float) (1.0D - pazEntity.getCurrentHealth() / pazEntity.getCurrentMaxHealth()), 0.0F, 1.0F);
		} else if(boss instanceof LivingEntity living && living.isAlive() && living.getMaxHealth() > 0) {
			progress = Mth.clamp((float) (living.getMaxHealth() - living.getHealth()) / living.getMaxHealth(), 0.0F, 1.0F);
		}
		return progress;
	}

	public boolean isBossChallenge() {
		return this.getRaidComponent() != null && this.getRaidComponent().isBossChallenge();
	}

	/**
	 * run when prepare time is finished.
	 */
	protected void waveStart() {
		this.status = Status.RUNNING;
		final IWaveComponent wave = this.getCurrentWaveComponent();
		int points = Math.max(1, wave.getThreat());
		if(this.currentWave >= 1) {
			points *= (int) Math.sqrt(Math.max(1, this.getPlayers().size()));
		}
		/* the whole roster enters in the same tick, staggered arrival comes from spawn placement offsets and zombie speed variance */
		int spawnedThreat = 0;
		for(ISpawnComponent spawn : this.pickWaveRoster(wave, points)) {
			if(this.summonEntity(spawn, this.currentWave)) {
				spawnedThreat += spawn.getThreat();
			}
		}
		this.waveSwitchThreshold = Mth.floor(spawnedThreat * (0.5F + this.world.random.nextFloat() * 0.15F));
		this.waveStartThreat = spawnedThreat;
		final boolean isFinalWave = this.currentWave >= this.challenge.getTotalWaveCount() - 1;
		this.getPlayers().forEach(p -> {
			if(this.getRaidComponent().showRoundTitle()){
				if(isFinalWave){
					p.connection.send(new ClientboundSetTitlesAnimationPacket(10, 60, 10));
					PlayerUtil.sendSubTitleToPlayer(p, Component.translatable("challenge.pvz.final_wave").withStyle(ChatFormatting.DARK_RED));
				} else if(this.warningSent){
					p.connection.send(new ClientboundClearTitlesPacket(false));
				} else if(wave.isBigWave()){
					p.connection.send(new ClientboundSetTitlesAnimationPacket(10, WAVE_WARNING_TICK - 20, 10));
					PlayerUtil.sendSubTitleToPlayer(p, Component.translatable("challenge.pvz.huge_wave").withStyle(ChatFormatting.DARK_RED));
				}
			}
			if(wave.isBigWave()){
				PlayerUtil.playClientSound(p, SoundRegister.SIREN.get());
			} else if(this.currentWave == 0){
				PlayerUtil.playClientSound(p, SoundRegister.AWOOGA.get());
			}
		});
		if(isFinalWave){
			this.finalWaveSoundDelay = FINAL_WAVE_SOUND_DELAY;
		}
	}

	private IWaveComponent getCurrentWaveComponent() {
		final List<IWaveComponent> waves = this.challenge.getWaves();
		return waves.isEmpty() ? null : waves.get(Mth.clamp(this.currentWave, 0, waves.size() - 1));
	}

	/**
	 * any tracked hero dies means the zombies broke into the house, aligned with htpvz2 target death failure.
	 * {@link com.hungteen.pvz.common.event.handler.PlayerEventHandler#handlePlayerDeath}
	 */
	public void onHeroDeath(ServerPlayer player) {
		if((this.isPreparing() || this.isRunning()) && this.heroes.contains(player.getUUID())) {
			this.status = Status.LOSS;
			this.onLoss();
		}
	}

	/**
	 * run when raid is not defeated.
	 */
	protected void onLoss() {
		this.tick = 0;
		//onHeroDeath返回后PlayerEventHandler会按真实阳光处理死亡掉落，必须先恢复
		this.releaseAllSunSessions();
		//终态重发bar包驱动BGM淡出，bar标题仍保留到remove
		this.syncBar();
		//结算到 remove 之间还要等 loss tick，带面留到那时会继续挂在屏幕上
		if(this.hasConveyorBelt()) {
			this.conveyorBelts.clear();
			this.getPlayers().forEach(p -> PVZPacketHandler.sendToClient(p, ConveyorBeltPacket.remove()));
		}
		this.getPlayers().forEach(p -> PlayerUtil.playClientSound(p, this.challenge.getLossSound()));
		MinecraftForge.EVENT_BUS.post(new RaidEvent.RaidLossEvent(this));
	}

	/**
	 * run when raid is defeated.
	 */
	protected void onVictory() {
		this.tick = 0;
		this.releaseAllSunSessions();
		//终态重发bar包驱动BGM淡出，bar标题仍保留到remove
		this.syncBar();
		//结算到 remove 之间还要等 win tick，带面留到那时会继续挂在屏幕上
		if(this.hasConveyorBelt()) {
			this.conveyorBelts.clear();
			this.getPlayers().forEach(p -> PVZPacketHandler.sendToClient(p, ConveyorBeltPacket.remove()));
		}
		this.getPlayers().forEach(p -> {
			PlayerUtil.playClientSound(p, this.challenge.getWinSound());
			ChallengeTrigger.INSTANCE.trigger(p, this.resource.toString());
		});
		if(! MinecraftForge.EVENT_BUS.post(new RaidEvent.RaidWinEvent(this))) {
			this.getPlayers().forEach(p -> {
				this.challenge.getRewards().forEach(r -> r.reward(p));
			});
			this.challenge.getRewards().forEach(r -> r.rewardGlobally(this));
		}
	}

	public void remove() {
		this.status = Status.REMOVING;
		//和平/组件缺失/无人超时等所有移除出口的终态兜底，幂等
		this.releaseAllSunSessions();
		//僵尸减速由僵尸实体自管理，挑战移除后其isInChallengeRange()失效自摘，无需在此处理
		//非雾挑战不存在对应UUID的雾，此处返回false且不发包，无副作用
		PVZFogCapability.modifyFogFeatures(this.world, this.getFogUUID(), PVZFogPacket.ModifyType.REMOVE, 0);
		final ChallengeBarPacket removePacket = ChallengeBarPacket.remove(this.id);
		//带面包同样按 heroes 补齐下发，非传送带挑战收到空包仅清空本地状态，幂等无害
		final ConveyorBeltPacket beltRemovePacket = ConveyorBeltPacket.remove();
		this.getPlayers().forEach(player -> {
			PVZPacketHandler.sendToClient(player, removePacket);
			PVZPacketHandler.sendToClient(player, beltRemovePacket);
		});
		//曾参与但已离开范围（被 updatePlayers 移出 challengeBar）的在线玩家收不到上面的 remove 包，
		//其客户端 BGM 会残留一直播放，此处按 heroes 补齐停曲；重复发送幂等无害
		this.heroes.forEach(uuid -> {
			final Player player = this.world.getPlayerByUUID(uuid);
			if(player instanceof ServerPlayer serverPlayer && ! this.challengeBar.getPlayers().contains(serverPlayer)) {
				PVZPacketHandler.sendToClient(serverPlayer, removePacket);
				PVZPacketHandler.sendToClient(serverPlayer, beltRemovePacket);
			}
		});
		this.challengeBar.removeAllPlayers();
		this.raiders.forEach(e -> e.remove(net.minecraft.world.entity.Entity.RemovalReason.KILLED));
		final List<PVZPlantEntity> challengePlants = this.world.getEntitiesOfClass(PVZPlantEntity.class,
				new AABB(this.center).inflate(ConfigUtil.getRaidRange()),
				plant -> this.getBarUuid().equals(plant.getChallengeUuid()));
		challengePlants.forEach(plant -> plant.remove(Entity.RemovalReason.KILLED));
	}

	public int getId() {
		return this.id;
	}

	public BlockPos getCenter() {
		return this.center;
	}

	private UUID getFogUUID() {
		return UUID.nameUUIDFromBytes(("pvz_challenge_fog_" + this.id).getBytes(StandardCharsets.UTF_8));
	}

	public boolean isRaider(Entity raider) {
		return this.raiders.contains(raider);
	}

	public boolean isStopping() {
		return this.stopTick > 0;
	}

	public boolean isPreparing() {
		return this.status == Status.PREPARE;
	}

	public boolean isRunning() {
		return this.status == Status.RUNNING;
	}

	public boolean isRemoving() {
		return this.status == Status.REMOVING;
	}

	public boolean isLoss() {
		return this.status == Status.LOSS;
	}

	public boolean isVictory() {
		return this.status == Status.VICTORY;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * get raid component by resource.
	 */
	public IChallengeComponent getRaidComponent() {
		return this.challenge != null ? this.challenge : (this.challenge = ChallengeManager.getChallengeByResource(this.resource));
	}

	/**
	 * get tracked players by raid bar.
	 */
	public List<ServerPlayer> getPlayers(){
		return new ArrayList<>(this.challengeBar.getPlayers());
	}

	public boolean hasTag(String tag) {
		return this.challenge.hasTag(tag);
	}

	public List<String> getAuthors(){
		return this.challenge.getAuthors();
	}

	public Set<Entity> getRaiders(){
		return this.raiders;
	}

	@Override
	public ServerLevel getWorld() {
		return world;
	}

	/**
	 * 进入范围：快照真实阳光并以挑战开局余额替换；存在离场会话时续用上次挑战余额。
	 * 上限包必须先于阳光值包到达客户端，否则余额高于树等级上限时会被客户端clamp截断。
	 */
	private void enterSunExchange(ServerPlayer player) {
		final SunSession session = this.sunSessions.get(player.getUUID());
		PlayerUtil.setSunLimitOverride(player, this.challenge.getSunLimit());
		if(session == null) {
			//未配置initial_sun时按智慧树等级线性映射：1级150、100级250
			final int configuredSun = this.challenge.getInitialSun();
			final int treeLevel = Math.min(100, PlayerUtil.getResource(player, Resources.TREE_LVL));
			final int initialSun = configuredSun > 0 ? configuredSun : 150 + (treeLevel - 1) * 100 / 99;
			this.sunSessions.put(player.getUUID(), new SunSession(PlayerUtil.getResource(player, Resources.SUN_NUM), initialSun));
			PlayerUtil.setResource(player, Resources.SUN_NUM, initialSun);
		} else {
			session.originalSun = PlayerUtil.getResource(player, Resources.SUN_NUM);
			PlayerUtil.setResource(player, Resources.SUN_NUM, session.challengeSun);
			session.exchanged = true;
		}
	}

	/**
	 * 玩家登出时：当前槽位值存回挑战余额，真实阳光快照写回槽位；离开范围不再调用，余额保留至挑战移除统一恢复。
	 */
	private void leaveSunExchange(ServerPlayer player) {
		final SunSession session = this.sunSessions.get(player.getUUID());
		if(session != null && session.exchanged) {
			session.challengeSun = PlayerUtil.getResource(player, Resources.SUN_NUM);
			PlayerUtil.setResource(player, Resources.SUN_NUM, session.originalSun);
			PlayerUtil.setSunLimitOverride(player, 0);
			session.exchanged = false;
		}
	}

	/**
	 * 挑战终态恢复全部在线玩家真实阳光；离线玩家登出时已写回，仅落标志。
	 */
	private void releaseAllSunSessions() {
		this.sunSessions.forEach((uuid, session) -> {
			if(session.exchanged) {
				final Player player = this.world.getPlayerByUUID(uuid);
				if(player != null) {
					PlayerUtil.setResource(player, Resources.SUN_NUM, session.originalSun);
					PlayerUtil.setSunLimitOverride(player, 0);
				}
				session.exchanged = false;
			}
		});
	}

	boolean isSunExchanged(UUID uuid) {
		final SunSession session = this.sunSessions.get(uuid);
		return session != null && session.exchanged;
	}

	/**
	 * 崩溃关服时玩家cap可能保存的是交换态余额，依NBT恢复的exchanged残留标志校正；正常登出则续用会话。
	 */
	public void onPlayerLogin(ServerPlayer player) {
		final SunSession session = this.sunSessions.get(player.getUUID());
		if(session != null) {
			if((this.isPreparing() || this.isRunning()) && this.isInRange(player)) {
				if(this.challenge != null) {
					PlayerUtil.setSunLimitOverride(player, this.challenge.getSunLimit());
				}
				if(! session.exchanged) {
					session.originalSun = PlayerUtil.getResource(player, Resources.SUN_NUM);
				}
				PlayerUtil.setResource(player, Resources.SUN_NUM, session.challengeSun);
				session.exchanged = true;
			} else if(session.exchanged) {
				PlayerUtil.setResource(player, Resources.SUN_NUM, session.originalSun);
				PlayerUtil.setSunLimitOverride(player, 0);
				session.exchanged = false;
			}
		}
	}

	public void onPlayerLogout(ServerPlayer player) {
		this.leaveSunExchange(player);
	}

	public enum Status {
		  PREPARE,
	      RUNNING,
	      VICTORY,
	      LOSS,
	      REMOVING
    }

	/**
	 * 单次挑战内一名玩家的阳光隔离会话：originalSun为进入前真实阳光，challengeSun为挑战内独立余额。
	 */
	private static final class SunSession {
		private int originalSun;
		private int challengeSun;
		private boolean exchanged;

		private SunSession(int originalSun, int challengeSun) {
			this(originalSun, challengeSun, true);
		}

		private SunSession(int originalSun, int challengeSun, boolean exchanged) {
			this.originalSun = originalSun;
			this.challengeSun = challengeSun;
			this.exchanged = exchanged;
		}
	}

	/**
	 * 单次挑战内一名玩家的传送带：cards 按带上顺序排列，entryTicks 记录各卡滑入当前槽位的gameTime，
	 * 客户端据此推算滑动位移，服务端无需维护坐标。
	 */
	private static final class ConveyorBelt {
		private final List<ItemStack> cards = new ArrayList<>();
		private final List<Long> entryTicks = new ArrayList<>();
		private int spawnCooldown = CONVEYOR_INITIAL_DELAY;
		private Item lastSeedType;
	}

}