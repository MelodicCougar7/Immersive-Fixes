package org.github.melodiccougar7.immersivefixes.mixin.client.gui;

import blusunrize.immersiveengineering.client.gui.ArcFurnaceScreen;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ArcFurnaceScreen.class, remap = false)
public abstract class ArcFurnaceScreenMixin {
    @WrapOperation(method = "gatherAdditionalTooltips(IILjava/util/function/Consumer;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/client/gui/elements/GuiButtonIE;isHoveredOrFocused()Z", remap = true))
    private boolean immersiveFixes$hoveredOnly(GuiButtonIE button, Operation<Boolean> original) {
        if (button.isHovered()) {
            ((ArcFurnaceScreen) (Object) this).getMenu().getCarried().isEmpty();
        }
        return false;
    }
}
