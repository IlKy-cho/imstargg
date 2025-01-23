package com.imstargg.client.brawlstars;

record BrawlStarsClientErrorResponse(
        String reason,
        String message
) {

    public boolean isInMaintenance() {
        return "inMaintenance".equals(reason);
    }
}
