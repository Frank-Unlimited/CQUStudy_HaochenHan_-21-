package com.zx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.dto.product.ProductDto;
import com.zx.domain.dto.product.SkuSaleDto;
import com.zx.domain.entity.base.BaseEntity;
import com.zx.domain.entity.product.Product;
import com.zx.domain.entity.product.ProductSku;
import com.zx.domain.vo.common.Result;
import com.zx.product.mapper.ProductMapper;
import com.zx.product.mapper.ProductSkuMapper;
import com.zx.product.service.ProductSkuService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements ProductSkuService{

//    @Autowired
//    private ProductSkuService productSkuService;
//
//    @Override
//    public Result<PageInfo<ProductSku>> findPage(Integer page, Integer limit, ProductDto){
//        /*
//         * 按条件分页查询
//         * 准备一个分页对象和查询条件，然后调用mapper的方法
//         * */
//        // 按条件查询product对应的id
//        List<Product> list = Produck
//    }
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public List<ProductSku> getSkuListBySkuIds(List<Long> ids) {
        List<ProductSku> productSkuList = lambdaQuery().in(BaseEntity::getId, ids).list();
        return productSkuList;
    }

    @Override
    public Boolean deductInventory(Long skuId, Integer num){

//        ProductSku sku = getById(skuId);
//        int currentInventory = sku.getStockNum();
//        if (currentInventory < num) {
//                //log.debug("商品：{} 库存不足,剩余：{}  下单购买：{}", skuId, currentInventory, num);
//                log.error("商品库存不足，剩余："+sku.getSaleNum());
//                return false;
//            }
//
//        LambdaUpdateWrapper<ProductSku> set = Wrappers.lambdaUpdate(ProductSku.class)
//                .eq(ProductSku::getId, skuId)
//                .set(ProductSku::getStockNum, currentInventory- num);
//        update(set);



        //----------Redisson加锁------------//
        RLock lock = redissonClient.getLock("mylock");
        lock.lock(3, TimeUnit.SECONDS);
        try{
            ProductSku sku = getById(skuId);
            int currentInventory = sku.getStockNum();
            if (currentInventory < num) {
                //log.debug("商品：{} 库存不足,剩余：{}  下单购买：{}", skuId, currentInventory, num);
                log.error("商品库存不足，剩余："+sku.getSaleNum());
                return false;
            }
            // 扣减库存
            LambdaUpdateWrapper<ProductSku> set = Wrappers.lambdaUpdate(ProductSku.class)
                    .eq(ProductSku::getId, skuId)
                    .set(ProductSku::getStockNum, currentInventory- num);
            update(set);

            System.out.println("Succeed!");

        } catch (Exception e){
            String exceotion_message = e.getMessage();
            System.out.println(exceotion_message);
        } finally {
            lock.unlock();
        }
        //------------end---------------//

        return true;
    }

    @Override
    public Boolean deductInventories(List<Long> skuIds, List<Integer> skuNums) {
        // Map<Long, Integer> maps
        Map<Long, Integer> maps = IntStream.range(0, skuIds.size())
                .boxed()
                .collect(Collectors.toMap(
                        skuIds::get,    // 获取 skuId
                        skuNums::get    // 获取 skuNum
                ));
        // 获取所有需要更新的商品ID
        // List<Long> skuIds = new ArrayList<>(maps.keySet());
        //--------------extend：Redisson加锁------------------//
        // 只有拿到锁才能去更新数据库信息，防止商品库存超发
        // ex1. 获取分布式锁
        RLock lock = redissonClient.getLock("mylock");
        // ex2. 申请锁
        lock.lock(3, TimeUnit.SECONDS);
        // ex3. 获取锁成功，执行后续工作
        try{
            // 批量查询商品信息
            List<ProductSku> skuList = getSkuListBySkuIds(skuIds);

            // 创建一个更新列表，用于存储所有需要更新的ProductSku
            List<ProductSku> updateList = new ArrayList<>();

            // 将查询结果转换成 Map，以便通过 SKU ID 查找
            Map<Long, ProductSku> skuMap = skuList.stream()
                    .collect(Collectors.toMap(ProductSku::getId, sku -> sku));

            // 遍历传入的 map，进行库存扣减操作
            for (Map.Entry<Long, Integer> entry : maps.entrySet()) {
                Long skuId = entry.getKey();
                Integer num = entry.getValue();

                // 从查询结果中获取对应的 ProductSku
                ProductSku sku = skuMap.get(skuId);
                if (sku == null) {
                    //log.error("商品ID：{} 未找到", skuId);
                    // continue;  // 如果没有找到对应商品，跳过此条记录
                    log.error("商品记录错误");
                    return false;
                }

                int currentInventory = sku.getStockNum();

                // 如果库存不足，记录日志并跳过该商品
                if (currentInventory < num) {
                    //log.error("商品库存不足，商品ID：{}，剩余库存：{}，尝试扣减：{}", skuId, currentInventory, num);
//                continue;
                    log.error("商品库存不足");
                    return  false;
                }

                // 如果库存足够，更新库存
                sku.setStockNum(currentInventory - num);
                updateList.add(sku);
            }
            // 执行批量更新
            if (!updateList.isEmpty()) {
                Boolean result = updateBatchById(updateList);
                log.error("hihihihihi");
                return result;
            }
        } catch (Exception e){
            String exceotion_message = e.getMessage();
            System.out.println(exceotion_message);
        } finally {
            // ex4. 解锁
            lock.unlock();
        }
        //----------------end--------------------//

        return false;
    }

    @Override
    public void updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList) {
        // Redisson 加锁
        RLock lock = redissonClient.getLock("sku_sale_update_lock");
        lock.lock(3, TimeUnit.SECONDS); // 锁3秒
        try {
            // 将 skuSaleDtoList 转换为 Map，便于操作
            Map<Long, Integer> skuSaleMap = skuSaleDtoList.stream()
                    .collect(Collectors.toMap(SkuSaleDto::getSkuId, SkuSaleDto::getNum));

            // 获取所有需要更新的商品 ID
            List<Long> skuIds = new ArrayList<>(skuSaleMap.keySet());

            // 查询数据库中对应的 SKU 信息
            List<ProductSku> skuList = lambdaQuery().in(BaseEntity::getId, skuIds).list();

            // 转换成 Map，便于操作
            Map<Long, ProductSku> skuMap = skuList.stream()
                    .collect(Collectors.toMap(ProductSku::getId, sku -> sku));

            // 创建一个更新列表，用于存储需要更新的 SKU
            List<ProductSku> updateList = new ArrayList<>();

            for (SkuSaleDto skuSaleDto : skuSaleDtoList) {
                Long skuId = skuSaleDto.getSkuId();
                Integer num = skuSaleDto.getNum();

                // 检查是否存在对应的 SKU
                ProductSku sku = skuMap.get(skuId);
                if (sku == null) {
                    log.error("商品 ID: {} 不存在，跳过更新"+skuId);
                    continue; // 如果 SKU 不存在，跳过此条记录
                }

                // 更新 sale_num
                sku.setSaleNum(sku.getSaleNum() + num);
                updateList.add(sku);
            }

            // 执行批量更新
            if (!updateList.isEmpty()) {
                boolean result = updateBatchById(updateList);
                if (!result) {
                    log.error("批量更新 sale_num 失败");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            // 解锁
            lock.unlock();
        }
    }



}
