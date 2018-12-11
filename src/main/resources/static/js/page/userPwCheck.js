$(document).ready(function () {
    setNavActive('userInfo');
    getUserInfo();
    resizeFooterTag();

});

function getUserInfo() {
    $.ajax({
        type: "GET",
        url: "/profile",
        dataType: 'json',
        success: function (result) {

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

