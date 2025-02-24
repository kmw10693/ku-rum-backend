package ku_rum.backend.domain.building.domain;

public enum BuildingAbbrev {

  //빌딩
  행정("행정관"),
  경영("경영관"),
  상허관("상허연구관"),
  사("교육과학관"),
  예("예술문화관"),
  언어원("언어교육원"),
  박물관("박물관"),
  종강("법학관"),
  도서관("상허기념도서관"),
  의("의생명과학연구관"),
  생("생명과학관"),
  동("동물생명과학관"),
  입학("입학정보관"),
  산학("산학협동관"),
  수("수의학관"),
  새("새천년관"),
  건("건축관"),
  부("해봉부동산학관"),
  문("인문학관"),
  학생회관("제1학생회관"),
  공("공학관"),
  신공("신공학관"),
  이("과학관"),
  창("창의관"),
  KU기술혁신관("KU기술혁신관"),
  기숙사("쿨하우스"),


  //카테고리
  레스티오_동생대점("레스티오_동물생명과학대학점"),
  레스티오_공대점("레스티오_공학관점"),
  카페_1984_학생회관점("카페_1984_제1학생회관점"),
  카페_1984_도서관점("카페_1984_도서관지하식당점"),
  
  //CU
  CU_학생회관점("CU_제1학생회관점"),
  CU_도서관점("CU_도서관3층"),

  //학생식당
  학생식당_학생회관점("학생식당_제1학생회관점"),
  학생식당_도서관점("학생식당_상허도서관점"),
  학생식당_기숙사점("학생식당_쿨하우스점"),

  //KCUBE
  KCUBE_공학관("KCUBE_공학관");


  final private String originalName;

  public String getOriginalName(){
    return originalName;
  }
  BuildingAbbrev(String originalName){
    this.originalName = originalName;
  }

  // originalName 값으로찾는 메서드
  public static BuildingAbbrev fromOriginalName(String originalName) {
    for (BuildingAbbrev abbrev : BuildingAbbrev.values()) {
      if (abbrev.originalName.equals(originalName)) {
        return abbrev;
      }
    }
    return null;
  }

  // 줄임말로 값으로찾는 메서드
  public static BuildingAbbrev fromAbbrevName(String abbrevName) {
    for (BuildingAbbrev abbrev : BuildingAbbrev.values()) {
      if (abbrev.name().equals(abbrevName)) {
        return abbrev;
      }
    }
    return null;
  }
}
