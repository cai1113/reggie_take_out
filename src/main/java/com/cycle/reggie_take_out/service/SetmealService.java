package com.cycle.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cycle.reggie_take_out.dto.SetmealDto;
import com.cycle.reggie_take_out.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐
     * @param setmealDto
     */
    public void addWithDish(SetmealDto setmealDto);

    public SetmealDto getByIdWithDish(Long id);

    public void updateByIdWithDish(SetmealDto setmealDto);
}
