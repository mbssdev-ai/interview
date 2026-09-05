package ir.rahgozin.wallet.application.customer.kyc;

import ir.rahgozin.wallet.application.customer.Customer;
import ir.rahgozin.wallet.application.customer.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerVerificationService {
    private CustomerRepository customerRepository;

    public void handleByCustomerId(String customerId, boolean verificationResult) {
        Customer customer = customerRepository.findById(Long.valueOf(customerId))
                .orElseThrow(() -> new RuntimeException("customer not found with id " + customerId));
        customer.setVerified(verificationResult);
        customerRepository.save(customer);
    }

    public void handleByNationalCode(String nationalCode, boolean verificationResult) {
        Customer customer = customerRepository.findByNationalCode(nationalCode); // what happened if no customer found here?
        customer.setVerified(verificationResult);
        customerRepository.save(customer);
    }
}
