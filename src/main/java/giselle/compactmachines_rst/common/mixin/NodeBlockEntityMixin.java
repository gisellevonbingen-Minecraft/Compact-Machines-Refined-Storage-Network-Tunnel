package giselle.compactmachines_rst.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeManager;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeProxy;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.capability.NetworkNodeProxyCapability;

import dev.compactmods.machines.machine.CompactMachineBlockEntity;
import dev.compactmods.machines.tunnel.TunnelWallEntity;
import giselle.compactmachines_rst.common.NodeProxyExtension;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

@Mixin(value = {CompactMachineBlockEntity.class, TunnelWallEntity.class}, remap = false)
public abstract class NodeBlockEntityMixin<NODE extends INetworkNode> extends BlockEntityMixin implements INetworkNodeProxy<NODE>
{
	private final LazyOptional<INetworkNodeProxy<NODE>> networkNodeProxy = LazyOptional.of(() -> this);
	@SuppressWarnings("unchecked")
	private final NodeProxyExtension<NODE> extension = (NodeProxyExtension<NODE>) (Object) this;

	private NODE clientNode;
	private NODE removedNode;

	@Override
	public NODE getNode()
	{
		if (this.level.isClientSide)
		{
			if (this.clientNode == null)
			{
				this.clientNode = this.compactmachines_rst$createNode();
			}

			return this.clientNode;
		}

		INetworkNodeManager manager = API.instance().getNetworkNodeManager((ServerLevel) this.level);
		INetworkNode node = manager.getNode(this.worldPosition);
		Class<NODE> nodeClass = this.extension.compactmachines_rst$getNodeClass();

		if (nodeClass.isInstance(node))
		{
			return nodeClass.cast(node);
		}
		else
		{
			return this.compactmachines_rst$createAndSetNode(manager);
		}

	}

	private NODE compactmachines_rst$createNode()
	{
		return this.extension.compactmachines_rst$getNodeFactory().apply(this.level, this.worldPosition);
	}

	private NODE compactmachines_rst$createAndSetNode(INetworkNodeManager manager)
	{
		NODE node = this.compactmachines_rst$createNode();
		manager.setNode(this.worldPosition, node);
		manager.markForSaving();
		return node;
	}

	@Inject(method = "getCapability", remap = false, at = @At(value = "head"), cancellable = true)
	private <T> void getCapability(Capability<T> cap, Direction direction, CallbackInfoReturnable<LazyOptional<T>> cir)
	{
		if (cap == NetworkNodeProxyCapability.NETWORK_NODE_PROXY_CAPABILITY)
		{
			if (this.extension.compactmachines_rst$canProvideCapability())
			{
				cir.setReturnValue(this.networkNodeProxy.cast());
			}

		}

	}

	@Override
	protected void compactmachines_rst$onRemovedNotDueToChunkUnload()
	{
		super.compactmachines_rst$onRemovedNotDueToChunkUnload();

		if (this.level instanceof ServerLevel level)
		{
			INetworkNodeManager manager = API.instance().getNetworkNodeManager(level);
			INetworkNode node = manager.getNode(this.worldPosition);
			Class<NODE> nodeClass = this.extension.compactmachines_rst$getNodeClass();
			INetwork network = null;

			if (nodeClass.isInstance(node))
			{
				network = node.getNetwork();
				this.removedNode = nodeClass.cast(node);
			}
			else
			{
				this.removedNode = null;
			}

			manager.removeNode(this.worldPosition);
			manager.markForSaving();

			if (node != null && network != null)
			{
				network.getNodeGraph().invalidate(Action.PERFORM, network.getLevel(), network.getPosition());
			}

		}

	}

	@Override
	protected void compactmachines_rst$onClearRemoved()
	{
		super.compactmachines_rst$onClearRemoved();

		if (this.level instanceof ServerLevel level)
		{
			INetworkNodeManager manager = API.instance().getNetworkNodeManager(level);

			if (manager.getNode(this.worldPosition) == null)
			{
				manager.setNode(this.worldPosition, this.compactmachines_rst$createNode());
				manager.markForSaving();
			}

		}

	}

}
