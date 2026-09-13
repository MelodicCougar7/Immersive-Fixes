package org.github.melodiccougar7.immersivefixes.mixin.common;

import org.github.melodiccougar7.immersivefixes.lib.IFLib;

import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MBInventoryUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = MBInventoryUtils.class, remap = false)
public abstract class MBInventoryUtilsMixin {
    @Inject(method = "dropItems(Lnet/minecraftforge/items/IItemHandler;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private static void immersiveFixes$clearDroppedItems(IItemHandler inv, Consumer<ItemStack> drop, CallbackInfo ci) {
        IFLib.logMixinActive("MBInventoryUtilsMixin");
        if (inv instanceof IItemHandlerModifiable modifiable) {
            for (int slot = 0; slot < modifiable.getSlots(); slot++) {
                if (!modifiable.getStackInSlot(slot).isEmpty()) { modifiable.setStackInSlot(slot, ItemStack.EMPTY); }
            }
        }
    }
}
