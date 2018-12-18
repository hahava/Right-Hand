$(document).ready(function () {
    var page = getParameterByName('page');
    if (page == null) {
        page = 1;
    }
    $('#myPage').attr('class', 'active');
    setSubPageText('userActivity');
    var data = getUserActivity(page);
    setUserActivityTable(data);
    var addr = {"default": "/user/activity"};
    set_page(data.data, page, addr, 7);
    resizeFooterTag();
});

function getUserActivity(page) {
    var data = null;
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/rhpower?page=' + page,
        dataType: 'json',
        async: false,
        success: function (result) {
            data = result;
        },
        error: function () {
        }
    });
    return data;
}

function setUserActivityTable(data) {
    var totalRhPower = data.data.totalRhPower;
    var infoList = data.data.datas;
    $('#user_RTH').text(data.data.totalRhPower);
    for (var i = 0; i < infoList.length; i++) {
        var activityDate = infoList[i].activityDate.substr(0, 10);
        var boardType = infoList[i].boardType;
        var rhPower = infoList[i].rhPower;
        var count = infoList[i].count;
        var boardSeq = infoList[i].boardSeq;
        var activityType = infoList[i].activityType;
        var content = infoList[i].content;
        var html = '<tr><td >' + count + '</td>\n' +
            '<td>' + activityDate + '</td>\n' +
            '<td>' + activityType + '</td>\n' +
            '<td style="color: skyblue">' + rhPower + '</td>\n' +
            '<td>' + content + '</td></tr>'
        $('#user_token_table_body').append(html);
        if (boardType != null) {
            content = '<a href="/board/content?boardSeq=' + boardSeq + '&type=' + boardType + '">' + content + '</a>'
        }

    }
}