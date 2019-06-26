package com.xmcc.dto;

import com.xmcc.common.OrderEnums;
import com.xmcc.common.PayEnums;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMasterResultDto {
    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus = OrderEnums.NEW.getCode();

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus = PayEnums.WAIT.getCode();

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;

    private List<OrderDetailResultDto> orderDetailList;

    public static OrderMasterResultDto build(OrderMaster orderMaster){
        OrderMasterResultDto orderMasterResultDto = new OrderMasterResultDto();
        BeanUtils.copyProperties(orderMaster,orderMasterResultDto);
        return orderMasterResultDto;
    }
}
