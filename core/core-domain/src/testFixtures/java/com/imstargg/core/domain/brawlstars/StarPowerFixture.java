package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.enums.Language;
import com.imstargg.test.java.LongIncrementUtil;

import javax.annotation.Nullable;
import java.util.Arrays;

public class StarPowerFixture {
    private BrawlStarsId id = new BrawlStarsId(LongIncrementUtil.next());
    private MessageCollection names = new MessageCollection(
            "name-" + LongIncrementUtil.next(),
            Arrays.stream(Language.values())
                    .map(language -> new Message(language, "name-" + LongIncrementUtil.next()))
                    .toList()
    );
    @Nullable
    private String imagePath = "imagePath-" + LongIncrementUtil.next();

    public StarPowerFixture id(BrawlStarsId id) {
        this.id = id;
        return this;
    }

    public StarPowerFixture names(MessageCollection names) {
        this.names = names;
        return this;
    }

    public StarPowerFixture imagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public StarPower build() {
        return new StarPower(id, names, imagePath);
    }
}