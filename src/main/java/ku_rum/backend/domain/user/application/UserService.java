package ku_rum.backend.domain.user.application;

import jakarta.servlet.http.HttpServletRequest;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.repository.BuildingRepository;
import ku_rum.backend.domain.common.mail.application.MailService;
import ku_rum.backend.domain.department.application.DepartmentQueryService;
import ku_rum.backend.domain.department.application.UserDepartmentService;
import ku_rum.backend.domain.department.domain.Department;
import ku_rum.backend.domain.department.domain.UserDepartment;
import ku_rum.backend.domain.department.domain.repository.DepartmentRepository;
import ku_rum.backend.domain.department.domain.repository.UserDepartmentRepository;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.domain.user.dto.request.*;
import ku_rum.backend.domain.user.dto.response.*;
import ku_rum.backend.global.exception.department.DuplicateDepartmentException;
import ku_rum.backend.global.exception.department.NoSuchDepartmentException;
import ku_rum.backend.global.exception.global.GlobalException;
import ku_rum.backend.global.exception.user.DuplicateNicknameException;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import ku_rum.backend.global.exception.user.UserMapBuildingNotFoundException;
import ku_rum.backend.global.utill.LocationUtils;
import ku_rum.backend.global.utill.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserQueryService userQueryService;
    private final DepartmentQueryService departmentQueryService;
    private final UserDepartmentRepository userDepartmentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserDepartmentService userDepartmentService;
    private final MailService mailService;
    private final UserUtil userUtil;

    // 소셜 로그인 X
    @Transactional
    public UserSaveResponse saveUser(final UserSaveRequest userSaveRequest) {
        log.info("사용자 저장 요청: {}", userSaveRequest);
        userValidator.validateUser(userSaveRequest);

        Department department = departmentQueryService.getDepartment(userSaveRequest);

        User user = UserSaveRequest.newUser(userSaveRequest, passwordEncoder.encode(userSaveRequest.password()));
        userDepartmentService.addDeptToUser(user, department);
        user.changeFirstLogin(false);

        log.info("사용자 저장 완료: ID={}", userSaveRequest.loginId());
        return UserSaveResponse.from(userRepository.save(user));
    }

    // 소셜 로그인 전용 회원 가입 토큰 필요
    @Transactional
    public UserSaveResponse saveUserBySocial(final UserSaveRequest userSaveRequest) {
        log.info("사용자 저장 요청: {}", userSaveRequest);
        userValidator.validateUser(userSaveRequest);

        Department department = departmentQueryService.getDepartment(userSaveRequest);
        User user = userUtil.getUser();
        user.changeProfile(userSaveRequest, passwordEncoder.encode(userSaveRequest.password()));

        userDepartmentService.addDeptToUser(user, department);

        log.info("사용자 저장 완료: ID={}", userSaveRequest.loginId());
        return UserSaveResponse.from(user);
    }

    @Transactional
    public void initiatePasswordReset(final InitiatePasswordResetRequest initiatePasswordResetRequest) {
        log.info("계정 초기화 요청: loginId={}", initiatePasswordResetRequest.loginId());
        User user = userQueryService.getUserByLoginId(initiatePasswordResetRequest.loginId());
        mailService.verifyCode(initiatePasswordResetRequest.emailRequest());


        
        if (passwordEncoder.matches(initiatePasswordResetRequest.newPassword(), user.getPassword())) {
            throw new GlobalException(PREV_NEW_EQUAL_EXCEPTION);
        }

        user.changePassword(passwordEncoder.encode(initiatePasswordResetRequest.newPassword()));
        log.info("계정 비밀번호 변경 완료: loginId={}", initiatePasswordResetRequest.loginId());
    }

    @Transactional
    public void resetPassword(final ResetPasswordRequest resetPasswordRequest) {
        log.info("기존 계정 비밀번호 변경 요청");
        User user = getUser();
        userValidator.validatePassword(resetPasswordRequest.prevPassword(), user.getPassword());

        if (resetPasswordRequest.newPassword().equals(resetPasswordRequest.prevPassword())) {
            throw new GlobalException(PREV_NEW_EQUAL_EXCEPTION);
        }
        user.changePassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        log.info("계정 비밀번호 변경 완료: loginId={}", user.getLoginId());
    }

    @Transactional
    public void changeProfile(final ProfileChangeRequest profileChangeRequest) {
        log.info("프로필 변경 요청: imageUrl={}", profileChangeRequest.imageUrl());
        User user = getUser();
        user.changeImage(profileChangeRequest.imageUrl());
        log.info("프로필 변경 완료: userId={}", user.getId());
    }

    public LoginIdResponse getLoginId(final String email) {
        log.info("로그인 ID 조회 요청: email={}", email);
        User user = userQueryService.getUserByEmail(email);
        log.info("로그인 ID 조회 완료: loginId={}", user.getLoginId());
        return LoginIdResponse.of(user.getLoginId());

    }

    @Transactional
    public void changeNickname(final NicknameChangeRequest nicknameChangeRequest) {
        User user = getUser();

        if (isNicknameDuplicate(nicknameChangeRequest.nickname())) {
            throw new DuplicateNicknameException(DUPLICATE_NICKNAME);
        }
        user.changeNickname(nicknameChangeRequest.nickname());
    }

    private User getUser() {
        Long memberId = UserUtil.getLongMemberId();
        log.debug("현재 사용자 조회: userId={}", memberId);
        return userRepository.findUserById(memberId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
    }

    @Transactional
    public void deactivate() {
        User user = getUser();
        log.info("사용자 탈퇴 명령");
        userRepository.delete(user);
    }

    @Transactional
    public void addDepartment(final String department) {
        User user = getUser();
        Department departmentEntity = departmentRepository.findFirstByName(department)
                .orElseThrow(() -> new NoSuchDepartmentException(NO_SUCH_DEPARTMENT));
        if (userDepartmentRepository.existsByUserIdAndDepartmentId(user.getId(), departmentEntity.getId())) {
            throw new DuplicateDepartmentException(DUPLICATE_DEPARTMENT);
        }
        userDepartmentRepository.save(UserDepartment.of(user, departmentEntity));
    }

    @Transactional
    public void deleteDepartment(final String department) {
        Department dept = departmentRepository.findFirstByName(department)
                .orElseThrow(() -> new NoSuchDepartmentException(NO_SUCH_DEPARTMENT));

        int deleted = userDepartmentRepository.deleteByUserIdAndDepartmentId(
                getUser().getId(), dept.getId()
        );

        if (deleted == 0) {
            throw new NoSuchDepartmentException(NO_SUCH_DEPARTMENT);
        }
    }

    private boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public UserShareActiveResponse isShareActive() {
        User currentUser = userUtil.getUser();
        return new UserShareActiveResponse(currentUser.isActive());
    }

    @Transactional(readOnly = true)
    public UserLocationResponse getUserLocation(UserLocationRequest request) {
        List<Building> buildings = buildingRepository.findAll();

        Building nearest = buildings.stream()
                .min(Comparator.comparingDouble(b ->
                        LocationUtils.distance(
                                request.latitude(),
                                request.longitude(),
                                b.getLatitude().doubleValue(),
                                b.getLongitude().doubleValue()
                        )
                ))
                .orElseThrow(() -> new UserMapBuildingNotFoundException(MATCHED_USER_BUILDING_ERROR));

        return new UserLocationResponse(nearest.getName());
    }


    @Transactional
    public UserLocationShareStartResponse startShareLocation(UserLocationShareStartRequest request) {
        //요청한 장소 이름 가져오기
        String placePointed = request.placePointed();

        //현재 로그인한 사용자 정보 가져오기
        User currentUser = userUtil.getUser();

        //사용자 위치 공유 활성화 상태 변경 및 activeBuildingName 변경
        currentUser.changeActiveBuildingName(placePointed);
        currentUser.setLocationSharingActive(true);

        //응답용 DTO 생성 및 반환
        return new UserLocationShareStartResponse(placePointed, true);
    }

    @Transactional
    public UserShareActiveResponse changeToNotActive() {
        User currentUser = userUtil.getUser();
        currentUser.setLocationSharingActive(false);
        return new UserShareActiveResponse(currentUser.isActive());
    }
}
