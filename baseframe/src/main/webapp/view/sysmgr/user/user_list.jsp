<%--
  Created by IntelliJ IDEA.
  User: zjt
  Date: 16/10/1
  Time: 17:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript">

    var pageScope = {};         //页面作用域,每次进入列表页面置为{},避免全局变量都挂载在window下,无法释放或冲突

    /**
     * 初始化用户组织机构树
     */
    pageScope.initCurrentUserOrgTree = function () {

        $.tree.initCurrentUserOrgTree({
            treeId: "organizationTree",
            callback: {
                onClick: pageScope.onClick,             //点击事件
            }
        });

    };

    /**
     *  初始化表格
     */
    pageScope.initUserTable = function () {

        pageScope.userTable = $("#userTable").initBootstrapTable({
            url: '${baseURL}/sysmgr/user/queryUserList?_csrf=' + token,
            method: "post",
            dataType: "json",
            toolbar: '#userPager',
            queryParams: function (params) {
                $.extend(params, $('#searchUserForm').serializeObject());
                return params;
            },
            onClickRow: function (row, tr) {
                console.log(row);
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
                {title: '用户昵称', field: 'nickname', align: 'center', sortable: true},
                {title: '登录账号', field: 'account', align: 'center', sortable: true},
                {title: '手机号码', field: 'phone', align: 'center', sortable: true},
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
                        html += "<button type='button' class='btn btn-link' onclick='pageScope.toEditUser()' ><i class='glyphicon glyphicon-pencil'></i></button>";
                        html += "<button type='button' class='btn btn-link' onclick='pageScope.toUserDetail(\"" + row.id + "\")' ><i class='glyphicon glyphicon-file'></i></button>";
                        html += "<button type='button' class='btn btn-link' onclick='pageScope.toGrantRole(\"" + row.id + "\")' >授予角色</button>";
                        html += "<button type='button' class='btn btn-link' onclick='pageScope.deleteUser(\"" + row.id + "\")' ><i class='glyphicon glyphicon-remove'></i></button>";
                        return html;
                    }
                }
            ]
        });

    };

    /**
     *  查询
     */
    pageScope.search = function () {
        pageScope.userTable.bootstrapTable('refresh');
    };

    /**
     * 新增用户
     */
    pageScope.toAddUser = function () {

        $.dialog.show({
            url: "${baseURL}/view/sysmgr/user/user_add.jsp?" + _csrf + "=" + token,
            onLoad: function () {

            }
        });

    };

    /**
     * 删除用户
     */
    pageScope.deleteUser = function (id) {

        var ids = $("#userTable input[name='btSelectItem']:checked").getCheckedIds(id);

        if ($.validate.isEmpty(ids)) {
            $.msg.toast("请选中一条记录!");
            return;
        }

        $.msg.confirm(function () {

            $.ajax({
                url: "${baseURL}/sysmgr/user/deleteUser",
                type: "post",
                dataType: "json",
                data: {"ids": ids, "_csrf": token},
                success: function (response) {

                    if (response && response.success == true) {
                        $.msg.success(response.msg);
                        pageScope.userTable.bootstrapTable('refresh');
                    } else {
                        $.msg.error(response.msg);
                    }

                }
            });

        });

    };

    /**
     * 编辑用户
     */
    pageScope.toEditUser = function () {

        $.dialog.show({
            url: "${baseURL}/view/sysmgr/user/user_edit.jsp?" + _csrf + "=" + token,
            onLoad: function () {
                console.log(JSON.stringify(pageScope.currentrow));
                $("editUserId").val(pageScope.currentrow.id);
                $("editNickName").val(pageScope.currentrow.nickname);
                $("editAccount").val(pageScope.currentrow.account);
                $("editPhone").val(pageScope.currentrow.phone);
                $("editOrgId").val(pageScope.currentrow.orgId);
                $("editAvatar").val(pageScope.currentrow.avatar);
            }
        });

    };

    /**
     * 用户详情
     */
    pageScope.toUserDetail = function (id) {
        $.dialog.show({
            url: "${baseURL}/sysmgr/user/toUserDetail",
            params: {"_csrf": token, "id": id}
        });
    };

    /**
     * 授予角色
     * @param id
     */
    pageScope.toGrantRole = function (id) {
        $.dialog.show({
            url: "${baseURL}/view/sysmgr/user/user_grant_role.jsp",
            params: {"_csrf": token, "userId": id},
            size: "small"
        });
    };

    /**
     * 查看节点信息
     * @param event
     * @param treeId
     * @param treeNode
     */
    pageScope.onClick = function (event, treeId, treeNode) {
        console.log(treeNode.id);
        $("#orgId").val(treeNode.id);
        pageScope.search();
    };

    $(function () {
        pageScope.initUserTable();
        pageScope.initCurrentUserOrgTree();
    });

</script>

<div class="row">
    <div class="col-md-12">

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">用户管理</h3>
            </div>
            <div class="panel-body">

                <div class="left-tree">
                    <ul id="organizationTree" class="ztree"></ul>
                </div>

                <div class="tree-content-middle-line"></div>

                <div class="right-content">

                    <!-- 搜索条件 -->
                    <form id="searchUserForm" method="post">
                        <table class="search" width="100%" border="0" cellspacing="0" cellpadding="0">

                            <input type="hidden" name="orgId" id="orgId">

                            <tr>
                                <td width="80" align="right">用户昵称：</td>
                                <td width="150"><input type="text" name="nickname" class="form-control btn-block" placeholder="" aria-describedby="basic-addon1"></td>

                                <td width="80" align="right">登录账号：</td>
                                <td width="150"><input ype="text" name="account" class="form-control btn-block" placeholder="" aria-describedby="basic-addon1"></td>

                                <td width="80" align="right">包含下级：</td>
                                <td width="150">

                                    <label class="checkbox-inline">
                                        <input type="radio" name="isContainChildren" value="1"> 是
                                    </label>

                                    <label class="checkbox-inline">
                                        <input type="radio" name="isContainChildren" value="0" checked> 不是
                                    </label>

                                </td>

                                <td colspan="2">
                                    <input class="btn btn-default btn-search" type="button" value="查 询" onclick="pageScope.search()">
                                    <input class="btn btn-default btn-reset" type="button" value="重 置" onclick="javascript:document.getElementById('searchUserForm').reset(); pageScope.search();">
                                </td>
                            </tr>

                        </table>
                    </form>

                    <!-- 操作按钮 -->
                    <div class="operation-button columns columns-left bars pull-left">

                        <button id="add" class="btn btn-success" onclick="pageScope.toAddUser()">
                            <i class="glyphicon glyphicon-plus"></i> 新增
                        </button>

                        <button id="remove" class="btn btn-danger" onclick="pageScope.deleteUser()">
                            <i class="glyphicon glyphicon-remove"></i> 删除
                        </button>

                    </div>

                    <!-- 数据表格 -->
                    <div class="data-table-wrapper">
                        <table id="userTable"></table>
                        <div id="userPager"></div>
                    </div>

                </div>

            </div>
        </div>

    </div>
</div>