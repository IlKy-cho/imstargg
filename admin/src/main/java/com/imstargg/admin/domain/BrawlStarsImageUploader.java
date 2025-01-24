package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionJpaRepository;
import com.imstargg.support.aws.s3.Image;
import com.imstargg.support.aws.s3.ImageUploader;
import io.awspring.cloud.s3.S3Template;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(BrawlStarsImageProperties.class)
public class BrawlStarsImageUploader {

    private static final String BRAWLERS_DIR = "brawlers";
    private static final String PROFILE_DIR = "profiles";
    private static final String GEAR_DIR = "gears";
    private static final String MAPS_DIR = "battle-events";

    private final ImageUploader imageUploader;
    private final BrawlStarsImageCollectionJpaRepository brawlStarsImageJpaRepository;
    private final S3Template s3Template;
    private final String bucketName;

    public BrawlStarsImageUploader(
            ImageUploader imageUploader,
            BrawlStarsImageCollectionJpaRepository brawlStarsImageJpaRepository,
            S3Template s3Template,
            BrawlStarsImageProperties brawlStarsImageProperties
    ) {
        this.imageUploader = imageUploader;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.s3Template = s3Template;
        this.bucketName = brawlStarsImageProperties.bucketName();
    }

    public void uploadBrawlerProfile(long brawlStarsId, Resource resource) {
        ImageUpload imageUpload = ImageUpload.builder()
                .type(BrawlStarsImageType.BRAWLER_PROFILE)
                .brawlStarsId(brawlStarsId)
                .image(Image.resource(resource))
                .directories(BRAWLERS_DIR, PROFILE_DIR)
                .build();

        processImageUpload(imageUpload);
    }

    public void uploadGear(long brawlStarsId, Resource resource) {
        ImageUpload imageUpload = ImageUpload.builder()
                .type(BrawlStarsImageType.GEAR)
                .brawlStarsId(brawlStarsId)
                .image(Image.resource(resource))
                .directories(GEAR_DIR)
                .build();

        processImageUpload(imageUpload);
    }

    public void uploadMap(long eventBrawlStarsId, Resource resource) {
        ImageUpload imageUpload = ImageUpload.builder()
                .type(BrawlStarsImageType.BATTLE_MAP)
                .brawlStarsId(eventBrawlStarsId)
                .image(Image.resource(resource))
                .directories(MAPS_DIR)
                .build();

        processImageUpload(imageUpload);
    }

    private void processImageUpload(ImageUpload imageUpload) {
        imageUploader.upload(
                imageUpload.image(),
                bucketName,
                imageUpload.storedName()
        );

        update(
                imageUpload.type(),
                imageUpload.code(),
                imageUpload.storedName()
        );
    }

    private void update(BrawlStarsImageType type, String code, String storedName) {
        brawlStarsImageJpaRepository.findByCode(code)
                .ifPresentOrElse(
                        entity -> updateExistingImage(entity, storedName),
                        () -> createNewImage(type, code, storedName)
                );
    }

    private void updateExistingImage(BrawlStarsImageCollectionEntity entity, String storedName) {
        if (!entity.getStoredName().equals(storedName)) {
            s3Template.deleteObject(bucketName, entity.getStoredName());
        }
        entity.update(storedName);
        brawlStarsImageJpaRepository.save(entity);
    }

    private void createNewImage(BrawlStarsImageType type, String code, String storedName) {
        brawlStarsImageJpaRepository.save(new BrawlStarsImageCollectionEntity(
                type,
                code,
                storedName
        ));
    }

    private static class ImageUpload {

        private final BrawlStarsImageType type;
        private final long key;
        private final Image image;
        private final String[] directories;

        private ImageUpload(
                BrawlStarsImageType type,
                long key,
                Image image,
                String[] directories
        ) {
            this.type = type;
            this.key = key;
            this.image = image;
            this.directories = directories;
        }

        BrawlStarsImageType type() {
            return type;
        }

        String code() {
            return type.code(key);
        }

        Image image() {
            return image;
        }

        String storedName() {
            return String.join("/", directories) + "/" + key + "." + image.ext();
        }

        static Builder builder() {
            return new Builder();
        }

        static class Builder {

            private BrawlStarsImageType type;
            private long key;
            private Image image;
            private String[] directories;

            Builder brawlStarsId(long brawlStarsId) {
                this.key = brawlStarsId;
                return this;
            }

            Builder type(BrawlStarsImageType type) {
                this.type = type;
                return this;
            }

            Builder image(Image image) {
                this.image = image;
                return this;
            }

            Builder directories(String ...directories) {
                this.directories = directories;
                return this;
            }

            ImageUpload build() {
                return new ImageUpload(type, key, image, directories);
            }
        }
    }
}
