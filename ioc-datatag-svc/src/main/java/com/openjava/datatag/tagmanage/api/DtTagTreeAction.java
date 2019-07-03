package com.openjava.datatag.tagmanage.api;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.utils.tree.TreeNode;
import io.swagger.annotations.*;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * api接口
 * @author lch
 *
 */
@Api(tags="DT_TAG_TREE")
@RestController
@RequestMapping("/datatag/tagmanage/dtTagTree")
public class DtTagTreeAction {

    @Resource
    private DtTagService dtTagService;
    @Resource
    private DtTagGroupService dtTagGroupService;
    /**
     * 用主键获取数据
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID获取", notes = "单个对象查询", nickname="id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签组编码", required = true, dataType = "string", paramType = "path"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
    })
    @Security(session=true)
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public TreeNode<DtTag> get(@PathVariable("id")Long id) throws APIException {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        DtTagGroup m = dtTagGroupService.get(id);
        if(m == null || m.getIsDeleted().equals(1L)){
            throw new APIException(10002,"无此标签组或 已被删除");
        }
        if(userInfo.getUserId().equals(m.getCreateUser().toString())){
            List<DtTag> tagList = dtTagService.findByTagsId(id);
            DtTag root = new DtTag();
            root.setId(0L);
            TreeNode<DtTag> treeNode = new TreeNode<>(root);
            antdAddChildNode(tagList, treeNode);
            return treeNode;
        }else{
            throw new APIException(10001,"无权限查看");
        }

    }

    /**
     * 把node的list结构，转为树形
     * @param DtTagList  查询到的属于某标签组的所有标签列
     * @param node 用来保存整个树的根节点
     */
    private void antdAddChildNode(List<DtTag> DtTagList, TreeNode<DtTag> node) {
        for (DtTag tag : DtTagList) {
            if(tag.getPreaTagId() == null){
                tag.setPreaTagId(0L);
            }
            if(tag.getPreaTagId().equals(node.getData().getId())) {
                TreeNode<DtTag> child = new TreeNode<DtTag>(tag);
                node.addChildrenNode(child);
                antdAddChildNode(DtTagList, child);
            }
        }
    }

}
