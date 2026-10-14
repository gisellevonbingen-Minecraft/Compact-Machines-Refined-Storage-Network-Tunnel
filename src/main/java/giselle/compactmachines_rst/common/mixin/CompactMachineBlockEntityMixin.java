package giselle.compactmachines_rst.common.mixin;

import java.util.function.BiFunction;

import org.spongepowered.asm.mixin.Mixin;

import dev.compactmods.machines.machine.CompactMachineBlockEntity;
import giselle.compactmachines_rst.common.INetworkNodeProxyExtension;
import giselle.compactmachines_rst.common.network.node.CompactMachineNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@Mixin(value = CompactMachineBlockEntity.class, remap = false)
public abstract class CompactMachineBlockEntityMixin implements INetworkNodeProxyExtension<CompactMachineNode>
{
	@Override
	public Class<CompactMachineNode> compactmachines_rst$getNodeClass()
	{
		return CompactMachineNode.class;
	}

	@Override
	public BiFunction<Level, BlockPos, CompactMachineNode> compactmachines_rst$getNodeFactory()
	{
		return CompactMachineNode::new;
	}

	@Override
	public boolean compactmachines_rst$canProvideCapability()
	{
		// return compactmachines_rst.getTunnelNodes(this.self).findAny().isPresent();
		return true;
	}

}
