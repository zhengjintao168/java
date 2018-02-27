<%--
  Created by IntelliJ IDEA.
  User: zjt
  Date: 16/10/1
  Time: 17:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="row">
    <div class="col-md-12">

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">角色管理</h3>
            </div>
            <div class="panel-body">

                <!-- 搜索条件 -->
                <form id="searchRoleForm" method="post">
                    <table class="search" width="100%" border="0" cellspacing="0" cellpadding="0">

                        <tr>
                            <td width="80" align="right">角色名称：</td>
                            <td width="150"><input type="text" name="name" class="form-control btn-block" placeholder="" aria-describedby="basic-addon1"></td>

                            <td width="80" align="right">角色码：</td>
                            <td width="150"><input ype="text" name="code" class="form-control btn-block" placeholder="" aria-describedby="basic-addon1"></td>

                            <td colspan="2">
                                <input class="btn btn-default btn-search" type="button" value="查 询" onclick="pageScope.search()">
                                <input class="btn btn-default btn-reset" type="button" value="重 置" onclick="javascript:document.getElementById('searchRoleForm').reset(); pageScope.search();">
                            </td>
                        </tr>

                    </table>
                </form>

                <!-- 操作按钮 -->
                <div class="operation-button columns columns-left bars pull-left">

                    <button id="add" class="btn btn-success" onclick="pageScope.addRole()">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>

                    <button id="remove" class="btn btn-danger" onclick="pageScope.deleteRole()">
                        <i class="glyphicon glyphicon-remove"></i> 删除
                    </button>

                </div>

                <!-- 数据表格 -->
                <div class="data-table-wrapper">
                    <table id="roleTable"></table>
                    <div id="rolePager"></div>
                </div>

            </div>
        </div>

    </div>
</div>

<script type="text/javascript">

    var pageScope = {};         //页面作用域,每次进入列表页面置为{},避免全局变量都挂载在window下,无法释放或冲突

    $(function () {

        /**
         * 加载列表
         */
        pageScope.roleTable = $('#roleTable').initBootstrapTable({
            url: '${baseURL}/sysmgr/role/queryRoleList?_csrf=' + token,
            method: 'post',
            dataType: "json",
            toolbar: '#rolePager',
            queryParams: function (params) {
                $.extend(params, $('#searchRoleForm').serializeObject());
                return params;
            },
            onClickRow: function (row, tr) {
                pageScope.currentrow = row;
            },
            columns: [
                {title: '全选', checkbox: true},                //正确
                {
                    title: '序号',
                    width: 46,
                    formatter: function () {
                        return arguments[2] + 1;
                    }
                },
                {title: '角色名称', field: 'name', align: 'center', sortable: true},
                {title: '角色码', field: 'code', align: 'center', sortable: true},
                {
                    title: '创建时间',
                    field: 'createTime',
                    align: 'center',
                    sortable: true,
                    formatter: function (value) {
                        return $.date.formatToDateTime(value);
                    }
                },
                {
                    title: '操作',
                    align: 'center',
                    formatter: function (value, row, index) {
                        var html = "";
                        html += "<button type='button' class='btn btn-link' onclick='pageScope.editRole()' ><i class='glyphicon glyphicon-pencil'></i></button>";
                        html += "<button type='button' class='btn btn-link' onclick='pageScope.toGrantPermission(\"" + row.id + "\")' >授予权限</button>";
                        html += "<button type='button' class='btn btn-link' onclick='pageScope.deleteRole(\"" + row.id + "\")' ><i class='glyphicon glyphicon-remove'></i></button>";
                        return html;
                    }
                }
            ]
        });

    });

    /**
     * 查询
     */
    pageScope.search = function () {
        pageScope.roleTable.bootstrapTable('refresh');
    };

    /**
     * 新增角色
     */
    pageScope.addRole = function () {

        var dialog = $.dialog.show({
            url: baseURL + "/view/sysmgr/role/role_add.jsp?" + _csrf + "=" + token,
            buttonEvent: {
                success: function () {

                    if (!$("#addRoleForm").valid()) {                   //表单验证
                        return;
                    }

                    var btn = $(".modal-footer .btn-success");        //防止重复提交
                    btn.attr("disabled", "disabled");

                    $('#addRoleForm').ajaxSubmit({
                        dataType: 'json',
                        type: "post",
                        success: function (response) {

                            btn.removeAttr("disabled");

                            if (response && response.success) {
                                $.msg.success(response.msg);
                                pageScope.roleTable.bootstrapTable('refresh');
                                $(".modal-footer .btn-danger").trigger("click");
                            }
                            else {
                                $.msg.error(response.msg);
                            }

                        }, error: function (e) {
                            btn.removeAttr("disabled");
                        }

                    });

                }
            }
        });

    };

    /**
     * 删除角色
     */
    pageScope.deleteRole = function (id) {

        var ids = $("#roleTable input[name='btSelectItem']:checked").getCheckedIds(id);

        if ($.validate.isEmpty(ids)) {
            $.msg.toast("请选中一条记录!");
            return;
        }

        $.msg.confirm(function () {

            $.ajax({
                url: "${baseURL}/sysmgr/role/deleteRole",
                type: "post",
                dataType: "json",
                data: {"ids": ids, "_csrf": token},
                success: function (response) {

                    if (response && response.success == true) {
                        $.msg.success(response.msg);
                        pageScope.roleTable.bootstrapTable('refresh');
                    } else {
                        $.msg.error(response.msg);
                    }

                }
            });

        });

    };

    /**
     * 编辑角色
     */
    pageScope.editRole = function () {

        var dialog = $.dialog.show({
            url: baseURL + "/view/sysmgr/role/role_edit.jsp?" + _csrf + "=" + token,
            onLoad: function () {
                $("#editRoleId").val(pageScope.currentrow.id);
                $("#editName").val(pageScope.currentrow.name);
                $("#editCode").val(pageScope.currentrow.code);
            },
            buttonEvent: {
                success: function () {
                    if (!$("#editRoleForm").valid()) {            //表单验证
                        return;
                    }

                    var btn = $(".modal-footer .btn-success");        //防止重复提交
                    btn.attr("disabled", "disabled");

                    $('#editRoleForm').ajaxSubmit({
                        dataType: 'json',
                        type: "post",
                        success: function (response) {

                            btn.removeAttr("disabled");

                            if (response && response.success) {
                                $.msg.success(response.msg);
                                pageScope.roleTable.bootstrapTable('refresh');
                                $(".modal-footer .btn-danger").trigger("click");
                            }
                            else {
                                $.msg.error(response.msg);
                            }

                        }, error: function (e) {
                            btn.removeAttr("disabled");
                        }

                    });
                }
            }
        });

    };

    /**
     * 授予权限
     * @param id
     */
    pageScope.toGrantPermission = function (id) {

        var dialog = $.dialog.show({
            url: baseURL + "/view/sysmgr/role/role_grant_permission.jsp?" + _csrf + "=" + token + "&roleId=" + id,
            size: "small"
        });

    };

</script>