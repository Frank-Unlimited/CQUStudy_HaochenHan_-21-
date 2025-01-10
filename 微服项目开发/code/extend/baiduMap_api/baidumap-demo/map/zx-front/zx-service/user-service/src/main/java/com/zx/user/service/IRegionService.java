package com.zx.user.service;

import com.zx.domain.entity.base.Region;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.domain.vo.common.Result;

import java.util.List;

/**
 * <p>
 * 地区信息表 服务类
 * </p>
 *
 * @author author
 * @since 2025-01-03
 */
public interface IRegionService extends IService<Region> {
    Result<List<Region>> findByParentCode(String parentCode);

    Region getByCode(String provinceCode);
}
