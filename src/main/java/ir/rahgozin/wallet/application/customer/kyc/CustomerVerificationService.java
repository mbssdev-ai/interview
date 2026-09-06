package ir.rahgozin.wallet.application.customer.kyc;

import ir.rahgozin.wallet.application.common.exception.BusinessException;
import ir.rahgozin.wallet.application.customer.Customer;
import ir.rahgozin.wallet.application.customer.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static ir.rahgozin.wallet.application.common.exception.BusinessError.CUSTOMER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomerVerificationService {

    private final CustomerRepository customerRepository;

    @Transactional
    public void handleByCustomerId(Long customerId, boolean verificationResult) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new BusinessException(CUSTOMER_NOT_FOUND));
        customer.setVerified(verificationResult);
    }

    @Transactional
    public void handleByNationalCode(String nationalCode, boolean verificationResult) {
        Customer customer = customerRepository.findByNationalCode(nationalCode);
        if(customer == null){
            throw new BusinessException(CUSTOMER_NOT_FOUND);
        }
        customer.setVerified(verificationResult);
    }
}
