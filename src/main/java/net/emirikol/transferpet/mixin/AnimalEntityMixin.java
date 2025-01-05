package net.emirikol.transferpet.mixin;

import net.emirikol.transferpet.item.PetContract;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
    public AnimalEntityMixin(EntityType<? extends PassiveEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "interactMob", at = @At("TAIL"), cancellable = true)
    protected void interactWithContract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cb) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof PetContract) {
            ActionResult result = stack.getItem().useOnEntity(stack, player, this, hand);
            cb.setReturnValue(result);
        }
    }
}
