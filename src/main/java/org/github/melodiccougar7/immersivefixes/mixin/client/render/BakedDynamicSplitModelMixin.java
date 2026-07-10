package org.github.melodiccougar7.immersivefixes.mixin.client.render;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.split.BakedDynamicSplitModel;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.github.melodiccougar7.immersivefixes.lib.IFLib;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Mixin(value = BakedDynamicSplitModel.class, remap = false)
public class BakedDynamicSplitModelMixin {
    @Shadow @Final private static Set<BakedDynamicSplitModel<?, ?>> WEAK_INSTANCES;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void immersiveFixes$registerForReload(CallbackInfo ci) {
        IFLib.logMixinActive("BakedDynamicSplitModelMixin");
        WEAK_INSTANCES.add((BakedDynamicSplitModel<?, ?>) (Object) this);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/cache/CacheBuilder;maximumSize(J)Lcom/google/common/cache/CacheBuilder;"))
    private CacheBuilder<Object, Object> immersiveFixes$cacheSize(CacheBuilder<Object, Object> builder, long maximumSize) { return builder.maximumSize(64); }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/cache/CacheBuilder;expireAfterAccess(JLjava/util/concurrent/TimeUnit;)Lcom/google/common/cache/CacheBuilder;"))
    private CacheBuilder<Object, Object> immersiveFixes$cacheExpiry(CacheBuilder<Object, Object> builder, long duration, TimeUnit unit) { return builder.expireAfterAccess(10, TimeUnit.MINUTES); }

    @Inject(method = "getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private void immersiveFixes$cullSplitSides(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType, CallbackInfoReturnable<List<BakedQuad>> cir) {
        IFLib.logMixinActive("BakedDynamicSplitModelMixin");
        if (side != null && data.get(IEProperties.Model.SUBMODEL_OFFSET) != null) { cir.setReturnValue(ImmutableList.of()); }
    }
}
