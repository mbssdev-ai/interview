package ir.rahgozin.wallet.application.common;

import ir.rahgozin.wallet.application.common.Message.MessageStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;


@Service
@AllArgsConstructor
public class NotificationService {

    private final MessageRepository messageRepository;
    public void send(String number , String message) throws RuntimeException {
        CompletableFuture.runAsync(() -> {
            Message messageEntity = new Message();
            messageEntity.setMessage(message);
            messageEntity.setNumber(number);
            messageEntity.setStatus(MessageStatus.PENDING);
            messageRepository.save(messageEntity);
        });
    }
}
