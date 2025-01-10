package com.zx.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户收藏表(UserCollect)表实体类
 *
 * @author 积云全哥
 * @since 2024-11-04 13:57:54
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user_collect")
public class UserCollect {
//主键
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
//用户ID
    private Long userId;
//商品skuID
    private Long skuId;
//创建时间
    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;
}

