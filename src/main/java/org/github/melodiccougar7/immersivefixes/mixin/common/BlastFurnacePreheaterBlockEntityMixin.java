package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.blocks.metal.BlastFurnacePreheaterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlastFurnacePreheaterBlockEntity.class, remap = false)
public abstract class BlastFurnacePreheaterBlockEntityMixin {
    @Redirect(method = "doSpeedup()I", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/energy/MutableEnergyStorage;extractEnergy(IZ)I"), require = 2)
    private int immersiveFixes$dirtyOnExtract(MutableEnergyStorage storage, int maxExtract, boolean simulate) {
        return ChunkDirty.extractEnergy((BlockEntity) (Object) this, storage, maxExtract, simulate);
    }

    @Inject(method = "turnOff()Ljava/lang/Void;", at = @At(value = "FIELD", target = "Lblusunrize/immersiveengineering/common/blocks/metal/BlastFurnacePreheaterBlockEntity;active:Z", opcode = Opcodes.PUTFIELD))
    private void immersiveFixes$dirtyOnTurnOff(CallbackInfoReturnable<Void> cir) {
        ChunkDirty.mark((BlockEntity) (Object) this);
    }
}
