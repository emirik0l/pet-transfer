package net.emirikol.transferpet.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class CreativeContract extends PetContract {
    public CreativeContract(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canSteal(LivingEntity entity) {
        return isValidEntity(entity);
    }
}
