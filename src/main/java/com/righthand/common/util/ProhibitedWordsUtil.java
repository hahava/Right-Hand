package com.righthand.common.util;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Created by MJ on 2018. 8. 20..
 */
public class ProhibitedWordsUtil {
    public static final String DELIMITERS = " ,;/'\"\n\r.~!@#$%^&*()+|-=`[]";

    /*
    * 금칙어 체크
    * String str = checkFilterWord(filterWordList, contents);
    * */
    public static String checkFilterWord(List<String> _filterWordList, String _contents) {
        if (_contents == null) return null;
        _contents = _contents.toLowerCase();

        StringTokenizer st = new StringTokenizer(_contents, DELIMITERS, false);

        // 단어를 추출한다.
        while (st.hasMoreElements()) {
            String str = st.nextToken();

            if (str == null || "".equals(str))
                continue;

            if (_filterWordList.stream().filter(a -> a.equals(str)).collect(Collectors.toList()).size() > 0)

                return str;
        }
        return null;
    }
}
