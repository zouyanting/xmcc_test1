package com.xmcc.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
//swagger 参数的描述信息
@ApiModel("订单详情类")

public class OrderDetailDto implements Serializable {
/*
    private String detailId;//
    private String orderId;//由工具类随机生成
*/

    @NotBlank(message = "商品id不能为空")
    //swagger 参数的描述信息
    @ApiModelProperty(value = "商品id",dataType = "String")
    private String productId;

    /*
    @NotBlank(message = "商品名称不能为空")
    @ApiModelProperty(value = "商品名称",dataType = "String")
    private String productName;

    @NotBlank(message = "商品id不能为空")
    @ApiModelProperty(value = "商品id",dataType = "String")
    private String productPrice;
*/

    @NotNull(message = "商品的数量不能为空")
    @Min(value = 1,message ="商品数量不能少于一件" )
    @ApiModelProperty(value = "商品数量",dataType = "int")
    private Integer productQuantity;

}
