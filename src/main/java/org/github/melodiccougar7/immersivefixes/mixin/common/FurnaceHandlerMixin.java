package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FurnaceHandler;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FurnaceHandler.class, remap = false)
public abstract class FurnaceHandlerMixin {
    @Shadow
    private int burnTime;

    @Unique
    private int immersiveFixes$burnTimeAtStart;

    @Inject(method = "tickServer(Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockContext;)Z", at = @At("HEAD"))
    private void immersiveFixes$recordBurnTime(IMultiblockContext<?> ctx, CallbackInfoReturnable<Boolean> cir) {
        immersiveFixes$burnTimeAtStart = burnTime;
    }

    @Inject(method = "tickServer(Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockContext;)Z", at = @At("RETURN"))
    private void immersiveFixes$dirtyWhileBurning(IMultiblockContext<?> ctx, CallbackInfoReturnable<Boolean> cir) {
        if (burnTime != immersiveFixes$burnTimeAtStart) ChunkDirty.mark(ctx);
    }
}
