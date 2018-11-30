$(document).ready(function () {
    var page = getParameterByName('page');
    if (page == null) {
        page = 1;
    }
    $('#myPage').attr('class', 'active');
    getUserActivity(page);
});