package com.xmcc.dto;

import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResultDto {

    private String detailId;
    //订单id.
    private String orderId;

    /** 商品id. */
    private String productId;
    // 商品名称.
    private String productName;
    //商品价格
    private BigDecimal productPrice;
    //商品数量.
    private Integer productQuantity;
    //商品小图
    private String productIcon;

    private String productImage;

    public static OrderDetailResultDto build(OrderDetail orderDetail){
        OrderDetailResultDto orderDetailResultDto = new OrderDetailResultDto();
        BeanUtils.copyProperties(orderDetail,orderDetailResultDto);
        return orderDetailResultDto;
    }


}
