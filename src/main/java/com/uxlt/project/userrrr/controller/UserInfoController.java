package com.uxlt.project.userrrr.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uxlt.project.core.Result;
import com.uxlt.project.core.ResultGenerator;
import com.uxlt.project.userrrr.entity.UserInfo;
import com.uxlt.project.userrrr.service.IUserInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author April Z
 * @since 2022-01-05
 */
@RestController
@RequestMapping("/userrrr/user-info")
public class UserInfoController {

    @Resource
    private IUserInfoService  userInfoServiceImpl;

    @ApiOperation(value = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userInfo", value = "待添加的对象", paramType = "body")
    })
    @PostMapping
    public Result add(@RequestBody @ModelAttribute("UserInfo") UserInfo userInfo) {
        userInfoServiceImpl.save(userInfo);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "待删除的对象的ID", required=true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        userInfoServiceImpl.removeById(id);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userInfo", value = "待更新的对象", required=true, paramType = "body")
    })
    @PutMapping
    public Result update(@RequestBody UserInfo userInfo) {
        userInfoServiceImpl.updateById(userInfo);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "待查询的对象的ID", required=true, paramType = "path")
    })
    @GetMapping("/{id}")
    public Result detail(@PathVariable String id) {
        UserInfo userInfo =  userInfoServiceImpl.getById(id);
        return ResultGenerator.genSuccessResult(userInfo);
    }

    @ApiOperation(value = "列表", notes="分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码，首页为1", defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页容量，每一页显示的数据条数", defaultValue = "100", paramType = "query"),
            @ApiImplicitParam(name = "query", value = "分页查询条件", paramType = "body")
    })
    @GetMapping
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size,
                       @RequestBody @ModelAttribute("UserInfo") @Nullable UserInfo query) {
        Page p = new Page(page, size);

        LambdaQueryWrapper<UserInfo> queryParam = null;
        if(Objects.nonNull(query))
            queryParam = Wrappers.lambdaQuery(query);

		IPage<UserInfo> pageList = userInfoServiceImpl.page(p, queryParam);

        return ResultGenerator.genSuccessResult(pageList);
    }

}
