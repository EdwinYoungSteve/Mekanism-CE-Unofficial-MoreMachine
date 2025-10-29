package mekceumoremachine.common.tile.interfaces;

import mekanism.common.base.ITierUpgradeable;
import mekanism.common.tier.ITier;

public interface ITierMachine<T extends ITier> extends ITierUpgradeable {

    T getTier();

}
