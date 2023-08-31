package com.cycle.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cycle.reggie_take_out.common.R;
import com.cycle.reggie_take_out.dto.SetmealDto;
import com.cycle.reggie_take_out.entity.Category;
import com.cycle.reggie_take_out.entity.Setmeal;
import com.cycle.reggie_take_out.service.CategoryService;
import com.cycle.reggie_take_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @return
     */
    @PostMapping
    public R<String> add(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐：{}", setmealDto);
        setmealService.addWithDish(setmealDto);
        return R.success("新增成功");
    }

    /**
     * 分页查询套餐
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页对象
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
        //构造条件对象
        LambdaQueryWrapper<Setmeal> setmealDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !"".equals(name)) {
            setmealDtoLambdaQueryWrapper.like(Setmeal::getName, name);
        }
        setmealDtoLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行分页
        setmealService.page(setmealPage, setmealDtoLambdaQueryWrapper);
        //将setmealPage转换成setmealDtoPage
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("修改套餐：{}", setmealDto);
        setmealService.updateByIdWithDish(setmealDto);
        return R.success("修改成功");
    }

    /**
     * 修改售卖状态
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status) {
//        Setmeal setmeal = new Setmeal();
//        setmeal.setId(ids);
//        setmeal.setStatus(status);
//        setmealService.updateById(setmeal);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(ids != null,Setmeal::getId, ids);
        List<Setmeal> setmealList = setmealService.list(setmealLambdaQueryWrapper);
        setmealList.forEach(setmeal -> {
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        });
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(ids != null,Setmeal::getId, ids);
        List<Setmeal> setmealList = setmealService.list(setmealLambdaQueryWrapper);
        setmealList.forEach(setmeal -> {
            setmeal.setStatus(0);
            setmealService.updateById(setmeal);
        });
        return R.success("删除成功");
    }


}
