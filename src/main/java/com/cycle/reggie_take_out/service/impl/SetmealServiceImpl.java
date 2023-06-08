package com.cycle.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cycle.reggie_take_out.entity.Setmeal;
import com.cycle.reggie_take_out.mapper.SetmealMapper;
import com.cycle.reggie_take_out.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
