# pinit-backend

### 단위 테스트
1. 기능 단위로 테스트한다.
    - Service Layer 단위로 클래스를 두고 기능을 중심으로 테스트한다.
2. 테스트 메서드명은 '_' 기반으로 가독성있게 작성하고, 의도를 정확히 나타낸다.
    - pin_create_without_picture_fail()
    - login_with_valid_id_passwd_success()
    - get_deleted_pin_throws_entityException()
    - ...
  
3. Given/When/Then(or AAA) 패턴으로 작성하고, 각 문단은 라인 구분한다.
4. When 구절은 최대한 하나의 선언으로 구성한다.
