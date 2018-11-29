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

    $('#board_title').text(title);
    $('#board_info').text(info);

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