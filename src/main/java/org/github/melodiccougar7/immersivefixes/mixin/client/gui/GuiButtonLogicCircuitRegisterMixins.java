package org.github.melodiccougar7.immersivefixes.mixin.client.gui;

import blusunrize.immersiveengineering.client.gui.elements.GuiButtonLogicCircuitRegister;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import org.github.melodiccougar7.immersivefixes.lib.IFLib;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Mixin to resolve #5856: Buttons in the circuit table behaving incorrectly
// Inspired by https://github.com/BluSunrize/ImmersiveEngineering/commit/8c72a302747debd42f80f6984de9db62a876589a
// The onPress handler shared by these buttons re-applies getNextStateInt(), advancing the state by one after
// charTyped sets it. Pre-decrementing every setValue in charTyped by one (with floorMod for wrap-around at WHITE)
// makes the increment land exactly on the intended entry, for both the digit branch (number+16) and the
// color-initial branch (options.get(next)).

@Mixin(GuiButtonLogicCircuitRegister.class)
public abstract class GuiButtonLogicCircuitRegisterMixins {
    @ModifyArg(
            method = "charTyped(CI)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/commons/lang3/mutable/MutableInt;setValue(I)V",
                    remap = false
            )
    )
    private int immersivefixes$compensateOnPressIncrement(int original) {
        IFLib.logMixinActive("GuiButtonLogicCircuitRegisterMixins");
        GuiButtonState<?> self = (GuiButtonState<?>) (Object) this;
        return Math.floorMod(original - 1, self.states.length);
    }
}
