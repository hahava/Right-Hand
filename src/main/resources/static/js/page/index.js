$(document).ready(function () {
    get_new_board();
});
//최신 게시판 데이터를 불러온다.
var get_new_board = function () {
    $.ajax({
        type: "GET",
        url: "/board/new",
        dataType: "json",
        success: function (result) {
            var data = result.data;
            get_board_list(data, "content");
        }, error: function () {
        }
    });
};