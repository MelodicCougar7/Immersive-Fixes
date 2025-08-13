package org.github.melodiccougar7.immersivefixes.mixin;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.Random;

// Mixin to resolve #6149: Inverted Arc Furnace Secondary Output Chances

@Mixin(value = ArcFurnaceRecipe.class, remap = false)
public abstract class ArcFurnaceSecondariesChanceMixin {

    /**
     * Completely replaces generateActualOutput()
     * Changed the comparison from '>' to '<'.
     * See the line further on for crediting
     * @author MelodicCougar7
     * @reason Simplest method of implementation. Please report incompatibilities to my GH.
     */

    @Overwrite
    public NonNullList<ItemStack> generateActualOutput(ItemStack input, NonNullList<ItemStack> additives, long seed) throws NoSuchFieldException, IllegalAccessException {
        // Some notable changes were made to the base patch, as shadowing and remapping are a pain. None have any impact on functionality though.

        Random random = new Random(seed);

        // Access the output field reflectively (to avoid remapping and shadowing issues)
        var outputField = ArcFurnaceRecipe.class.getDeclaredField("output");
        outputField.setAccessible(true);
        var outputLazy = (net.minecraftforge.common.util.Lazy<NonNullList<ItemStack>>) outputField.get(this);
        var output = outputLazy.get();

        // Access the secondaryOutputs field reflectively (again, to avoid remapping and shadowing issues)
        var secondaryField = ArcFurnaceRecipe.class.getDeclaredField("secondaryOutputs");
        secondaryField.setAccessible(true);
        var secondaryOutputs = (java.util.List<StackWithChance>) secondaryField.get(this);

        int remainingIndex = output.size();
        NonNullList<ItemStack> actualOutput = NonNullList.withSize(output.size() + secondaryOutputs.size(), ItemStack.EMPTY);

        for (int i = 0; i < output.size(); ++i)
            actualOutput.set(i, output.get(i).copy());

        for (StackWithChance secondary : secondaryOutputs) {

            if (secondary.chance() < random.nextFloat()) // directly referenced from IE's code under Blu's License of Common Sense. See https://github.com/BluSunrize/ImmersiveEngineering/issues/6149 and the exact source of the code, https://github.com/voidsong-dragonfly/ImmersiveEngineering/commit/5bfaecd561b46e0561b22ed1fa15363814cc39aa#diff-4c4317fb6f91bb9e2f9eeeeb219b1b71e8eb595f1711403d05834a52605f2228
                continue;
            ItemStack remaining = secondary.stack().get();
            for (ItemStack existing : actualOutput)
                if (ItemHandlerHelper.canItemStacksStack(remaining, existing)) {
                    existing.grow(remaining.getCount());
                    remaining = ItemStack.EMPTY;
                    break;
                }
            if (!remaining.isEmpty()) {
                actualOutput.set(remainingIndex, remaining);
                remainingIndex++;
            }
        }
        return actualOutput;
    }
}