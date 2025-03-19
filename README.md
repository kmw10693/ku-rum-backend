## 쿠룸 백엔드
<img width="100%" alt="image" src="https://github.com/user-attachments/assets/3024650b-a989-4e47-947b-101710730c06" />

## 기술 스택

### 백엔드
<img width="80%" alt="image" src="https://github.com/user-attachments/assets/d96a8087-689e-410c-8a81-d243192997c1" />

<br>

### 인프라
<img width="80%" alt="image" src="https://github.com/user-attachments/assets/5c09b4f9-09d2-47c3-8532-4e04d4cc2335" />


### CI/CD
<img width="80%" alt="image" src="https://github.com/user-attachments/assets/bc53d74f-dc04-4066-9c5e-3773a82c992f" />


### 모니터링
<img width="80%" alt="image" src="https://github.com/user-attachments/assets/36a4488b-3db5-4cc2-91d0-9af329e0d002" />


## 기술 의사 결정

<details>
<summary><h3> &nbsp Spring RestDocs </h3></summary>

### 도입 배경

---
- Swagger (Springdoc OpenAPI)와 같은 UI 기반 문서화 도구도 있지만, </br>
테스트 코드 기반이 아니라서, 실제 API와 문서가 불일치할 가능성이 있음

- Spring REST Docs는 테스트 코드 기반으로 API 문서를 자동 생성하기 때문에 </br>
API 변경 사항이 있으면 테스트 코드 수정과 함께 문서도 자동 업데이트됨
### 선택지

---

|     | Swagger                                                      | Spring REST Docs                                                                                    | 
|:---:|:-----------------------------------------------------------|:-----------------------------------------------------------------------------------------|                                                
| 장점  | 쉬운 세팅 및 사용, 실제 API 호출 기능 제공 | 실제 API의 동작을 테스트하면서 문서가 생성되므로, 코드와 문서 사이의 불일치가 발생할 가능성이 적음 |
| 단점  | 코드에 주석을 기반으로 하므로, 실제 코드와 문서화 사이에 불일치 발생 가능    | 테스트 코드 작성에 투자해야 하는 시간과 노력이 필요  |

### 최종 결정

---
프로덕션 코드에 영향이 없고, 컨트롤러 테스트 코드를 의무화하는 Spring REST Docs와<br> API를 호출 할 수 있는 웹 기반 UI가 제공하는 Swagger UI 통합 사용
</details>

## 멤버

### 백엔드

| <img src="https://avatars.githubusercontent.com/Hyeri1ee" width="130" height="130"> | <img src="https://avatars.githubusercontent.com/buzz0331" width="130" height="130"> |   <img src="https://avatars.githubusercontent.com/tintin010" height="130">   | <img src="https://avatars.githubusercontent.com/kmw10693" width="130" height="130">  |
|:-----------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------:|
|                              [혜리](https://github.com/Hyeri1ee)                               |                             [현준](https://github.com/buzz0331)                              |                          [재윤](https://github.com/tintin010)                              |                          [민우](https://github.com/kmw10693)                           |




