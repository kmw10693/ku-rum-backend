package ku_rum.backend.domain.department.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DepartmentType {

  //자유전공학부
  자유전공학부("Liberal Arts and Sciences"),


  //공과대학 (College of Engineering)
  컴퓨터공학부("Computer Science Engineering"),
  사회환경공학부("Civil and Environmental Engineering"),
  기계공학부("Mechanical Engineering"),
  전기전자공학부("Electrical and Electronic Engineering"),
  화학공학부("Chemical Engineering"),
  신산업융합학과("New Industry Convergence"),
  K뷰티산업융합학과("K-Beauty Industry Convergence"),
  항공우주정보시스템공학과("Aerospace Information Systems Engineering"),
  생물공학과("Biotechnology Engineering"),
  산업공학과("Industrial Engineering"),

  //문과대학 (College of Liberal Arts)
  국어국문학과("Korean Language and Literature"),
  영어영문학과("English Language and Literature"),
  중어중문학과("Chinese Language and Literature"),
  철학과("Philosophy"),
  사학과("History"),
  지리학과("Geography"),
  미디어커뮤니케이션학과("Media and Communication"),
  문화콘텐츠학과("Culture and Contents"),
  휴먼ICT연계전공("Human ICT Interdisciplinary Major"),
  글로벌MICE연계전공("Global MICE Interdisciplinary Major"),
  인문상담치유연계전공("Humanities Counseling and Healing Interdisciplinary Major"),
  통일인문교육연계전공("Unification Humanities Education Interdisciplinary Major"),

  //사회과학대학 (College of Social Sciences)
  정치외교학과("Political Science and Diplomacy"),
  경제학과("Economics"),
  행정학과("Public Administration"),
  국제무역학과("International Trade"),
  응용통계학과("Applied Statistics"),
  융합인재학과("Convergence Human Resources"),
  글로벌비즈니스학과("Global Business"),

  //경영대학 (College of Business)
  경영학과("Business Administration"),
  기술경영학과("Management of Technology"),
  부동산학과("Real Estate Studies"),

  //KU 융합기술원 (KU Institute of Convergence Technology)
  미래에너지공학과("Future Energy Engineering"),
  화장품공학과("Cosmetic Engineering"),
  줄기세포재생공학과("Stem Cell and Regenerative Engineering"),
  의생명공학과("Biomedical Engineering"),
  시스템생명공학과("Systems Biotechnology"),
  융합생명공학과("Convergence Biotechnology"),

  //상허생명과학대학 (Sanghuh College of Life Sciences)
  생명과학특성학과("Biological Sciences"),
  동물자원과학과("Animal Science and Technology"),
  식량자원과학과("Crop Science"),
  축산식품생명공학과("Food Science and Biotechnology of Animal Resources"),
  식품유통공학과("Food Marketing and Technology"),
  환경보건과학과("Environmental Health Science"),
  산림조경학과("Forest and Landscape Architecture");

  private final String text;
}