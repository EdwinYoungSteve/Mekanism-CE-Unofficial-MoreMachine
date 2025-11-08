package mekceumoremachine.common.tile.interfaces;

import mekanism.common.base.ITierUpgradeable;
import mekanism.common.tier.ITier;

public interface INeedRepeatTierUpgrade<T extends ITier>  extends ITierUpgradeable {

    T getNowTier();
}
