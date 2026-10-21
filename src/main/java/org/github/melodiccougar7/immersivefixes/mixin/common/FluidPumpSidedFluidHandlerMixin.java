package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.FluidPumpBlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "blusunrize.immersiveengineering.common.blocks.metal.FluidPumpBlockEntity$SidedFluidHandler", remap = false)
public abstract class FluidPumpSidedFluidHandlerMixin {
    @Shadow
    FluidPumpBlockEntity pump;

    @Redirect(method = "fill(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)I", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/capability/templates/FluidTank;fill(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)I"))
    private int immersiveFixes$dirtyOnFill(FluidTank tank, FluidStack resource, IFluidHandler.FluidAction action) {
        return ChunkDirty.fill(pump, tank, resource, action);
    }

    @Redirect(method = "drain(ILnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/capability/templates/FluidTank;drain(ILnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;"))
    private FluidStack immersiveFixes$dirtyOnDrain(FluidTank tank, int maxDrain, IFluidHandler.FluidAction action) {
        return ChunkDirty.drain(pump, tank, maxDrain, action);
    }
}
