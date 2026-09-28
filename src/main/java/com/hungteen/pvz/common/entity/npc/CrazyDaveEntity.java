package com.hungteen.pvz.common.entity.npc;

import com.hungteen.pvz.common.container.provider.PVZContainerProvider;
import com.hungteen.pvz.common.container.shop.DaveShopContainer;
import com.hungteen.pvz.utils.StringUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class CrazyDaveEntity extends AbstractDaveEntity {

	public CrazyDaveEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.transactionResource = StringUtil.prefix("crazy_dave");
	}

	@Override
	protected void openContainer(ServerPlayer player) {
		NetworkHooks.openScreen(player, new PVZContainerProvider() {

			@Override
			public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, Inventory inventory,
										Player playerEntity) {
				return new DaveShopContainer(id, playerEntity, CrazyDaveEntity.this.getId());
			}

		}, buffer -> {
			buffer.writeInt(CrazyDaveEntity.this.getId());
		});
	}

}