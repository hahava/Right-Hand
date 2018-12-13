function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}


function setSubpageParam(title, info) {

    $('#page_title').text(title);
    $('#page_info').text(info);

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
        case "userActivity":
            board_title = "MyPage";
            board_info = "RH파워 획득 내역";
    }
    setSubpageParam(board_title, board_info);
}

$(window).on("resize", resizeFooterTag);

function resizeFooterTag() {
    if ($("html").height() > window.innerHeight) {
        $("#footer").css("position", "relative");
        $("#footer").css("bottom", "0px");
    } else {
        $("#footer").css("position", "absolute");
        $("#footer").css("bottom", "0px");
    }
}