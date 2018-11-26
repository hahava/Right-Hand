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

// 모달이 켜질 때, 엔터키를 이벤트에 따란 submit 버튼을 클릭하게 됨
var captureEnterKey = $(document).keypress(function (e) {
    // 로그인
    if ($("#loginModal").hasClass('in') && (e.keycode == 13 || e.which == 13)) {
        $('#login_submit').click();
    }
    // 회원가입
    else if ($('#registerModal').hasClass('in') && (e.keycode == 13 || e.which == 13)) {
        $('#register_submit').click();
    }
});