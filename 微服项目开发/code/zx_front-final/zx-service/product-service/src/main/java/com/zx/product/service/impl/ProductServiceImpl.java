package com.zx.product.service.impl;




import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

//import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import com.zx.domain.dto.h5.ProductSkuDto;
import com.zx.domain.dto.product.ProductDto;
import com.zx.domain.entity.base.BaseEntity;
import com.zx.domain.entity.product.Category; // 导入分类实体类，用于操作商品分类数据
import com.zx.domain.entity.product.Product; // 导入商品实体类，用于操作商品数据
import com.zx.domain.entity.product.ProductDetails;
import com.zx.domain.entity.product.ProductSku; // 导入商品库存单位实体类，用于操作商品的库存单位数据
import com.zx.domain.vo.h5.IndexVo; // 导入IndexVo类，用于封装返回给前端的展示数据（一级分类和热销商品列表）
import com.zx.domain.vo.h5.ProductItemVo;
import com.zx.product.mapper.ProductDetailsMapper;
import com.zx.product.mapper.ProductMapper; // 导入商品Mapper接口，用于与数据库进行商品数据交互
import com.zx.product.mapper.ProductSkuMapper;
import com.zx.product.service.CategoryService; // 导入CategoryService接口，用于处理商品分类业务逻辑
import com.zx.product.service.ProductDetailsService;
import com.zx.product.service.ProductService; // 导入ProductService接口，用于处理商品业务逻辑
import com.zx.product.service.ProductSkuService; // 导入ProductSkuService接口，用于处理商品库存单位业务逻辑
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired; // 导入@Autowired注解，用于注入依赖
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service; // 导入@Service注解，声明该类为Spring的Service层组件

import java.util.Arrays;
import java.util.HashMap;
import java.util.List; // 导入List类，用于处理多个数据的集合
import java.util.Map;
import java.util.stream.Collectors;

// ProductServiceImpl类实现了ProductService接口，负责处理商品相关的业务逻辑
@Service // 标记该类为一个Spring服务类，便于Spring容器管理
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductDetailsService productDetailsService;

    /**
     * 查找首页展示所需的数据，包括一级分类列表和热销商品列表。
     *
     * @return 返回封装了首页展示数据的IndexVo对象
     */
    @Cacheable(cacheNames = "indexVo", key = "'indexVo'")
    @Override
    public IndexVo findIndexData() {
        System.out.println("go DB");
        // 1. 查询所有一级分类（parentId为0的分类）
        List<Category> categoryList = categoryService.lambdaQuery()
                .eq(Category::getParentId, 0) // 过滤出parentId为0的分类，表示一级分类
                .list(); // 执行查询，返回符合条件的分类列表

        // 2. 查询销量最高的前20个商品库存单位（ProductSku）
        List<ProductSku> productSkuList = productSkuService.lambdaQuery()
                .orderByDesc(ProductSku::getSaleNum) // 按照销售数量降序排序
                .last("limit 20") // 限制查询结果为前20条记录
                .list(); // 执行查询，返回销量最高的前20个商品库存单位列表

        // 3. 创建IndexVo对象，用于封装分类列表和商品库存单位列表
        IndexVo indexVo = new IndexVo();
        indexVo.setCategoryList(categoryList); // 设置一级分类列表
        indexVo.setProductSkuList(productSkuList); // 设置热销商品库存单位列表

        // 4. 返回封装好的IndexVo对象
        return indexVo;
    }

    //@Cacheable(cacheNames = "productSkuPageInfo", key = "'productSkuPageInfo'")
    @Override
    public PageInfo<ProductSku> findByPage(Integer pageNum, Integer pageSize, ProductSkuDto dto) {
        //1: 根据用户传递的分类信息,查询出该分类下所有的spu商品列表
        System.out.println("dto.getBrandId(): " + dto.getBrandId());
        List<Product> productList = lambdaQuery()
                .eq(dto.getCategory1Id() != null, Product::getCategory1Id, dto.getCategory1Id())
                .eq(dto.getCategory2Id() != null, Product::getCategory2Id, dto.getCategory2Id())
                .eq(dto.getCategory3Id() != null, Product::getCategory3Id, dto.getCategory3Id())
                .like(StringUtils.isNotBlank(dto.getKeyword()), Product::getName, dto.getKeyword())
                .eq(dto.getBrandId() != null, Product::getBrandId, dto.getBrandId())
                .eq(Product::getStatus, 1)
                .eq(Product::getAuditStatus, 1)
                .eq(BaseEntity::getIsDeleted, true)
                .list();
        //2: 遍历spu商品列表,获取每一个商品对象的id
        List<Long> pids = productList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        //3.调用productSkuService 根据pids查询 sku列表
        Page<ProductSku> productSkuPage = new Page<>(pageNum, pageSize);
        Page<ProductSku> skuPage = productSkuService.lambdaQuery()
                .in(ProductSku::getProductId, pids)
                .orderByDesc(dto.getOrder() == 1, ProductSku::getSaleNum)
                .orderByAsc(dto.getOrder() == 2, ProductSku::getSalePrice)
                .orderByDesc(dto.getOrder() == 3, ProductSku::getSalePrice)
                .page(productSkuPage);
        // 4: 将sku商品列表封装到PageInfo中并返回
        PageInfo<ProductSku> productSkuPageInfo = PageInfo.of(skuPage.getRecords());
        return productSkuPageInfo;
    }



    @Override
    public ProductItemVo item(Long skuId) {
        //1: 根据商品skuId,查询出该商品对应的sku对象
        ProductSku sku = productSkuService.getById(skuId);
        //2: 根据sku对象中的productId,查询出该商品对应的spu对象
        Product spu = getById(sku.getProductId());
        //3: 从spu对象中获取轮播图字段的值并转成list
        List<String> sliderUrlList = Arrays.asList(spu.getSliderUrls().split(","));
        //4: 从spu对象中获取商品的spuid,并根据spuid从detail表中查询出对应的商品详情图片字段的值并转成list
        ProductDetails details=productDetailsService.lambdaQuery().eq(ProductDetails::getProductId, spu.getId()).one();
        List<String> detailsImageUrlList = Arrays.asList(details.getImageUrls().split(","));
        // 5: 从spu对象中获取商品的规格值字段的值并转成list
        JSONArray specValueList = JSON.parseArray(spu.getSpecValue());
        //6: 商品规格对应商品skuId信息
        // 6.1 : 先根据skuid获取到的sku对象中记录的productId(spuId),查询出该spuId对应的所有sku商品列表
        // 6.2: 变量存储所有sku商品列表中记录的规格值字段的值,把这个值当成map的key,sku对象的id当成map的value
        // 存储到map中即可
        List<ProductSku> productSkuList = productSkuService.lambdaQuery().eq(ProductSku::getProductId, spu.getId()).list();
        Map<String,Object>skuSpecValueMap = new HashMap<>();
        productSkuList.stream().forEach(productSku -> skuSpecValueMap.put(productSku.getSkuSpec(), productSku.getId()));
        //7: 将上面所有的数据封装到ProductItemVo对象中并返回
        ProductItemVo productItemVo = new ProductItemVo(sku, spu, sliderUrlList, detailsImageUrlList, specValueList, skuSpecValueMap);
        return productItemVo;
    }



}
