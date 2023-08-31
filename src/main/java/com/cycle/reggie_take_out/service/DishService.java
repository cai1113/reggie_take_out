package com.cycle.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cycle.reggie_take_out.dto.DishDto;
import com.cycle.reggie_take_out.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavors(DishDto dishDto);

    public DishDto getByIdWithFlavors(Long id);

    public void updateWithFlavors(DishDto dishDto);
}
