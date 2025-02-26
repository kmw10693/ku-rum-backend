package ku_rum.backend.domain.recruitment.application;

import ku_rum.backend.domain.recruitment.domain.RecruitCategory;
import ku_rum.backend.domain.recruitment.domain.Recruitment;
import ku_rum.backend.domain.recruitment.domain.repository.RecruitmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {

    @Mock
    private RecruitmentRepository recruitmentRepository;

    @Mock
    private WebDriver driver;

    @InjectMocks
    private RecruitmentService recruitmentService;

    @Test
    void crawlAndSaveRecruitments_정상_저장() {
        // given
        WebElement element1 = mock(WebElement.class);
        WebElement element2 = mock(WebElement.class);

        // 실제 selector를 사용해야 한다.
        String mockSelector = ".job-list";
        String titleSelector = ".title";
        String urlSelector = ".link";
        String companySelector = ".company";
        String locationSelector = ".location";

        when(driver.findElements(By.cssSelector(mockSelector))).thenReturn(List.of(element1, element2));

        // title
        WebElement titleElement1 = mock(WebElement.class);
        WebElement titleElement2 = mock(WebElement.class);
        when(element1.findElement(By.cssSelector(titleSelector))).thenReturn(titleElement1);
        when(element2.findElement(By.cssSelector(titleSelector))).thenReturn(titleElement2);
        when(titleElement1.getText()).thenReturn("Job Title 1");
        when(titleElement2.getText()).thenReturn("Job Title 2");

        // url
        WebElement urlElement1 = mock(WebElement.class);
        WebElement urlElement2 = mock(WebElement.class);
        when(element1.findElement(By.cssSelector(urlSelector))).thenReturn(urlElement1);
        when(element2.findElement(By.cssSelector(urlSelector))).thenReturn(urlElement2);
        when(urlElement1.getAttribute("href")).thenReturn("http://example.com/1");
        when(urlElement2.getAttribute("href")).thenReturn("http://example.com/2");

        // company
        WebElement companyElement1 = mock(WebElement.class);
        WebElement companyElement2 = mock(WebElement.class);
        when(element1.findElement(By.cssSelector(companySelector))).thenReturn(companyElement1);
        when(element2.findElement(By.cssSelector(companySelector))).thenReturn(companyElement2);
        when(companyElement1.getText()).thenReturn("Company 1");
        when(companyElement2.getText()).thenReturn("Company 2");

        // location & career
        WebElement locationElement1 = mock(WebElement.class);
        WebElement locationElement2 = mock(WebElement.class);
        when(element1.findElement(By.cssSelector(locationSelector))).thenReturn(locationElement1);
        when(element2.findElement(By.cssSelector(locationSelector))).thenReturn(locationElement2);
        when(locationElement1.getText()).thenReturn("Seoul · 3 years");
        when(locationElement2.getText()).thenReturn("Busan · 5 years");

        // RecruitCategory 설정
        RecruitCategory category = mock(RecruitCategory.class);
        when(category.getSelector()).thenReturn(mockSelector);
        when(category.getTitleSelector()).thenReturn(titleSelector);
        when(category.getUrlSelector()).thenReturn(urlSelector);
        when(category.getCompanySelector()).thenReturn(companySelector);
        when(category.getLocationSelector()).thenReturn(locationSelector);

        // when
        recruitmentService.crawlAndSave(category, driver);

        // then
        verify(recruitmentRepository, times(2)).save(any(Recruitment.class));
    }
}