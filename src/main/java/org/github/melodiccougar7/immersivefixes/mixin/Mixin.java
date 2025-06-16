package org.github.melodiccougar7.immersivefixes.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import blusunrize.immersiveengineering.common.register.IEBlocks; 

import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.logging.LogManager;


@Mixin(IEBlocks.class)
class MixinFluidProtection {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Shadow @Final @Mutable
    private static Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OCCLUSION;

    @Shadow @Final
    private static Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OVERLAY;

    @Inject(
            method = "<clinit>",  // Target the static initializer method (<clinit>)
            at = @At("RETURN")   // Inject after the static initialization logic
    )

    private static void injectAfterStaticInitialization(CallbackInfo ci) {
        // quick debug statement
       LOGGER.info("Mixin applied: IEBlocks <clinit> method called.");
       LOGGER.info("Original METAL_PROPERTIES_NO_OVERLAY: {}", METAL_PROPERTIES_NO_OVERLAY.get());

      // replace the static value

        METAL_PROPERTIES_NO_OCCLUSION = () -> METAL_PROPERTIES_NO_OVERLAY.get() // directly referenced from IE's code under Blu's License of Common Sense. See https://github.com/BluSunrize/ImmersiveEngineering/issues/5906 and the exact source of the code, https://github.com/BluSunrize/ImmersiveEngineering/commit/b6fb636b616bf0c1018c5c2c0095c18bd190161d
                .noOcclusion()
                .forceSolidOn();

        LOGGER.info("Modified METAL_PROPERTIES_NO_OCCLUSION has been set.");
    }
}
