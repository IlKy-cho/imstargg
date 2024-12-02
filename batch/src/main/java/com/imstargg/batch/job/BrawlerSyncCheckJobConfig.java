package com.imstargg.batch.job;

import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.core.enums.Brawler;
import com.imstargg.core.enums.Gadget;
import com.imstargg.core.enums.StarPower;
import com.imstargg.support.alert.AlertCommand;
import com.imstargg.support.alert.AlertManager;
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
    private final AlertManager alertManager;

    public BrawlerSyncCheckJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            BrawlStarsClient brawlStarsClient,
            AlertManager alertManager
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.brawlStarsClient = brawlStarsClient;
        this.alertManager = alertManager;
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
                    for (BrawlerResponse brawler : brawlers) {
                        if (check(brawler)) {
                            continue;
                        }
                        alertManager.alert(AlertCommand.builder()
                                .title("브롤러 업데이트 필요")
                                .content("BrawlerResponse: " + brawler)
                                .build());
                    }
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    private boolean check(BrawlerResponse brawler) {
        return checkBrawler(brawler) && checkGadget(brawler) && checkStarPower(brawler);
    }

    private boolean checkBrawler(BrawlerResponse brawler) {
        return Brawler.exists(brawler.id());
    }

    private boolean checkGadget(BrawlerResponse brawler) {
        return brawler.gadgets().stream().allMatch(gadget -> Gadget.exists(gadget.id()));
    }

    private boolean checkStarPower(BrawlerResponse brawler) {
        return brawler.starPowers().stream().allMatch(starPower -> StarPower.exists(starPower.id()));
    }
}
