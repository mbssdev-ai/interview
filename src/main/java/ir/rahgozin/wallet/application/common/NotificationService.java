package ir.rahgozin.wallet.application.common;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MessageService messageService;

    @Scheduled(fixedDelay = 5000)
    public void notificationProcess() {
        List<Message> messages = messageService.claimMessages();
        List<Message> successSending = new ArrayList<>();
        List<Message> unsuccessSending = new ArrayList<>();
        for (Message message : messages) {
            try {
                sendSms(message);
                successSending.add(message);
            } catch (Exception e) {
                unsuccessSending.add(message);
            }
        }
        messageService.updateMessageStatus(successSending, unsuccessSending);
    }


    private void sendSms(Message message) {
        // call sending sms api
    }

}
