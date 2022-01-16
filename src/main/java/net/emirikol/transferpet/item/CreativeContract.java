package net.emirikol.transferpet.item;

import net.minecraft.entity.LivingEntity;

public class CreativeContract extends PetContract {
    public CreativeContract(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canSteal(LivingEntity entity) {
        return isValidEntity(entity);
    }
}
