/*
* 요청된 주소에서 get 파라미터를 분리하는 함수
* 순서나 위치에 상관없이 현재 페이지의 a=b 형태의 단어를 분리한다.
*    */
function getParameterByName(name) {
    url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}


/* 현재 로그인 여부 확인 기능 */
function sessionChecker() {
    var data;
    $.ajax({
        type: 'POST',
        /* 비동기를 꺼야 변수로 사용이 가능하다. */
        async: false,
        url: "/api/membership/check/live/session",
        success: function (result) {
            data = result;
        },
        error: function (e) {
        }
    });
    return data;
}

/*
* input 태그 중 text에 있는 값을 전송 할 때, 엔터 키를 체크하는 메서드
* @param elem 호출한 태그 객체 정보
* @param event 호출시 엔터키를 체크 하는 이벤트
* */
function checkEnter(elem, event) {
    var j_id = elem.id;
    // 13은 엔터키
    if (event.keyCode == 13) {
        switch (j_id) {
            case 'board_search' :
                getSearchResults();
                break;
            case 'userPwd':
                $('#checkPwDup').trigger('click');
                break;
        }
    }
}

/*
* 상단 네비게이션를 페이지 요청에 따라 active class로 변경하는 함수
* */
function setNavActive(type) {
    var id;
    switch (type) {
        case 'dev':
            id = '#dev_story_nav';
            break;
        case 'tech':
            id = '#it_story_nav';
            break;
        case 'notice':
            id = '#notice_nav';
            break;
        case 'userInfo':
            id = '#myPage';
        // 로그인 안된 상태를 제외하곤 전부 존재하기에 default는 사용하지 않는다.
    }
    $(id).attr('class', 'active');
}


function setSubPageText(type) {
    var board_title;
    var board_info;
    switch (type) {
        case 'dev':
            board_title = "Dev. Story";
            board_info = "개발 중 어려운 점이 있다면, 언제든지 무엇이든 질문하세요! 만약, 원하는 답변을 찾았다면 RHT로 고마움을 표현하세요.";
            break;
        case 'tech':
            board_title = "IT. Story";
            board_info = "공유하고싶은 지식과 정보를 작성해보세요. 또한, 소개하고 싶은 IT 제품에 대한 글을 남겨보세요.";
            break;
        case 'notice':
            board_title = "공지사항";
            board_info = "공지사항 입니다.";
            break;
        case "userInfo":
            board_title = "MyPage";
            board_info = "회원정보";
            break;
        case "userToken":
            board_title = "MyPage";
            board_info = "토큰사용내역";
            break;
        case "userActivity":
            board_title = "MyPage";
            board_info = "RH파워 획득 내역";
            break;
        case "userBoard":
            board_title = "MyPage";
            board_info = "작성글 내역";
            break;
    }
    setSubpageParam(board_title, board_info);

    /*
    * subpage의 제목과 정보를 설정하는 함수
    * */
    function setSubpageParam(title, info) {
        $('#page_title').text(title);
        $('#page_info').text(info);
    }
}

$(window).on("resize", resizeFooterTag);

// 화면 변경에 상관없이 동적으로 footer 태그를 하단에 고정하는 함수
function resizeFooterTag() {
    if ($("html").height() > window.innerHeight) {
        $("#footer").css("position", "relative");
        $("#footer").css("bottom", "0px");
    } else {
        $("#footer").css("position", "absolute");
        $("#footer").css("bottom", "0px");
    }
}