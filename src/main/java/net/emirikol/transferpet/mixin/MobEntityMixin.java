package net.emirikol.transferpet.mixin;

import net.emirikol.transferpet.PetTransfer;
import net.emirikol.transferpet.item.PetContract;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    public MobEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "interactWithItem", at = @At("TAIL"), cancellable = true)
    protected void interactWithContract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cb) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof PetContract) {
            ActionResult result = stack.getItem().useOnEntity(stack, player, this, hand);
            cb.setReturnValue(result);
        }
    }
}
