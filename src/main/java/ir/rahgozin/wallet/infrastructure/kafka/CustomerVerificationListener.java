package ir.rahgozin.wallet.infrastructure.kafka;

import ir.rahgozin.wallet.application.customer.kyc.CustomerVerificationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CustomerVerificationListener {
    private CustomerVerificationService customerVerificationService;

    //@KafkaListener(topics = "customer_verification_topic")
    public void handleKycVerificationEvent(KycVerificationEvent event) {
        try {
            if (event != null) {
                if (event.getIdentifierType() != null && !event.getIdentifierType().isEmpty()) {
                    if (event.getIdentifierValue() != null && !event.getIdentifierValue().isEmpty()) {
                        switch (event.getIdentifierType()) {
                            case "ID":
                                customerVerificationService.handleByCustomerId(event.getIdentifierValue(), event.isVerified());
                            case "NATIONAL_CODE":
                                customerVerificationService.handleByNationalCode(event.getIdentifierValue(), event.isVerified());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("error while handling kyc verification event", e);
        }
    }

    @Data
    public static class KycVerificationEvent {
        private String identifierType;
        private String identifierValue;
        private boolean verified;
    }

}
