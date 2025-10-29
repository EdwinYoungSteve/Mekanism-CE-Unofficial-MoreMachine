package mekceumoremachine.mixin.mekanism;

import mekanism.common.tier.GasTankTier;
import mekanism.common.tile.TileEntityGasTank;
import mekceumoremachine.common.tile.interfaces.ITierMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityGasTank.class,remap = false)
public abstract class MixinTileEntityGasTank implements ITierMachine<GasTankTier> {


    @Shadow
    public GasTankTier tier;

    @Override
    public GasTankTier getTier() {
        return tier;
    }
}
