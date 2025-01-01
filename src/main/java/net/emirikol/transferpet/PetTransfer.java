package net.emirikol.transferpet;

import net.emirikol.transferpet.item.CreativeContract;
import net.emirikol.transferpet.item.PetContract;
import net.emirikol.transferpet.component.ContractComponent;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class PetTransfer implements ModInitializer {
	public static final String MOD_ID = "transferpet";
	
	public static PetContract PET_CONTRACT;
	public static CreativeContract CREATIVE_CONTRACT;
	
	public static ComponentType<ContractComponent> CONTRACT_COMPONENT;

	@Override
	public void onInitialize() {
		//Instantiate contract.
		Identifier contract_id = Identifier.of(MOD_ID, "contract");
		RegistryKey<Item> contract_key = RegistryKey.of(RegistryKeys.ITEM, contract_id);
		Item.Settings contract_settings = new Item.Settings().useItemPrefixedTranslationKey().registryKey(contract_key);
		
		Identifier contract_creative_id = Identifier.of(MOD_ID, "contract_creative");
		RegistryKey<Item> contract_creative_key = RegistryKey.of(RegistryKeys.ITEM, contract_creative_id);
		Item.Settings contract_creative_settings = new Item.Settings().useItemPrefixedTranslationKey().registryKey(contract_creative_key);
		
		PET_CONTRACT = new PetContract(contract_settings);
		CREATIVE_CONTRACT = new CreativeContract(contract_creative_settings);
		
		// Add items to item groups.
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.add(PET_CONTRACT));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.add(CREATIVE_CONTRACT));
		
		//Register contract.
		Registry.register(Registries.ITEM, contract_id, PET_CONTRACT);
		Registry.register(Registries.ITEM, contract_creative_id, CREATIVE_CONTRACT);
		
		// Instantiate component.
		CONTRACT_COMPONENT = ComponentType.<ContractComponent>builder().codec(ContractComponent.CODEC).build();
		
		// Register component.
		Registry.register(Registries.DATA_COMPONENT_TYPE, "transferpet:contract_component", CONTRACT_COMPONENT);
	}
}
