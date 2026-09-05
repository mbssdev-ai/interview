package ir.rahgozin.wallet.application.customer;

import ir.rahgozin.wallet.application.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ir.rahgozin.wallet.application.common.exception.BusinessError.CUSTOMER_IS_EXIST;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer create(Customer customer) {
        boolean isExistByNationalCode = customerRepository.existsByNationalCode(customer.getNationalCode());
        if (isExistByNationalCode) {
            throw new BusinessException(CUSTOMER_IS_EXIST);
        }
        return customerRepository.save(customer);
    }

    public boolean existsByNationalCode(String nationalCode) {
        return customerRepository.existsByNationalCode(nationalCode);
    }

    public Customer findByNationalCode(String nationalCode) {
        return customerRepository.findByNationalCode(nationalCode);
    }

}
