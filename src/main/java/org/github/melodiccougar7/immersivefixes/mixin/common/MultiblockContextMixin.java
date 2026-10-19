package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.multiblocks.blockimpl.MultiblockBEHelperMaster;
import blusunrize.immersiveengineering.common.blocks.multiblocks.blockimpl.MultiblockContext;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = MultiblockContext.class, remap = false)
public abstract class MultiblockContextMixin {
    @Shadow
    @Final
    private MultiblockBEHelperMaster<?> masterHelper;

    @ModifyVariable(method = "registerCapability(Ljava/lang/Object;)Lnet/minecraftforge/common/util/LazyOptional;", at = @At("HEAD"), argsOnly = true)
    private Object immersiveFixes$wrapStorage(Object value) {
        return ChunkDirty.wrap(value, masterHelper.getMasterBE());
    }
}
