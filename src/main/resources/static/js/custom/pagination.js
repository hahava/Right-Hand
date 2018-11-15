function set_page(total, requested_page, requested_type) {
    /*
    *   totalpage= 전체 페이지 개수
    *   전체 데이터가 41 개 라면, 가능한 페이지는 9
    *   전체 데이터가 40 개 라면, 가능한 페이지는 8 이다.
    *   stand를 수정하면 페이지 개수 수정이 가능하다.
    * */
    var stand = 5;
    var totalPage = (total % 5 == 0) ? (total / 5) : (parseInt(total / 5) + 1);
    var startNum = parseInt((requested_page - 1) / stand) * stand + 1;
    var endNum;
    if (startNum + (stand - 1) >= totalPage) {
        endNum = totalPage;
    } else {
        endNum = startNum + (stand - 1);
    }

    var prevArrow = ' <li id="prevArrow" ><a href="#" id="prevArrowAnc">«</a></li> ';
    var nextArrow = ' <li id="nextArrow" ><a href="#" id="nextArrowAnc">»</a></li>';
    var li_tag = "";

    for (var i = startNum; i <= endNum; i++) {
        //요청 페이지와 현재 페이지가 같다면 'active' 클래스를 입력한다.
        if (i == requested_page) {
            li_tag = li_tag + "<li class='active'><a >" + i + "</a></li>";
        } else {
            li_tag = li_tag + "<li><a href='/board/list?type=" + requested_type + "&page=" + i + "'>" + i + "</a></li>";
            // li_tag = li_tag + "<li><a onclick= 'req_page(" + i + ")'>" + i + "</a></li>";
        }
    }
    $('#pageNation').append(prevArrow + li_tag + nextArrow);
    //5 이하일 경우 prev 버튼 클릭 금지
    if (endNum <= stand) {
        $('#prevArrow').attr('class', 'disabled');
    } else {
        //5 이상일 경우 이전 버튼 활성화
        var prevCount = startNum - stand;
        $('#prevArrowAnc').attr('onclick', 'req_page(' + prevCount + ')');
    }

    // 마지막 페이지와 전체 페이지가 같으면 다음 버튼 비활성화
    if (endNum == totalPage) {
        $('#nextArrow').attr('class', 'disabled');
    } else {
        // 다음 버튼 활성화
        var nextCount = startNum + stand;
        $('#nextArrowAnc').attr('href', '/board/list?type=' + requested_type + '&page=' + nextCount);
    }
}

function search_page(total, requested_page, requested_type, searchedWord) {
    /*
    *   totalpage= 전체 페이지 개수
    *   전체 데이터가 41 개 라면, 가능한 페이지는 9
    *   전체 데이터가 40 개 라면, 가능한 페이지는 8 이다.
    *   stand를 수정하면 페이지 개수 수정이 가능하다.
    * */
    var stand = 5;
    var totalPage = (total % 5 == 0) ? (total / 5) : (parseInt(total / 5) + 1);
    var startNum = parseInt((requested_page - 1) / stand) * stand + 1;
    var endNum;
    if (startNum + (stand - 1) >= totalPage) {
        endNum = totalPage;
    } else {
        endNum = startNum + (stand - 1);
    }

    var prevArrow = ' <li id="prevArrow" ><a href="#" id="prevArrowAnc">«</a></li> ';
    var nextArrow = ' <li id="nextArrow" ><a href="#" id="nextArrowAnc">»</a></li>';
    var li_tag = "";

    for (var i = startNum; i <= endNum; i++) {
        //요청 페이지와 현재 페이지가 같다면 'active' 클래스를 입력한다.
        if (i == requested_page) {
            li_tag = li_tag + "<li class='active'><a >" + i + "</a></li>";
        } else {
            li_tag = li_tag + "<li><a href='/board/search?type=" + requested_type + "&page=" + i + "&searchedWord=" + searchedWord + "'>" + i + "</a></li>";
        }
    }
    $('#pageNation').append(prevArrow + li_tag + nextArrow);
    //5 이하일 경우 prev 버튼 클릭 금지
    if (endNum <= stand) {
        $('#prevArrow').attr('class', 'disabled');
    } else {
        //5 이상일 경우 이전 버튼 활성화
        var prevCount = startNum - stand;
        $('#prevArrowAnc').attr('onclick', 'req_page(' + prevCount + ')');
    }

    // 마지막 페이지와 전체 페이지가 같으면 다음 버튼 비활성화
    if (endNum == totalPage) {
        $('#nextArrow').attr('class', 'disabled');
    } else {
        // 다음 버튼 활성화
        var nextCount = startNum + stand;
        $('#nextArrowAnc').attr('href', '/board/search?type=' + requested_type + '&page=' + nextCount + "&searchedWord=" + searchedWord);
    }
}