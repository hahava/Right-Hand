$(document).ready(function () {

    var type = getParameterByName('type') != null ? getParameterByName('type') : 'dev';
    var page = getParameterByName('page');
    var keyword = getParameterByName('searchedWord');
    setNavActive(type);
    setSubPageText(type);
    searchResult(type, page, keyword);
    resizeFooterTag();
});


function searchResult(type, page, keyword) {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/list/searched/" + type + "?searchedWord=" + keyword + "&page=" + page,
        dataType: 'json',
        async: false,
        success: function (result) {


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