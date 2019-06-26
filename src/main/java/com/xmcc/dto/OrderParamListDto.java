package com.xmcc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderParamListDto {
    private String openid;
    private Integer page;
    private Integer size;
}
