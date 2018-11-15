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
    }

    $('#board_title').text(board_title);
    $('#board_info').text(board_info);

    search_result();

});

// 검색결과 하이라팅 기능
function highlight(replace_word, original_word) {
    var reg = new RegExp(replace_word, 'gi');
    var final_str = original_word.replace(reg, function (str) {
        return '<span  style="background:yellow">' + str + '</span>'
    });
    return final_str;
}

function search_result() {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/list/searched/" + type + "?searchedWord=" + keyword + "&page=" + page,
        dataType: 'json',
        success: function (result) {


            // 게시판 리스트를 초기화 한다..
            $('#blog_list').empty();

            var data = result.data;
            var board_list = data.data;
            var total = data.total;

            // 페이지 리스트를 초기화 한다.
            $('#pageNation').empty();
            search_page(total, page, type, keyword);

            $('#keyword_info').text(keyword);
            $('#total_info').text(total);


            for (var i = 0; i < board_list.length; i++) {

                var seq = board_list[i].BOARD_SEQ;
                var title = highlight(keyword, board_list[i].BOARD_TITLE);
                var content = board_list[i].BOARD_CONTENT;
                content = regex_content(content);
                content = highlight(keyword, content);
                var nick_name = board_list[i].NICK_NAME;
                var date = board_list[i].BOARD_DATE.substring(0, 10);

                $('#blog_list').append('  <div class="row has-margin-bottom">' +
                    '<div class="col-md-4 col-sm-4">' +
                    '<img class="img-responsive center-block"  th:src="@{../images/blog-thumb-1.jpg}" alt="bulletin blog"src="images/blog-thumb-1.jpg"> </div>' +
                    '<div class="col-md-8 col-sm-8 bulletin">' +
                    '<a href="/board/content?boardSeq=' + seq + '&type=' + type + ' "><h4 class="media-heading" id="title">' + title + ' </h4></a>' +
                    '<p>' + date + ' <a href="#" class="link-reverse">' + nick_name + '</a></p>' +
                    '<p>' + content + '</p></div></div>');
            }
            window.scrollTo(0, 0);
        }, error: function () {
            //    TODO : 에러 페이지 제작해야 함
        }
    });
}