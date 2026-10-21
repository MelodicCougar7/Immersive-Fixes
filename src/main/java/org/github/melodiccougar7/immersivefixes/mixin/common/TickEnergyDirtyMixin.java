package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.ElectromagnetBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.FluidPumpBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.FurnaceHeaterBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.TeslaCoilBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.TurretBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {FurnaceHeaterBlockEntity.class, TurretBlockEntity.class, ElectromagnetBlockEntity.class, TeslaCoilBlockEntity.class, EnergyConnectorBlockEntity.class, ClocheBlockEntity.class, FluidPumpBlockEntity.class}, remap = false)
public abstract class TickEnergyDirtyMixin {
    @Redirect(method = "tickServer()V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/energy/MutableEnergyStorage;extractEnergy(IZ)I"))
    private int immersiveFixes$dirtyOnExtract(MutableEnergyStorage storage, int maxExtract, boolean simulate) {
        return ChunkDirty.extractEnergy((BlockEntity) (Object) this, storage, maxExtract, simulate);
    }
}
