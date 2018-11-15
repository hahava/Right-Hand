// 기본은 dev 게시판
var type = getParameterByName('type') != null ? getParameterByName('type') : 'dev';

// 게시글 번호
var board_seq = getParameterByName('boardSeq');

$(document).ready(function () {

    var board_title;
    var board_info;

    switch (type) {
        case 'dev':
            $('#dev_story_nav').attr('class', 'active');
            break;
        case 'tech':
            $('#it_story_nav').attr('class', 'active');
            break;
    }


    view_detail();

});


function view_detail() {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/detail/" + type + "?boardSeq=" + Number(board_seq),
        dataType: 'json',
        success: function (result) {

            // 로그인 여부와 종류를 확인한다.
            var login_authority = result.data.authority;

            switch (login_authority) {
                case 1:
                    login_user();
                    break;
                default :
            }

            var data = result.data;
            var board_detail = data.boardDetailData;
            var board_content = board_detail.BOARD_CONTENT;

            var editor = new tui.Editor.factory({
                el: document.querySelector('#viewerSection'),
                initialEditType: 'wysiwyg',
                previewStyle: 'vertical',
                viewer: true,
                hideModeSwitch: true,
                initialValue: board_content
            });

            var board_title = board_detail.BOARD_TITLE;
            var nick_name = board_detail.NICK_NAME;
            var board_date = board_detail.BOARD_DATE.substr(0, 10);

            $('#board_title').text(board_title);
            $('#board_date').text(board_date);
            $('#nick_name').text(nick_name);

            /*reply*/
            var reply_list = data.replyDetailData;
            $('#reply_count').text(reply_list.length + " 개의 댓글");
            view_reply(reply_list);

        }, error: function () {
            //    TODO : 에러 페이지 제작해야 함
        }
    });
}

var reply_index = 0;

function view_reply(reply_list) {

    for (var temp = reply_index; temp < reply_list.length; temp++) {
        var reply_content = reply_list[temp].REPLY_CONTENT;
        var reply_date = reply_list[temp].REPLY_DATE;
        var reply_nickName = reply_list[temp].NICK_NAME;

        $('#reply_list').append('<div class="media has-margin-bottom"><a class="pull-left" href="#">' +
            ' <img class="media-object" alt="avatar" src="images/avatar-1.jpg"> </a>' +
            '<div class="media-body"><h6 class="media-heading">' + reply_nickName + '</h6>' +
            ' <p class="text-muted" id="reply_date">' + reply_date.substr(0, 10) + '</p>' +
            reply_content + '  </div>'
        );

    }
}

// 댓글 작성
function send_reply() {

    var reply_success = false;
    $.ajax({
        async: false,
        url: "http://localhost:8080/reply/" + type + "?boardSeq=" + Number(board_seq),
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: $('#reply_content').val(),
        success: function (result) {
            switch (result.code) {
                case 0 :
                    reply_success = true;
                    break;
                default :
                    reply_success = false;
            }
        }, error: function (e) {

        }
    });

    if (reply_success) {
        window.location.href = "http://localhost:8080/board/content?boardSeq=" + board_seq + "&type=" + type;
    }

};