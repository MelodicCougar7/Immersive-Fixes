package org.github.melodiccougar7.immersivefixes.mixin.client.render;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.split.BakedBasicSplitModel;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * @author tgstyle
 */

@Mixin(value = BakedBasicSplitModel.class, remap = false)
public class BakedBasicSplitModelMixin {
    @Inject(method = "getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private void immersiveFixes$cullSplitSides(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType layer, CallbackInfoReturnable<List<BakedQuad>> cir) {
        if (side != null && extraData.get(IEProperties.Model.SUBMODEL_OFFSET) != null) { cir.setReturnValue(ImmutableList.of()); }
    }
}
