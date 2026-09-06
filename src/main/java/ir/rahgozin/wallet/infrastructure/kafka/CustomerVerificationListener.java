package ir.rahgozin.wallet.infrastructure.kafka;

import ir.rahgozin.wallet.application.customer.kyc.CustomerVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Log4j2
@Component
@RequiredArgsConstructor
public class CustomerVerificationListener {
    private final CustomerVerificationService customerVerificationService;

    @KafkaListener(topics = "customer_verification_topic")
    public void handleKycVerificationEvent(KycVerificationEvent event) {
        try {
            if (isValidEvent(event)) {
                switch (event.identifierType()) {
                    case ID ->
                            customerVerificationService.handleByCustomerId(Long.valueOf(event.identifierValue()), event.verified());
                    case NATIONAL_CODE ->
                            customerVerificationService.handleByNationalCode(event.identifierValue(), event.verified());
                }
            }

        } catch (Exception e) {
            log.error("error while handling kyc verification event", e);
        }
    }

    private boolean isValidEvent(KycVerificationEvent event) {
        return event != null && event.identifierType != null && StringUtils.hasLength(event.identifierValue());
    }

    public record KycVerificationEvent(IdentifierType identifierType,
                                       String identifierValue,
                                       boolean verified) {

    }

    enum IdentifierType {
        ID,
        NATIONAL_CODE
    }

}
