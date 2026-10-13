package giselle.compactmachines_rst;

import java.util.function.BiFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.refinedmods.refinedstorage.api.network.node.INetworkNodeFactory;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeRegistry;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;

import dev.compactmods.machines.tunnel.TunnelItem;
import giselle.compactmachines_rst.common.config.ModServerConfig;
import giselle.compactmachines_rst.common.init.ModTunnels;
import giselle.compactmachines_rst.common.network.node.CompactMachineNode;
import giselle.compactmachines_rst.common.network.node.TunnelWallNode;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CompactMachines_RST.MODID)
public class CompactMachines_RST
{
	public static final String MODID = "compactmachines_rst";
	public static final Logger LOGGER = LogManager.getLogger();

	public CompactMachines_RST()
	{
		ModLoadingContext fml_context = ModLoadingContext.get();
		fml_context.registerConfig(ModConfig.Type.SERVER, ModServerConfig.SPEC);

		IEventBus fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(this::onCommonSetup);
		ModTunnels.REGISTER.register(fml_bus);

		IEventBus forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(this::onItemTooltip);
	}

	public void onCommonSetup(FMLCommonSetupEvent e)
	{
		INetworkNodeRegistry networkNodeRegistry = API.instance().getNetworkNodeRegistry();
		networkNodeRegistry.add(CompactMachineNode.ID, creatFactory(CompactMachineNode::new));
		networkNodeRegistry.add(TunnelWallNode.ID, creatFactory(TunnelWallNode::new));
	}

	private static INetworkNodeFactory creatFactory(BiFunction<Level, BlockPos, NetworkNode> factory)
	{
		return (tag, level, pos) ->
		{
			NetworkNode node = factory.apply(level, pos);
			node.read(tag);
			return node;
		};
	}

	public void onItemTooltip(ItemTooltipEvent e)
	{
		if (e.getItemStack().getItem() instanceof TunnelItem)
		{
			TunnelItem.getDefinition(e.getItemStack()).ifPresent(definition ->
			{
				if (definition == ModTunnels.NETWORK_TUNNEL_DEF.get())
				{
					e.getToolTip().add(1, Component.translatable("item.compactmachines_rst.tunnels.network.tooltip").withStyle(ChatFormatting.GRAY));
				}

			});
		}

	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(MODID, path);
	}

}
