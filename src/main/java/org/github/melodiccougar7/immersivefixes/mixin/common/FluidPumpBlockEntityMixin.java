package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.blocks.metal.FluidPumpBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FluidPumpBlockEntity.class, remap = false)
public abstract class FluidPumpBlockEntityMixin {
    @Redirect(method = {"tickServer()V", "gatherInfiniteFluidFromWorld(Lnet/minecraft/core/Direction;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/capability/templates/FluidTank;fill(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)I"), require = 4)
    private int immersiveFixes$dirtyOnFill(FluidTank tank, FluidStack resource, IFluidHandler.FluidAction action) {
        return ChunkDirty.fill((BlockEntity) (Object) this, tank, resource, action);
    }

    @Redirect(method = {"gatherInfiniteFluidFromWorld(Lnet/minecraft/core/Direction;)V", "outputFluid(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)I", "canOutputPressurized(Z)Z"}, at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/energy/MutableEnergyStorage;extractEnergy(IZ)I"), require = 7)
    private int immersiveFixes$dirtyOnExtract(MutableEnergyStorage storage, int maxExtract, boolean simulate) {
        return ChunkDirty.extractEnergy((BlockEntity) (Object) this, storage, maxExtract, simulate);
    }
}
