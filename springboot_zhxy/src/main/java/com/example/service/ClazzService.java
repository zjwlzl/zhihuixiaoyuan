package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.Clazz;

public interface ClazzService extends IService<Clazz> {
    public Page<Clazz> getClazzPage(Page<Clazz> page,String gradeName,String name);
}
