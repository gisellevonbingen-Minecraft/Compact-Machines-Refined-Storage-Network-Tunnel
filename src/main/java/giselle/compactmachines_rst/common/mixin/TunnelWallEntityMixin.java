package giselle.compactmachines_rst.common.mixin;

import java.util.function.BiFunction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeManager;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.apiimpl.API;

import dev.compactmods.machines.api.location.IDimensionalBlockPosition;
import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.location.LevelBlockPosition;
import dev.compactmods.machines.tunnel.TunnelWallEntity;
import giselle.compactmachines_rst.common.INetworkNodeProxyExtension;
import giselle.compactmachines_rst.common.init.ModTunnels;
import giselle.compactmachines_rst.common.network.node.CompactMachineNode;
import giselle.compactmachines_rst.common.network.node.TunnelWallNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = TunnelWallEntity.class, remap = false)
public abstract class TunnelWallEntityMixin extends BlockEntity implements INetworkNodeProxyExtension<TunnelWallNode>
{
	@Shadow(remap = false)
	private LevelBlockPosition connectedMachine;
	@Shadow(remap = false)
	private TunnelDefinition tunnelType;

	public TunnelWallEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_)
	{
		super(p_155228_, p_155229_, p_155230_);
	}

	@Inject(method = "setConnectedTo", remap = false, at = @At(value = "tail"), cancellable = false)
	private void setConnectedTo(IDimensionalBlockPosition machine, Direction side, CallbackInfo ci)
	{
		if (this.level instanceof ServerLevel compactLevel)
		{
			if (this.connectedMachine == null)
			{
				return;
			}

			ServerLevel machineLevel = compactLevel.getServer().getLevel(this.connectedMachine.dimensionKey());

			if (machineLevel == null)
			{
				return;
			}

			INetworkNodeManager manager = API.instance().getNetworkNodeManager(machineLevel);

			if (manager.getNode(this.connectedMachine.getBlockPosition()) instanceof CompactMachineNode node)
			{
				INetwork network = node.getNetwork();

				if (network == null)
				{
					return;
				}

				network.getNodeGraph().invalidate(Action.PERFORM, network.getLevel(), network.getPosition());
			}

		}

	}

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
		return this.tunnelType == ModTunnels.NETWORK_TUNNEL_DEF.get();
	}

}
