// 기본은 dev 게시판
var type = getParameterByName('type') != null ? getParameterByName('type') : 'dev';

// 현재 페이지 정보
var page = getParameterByName('page');

// 검색 키워드
var keyword = getParameterByName('searchedWord');

$(document).ready(function () {
    setNavActive(type);
    setSubPageText(type);
    searchResult();
    resizeFooterTag();
});


function searchResult() {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/list/searched/" + type + "?searchedWord=" + keyword + "&page=" + page,
        dataType: 'json',
        async: false,
        success: function (result) {

            console.log(result);

            // 게시판 리스트를 초기화 한다..
            $('#blog_list').empty();

            var data = result.data;
            var total = data.total;

            // 페이지 리스트를 초기화 한다.
            $('#pageNation').empty();

            var addr = {"default": "/board/search", "type": type, "searchedWord": keyword};
            set_page(data, page, addr, 5);

            $('#keyword_info').text(keyword);
            $('#total_info').text(total);


            setBoardList(data, 'content');

            window.scrollTo(0, 0);
        }, error: function () {
        }
    });

}