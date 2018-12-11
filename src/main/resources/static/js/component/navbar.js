function loginChecker() {
    var user_nickname;
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/api/membership/coin',
        async: false,
        success: function (result) {
            var auth = result.code;
            switch (auth) {
                case 0:
                    data = {"rhCoin": result.data.rhCoin, "rewardPower": result.data.rewardPower};
                    setLoginUserNavbar(data);
                    break;
                default:
                    notLogin();
            }
        }, error: function (e) {

        }
    });
}

function setLoginUserNavbar(data) {

    $('#modalOpener').remove();
    $('#registerOpener').remove();

    $('#navbar').append('<li><a href="#none">Coin <span class="badge">' + data.rhCoin + '</span></a></li>' +
        '<li><a href="#none">Rp <span class="badge">' + data.rewardPower + '</span></a></li>');

    $('#navbar').append('<li class="dropdown" id="myPage"><a href="#none" class="dropdown-toggle" data-toggle="dropdown">MyPage <span class="caret"></span></a>  ' +
        '<ul class="dropdown-menu dropdown-menu-left" role="menu">\n' +
        '<li><a href="/user/info">회원정보</a></li>\n' +
        '<li><a href="/user/token">토큰내역</a></li>\n' +
        '<li><a href="/user/activity">활동내역</a></li>\n' +
        '<li><a href="/user/board">작성글</a></li>\n' +
        '<li class="divider"></li>\n' +
        '<li><a href="javascript:logout();">LogOut</a></li>\n' +
        '</ul></li>');


}

function notLogin() {
    $('#navbar').append('<li id="login_li"><a href="#" data-toggle="modal" data-target="#loginModal" id="modalOpener">Login</a>\n' +
        '                </li>\n' +
        '                <li id="register_li"><a href="#" data-toggle="modal" data-target="#registerModal" id="registerOpener">Register</a>\n' +
        '                </li>');
}