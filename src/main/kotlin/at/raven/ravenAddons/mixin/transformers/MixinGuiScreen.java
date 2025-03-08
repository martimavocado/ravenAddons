package at.raven.ravenAddons.mixin.transformers;

import at.raven.ravenAddons.mixin.hooks.TooltipHook;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {
    @Unique
    private final TooltipHook ravenAddons$hook = new TooltipHook();

    @Inject(method = "renderToolTip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getRarity()Lnet/minecraft/item/EnumRarity;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo ci, List<String> list) {
        list = ravenAddons$hook.getTooltip(stack, list);
        if (list.isEmpty()) {
            ci.cancel();
        }
    }
}
