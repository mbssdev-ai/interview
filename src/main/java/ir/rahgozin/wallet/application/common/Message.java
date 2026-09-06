package ir.rahgozin.wallet.application.common;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Message extends AbstractEntity {
    private String message;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    private String number;
    private Integer tryCount = 0;

    public Message(String message, MessageStatus status, String number) {
        this.message = message;
        this.status = status;
        this.number = number;
    }
}
