package ku_rum.backend.domain.building.application.method;

import java.util.List;

public interface SearchStrategy <T>{
  List<T> search(String searchText);
 }
