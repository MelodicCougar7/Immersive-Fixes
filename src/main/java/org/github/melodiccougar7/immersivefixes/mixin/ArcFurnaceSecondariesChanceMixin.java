package org.github.melodiccougar7.immersivefixes.mixin;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

// Mixin to resolve #6149: Inverted Arc Furnace Secondary Output Chances
// Updated to add compatibility with Compressed Engineering by Big Barza

@Mixin(value = ArcFurnaceRecipe.class, remap = false)
public abstract class ArcFurnaceSecondariesChanceMixin {

    @Redirect(
        method = "generateActualOutput(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/NonNullList;J)Lnet/minecraft/core/NonNullList;",
        at = @At(
            value = "INVOKE",
            target = "Lblusunrize/immersiveengineering/api/crafting/StackWithChance;chance()F"
        )
    )
    private float invertChance(StackWithChance instance) {
        return 1.0F - instance.chance();
    }
}