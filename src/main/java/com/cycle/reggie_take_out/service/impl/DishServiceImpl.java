package com.cycle.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cycle.reggie_take_out.entity.Dish;
import com.cycle.reggie_take_out.mapper.DishMapper;
import com.cycle.reggie_take_out.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
