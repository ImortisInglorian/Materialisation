package me.shedaniel.materialisation.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.shedaniel.materialisation.MaterialisationUtils;
import me.shedaniel.materialisation.items.MaterialisedMiningTool;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    
    private static final UUID MODIFIER_SWING_SPEED = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    private static final UUID MODIFIER_DAMAGE = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    
    @Shadow
    public abstract Item getItem();
    
    /**
     * Disable italic on tools
     */
    @Inject(method = "hasDisplayName", at = @At("HEAD"), cancellable = true)
    public void hasDisplayName(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (getItem() instanceof MaterialisedMiningTool)
            callbackInfo.setReturnValue(false);
    }
    
    @Inject(method = "getAttributeModifiers", at = @At(value = "INVOKE",
                                                       target = "Lnet/minecraft/item/Item;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;",
                                                       shift = At.Shift.BEFORE), cancellable = true)
    public void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<String, EntityAttributeModifier>> callbackInfo) {
        if (getItem() instanceof MaterialisedMiningTool) {
            HashMultimap<String, EntityAttributeModifier> multimap = HashMultimap.create();
            if (slot == EquipmentSlot.MAINHAND) {
                multimap.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(MODIFIER_SWING_SPEED, "Tool modifier", ((MaterialisedMiningTool) getItem()).getAttackSpeed(), EntityAttributeModifier.Operation.ADDITION));
                multimap.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(MODIFIER_DAMAGE, "Tool modifier", MaterialisationUtils.getToolAttackDamage((ItemStack) (Object) this), EntityAttributeModifier.Operation.ADDITION));
            }
            callbackInfo.setReturnValue(multimap);
        }
    }
    
    @ModifyVariable(method = "getTooltipText", at = @At(value = "INVOKE", ordinal = 0,
                                                        target = "Lnet/minecraft/entity/attribute/EntityAttributeModifier;getOperation()Lnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;"),
                    name = "boolean_1")
    private boolean getTooltipText(boolean a) {
        if (getItem() instanceof MaterialisedMiningTool)
            return true;
        return a;
    }
}