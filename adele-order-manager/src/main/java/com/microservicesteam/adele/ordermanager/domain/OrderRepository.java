package com.microservicesteam.adele.ordermanager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("update Order o set o.paymentId = :paymentId where orderId = :orderId")
    void updatePaymentId(@Param("orderId") String orderId, @Param("paymentId") String paymentId);

}
