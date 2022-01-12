package net.emirikol.transferpet;

import net.emirikol.transferpet.item.PetContract;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class PetTransfer implements ModInitializer {
	public static PetContract PET_CONTRACT;

	@Override
	public void onInitialize() {
		//Instantiate contract.
		FabricItemSettings petContractSettings = new FabricItemSettings();
		petContractSettings.group(ItemGroup.MISC);
		PET_CONTRACT = new PetContract(petContractSettings);
		//Register contract.
		Registry.register(Registry.ITEM, "transfer:contract", PET_CONTRACT);
	}
}
