package org.github.melodiccougar7.immersivefixes.mixin.common;

import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.metal.CrafterPatternInventory;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AssemblerLogic;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = AssemblerLogic.class, remap = false)
public abstract class AssemblerLogicMixin {
    @Unique private CrafterPatternInventory immersiveFixes$consumingPattern;

    @Inject(method = "craftRecipes", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/AssemblerLogic;consumeIngredients(Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/AssemblerLogic$State;Ljava/util/List;Ljava/util/ArrayList;ZLblusunrize/immersiveengineering/common/blocks/multiblocks/logic/AssemblerLogic$RecipeInputSources;)Z", ordinal = 1))
    private void immersiveFixes$beginConsume(IMultiblockContext<?> ctx, CallbackInfoReturnable<List<?>> cir, @Local(name = "pattern") CrafterPatternInventory pattern) { this.immersiveFixes$consumingPattern = pattern; }

    @Inject(method = "craftRecipes", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/AssemblerLogic;consumeIngredients(Lblusunrize/immersiveengineering/common/blocks/multiblocks/logic/AssemblerLogic$State;Ljava/util/List;Ljava/util/ArrayList;ZLblusunrize/immersiveengineering/common/blocks/multiblocks/logic/AssemblerLogic$RecipeInputSources;)Z", ordinal = 1, shift = At.Shift.AFTER))
    private void immersiveFixes$endConsume(IMultiblockContext<?> ctx, CallbackInfoReturnable<List<?>> cir) { this.immersiveFixes$consumingPattern = null; }

    @ModifyArg(method = "consumeFluid", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/booleans/BooleanList;set(IZ)Z"), index = 0)
    private int immersiveFixes$fluidGridSlot(int queryIndex) { return immersiveFixes$gridSlot(queryIndex); }

    @ModifyArg(method = "consumeItem", at = @At(value = "INVOKE", target = "Ljava/util/List;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 0)
    private int immersiveFixes$itemGridSlot(int queryIndex) { return immersiveFixes$gridSlot(queryIndex); }

    @Unique private int immersiveFixes$gridSlot(int queryIndex) {
        IFLib.logMixinActive("AssemblerLogicMixin");
        CrafterPatternInventory pattern = this.immersiveFixes$consumingPattern;
        if (pattern == null) { return queryIndex; }
        int remaining = queryIndex;
        for (int slot = 0; slot < 9; slot++) {
            if (!pattern.inv.get(slot).isEmpty() && --remaining < 0) { return slot; }
        }
        return 0;
    }
}
