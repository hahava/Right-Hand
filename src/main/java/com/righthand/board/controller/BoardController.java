package com.righthand.board.controller;

import com.righthand.board.dao.BoardDao;
import com.righthand.board.dto.model.BoardSearchVO;
import com.righthand.board.dto.req.BoardReq;
import com.righthand.board.dto.req.ReplyReq;
import com.righthand.board.service.BoardService;
import com.righthand.common.BASE64DecodedMultipartFile;
import com.righthand.common.board.BoardChecker;
import com.righthand.common.GetClientProfile;
import com.righthand.common.dto.res.ResponseHandler;
import com.righthand.common.type.ReturnType;

import io.swagger.annotations.Api;
import org.springframework.validation.BindingResult;

import com.righthand.common.util.ConvertUtil;
import com.righthand.file.service.FileService;
import com.righthand.membership.service.MembershipInfo;
import com.righthand.membership.service.MembershipService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    MembershipService membershipService;

    @Autowired
    FileService fileService;

    @Autowired
    BoardDao boardDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String bucketUrl = "https://s3.ap-northeast-2.amazonaws.com/right-hand-dev";

    private boolean checkBoardType(String bType) {
        if (bType.equalsIgnoreCase("tech"))
            return true;
        else if (bType.equalsIgnoreCase("dev"))
            return true;
        else
            return false;
    }

    private boolean checkCoinType(String cType) {
        if (cType.equalsIgnoreCase("rp"))
            return true;
        else if (cType.equalsIgnoreCase("coin"))
            return true;
        else
            return false;
    }

    private String storeImgsAndGetChangedText(Map<String, Object> params) {
        final String regex = "\\!\\[.*?\\)";
        final String[] srcTag = {"<img class=\"img-responsive\" src=\"", "\" data=todos/>"};
        String text = (String) params.get("boardContent");
        log.info("text : {}", text);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        ArrayList<String> imgTexts = new ArrayList<>();
        int total = 0;
        while (matcher.find()) {
            imgTexts.add(matcher.group());
            total++;
        }

        /*
         * 복사 붙여넣기 - BASE64가 URL(http, https)의 형식으로 넘어오는 이미지
         * */
        for (int i = 0; i < total; i++) {
            StringTokenizer stringTokenizer = new StringTokenizer(imgTexts.get(i), "(");
            stringTokenizer.nextToken();
            String secondText = stringTokenizer.nextToken();
            String isHttps = secondText.substring(0, 8);
            String isHttp = secondText.substring(0, 7);
            if (isHttps.equals("https://") || isHttp.equals("http://")) {
                String imgUrl = secondText.replace(")", "");
                text = text.replace(imgTexts.get(i), srcTag[0] + imgUrl + srcTag[1]);
                imgTexts.remove(i);
                total--;
            }
        }


        for (int i = 0; i < total; i++) {
            StringTokenizer firstSt = new StringTokenizer(imgTexts.get(i), ",");
            firstSt.nextToken();
            String transformText = firstSt.nextToken();
            StringBuilder base64 = new StringBuilder();
            for (int j = 0; j < transformText.length() - 1; j++) {
                base64.append(transformText.charAt(j));
            }
            byte[] decodedBytes = Base64.getDecoder().decode(String.valueOf(base64));
            MultipartFile[] files = new MultipartFile[1];

            // Base64 -> Multipart 커스터마이징
            BASE64DecodedMultipartFile base64DecodedMultipartFile = new BASE64DecodedMultipartFile(decodedBytes);

            files[0] = base64DecodedMultipartFile;
            Map<String, Object> param = new HashMap<>();
            ArrayList<HashMap<String, Object>> urlMap = null;
            try {
                urlMap = fileService.storeFile(files, param);
                /**
                 * Right-Hand-Imgs가 resource Root 디렉토리이기 때문에 subFileUrl 적용
                 */
                String newText = srcTag[0] + bucketUrl + (String) urlMap.get(0).get("subFileUrl") + srcTag[1];
//                String newText = srcTag[0] + (String) urlMap.get(0).get("fileUrl") + srcTag[1];
                text = text.replace(imgTexts.get(i), newText);
            } catch (Exception e) {
                log.error("[StoreFile][Exception]" + e.toString());
            }
        }
        return text;
    }

    private double addAllRhCoin(List<Map<String, Object>> replyDetilData) {
        double temp = 0;
        for (int i = 0; i < replyDetilData.size(); i++) {
            temp += (double) replyDetilData.get(i).get("REPLY_RH_COIN");
        }
        return temp;
    }

    private Map<String, Object> getMembershipInfoAndReturnType(MembershipService membershipService) {
        Map<String, Object> temp = new HashMap<>();
        try {
            MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
            if (membershipInfo == null) throw new Exception();
            temp.put("membershipInfo", membershipInfo);
            temp.put("ReturnType", ReturnType.RTN_TYPE_OK);
        } catch (Exception e) {
            logger.error("[Session][Exception] " + e.toString());
            temp.put("membershipInfo", null);
            temp.put("returnType", ReturnType.RTN_TYPE_SESSION);
        }
        return temp;
    }

    @ApiOperation("게시물 리스트")
    @GetMapping("/board/list/{btype}")
    public ResponseHandler<?> showBoardList(@ApiParam(value = "페이지 번호") @RequestParam int page,
                                            @ApiParam(value = "게시판 종류") @PathVariable String btype) {
        final ResponseHandler<Object> result = new ResponseHandler<>();
        if (checkBoardType(btype) == true) {
            Map<String, Object> tempBoardData = new HashMap<>();
            List<Map<String, Object>> tempBoardList;
            // 사용자 정보 가져옴
            Map<String, Object> userInfo = null;
            try {
                userInfo = GetClientProfile.getUserInfo(membershipService);
            } catch (Exception e) {
                log.error("[getUserInfo][Exception]" + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            tempBoardData.put("authority", userInfo.get("authority"));
            tempBoardData.put("nickname", userInfo.get("nickname"));
            if (btype.equals("tech")) {
                try {
                    int total = boardDao.selectCountListTech();
                    tempBoardData.put("total", total);
                    tempBoardList = boardService.selectBoardListTech(page);
                    tempBoardData.put("data", tempBoardList);
                    result.setData(tempBoardData);
                    if (total == 0) {
                        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NG);
                        return result;
                    }
                    if (tempBoardList == null || tempBoardList.isEmpty()) {
                        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                        return result;
                    }
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                } catch (Exception e) {
                    logger.error("[ShowDevBoardList][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }

            } else if (btype.equals("dev")) {
                try {
                    int total = boardDao.selectCountListDev();
                    tempBoardData.put("total", total);
                    tempBoardList = boardService.selectBoardListDev(page);
                    tempBoardData.put("data", tempBoardList);
                    result.setData(tempBoardData);
                    if (total == 0) {
                        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NG);
                        return result;
                    }
                    if (tempBoardList == null || tempBoardList.isEmpty()) {
                        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                        return result;
                    }
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                } catch (Exception e) {
                    logger.error("[ShowDevBoardList][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            }
        } else {
            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_TYPE_NG);
        }

        return result;
    }

    // 검색어가 null일 경우 400이 return됨
    // 나머지는 ReturnType 참고
    @ApiOperation("게시판 검색")
    @GetMapping("/board/list/searched/{btype}")
    public ResponseHandler<?> searchedBoardList(@ApiParam(value = "검색어") @RequestParam String searchedWord,
                                                @ApiParam(value = "페이지 번호") @RequestParam int page,
                                                @ApiParam(value = "게시판 종류") @PathVariable String btype) {

        final ResponseHandler<Object> result = new ResponseHandler<>();
//        if(searchedWord.equals("") || searchedWord == null)
        if (checkBoardType(btype) == true) {
            Map<String, Object> tempBoardData = new HashMap<>();
            List<Map<String, Object>> tempBoardList;
            // 사용자 정보 가져옴
            Map<String, Object> userInfo = null;
            try {
                userInfo = GetClientProfile.getUserInfo(membershipService);
            } catch (Exception e) {
                log.error("[getUserInfo][Exception]" + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }

            BoardSearchVO vo = new BoardSearchVO();
            vo.setSearchedWord(searchedWord);
            tempBoardData.put("authority", userInfo.get("authority"));
            tempBoardData.put("nickname", userInfo.get("nickname"));
            tempBoardData.put("searchedWord", searchedWord);

            if (btype.equals("tech")) {
                try {
                    tempBoardList = boardService.searchedBoardListTech(searchedWord, page);
                    if (tempBoardList.isEmpty() || tempBoardList == null) {
                        tempBoardData.put("total", 0);
                        tempBoardData.put("data", null);
                        result.setData(tempBoardData);
                        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                        return result;
                    }
                    tempBoardData.put("total", boardDao.selectSearchedCountListTech(vo));
                    tempBoardData.put("data", tempBoardList);
                    result.setData(tempBoardData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                } catch (Exception e) {
                    logger.error("[SearchTechBoardList] [Exception " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            } else if (btype.equals("dev")) {
                try {
                    tempBoardList = boardService.searchedBoardListDev(searchedWord, page);
                    if (tempBoardList.isEmpty() || tempBoardList == null) {
                        tempBoardData.put("data", null);
                        tempBoardData.put("total", 0);
                        result.setData(tempBoardData);
                        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                        return result;
                    }
                    tempBoardData.put("total", boardDao.selectSearchedCountListDev(vo));
                    tempBoardData.put("data", tempBoardList);
                    result.setData(tempBoardData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                } catch (Exception e) {
                    logger.error("[SearchDevBoardList] [Exception " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            }
        } else {
            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_TYPE_NG);
        }
        return result;
    }


    @ApiOperation("글작성")
    @PostMapping("/board/{btype}")
    public ResponseHandler<?> writeBoard(@ApiParam(value = "게시물 제목, 내용") @Valid @RequestBody BoardReq _params, BindingResult bindingResult,
                                         @ApiParam(value = "게시판 종류") @PathVariable String btype
    ) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        if (!bindingResult.hasFieldErrors()) {
            try {
                MembershipInfo membershipInfo = membershipService.currentSessionUserInfo();
                if (membershipInfo == null) {
                    result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                    return result;
                } else {
                    ReturnType returnType = BoardChecker.checkParam(_params);
                    if (returnType.equals(ReturnType.RTN_TYPE_OK)) {
                        Map<String, Object> params = ConvertUtil.convertObjectToMap(_params);
                        String changedText = storeImgsAndGetChangedText(params);
                        params.replace("boardContent", changedText);
                        // 제목에 html태그가 들어가는 것 제거
                        params.replace("boardTitle", ConvertUtil.eliminateHtmlTags((String) params.get("boardTitle")));

                        // 검색용 Column
                        //Tag를 지운 후에 MarkDown 또한 지운다.
                        changedText = ConvertUtil.eliminateHtmlTags(changedText);
                        changedText = ConvertUtil.eliminateMarkdown(changedText);
                        params.put("boardContent4Searching", changedText);
                        if (checkBoardType(btype)) {
                            params.put("boardProfileSeq", membershipInfo.getProfileSeq());
                            ReturnType rtn;
                            if (btype.equals("tech")) {
                                try {
                                    rtn = boardService.insertBoardListTech(params, membershipInfo);
                                    result.setReturnCode(rtn);
                                } catch (Exception e) {
                                    logger.error("[TechBoard][Exception] " + e.toString());
                                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_INSERT_NG);
                                }
                            } else if (btype.equals("dev")) {
                                try {
                                    rtn = boardService.insertBoardListDev(params, membershipInfo);
                                    result.setReturnCode(rtn);
                                } catch (Exception e) {
                                    logger.error("[DevBoard][Exception] " + e.toString());
                                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_INSERT_NG);
                                }
                            }
                        } else {
                            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_TYPE_NG);
                        }
                    } else {
                        result.setReturnCode(returnType);
                    }
                }

            } catch (Exception e) {
                logger.error("[Board][Exception] " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            return result;
        }
        result.setReturnCode(ReturnType.RTN_TYPE_NO_DATA);
        return result;
    }


    @ApiOperation("게시물 상세보기")
    @GetMapping("/board/detail/{btype}")
    public ResponseHandler<?> showBoardDetail(@ApiParam(value = "게시물 번호") @RequestParam int boardSeq, @ApiParam(value = "게시판 종류") @PathVariable String btype) {
        final ResponseHandler<Object> result = new ResponseHandler<>();
        if (checkBoardType(btype)) {
            Map<String, Object> boardDetailData = null;
            List<Map<String, Object>> replyDetilData = null;
            // 사용자 정보 가져옴
            Map<String, Object> userInfo = null;
            try {
                userInfo = GetClientProfile.getUserInfo(membershipService);
            } catch (Exception e) {
                log.error("[getUserInfo][Exception]" + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            }
            Map<String, Object> tempBoardData = new HashMap<>();
            try {

                Integer profileSeq = (Integer) userInfo.get("profileSeq");
                if (btype.equals("tech")) {
                    // 로그인 유저와 작성자가 같은가?
                    int boardProfileSeq = boardService.findProfileSeqByBoardSeqTech(boardSeq);
                    tempBoardData.put("isWriter", true);
                    if (profileSeq == null || (profileSeq != boardProfileSeq)) {
                        tempBoardData.replace("isWriter", false);
                    }

                    boardDetailData = boardService.showBoardDetailTech(boardSeq);
                    replyDetilData = boardService.showReplyBoardTech(boardSeq);
                    double totalRhCoin = addAllRhCoin(replyDetilData);
                    tempBoardData.put("totalRhCoin", totalRhCoin);
                } else if (btype.equals("dev")) {
                    boardDetailData = boardService.showBoardDetailDev(boardSeq);
                    replyDetilData = boardService.showReplyBoardDev(boardSeq);
                    // 로그인 유저와 작성자가 같은가?
                    int boardProfileSeq = boardService.findProfileSeqByBoardSeqDev(boardSeq);
                    tempBoardData.put("isWriter", true);
                    if (profileSeq == null || (profileSeq != boardProfileSeq)) {
                        tempBoardData.replace("isWriter", false);
                    }
                }
                if (!(boardDetailData.isEmpty() || boardDetailData == null)) {
                    tempBoardData.put("authority", userInfo.get("authority"));
                    tempBoardData.put("nickname", userInfo.get("nickname"));
                    tempBoardData.put("data", boardDetailData);
                    if (!replyDetilData.isEmpty() || replyDetilData != null) {
                        tempBoardData.put("replyDetailData", replyDetilData);
                    }
                    result.setData(tempBoardData);
                    result.setReturnCode(ReturnType.RTN_TYPE_OK);
                } else {
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
                }
            } catch (Exception e) {
                logger.error("[BoardDetail] [Exception " + e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
            }
        } else {
            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_TYPE_NG);
        }

        return result;
    }


    @ApiOperation("DEV STORY 댓글달기")
    @PostMapping("/reply/dev")
    public ResponseHandler<?> writeReplyDev(@ApiParam(value = "댓글 내용") @Valid @RequestBody ReplyReq replyReq, BindingResult bindingResult) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        if (!bindingResult.hasErrors()) {
            Map<String, Object> membershipInfoAndReturnType = getMembershipInfoAndReturnType(membershipService);
            if (membershipInfoAndReturnType.get("returnType") == ReturnType.RTN_TYPE_SESSION) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                return result;
            }
            MembershipInfo membershipInfo = (MembershipInfo) membershipInfoAndReturnType.get("membershipInfo");
            Map<String, Object> params = ConvertUtil.convertObjectToMap(replyReq);
            params.put("replyProfileSeq", membershipInfo.getProfileSeq());
            params.put("replyContent", params.get("content"));
            try {
                ReturnType rtn = boardService.insertReplyListDev(params, membershipInfo);
                result.setReturnCode(rtn);
                return result;
            } catch (Exception e) {
                log.error("[InsertReplyListDev][Exception]");
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_REPLY_NG);
                return result;
            }
        }
        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_REPLY_IS_EMPTY_NG);
        return result;
    }


    @ApiOperation("Dev Story 코인 전송")
    @PostMapping("/coin/dev")
    public ResponseHandler<?> sendCoinInDev(@ApiParam(value = "코인 종류") @RequestParam(required = false) String ctype,
                                            @ApiParam(value = "송금 하고자 하는 코인의 양") @RequestParam Double reqCoin,
                                            @ApiParam(value = "댓글 번호") @RequestParam int replySeq) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        ReturnType rtn;
        Map<String, Object> membershipInfoAndReturnType = getMembershipInfoAndReturnType(membershipService);
        if (membershipInfoAndReturnType.get("returnType") == ReturnType.RTN_TYPE_SESSION) {
            result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
            return result;
        }
        MembershipInfo membershipInfo = (MembershipInfo) membershipInfoAndReturnType.get("membershipInfo");

        if (boardDao.findReplyIsRewarded(replySeq) != 0) {
            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_REPLY_IS_REWARDED_NG);
            return result;
        }

        if (checkCoinType(ctype)) {
            // 코인은 0보다 커야 한다.
            if (reqCoin <= 0) {
                log.error("[NotEnoughReqCoin][Error]");
                result.setReturnCode(ReturnType.RTN_TYPE_REQ_COIN_NOT_ENOUGH_NG);
                return result;
            }
            Map<String, Object> params = new HashMap<>();
            //보내고자 하는 코인의 양
            params.put("reqCoin", reqCoin);
            //댓글의 Sequence
            params.put("replySeq", replySeq);
            int boardSeq = boardDao.findBoardSeqByReplySeq(replySeq);
            params.put("boardSeq", boardSeq);
            Map senderInfo;
            try {
                senderInfo = membershipService.getRewardPowerAndCoin(membershipInfo.getProfileSeq());
                if (senderInfo == null) throw new Exception();
                params.put("senderSeq", membershipInfo.getProfileSeq());
            } catch (Exception e) {
                log.error("[GetRewardPowerAndCoin][Exception] : {}", e.toString());
                result.setReturnCode(ReturnType.RTN_TYPE_GET_COIN_POWER_NG);
                return result;
            }
            double rewardPower = (double) senderInfo.get("REWARD_POWER");
            double rhCoin = (double) senderInfo.get("RH_COIN");
            //보유하고 있는 Reward Power가 보내고자 하는 코인보다 작으면
            //Error 발생
            if (ctype.equals("rp")) {
                if (rewardPower < reqCoin) {
                    log.error("[NotEnoughRewardPower][Error]");
                    result.setReturnCode(ReturnType.RTN_TYPE_REWARD_POWER_NOT_ENOUGH_NG);
                    return result;
                }
            }
            //보유하고 있는 RH COIN이 보내고자 하는 코인보다 작으면
            //Error 발생
            else {
                if (rhCoin < reqCoin) {
                    log.error("[NotEnoughCoin][Error]");
                    result.setReturnCode(ReturnType.RTN_TYPE_RH_COIN_NOT_ENOUGH_NG);
                    return result;
                }
            }

            // rewardPower를 보낸다.
            if (ctype.equals("rp")) {
                try {
                    rtn = boardService.sendDevWithRewardPower(params, membershipInfo);
                } catch (Exception e) {
                    logger.error("[sendRewardPowerInDev][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_COIN_SEND_NG);
                    return result;
                }
                result.setReturnCode(rtn);
                return result;
            }
            //코인을 보낸다.
            else {
                try {
                    rtn = boardService.sendDevWithRhCoin(params, membershipInfo);
                } catch (Exception e) {
                    logger.error("[sendRhCoinInDev][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_COIN_SEND_NG);
                    return result;
                }
                result.setReturnCode(rtn);
                return result;
            }

        }
        result.setReturnCode(ReturnType.RTN_TYPE_COIN_TYPE_NG);
        return result;
    }

    @ApiOperation("IT STORY 댓글달기")
    @PostMapping("/reply/tech")
    public ResponseHandler<?> writeReplyTech(@ApiParam(value = "코인 종류", example = "리워드 파워 = rp, 코인 = coin") @RequestParam String ctype,
                                             @ApiParam(value = "송금 하고자 하는 코인의 양") @RequestParam(required = false) Double reqCoin,
                                             @ApiParam(value = "댓글 내용") @Valid @RequestBody ReplyReq replyReq,
                                             BindingResult bindingResult) {
        final ResponseHandler<?> result = new ResponseHandler<>();
        ReturnType rtn;

        if (reqCoin == null) reqCoin = 0.0;

        if (reqCoin < 0) {
            result.setReturnCode(ReturnType.RTN_TYPE_COIN_MUST_POSITIVE_NUM_NG);
            return result;
        }
        if (!bindingResult.hasErrors()) {
            Map<String, Object> membershipInfoAndReturnType = getMembershipInfoAndReturnType(membershipService);
            if (membershipInfoAndReturnType.get("returnType") == ReturnType.RTN_TYPE_SESSION) {
                result.setReturnCode(ReturnType.RTN_TYPE_SESSION);
                return result;
            }
            MembershipInfo membershipInfo = (MembershipInfo) membershipInfoAndReturnType.get("membershipInfo");
            Map<String, Object> params = ConvertUtil.convertObjectToMap(replyReq);
            params.put("replyProfileSeq", membershipInfo.getProfileSeq());
            params.put("replyContent", params.get("content"));
            if (checkCoinType(ctype)) {
                // 보내고자 하는 코인의 양
                params.put("reqCoin", reqCoin);
                Map senderInfo = null;
                try {
                    senderInfo = membershipService.getRewardPowerAndCoin(membershipInfo.getProfileSeq());
                    if (senderInfo == null) throw new Exception();
                } catch (Exception e) {
                    log.error("[GetRewardPowerAndCoin][Exception] : {}", e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_GET_COIN_POWER_NG);
                    return result;
                }
                double rewardPower = (double) senderInfo.get("REWARD_POWER");
                double rhCoin = (double) senderInfo.get("RH_COIN");
                try {
                    // rewardPower를 보낸다.
                    if (ctype.equals("rp")) {
                        //보유하고 있는 Reward Power가 보내고자 하는 코인보다 작으면
                        //Error 발생
                        if (rewardPower < reqCoin) {
                            log.error("[NotEnoughRewardPower][Error]");
                            result.setReturnCode(ReturnType.RTN_TYPE_REWARD_POWER_NOT_ENOUGH_NG);
                            return result;
                        }
                        rtn = boardService.insertReplyListTechWithRewardPower(params, membershipInfo);
                        result.setReturnCode(rtn);
                    }
                    //코인을 보낸다.
                    else {
                        //보유하고 있는 RH COIN이 보내고자 하는 코인보다 작으면
                        //Error 발생
                        if (rhCoin < reqCoin) {
                            log.error("[NotEnoughCoin][Error]");
                            result.setReturnCode(ReturnType.RTN_TYPE_RH_COIN_NOT_ENOUGH_NG);
                            return result;
                        }
                        rtn = boardService.insertReplyListTechWithRhCoin(params, membershipInfo);
                        result.setReturnCode(rtn);
                    }
                } catch (Exception e) {
                    logger.error("[Reply][Exception] " + e.toString());
                    result.setReturnCode(ReturnType.RTN_TYPE_BOARD_REPLY_NG);
                }

            } else {
                result.setReturnCode(ReturnType.RTN_TYPE_COIN_TYPE_NG);
            }
            return result;
        }
        result.setReturnCode(ReturnType.RTN_TYPE_BOARD_REPLY_IS_EMPTY_NG);
        return result;
    }

    @ApiOperation("최근 게시물")
    @GetMapping("/board/new")
    public ResponseHandler<?> showNewBoard() throws Exception {
        final ResponseHandler<Object> result = new ResponseHandler<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> userInfo = GetClientProfile.getUserInfo(membershipService);
        List<Map<String, Object>> newBoards = null;
        try {
            newBoards = boardService.showNewBoard();
            if (newBoards.isEmpty() || newBoards == null) {
                result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
            } else {
                res.put("authority", userInfo.get("authority"));
                res.put("nickname", userInfo.get("nickname"));
                res.put("data", newBoards);
                result.setData(res);
                result.setReturnCode(ReturnType.RTN_TYPE_OK);
            }
        } catch (Exception e) {
            logger.error("[NewBoard][Exception] " + e.toString());
            result.setReturnCode(ReturnType.RTN_TYPE_BOARD_LIST_NO_EXIST);
        }
        return result;
    }


}
