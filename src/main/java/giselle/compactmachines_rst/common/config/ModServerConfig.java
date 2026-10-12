package giselle.compactmachines_rst.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ModServerConfig
{
	public static final ForgeConfigSpec SPEC;
	public static final IntValue COMPACT_MACHINE_USAGE;
	public static final IntValue NETWORK_TUNNEL_USAGE;

	static
	{
		var builder = new ForgeConfigSpec.Builder();

		builder.push("CompactMachine");
		COMPACT_MACHINE_USAGE = builder.defineInRange("usage", 0, 0, Integer.MAX_VALUE);
		builder.pop();

		builder.push("NetworkTunnel");
		NETWORK_TUNNEL_USAGE = builder.defineInRange("usage", 0, 0, Integer.MAX_VALUE);
		builder.pop();

		SPEC = builder.build();
	}

}
