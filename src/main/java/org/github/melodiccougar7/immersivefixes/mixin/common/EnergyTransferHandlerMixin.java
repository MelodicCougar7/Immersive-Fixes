package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.wires.localhandlers.EnergyTransferHandler;
import blusunrize.immersiveengineering.api.wires.localhandlers.EnergyTransferHandler.EnergyConnector;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EnergyTransferHandler.class, remap = false)
public abstract class EnergyTransferHandlerMixin {
    @Redirect(method = "transferPower()V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/wires/localhandlers/EnergyTransferHandler$EnergyConnector;insertEnergy(I)V"))
    private void immersiveFixes$dirtyOnInsert(EnergyConnector connector, int amount) {
        connector.insertEnergy(amount);
        if (amount != 0 && connector instanceof BlockEntity be) ChunkDirty.mark(be);
    }

    @Redirect(method = "transferPower()V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/wires/localhandlers/EnergyTransferHandler$EnergyConnector;extractEnergy(I)V"), require = 2)
    private void immersiveFixes$dirtyOnExtract(EnergyConnector connector, int amount) {
        connector.extractEnergy(amount);
        if (amount != 0 && connector instanceof BlockEntity be) ChunkDirty.mark(be);
    }
}
