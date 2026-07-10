package org.github.melodiccougar7.immersivefixes.mixin.client.render;

import blusunrize.immersiveengineering.client.models.split.AbstractSplitModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author tgstyle
 */

@Mixin(value = AbstractSplitModel.class, remap = false)
public abstract class AbstractSplitModelMixin implements IForgeBakedModel {
    @Override public boolean useAmbientOcclusion(@Nonnull BlockState state) { return false; }

    @Override public boolean useAmbientOcclusion(@Nonnull BlockState state, @Nullable RenderType renderType) { return false; }
}
