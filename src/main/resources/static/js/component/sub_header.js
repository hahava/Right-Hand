var set_sub_page_header = function (title, info) {

    var board_title = title;
    var board_info = info;

    var tag = '<div class="subpage-head has-margin-bottom">\n' +
        '    <div class="container">\n' +
        '        <h3 id="board_title">' + board_title + '</h3>\n' +
        '        <p id="board_info" class="lead">' + board_info + '</p>\n' +
        '    </div>\n' +
        '</div>'

    return tag;
};