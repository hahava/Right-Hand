package com.righthand.membership.config;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix="membership")
@PropertySource("classpath:properties/configMembership.properties")
public class ConfigMembership{

    @NotNull
    private boolean useMemberShip; //

    // 필드 및 기능 사용할지 말지를 결정
    private boolean useUserEmail ; //  true

    // Default ; // > 일반 유저 권한 // 00 - 인증 필요, 01 - 일반 유저 , 0x100 - 어드민
    private int selectAuthorityLevelDefault; // 0x00

    // 기본 권한 그룹
    // 인증 사용 - 인증 받지 않은 사용자
    private String selectUnauthorityUserStr ; //  UN_USER
    private int selectUnauthorityUserNo ; //  0x00

    //유저 권한 User
    /////////////////////////////////////////////////////////////////////////////////////////
    private String selectAuthorityUserStr ; //  USER
    private int selectAuthorityUserNo ; //  0x01

    //admin 권한
    private String selectAuthorityContentsAdminStr ; //  컨텐츠 ADMIN
    private int selectAuthorityContentsAdminNo ; //  0x0010

    private String selectAuthorityAdminStr ; //  일반 ADMIN
    private int selectAuthorityAdminNo ; //  0x0020

    private String selectAuthoritySuperAdminStr ; //  슈퍼 ADMIN
    private int selectAuthoritySuperAdminNo ; //  0x0040
    /////////////////////////////////////////////////////////////////////////////////////////

    private int LoginTypeEmail ; // 0
    private int LoginTypeFacebookAccountKitEmail ; // 1
    private int LoginTypeFacebookAccountKitTel ; // 2
    private int LoginTypeFacebookApp ; // 3
    private int LoginTypeGoogle ; // 4

    //thirdparty
    private ClientId clientid = new ClientId();

    @Setter
    @Getter
    public static class ClientId {
        private String google;
    }

    private String defaultLangCode;

    private String emailUrlHostName;
    private String passwordConfirmUrl; //비밀번호 찾기 페이지 URL
}
