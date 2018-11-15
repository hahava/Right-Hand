/*
*   2회 이상 사용되는 함수를 분리합니다.
*   지역 변수의 사용을 최소화 하고 파라미터를 넘기는 방식으로  코딩합니다.
* */

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}


