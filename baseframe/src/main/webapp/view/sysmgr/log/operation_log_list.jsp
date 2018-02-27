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
                <h3 class="panel-title">日志管理</h3>
            </div>
            <div class="panel-body">

                <!-- 搜索条件 -->
                <form id="searchOperationLogForm" method="post">
                    <table class="search" width="100%" border="0" cellspacing="0" cellpadding="0">

                        <tr>
                            <td width="80" align="right">操作用户：</td>
                            <td width="150"><input type="text" name="userName" class="form-control btn-block" placeholder="" aria-describedby="basic-addon1"></td>

                            <td width="80" align="right">IP地址：</td>
                            <td width="180"><input type="text" name="ip" class="form-control btn-block" placeholder="" aria-describedby="basic-addon1"></td>

                            <td width="80" align="right">操作模块：</td>
                            <td width="180"><input type="text" name="module" class="form-control btn-block" placeholder="" aria-describedby="basic-addon1"></td>

                            <td colspan="2">
                                <input class="btn btn-default btn-search" type="button" value="查 询" onclick="pageScope.search()">
                                <input class="btn btn-default btn-reset" type="button" value="重 置" onclick="javascript:document.getElementById('searchOperationLogForm').reset();pageScope.search();">
                            </td>
                        </tr>

                        <tr>
                            <td width="80" align="right">操作动作：</td>
                            <td width="150"><input type="text" name="operation" class="form-control btn-block" placeholder="" aria-describedby="basic-addon1"></td>

                            <td width="80" align="right">时间范围：</td>
                            <td width="180">
                                <div class='input-group date' id='startTime'>
                                    <input type='text' name="startTime" class="form-control" data-date-format="YYYY-MM-DD HH:mm"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </td>

                            <td width="80" align="center">至</td>
                            <td width="180">
                                <div class='input-group date' id='endTime'>
                                    <input type='text' name="endTime" class="form-control" data-date-format="YYYY-MM-DD HH:mm"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </td>

                        </tr>

                    </table>
                </form>

                <!-- 操作按钮 -->
                <div class="operation-button columns columns-left bars pull-left">

                </div>

                <!-- 数据表格 -->
                <div class="data-table-wrapper">
                    <table id="operationLogTable"></table>
                    <div id="operationLogPager"></div>
                </div>

            </div>
        </div>

    </div>
</div>

<script type="text/javascript">

    var pageScope = {};         //页面作用域,每次进入列表页面置为{},避免全局变量都挂载在window下,无法释放或冲突

    pageScope.initOperationLogTable = function () {

        pageScope.operationLogTable = $('#operationLogTable').initBootstrapTable({
            url: '${baseURL}/sysmgr/log/queryOperationLogList?_csrf=' + token,
            method: 'post',
            dataType: "json",
            toolbar: '#operationLogPager',
            queryParams: function (params) {
                $.extend(params, $('#searchOperationLogForm').serializeObject());
                return params;
            },
            onClickRow: function (row, tr) {
                pageScope.currentrow = row;
            },
            columns: [
                {
                    title: '序号',
                    width: 46,
                    formatter: function () {
                        return arguments[2] + 1;
                    }
                },
                {title: '操作用户', field: 'userName', align: 'center', sortable: true},
                {title: 'IP地址', field: 'ip', align: 'center', sortable: true},
                {title: '操作模块', field: 'module', align: 'center', sortable: true},
                {title: '操作动作', field: 'operation', align: 'center', sortable: true},
                {
                    title: '操作结果',
                    field: 'status',
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return value == 0 ? "失败" : "成功";
                    }
                },
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
                        html += "<button type='button' class='btn btn-link' onclick='pageScope.showLogDetail(\"" + row.id + "\")' ><i class='glyphicon glyphicon-file'></i></button>";
                        return html;
                    }
                }
            ]
        });

    };

    /**
     * 查询
     */
    pageScope.search = function () {
        pageScope.operationLogTable.bootstrapTable('refresh');
    };

    /**
     * 查看详情
     * @param id
     */
    pageScope.showLogDetail = function (id) {

        var dialog = $.dialog.show({
            url: baseURL + "/view/sysmgr/log/operation_log_detail.jsp?" + _csrf + "=" + token,
            onLoad: function () {

                $("#detailUserName").html(pageScope.currentrow.userName);
                $("#detailIp").html(pageScope.currentrow.ip);
                $("#detailModule").html(pageScope.currentrow.module);
                $("#detailOperation").html(pageScope.currentrow.operation);
                $("#detailDescription").html(pageScope.currentrow.description);

                $("#detailStatus").html((pageScope.currentrow.status == "0" ? '失败' :'成功' ));
                $("#detailCreateTime").html(pageScope.currentrow.createTime);

            }
        });

    };

    $(function () {

        pageScope.initOperationLogTable();

        $('#startTime').datetimepicker();
        $('#endTime').datetimepicker();

    });

</script>