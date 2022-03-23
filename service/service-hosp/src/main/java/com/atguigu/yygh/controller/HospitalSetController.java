package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.service.HospitalSetService;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


@Api("医院设置管理")
@RestController
@RequestMapping("/api/hospitalSet")
@CrossOrigin
public class HospitalSetController {

    //注入HospitalSetService
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询医院设置所有信息
    @ApiOperation("获取所有医院设置")
    @GetMapping("/findAllHospitalSet")
    public Result findAllHospitalSet(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //根据id逻辑删除医院设置
    @ApiOperation("根据id删除医院设置")
    @DeleteMapping("/removeHospitalSet/{id}")
    public Result removeHospitalSet(@PathVariable Long id){
        boolean isRemove = hospitalSetService.removeById(id);
        if(isRemove) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //条件查询带分页
    @ApiOperation("条件查询医院设置带分页")
    @PostMapping("/findPageHospitalSet/{current}/{size}")
    public Result findPageHospitalSet(@PathVariable long current,
                                      @PathVariable long size,
                                      @RequestBody(required = false)HospitalSetQueryVo hospitalSetQueryVo){
        //创建page对象，传递当前页和每页记录数
        Page<HospitalSet> page = new Page<>(current, size);
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        //获取查询内容
        String hosname = "";
        String hoscode = "";
        if(hospitalSetQueryVo != null) {
            hosname = hospitalSetQueryVo.getHosname();
            hoscode = hospitalSetQueryVo.getHoscode();
        }
        if(!StringUtils.isEmpty(hosname)){
            //查询医院名字中带有hosname信息的医院设置
            wrapper.like("hosname", hosname);
        }
        if(!StringUtils.isEmpty(hoscode)){
            //根据医院编号查询
            wrapper.eq("hoscode", hoscode);
        }
        //实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        return Result.ok(pageHospitalSet);
    }

    //添加医院设置
    @ApiOperation("添加医院设置")
    @PostMapping("/addHospitalSet")
    public Result addHospitalSet(@RequestBody HospitalSet hospitalSet){
        //新增医院设置状态--->0、不可用   1、可用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        //调用方法，执行保存操作
        boolean isSave = hospitalSetService.save(hospitalSet);
        if(isSave) {
            return Result.ok(hospitalSet);
        }else {
            return Result.fail();
        }
    }

    //根据医院id获取医院设置
    @ApiOperation("根据医院id获取医院设置")
    @GetMapping("/getHospitalSet/{id}")
    public Result getHospitalSetById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //修改医院设置
    @ApiOperation("修改医院设置")
    @PostMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean isUpdate = hospitalSetService.updateById(hospitalSet);
        if(isUpdate){
            return Result.ok(hospitalSet);
        }else {
            return Result.fail();
        }
    }

    //批量删除医院设置
    @ApiOperation("批量删除医院设置")
    @DeleteMapping("/removeHospitalSetBatch")
    public Result removeHospitalSetBatch(List<Long> ids){
        boolean isRemove = hospitalSetService.removeByIds(ids);
        if(isRemove) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}
