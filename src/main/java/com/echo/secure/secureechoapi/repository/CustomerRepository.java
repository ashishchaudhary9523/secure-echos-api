package com.echo.secure.secureechoapi.repository;


import com.echo.secure.secureechoapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer , String> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByUserNameOrEmail(String username, String email);
    Customer findByUserName(String username);
    Boolean existsCustomerByUserName(String username);
    Boolean existsCustomerByEmail(String email);

}
