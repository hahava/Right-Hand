$(document).ready(function () {


    var board_title;
    var board_info;
    var session = session_checker();
    switch (type) {
        case 'dev':
            $('#dev_story_nav').attr('class', 'active');
            board_title = "Dev. Story";
            board_info = "개발 중 어려운 점이 있다면, 언제든지 무엇이든 질문하세요! 만약, 원하는 답변을 찾았다면 RHT로 고마움을 표현하세요.";
            break;
        case 'tech':
            $('#it_story_nav').attr('class', 'active');
            board_title = "IT. Story";
            board_info = "공유하고싶은 지식과 정보를 작성해보세요. 또한, 소개하고 싶은 IT 제품에 대한 글을 남겨보세요.";
            break;
        case 'notice':
            $('#notice_nav').attr('class', 'active');
            board_title = "공지사항";
            board_info = "공지사항 입니다.";
            break;
    }

    setSubpageParam(board_title, board_info);


    // 해당 페이지 게시글 요청
    req_page(type, page);
    if ((session == 101 || session == 0) && type == 'notice') {
        $('#board_writer').remove();
    }
});
// 검색하기 기능 엔터키 입력 가능
$(document).keypress(function (e) {
    if ($('#search_input').focusin() && e.keycode == 13 || e.which == 13) {
        getSearchResults();
    }
});
/*현재 페이지 정보*/

var type = getParameterByName('type');

// 기본 1 페이지 요청
var page = getParameterByName('page') != null ? getParameterByName('page') : 1;


// 사용자가 원하는 페이지를 요청 번호에 맞춰 반환
function req_page(requested_type, requested_page) {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/list/" + requested_type + "?page=" + requested_page,
        dataType: 'json',
        success: function (result) {


            // 게시판 리스트를 초기화 한다..
            $('#blog_list').empty();

            // 페이지 리스트를 초기화 한다.
            $('#pageNation').empty();

            var data = result.data;
            var total = data.total;
            set_page(total, requested_page, requested_type);

            get_board_list(data, 'content');
            window.scrollTo(0, 0);
        },
        error: function () {
        }
    });

}

/* 검색하기 기능 */
function getSearchResults() {
    var keyword = $('#search_text').val();
    if (keyword.length == 0) {
        alert("빈칸으로 검색하실 수 없습니다.");
        return false;
    }
    location.href = "/board/search?type=" + type + "&page=1&searchedWord=" + keyword;
}

/* 글작성 */
function getBoardWriterPage() {
    var session = session_checker();
    if (session == 1 || session == 103) {
        location.href = "/board/writer?type=" + type;
    } else {
        alert("로그인 후 이용해 주세요!");
    }
}
