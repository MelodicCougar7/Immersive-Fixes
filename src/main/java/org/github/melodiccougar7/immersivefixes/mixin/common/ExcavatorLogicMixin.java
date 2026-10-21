package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.ExcavatorLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.ExcavatorLogic.State;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ExcavatorLogic.class, remap = false)
public abstract class ExcavatorLogicMixin {
    @Redirect(method = "tickServer(Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockContext;)V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/energy/MutableEnergyStorage;extractEnergy(IZ)I"), require = 2)
    private int immersiveFixes$dirtyOnExtract(MutableEnergyStorage storage, int maxExtract, boolean simulate, IMultiblockContext<State> context) {
        return ChunkDirty.extractEnergy(context, storage, maxExtract, simulate);
    }
}
