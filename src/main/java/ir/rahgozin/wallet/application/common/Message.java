package ir.rahgozin.wallet.application.common;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
@Entity
public class Message extends AbstractEntity {
    private String message;
    private MessageStatus status;
    private String number;

    enum MessageStatus {
        PENDING, SENT, FAILED
    }
}
