/*
* 게시판을 리스트 형식으로 보여주는 기능
*
* @param data 게시판 데이터
* @param list_type 게시판 형태
*
* */
function setBoardList(data, list_type) {
    var searchedWord = data.searchedWord;
    var board_list = data.data;

    for (var i = 0; i < board_list.length; i++) {

        var board = new Board(board_list[i].BOARD_SEQ, board_list[i].BOARD_TITLE, board_list[i].BOARD_CONTENT,
            board_list[i].NICK_NAME, board_list[i].BOARD_TYPE, board_list[i].BOARD_DATE.substring(0, 10), board_list[i].REPLY_CNT);

        $('#tempEditor').empty();
        /* markdown으로 변경하기 위한 임시 tui-editor */
        var editor = new tui.Editor.factory({
            el: document.getElementById('tempEditor'),
            initialEditType: 'wysiwyg',
            previewStyle: 'vertical',
            height: '100px',
            viewer: true,
            hideModeSwitch: true,
            initialValue: board.content
        });
        board.content = $('#tempEditor').text();
        if (searchedWord != null) {
            board.content = highlight(searchedWord, board.content);
            board.title = highlight(searchedWord, board.title);
        }

        // 글자수가 340자 이상이면 해당내용을 생략한다.
        if (board.content.length > 340) {
            board.content = board.content.substr(0, 340) + "...(중략)...";
        }
        var params = {
            "boardSeq": board.boardSeq, "type": board.type, "searchedWord": searchedWord
        };
        var address = '/board/' + list_type + '?';
        address = address + setAddress(params);
        var visibility = "hidden";
        if (board.type != 'notice') {
            visibility = "inline";
        }
        $('#board_list').append('  <div class="row has-margin-bottom">' +
            '<div class="col-md-12 col-sm-12">' + '<div class="col-md-2 col-sm-2 title_image">' + board.first_image + '</div>' +
            '<div class="col-md-8 col-sm-8 bulletin" style="text-overflow: ellipsis;  overflow : hidden;">' +
            '<a href=' + address + '><h4 class="media-heading" id="title" style="display: inline-block">' + board.title + ' </h4><span class="badge reply_cnt" style="margin-left: 5px; visibility:' + visibility + ';" >' + board.reply_cnt + '</span></a>' +
            '<p>' + board.date + ' <a href="#" class="link-reverse">' + board.nick_name + '</a></p>' +
            '<p>' + board.content + '</p></div></div>');


    }


    $('.title_image').children('img').attr('class', 'img-responsive center-block');
}

// 게시판 객체
function Board(boardSeq, title, content, nick_name, type, date, reply_cnt) {
    this.boardSeq = boardSeq;
    this.title = title;
    this.content = content;
    this.nick_name = nick_name;
    this.type = type;
    this.date = date;
    this.reply_cnt = reply_cnt;
    this.first_image = getFirstImage(content);

    // 업로드한 이미지 중, 대표 이미지를 등록한다.
    function getFirstImage(content) {
        var image = content.match(/\<img[^\<]*?(data=todos)*[^\<]\/\>/i) != null ? content.match(/\<img[^\<]*?(data=todos)*[^\<]\/\>/i) : ' ';
        image = image[0];
        if (image.length < 5) {
            image = '<img class="img-responsive center-block" src="https://via.placeholder.com/160">';
        }
        return image;
    }
}


function setAddress(params_) {
    var href = "";
    for (var key in params_) {
        if (typeof params_[key] !== "undefined") {
            if (href.length < 1) {
                href = href + key + "=" + params_[key];
            } else {
                href = href + "&" + key + "=" + params_[key];
            }
        }
    }
    return href;
}

/*
* 검색결과 하이라이팅 기능
*
* @param replace_word 하이라이팅하고자 하는 키워드
* @param original_word 변경하고자 하는 문장
*
* */
function highlight(replace_word, original_word) {
    var reg = new RegExp(replace_word, 'gi');
    var final_str = original_word.replace(reg, function (str) {
        return '<span  style="background:yellow">' + str + '</span>'
    });
    return final_str;
}
