package com.xmcc.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Data
@Builder
public class OrderDetail implements Serializable {
    @Id
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
}
