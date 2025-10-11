package org.github.melodiccougar7.immersivefixes.mixin;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic.State;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Accessor to help resolve #6305: Coke Oven Exploit. I reported and patched this to the main IE branch (https://github.com/BluSunrize/ImmersiveEngineering/pull/6305), so this code is my work licensed to IE under Blu's License of Common Sense.

@Mixin(value = CokeOvenLogic.class, remap = false)
public abstract class CokeOvenLogicFix {
    @Inject(method = "tickServer", at = @At("HEAD"))
    private void immersiveFixes$resetProcessIfEmpty(IMultiblockContext<State> context, CallbackInfo ci) {
        State state = context.getState();
        ItemStack input = state.getInventory().getStackInSlot(CokeOvenLogic.INPUT_SLOT);

        if (input.isEmpty()) {
            CokeOvenStateAccessor accessor = (CokeOvenStateAccessor) (Object) state;
            accessor.setProcess(0);
            accessor.setProcessMax(0);
            context.markMasterDirty();
        }
    }
}
