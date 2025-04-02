package com.huynhhoapy97.service;

import com.huynhhoapy97.model.MessageDTO;
import com.huynhhoapy97.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollingService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MessageRepository messageRepository;

    public PollingService(KafkaTemplate<String, Object> kafkaTemplate, MessageRepository messageRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
    }

    @Scheduled(fixedDelay = 1000)
    public void pollKafka() {
        List<MessageDTO> messages = messageRepository.findByStatus(false);

        messages.forEach(message -> kafkaTemplate.send("notification", message)
                .addCallback(new KafkaSendCallback<String, Object>() {
                    // Hàm callback này sẽ lắng nghe sự kiện trả về
                    @Override
                    public void onSuccess(SendResult<String, Object> result) {
                        /*
                           Xử lý khi gửi message thành công
                           Handle: lưu lại trạng thái thành công của message là true
                         */

                        message.setStatus(true);
                        messageRepository.save(message);
                    }

                    @Override
                    public void onFailure(KafkaProducerException e) {
                        /*
                          Xử lý khi gửi message thất bại
                          Handle: save Message failed into Database
                         */
                        logger.error("Kafka Producer Exception: {}", String.valueOf(e));
                    }
                }));
    }

    @Scheduled(fixedDelay = 60000)
    public void delete() {
        List<MessageDTO> messages = messageRepository.findByStatus(true);

        messageRepository.deleteAllInBatch(messages);
    }
}
