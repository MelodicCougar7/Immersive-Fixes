package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.energy.AveragingEnergyStorage;
import blusunrize.immersiveengineering.common.blocks.metal.ChargingStationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChargingStationBlockEntity.class, remap = false)
public abstract class ChargingStationBlockEntityMixin {
    @Redirect(method = "tickServer()V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/energy/AveragingEnergyStorage;extractEnergy(IZ)I"), require = 2)
    private int immersiveFixes$dirtyOnExtract(AveragingEnergyStorage storage, int energy, boolean simulate) {
        return ChunkDirty.extractEnergy((BlockEntity) (Object) this, storage, energy, simulate);
    }

    @Inject(method = "tickServer()V", at = @At(value = "FIELD", target = "Lblusunrize/immersiveengineering/common/blocks/metal/ChargingStationBlockEntity;charging:Z", opcode = Opcodes.PUTFIELD), require = 2)
    private void immersiveFixes$dirtyOnChargingFlag(CallbackInfo ci) {
        ChunkDirty.mark((BlockEntity) (Object) this);
    }
}
