package com.imstargg.admin.domain;

import com.imstargg.core.enums.BrawlStarsImageType;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionJpaRepository;
import com.imstargg.support.aws.s3.Image;
import com.imstargg.support.aws.s3.ImageUploader;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class BrawlStarsImageUploader {

    private static final String BUCKET_NAME = "brawlstars";

    private static final String BRAWLERS_DIR = "brawlers";

    private final ImageUploader imageUploader;
    private final BrawlStarsImageCollectionJpaRepository brawlStarsImageJpaRepository;

    public BrawlStarsImageUploader(
            ImageUploader imageUploader,
            BrawlStarsImageCollectionJpaRepository brawlStarsImageJpaRepository
    ) {
        this.imageUploader = imageUploader;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
    }

    public void uploadBrawlerProfile(long brawlStarsId, Image image) {
        String storedName = storedName(brawlStarsId, image.ext(), BRAWLERS_DIR, "profiles");
        URL imageUrl = imageUploader.upload(image, BUCKET_NAME, storedName);

        BrawlStarsImageType type = BrawlStarsImageType.BRAWLER_PROFILE;
        brawlStarsImageJpaRepository.save(new BrawlStarsImageCollectionEntity(
                type,
                type.code(brawlStarsId),
                storedName,
                imageUrl.toString()
        ));
    }

    private String storedName(long brawlStarsId, String ext, String... dirs) {
        return String.join("/", dirs) + "/" + brawlStarsId + "." + ext;
    }
}
