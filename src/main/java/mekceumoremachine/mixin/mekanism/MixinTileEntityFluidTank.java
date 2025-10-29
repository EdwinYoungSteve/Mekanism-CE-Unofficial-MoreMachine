package mekceumoremachine.mixin.mekanism;

import mekanism.common.tier.FluidTankTier;
import mekanism.common.tile.TileEntityFluidTank;
import mekceumoremachine.common.tile.interfaces.ITierMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityFluidTank.class,remap = false)
public abstract class MixinTileEntityFluidTank implements ITierMachine<FluidTankTier> {
    @Shadow
    public FluidTankTier tier;

    @Override
    public FluidTankTier getTier() {
        return tier;
    }
}
