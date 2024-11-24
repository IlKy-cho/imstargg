package com.imstargg.batch.job.support;


import com.imstargg.support.alert.AlertCommand;
import com.imstargg.support.alert.AlertManager;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class ExceptionAlertJobExecutionListener implements JobExecutionListener {

    private final AlertManager alertManager;

    public ExceptionAlertJobExecutionListener(AlertManager alertManager) {
        this.alertManager = alertManager;
    }

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
        alertManager.alert(
                AlertCommand.builder()
                        .error()
                        .title("Job 실행 중 오류 발생")
                        .content(String.format("""
                                - jobName: %s
                                - exitDescription: %s
                                """,
                                jobName, exitDescription)
                        )
                        .build()
        );
    }
}
