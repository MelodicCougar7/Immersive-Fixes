package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.LightningRodLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.LightningRodLogic.State;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LightningRodLogic.class, remap = false)
public abstract class LightningRodLogicMixin {
    @Redirect(method = "tickServer(Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockContext;)V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/energy/MutableEnergyStorage;modifyEnergyStored(I)V"))
    private void immersiveFixes$dirtyOnOutput(MutableEnergyStorage storage, int energy, IMultiblockContext<State> context) {
        storage.modifyEnergyStored(energy);
        if (energy != 0) ChunkDirty.mark(context);
    }

    @Redirect(method = "tickServer(Lblusunrize/immersiveengineering/api/multiblocks/blocks/env/IMultiblockContext;)V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/energy/MutableEnergyStorage;setStoredEnergy(I)V"))
    private void immersiveFixes$dirtyOnStrike(MutableEnergyStorage storage, int stored, IMultiblockContext<State> context) {
        if (storage.getEnergyStored() != stored) ChunkDirty.mark(context);
        storage.setStoredEnergy(stored);
    }
}
