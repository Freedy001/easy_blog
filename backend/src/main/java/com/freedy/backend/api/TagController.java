package com.freedy.backend.api;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.AuthorityUtils;
import com.freedy.backend.common.utils.Local;
import com.freedy.backend.common.utils.Result;
import com.freedy.backend.exception.NoPermissionsException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.TagEntity;
import com.freedy.backend.service.TagService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 文章标签
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @ApiOperation("列出所有标签")
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);
        return Result.ok().put("page", page);
    }


    @ApiOperation("保存标签")
    @PostMapping("/save")
    public Result save(@RequestBody TagEntity tag){
        tag.setCreatorId(Local.MANAGER_LOCAL.get().getId());
		tagService.save(tag);
        return Result.ok();
    }

    @ApiOperation("修改标签")
    @PostMapping("/update")
    public Result update(@RequestBody TagEntity tag){
        //修改他人且没权限
        if (!tag.getCreatorId().equals(Local.MANAGER_LOCAL.get().getId())&&
                !AuthorityUtils.hasAuthority("tag-operation-to-others"))
            throw new NoPermissionsException();
		tagService.updateById(tag);
        return Result.ok();
    }

    @ApiOperation("删除标签")
    @GetMapping("/delete")
    public Result delete(Integer[] ids){
       boolean exception=false;
        for (Integer id : ids) {
            if (!id.equals(Local.MANAGER_LOCAL.get().getId())&&
                    !AuthorityUtils.hasAuthority("tag-operation-to-others")){
                exception=true;
            }else {
                tagService.removeByIds(Arrays.asList(ids));
            }
        }
        if (exception){
            throw new NoPermissionsException();
        }
        return Result.ok();
    }

}
