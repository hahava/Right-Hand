/* 로그인 성공 여부 확인하여 모달 이벤트를 제어하는 변수 */
var loginOk = false;

function login() {

    var user_login_email = $('#user_login_email').val();
    var user_login_pw = $('#user_login_pw').val();

    // 아이디 또는 패스워드를 입력하지 않은 경우
    if (user_login_email.length == 0 || user_login_pw.length == 0) {
        alert("항목을 전부 채워주세요");
        return;
    }
    $.ajax({
        type: 'POST',
        url: "/api/login",
        /* 비동기로 처리 시 리다이렉트 정상적으로 실행 안될 수 있음 */
        async: false,
        data: {
            userId: user_login_email,
            userPwd: user_login_pw
        },
        /* spring security를 사용했기 때문에 success는 반드시 로그인 성공을 의미한다.*/
        success: function (data) {
            alert("로그인 되었습니다. 반갑습니다.");
            loginOk = true;
            /* 로그인 성공 시 모달 종료 */
            $('#login_submit').attr('data-dismiss', 'modal');
            location.reload();
            loginUser();
        },
        error: function () {
            alert("id 또는 password 가 틀렸습니다.");
            loginOk = false;
        }
    });
    return loginOk;
}

function logout() {
    var session = sessionChecker();
    var userSeq = session.data.userSeq;
    $.ajax({
        type: 'GET',
        url: "/api/logout?userSeq=" + userSeq,
        success: function (data) {
            alert("logout 되었습니다.");
            /* 로그아웃 성공후 페이지 새로고침 */
            location.reload();
        },
        error: function () {
            alert("잘못된 접근입니다.")
        }
    });
}

// 모달이 켜질 때, 엔터키를 이벤트에 따란 submit 버튼을 클릭하게 됨
$(document).keypress(function (e) {
    // 로그인
    if ($("#loginModal").hasClass('in') && (e.keycode == 13 || e.which == 13)) {
        $('#login_submit').click();
    }
});


function findUserId() {
    if ($('#find_user_id').val().length == 0) {
        alert("이메일을 입력해주세요");
        return;
    }
    alert("메일이 전송되었습니다!");
    $.ajax({
        type: "GET",
        url: "/api/membership/email?userId=" + $('#find_user_id').val(),
        success: function (data) {
            /* 반드시 성공 응답코드가 날아온다. */
        }, error: function (e) {
        }
    });
}


function findUserPw() {
    if ($('#find_user_pw').val().length == 0) {
        alert("이메일을 입력해주세요");
        return;
    }
    alert("메일이 전송되었습니다.!");
    $.ajax({
        type: "GET",
        url: "/api/membership/tempPwd?userId=" + $('#find_user_pw').val(),
        success: function (data) {
            /* 반드시 성공 응답코드가 날아온다. */
        }, error: function (e) {
        }
    })
}