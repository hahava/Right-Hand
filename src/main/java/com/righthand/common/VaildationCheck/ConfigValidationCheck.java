package com.righthand.common.VaildationCheck;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Component
@PropertySource("classpath:properties/validationCheck.properties")
@ConfigurationProperties(prefix = "validation.input")
public class ConfigValidationCheck {
    @Valid
    @NotNull
    private Id id = new Id();

    @Getter
    @Setter
    public static class Id {
        private int set;
        private String length;
        private String normal;
        private String email;
    }

    private Pwd pwd = new Pwd();
    @Getter
    @Setter
    public static class Pwd {
        private int set;
        private String length;
        private String capChar;
        private String specChar;
        private String allChar;
        private String digitChar;
    }

    private Phone phone = new Phone();
    @Getter
    @Setter
    public static class Phone {
        private int set;
        private String dash;
        private String noDash;
    }

    private Email email = new Email();
    @Getter
    @Setter
    public static class Email {
        private int set;
        private String length;
        private String form;
    }

    private Nickname nickname = new Nickname();
    @Getter
    @Setter
    public static class Nickname {
        private int set;
        private String length;
        private String specChar;
    }


    /**
     * 아이디 검증
     * @param id 아이디
     * @return {
     *      0 사용가능한 아이디
     *      1 자리수 맞지 않을경우
     *      2 영문, 숫자 로 만들어지지 않은 경우
     *      4 이메일 형식이 아닌 경우
     * }
     */
    public int checkId(String id) {


        int idSet = this.id.getSet();
        if((idSet & 0x01) == 0x01) {
            if(!id.matches(this.id.getLength())){
                return 1;
            }
        }

        if((idSet & 0x02) == 0x02) {
            if(!id.matches(this.id.getNormal())) {
                return 2;
            }
        }
        else if((idSet & 0x04) == 0x04) {
            if(!id.matches(this.id.getEmail())) {
                return 4;
            }
        }

        System.out.println("idSet");

        return 0;
    }

    /**
     * 비밀번호 검증
     * @param pwd 비밀번호
     * @return {
     *      0 사용가능한 비밀번호
     *      1 자리수 맞지 않을경우
     *      2 영대문자가 포함되지 않은경우
     *      3 특수문자가 포함되지 않은경우
     * }
     */
    public int checkPwd(String pwd) {

        if((this.pwd.getSet() & 0x01) == 0x01) {
            if(!pwd.matches(this.pwd.length)) {
                return 1;
            }
        }

        if((this.pwd.getSet() & 0x02) == 0x02) {
            if(!pwd.matches(this.pwd.capChar)) {
                return 2;
            }
        }

        if((this.pwd.getSet() & 0x04) == 0x04) {
            if(!pwd.matches(this.pwd.specChar)) {
                return 3;
            }
        }

        if((this.pwd.getSet() & 0x08) == 0x08) {
            if(!pwd.matches(this.pwd.allChar)) {
                return 4;
            }
        }

        if((this.pwd.getSet() & 0x10) == 0x10) {
            if(!pwd.matches(this.pwd.digitChar)) {
                return 5;
            }
        }
        return 0;
    }

    /**
     * 전화번호 검증
     * @param phone 전화번호
     * @return {
     *      0 사용가능한 전화번호
     *      1 형식에 맞지 않은 전화번호
     * }
     */
    public int checkPhone(String phone) {

        if((this.phone.getSet() & 0x01) == 0x01) {
            if(!phone.matches(this.phone.dash)){
                return 1;
            }
        }

        if((this.phone.getSet() & 0x02) == 0x02) {
            if(!phone.matches(this.phone.noDash)) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * 이메일 검증
     * @param email 이메일
     * @return {
     *      0 사용가능한 이메일
     *      1 자리수가 맞지 않을 경우
     *      2 형식이 맞지 않을경우
     * }
     */
    public int checkEmail(String email) {

        if((this.email.getSet() & 0x01) == 0x01) {
            if(!email.matches(this.email.length)){
                return 1;
            }
        }

        if((this.email.getSet() & 0x02) == 0x02) {
            if (!email.matches(this.email.form)) {
                return 2;
            }
        }
        return 0;
    }

}
