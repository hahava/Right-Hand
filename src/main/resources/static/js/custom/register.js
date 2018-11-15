function register_user() {
    var pw = $('#register_pw').val();
    var check_pw = $('#register_pw_check').val();
    var check = password_checker(pw, check_pw);
    var register_success = false;


    if (check) {
        var member = {
            "birthYear": $('#register_birth').val(),
            "email": $('#register_email').val(),
            "gender": ($('input[name="gender"]:checked').val() == '남자') ? 'M' : 'F',
            "nickName": $('#register_nickname').val(),
            "tel": $('#register_phone').val(),
            "userId": $('#register_email').val(),
            "userName": $('#register_name').val(),
            "userPwd": $('#register_pw').val()
        };

        $.ajax({
            type: 'POST',
            url: "http://localhost:8080/api/membership/signUp",
            async: false,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            /*반드시 JSON 변환을 거쳐서 요청 보낼 것*/
            data: JSON.stringify(member),
            success: function (data) {
                switch (data.code) {

                    case 0 :
                        alert("성공입니다! 로그인 해 주세요")
                        loginOk = true;
                        /*모달 속성 변경 후 Reload*/
                        $('#register_submit').attr('data-dismiss', 'modal');
                        register_success = true;
                        window.reload();
                        return true;
                        break;

                    default:
                        alert("실패했습니다." + data.message + data.code);

                }

            },
            error: function (data) {
                alert(data.toString());
                register_success = false;
                return false;
            }
        });
        return true;
    }
    return register_success;
};

/*회원가입 데이터 유효성 검증 */
var input_checker = function () {
    if ($('#register_email').val().length == 0 || $('#register_email').val() == "") {
        $('#register_email').focus();
        return false;
    }
};

var password_checker = function (pw, check_pw) {

    var regex = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;


    // 암호 길이
    if (pw.length < 8 || pw.length > 16) {
        alert("비빌번호는 8자 이상 15자 이하로 해주세요!");
        return false;
    }

    //암호 형식
    if (!regex.test(pw)) {
        alert("비빌번호는 대소문자와 특수문자를 포함해야 합니다.");
        return false;
    }

    //암호 2번 입력
    if (pw != check_pw) {
        alert("비밀번호가 일치 하지 않습니다.");
        $('#register_pw_check').focus();
        return false;
    }
    return true;
};