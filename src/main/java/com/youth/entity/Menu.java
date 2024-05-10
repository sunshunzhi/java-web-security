package com.youth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("sys_menu")
public class Menu {
    @TableId
    private Long id;
    @TableField("menuName")
    private String menuName;
    @TableField("path")
    private String path;
    @TableField("component")
    private String component;
    @TableField("visible")
    private String visible;
    @TableField("status")
    private String status;
    @TableField("icon")
    private String icon;
    @TableField("createBy")
    private Long createBy;
    @TableField("createTime")
    private Date createTime;
    @TableField("updateBy")
    private Long updateBy;
    @TableField("updateTime")
    private Date updateTime;
    @TableField("delFlg")
    private int delFlg;
    @TableField("remark")
    private String remark;
}

