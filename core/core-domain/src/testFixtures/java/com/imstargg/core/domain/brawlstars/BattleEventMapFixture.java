package com.imstargg.core.domain.brawlstars;


import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.enums.Language;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;
import java.util.Arrays;

public class BattleEventMapFixture {

    @Nullable
    private MessageCollection names = new MessageCollection(
            "name-" + LongIncrementUtil.next(),
            Arrays.stream(Language.values())
                    .map(language -> new Message(language, "name-" + LongIncrementUtil.next()))
                    .toList());
    
    @Nullable
    private String imagePath = "/images/maps/" + LongIncrementUtil.next();

    public BattleEventMapFixture names(MessageCollection names) {
        this.names = names;
        return this;
    }

    public BattleEventMapFixture imagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public BattleEventMap build() {
        return new BattleEventMap(names, imagePath);
    }
} 