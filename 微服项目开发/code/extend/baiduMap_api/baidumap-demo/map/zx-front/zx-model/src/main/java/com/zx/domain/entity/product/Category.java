package com.zx.domain.entity.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zx.domain.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "分类实体类")
public class Category extends BaseEntity {

	@Schema(description = "分类名称")
	private String name;

	@Schema(description = "分类图片url")
	private String imageUrl;

	@Schema(description = "父节点id")
	private Long parentId;

	@Schema(description = "分类状态: 是否显示[0-不显示，1显示]")
	private Integer status;

	@Schema(description = "排序字段")
	private Integer orderNum;

	@Schema(description = "是否存在子节点")
	@TableField(exist = false)
	private Boolean hasChildren;

	@Schema(description = "子节点List集合")
	@TableField(exist = false)
	private List<Category> children;

}
