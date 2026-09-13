package org.github.melodiccougar7.immersivefixes.mixin.common;

import blusunrize.immersiveengineering.common.items.IEBaseItem;
import blusunrize.immersiveengineering.common.items.InternalStorageItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(value = InternalStorageItem.class, remap = false)
public abstract class InternalStorageItemMixin extends IEBaseItem {
    @Unique private static final String IMMERSIVEFIXES$CONTENTS = "immersivefixes:contents";

    protected InternalStorageItemMixin(Properties props) { super(props); }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag shared = super.getShareTag(stack);
        CompoundTag contents = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().map(handler -> {
            NonNullList<ItemStack> items = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
            boolean any = false;
            for (int slot = 0; slot < items.size(); slot++) {
                items.set(slot, handler.getStackInSlot(slot));
                if (!items.get(slot).isEmpty()) { any = true; }
            }
            return any ? ContainerHelper.saveAllItems(new CompoundTag(), items) : null;
        }).orElse(null);
        if (contents == null) { return shared; }
        CompoundTag result = shared == null ? new CompoundTag() : shared.copy();
        result.put(IMMERSIVEFIXES$CONTENTS, contents);
        return result;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt != null && nbt.contains(IMMERSIVEFIXES$CONTENTS)) {
            CompoundTag contents = nbt.getCompound(IMMERSIVEFIXES$CONTENTS);
            nbt.remove(IMMERSIVEFIXES$CONTENTS);
            stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                if (handler instanceof IItemHandlerModifiable modifiable) {
                    NonNullList<ItemStack> items = NonNullList.withSize(modifiable.getSlots(), ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(contents, items);
                    for (int slot = 0; slot < items.size(); slot++) { modifiable.setStackInSlot(slot, items.get(slot)); }
                }
            });
        }
        super.readShareTag(stack, nbt);
    }
}
