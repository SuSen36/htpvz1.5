package com.hungteen.pvz.utils;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;

public class PlantUtil {
    /**
     * refund sun cost equivalent of a plant: upgrade plants (purple rank) refund the base plant cost plus their own cost.
     */
    public static int getPlantRefundSunCost(IPlantType type) {
        return type.getUpgradeFrom().isPresent()
                ? type.getUpgradeFrom().get().getSunCost() + type.getSunCost()
                : type.getSunCost();
    }

    /**
     * copy data from p1 to p2
     */
    public static void copyPlantData(PVZPlantEntity p2, PVZPlantEntity p1) {
        p2.setSkills(p1.getSkills());
        p2.setCharmed(p1.isCharmed());
        p2.setOwnerUUID(p1.getOwnerUUID().orElse(null));
        p2.setChallengeUuid(p1.getChallengeUuid());
    }

}
