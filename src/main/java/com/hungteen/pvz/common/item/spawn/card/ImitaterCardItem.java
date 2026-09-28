package com.hungteen.pvz.common.item.spawn.card;

import com.hungteen.pvz.api.types.ICoolDown;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.client.render.itemstack.ImitaterCardISTER;
import com.hungteen.pvz.common.container.ImitaterContainer;
import com.hungteen.pvz.common.container.inventory.ItemInventory;
import com.hungteen.pvz.common.entity.plant.magic.ImitaterEntity;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.impl.plant.PlantType;
import com.hungteen.pvz.common.item.PVZItemGroups;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ImitaterCardItem extends PlantCardItem {

	public static final String IMITATE_STRING = "imitate_plant_type";

	public ImitaterCardItem() {
		super(new Item.Properties().tab(PVZItemGroups.PVZ_PLANT_CARD).stacksTo(1), PVZPlants.IMITATER, false);
	}

	public ImitaterCardItem(boolean isEnjoyCard) {
		super(new Item.Properties().tab(PVZItemGroups.PVZ_PLANT_CARD).stacksTo(16), PVZPlants.IMITATER, isEnjoyCard);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return new ImitaterCardISTER(
					net.minecraft.client.Minecraft.getInstance().getBlockEntityRenderDispatcher(),
					net.minecraft.client.Minecraft.getInstance().getEntityModels());
			}
		});
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
		ItemStack heldStack = player.getItemInHand(handIn);
		final ItemStack plantStack = getImitatedCard(heldStack);
		/* left hand to open gui */
		if(handIn == InteractionHand.OFF_HAND) {
			if(! world.isClientSide) {
				this.openImitateGui(player);
			}
			return InteractionResultHolder.success(heldStack);
		}
		/* imitated card use */
		if(plantStack.getItem() instanceof PlantCardItem) {
			return super.use(world, player, handIn);
		}
		return InteractionResultHolder.fail(heldStack);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		final Player player = context.getPlayer();
		final ItemStack heldStack = context.getItemInHand();
		final ItemStack plantStack = getImitatedCard(heldStack);
		/* left hand click means open gui */
		if(context.getHand() == InteractionHand.OFF_HAND) {
			if(! player.level.isClientSide) {
				this.openImitateGui(player);
			}
			return InteractionResult.SUCCESS;
		}
		/* imitated card use on block */ 
		if(plantStack.getItem() instanceof PlantCardItem) {
			return super.useOn(context);
		}
		return InteractionResult.FAIL;
	}
	
	private void openImitateGui(Player player) {
		if (player instanceof ServerPlayer) {
			NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {

				@Override
				public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
					return new ImitaterContainer(id, player);
				}

				@Override
				public Component getDisplayName() {
					return Component.translatable("gui.pvz.imitater.show");
				}
				
			});
		}
	}
	
	public static boolean summonImitater(Player player, ItemStack heldStack, ItemStack plantStack, PlantCardItem cardItem, BlockPos pos, ImitaterEntity imitaterEntity, Consumer<ImitaterEntity> consumer) {
		return PlantCardItem.joinPlantEntity(player, PVZPlants.IMITATER, pos, imitaterEntity, i -> {
			if(i instanceof ImitaterEntity imitater) {
                imitater.setImitateCard(plantStack.copy());
    	        imitater.setDirection(player.getDirection().getOpposite());
		        imitater.onSpawnedByPlayer(player, cardItem.getBasisSunCost(plantStack));
		        /* enchantment effects */
				enchantPlantEntityByCard(imitater, plantStack);
		        consumer.accept(imitater);
		        
		        PlantCardItem.onUsePlantCard(player, heldStack, plantStack, cardItem);
			}
		});
	}
	
	public boolean isPlantTypeEqual(ItemStack stack, PlantType tmp) {
		Optional<IPlantType> opt = getImitatePlantType(stack);
		return opt.isPresent() && opt.get() == tmp;
	}
	
	@Override
	public int getBasisSunCost(ItemStack stack) {
		return super.getBasisSunCost(getDoubleStack(stack).getSecond());
	}
	
	@Override
	public ICoolDown getBasisCoolDown(ItemStack stack) {
		return super.getBasisCoolDown(getDoubleStack(stack).getSecond());
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;//can not enchant.
	}
	
	/**
	 * first is imitater card item, second is imitated card item.
	 */
	public static Pair<ItemStack, ItemStack> getDoubleStack(ItemStack stack) {
		final Container inv = getInventory(stack);
		return inv != null ? Pair.of(stack, inv.getItem(0)) : Pair.of(stack, stack);
	}

	public static Optional<IPlantType> getImitatePlantType(ItemStack stack) {
		final Container inv = getInventory(stack);
		if (inv != null) {
			final ItemStack itemstack = inv.getItem(0);
			if (itemstack.getItem() instanceof PlantCardItem card) {
				return Optional.ofNullable(card.plantType);
			}
		}
		return Optional.empty();
	}
	
	public static ItemStack getImitatedCard(ItemStack stack) {
		return getInventory(stack).getItem(0);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if(this.isEnjoyCard){
			return;
		}
		Optional<IPlantType> opt = getImitatePlantType(stack);
		if(! opt.isPresent()) {
			tooltip.add(Component.translatable("tooltip.pvz.imitater_card.empty").withStyle(ChatFormatting.RED));
		} else {
			tooltip.add(Component.translatable("tooltip.pvz.imitater_card.full", opt.get().getText().getString()).withStyle(ChatFormatting.LIGHT_PURPLE));
		    super.appendHoverText(getDoubleStack(stack).getSecond(), worldIn, tooltip, flagIn);
		}
	}
	
	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt) {
		return new InvProvider(stack);
	}

	@Nullable
	public static Container getInventory(ItemStack stack) {
		return (stack.getItem() instanceof ImitaterCardItem) ? new ItemInventory(stack, 1) {
			@Override
			public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
				return isValidImitateSlot(stack);
			}
		} : null;
	}
	
	public static boolean isValidImitateSlot(ItemStack stack) {
		return stack.getItem() instanceof PlantCardItem && !((PlantCardItem) stack.getItem()).isEnjoyCard;
	}
	
	private static class InvProvider implements ICapabilityProvider {

		private final LazyOptional<IItemHandler> opt;

		private InvProvider(ItemStack stack) {
			opt = LazyOptional.of(() -> new InvWrapper(getInventory(stack)));
		}

			@Nonnull
			@Override
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
				return ForgeCapabilities.ITEM_HANDLER.orEmpty(capability, opt);
			}
		}

}