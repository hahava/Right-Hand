$(document).ready(function () {

    /*현재 페이지 정보*/
    var type = getParameterByName('type');
    // 기본 1 페이지 요청
    var page = getParameterByName('page') != null ? getParameterByName('page') : 1;

    var session = sessionChecker();
    var authorityLevel = session.data.authorityLevel;

    setNavActive(type);
    setSubPageText(type);
    // 공지사항은 관리자 권한만 글 작성 버튼이 보인다.
    if ((authorityLevel == 1 || authorityLevel == 0) && type == 'notice') {
        $('#board_writer_btn').remove();
    }
    getBoardList(page, type);

    resizeFooterTag();
});


// 사용자가 원하는 게시판 페이지를 요청 번호에 맞춰 반환
function getBoardList(requested_page, board_type) {
    $.ajax({
        type: 'GET',
        url: "/board/list/" + board_type + "?page=" + requested_page,
        dataType: 'json',
        async: false,
        success: function (result) {


            // 게시판 리스트를 초기화 한다.
            $('#blog_list').empty();

            // 페이지 리스트를 초기화 한다.
            $('#pageNation').empty();
            var data = result.data;
            setBoardList(data, 'content');

            var addr = {"default": "/board/list", "type": board_type};
            set_page(data, requested_page, addr, 5);
            window.scrollTo(0, 0);
        },
        error: function () {
        }
    });
}

/* 검색 기능 */
function getSearchResults() {
    var keyword = $('#board_search').val();
    var type = getParameterByName('type');
    if (keyword.length == 0) {
        alert("빈칸으로 검색하실 수 없습니다.");
        return false;
    }
    location.href = "/board/search?type=" + type + "&page=1&searchedWord=" + keyword;
}

/* 글작성 */
function getBoardWriterPage() {
    var session = sessionChecker();
    var type = getParameterByName('type');
    var authorityLevel = session.data.authorityLevel;
    if (authorityLevel == 1 || authorityLevel == 113) {
        location.href = "/board/writer?type=" + type;
    } else {
        alert("로그인 후 이용해 주세요!");
    }
}
