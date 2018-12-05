$(document).ready(function () {

    setNavActive("userInfo");
    setSubPageText("userInfo");

    var req = getParameterByName('userdata');

    if (req === 'editInfo' || req === 'editPw' || req === 'userDel') {
        /* 회원정보 또는 비밀번호 수정 시, 비밀번호 인증 요청 화면으로 변경 */
        setPwRequestView($('#user_info_list').attr('id'), req);
    } else {
        $('#btn_userInfoEdit').css('display', 'inline');
        $('#btn_userPwEdit').css('display', 'inline');
        $('#btn_userDel').css('display', 'inline');

        setUserInfoTableView($('#user_info_list').attr('id'));
        var userInfoData = getUserInfo();
        setUserInfoTableData(userInfoData);

    }

});

function setProfilePhoto(elem) {
    var j_id = '#' + elem.id;
    $(j_id).css('opacity', '0.3');
}

function setProfilePhotoOrigin(elem) {
    var j_id = '#' + elem.id;
    $(j_id).css('opacity', '1');
}

function setUserInfoTableView(tag_id) {
    var tag = '#' + tag_id;
    $(tag).replaceWith(' <div class="row" id="' + tag_id + '">\n' +
        '        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 has-margin-top">' +
        '<div class="outer">\n' +
        '     <img data-src="holder.js/200x200" class="img-responsive image" alt="200x200" id="user_profile_photo" \n' +
        '                 src="https://via.placeholder.com/200"  style="width: 200px; height: 200px;"> \n' +
        '  </div></div>\n' +
        '        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">\n' +
        '            <table class="table">\n' +
        '                <thead>\n' +
        '                <th>항목</th>\n' +
        '                <th>내용</th>\n' +
        '                </thead>\n' +
        '                <tbody>\n' +
        '                <tr>\n' +
        '                    <td>아이디</td>\n' +
        '                    <td><span class="pull-left" id="user_id"></span></td>\n' +
        '                </tr>\n' +
        '                <tr>\n' +
        '                    <td>이름</td>\n' +
        '                    <td><span class="pull-left" id="user_name"></span></td>\n' +
        '                </tr>\n' +
        '                <tr>\n' +
        '                    <td style="vertical-align: middle">닉네임</td>\n' +
        '                    <td><span class="pull-left" id="user_nickname"></span></td>\n' +
        '                </tr>\n' +
        '                <tr>\n' +
        '                    <td>성별</td>\n' +
        '                    <td><span class="pull-left" id="user_gender"></span></td>\n' +
        '                </tr>\n' +
        '                <tr>\n' +
        '                    <td style="vertical-align: middle">전화번호</td>\n' +
        '                    <td><span class="pull-left" id="user_tel"></span></td>\n' +
        '                </tr>\n' +
        '                <tr>\n' +
        '                    <td>출생년도</td>\n' +
        '                    <td><span class="pull-left" id="user_birth"></span></td>\n' +
        '                </tr>\n' +
        '                </tbody>\n' +
        '            </table>\n' +
        '        </div>\n' +
        '    </div>');
}

function setPwRequestView(tag_id, next_page) {
    var tag = '#' + tag_id;
    $(tag).replaceWith('    <div class="row" id="' + tag_id + '">\n' +
        '        <div class="col-sm-4 col-sm-offset-4">\n' +
        '            <div class="panel panel-default">\n' +
        '                <div class="panel-heading">\n' +
        '                    <h3 class="panel-title">암호를 입력해주세요</h3>\n' +
        '                </div>\n' +
        '                <div class="panel-body">\n' +
        '                    <div class="form-group">\n' +
        '                        <input type="password" class="form-control" id="userPwd" onkeypress="checkEnter(this,event)"/><br/>\n' +
        '                        <button class="btn btn-primary btn-sm pull-right"id="checkPwDup" onclick="checkPwDup(\'' + next_page + '\')">확인</button>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>'
    );
}

function getUserInfo() {
    var userInfoData;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/profile",
        dataType: 'json',
        async: false,
        success: function (result) {
            console.log(result.code);
            if (result.code == 0) {
                userInfoData = result.data;
            } else {
                alert("로그인을 해주세요");
                location.href = "/";
            }
        }, error: function () {
        }
    });
    return userInfoData;
}

function setUserInfoTableData(data) {
    var gender = data.gender;
    var birthYear = data.birthYear;
    var nickName = data.nickname;
    var tel = data.tel;
    var userName = data.userName;
    var userId = data.userId;

    $('#user_gender').text(gender);
    $('#user_birth').text(birthYear);
    $('#user_name').text(userName);
    $('#user_nickname').text(nickName);
    $('#user_id').text(userId);
    $('#user_tel').text(tel);
}

