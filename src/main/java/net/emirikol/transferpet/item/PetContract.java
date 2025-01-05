package net.emirikol.transferpet.item;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class PetContract extends Item {
    public PetContract(Settings settings) {
        super(settings);
    }

    public void fillContract(ItemStack stack, PlayerEntity player, TameableEntity entity) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("CustomModelData", 1);
        nbt.putString("contract_owner", player.getUuidAsString());
        nbt.putString("contact_pet", entity.getUuidAsString());
    }

    public boolean isContractFilled(ItemStack stack) {
        return stack.getOrCreateNbt().getString("contract_owner").length() > 0;
    }

    public UUID getOwner(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return UUID.fromString(nbt.getString("contract_owner"));
    }

    public UUID getPet(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return UUID.fromString(nbt.getString("contract_pet"));
    }
}

