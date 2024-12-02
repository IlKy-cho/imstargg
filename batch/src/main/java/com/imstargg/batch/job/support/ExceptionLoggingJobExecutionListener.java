package com.imstargg.batch.job.support;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class ExceptionLoggingJobExecutionListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(ExceptionLoggingJobExecutionListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (!jobExecution.getStatus().isUnsuccessful()) {
            return;
        }

        String jobName = jobExecution.getJobInstance().getJobName();
        String exitDescription = jobExecution.getExitStatus().getExitDescription();

        sendNotification(jobName, exitDescription);
    }

    private void sendNotification(String jobName, String exitDescription) {
        log.warn("Job {} failed with exit description: {}", jobName, exitDescription);
    }
}
