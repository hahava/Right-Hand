// 기본은 dev 게시판
var type = getParameterByName('type') != null ? getParameterByName('type') : 'dev';

// 현재 페이지 정보
var page = getParameterByName('page');

// 검색 키워드
var keyword = getParameterByName('searchedWord');

$(document).ready(function () {

    var board_title;
    var board_info;

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
            $('#it_story_nav').attr('class', 'active');
            board_title = "공지사항";
            board_info = "공지사항";
            break;
    }
    setSubpageParam(board_title, board_info);
    search_result();
});


function search_result() {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/list/searched/" + type + "?searchedWord=" + keyword + "&page=" + page,
        dataType: 'json',
        success: function (result) {


            // 게시판 리스트를 초기화 한다..
            $('#blog_list').empty();

            var data = result.data;
            var total = data.total;

            // 페이지 리스트를 초기화 한다.
            $('#pageNation').empty();
            search_page(total, page, type, keyword);

            $('#keyword_info').text(keyword);
            $('#total_info').text(total);


            get_board_list(data, 'content');

            window.scrollTo(0, 0);
        }, error: function () {
        }
    });

}