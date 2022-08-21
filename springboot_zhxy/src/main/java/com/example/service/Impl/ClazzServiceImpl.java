package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ClazzMapper;
import com.example.pojo.Clazz;
import com.example.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {

    @Autowired
    ClazzMapper clazzMapper;
    @Override
    public Page<Clazz> getClazzPage(Page<Clazz> page, String gradeName, String name) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        Page<Clazz> clazzPage = clazzMapper.selectPage(page, queryWrapper);

        return clazzPage;
    }
}
