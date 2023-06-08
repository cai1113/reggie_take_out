package com.cycle.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cycle.reggie_take_out.common.CustomException;
import com.cycle.reggie_take_out.entity.Category;
import com.cycle.reggie_take_out.entity.Dish;
import com.cycle.reggie_take_out.entity.Setmeal;
import com.cycle.reggie_take_out.mapper.CategoryMapper;
import com.cycle.reggie_take_out.service.CategoryService;
import com.cycle.reggie_take_out.service.DishService;
import com.cycle.reggie_take_out.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void removeById(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果关联了菜品，则不允许删除，抛出异常
        if (count > 0) {
            throw new CustomException("当前分类已经关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        //查询当前分类是否关联了套餐，如果关联了套餐，则不允许删除，抛出异常
        if (count2 > 0) {
            throw new CustomException("当前分类已经关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
