package giselle.compactmachines_rst.common.tunnel.definitions;

import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.api.tunnels.TunnelPosition;
import dev.compactmods.machines.api.tunnels.lifecycle.TunnelInstance;
import dev.compactmods.machines.api.tunnels.lifecycle.TunnelTeardownHandler;
import giselle.compactmachines_rst.common.INetworkNodeProxyExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class NetworkTunnelDefinition implements TunnelDefinition, TunnelTeardownHandler<TunnelInstance>
{
	@Override
	public int ringColor()
	{
		return 0xFF37E3DB;
	}

	@Override
	public TunnelInstance newInstance(BlockPos pos, Direction direction)
	{
		return new TunnelInstance()
		{
		};
	}

	@Override
	public void onRemoved(TunnelPosition position, TunnelInstance instance)
	{
		if (position.level().getBlockEntity(position.pos()) instanceof INetworkNodeProxyExtension extension)
		{
			extension.compactmachines_rst$removeNode();
		}

	}

}
