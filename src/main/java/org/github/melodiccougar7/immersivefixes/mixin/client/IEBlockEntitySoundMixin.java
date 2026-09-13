package org.github.melodiccougar7.immersivefixes.mixin.client;

import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import blusunrize.immersiveengineering.common.util.sound.IEBlockEntitySound;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = IEBlockEntitySound.class, remap = false)
public abstract class IEBlockEntitySoundMixin {
    @ModifyExpressionValue(method = "evaluateVolume()V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/items/EarmuffsItem;getVolumeMod(Lnet/minecraft/world/item/ItemStack;)F"))
    private float immersiveFixes$leaveEarmuffsToEngine(float mod) {
        IFLib.logMixinActive("IEBlockEntitySoundMixin");
        return 1.0F;
    }
}
