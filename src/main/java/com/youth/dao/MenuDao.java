package com.youth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youth.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuDao extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long userId);
}

