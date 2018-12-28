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
    setReplyWriterView(board_content.writer);
    console.log(type);
    if (type != 'notice') {
        $('#post-comment-form').css('visibility','visible');
    }
    resizeFooterTag();

});

function getBoardContent(type, board_seq) {
    var content;
    $.ajax({
        type: 'GET',
        url: "/board/detail/" + type + "?boardSeq=" + Number(board_seq),
        dataType: 'json',
        async: false,
        success: function (result) {
            var data = result.data;
            // 게시글 내용과 해당 게시글의 댓글
            content = {
                "data": data.data,
                "reply_list": data.replyDetailData,
                "total_coin": data.totalRhCoin,
                "writer": data.isWriter,
                "nickname": data.nickname
            };
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
    var writer_nickname = board_content.nickname;
    var total_coin = board_content.total_coin != null ? board_content.total_coin : 0;
    if (type == 'dev') {
        $('#reply_count').text(reply_list.length + " 개의 댓글");
    } else {
        $('#reply_count').text(reply_list.length + " 개의 댓글 (" + total_coin + "$)");

    }

    for (var temp = 0; temp < reply_list.length; temp++) {
        var reply_content = reply_list[temp].REPLY_CONTENT;
        var reply_date = reply_list[temp].REPLY_DATE;
        var reply_nickName = reply_list[temp].NICK_NAME;
        var reply_profile = (reply_list[temp].FILE_PATH != null) ? reply_list[temp].FILE_PATH : 'https://via.placeholder.com/128';
        var reply_coin_modal = '';
        var reply_coin = reply_list[temp].REPLY_RH_COIN;
        var reply_seq = reply_list[temp].REPLY_SEQ;
        if (reply_coin == 0 && type != 'tech' && board_content.writer && (writer_nickname != reply_nickName)) {
            reply_coin_modal = '<a class="pull-right" data-toggle="modal" data-target="#myModal" id="' + temp + '" onclick="setModalData(this,' + reply_seq + ')" style="border-bottom: #0a001f 1px dotted;">보상하기</a>';
        }
        var html = '<div class="media has-margin-bottom"><a class="pull-left" href="#none">' +
            ' <img class="media-object" alt="avatar" src=' + reply_profile + '> </a>' +
            '<div class="media-body"><h6 class="media-heading">' +
            '<a class="link-reverse" href="#none">' + reply_nickName + '</a>' +
            '<span class="pull-right" id="reply_coin">' + reply_coin + '&nbsp;$</span></h6>' +
            ' <p class="text-muted" id="reply_date">' + reply_date.substr(0, 10) + '</p>' +
            reply_content + reply_coin_modal + '</div></div>';
        $('#reply_list').append(function () {
                return html;
            }
        );

    }
}

function setModalData(elem, reply_seq) {
    $('#myModalLabel').text('코인 보상을 하시겠습니까?');
    $('#reply_seq').val(reply_seq);
}

// <,> 등의 태그를 &lt, &gt 변환
function tagRemover(tag) {
    var text = tag;
    text = text.replace(/\</g, "&lt");
    text = text.replace(/\>/g, "&gt");
    return text;
}

// 댓글 작성
function sendReply() {

    var session = sessionChecker();

    var authorityLevel = session.data.authorityLevel;
    if ($('#reply_content').val().length < 1) {
        alert("빈칸으로 제출할수 없습니다.");
        return;
    }
    var content = $('#reply_content').val();
    content = tagRemover(content);
    var token;
    var token_value = $('#coin_value').val().length != 0 ? $('#coin_value').val() : 0;

    if ($('#coin').hasClass('active')) {
        token = 'coin';
    } else {
        token = 'rp';
    }


    if (authorityLevel == 1 || authorityLevel == 103) {
        var data = {
            "boardSeq": Number(board_seq),
            "content": content
        };
        var reply_success = false;
        $.ajax({
            async: false,
            url: "/reply/" + type + "?ctype=" + token + "&reqCoin=" + parseFloat(token_value),
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(data),
            success: function (result) {
                switch (result.code) {
                    case 320 :
                        reply_success = true;
                        alert("댓글 작성되었습니다.");
                        break;
                    case 322 :
                        reply_success = true;
                        alert("댓글 작성되었습니다.");
                        break;
                    default :
                        alert(result.message + result.code);
                        reply_success = false;
                }
            }, error: function (e) {
            }
        });

        if (reply_success) {
            let url = window.location.protocol + "//" + window.location.host;
            window.location.href = url + "/board/content?boardSeq=" + board_seq + "&type=" + type;
        }
    } else {
        alert("로그인 후 이용해주세요");
        return;
    }
}

function setReplyWriterView(isWriter) {


    var html = " <div class='btn-group btn-toggle pull-right' style='margin-top: 4px;margin-left: 10px'>\n" +
        "                            <button class='btn btn-xs btn-success active' onclick='setToggle(this)'\n" +
        "                                    id='coin'>\n" +
        "                                코인\n" +
        "                            </button>\n" +
        "                            <button class='btn btn-xs btn-default' onclick='setToggle(this)' id='rh_power'>\n" +
        "                                RP\n" +
        "                            </button>\n" +
        "                        </div>\n" +
        "                        <input class='form-control pull-right input-sm' id='coin_value' type=number min=0\n" +
        "                               step=0.001\n" +
        "                               max=10\n" +
        "                               placeholder='코인' size='5' value='0' />";

    /* 작성자일 경우 코인을 전송하지 못하도록 한다.*/
    if (!isWriter && type != 'dev') {
        $('#coin_dev').append(function () {
            return html;
        });
    }
}

function setToggle(elem) {
    if (elem.id == 'coin') {
        $('#coin').attr('class', 'toggle btn btn-xs btn-success active');
        $('#rh_power').attr('class', 'toggle btn btn-xs btn-default');
    } else {
        $('#rh_power').attr('class', 'toggle btn btn-xs btn-success active');
        $('#coin').attr('class', 'toggle btn btn-xs btn-default');
    }
}

function sendCoin() {
    var coin_send;
    var token;
    var token_value = $('#coin_value').val() != null ? $('#coin_value').val() : 0;
    if ($('#coin').hasClass('active')) {
        token = 'coin';
    } else {
        token = 'rp';
    }
    $.ajax({
        async: false,
        url: "/coin/dev?ctype=" + token + "&reqCoin=" + token_value + "&replySeq=" + $('#reply_seq').val(),
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (result) {
            if (result.code == 511 || result.code == 512) {
                coin_send = true;
            } else {
                coin_send = false;
            }
            alert(result.message);
        }, error: function (e) {
        }
    });
    if (coin_send) {
        $('#coin_send_btn').attr('data-dismiss', 'modal');
        location.reload();
    }
    return coin_send;
}