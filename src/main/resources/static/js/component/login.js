/* 로그인 성공 여부 확인 */
var loginOk = false;

/* 비동기 로그인 기능 */
function login() {
    $.ajax({
        type: 'POST',
        url: "http://localhost:8080/api/login",
        /* 비동기로 처리 시 리다이렉트 정상적으로 실행 안될 수 있음*/
        async: false,
        data: {
            userId: $('#user_login_email').val(),
            userPwd: $('#user_login_pw').val()
        },
        success: function (data) {
            loginOk = true;
            /* 로그인 성공 시 모달 종료 */
            $('#submitBtn').attr('data-dismiss', 'modal');
            login_user();
        },
        error: function () {
            loginOk = false;
            alert("id 또는 password 가 틀렸습니다.");
        }
    });
    return loginOk;
}

function logout() {
    $.ajax({
        type: 'GET',
        url: "/api/logout",
        success: function (data) {
            alert("logout 되었습니다.");
            /* 로그아웃 성공후 페이지 새로고침 */
            location.reload();
        },
        error: function () {
        }
    });
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