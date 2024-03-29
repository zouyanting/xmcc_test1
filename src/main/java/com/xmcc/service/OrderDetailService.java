package com.xmcc.service;



import com.xmcc.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    //批量插入
    void batchInsert(List<OrderDetail> orderDetailList);

    //根据OrderId查找
    List<OrderDetail> queryByOrderId(String orderId);

}
