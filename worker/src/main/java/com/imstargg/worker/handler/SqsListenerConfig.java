package com.imstargg.worker.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.autoconfigure.sqs.SqsProperties;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.ContainerOptionsBuilder;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(SqsProperties.class)
class SqsListenerConfig {

    @Bean
    SqsMessageListenerContainerFactory<Object> sqsListenerContainerFactory(
            SqsAsyncClient sqsAsyncClient,
            ObjectMapper objectMapper,
            SqsProperties sqsProperties
    ) {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(options -> configureContainerOptions(options, sqsProperties))
                .configure(options -> configureObjectMapper(options, objectMapper))
                .configure(options -> options
                        .maxDelayBetweenPolls(Duration.ofSeconds(5))
                )
                .build();
    }

    private void configureContainerOptions(ContainerOptionsBuilder<?, ?> options, SqsProperties sqsProperties) {
        PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        mapper.from(sqsProperties.getListener().getMaxConcurrentMessages()).to(options::maxConcurrentMessages);
        mapper.from(sqsProperties.getListener().getMaxMessagesPerPoll()).to(options::maxMessagesPerPoll);
        mapper.from(sqsProperties.getListener().getPollTimeout()).to(options::pollTimeout);
    }

    private void configureObjectMapper(ContainerOptionsBuilder<?, ?> options, ObjectMapper objectMapper) {
        var messageConverter = new SqsMessagingMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        options.messageConverter(messageConverter);
    }
}
