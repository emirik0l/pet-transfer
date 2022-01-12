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
            //If the contract is filled, check if the target matches the contract.
            if (entity instanceof TameableEntity && this.getPet(stack).equals(entity.getUuid())) {
                //If it does, check that the pet's owner and current user are not the same person.
                TameableEntity tameableEntity = (TameableEntity) entity;
                if (tameableEntity.getOwnerUuid() != user.getUuid()) {
                    //If they're not, perform the transfer of ownership.
                    this.transferOwnership(user, (TameableEntity) entity);
                    stack.decrement(1);
                    return ActionResult.SUCCESS;
                } else {
                    //If they are, inform the player of their mistake.
                    if (!user.getWorld().isClient) { user.sendMessage(new TranslatableText("text.transferpet.same_owner"), true); }
                    return ActionResult.PASS;
                }
            } else {
                //If it doesn't, inform the player of their mistake.
                if (!user.getWorld().isClient) { user.sendMessage(new TranslatableText("text.transferpet.wrong_pet"), true); }
                return ActionResult.PASS;
            }
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
        nbt.putString("contract_pet", entity.getUuidAsString());
        PlayerInventory inventory = player.getInventory();
        inventory.offerOrDrop(filled);
    }

    public void transferOwnership(PlayerEntity player, TameableEntity entity) {
        entity.setOwner(player);
    }

    public boolean isContractFilled(ItemStack stack) {
        return !stack.getOrCreateNbt().getString("contract_pet").isEmpty();
    }

    public UUID getPet(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return UUID.fromString(nbt.getString("contract_pet"));
    }
}

