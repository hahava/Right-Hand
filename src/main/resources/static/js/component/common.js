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
function session_checker() {
    var authorityLevel;
    $.ajax({
        type: 'POST',
        /* 비동기를 꺼야 변수로 사용이 가능하다. */
        async: false,
        url: "/api/membership/check/live/session",
        success: function (result) {
            if (result.code == 0) {
                authorityLevel = result.data.authorityLevel;
            } else if (result.code == 101) {
                authorityLevel = 101;
            }
        },
        error: function (e) {
        }
    });
    return authorityLevel;
}