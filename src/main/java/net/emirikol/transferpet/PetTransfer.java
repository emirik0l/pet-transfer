package net.emirikol.transferpet;

import net.emirikol.transferpet.item.CreativeContract;
import net.emirikol.transferpet.item.PetContract;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class PetTransfer implements ModInitializer {
	public static PetContract PET_CONTRACT;
	public static CreativeContract CREATIVE_CONTRACT;

	@Override
	public void onInitialize() {
		//Instantiate contract.
		FabricItemSettings petContractSettings = new FabricItemSettings();
		petContractSettings.group(ItemGroup.MISC);
		PET_CONTRACT = new PetContract(petContractSettings);
		CREATIVE_CONTRACT = new CreativeContract(petContractSettings);
		//Register contract.
		Registry.register(Registry.ITEM, "transferpet:contract", PET_CONTRACT);
		Registry.register(Registry.ITEM, "transferpet:contract_creative", CREATIVE_CONTRACT);
	}
}
