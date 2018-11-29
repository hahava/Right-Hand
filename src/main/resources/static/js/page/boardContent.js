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
        case 'notice':
            $('#notice_nav').attr('class', 'active');
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

            var data = result.data;
            var board_detail = data.data;
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

            $('#board_title').html(board_title);
            $('#board_date').text(board_date);
            $('#nick_name').text(nick_name);

            /*reply*/
            var reply_list = data.replyDetailData;
            $('#reply_count').text(reply_list.length + " 개의 댓글");
            view_reply(reply_list);

            console.log(type + board_seq);

        }, error: function () {
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

// <,> 등의 태그를 &lt, &gt 변환
function tagRemover(tag) {
    var text = tag;
    text = text.replace(/\</g, "&lt");
    text = text.replace(/\>/g, "&gt");
    return text;
}

// 댓글 작성
function send_reply() {
    var session = sessionChecker();
    var authorityLevel = session.data.authorityLevel;
    if ($('#reply_content').val().length < 1) {
        alert("빈칸으로 제출할수 없습니다.");
    }
    var content = $('#reply_content').val();
    content = tagRemover(content);
    if (authorityLevel == 1 || authorityLevel == 103) {
        var data = {
            "boardSeq": Number(board_seq),
            "content": content
        };


        var reply_success = false;
        $.ajax({
            async: false,
            url: "http://localhost:8080/reply/" + type,
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(data),
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
    } else {
        alert("로그인 후 이용해주세요");
        return;
    }
}