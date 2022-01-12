package net.emirikol.transferpet.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.UUID;

public class PetContract extends Item {
    public PetContract(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        //Check if contract is filled.
        if (this.isContractFilled(stack)) {
            return super.useOnEntity(stack, user, entity, hand);
            //TODO
        } else {
            //If contract is not filled, check if target is owned by the player.
            if (entity instanceof TameableEntity && ((TameableEntity) entity).getOwner() == user) {
                //If the target is owned by the player, fill the contract.
                this.fillContract(stack, user, (TameableEntity) entity);
                return ActionResult.SUCCESS;
            } else {
                //If the target is not owned by the player, inform them of their mistake.
                if (!user.getWorld().isClient) { user.sendMessage(new TranslatableText("text.transferpet.contract_fail"), true); }
                return ActionResult.PASS;
            }
        }

    }

    public void fillContract(ItemStack stack, PlayerEntity player, TameableEntity entity) {
        ItemStack filled = new ItemStack(this, 1);
        stack.decrement(1);
        NbtCompound nbt = filled.getOrCreateNbt();
        nbt.putInt("CustomModelData", 1);
        nbt.putString("contract_owner", player.getUuidAsString());
        nbt.putString("contact_pet", entity.getUuidAsString());
        PlayerInventory inventory = player.getInventory();
        inventory.offerOrDrop(filled);
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

