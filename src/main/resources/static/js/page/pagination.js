/*
*  stand를 수정하면 페이지당 보여지는 데이터의 개수 수정이 가능하다.
*  num은 페이지 숫자 개수 자체이다.
*
* */
function set_page(data, requested_page, addr, page_count) {

    var address = setPageAddress(addr);
    var total = data.total;

    var num = 5;
    var stand = page_count;
    var totalPage = (total % stand == 0) ? (total / stand) : (parseInt(total / stand) + 1);
    var startNum = parseInt((requested_page - 1) / num) * num + 1;
    var endNum;
    if (startNum + (num - 1) >= totalPage) {
        endNum = totalPage;
    } else {
        endNum = startNum + (num - 1);
    }
    console.log(startNum + "\t" + endNum);

    var prevArrow = ' <li id="prevArrow" ><a href="#" id="prevArrowAnc">«</a></li> ';
    var nextArrow = ' <li id="nextArrow" ><a href="#" id="nextArrowAnc">»</a></li>';
    var li_tag = "";

    data = data.data;
    for (var i = startNum, j = 0; i <= endNum; i++, j++) {
        //요청 페이지와 현재 페이지가 같다면 'active' 클래스를 입력한다.
        if (i == requested_page) {
            li_tag = li_tag + "<li class='active'><a >" + i + "</a></li>";
        } else {
            li_tag = li_tag + "<li><a href=" + address + i + ">" + i + "</a></li>";
        }
    }
    $('#pageNation').append(prevArrow + li_tag + nextArrow);
    //5 이하일 경우 prev 버튼 클릭 금지
    if (endNum <= num) {
        $('#prevArrow').attr('class', 'disabled');
    } else {
        //5 이상일 경우 이전 버튼 활성화
        var prevCount = startNum - num;
        $('#prevArrowAnc').attr('href', address + prevCount);
    }

    // 마지막 페이지와 전체 페이지가 같으면 다음 버튼 비활성화
    if (endNum == totalPage) {
        $('#nextArrow').attr('class', 'disabled');
    } else {
        // 다음 버튼 활성화
        var nextCount = startNum + num;
        $('#nextArrowAnc').attr('href', address + nextCount);
    }
}


function setPageAddress(params_) {
    var href = "";
    for (var key in params_) {
        if (key == 'default') {
            href = params_[key] + '?';
            continue;
        }
        if (typeof params_[key] !== "undefined") {
            if (href.substr(href.length - 1, href.length) == '?') {
                href = href + key + "=" + params_[key];
            } else {
                href = href + "&" + key + "=" + params_[key];
            }
        }
    }
    return href + "&page=";
}