package com.microservicesteam.adele.ordermanager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Modifying(clearAutomatically = true)
    @Query("update Orders o set o.paymentId = :paymentId where orderId = :orderId")
    int updatePaymentId(@Param("orderId") String orderId, @Param("paymentId") String paymentId);

    @Modifying(clearAutomatically = true)
    @Query("update Orders o set o.status = :newStatus where o.orderId = :orderId")
    int updateStatusByOrderId (@Param("orderId")String orderId, @Param("newStatus") OrderStatus newStatus);

    @Modifying(clearAutomatically = true)
    @Query("update Orders o set o.status = :newStatus "
            + "where o.orderId = :orderId "
            + "and o.paymentId = :paymentId "
            + "and o.status = :status")
    int updateStatusByOrderIdPaymentIdStatus(
            @Param("status") OrderStatus status,
            @Param("orderId") String orderId,
            @Param("paymentId") String paymentId,
            @Param("newStatus") OrderStatus newStatus);

}
