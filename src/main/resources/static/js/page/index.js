// 최신 게시판 데이터를 불러온다.
var getNewBoardList = function () {
    var data;
    $.ajax({
        type: "GET",
        url: "/board/new",
        dataType: "json",
        async: false,
        success: function (result) {
            data = result;
        }, error: function () {
        }
    });
    return data;
};

$(document).ready(function () {
    /*
    * 로그인이 필요한 메뉴를 로그인 없이 접근할 경우,
    * error=true 주소에 붙어 index로 리턴 됨
    */
    var error = getParameterByName('error');
    if (error === 'true') {
        // 새로고침 등의 방지를 위해 로그인 여부를 한번 더 체크
        if (sessionChecker().code == 101) {
            alert("로그인 후 이용해주세요");
        }
    }
    var board_list = getNewBoardList();
    setBoardList(board_list.data, "content");
    resizeFooterTag();

});