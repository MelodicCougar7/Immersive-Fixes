package org.github.melodiccougar7.immersivefixes.mixin.client.gui;

import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import blusunrize.immersiveengineering.client.gui.elements.GuiButtonLogicCircuitRegister;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiButtonLogicCircuitRegister.class)
public abstract class GuiButtonLogicCircuitRegisterMixins {
    @ModifyArg(method = "charTyped(CI)Z", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableInt;setValue(I)V", remap = false))
    private int immersivefixes$compensateDigitPress(int original) { return immersivefixes$beforePress(original); }

    @ModifyArg(method = "charTyped(CI)Z", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableInt;setValue(Ljava/lang/Number;)V", remap = false))
    private Number immersivefixes$compensateColorPress(Number original) { return immersivefixes$beforePress(original.intValue()); }

    @Unique private int immersivefixes$beforePress(int target) {
        IFLib.logMixinActive("GuiButtonLogicCircuitRegisterMixins");
        GuiButtonState<?> self = (GuiButtonState<?>) (Object) this;
        return Math.floorMod(target - 1, self.states.length);
    }
}
