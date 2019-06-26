package com.xmcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailListDto {
    @NotBlank(message = "微信号不能为空")
    private String openid;
    @NotBlank(message = "订单号不能为空")
    private String orderId;
}
