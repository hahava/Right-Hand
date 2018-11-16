function register_user() {

    var check = input_checker;
    var register_success = false;
    if (null_checker() && check) {

        var member = {
            "birthYear": $('#register_birth').val(),
            "gender": ($('input[name="gender"]:checked').val() == '남자') ? 'M' : 'F',
            "nickName": $('#register_nickname').val(),
            "tel": $('#register_phone').val(),
            "userId": $('#register_id').val(),
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
var input_checker = false;

// 아이디 체크
$('#register_id').focusout(function () {

    input_checker = id_checker();

    function id_checker() {
        // 공백 또는 빈칸 체크
        if ($('#register_id').val().length == 0 || $('#register_id').val() == '') {
            console.log("// 공백 또는 빈칸 체크")
            $('#register_id_hint').text("아이디를 입력해주세요!");
            $('#register_id_hint').css('color', 'red');
            return false;
        } else {
            $('#register_id_hint').text("이메일 형식으로 입력해주세요");
            $('#register_id_hint').css('color', 'black');
        }
        // 패턴 체크, 첫 글자는 숫자 불가
        var pattern = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
        if ($('#register_id').val().search(pattern) == -1) {
            console.log(" 패턴 체크, 첫 글자는 숫자 불가");
            $('#register_id_hint').text("옳바른 이메일 형식이 아닙니다!");
            $('#register_id_hint').css('color', 'red');
            return false;
        }

        $('#register_id_hint').text("이메일이 입력되었습니다.V");
        $('#register_id_hint').css('color', 'green');
        return true;
    }
});

// 닉네임 체크
$('#register_nickname').focusout(function () {

    input_checker = nickname_checker();

    function nickname_checker() {
        if ($('#register_nickname').val().length == 0 || $('#register_nickname').val() == '') {
            console.log("// 공백 또는 빈칸 체크")
            $('#register_nickname_hint').text("닉네임을 입력해주세요");
            $('#register_nickname_hint').css('color', 'red');
            return false;
        }

        $('#register_nickname_hint').text("닉네임이 입력되었습니다.V");
        $('#register_nickname_hint').css('color', 'green');
        return true;
    }
});

//비밀번호 체크
$('#register_pw').focusout(function () {

    input_checker = password_checker();

    function password_checker() {
        var regex = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;

        // 암호 길이
        if ($('#register_pw').val().length < 8 || $('#register_pw').val().length > 16) {
            $('#register_pw_hint').text("비빌번호는 8자 이상 15자 이하로 해주세요!");
            $('#register_pw_hint').css('color', 'red');
            return false;
        } else {
            $('#register_pw_hint').text(' ');
        }

        //암호 형식
        if ($('#register_pw').val().search(regex) == -1) {
            $('#register_pw_hint').text("비빌번호는 대소문자와 특수문자를 포함해야 합니다!");
            $('#register_pw_hint').css('color', 'red');
            return false;
        }

        $('#register_pw_hint').text(' ');
        return true;
    };
});

$('#register_pw_check').focusout(function () {

    input_checker = pw_double_checker();

    function pw_double_checker() {

        if ($('#register_pw').val().length < 8 || $('#register_pw').val().length > 16) {
            $('#register_pw_hint').text("비빌번호는 8자 이상 15자 이하로 해주세요!");
            $('#register_pw_hint').css('color', 'red');
            return false;
        }
        if ($('#register_pw_check').val() != $('#register_pw').val()) {
            $('#register_pw_hint').text("비밀번호가 일치 하지 않습니다.");
            $('#register_pw_hint').css('color', 'red');
            return false;
        }

        $('#register_pw_hint').text("비밀번호를 입력하였습니다.");
        $('#register_pw_hint').css('color', 'green');
        return true;
    }
});

// 핸드폰 번호 확인
$('#register_phone').on('keyup', function (event) {


    if (event.keyCode == 46 || event.keyCode == 8) {
        $('#register_phone_hint').text("전화번호를 입력하세요.");
        $('#register_phone_hint').css("color", "gray");
        input_checker = false;
        return false;
    }

    if ($('#register_phone').val().match(/[^0-9\-]/)) {
        $('#register_phone_hint').text("숫자만 입력가능합니다.");
        $('#register_phone_hint').css("color", "red");
        input_checker = false;
        return false;
    } else {
        $('#register_phone_hint').text("전화번호를 입력하세요.");
        $('#register_phone_hint').css("color", "gray");
        input_checker = false;
    }

    if ($('#register_phone').val().length == 3 || $('#register_phone').val().length == 8) {
        $('#register_phone').val($('#register_phone').val() + '-');
    } else if ($('#register_phone').val().length == 13) {
        if (!$('#register_phone').val().match(/[0-9]{3}-[0-9]{4}-[0-9]{4}/)) {
            $('#register_phone_hint').text("전화번호 양식이 옳바르지 않습니다.");
            $('#register_phone_hint').css("color", "red");
        } else {
            input_checker = true;
            $('#register_phone_hint').text("전화번호 입력되었습니다.");
            $('#register_phone_hint').css("color", "green");
        }
    }
});

$('#register_phone').on("focusout", function () {
    if (!$('#register_phone').val().length == 13) {
        console.log("focusout!")
        $('#register_phone_hint').text("전화번호를 입력해주세요");
        $('#register_phone_hint').css("color", "red");
        input_checker = false;
    } else if ($('#register_phone').val().length == 0) {
        input_checker = false;
    }
});

//출생년도
$('#register_birth').focusout(function () {

    input_checker = user_tel_checker();

    function user_tel_checker() {
        if ($('#register_birth').val().length != 4) {
            $('#register_birth_hint').text("출생년도는 4자리입니다.!");
            $('#register_birth_hint').css('color', 'red');
            return false;
        }

        if ($('#register_birth').val() <= 1900 || $('#register_birth').val() >= 2018) {
            $('#register_birth_hint').text("출생년도가 잘못되었습니다!");
            $('#register_birth_hint').css('color', 'red');
            return false;
        }

        $('#register_birth_hint').text("출생년도를 입력하였습니다.!");
        $('#register_birth_hint').css('color', 'green');
        return true;
    }
});

function null_checker() {
    console.log("널체커")
    if ($('#register_id').val().length == 0) {
        $('#register_id_hint').focus();
        $('#register_id_hint').text("아이디를 입력해주세요!");
        $('#register_id_hint').css('color', 'red');
        return false;
    } else if ($('#register_nickname').val().length == 0) {
        $('#register_nickname_hint').focus();
        $('#register_nickname_hint').text("닉네임을 입력해주세요");
        $('#register_nickname_hint').css('color', 'red');
        return false;
    } else if ($('#register_pw_check').val().length == 0) {
        $('#register_pw').focus();
        $('#register_pw_hint').text("비빌번호를 입력해주세요");
        $('#register_pw_hint').css('color', 'red');
        return false;
    } else if ($('#register_phone').val().length == 0) {
        console.log('여기ㅏ여디');
        $('#register_phone').focus();
        $('#register_phone_hint').text("전화번호를 입력해주세요");
        $('#register_phone_hint').css('color', 'red');
        return false;
    } else if ($('#register_birth').val().length == 0) {
        $('#register_birth').focus();
        $('#register_birth_hint').text("출생년도를 입력해주세요");
        $('#register_birth_hint').css('color', 'red');
        return false;
    } else {
        return true;
    }
}