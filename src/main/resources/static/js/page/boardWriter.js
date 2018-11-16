$(document).ready(function () {

    switch (type) {
        case 'dev':
            $('#dev_story_nav').attr('class', 'active');
            $('#board_title').text("DEV. Story")
            break;
        case 'tech':
            $('#it_story_nav').attr('class', 'active');
            $('#board_title').text("TECH. Story")
            break;
    }

    $('#board_info').text("글작성");
    /*초기화*/
    $('#writer_title').attr('value', '');
});

// 기본은 dev 게시판
var type = getParameterByName('type') != null ? getParameterByName('type') : 'dev';

function write_content() {

    var data = {"boardTitle": $('#writer_title').val(), "boardContent": editor.getValue()};
    var writer_success = false;
    $.ajax({
        type: 'POST',
        async: false,
        url: "http://localhost:8080/board/" + type,
        data: JSON.stringify(data),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (result) {
            switch (result.code) {
                case 100:
                    alert("등록 실패 입니다.");
                    writer_success = false;
                    break;
                case 0:
                    alert("등록되었습니다.");
                    writer_success = true;
                    break;
            }
        }, error: function (e) {
            alert("등록 실패입니다.");
        }
    });


    //성공시 이전 게시글 목록으로 리다이렉트 한다.
    if (writer_success) {
        location.href = "http://localhost:8080/board/list?type=" + type;
    }

}

var editor = new tui.Editor({
    el: document.querySelector('#editSection'),
    initialEditType: 'wysiwyg',
    previewStyle: 'vertical',
    height: '400px',
    hideModeSwitch: true
});
