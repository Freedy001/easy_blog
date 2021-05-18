package com.freedy.backend.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.entity.CategoryEntity;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.Local;
import com.freedy.backend.utils.Result;
import com.freedy.backend.exception.NoPermissionsException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.TagEntity;
import com.freedy.backend.service.TagService;
import com.freedy.backend.utils.PageUtils;


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
    public Result list(@RequestParam Map<String, Object> params) {
        PageUtils page = tagService.queryPage(params);
        return Result.ok().put("page", page);
    }

    @RecordLog(type = RecordEnum.TAG)
    @ApiOperation("保存标签")
    @PostMapping("/save")
    public Result saveTag(@RequestBody TagEntity tag) {
        tag.setCreatorId(Local.MANAGER_LOCAL.get().getId());
        tagService.save(tag);
        return Result.ok();
    }

    @RecordLog(type = RecordEnum.TAG)
    @ApiOperation("修改标签")
    @PostMapping("/update")
    public Result updateTag(@RequestBody TagEntity tag) {
        ManagerEntity entity = Local.MANAGER_LOCAL.get();
        //修改他人且没权限
        if (!tag.getCreatorId().equals(entity.getId())&&entity.getStatus()!=1)
            throw new NoPermissionsException();
        tagService.updateById(tag);
        return Result.ok();
    }

    @RecordLog(type = RecordEnum.TAG)
    @ApiOperation("删除标签")
    @GetMapping("/delete")
    public Result deleteTag(Integer[] ids) {
        tagService.deleteTags(Arrays.asList(ids));
        return Result.ok();
    }


    @ApiOperation("获取建议")
    @GetMapping("/getSuggestion")
    public Result getSuggestion(@RequestParam String queryString) {
        List<TagEntity> list = tagService.list(new QueryWrapper<TagEntity>()
                .lambda().like(TagEntity::getTagName, queryString)
        );
        return Result.ok().setData(list);
    }

}
