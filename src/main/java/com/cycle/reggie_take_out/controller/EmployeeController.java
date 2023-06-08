package com.cycle.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cycle.reggie_take_out.common.R;
import com.cycle.reggie_take_out.entity.Employee;
import com.cycle.reggie_take_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        /**
         * 1.根据用户名和密码查询用户
         * 2.如果用户存在，登录成功，否则登录失败
         */
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee employeeOne = employeeService.getOne(employeeLambdaQueryWrapper);

        if (employeeOne != null) {
            if (employeeOne.getPassword().equals(password)) {
                if(employeeOne.getStatus() == 0){
                    //账号已被禁用
                    return R.error("账号已被禁用");
                }else {
                    //登录成功
                    request.getSession().setAttribute("employee", employeeOne.getId());
                    return R.success(employeeOne);
                }
            } else {
                //密码错误
                return R.error("登录失败");
            }
        } else {
            //用户不存在
            return R.error("用户不存在");
        }
    }

    /**
     * 注销
     * @param request
     * @return
     */
    @PostMapping("logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("注销成功");
    }

    /**
     * 添加员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("添加员工：{}", employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页查询员工列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("查询员工列表：page={},pageSize={},name={}", page, pageSize, name);
        //构造分页构造器
        Page<Employee> employeePage = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName, name);
        employeeLambdaQueryWrapper.orderByDesc(Employee::getCreateTime);
        //调用分页查询方法
        employeeService.page(employeePage, employeeLambdaQueryWrapper);
        return R.success(employeePage);
    }

    /**
     * 根据id删除员工
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updata(HttpServletRequest request,@RequestBody Employee employee){
        log.info("修改员工：{}", employee.toString());
        long id = Thread.currentThread().getId();
        log.info("当前线程id：{}", id);
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("查询员工：{}", id);
        Employee employee = employeeService.getById(id);
        if (employee == null){
            return R.error("员工不存在");
        }
        return R.success(employee);
    }

}
