# Right Hand [![Build Status](https://travis-ci.com/sangnyyy/Right-Hand.svg?token=npsQLNTNcFcm4XrdnETF&branch=master)](https://travis-ci.com/sangnyyy/Right-Hand)

## 로컬 이미지 등록 방법

### Project Structure  

    1. Modules 클릭
    2. Add Content Root 클릭
    3. Right-Hand-Imgs 디렉토리 선택(Right-Hand 프로젝트와 동일한 위치에 둘 것!)
    4. Mark As Resources 선택
    
### application.properties
```markdown
spring.resources.static-locations=classpath:/resources,classpath:/static/,classpath:/templates/static/,file:../Right-Hand-Imgs/
```

### configFile.properties
```markdown
file.stroageLoc=../Right-Hand-Imgs
```
