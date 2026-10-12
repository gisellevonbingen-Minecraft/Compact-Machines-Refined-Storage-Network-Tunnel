package giselle.compactmachines_rst.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(value = BlockEntity.class, remap = true)
public abstract class BlockEntityMixin
{
	private boolean compactmachines_rst$unloaded;

	@Shadow(remap = true)
	protected Level level;
	@Shadow(remap = true)
	protected BlockPos worldPosition;

	@Inject(method = "setRemoved", remap = true, at = @At(value = "head"), cancellable = false)
	private void setRemoved(CallbackInfo ci)
	{
		if (!this.compactmachines_rst$unloaded)
		{
			this.compactmachines_rst$onRemovedNotDueToChunkUnload();
		}

	}

	@Inject(method = "clearRemoved", remap = true, at = @At(value = "head"), cancellable = false)
	private void clearRemoved(CallbackInfo ci)
	{
		this.compactmachines_rst$onclearRemoved();
	}

	@Inject(method = "onChunkUnloaded", remap = false, at = @At(value = "head"), cancellable = false)
	private void onChunkUnloaded(CallbackInfo ci)
	{
		this.compactmachines_rst$unloaded = true;
	}

	protected void compactmachines_rst$onRemovedNotDueToChunkUnload()
	{

	}

	protected void compactmachines_rst$onclearRemoved()
	{

	}

}
