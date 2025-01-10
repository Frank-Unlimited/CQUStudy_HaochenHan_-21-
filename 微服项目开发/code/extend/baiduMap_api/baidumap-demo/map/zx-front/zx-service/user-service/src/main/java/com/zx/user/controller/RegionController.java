package com.zx.user.controller;


import com.zx.domain.entity.base.Region;
import com.zx.domain.vo.common.Result;
import com.zx.user.service.IRegionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 地区信息表 前端控制器
 * </p>
 *
 * @author kdm
 * @since 2024-12-19
 */
@RestController
@RequestMapping("/user/region")
public class RegionController {
    @Resource
    private IRegionService regionService;

    @GetMapping("/findByParentCode/{parentCode}")
    public Result<List<Region>> findByParentCode(@PathVariable String parentCode) {
        return regionService.findByParentCode(parentCode);
    }
}