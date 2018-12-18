$(document).ready(function () {

    var title;

    switch (type) {
        case 'dev':
            $('#dev_story_nav').attr('class', 'active');
            title = 'DEV.Story';
            break;
        case 'tech':
            $('#it_story_nav').attr('class', 'active');
            title = 'TECH.Story';
            break;
        case 'notice':
            $('#notice_nav').attr('class', 'active');
            title = 'notice';
            break;
    }
    setSubPageText(type);
    /*초기화*/
    $('#writer_title').attr('value', '');

    imageFinder();
    resizeFooterTag();

});

// 기본은 dev 게시판
var type = getParameterByName('type') != null ? getParameterByName('type') : 'dev';

function write_content() {

    var board_title = $('#writer_title').val();
    var board_content = editor.getValue();

    board_title = tagRemover(board_title);

    var data = {"boardTitle": board_title, "boardContent": board_content};


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
                case 321:
                    alert("등록되었습니다.");
                    writer_success = true;
                    break;
                default:
                    alert("등록 실패 입니다.");
                    writer_success = false;

            }
        }, error: function (e) {
            alert("등록 실패입니다.");
        }
    });


    //성공시 이전 게시글 목록으로 리다이렉트 한다.
    if (writer_success) {
        location.replace("http://localhost:8080/board/list?type=" + type);
    }

}

var editor = new tui.Editor({
    el: document.querySelector('#editSection'),
    initialEditType: 'wysiwyg',
    previewStyle: 'vertical',
    height: '600px',
    hideModeSwitch: true
});

// <,> 등의 태그를 &lt, &gt 변환
function tagRemover(tag) {
    var text = tag;
    text = text.replace(/\</g, "&lt");
    text = text.replace(/\>/g, "&gt");
    return text;
}

/*
* 에디터에서 동적으로 객체를 제어하는 기능
* 생성된 이미지 태그의 클래스를 변경한다.
*
* */
function imageFinder() {
    var mutationObserver = new MutationObserver(function (mutations) {
        mutations.forEach(function (mutation) {
            if (mutation.addedNodes[0] != null) {
                var tag = mutation.addedNodes[0];
                if (tag.toString() == '[object HTMLImageElement]') {
                    $('.tui-editor-contents').find('img').attr('class', 'img-responsive');
                }
            }
        });
    });
    mutationObserver.observe(document.documentElement, {
        attributes: true,
        characterData: true,
        childList: true,
        subtree: true,
        attributeOldValue: true,
        characterDataOldValue: true
    });
}