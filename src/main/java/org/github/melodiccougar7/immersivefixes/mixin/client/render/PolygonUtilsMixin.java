package org.github.melodiccougar7.immersivefixes.mixin.client.render;

import blusunrize.immersiveengineering.client.models.split.PolygonUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import malte0811.modelsplitter.model.Polygon;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import org.github.melodiccougar7.immersivefixes.lib.IFLib;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PolygonUtils.class, remap = false)
public class PolygonUtilsMixin {
    @Unique private static final int IMMERSIVEFIXES$STRIDE = DefaultVertexFormat.BLOCK.getIntegerSize();
    @Unique private static final int IMMERSIVEFIXES$COLOR_OFFSET = immersiveFixes$getOffset(DefaultVertexFormat.ELEMENT_COLOR);
    @Unique private static final int IMMERSIVEFIXES$UV2_OFFSET = immersiveFixes$getOffset(DefaultVertexFormat.ELEMENT_UV2);
    @Unique private static final int IMMERSIVEFIXES$NORMAL_OFFSET = immersiveFixes$getOffset(DefaultVertexFormat.ELEMENT_NORMAL);

    @Unique private static int immersiveFixes$getOffset(VertexFormatElement element) {
        int offset = 0;
        for (VertexFormatElement e : DefaultVertexFormat.BLOCK.getElements()) {
            if (e == element) { return offset / 4; }
            offset += e.getByteSize();
        }
        throw new IllegalStateException("Element not found: " + element);
    }

    @Inject(method = "toBakedQuad(Lmalte0811/modelsplitter/model/Polygon;Lnet/minecraft/client/resources/model/ModelState;)Lnet/minecraft/client/renderer/block/model/BakedQuad;", at = @At("RETURN"), cancellable = true)
    private static void immersiveFixes$bakeSplitLighting(Polygon<PolygonUtils.ExtraQuadData> poly, ModelState transform, CallbackInfoReturnable<BakedQuad> cir) {
        IFLib.logMixinActive("PolygonUtilsMixin");
        BakedQuad in = cir.getReturnValue();
        int[] verts = in.getVertices();
        for (int v = 0; v < 4; ++v) {
            int base = v * IMMERSIVEFIXES$STRIDE;
            int packedNormal = verts[base + IMMERSIVEFIXES$NORMAL_OFFSET];
            float nx = ((byte) packedNormal) / 127f;
            float ny = ((byte) (packedNormal >> 8)) / 127f;
            float nz = ((byte) (packedNormal >> 16)) / 127f;
            float shade = Math.min(nx * nx * 0.6f + ny * ny * ((3.0f + ny) / 4.0f) + nz * nz * 0.8f, 1.0f);
            int c = verts[base + IMMERSIVEFIXES$COLOR_OFFSET];
            int r = Math.min((int) ((c & 255) * shade), 255);
            int g = Math.min((int) (((c >> 8) & 255) * shade), 255);
            int b = Math.min((int) (((c >> 16) & 255) * shade), 255);
            verts[base + IMMERSIVEFIXES$COLOR_OFFSET] = r | (g << 8) | (b << 16) | (c & 0xFF000000);
            verts[base + IMMERSIVEFIXES$UV2_OFFSET] = 0xF00000;
        }
        cir.setReturnValue(new BakedQuad(verts, in.getTintIndex(), in.getDirection(), in.getSprite(), false));
    }
}
