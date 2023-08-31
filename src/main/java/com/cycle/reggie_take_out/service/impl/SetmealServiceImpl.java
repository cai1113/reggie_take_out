package com.cycle.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cycle.reggie_take_out.dto.SetmealDto;
import com.cycle.reggie_take_out.entity.Setmeal;
import com.cycle.reggie_take_out.entity.SetmealDish;
import com.cycle.reggie_take_out.mapper.SetmealMapper;
import com.cycle.reggie_take_out.service.SetmealDishService;
import com.cycle.reggie_take_out.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void addWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息
        this.save(setmealDto);
        //保存套餐和菜品的关系
        setmealDto.getSetmealDishes().stream().map(item->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());

    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //根据id查询套餐基本信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //对象属性拷贝
        BeanUtils.copyProperties(setmeal, setmealDto);
        //根据套餐id查询套餐和菜品的关系
        //构造条件查询
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        setmealDto.setSetmealDishes(setmealDishService.list(setmealDishLambdaQueryWrapper));
        return setmealDto;
    }

    @Override
    @Transactional
    public void updateByIdWithDish(SetmealDto setmealDto) {
        //更新套餐基本信息
        this.updateById(setmealDto);
        //清理原有的套餐和菜品的关系
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        //保存新的套餐和菜品的关系
        setmealDto.getSetmealDishes().stream().map(item->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }
}
