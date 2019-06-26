package com.xmcc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.*;
import com.xmcc.dto.OrderDetailDto;
import com.xmcc.dto.OrderDetailResultDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.OrderMasterResultDto;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.OrderDetailRepository;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.exception.CustomException;
import com.xmcc.util.BigDecimalUtil;
import com.xmcc.util.IDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderMasterServiceImpl implements OrderMasterService{

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
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


    /**
     * 根据买家Id查询信息
     * @param opendId
     * @return
     */
    @Override
    public ResultResponse findByOpenIdAndOrderId(String opendId,String orderId) {
        //1:根据buyerOpenid查询
        List<OrderMaster> byBuyerOpenidIn = orderMasterRepository.findByBuyerOpenidIn(opendId);
        //2.1:byBuyerOpenidIn转换为OrderMasterDto,并将其收集起来

        List<OrderMasterResultDto>  orderMasterDtoList= byBuyerOpenidIn.stream().map(
                orderMaster -> OrderMasterResultDto.build(orderMaster)).collect(Collectors.toList());
        //1.1:判断是否为空

        if(CollectionUtils.isEmpty(byBuyerOpenidIn)){
            return ResultResponse.fail(OrderEnums.OPENID_ERROR.getMsg());
        }
        //2:获取opndid的集合
//        List<String> openIdList
//                = collect.stream().map(orderMasterDto -> orderMasterDto.getOpenid()).collect(Collectors.toList());
        //3:根据orderId查询订单项
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrOrderIdIn(orderId);

        //4：对orderMasterDtoList集合进行遍历，取出订单项设置到对应的订单中
            //将orderdtail设置在items中
            //将orderDetail转换为dto
        List<OrderMasterResultDto> orderMasterResultDtoBig = orderMasterDtoList.stream()
                .map(orderMasterResultDto -> {
                    orderMasterResultDto.setOrderDetailList(orderDetailList.stream()
                            .map(orderDetail -> {
                                OrderDetailResultDto build = OrderDetailResultDto.build(orderDetail);
                                return build;
                            })
                            .collect(Collectors.toList()));
                    return orderMasterResultDto;
                })
                .collect(Collectors.toList());
        return ResultResponse.success(orderMasterResultDtoBig);

    }


}
