package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.FluidPipeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidPipeBlockEntity.class, remap = false)
public abstract class FluidPipeBlockEntityMixin {
    @Shadow
    private byte connections;

    @Inject(method = "getAvailableConnectionByte()B", at = @At("HEAD"), cancellable = true)
    private void immersiveFixes$guardNullLevel(CallbackInfoReturnable<Byte> cir) {
        if (((BlockEntity) (Object) this).getLevel() == null) cir.setReturnValue(connections);
    }
}
