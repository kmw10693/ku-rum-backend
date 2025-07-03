package ku_rum.backend.global.dataInit;

import jakarta.annotation.PostConstruct;
import ku_rum.backend.domain.college.domain.College;
import ku_rum.backend.domain.college.domain.repository.CollegeRepository;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final CollegeRepository collegeRepository;
    private final CollegeInitializer collegeInitializer;

    private final DepartmentRepository departmentRepository;
    private final DepartmentInitializer departmentInitializer;

    @PostConstruct
    public void init() {
        System.out.println("DataLoader 실행됨!");
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<College> savedColleges;
        List<Department> savedDepartments;

        //단과대 정보 반환
        if (collegeRepository.count() == 0){
            savedColleges = collegeRepository.saveAll(collegeInitializer.initialize());
            System.out.println("Colleges saved1: " + savedColleges.size());

        }else{
            savedColleges = collegeRepository.findAll();
            System.out.println("Colleges saved2: " + savedColleges.size());

        }

        //학과 정보 반환
        if (departmentRepository.count() == 0){
            savedDepartments = departmentRepository.saveAll(departmentInitializer.initialize(savedColleges));
            System.out.println("departments saved1: " + savedDepartments.size());

        }else{
            savedDepartments = departmentRepository.findAll();
            System.out.println("departments saved2: " + savedDepartments.size());

        }
    }
}

