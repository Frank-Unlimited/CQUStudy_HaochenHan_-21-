package com.zx.order.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.common.AuthThreadLocalUtils;
import com.zx.domain.dto.h5.OrderInfoDto;
import com.zx.domain.entity.base.BaseEntity;
import com.zx.domain.entity.base.Region;
import com.zx.domain.entity.h5.CartInfo;
import com.zx.domain.entity.order.OrderInfo;
import com.zx.domain.entity.order.OrderItem;
import com.zx.domain.entity.order.OrderLog;
import com.zx.domain.entity.product.ProductSku;
import com.zx.domain.entity.user.UserAddress;
import com.zx.domain.entity.user.UserInfo;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.domain.vo.h5.TradeVo;
import com.zx.exception.CustomException;
import feignclient.ProductSkuFeignClient;
import feignclient.UserFeignClient;
import com.zx.order.mapper.OrderInfoMapper;
import com.zx.order.service.CartService;
import com.zx.order.service.OrderInfoService;
import com.zx.order.service.OrderItemService;
import com.zx.order.service.OrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private ProductSkuFeignClient productSkuFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private CartService cartService;


    public TradeVo trade() {

        String oid = LocalDateTime.now().toString() + UUID.randomUUID().toString().replace("-", "");

        // 1. 生成订单id,将这个订单id保存到redis中,当用户点击"提交订单"按钮的时候,从redis中获取这个订单id,并从redis中删除;(防抖)
        Long userId = AuthThreadLocalUtils.getThreadLocal().getId();
        redisTemplate.opsForValue().set("userId:" + userId, oid, 15, TimeUnit.MINUTES);

        //2. 从购物车中获取用户勾选的商品信息(CartIno),封装到OrderItem中;
        Map<String, Object> map = getOrderItemList();

        //3: 把OrderItem中的数据封装到 TradeVo中;
        TradeVo tradeVo = new TradeVo();
        tradeVo.setOrderItemList((List<OrderItem>) map.get("list"));
        tradeVo.setTotalAmount((BigDecimal) map.get("sum"));
        return tradeVo;
    }

    private Map<String, Object> getOrderItemList() {
        Long id = AuthThreadLocalUtils.getThreadLocal().getId();
        ObjectMapper objectMapper = new ObjectMapper();

        // 获取 Redis 中的数据
        List<Object> objects = redisTemplate.opsForHash().values("cart:" + id);

        // 反序列化为 List<CartInfo>
        List<CartInfo> values = objects.stream()
                .map(object -> {
                    try {
                        String json = (String) object;
                        return objectMapper.readValue(json, CartInfo.class);
                    } catch (Exception e) {
                        throw new RuntimeException("反序列化失败", e);
                    }
                })
                .collect(Collectors.toList());

        // 只选择选中的商品
        values = values.stream()
                    .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                    .collect(Collectors.toList());;

        // ------校验是否有商品价格变动或者下架或者库存不足--------- //

        // 收集所有商品的 skuId
                List<Long> skuIds = values.stream().map(CartInfo::getSkuId).collect(Collectors.toList());

        // 一次性批量查询所有商品的库存、价格等信息
                List<ProductSku> productSkuList = productSkuFeignClient.getSkuListBySkuIds(skuIds);

        // 将查询到的商品信息转换为 Map<skuId, ProductSku>，方便后续快速查找
                Map<Long, ProductSku> productSkuMap = productSkuList.stream()
                        .collect(Collectors.toMap(ProductSku::getId, productSku -> productSku));

        // 遍历购物车中的商品，检查商品信息是否变动
                List<String> deletedList = new ArrayList<>();
                Iterator<CartInfo> iterator = values.iterator();
                while (iterator.hasNext()) {
                    CartInfo cartInfo = iterator.next();
                    ProductSku productSku = productSkuMap.get(cartInfo.getSkuId());

                    // 校验条件：库存不足、价格变化、商品下架
                    if (productSku == null ||
                            productSku.getStockNum() < cartInfo.getSkuNum() ||
                            !productSku.getSalePrice().equals(cartInfo.getCartPrice()) ||
                            productSku.getStatus() != null && productSku.getStatus() == -1) {

                        //--------消息提示-------//
                        if(productSku.getStockNum() < cartInfo.getSkuNum()){
                            System.out.println("=========================================");
                            System.out.println("库存不足：");
                            System.out.println("商品名称："+productSku.getSkuName());
                            System.out.println("剩余库存："+productSku.getSaleNum());
                            System.out.println("========================================");
                        }

                        if(productSku.getStatus() == -1){
                            System.out.println("=========================================");
                            System.out.println("商品下架：");
                            System.out.println("商品名称："+productSku.getSkuName());
                            System.out.println("========================================");
                        }

                        if(!productSku.getSalePrice().equals(cartInfo.getCartPrice())){
                            System.out.println("=========================================");
                            System.out.println("商品价格变动：");
                            System.out.println("商品名称："+productSku.getSkuName());
                            System.out.println("原价格："+cartInfo.getCartPrice());
                            System.out.println("新价格："+productSku.getSalePrice());
                            System.out.println("========================================");
                        }
                        //--------end------//

                        // 如果商品有问题，记录商品名称并从购物车移除
                        deletedList.add(cartInfo.getSkuName());
                        iterator.remove(); // 使用 iterator 移除，避免 ConcurrentModificationException
                    }
                }

                System.out.println("有商品变动，请重新加入购物车：");
                System.out.println("问题商品：" + deletedList);
        // -------------end-------------- //


        BigDecimal[] sum = {new BigDecimal("0")};
        List<OrderItem> list = values.stream()
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .map(cartInfo -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setSkuId(cartInfo.getSkuId());
                    orderItem.setSkuName(cartInfo.getSkuName());
                    orderItem.setThumbImg(cartInfo.getImgUrl());
                    orderItem.setSkuNum(cartInfo.getSkuNum());
                    orderItem.setSkuPrice(cartInfo.getCartPrice());
                    orderItem.setCreateTime(cartInfo.getCreateTime());
                    orderItem.setUpdateTime(cartInfo.getUpdateTime());
                    //log.error("price: " + orderItem.getSkuPrice());
                    sum[0] = sum[0].add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
                    return orderItem;
                }).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("sum", sum[0]);
        map.put("list", list);
