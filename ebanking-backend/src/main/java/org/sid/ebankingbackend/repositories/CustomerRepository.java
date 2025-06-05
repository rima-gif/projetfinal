package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
        @Query("select c from Customer c where c.name like :kw")
        List<Customer> searchCustomer(@Param("kw") String keyword);
        Optional<Customer> findByEmailAndPassword(String email, String password);

}
