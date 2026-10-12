package giselle.compactmachines_rst.common.mixin;

import java.util.function.BiFunction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.tunnel.TunnelWallEntity;
import giselle.compactmachines_rst.common.NodeProxyExtension;
import giselle.compactmachines_rst.common.init.ModTunnels;
import giselle.compactmachines_rst.common.network.node.TunnelWallNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@Mixin(value = TunnelWallEntity.class, remap = false)
public abstract class TunnelWallEntityMixin implements NodeProxyExtension<TunnelWallNode>
{
	@Shadow(remap = false)
	abstract TunnelDefinition getTunnelType();

	@Override
	public Class<TunnelWallNode> compactmachines_rst$getNodeClass()
	{
		return TunnelWallNode.class;
	}

	@Override
	public BiFunction<Level, BlockPos, TunnelWallNode> compactmachines_rst$getNodeFactory()
	{
		return TunnelWallNode::new;
	}

	@Override
	public boolean compactmachines_rst$canProvideCapability()
	{
		return this.getTunnelType() == ModTunnels.NETWORK_TUNNEL_DEF.get();
	}

}
