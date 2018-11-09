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

            var board_list = data.data;
            var total = data.total;

            for (var i = 0; i < board_list.length; i++) {
                var seq = board_list[i].BOARD_SEQ;
                var title = board_list[i].BOARD_TITLE;
                var content = board_list[i].BOARD_CONTENT;
                var nick_name = board_list[i].NICK_NAME;
                var board_type = board_list[i].BOARD_TYPE;
                var date = board_list[i].BOARD_DATE.substring(0, 10);

                $('#board_list').append('  <div class="row has-margin-bottom">' +
                    '<div class="col-md-4 col-sm-4">' +
                    '<img class="img-responsive center-block"  th:src="@{../images/blog-thumb-1.jpg}" alt="bulletin blog"src="images/blog-thumb-1.jpg"> </div>' +
                    '<div class="col-md-8 col-sm-8 bulletin">' +
                    '<a href="/board/content?boardSeq=' + seq + '&type=' + board_type + ' "><h4 class="media-heading" id="title">' + title + ' </h4></a>' +
                    '<p>' + date + ' <a href="#" class="link-reverse">' + nick_name + '</a></p>' +
                    '<p>' + content + '</p></div></div>');
            }

        }, error: function () {
            //    TODO : 에러 페이지 제작해야 함
        }
    });
});
