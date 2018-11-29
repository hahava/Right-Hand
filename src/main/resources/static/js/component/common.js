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

