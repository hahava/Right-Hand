$(document).ready(function () {
    var page = getParameterByName('page');
    if (page == null) {
        page = 1;
    }
    setNavActive("userInfo");
    setSubPageText("userToken");
    var data = getUserTokenList(page);
    setUserTokenTable(data);
    var addr = {"default": "/user/token"};
    set_page(data.data, page, addr, 7);
    resizeFooterTag();

});

function getUserTokenList(page) {
    var data = null;
    $.ajax({
        type: 'GET',
        url: ' /coin?page=' + page,
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

function setUserTokenTable(data) {
    var infoList = data.data.datas;
    for (var i = 0; i < infoList.length; i++) {
        var activityDate = infoList[i].activityDate.substr(0, 10);
        var boardType = infoList[i].boardType;
        var rhCoin = infoList[i].rhCoin;
        var count = infoList[i].count;
        var boardSeq = infoList[i].boardSeq;
        var activityType = infoList[i].activityType;
        var content = infoList[i].content;
        if (boardType != null) {
            content = '<a href="/board/content?boardSeq=' + boardSeq + '&type=' + boardType + '">' + content + '</a>'
        }
        var isSender = infoList[i].isSender;
        var color = 'skyblue';
        if (isSender) {
            color = 'red';
        }
        var html = '<tr><td >' + count + '</td>\n' +
            '<td>' + activityDate + '</td>\n' +
            '<td>' + activityType + '</td>\n' +
            '<td style="color:' + color + '">' + rhCoin + '</td>\n' +
            '<td>' + content + '</td></tr>'
        $('#user_token_table_body').append(html);
    }
}