package com.imstargg.client.brawlstars;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "brawlstars", url = "${app.client.brawlstars.api.url}", configuration = BrawlstarsClientConfig.class)
public interface BrawlstarsApi {


}
