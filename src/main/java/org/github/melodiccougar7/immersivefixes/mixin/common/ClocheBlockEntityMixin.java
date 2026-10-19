package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.github.melodiccougar7.immersivefixes.util.ChunkDirty;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ClocheBlockEntity.class, remap = false)
public abstract class ClocheBlockEntityMixin {
    @Shadow
    private float growth;

    @Redirect(method = "tickServer()V", at = @At(value = "FIELD", target = "Lblusunrize/immersiveengineering/common/blocks/metal/ClocheBlockEntity;growth:F", opcode = Opcodes.PUTFIELD), require = 4)
    private void immersiveFixes$dirtyOnGrowth(ClocheBlockEntity cloche, float value) {
        if (growth != value) ChunkDirty.mark(cloche);
        growth = value;
    }

    @Redirect(method = "tickServer()V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemHandlerHelper;insertItem(Lnet/minecraftforge/items/IItemHandler;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack immersiveFixes$dirtyOnOutput(IItemHandler dest, ItemStack stack, boolean simulate) {
        int count = stack.getCount();
        ItemStack remainder = ItemHandlerHelper.insertItem(dest, stack, simulate);
        if (!simulate && remainder.getCount() != count) ChunkDirty.mark((BlockEntity) (Object) this);
        return remainder;
    }
}
