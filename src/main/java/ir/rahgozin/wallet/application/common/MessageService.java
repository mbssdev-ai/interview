package ir.rahgozin.wallet.application.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    @Transactional
    public List<Message> claimMessages() {
        List<Message> messages = messageRepository.lockPendingMessages();
        messages.forEach(
                m -> m.setStatus(
                        MessageStatus.PROCESSING
                )
        );
        return messages;
    }

    @Transactional
    public void updateMessageStatus(List<Message> successSending, List<Message> unsuccessSending) {
        successSending.forEach(
                m -> m.setStatus(
                        MessageStatus.SENT
                )
        );
        for (Message message : unsuccessSending) {
            if (message.getTryCount() > 2) {
                message.setStatus(MessageStatus.FAILED);
            } else {
                message.setStatus(MessageStatus.PENDING);
                message.setTryCount(message.getTryCount() + 1);
            }
        }
    }
}
