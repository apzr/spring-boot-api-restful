package ${package.Controller};

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.uxlt.project.core.Result;
import com.uxlt.project.core.ResultGenerator;

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.*;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Resource
    private ${table.serviceName}  ${table.serviceImplName?uncap_first};

    @ApiOperation(value = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "${entity?uncap_first}", value = "待添加的对象", paramType = "body")
    })
    @PostMapping
    public Result add(@RequestBody @ModelAttribute("${entity}") ${entity} ${entity?uncap_first}) {
        ${table.serviceImplName?uncap_first}.save(${entity?uncap_first});
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "待删除的对象的ID", required=true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        ${table.serviceImplName?uncap_first}.removeById(id);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "${entity?uncap_first}", value = "待更新的对象", required=true, paramType = "body")
    })
    @PutMapping
    public Result update(@RequestBody ${entity} ${entity?uncap_first}) {
        ${table.serviceImplName?uncap_first}.updateById(${entity?uncap_first});
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "待查询的对象的ID", required=true, paramType = "path")
    })
    @GetMapping("/{id}")
    public Result detail(@PathVariable String id) {
        ${entity} ${entity?uncap_first} =  ${table.serviceImplName?uncap_first}.getById(id);
        return ResultGenerator.genSuccessResult(${entity?uncap_first});
    }

    @ApiOperation(value = "列表", notes="分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码，首页为1", defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页容量，每一页显示的数据条数", defaultValue = "100", paramType = "query"),
            @ApiImplicitParam(name = "query", value = "分页查询条件", paramType = "body")
    })
    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,
                       @RequestBody @ModelAttribute("${entity}") @Nullable ${entity} query) {
        Page p = new Page(page, size);

        LambdaQueryWrapper<${entity}> queryParam = null;
        if(Objects.nonNull(query))
            queryParam = Wrappers.lambdaQuery(query);

		IPage<${entity}> pageList = userServiceImpl.page(p, queryParam);

        return ResultGenerator.genSuccessResult(pageList);
    }

}
</#if>
