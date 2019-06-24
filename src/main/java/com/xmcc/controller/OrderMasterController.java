package com.xmcc.controller;

import com.google.common.collect.Maps;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.service.OrderMasterService;
import com.xmcc.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("buyer/order")
@Api(value = "订单信息接口",description="完成订单的增删改查")
public class OrderMasterController {
    @Autowired
    private OrderMasterService orderMasterService;
    @RequestMapping("/create")
    //对接口的描述
    @ApiOperation(value = "创建订单",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse create(
            @Validated @ApiParam(name = "订单对象",value = "传入json格式",required = true) OrderMasterDto orderMasterDto,
            BindingResult bindingResult){
        //创建map接受参数
        Map<String,String> map= Maps.newHashMap();
        //判断参数是否有误
        if(bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验异常", JsonUtil.object2string(collect));
            return ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }
}
