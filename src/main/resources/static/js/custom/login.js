<!--로그인 스크립트-->
/*로그인 성공 여부 확인*/
var loginOk = false;

/*비동기 로그인 기능*/
function login() {
    $.ajax({
        type: 'POST',
        url: "http://localhost:8080/api/login",
        async: false,
        data: {
            userId: $('#user_login_email').val(),
            userPwd: $('#user_login_pw').val()
        },
        success: function (data) {
            alert(data.toString());
            loginOk = true;
            $('#submitBtn').attr('data-dismiss', 'modal');
            login_user();
            return true;
        },
        error: function () {
            loginOk = false;
            alert("id 또는 password 가 틀렸습니다.");
            return false;
        }
    });
    return loginOk;
};

function logout() {
    $.ajax({
        type: 'GET',
        url: "/api/logout",
        success: function (data) {
            alert("logout 되었습니다.");
            location.reload();
        },
        error: function () {
        }
    });
}
