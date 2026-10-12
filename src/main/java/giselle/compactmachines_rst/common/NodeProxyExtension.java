package giselle.compactmachines_rst.common;

import java.util.function.BiFunction;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface NodeProxyExtension<NODE extends INetworkNode>
{
	Class<NODE> compactmachines_rst$getNodeClass();

	BiFunction<Level, BlockPos, NODE> compactmachines_rst$getNodeFactory();

	boolean compactmachines_rst$canProvideCapability();
}
