package giselle.compactmachines_rst.common.network.node;

import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;

import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.machine.CompactMachineBlockEntity;
import dev.compactmods.machines.tunnel.Tunnels;
import dev.compactmods.machines.tunnel.graph.TunnelConnectionGraph;
import giselle.compactmachines_rst.CompactMachines_RST;
import giselle.compactmachines_rst.common.config.ModServerConfig;
import giselle.compactmachines_rst.common.init.ModTunnels;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class CompactMachineNode extends NetworkNode
{
	public static final ResourceLocation ID = CompactMachines_RST.rl("compact_machine");

	public CompactMachineNode(Level level, BlockPos pos)
	{
		super(level, pos);
	}

	@Override
	public void visit(Operator operator)
	{
		super.visit(operator);

		if (this.level.getBlockEntity(this.pos) instanceof CompactMachineBlockEntity compactMachine)
		{
			ServerLevel compactLevel = CompactDimension.forServer(this.level.getServer());

			if (compactLevel == null)
			{
				return;
			}

			ChunkPos roomChunk = compactMachine.getConnectedRoom().orElse(null);

			if (roomChunk == null)
			{
				return;
			}

			TunnelConnectionGraph tunnelGraph = TunnelConnectionGraph.forRoom(compactLevel, roomChunk);
			tunnelGraph.getTunnelNodesByType(Tunnels.getRegistryId(ModTunnels.NETWORK_TUNNEL_DEF.get())).forEach(node ->
			{
				operator.apply(compactLevel, node.position(), null);
			});

		}

	}

	@Override
	public int getEnergyUsage()
	{
		return ModServerConfig.COMPACT_MACHINE_USAGE.get();
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

}