function checkPwDup(next_page) {
    var userPwd = $('#userPwd').val();
    var data = {"userPwd": userPwd};
    if (userPwd == null) {
        alert("빈칸으로 입력할 수 없습니다.");
        return;
    }
    $.ajax({
        type: 'POST',
        url: "/api/membership/check/pwd/dup",
        async: false,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }, data: JSON.stringify(data),
        success: function (result) {
            if (result.code === 0) {
                switch (next_page) {
                    case 'editInfo':
                        setUserInfoTableView($('#user_info_list').attr('id'));
                        var userInfoData = getUserInfo();
                        setUserInfoTableData(userInfoData);
                        $('.outer').append(' <div class="middle"> <input class="form-control "  id="profile_image" type="file" name="image" onchange="encodeImagetoBase64(this)"/></div><div class="text-center"><button class="btn btn-default btn-sm" style="margin-top: 5px" onclick="resetProfile()">초기화\n' +
                            '</button></div>');
                        var user_tel = $('#user_tel').text();
                        var user_nickname = $('#user_nickname').text();
                        /* 수정가능한 항목들을 input 태그로 변경 */
                        $('#user_tel').replaceWith('<input class="pull-left form-control input-sm" id="user_tel" value="' + user_tel + '"/>');
                        $('#user_nickname').replaceWith('<input class="pull-left form-control input-sm" id="user_nickname" value="' + user_nickname + '"/>');
                        $('#container').append('<br><button class="btn btn-primary pull-right" onclick="reqModifiedUserInfo()">수정</button>');
                        break;
                    case 'editPw':
                        setUserInfoTableView($('#user_info_list').attr('id'));
                        var userInfoData = getUserInfo();
                        setUserInfoTableData(userInfoData);
                        $('#user_info_list').append(
                            '<form class="form-horizontal">' +
                            '<div class="form-group">' +
                            '<label for="pw_first" class="col-xs-offset-4 col-lg-offset-4 col-lg-2 col-xs-2">비밀번호</label>' +
                            '<div class="col-xs-4 col-lg-4">' +
                            '<input type="password" class="form-control input-sm" id="pw_first"/>' +
                            '</div>' +
                            '</div>' +
                            '<div class="form-group">' +
                            '<label for="pw_first" class="col-lg-offset-4 col-xs-offset-4 col-lg-2 col-xs-2">비밀번호 확인</label>' +
                            '<div class="col-xs-4 col-lg-4">' +
                            '<input type="password" class="form-control input-sm" id="pw_second"/>' +
                            '</div>' +
                            '</div>' +
                            '</form>');
                        $('#container').append('<br><button class="btn btn-primary pull-right" onclick="reqModifiedUserPw()">수정</button>');
                        break;

                    case 'userDel':
                        setUserInfoTableView($('#user_info_list').attr('id'));
                        var userInfoData = getUserInfo();
                        setUserInfoTableData(userInfoData);
                        var html = '<div class="row"><div class="col-xs-10 col-lg-10 col-sm-10 col-md-10"><div class="form-group">\n' +
                            '  <label for="comment">탈퇴사유:</label>\n' +
                            '  <textarea class="form-control" rows="5" id="comment" ></textarea>\n' +
                            '<button class="btn btn-primary pull-right has-margin-top" onclick="resign()">제출</button></div></div></div>';
                        $('#container').append(html);
                    /* 패스워드가 필요한 부분이기에 default 없음 */
                }
            } else {
                alert("암호가 틀렸습니다.");
            }
        }, error: function (e) {
        }
    });
}

function resetProfile() {
    $('#profile_image').val('');
    $.ajax({
        url: '/img/profile',
        type: 'DELETE',
        async: false,
        success: function (result) {
            $('#user_profile_photo').attr('src', 'https://via.placeholder.com/200');
            console.log("성공");
        }, error: function (e) {
        }
    });
}

function encodeImagetoBase64(element) {
    console.log("encodeImagetoBase64")
    var file = element.files[0];
    var reader = new FileReader();
    reader.onloadend = function () {
        $("#user_profile_photo").attr("src", reader.result);
    };
    reader.readAsDataURL(file);

    var fd = new FormData();
    fd.append('img', file);


    $.ajax({
        type: 'PUT',
        url: '/img/profile',
        data: fd,
        processData: false,
        contentType: false,
        success: function (result) {
            if (result.code == 0) {
                console.log("파일변경됨")
            }
        }, error: function (e) {

        }
    });
}

function resign() {
    data = {'reason': $('#comment').val()};
    if (data.length <= 1) {
        alert('사유를 입력해주세요');
        return
    }
    $.ajax({
        type: 'PUT',
        url: '/api/membership/resign',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(data),
        async: false,
        success: function (result) {
            if (result.code == 0) {
                alert("탈퇴되었습니다.")
                location.href = "/";
            }
        }, error: function (e) {

        }
    });
}

// 유저의 정보를 변경
function reqModifiedUserInfo() {
    var user_tel = $('#user_tel').val();
    var user_nickname = $('#user_nickname').val();
    var data = {"nickname": user_nickname, "tel": user_tel};

    $.ajax({
        type: 'PUT',
        url: "http://localhost:8080/profile",
        async: false,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        /*반드시 JSON 변환을 거쳐서 요청 보낼 것*/
        data: JSON.stringify(data),
        success: function (result) {
            if (result.code === 0) {
                alert("수정되었습니다.");
                location.href = "/user/info";
            }   
            else {
                alert(data.code);
            }
        }, error: function (e) {
        }
    });
}

function reqModifiedUserPw() {
    var pw_first = $('#pw_first').val();
    var pw_second = $('#pw_second').val();
    var data = {"newPwd": pw_first, "newPwdDup": pw_second};

    $.ajax({
        type: 'PUT',
        url: "http://localhost:8080/pwd",
        async: false,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        /*반드시 JSON 변환을 거쳐서 요청 보낼 것*/
        data: JSON.stringify(data),
        success: function (result) {
            console.log(result.code);
            console.log(result.message);
            alert("수정되었습니다.");
            location.href = "/user/info";
        }, error: function (e) {
        }
    });
}

/* TODO : user정보 객체 */
function UserInfo() {

}