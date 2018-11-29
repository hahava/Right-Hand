$(document).ready(function () {
    var page = getParameterByName('page');
    if (page == null) {
        page = 1;
    }
    $('#myPage').attr('class', 'active');
    getUserActivity(page);
});

function getUserActivity(page) {
    $.ajax({
        type: 'GET',
        url: '/myBoard?page=' + page,
        dataType: 'json',
        async: false,
        success: function (result) {
            var data = result.data;
            var total = data.total;

            setBoardList(data);

            var addr = {"default": "/user/activity"};
            set_page(data, page, addr);
        },
        error: function () {
        }
    })
}