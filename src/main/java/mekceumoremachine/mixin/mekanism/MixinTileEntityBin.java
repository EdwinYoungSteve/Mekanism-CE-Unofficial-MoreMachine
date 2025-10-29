package mekceumoremachine.mixin.mekanism;

import mekanism.common.tier.BinTier;
import mekanism.common.tile.TileEntityBin;
import mekceumoremachine.common.tile.interfaces.ITierMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityBin.class, remap = false)
public abstract class MixinTileEntityBin implements ITierMachine<BinTier> {
    @Shadow
    public BinTier tier;

    @Override
    public BinTier getTier() {
        return tier;
    }
}
