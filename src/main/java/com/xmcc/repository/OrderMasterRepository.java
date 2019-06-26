package com.xmcc.repository;

import com.xmcc.entity.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {
    @Query(value = "SELECT * FROM order_master WHERE buyer_openid=?1",nativeQuery = true)
    List <OrderMaster> findByBuyerOpenidIn (String openId);
}
