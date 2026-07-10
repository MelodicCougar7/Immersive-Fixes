package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.energy.GeneratorFuel;
import blusunrize.immersiveengineering.api.utils.CapabilityReference;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.DieselGeneratorLogic;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.BiFunction;

@Mixin(DieselGeneratorLogic.State.class)
public interface DieselGenAccessor {
    @Accessor("energyOutputs")
    List<CapabilityReference<IEnergyStorage>> getEnergyOutputs();

    @Accessor("recipeGetter")
    BiFunction<Level, Fluid, GeneratorFuel> getRecipeGetter();

    @Accessor("consumeTick")
    int getConsumeTick();

    @Accessor("consumeTick")
    void setConsumeTick(int value);

    @Accessor("active")
    boolean getActive();

    @Accessor("active")
    void setActive(boolean value);
}
