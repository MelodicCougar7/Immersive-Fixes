package org.github.melodiccougar7.immersivefixes.mixin;

import com.mojang.logging.LogUtils;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import java.util.function.Supplier;

// Mixin to resolve #5906: Water Breaking Multiblocks

@Mixin(IEBlocks.class)
class MixinFluidProtection {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Shadow @Final @Mutable
    private static Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OCCLUSION;

    @Shadow @Final
    private static Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OVERLAY;

    @Inject(
            method = "<clinit>",
            at = @At("RETURN")
    )

    private static void injectAfterStaticInitialization(CallbackInfo ci) {

        METAL_PROPERTIES_NO_OCCLUSION = () -> METAL_PROPERTIES_NO_OVERLAY.get() // directly referenced from IE's code under Blu's License of Common Sense. See https://github.com/BluSunrize/ImmersiveEngineering/issues/5906 and the exact source of the code, https://github.com/BluSunrize/ImmersiveEngineering/commit/b6fb636b616bf0c1018c5c2c0095c18bd190161d
                .noOcclusion()
                .forceSolidOn();
    }
}