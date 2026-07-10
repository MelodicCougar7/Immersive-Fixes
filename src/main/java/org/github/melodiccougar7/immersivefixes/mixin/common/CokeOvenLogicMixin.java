package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic.State;
import net.minecraft.world.item.ItemStack;
import org.github.melodiccougar7.immersivefixes.lib.IFLib;
import org.github.melodiccougar7.immersivefixes.mixin.common.helper.CokeOvenLogicAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Accessor to help resolve #6305: Coke Oven Exploit. I reported and patched this to the main IE branch (https://github.com/BluSunrize/ImmersiveEngineering/pull/6305), so this code is my work licensed to IE under Blu's License of Common Sense.

@Mixin(value = CokeOvenLogic.class, remap = false)
public abstract class CokeOvenLogicMixin {
    @Inject(method = "tickServer", at = @At("HEAD"))
    private void immersiveFixes$resetProcessIfEmpty(IMultiblockContext<State> context, CallbackInfo ci) {
        IFLib.logMixinActive("CokeOvenLogicFix");
        State state = context.getState();
        ItemStack input = state.getInventory().getStackInSlot(CokeOvenLogic.INPUT_SLOT);

        if (input.isEmpty()) {
            CokeOvenLogicAccessor accessor = (CokeOvenLogicAccessor) state;
            if (accessor.getProcess() == 0 && accessor.getProcessMax() == 0) { return; }
            accessor.setProcess(0);
            accessor.setProcessMax(0);
            context.markMasterDirty();
        }
    }
}
