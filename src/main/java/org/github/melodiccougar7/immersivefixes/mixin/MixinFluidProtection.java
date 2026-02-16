package org.github.melodiccougar7.immersivefixes.mixin;

import com.mojang.logging.LogUtils;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
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

    @Shadow @Final @Mutable
    private static Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_NO_OVERLAY;

    @Shadow @Final @Mutable
    private static Supplier<BlockBehaviour.Properties> SHEETMETAL_PROPERTIES;

    @Shadow @Final @Mutable
    private static Supplier<BlockBehaviour.Properties> DEFAULT_METAL_PROPERTIES;

    @Inject(
            method = "<clinit>",
            at = @At("RETURN")
    )

    private static void injectAfterStaticInitialization(CallbackInfo ci) {

        METAL_PROPERTIES_NO_OCCLUSION = () -> METAL_PROPERTIES_NO_OVERLAY.get() // directly referenced from IE's code under Blu's License of Common Sense. See https://github.com/BluSunrize/ImmersiveEngineering/issues/5906 and the exact source of the code, https://github.com/BluSunrize/ImmersiveEngineering/commit/b6fb636b616bf0c1018c5c2c0095c18bd190161d
                .noOcclusion()
                .forceSolidOn();
    }
// Somewhat random attempts to resolve the issue with scaffolding, left for now when I tackle that again
//    @Inject(
//            method = "<clinit>",
//            at = @At("RETURN")
//    )
//
//    private static void alterRandomFieldsAtRandom1(CallbackInfo ci) {
//        METAL_PROPERTIES_NO_OVERLAY = () -> BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(3.0F, 15.0F).requiresCorrectToolForDrops().isViewBlocking((state, blockReader, pos) -> false)
//                .noOcclusion()
//                .forceSolidOn(); // adding these two won't backfire horribly LOL
//
//    }
//
//    @Inject(
//            method = "<clinit>",
//            at = @At("RETURN")
//    )
//
//    private static void alterFieldsAtRandom2(CallbackInfo ci) {
//        SHEETMETAL_PROPERTIES = () -> BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(2.0F, 2.0F)
//                .noOcclusion()
//                .forceSolidOn(); // adding these two won't backfire horribly LOL
//    }
//
//    @Inject(
//            method = "<clinit>",
//            at = @At("RETURN")
//    )
//
//    private static void alterFieldsAtRandom3(CallbackInfo ci) {
//        DEFAULT_METAL_PROPERTIES = () -> BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.0F, 15.0F)
//                .noOcclusion()
//                .forceSolidOn(); // adding these two won't backfire horribly LOL
//    }
}