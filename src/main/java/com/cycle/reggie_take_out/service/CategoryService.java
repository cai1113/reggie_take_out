package com.cycle.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cycle.reggie_take_out.entity.Category;

public interface CategoryService extends IService<Category> {
    public void removeById(Long id);
}
