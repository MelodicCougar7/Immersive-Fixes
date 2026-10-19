package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.FluidPumpBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {ClocheBlockEntity.class, FluidPumpBlockEntity.class}, remap = false)
public abstract class TickTankDrainDirtyMixin {
    @Redirect(method = "tickServer()V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/capability/templates/FluidTank;drain(ILnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;"))
    private FluidStack immersiveFixes$dirtyOnDrain(FluidTank tank, int maxDrain, IFluidHandler.FluidAction action) {
        return ChunkDirty.drain((BlockEntity) (Object) this, tank, maxDrain, action);
    }
}
