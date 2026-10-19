package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.CapacitorBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.CapacitorCreativeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.IEnergyStorage;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CapacitorBlockEntity.class, remap = false)
public abstract class CapacitorBlockEntityMixin {
    @Redirect(method = "transferEnergy(Lnet/minecraft/core/Direction;)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/energy/IEnergyStorage;extractEnergy(IZ)I"))
    private int immersiveFixes$dirtyOnOutput(IEnergyStorage storage, int maxExtract, boolean simulate) {
        if ((Object) this instanceof CapacitorCreativeBlockEntity) return storage.extractEnergy(maxExtract, simulate);
        return ChunkDirty.extractEnergy((BlockEntity) (Object) this, storage, maxExtract, simulate);
    }
}
