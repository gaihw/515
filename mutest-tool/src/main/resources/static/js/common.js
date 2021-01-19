/**
 * 表格重载
 * @param tableId
 * @param where
 * @param contentType
 * @param url
 * @param method
 */
function tableReload(tableId, where, contentType, url, method) {
    layui.table.reload(tableId, {
        where: where,
        contentType: contentType,
        page: {
            curr: 1 //重新从第 1 页开始
        },
        url: url
        , method: method
    });
}

function checkForm(formId) {
    var form = document.getElementById(formId);
    var count = 0;
    for (var i = 0; i < form.elements.length - 1; i++) {
        if (!form.elements[i].value == "") {
            count++;
        }
    }
    return count;
}

/**
 * 给下拉选择框赋值
 * @param url
 * @param select
 */
function selectInit(url, select, title) {
    if (title == null) {
        title = '请选择';
    }
    select.empty().append("<option value=''>" + title + "</option>");
    $.ajax({
        type: 'get',
        url: url,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            $.each(data, function (index, item) {
                select.append(new Option(item));//往下拉菜单里添加元素
            });
            layui.form.render();//菜单渲染 把内容加载进去
        }, error: function (e) {//响应不成功时返回的函数
            console.log(e, 'error');
        }
    });
}