package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.Grade;

public interface GradeService extends IService<Grade> {
    Page<Grade> getGradePage(Page<Grade> page, String gradeName);

}
