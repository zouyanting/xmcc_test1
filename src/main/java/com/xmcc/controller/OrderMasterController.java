package com.xmcc.controller;

import com.google.common.collect.Maps;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.DetailListDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("buyer/order")
@Api(value = "订单信息接口",description="完成订单的增删改查")
public class OrderMasterController {
    /**
     * 创建订单
     */
    @Autowired
    private OrderMasterService orderMasterService;
    @RequestMapping("/create")
    //对接口的描述
    @ApiOperation(value = "创建订单",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse create(
            @Valid @ApiParam(name = "订单对象",value = "传入json格式",required = true) OrderMasterDto orderMasterDto,
            BindingResult bindingResult){

        Map<String,String> map= Maps.newHashMap();
        //判断参数是否有误
        if(bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验异常", JsonUtil.object2string(collect));
            return ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }


    /**
     * 订单列表
     */
    @ApiOperation(value = "订单详情（*）",httpMethod = "GET",response = ResultResponse.class)
    @RequestMapping("/detail")
    public ResultResponse list(DetailListDto detailListDto){
        ResultResponse byOpenIdAndOrderId = orderMasterService.findByOpenIdAndOrderId(detailListDto.getOpenid(), detailListDto.getOrderId());
        return byOpenIdAndOrderId;
    }

    /**
     * 取消订单
     */
    @ApiOperation(value = "取消详情（*）",httpMethod = "POST",response = ResultResponse.class)
    @RequestMapping("/cancel")
    public ResultResponse cansel(DetailListDto detailListDto){
        ResultResponse resultResponse = orderMasterService.CancelDetail(detailListDto.getOpenid(), detailListDto.getOrderId());
        return resultResponse;
    }

}
