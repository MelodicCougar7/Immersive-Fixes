package org.github.melodiccougar7.immersivefixes.mixin;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraftforge.fml.loading.LoadingModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


// Mixin to resolve #6149: Inverted Arc Furnace Secondary Output Chances
// Updated to add compatibility with Compressed Engineering by Big Barza

@Mixin(value = ArcFurnaceRecipe.class, remap = false)
public abstract class ArcFurnaceSecondariesChanceMixin {
    @ModifyExpressionValue(
            method = "generateActualOutput",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/immersiveengineering/api/crafting/StackWithChance;chance()F"
            )
    )
    private float immersivefixes$invertSecondaryChance(float originalChance) {
        if (LoadingModList.get().getModFileById("compressedengineering") == null)
            return 1.0F - originalChance;

        else return originalChance;
    }
}