# Java Firestore Template

자바에서 Firestore를 사용하기 위한 템플릿입니다.

## 사용법

1. Firestore 인스턴스 초기화
    ```java
    InputStream credentialsStream = new FileInputStream("YOUR_FIREBASE_ADMIN_SDK.json");
    GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
    
    FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build();
    
    FirebaseApp.initializeApp(options);
    ```

2. Data 생성
    ```java
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Money extends Data {
        int money;
    }
    ```

3. Storage 생성
    ```java
    Storage<Money> moneyStorage = Storage.of(Money.class);
    ```

4. 사용
   - Create
        ```java
        Money money = Money.builder()
                              .id(uuid)
                              .money(1000)
                              .build();
        
        moneyStorage.create(money);
        ```
     
   - Read
      ```java
      Money money = moneyStorage.get(uuid);
      ```
       
   - Update
      ```java
      moneyStorage.update(uuid, prev -> prev.setMoney(prev.getMoney() + 1000));
      ```
       
   - Delete
      ```java
      moneyStorage.delete(uuid);
      ```
