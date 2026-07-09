package org.github.melodiccougar7.immersivefixes.mixin.gui;

import blusunrize.immersiveengineering.client.gui.elements.GuiButtonLogicCircuitRegister;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.util.List;

// Mixin to resolve #5856: Buttons in the circuit table behaving incorrectly
// Inspired by https://github.com/BluSunrize/ImmersiveEngineering/commit/8c72a302747debd42f80f6984de9db62a876589a

@Mixin(GuiButtonLogicCircuitRegister.class)
public abstract class GuiButtonLogicCircuitRegisterMixins {

    @ModifyConstant(
        method = "charTyped(CI)Z",
        constant = @Constant(intValue = 16)
    )
    private int colorValueOffset(int original) {
        return 15;
    }

    @ModifyArg(
            method = "charTyped(CI)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/commons/lang3/mutable/MutableInt;setValue(I)V"
            )
    )
    private int modifySetValueArgument(int original) {
        GuiButtonState<?> self = (GuiButtonState<?>)(Object)this;
        return Math.floorMod(original - 1, self.states.length);
        //Math.floorMod(options.get(next)-1, states.length); //hmmm
    }
}