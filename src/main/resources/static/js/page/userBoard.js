$(document).ready(function () {
    var page = getParameterByName('page');
    if (page == null) {
        page = 1;
    }
    setNavActive("userInfo");
    setSubPageText("userBoard");
    var userboard_list = getUserBoardList(page);
    setBoardList(userboard_list.data, 'content');
    var addr = {"default": "/user/board"};
    set_page(userboard_list.data, page, addr, 5);
    resizeFooterTag();

});


function getUserBoardList(page) {
    var data;
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/myBoard?page=' + page,
        dataType: 'json',
        async: false,
        success: function (result) {
            data = result;
            console.log(result);
        }, error: function (e) {
        }
    });
    return data;
}

