package mekceumoremachine.common.tier;

import mekanism.common.tier.BaseTier;
import mekanism.common.tier.ITier;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum MachineTier implements ITier, IStringSerializable {

    BASIC(3),
    ADVANCED(5),
    ELITE(7),
    ULTIMATE(9);

    public final int processes;
    private final BaseTier baseTier;

    MachineTier(int process) {
        processes = process;
        baseTier = BaseTier.values()[ordinal()];
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }


    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