//        log.error("sum: " + sum[0]);
        return map;
    }

    public TradeVo buy(Long skuId){
        ProductSku sku = productSkuFeignClient.getProductById(skuId);//------校验商品是否已下架或库存不足-------//
        if(sku.getStatus() != null && sku.getStatus() == -1){
            throw new CustomException(ResultCodeEnum.PRODUCT_DELETED);
        }
        if(sku.getStockNum() <= 0){
            throw new CustomException(ResultCodeEnum.STOCK_LESS);
        }
        //----------end-----------//

        String oid = LocalDateTime.now().toString() + UUID.randomUUID().toString().replace("-", "");

        // 1. 生成订单id,将这个订单id保存到redis中,当用户点击"提交订单"按钮的时候,从redis中获取这个订单id,并从redis中删除;(防抖)
        Long userId = AuthThreadLocalUtils.getThreadLocal().getId();
        redisTemplate.opsForValue().set("userId:" + userId, oid, 1, TimeUnit.MINUTES);


//
        //根据sku对象转成orderItem
//        OrderItem item = new OrderItem();
//        BeanUtils.copyProperties(sku, item);
//        item.setSkuId(sku.getId());
//        item.setSkuNum(1);
//        item.setSkuPrice(sku.getSalePrice());
//        List<OrderItem> item1 = List.of(item);
//        TradeVo tradeVo = new TradeVo();
//        tradeVo.setOrderItemList(item1);
//        tradeVo.setTotalAmount(item.getSkuPrice());

        cartService.allCheckCart(0);//设置当前登陆用户购物车中所有 商品 是未选中状态
        cartService.deleteCartBySkuId(skuId);
        cartService.addCart(skuId, 1);
        cartService.isChecked(skuId, 1);

        //2. 从购物车中获取用户勾选的商品信息(CartIno),封装到OrderItem中;
        Map<String, Object> map = getOrderItemList();

        //3: 把OrderItem中的数据封装到 TradeVo中;
        TradeVo tradeVo = new TradeVo();
        tradeVo.setOrderItemList((List<OrderItem>) map.get("list"));
        tradeVo.setTotalAmount((BigDecimal) map.get("sum"));


        return tradeVo;
    }

    @Override
    public Long submitOrder(OrderInfoDto dto){
        System.out.println("order dto: " + dto);
        // 1. 从redis中获取订单id，并从redis中删除
        UserInfo userInfo = AuthThreadLocalUtils.getThreadLocal();
        Long userId = userInfo.getId();
        String order_no = (String) redisTemplate.opsForValue().get("userId:"+userId);
        System.out.println("order_no: "+order_no);
        // 2. 判断订单是否存在
        if(order_no == null) throw new CustomException(ResultCodeEnum.ORDER_EXISTS);
        redisTemplate.delete("userId:"+userId);
        OrderInfo orderInfo = null;
        try{
            List<OrderItem> orderItemList = dto.getOrderItemList();

            // Map<Long, Integer> inventoryDeductionMap = new HashMap<>();
            List<Long> skuIds =  new ArrayList<>();
            List<Integer> skuNums = new ArrayList<>();

            for (OrderItem orderItem : orderItemList) {
                Long skuId = orderItem.getSkuId();   // 获取skuId
                Integer skuNum = orderItem.getSkuNum(); // 获取skuNum
                // inventoryDeductionMap.merge(skuId, skuNum, Integer::sum);
                skuIds.add(skuId);
                skuNums.add(skuNum);
            }

            boolean flag = productSkuFeignClient.deductInventories(skuIds, skuNums);

            //3. 库存扣除失败报错
            if (!flag) {
                throw new CustomException(ResultCodeEnum.STOCK_LESS);
            }

            //4. 保存订单信息
            orderInfo = saveOrder(dto, userId, userInfo, order_no);
            Long id = orderInfo.getId();
            // 5. 生成订单项并保存
            System.out.println("orderID"+id);
            orderItemList.stream().forEach(item -> item.setOrderId(id));
            System.out.println("orderItemList: "+orderItemList);
            orderItemService.saveBatch(orderItemList);
            // 6. 清除购物车
            // 6.1----获取购物车数据
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> objects = redisTemplate.opsForHash().values("cart:" + userId);
            List<CartInfo> cartList = objects.stream()
                    .map(object -> {
                        try {
                            String json = (String) object;
                            return objectMapper.readValue(json, CartInfo.class);
                        } catch (Exception e) {
                            throw new RuntimeException("反序列化失败", e);
                        }
                    })
                    .collect(Collectors.toList());
            System.out.println("redisInfoList: "+cartList);
            // 6.2-----找到购物车中被提交订单后的商品
            List<Long> redisCheckedInfoList = cartList.stream()
                    .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                    .map(cartInfo -> cartInfo.getSkuId()).toList();
            System.out.println("redisCheckedInfoList: "+redisCheckedInfoList);
            redisCheckedInfoList.forEach(skuId -> cartService.deleteCartBySkuId(skuId));
            return orderInfo.getId();
        } catch (Exception e) {
            throw new CustomException("下单失败:"+e.getMessage());
        } finally {
            saveLog(orderInfo, userInfo);
        }

    }

    public void saveLog(OrderInfo orderInfo, UserInfo info) {
        if (orderInfo == null) {
            orderInfo = new OrderInfo();
            orderInfo.setId(0L);
            orderInfo.setOrderStatus(0);
        }
        // 6: 保存订单日志
        OrderLog log = new OrderLog();
        log.setOrderId(orderInfo.getId());
        log.setCreateTime(new Date());
        log.setOperateUser(info.getNickName());
        log.setProcessStatus(orderInfo.getOrderStatus());
        log.setUpdateTime(new Date());
        orderLogService.save(log);
    }
    private OrderInfo saveOrder(OrderInfoDto dto, Long userId, UserInfo info, String order_no) {
        // 5: 生成订单信息,保存到数据库中;
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(info.getNickName());
        orderInfo.setOrderNo(order_no);
        Map<String, Object> map = getOrderItemList();
        orderInfo.setTotalAmount((BigDecimal) map.get("sum"));
        orderInfo.setOriginalTotalAmount(orderInfo.getTotalAmount());
        orderInfo.setFeightFee(dto.getFeightFee());
        orderInfo.setOrderStatus(0);
        Long addressId = dto.getUserAddressId();
        // 远程调用,获取地址相关信息
        UserAddress address = userFeignClient.getUserAddressById(JSONObject.toJSONString(info), addressId);
        orderInfo.setReceiverName(address.getName());
        orderInfo.setReceiverPhone(address.getPhone());
        orderInfo.setReceiverTagName(address.getTagName());
        String provinceCode = address.getProvinceCode();
        String cityCode = address.getCityCode();
        String districtCode = address.getDistrictCode();
        Region region = userFeignClient.getRegionById(provinceCode);
        Region region2 = userFeignClient.getRegionById(cityCode);
        Region region3 = userFeignClient.getRegionById(districtCode);
        orderInfo.setReceiverProvince(region.getId());
        orderInfo.setReceiverCity(region2.getId());
        orderInfo.setReceiverDistrict(region3.getId());
        orderInfo.setReceiverAddress(address.getFullAddress());
        orderInfo.setRemark(dto.getRemark());
        orderInfo.setOrderItemList((List<OrderItem>) map.get("list"));
        //5: 生成订单信息,保存到数据库中;
        save(orderInfo);
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return getById(orderId);
    }

    @Override
    public PageInfo<OrderInfo> myPageList(Integer page, Integer limit, Integer orderStatus){
        // 1. 获取当前用户id
        Long id = AuthThreadLocalUtils.getThreadLocal().getId();
        // 2. 创建分页查询对象
        PageHelper.startPage(page, limit);
        // 3. 准备条件对象
        LambdaQueryWrapper<OrderInfo> eq = Wrappers.lambdaQuery(OrderInfo.class)
                .eq(OrderInfo::getUserId, id)
                .eq(orderStatus != null, OrderInfo::getOrderStatus, orderStatus)
                .orderByDesc(BaseEntity::getCreateTime);
        List<OrderInfo> list = list(eq);
        if(list.size() == 0){
            return null;
        }
        list.stream().forEach(orderInfo ->
                orderInfo.setOrderItemList(
                        orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
                                .eq(OrderItem::getOrderId, orderInfo.getId()))));
        // 4: 将分页查询的结果,封装到PageInfo中并返回
        PageInfo<OrderInfo> pageInfo = PageInfo.of(list);
        //5: 将pageinfo封装到Result并返回
        return pageInfo;
    }


}