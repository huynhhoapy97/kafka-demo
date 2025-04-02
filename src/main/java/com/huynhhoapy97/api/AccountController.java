package com.huynhhoapy97.api;

import com.huynhhoapy97.model.AccountDTO;
import com.huynhhoapy97.model.MessageDTO;
import com.huynhhoapy97.model.StatisticDTO;
import com.huynhhoapy97.repository.MessageRepository;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("account")
public class AccountController {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MessageRepository messageRepository;

    public AccountController(KafkaTemplate<String, Object> kafkaTemplate,
                             MessageRepository messageRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
    }

    @PostMapping("create")
    public AccountDTO create(@RequestBody AccountDTO account) {

        MessageDTO message = new MessageDTO();
        message.setToEmail(account.getEmail());
        message.setToName(account.getName());
        message.setSubject("Welcome to our Website");
        message.setContent("Let's to learn something news");
        message.setStatus(false);

        StatisticDTO statistic = new StatisticDTO(
                "Account " + account.getEmail() + " is created",
                new Date());

        // Thay vì gửi luôn message đến Kafka, ta lưu xuống DB message với trạng thái ban đầu là false
        messageRepository.save(message);

        /*
          Vòng for giúp demo việc gửi nhiều message lên Kafka Server
          từ đó ta sẽ nhận được response bị lỗi (exception) vì message bị mất
          */
//        for (int i = 0; i < 100; i++) {
//            kafkaTemplate.send("notification", message)
//                    .addCallback(new KafkaSendCallback<String, Object>() {
//                        // Hàm callback này sẽ lắng nghe sự kiện trả về
//                        @Override
//                        public void onSuccess(SendResult<String, Object> result) {
//                            /*
//                               Xử lý khi gửi message thành công
//                               ví dụ ở đây lấy ra topic và partition nơi message được lưu
//                             */
//
//                            System.out.println(result.getRecordMetadata().topic() +
//                                    " - "
//                                    +  result.getRecordMetadata().partition());
//                        }
//
//                        @Override
//                        public void onFailure(KafkaProducerException e) {
//                            /*
//                              Xử lý khi gửi message thất bại
//                              Handle: save Message failed into Database
//                             */
//                            e.printStackTrace();
//                        }
//                    });
//        }

        kafkaTemplate.send("statistic", statistic);

        return account;
    }
}
