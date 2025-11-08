package mekceumoremachine.mixin.mekanism;

import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import mekceumoremachine.common.tile.interfaces.INeedRepeatTierUpgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityFactory.class, remap = false)
public abstract class MixinTileEntityFactory implements INeedRepeatTierUpgrade<FactoryTier> {
    @Shadow
    public FactoryTier tier;

    @Override
    public FactoryTier getNowTier() {
        return tier;
    }
}
