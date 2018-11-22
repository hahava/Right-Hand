$(document).ready(function () {
    var req = getParameterByName('userdata');
    $('#myPage').attr('class', 'active');
    if (req === 'editInfo' || req === 'editPw') {
        /* 회원정보 또는 비밀번호 수정 시, 비밀번호 인증 요청 화면으로 변경 */
        setPwRequestView($('#user_info_list').attr('id'), req);
    } else {
        setUserInfoTable($('#user_info_list').attr('id'));
    }
});


function setUserInfoTable(tag_id) {
    var tag = '#' + tag_id;
    $(tag).replaceWith(' <div class="row" id="' + tag_id + '">\n' +
        '        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 has-margin-top">\n' +
        '            <img data-src="holder.js/200x200" class="img-rounded" alt="200x200"\n' +
        '                 src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iI2VlZSIvPjx0ZXh0IHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjEwMCIgeT0iMTAwIiBzdHlsZT0iZmlsbDojYWFhO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zaXplOjEzcHg7Zm9udC1mYW1pbHk6QXJpYWwsSGVsdmV0aWNhLHNhbnMtc2VyaWY7ZG9taW5hbnQtYmFzZWxpbmU6Y2VudHJhbCI+MjAweDIwMDwvdGV4dD48L3N2Zz4="\n' +
        '                 style="width: 200px; height: 200px;">\n' +
        '        </div>\n' +
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
        '                    <td>닉네임</td>\n' +
        '                    <td><span class="pull-left" id="user_nickname"></span></td>\n' +
        '                </tr>\n' +
        '                <tr>\n' +
        '                    <td>성별</td>\n' +
        '                    <td><span class="pull-left" id="user_gender"></span></td>\n' +
        '                </tr>\n' +
        '                <tr>\n' +
        '                    <td>전화번호</td>\n' +
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
    getUserInfo();
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
        '                        <input type="password" class="form-control" id="userPwd"/><br/>\n' +
        '                        <button class="btn btn-primary btn-sm pull-right" onclick="checkPwDup(\'' + next_page + '\')">확인</button>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>'
    );
}

function getUserInfo() {
    console.log("getUserInfo()");
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/profile",
        dataType: 'json',
        async: false,
        success: function (result) {
            console.log(result.code);
            if (result.code == 0) {
                var data = result.data;
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
            } else {
                alert("로그인을 해주세요");
                location.href = "/";
            }

        }, error: function () {

        }
    });
}


function checkPwDup(next_page) {
    var userPwd = $('#userPwd').val();
    var data = {"userPwd": userPwd};
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
                        setUserInfoTable($('#user_info_list').attr('id'));
                        var user_tel = $('#user_tel').text();
                        var user_nickname = $('#user_nickname').text();
                        /* 수정가능한 항목들을 input 태그로 변경 */
                        $('#user_tel').replaceWith('<input class="pull-left form-control input-sm" id="user_tel" value="' + user_tel + '"/>');
                        $('#user_nickname').replaceWith('<input class="pull-left form-control input-sm" id="user_nickname" value="' + user_nickname + '"/>');
                        $('#container').append('<br><button class="btn btn-primary pull-right" onclick="reqModifiedUserInfo()">수정</button>');
                        break;
                    case 'editPw':
                        setUserInfoTable($('#user_info_list').attr('id'));
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

                    /* 패스워드가 필요한 부분이기에 default 없음 */
                }
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
                alert(data.message);
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