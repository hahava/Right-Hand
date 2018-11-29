function setBoardList(data, list_type) {
    var searchedWord = data.searchedWord;
    var board_list = data.data;
    for (var i = 0; i < board_list.length; i++) {

        var boardSeq = board_list[i].BOARD_SEQ;
        var title = board_list[i].BOARD_TITLE;
        var content = board_list[i].BOARD_CONTENT;
        var first_image = content.match(/\<img[^\<]*?(data=todos)*[^\<]\/\>/i) != null ? content.match(/\<img[^\<]*?(data=todos)*[^\<]\/\>/i) : ' ';
        first_image = first_image[0];
        if (first_image.length < 5) {
            first_image = '<img class="img-responsive center-block" src="https://via.placeholder.com/160">';
        }

        $('#tempEditor').empty();
        var editor = new tui.Editor.factory({
            el: document.getElementById('tempEditor'),
            initialEditType: 'wysiwyg',
            previewStyle: 'vertical',
            height: '100px',
            viewer: true,
            hideModeSwitch: true,
            initialValue: content
        });
        content = $('#tempEditor').text();
        // content = regex_content(content);
        if (searchedWord != null) {
            content = highlight(searchedWord, content);
            title = highlight(searchedWord, title);
        }
        var nick_name = board_list[i].NICK_NAME;
        var type = board_list[i].BOARD_TYPE;
        var date = board_list[i].BOARD_DATE.substring(0, 10);

        if (content.length > 340) {
            content = content.substr(0, 340) + "...(중략)...";
        }
        var params = {
            "boardSeq": boardSeq, "type": type, "searchedWord": searchedWord
        };
        var address = '/board/' + list_type + '?';
        address = address + set_address(params);


        $('#board_list').append('  <div class="row has-margin-bottom">' +
            '<div class="col-md-12 col-sm-12">' + '<div class="col-md-2 col-sm-2 title_image">' + first_image + '</div>' +
            '<div class="col-md-8 col-sm-8 bulletin" style="text-overflow: ellipsis;  overflow : hidden;">' +
            '<a href=' + address + '><h4 class="media-heading" id="title">' + title + ' </h4></a>' +
            '<p>' + date + ' <a href="#" class="link-reverse">' + nick_name + '</a></p>' +
            '<p>' + content + '</p></div></div>');

        $('#board_list').css('min-height', '300px');

    }

    $('.title_image').children('img').attr('class', 'img-responsive center-block');

}

function set_address(params_) {
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

/*정규식*/
function regex_content(text) {
    var regex_text = text.replace(/\<img[^\<]*?(data=todos)*[^\<]\/\>/gi, '(사진)');
    regex_text = regex_text.replace(/\#|\*|~|_|-|>/gi, '');
    return regex_text;
};

// 검색결과 하이라팅 기능
function highlight(replace_word, original_word) {
    var reg = new RegExp(replace_word, 'gi');
    var final_str = original_word.replace(reg, function (str) {
        return '<span  style="background:yellow">' + str + '</span>'
    });
    return final_str;
};
