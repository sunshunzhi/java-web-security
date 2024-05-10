package com.youth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User implements Serializable {
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 用户名
     */
    @TableField("userName")
    private String username;
    /**
     * 昵称
     */
    @TableField("nickName")
    private String nickName;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    /**
     * 账号状态（0 正常 1停用）
     */
    @TableField("status")
    private String status;
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 手机号
     */
    @TableField("phoneNumber")
    private String phoneNumber;
    /**
     * 用户性别（0男，1女，2未知）
     */
    @TableField("sex")
    private String sex;
    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;
    /**
     * 用户类型（0管理员，1普通用户）
     */
    @TableField("userType")
    private String userType;
    /**
     * 创建人的用户id
     */
    @TableField("createBy")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;
    /**
     * 更新人
     */
    @TableField("updateBy")
    private Long updateBy;
    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;
    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("delFlag")
    private Integer delFlag;
}


