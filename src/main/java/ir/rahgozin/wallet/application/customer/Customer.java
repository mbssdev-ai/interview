package ir.rahgozin.wallet.application.customer;

import ir.rahgozin.wallet.application.common.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_customer_national_code",
                        columnNames = "national_code"
                ),
                @UniqueConstraint(
                        name = "uk_customer_mobile",
                        columnNames = "mobile"
                )
        }
)
@NoArgsConstructor
public class Customer extends AbstractEntity {
    @Column(name = "national_code", nullable = false)
    private String nationalCode;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(nullable = false)
    private String mobile;
    private Integer age;
    private boolean isVerified;
    private boolean isDeleted;

}
