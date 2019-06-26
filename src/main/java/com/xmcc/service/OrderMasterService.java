package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;

public interface OrderMasterService {
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);

    //*根据买家id查询
    ResultResponse findByOpenIdAndOrderId(String opendId,String orderId);

    //*根据orderId查询数据
    ResultResponse CancelDetail(String opendId,String orderId);
}
