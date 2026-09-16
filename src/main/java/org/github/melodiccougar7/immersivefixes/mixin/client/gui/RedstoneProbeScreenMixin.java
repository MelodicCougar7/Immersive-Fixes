package org.github.melodiccougar7.immersivefixes.mixin.client.gui;

import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import blusunrize.immersiveengineering.client.gui.RedstoneProbeScreen;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonBoolean;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RedstoneProbeScreen.class, remap = false)
public abstract class RedstoneProbeScreenMixin {
    @WrapOperation(method = "drawGuiContainerForegroundLayer(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/client/gui/elements/GuiButtonBoolean;isHoveredOrFocused()Z", remap = true), require = 2)
    private boolean immersiveFixes$hoveredOnly(GuiButtonBoolean button, Operation<Boolean> original) {
        IFLib.logMixinActive("RedstoneProbeScreenMixin");
        return button.isHovered();
    }
}
