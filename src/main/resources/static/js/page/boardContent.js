// 기본은 dev 게시판
var type = getParameterByName('type') != null ? getParameterByName('type') : 'dev';

// 게시글 번호
var board_seq = getParameterByName('boardSeq');

$(document).ready(function () {

    setSubPageText(type);
    setNavActive(type);
    var board_content = getBoardContent(type, board_seq);
    setBoardContentView(board_content.data);
    setReplyView(board_content);
});

function setToggle(elem) {
    if (elem.id == 'coin') {
        $('#coin').attr('class', 'toggle btn btn-xs btn-success active');
        $('#rh_power').attr('class', 'toggle btn btn-xs btn-default');
    } else {
        $('#rh_power').attr('class', 'toggle btn btn-xs btn-success active');
        $('#coin').attr('class', 'toggle btn btn-xs btn-default');
    }
}

function getBoardContent(type, board_seq) {
    var content;
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/detail/" + type + "?boardSeq=" + Number(board_seq),
        dataType: 'json',
        async: false,
        success: function (result) {
            var data = result.data;
            // 게시글 내용과 해당 게시글의 댓글
            content = {"data": data.data, "reply_list": data.replyDetailData, "total_coin": data.totalRhCoin};
        }, error: function () {
        }
    });
    return content;
}

function setBoardContentView(content) {

    var board_title = content.BOARD_TITLE;
    var board_content = content.BOARD_CONTENT;
    var nick_name = content.NICK_NAME;
    var board_date = content.BOARD_DATE.substr(0, 10);
    var profile = (content.FILE_PATH != null) ? (content.FILE_PATH) : 'https://via.placeholder.com/128';

    $('#board_title').html(board_title);
    $('#board_date').text(board_date);
    $('#nick_name').text(nick_name);
    $('#board_user_profile').attr('src', profile);

    var editor = new tui.Editor.factory({
        el: document.querySelector('#viewerSection'),
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical',
        viewer: true,
        hideModeSwitch: true,
        height: '500px',
        initialValue: board_content
    });
}


function setReplyView(board_content) {
    var reply_list = board_content.reply_list;
    $('#reply_count').text(reply_list.length + " 개의 댓글 (" + board_content.total_coin + "$)");

    for (var temp = 0; temp < reply_list.length; temp++) {
        var reply_content = reply_list[temp].REPLY_CONTENT;
        var reply_date = reply_list[temp].REPLY_DATE;
        var reply_nickName = reply_list[temp].NICK_NAME;
        var reply_profile = (reply_list[temp].FILE_PATH != null) ? reply_list[temp].FILE_PATH : 'https://via.placeholder.com/128';
        var reply_coin = reply_list[temp].REPLY_RH_COIN;
        $('#reply_list').append('<div class="media has-margin-bottom"><a class="pull-left" href="#none">' +
            ' <img class="media-object" alt="avatar" src=' + reply_profile + '> </a>' +
            '<div class="media-body"><h6 class="media-heading"><a class="link-reverse" href="#none">' + reply_nickName + '</a><span class="pull-right" id="reply_coin">' + reply_coin + '&nbsp;$</span></h6>' +
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

    var token;
    var token_value = $('#coin_value').val();

    if ($('#coin').hasClass('active')) {
        token = 'coin';
    } else {
        token = 'rp';
    }

    console.log(token);

    if (authorityLevel == 1 || authorityLevel == 103) {
        var data = {
            "boardSeq": Number(board_seq),
            "content": content
        };
        var reply_success = false;
        $.ajax({
            async: false,
            url: "http://localhost:8080/reply/" + type + "?ctype=" + token + "&coin=" + token_value,
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
                        alert(result.message);
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