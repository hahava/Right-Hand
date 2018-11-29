$(document).ready(function () {


    var session = sessionChecker();
    var authorityLevel = session.data.authorityLevel;

    setNavActive(type);
    setSubPageText(type);

    // 해당 페이지 게시글 요청
    req_page(page);
    if ((authorityLevel == 101 || authorityLevel == 0) && type == 'notice') {
        $('#board_writer').remove();
    }
});


/*현재 페이지 정보*/

var type = getParameterByName('type');

// 기본 1 페이지 요청
var page = getParameterByName('page') != null ? getParameterByName('page') : 1;


// 사용자가 원하는 페이지를 요청 번호에 맞춰 반환
function req_page(requested_page) {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/list/" + type + "?page=" + requested_page,
        dataType: 'json',
        success: function (result) {


            // 게시판 리스트를 초기화 한다..
            $('#blog_list').empty();

            // 페이지 리스트를 초기화 한다.
            $('#pageNation').empty();

            var data = result.data;
            var total = data.total;
            setBoardList(data, 'content');

            var addr = {"default": "/board/list", "type": type};
            set_page(data, page, addr);
            window.scrollTo(0, 0);
        },
        error: function () {
        }
    });

}

/* 검색하기 기능 */
function getSearchResults() {
    var keyword = $('#board_search').val();
    if (keyword.length == 0) {
        alert("빈칸으로 검색하실 수 없습니다.");
        return false;
    }
    location.href = "/board/search?type=" + type + "&page=1&searchedWord=" + keyword;
}

/* 글작성 */
function getBoardWriterPage() {
    var session = sessionChecker();
    var authorityLevel = session.data.authorityLevel;
    if (authorityLevel == 1 || authorityLevel == 103) {
        location.href = "/board/writer?type=" + type;
    } else {
        alert("로그인 후 이용해 주세요!");
    }
}
