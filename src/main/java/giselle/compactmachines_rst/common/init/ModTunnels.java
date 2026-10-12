package giselle.compactmachines_rst.common.init;

import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.core.Registries;
import giselle.compactmachines_rst.CompactMachines_RST;
import giselle.compactmachines_rst.common.tunnel.definitions.NetworkTunnelDefinition;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTunnels
{
	public static final DeferredRegister<TunnelDefinition> REGISTER = DeferredRegister.create(Registries.TYPES_REG_KEY, CompactMachines_RST.MODID);
	public static final RegistryObject<TunnelDefinition> NETWORK_TUNNEL_DEF = REGISTER.register("network", NetworkTunnelDefinition::new);

	private ModTunnels()
	{

	}

}
