package com.zx.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.entity.base.Region;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.user.mapper.RegionMapper;
import com.zx.user.service.IRegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 地区信息表 服务实现类
 * </p>
 *
 * @author kdm
 * @since 2024-12-19
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

    @Override
    public Result<List<Region>> findByParentCode(String parentCode) {
         /*
            1: 根据上级code查询下级列表
         */
        LambdaQueryWrapper<Region> eq = Wrappers.lambdaQuery(Region.class)
                .eq(Region::getParentCode, parentCode);
        return Result.build(list(eq), ResultCodeEnum.SUCCESS);
    }

    @Override
    public Region getByCode(String code) {
        LambdaQueryWrapper<Region> eq = Wrappers.lambdaQuery(Region.class).eq(Region::getCode, code);
        return getOne(eq,false);
    }
}