function registerUser() {

    // 유효성 검사
    var check = input_checker;
    var register_success = false;
    /*
    *  공백 및 유효성을 동시에 검사한다.
    *  focus 이벤트를 체크하기 때문에 동시에 검사 불가능
    */
    if (nullChecker() && check) {

        var member = {
            "birthYear": $('#register_birth').val(),
            "gender": ($('input[name="gender"]:checked').val() == '남자') ? 'M' : 'F',
            "nickName": $('#register_nickname').val(),
            "tel": $('#register_phone').val(),
            "userId": $('#register_id').val(),
            "userName": $('#register_name').val(),
            "userPwd": $('#register_pw').val(),
            "recommender": $('#register_recommender').val()
        };

        console.log(member);
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
                        register_success = false;
                }
            },
            error: function (data) {
            }
        });
        return true;
    }
    return register_success;
};

/* 회원가입 데이터 유효성 검증 */
var input_checker = false;

// 아이디 체크
$('#register_id').focusout(function () {

    input_checker = id_checker();

    function id_checker() {
        // 공백 또는 빈칸 체크
        if ($('#register_id').val().length == 0 || $('#register_id').val() == '') {
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
            $('#register_id_hint').text("옳바른 이메일 형식이 아닙니다!");
            $('#register_id_hint').css('color', 'red');
            return false;
        }

        if (hasSameId($('#register_id').val())) {
            $('#register_id_hint').text("중복된 아이디입니다.!");
            $('#register_id_hint').css('color', 'red');
            return false;
        }

        $('#register_id_hint').text("이메일이 입력되었습니다.V");
        $('#register_id_hint').css('color', 'green');
        return true;
    }
});

function hasSameId(register_id) {

    var data = {"userId": register_id};
    var isSameId = false;
    $.ajax({
        type: 'POST',
        url: "/api/membership/check/id/dup",
        async: false,
        data: JSON.stringify(data),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            if (data.code == 202) {
                isSameId = true;
            } else {
                isSameId = false;
            }
        }, error: function (e) {
        }
    });
    return isSameId;
}

// 닉네임 체크
$('#register_nickname').focusout(function () {

    input_checker = nickname_checker();

    function nickname_checker() {
        if ($('#register_nickname').val().length == 0 || $('#register_nickname').val() == '') {
            $('#register_nickname_hint').text("닉네임을 입력해주세요");
            $('#register_nickname_hint').css('color', 'red');
            return false;
        }

        if (hasSameNickname($('#register_nickname').val())) {
            $('#register_nickname_hint').text("중복된 닉네임입니다.");
            $('#register_nickname_hint').css('color', 'red');
            return false;
        }

        $('#register_nickname_hint').text("닉네임이 입력되었습니다.V");
        $('#register_nickname_hint').css('color', 'green');
        return true;
    }
});

function hasSameNickname(register_nickname) {

    var data = {"nickName": register_nickname};
    var isSameNickname = false;
    $.ajax({
        type: 'POST',
        url: "/api/membership/check/nick/dup",
        async: false,
        data: JSON.stringify(data),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            if (data.code == 312) {
                isSameNickname = true;
            } else {
                isSameNickname = false;
            }
        }, error: function (e) {
        }
    });
    return isSameNickname;
}

// 이름 체크
$('#register_name').focusout(function () {
    input_checker = name_checker();

    function name_checker() {
        if ($('#register_name').val().length == 0) {
            $('#register_name_hint').text("이름을 입력해주세요");
            $('#register_name_hint').css('color', 'red');
            return false;
        } else {
            $('#register_name_hint').text("이름이 입력되었습니다.");
            $('#register_name_hint').css('color', 'green');
            return true;
        }
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
$('#register_phone').focusout(function () {

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

    if ($('#register_phone').val().length == 11) {
        if (!$('#register_phone').val().match(/[0-9]{11}/)) {
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
        $('#register_phone_hint').text("전화번호를 입력해주세요");
        $('#register_phone_hint').css("color", "red");
        input_checker = false;
    } else if ($('#register_phone').val().length == 0) {
        input_checker = false;
    }
});

// 출생년도
$('#register_birth').focusout(function () {

    input_checker = userBirthChecker();

    function userBirthChecker() {
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
// 추천인
$('#register_recommender').on("focusout", function () {


    input_checker = recommenderChecker();

    function recommenderChecker() {

        if ($('#register_recommender').val().length == 0) {
            $('#register_recommender_hint').text('추천인이 있다면, 입력해주세요.');
            $('#register_recommender_hint').css('color', 'black');
            return true;
        } else {
            if (!hasSameId($('#register_recommender').val())) {
                $('#register_recommender_hint').text('추천인이 존재하지 않습니다.');
                $('#register_recommender_hint').css('color', 'red');
                return false;
            }
        }

    }
})

function nullChecker() {
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
    } else if ($('#register_name').val().length == 0) {
        $('#register_name_hint').text("이름을 입력해주세요");
        $('#register_name_hint').css('color', 'red');
        return false;
    } else if ($('#register_phone').val().length == 0) {
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

// input 태그에서 숫자만 허용하게 하는 함수
function onlyNumber(event) {
    event = event || window.event;
    var keyID = (event.which) ? event.which : event.keyCode;
    // 숫자 외에 Enter, BackSpace, Tab 등의 키는 허용한다.
    if ((keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105) || keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 || keyID == 9) {
        return;

    } else {
        return false;
    }
}

// 영어가 아닌 한국어등을 제거하는 함수
function removeChar(event) {
    event = event || window.event;
    var keyID = (event.which) ? event.which : event.keyCode;
    if (keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39) {
        return;
    }
    else {
        event.target.value = event.target.value.replace(/[^0-9]/g, "");
    }
}

// 모달이 켜질 때, 엔터키 입력 시 submit 버튼을 클릭하게 됨
$(document).keypress(function (e) {
    if ($('#registerModal').hasClass('in') && (e.keycode == 13 || e.which == 13)) {
        $('#register_submit').click();
    }
})