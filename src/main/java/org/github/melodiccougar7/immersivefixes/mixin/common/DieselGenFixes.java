package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.energy.GeneratorFuel;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.utils.CapabilityReference;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.DieselGeneratorLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.DieselGeneratorLogic.State;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/*
Mixin to resolve #5870, #5909, #6203, #5440, #5933, and #4917 (and their duplicates) on various logic issues with the Diesel Generator.
Created by overwriting the 1.20.1 logic with the 1.21.1 logic with some changes. Using code licensed under Blu's License of Common sense:
https://github.com/BluSunrize/ImmersiveEngineering/blob/1.21.1/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/DieselGeneratorLogic.java
 */

@Mixin(DieselGeneratorLogic.class)
public abstract class DieselGenFixes implements IMultiblockLogic<State>, IServerTickableComponent<State>, IClientTickableComponent<State> {
    /**
     * @author MelodicCougar7
     * @reason Simple solution to complex list of problems
     */
    @Overwrite(remap = false)
    public void tickServer(IMultiblockContext<State> context)
    {
        final State state = context.getState();
        DieselGenAccessor accessor = (DieselGenAccessor) state;
        boolean active = context.getState().isActive();
        if(state.rsState.isEnabled(context)&&!state.tank.getFluid().isEmpty())
        {
            int output = IEServerConfig.MACHINES.dieselGen_output.get();
            List<IEnergyStorage> presentOutputs = accessor.getEnergyOutputs().stream()
                    .map(CapabilityReference::getNullable) // keep an eye on these three, I seem to recall them causing problems in the past that have since resolved themselves
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            GeneratorFuel recipe = accessor.getRecipeGetter().apply(
                    context.getLevel().getRawLevel(), state.tank.getFluid().getFluid()
            );
            if(recipe!=null&&
                    !presentOutputs.isEmpty()&&
                    EnergyHelper.distributeFlux(presentOutputs, output, true) < output)
            {
                accessor.setConsumeTick(accessor.getConsumeTick() - 1);
                if(accessor.getConsumeTick() <= 0) //Consume 10*tick-amount every 10ticks to allow for 1/10th mB amounts
                {
                    int toConsume = (10*FluidType.BUCKET_VOLUME)/recipe.getBurnTime();
                    float fluidConsumed;
                    if((fluidConsumed = state.tank.drain(toConsume, IFluidHandler.FluidAction.EXECUTE).getAmount()) > 0)
                    {
                        if(!active)
                            active = true;
                        accessor.setConsumeTick((int)(10*(fluidConsumed/toConsume)));
                    }
                    else if(active)
                        active = false;
                }
                EnergyHelper.distributeFlux(presentOutputs, output, false);
            }
            else if(active)
                active = false;
        }
        else if(active)
            active = false;

        if(active!=accessor.getActive())
        {
            accessor.setActive(active);
            context.markMasterDirty();
            context.requestMasterBESync();
        }
    }
}
