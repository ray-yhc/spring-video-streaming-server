package com.ray.hlsstreamingserver.src.message;

import com.ray.hlsstreamingserver.src.message.model.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class KafkaMessageService {
    private KafkaTemplate<String, MessageModel> kafkaTemplate;
    private SimpMessageSendingOperations simpMessageSendingOperations;
    private final String TOPIC_NAME = "video";
    private final String CONSUMER_GROUP_ID = "videoserver-1";

    @Autowired
    public KafkaMessageService(KafkaTemplate<String, MessageModel> kafkaTemplate, SimpMessageSendingOperations simpMessageSendingOperations) {
        this.kafkaTemplate = kafkaTemplate;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public void send(MessageModel content) {
        kafkaTemplate.send(TOPIC_NAME, content);
    }

    @KafkaListener(groupId = CONSUMER_GROUP_ID,topics = TOPIC_NAME)
    public void receive(MessageModel videoMessage) {
        log.debug("videoMessage = {}", videoMessage.getMessage());
        log.debug("videoMessage.getTimestamp() = {}", videoMessage.getTimestamp());
    }
}
