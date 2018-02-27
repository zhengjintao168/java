<%--
  Created by IntelliJ IDEA.
  User: zjt
  Date: 2016/10/6
  Time: 15:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" >

    $(function(){
        $('#editPermissionForm').validation();
    });

    /**
     * 修改权限信息
     */
    pageScope.updatePermission = function(){

        if(!$("#editPermissionForm").valid()){                   //表单验证
            return;
        }

        var btn = $(".modal-footer .btn-success");            //防止重复提交
        btn.attr("disabled","disabled");

        if($('#editPermissionForm').valid()){

            $('#editPermissionForm').ajaxSubmit({
                dataType: 'json',
                type: "post",
                success: function (response) {

                    btn.removeAttr("disabled");

                    if(response && response.success){
                        $.msg.success(response.msg);

                        //重新加载树
                        pageScope.initPermissionTree();

                        //重新加载详情内容
                        pageScope.loadDetailContent("${permission.id}");

                        $(".modal-footer .btn-danger").trigger("click");
                    }
                    else{
                        $.msg.error(response.msg);
                    }

                },error:function(e){
                    btn.removeAttr("disabled");
                }

            });

        }

    }

</script>

<div class="modal-header">
    <button type="button" class="bootbox-close-button close" data-dismiss="modal" aria-hidden="true">×</button>
    <h4 class="modal-title" >
        <c:if test="${permission.level == 1}">编辑目录</c:if>
        <c:if test="${permission.level == 2}">编辑链接</c:if>
    </h4>
</div>

<div class="modal-body">
    <div class="bootbox-body">

        <form id="editPermissionForm" action="${baseURL}/sysmgr/permission/updatePermission" class="form-horizontal">

            <input type="hidden" name="${_csrf.parameterName}" value = "${_csrf.token}" />
            <input type="hidden" name="id" value="${permission.id}" />

            <div class="form-group">
                <label class="col-md-3 col-sm-3 control-label" id="labelTitle" >
                    <c:if test="${permission.level == 1}">目录名称：</c:if>
                    <c:if test="${permission.level == 2}">链接名称：</c:if>
                </label>
                <div class="col-md-8 col-sm-8">
                    <input name="name" type="text" value="${permission.name}" class="form-control" check-type="required" placeholder="请输入${param.level == 1 ? '目录名称' : '链接名称'}" maxlength="50" />
                </div>
            </div>

            <%-- 编辑目录 --%>
            <c:if test="${permission.level == 1}">

                <div class="form-group">
                    <label class="col-md-3 col-sm-3 control-label">目录图标：</label>
                    <div class="col-md-8 col-sm-8">
                        <input name="icon" type="text" value="${permission.icon}"  class="form-control" check-type="required" placeholder="请输入目录图标" maxlength="50" />
                    </div>
                </div>

            </c:if>

            <%-- 编辑权限 --%>
            <c:if test="${permission.level == 2}">

                <div class="form-group">
                    <label class="col-md-3 col-sm-3 control-label">链接地址：</label>
                    <div class="col-md-8 col-sm-8">
                        <input name="linkUrl" type="text" value="${permission.linkUrl}"  class="form-control" check-type="required" placeholder="请输入链接地址" maxlength="255" />
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-3 col-sm-3 control-label">权限路径：</label>
                    <div class="col-md-8 col-sm-8">
                        <input name="permissionUrl" type="text" value="${permission.permissionUrl}" class="form-control" check-type="required" placeholder="请输入权限路径" maxlength="255" />
                    </div>
                </div>

            </c:if>

        </form>

    </div>
</div>

<div class="modal-footer operation-button" >
    <button data-bb-handler="success" type="button" class="btn btn-success" onclick="pageScope.updatePermission()" >保存</button>
    <button data-bb-handler="cancel" type="button" class="btn btn-danger" >取消</button>
</div>