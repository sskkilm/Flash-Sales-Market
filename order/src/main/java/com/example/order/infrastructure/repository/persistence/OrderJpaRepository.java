package com.example.order.infrastructure.repository.persistence;

import com.example.order.infrastructure.repository.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByMemberId(Long memberId);


    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Order o " +
            "set o.status = :newStatus " +
            "where o.status = :currentStatus " +
            "and " +
            "o.createdAt between :start and :end")
    int updateOrderStatusBetween(@Param("currentStatus") String currentStatus,
                                 @Param("newStatus") String newStatus,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    @Query("select o from Order o " +
            "where o.status = :orderStatus " +
            "and " +
            "o.updatedAt between :start and :end")
    List<OrderEntity> findAllByOrderStatusBetween(
            @Param("orderStatus") String orderStatus,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
