package giselle.compactmachines_rst.common.network.node;

import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;

import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.location.IDimensionalBlockPosition;
import dev.compactmods.machines.tunnel.TunnelItem;
import dev.compactmods.machines.tunnel.TunnelWallEntity;
import giselle.compactmachines_rst.CompactMachines_RST;
import giselle.compactmachines_rst.common.config.ModServerConfig;
import giselle.compactmachines_rst.common.init.ModTunnels;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TunnelWallNode extends NetworkNode
{
	public static final ResourceLocation ID = CompactMachines_RST.rl("tunnel");

	public TunnelWallNode(Level level, BlockPos pos)
	{
		super(level, pos);
	}

	@Override
	public void visit(Operator operator)
	{
		super.visit(operator);

		if (this.level.getBlockEntity(this.pos) instanceof TunnelWallEntity tunnelWall)
		{
			MinecraftServer server = this.level.getServer();
			ServerLevel compactLevel = CompactDimension.forServer(server);

			if (compactLevel == null)
			{
				return;
			}

			IDimensionalBlockPosition machinePos = tunnelWall.getConnectedPosition();
			ServerLevel machineLevel = machinePos.level(server);

			if (machineLevel == null)
			{
				return;
			}

			operator.apply(machineLevel, machinePos.getBlockPosition(), null);
		}

	}

	@Override
	public ItemStack getItemStack()
	{
		return TunnelItem.createStack(ModTunnels.NETWORK_TUNNEL_DEF.get());
	}

	@Override
	public int getEnergyUsage()
	{
		return ModServerConfig.NETWORK_TUNNEL_USAGE.get();
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

}
