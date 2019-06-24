package com.xmcc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.*;
import com.xmcc.dto.OrderDetailDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.exception.CustomException;
import com.xmcc.util.BigDecimalUtil;
import com.xmcc.util.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        /**
         * @Valid:用于配合JSR303注解 验证参数，只能在controller层验证
         * validetor:在service层验证
         */
        //1：取出订单项
        List<OrderDetailDto> items = orderMasterDto.getItems();
        //2：创建的集合来存储OrderDetail
        List<OrderDetail> orderDetailList= Lists.newArrayList();
        //3:设置初始化订单的总金额
        BigDecimal totalPrice=new BigDecimal("0");

        //4：遍历订单项，获取商品详情
        for (OrderDetailDto orderDetailDto:items) {
            //4.1:查询订单
            ResultResponse<ProductInfo> resultResponse = productInfoService.queryById(orderDetailDto.getProductId());
            //4.2:判断ResultResponse的code即可
            if(resultResponse.getCode()== ResultEnums.FAIL.getCode()){
                throw  new CustomException(resultResponse.getMsg());
            }
            //4.3得到商品
            ProductInfo prductInfo = resultResponse.getData();
            //4.4:比较库存
            if(prductInfo.getProductStock() < orderDetailDto.getProductQuantity()){
                throw new CustomException(ProductEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //4.5；创建订单项
            OrderDetail orderDetail=OrderDetail.builder().detailId(IDUtils.createIdbyUUID()).productIcon(prductInfo.getProductIcon())
                    .productId(prductInfo.getProductId()).productName(prductInfo.getProductName())
                    .productPrice(prductInfo.getProductPrice()).productQuantity(orderDetailDto.getProductQuantity()).build();
            orderDetailList.add(orderDetail);
            //4.6:减少商品库存
            prductInfo.setProductStock(prductInfo.getProductStock()-orderDetail.getProductQuantity());
            //4.7:计算价格
            totalPrice= BigDecimalUtil.add(totalPrice,BigDecimalUtil.multi(prductInfo.getProductPrice(),orderDetail.getProductQuantity()));

        }
        //5:生成订单id
        String order_id=IDUtils.createIdbyUUID();
        //6:构建订单信息
        OrderMaster orderMaster = OrderMaster.builder().buyerAddress(orderMasterDto.getAddress()).buyerName(orderMasterDto.getName())
                .buyerOpenid(orderMasterDto.getOpenid()).orderStatus(OrderEnums.NEW.getCode())
                .payStatus(PayEnums.WAIT.getCode()).buyerPhone(orderMasterDto.getPhone())
                .orderId(order_id).orderAmount(totalPrice).build();
        //7:将生成的订单id，设置到订单项中
        List<OrderDetail> detailList = orderDetailList.stream().map(orderDetail1 -> {
            orderDetail1.setOrderId(order_id);
            return orderDetail1;
        }).collect(Collectors.toList());
        //8:插入订单项
        orderDetailService.batchInsert(detailList);
        //9:插入订单
        orderMasterRepository.save(orderMaster);
        HashMap<String, String> map=Maps.newHashMap();
        //9:按照前台要求的数据结构传入
        map.put("orderId",order_id);
        return ResultResponse.success(map);
    }
}
