package com.imstargg.support.localstack;

import cloud.localstack.awssdkv2.TestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;

@Component
@ConditionalOnProperty(value = "app.localstack.enabled", havingValue = "true")
public class LocalStackSqsSetup implements ApplicationRunner {

    private final String queueName;

    public LocalStackSqsSetup(@Value("app.event.queue.player-renewal") String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SqsClient sqsClient = TestUtils.getClientSQSV2();
        CreateQueueResponse response = sqsClient.createQueue(builder -> builder.queueName(queueName));

        System.out.println("LocalStack SQS Queue created: " + response);
    }
}
