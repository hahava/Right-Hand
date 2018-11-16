<!--init script-->
$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/new",
        dataType: 'json',
        success: function (result) {
            var data = result.data;
            var authority = result.data.authority;

            switch (authority) {
                case 1:
                    login_user();
                    break;
                // TODO: 관리자용 만들 것
            }

            get_board_list(data, 'content');

        }, error: function () {
            //    TODO : 에러 페이지 제작해야 함
        }
    });
});
