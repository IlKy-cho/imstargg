package com.imstargg.batch.job;

import com.imstargg.batch.domain.ThingToUpdateAppender;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.core.enums.Brawler;
import com.imstargg.core.enums.Gadget;
import com.imstargg.core.enums.StarPower;
import com.imstargg.core.enums.UpdateType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.util.List;

@Configuration
public class BrawlerSyncCheckJobConfig {

    private static final String JOB_NAME = "brawlerSyncCheckJob";
    private static final String STEP_NAME = "brawlerSyncCheckStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final BrawlStarsClient brawlStarsClient;
    private final ThingToUpdateAppender thingToUpdateAppender;

    public BrawlerSyncCheckJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            BrawlStarsClient brawlStarsClient,
            ThingToUpdateAppender thingToUpdateAppender
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.brawlStarsClient = brawlStarsClient;
        this.thingToUpdateAppender = thingToUpdateAppender;
    }

    @Bean(JOB_NAME)
    public Job job() {
        final JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunTimestampIncrementer(clock))
                .start(step())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(stepBuilder);
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {
                    List<BrawlerResponse> brawlers = brawlStarsClient.getBrawlers().items();
                    checkGadgets(brawlers);
                    checkStarPowers(brawlers);
                    checkBrawlers(brawlers);
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    private void checkGadgets(List<BrawlerResponse> brawlers) {
        brawlers.stream()
                .flatMap(brawler -> brawler.gadgets().stream())
                .filter(gadget -> !Gadget.exists(gadget.id()))
                .forEach(gadget -> thingToUpdateAppender.append(gadget.id(), UpdateType.GADGET, gadget));
    }

    private void checkStarPowers(List<BrawlerResponse> brawlers) {
        brawlers.stream()
                .flatMap(brawler -> brawler.starPowers().stream())
                .filter(starPower -> !StarPower.exists(starPower.id()))
                .forEach(starPower -> thingToUpdateAppender.append(starPower.id(), UpdateType.STAR_POWER, starPower));
    }

    private void checkBrawlers(List<BrawlerResponse> brawlers) {
        brawlers.stream()
                .filter(brawler -> !Brawler.exists(brawler.id()))
                .forEach(brawler -> thingToUpdateAppender.append(brawler.id(), UpdateType.BRAWLER, brawler));
        brawlers.stream()
                .filter(brawler -> Brawler.exists(brawler.id()))
                .filter(brawler ->
                        !brawler.gadgets().stream().map(AccessoryResponse::id).equals(
                                Brawler.find(brawler.id()).getGadgets().stream().map(Gadget::getBrawlStarsId))
                ).forEach(brawler -> thingToUpdateAppender.append(brawler.id(), UpdateType.BRAWLER, brawler));
    }
}
