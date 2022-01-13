package net.emirikol.transferpet.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class PetContract extends Item {
    public PetContract(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        if (this.isContractFilled(stack)) {
            return new TranslatableText("item.transferpet.filled_contract");
        } else {
            return super.getName(stack);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (this.isContractFilled(stack)) {
            Text petText = new TranslatableText("text.transferpet.tooltip_pet").formatted(Formatting.DARK_GRAY).append(this.getPetName(stack));
            tooltip.add(petText);
            Text ownerText = new TranslatableText("text.transferpet.tooltip_owner").formatted(Formatting.DARK_GRAY).append(this.getOwnerName(stack));
            tooltip.add(ownerText);
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        //Check if contract is filled.
        if (this.isContractFilled(stack)) {
            //Check that the pet does not already belong to the user.
            if (this.isTargetOwned(user, entity)) {
                if (!user.getWorld().isClient) { user.sendMessage(new TranslatableText("text.transferpet.same_owner"), true); }
                return ActionResult.PASS;
            }
            //Check if the target matches the contract.
            if (isContractValid(stack, entity)) {
                //If it does, perform the transfer of ownership.
                this.transferOwnership(user, (TameableEntity) entity);
                stack.decrement(1);
                entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 1.0F, 1.0F + (entity.world.random.nextFloat() - entity.world.random.nextFloat()) * 0.4F);
                return ActionResult.SUCCESS;
            } else {
                //If it doesn't, inform the player of their mistake.
                if (!user.getWorld().isClient) { user.sendMessage(new TranslatableText("text.transferpet.contract_invalid"), true); }
                return ActionResult.PASS;
            }
        } else {
            //If contract is not filled, check if target is owned by the player.
            if (this.isTargetOwned(user, entity)) {
                //If the target is owned by the player, fill the contract.
                this.fillContract(stack, user, (TameableEntity) entity);
                entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 1.0F, 1.0F + (entity.world.random.nextFloat() - entity.world.random.nextFloat()) * 0.4F);
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
        nbt.putString("contract_uuid", entity.getUuidAsString());
        nbt.putString("contract_name", entity.getDisplayName().getString());
        nbt.putString("contract_owner_uuid", player.getUuidAsString());
        nbt.putString("contract_owner_name", player.getDisplayName().getString());
        PlayerInventory inventory = player.getInventory();
        inventory.offerOrDrop(filled);
    }

    public void transferOwnership(PlayerEntity player, TameableEntity entity) {
        entity.setOwner(player);
    }

    public boolean isContractFilled(ItemStack stack) {
        return !stack.getOrCreateNbt().getString("contract_uuid").isEmpty();
    }

    public boolean isTargetOwned(PlayerEntity player, LivingEntity entity) {
        if (!(entity instanceof TameableEntity)) return false;
        TameableEntity tameableEntity = (TameableEntity) entity;
        return tameableEntity.getOwner() == player;
    }

    public boolean isContractValid(ItemStack stack, LivingEntity entity) {
        if (!(entity instanceof TameableEntity)) return false;
        TameableEntity tameableEntity = (TameableEntity) entity;
        return (tameableEntity.getUuid().equals(this.getPet(stack))) && (tameableEntity.getOwnerUuid().equals(this.getOwner(stack)));
    }

    public UUID getPet(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return UUID.fromString(nbt.getString("contract_uuid"));
    }

    public String getPetName(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getString("contract_name");
    }

    public UUID getOwner(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return UUID.fromString(nbt.getString("contract_owner_uuid"));
    }

    public String getOwnerName(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getString("contract_owner_name");
    }
}

