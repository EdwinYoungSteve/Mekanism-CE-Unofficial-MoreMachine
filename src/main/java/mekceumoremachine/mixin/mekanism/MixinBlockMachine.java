package mekceumoremachine.mixin.mekanism;

import com.llamalad7.mixinextras.sugar.Local;
import mekanism.common.base.IFactory.RecipeType;
import mekanism.common.block.BlockMachine;
import mekanism.common.block.BlockMekanismContainer;
import mekanism.common.block.states.BlockStateMachine;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockMachine.class)
public abstract class MixinBlockMachine extends BlockMekanismContainer {

    protected MixinBlockMachine(Material materialIn) {
        super(materialIn);
    }

    @Redirect(method = "getSubBlocks", at = @At(value = "INVOKE", target = "Lmekanism/common/block/states/BlockStateMachine$MachineType;isEnabled()Z", ordinal = 1, remap = false))
    public boolean removeFactoryMachine(BlockStateMachine.MachineType instance, @Local RecipeType recipeType) {
        return instance.isEnabled() && recipeType != RecipeType.WASHER && recipeType != RecipeType.Dissolution && recipeType != RecipeType.OXIDIZER;
    }
}
