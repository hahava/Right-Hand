<!--init script-->
$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/new",
        dataType: 'json',
        success: function (result) {
            var data = result.data;
            get_board_list(data, 'content');
        }, error: function () {
            //    TODO : 에러 페이지 제작해야 함
        }
    });
});
