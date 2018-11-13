// 요청 주소 뒤에 get 파라미터를 매핑한다.
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

    // 해당 페이지 게시글 요청
    req_page(type, page);
});
/*현재 페이지 정보*/

// 기본 dev 게시판 요청
var type = getParameterByName('type') != null ? getParameterByName('type') : 'dev';

// 기본 1 페이지 요청
var page = getParameterByName('page') != null ? getParameterByName('page') : 1;


// 사용자가 원하는 페이지를 요청 번호에 맞춰 반환한다.
function req_page(requested_type, requested_page) {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/board/list/" + requested_type + "?page=" + requested_page,
        dataType: 'json',
        success: function (result) {

            // 로그인 여부와 종류를 확인한다.
            var login_authority = result.data.authority;

            switch (login_authority) {
                case 1:
                    login_user();
                    break;
                default :
            }

            // 게시판 리스트를 초기화 한다..
            $('#blog_list').empty();

            var data = result.data;
            var board_list = data.data;
            var total = data.total;

            // 페이지 리스트를 초기화 한다.
            $('#pageNation').empty();
            set_page(total, requested_page, requested_type);

            for (var i = 0; i < board_list.length; i++) {

                var seq = board_list[i].BOARD_SEQ;
                var title = board_list[i].BOARD_TITLE;
                var content = board_list[i].BOARD_CONTENT;
                content = regex_content(content);
                var nick_name = board_list[i].NICK_NAME;
                var date = board_list[i].BOARD_DATE.substring(0, 10);

                $('#blog_list').append('  <div class="row has-margin-bottom">' +
                    '<div class="col-md-4 col-sm-4">' +
                    '<img class="img-responsive center-block"  th:src="@{../images/blog-thumb-1.jpg}" alt="bulletin blog"src="images/blog-thumb-1.jpg"> </div>' +
                    '<div class="col-md-8 col-sm-8 bulletin">' +
                    '<a href="/board/content?boardSeq=' + seq + '&type=' + requested_type + ' "><h4 class="media-heading" id="title">' + title + ' </h4></a>' +
                    '<p>' + date + ' <a href="#" class="link-reverse">' + nick_name + '</a></p>' +
                    '<p>' + content + '</p></div></div>');
            }
            window.scrollTo(0, 0);
        },
        error: function () {
            //TODO 에러 페이지 제작 해야 함
            alert("존재하지 않는 페이지 입니다.");
        }
    });

}


<!--검색하기 기능-->
function search_result() {
    var keyword = $('#search_text').val();
    location.href = "/board/search?type=" + type + "&page=1&searchedWord=" + keyword;
}

/*글작성*/
function board_writer() {
    location.href = "/board/writer?type=" + type;
}