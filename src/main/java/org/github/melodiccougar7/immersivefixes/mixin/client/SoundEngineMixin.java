package org.github.melodiccougar7.immersivefixes.mixin.client;

import org.github.melodiccougar7.immersivefixes.client.EarmuffHandler;
import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Inject(method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F", at = @At("RETURN"), cancellable = true)
    private void immersiveFixes$muffleLiveVolume(SoundInstance p_120328_, CallbackInfoReturnable<Float> cir) {
        IFLib.logMixinActive("SoundEngineMixin");
        cir.setReturnValue(EarmuffHandler.apply(cir.getReturnValue(), p_120328_));
    }

    @ModifyExpressionValue(method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundEngine;calculateVolume(FLnet/minecraft/sounds/SoundSource;)F"))
    private float immersiveFixes$muffleStartVolume(float volume, @Local(argsOnly = true) SoundInstance sound) { return EarmuffHandler.apply(volume, sound); }
}
