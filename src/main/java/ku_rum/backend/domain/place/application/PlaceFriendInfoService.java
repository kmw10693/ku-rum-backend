package ku_rum.backend.domain.place.application;

import com.amazonaws.services.s3.AmazonS3;
import ku_rum.backend.domain.building.domain.Building;
import ku_rum.backend.domain.building.domain.repository.BuildingRepository;
import ku_rum.backend.domain.category.domain.repository.CategoryRepository;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.friend.domain.vo.FriendStatus;
import ku_rum.backend.domain.place.domain.Place;
import ku_rum.backend.domain.place.domain.repository.PlaceRepository;
import ku_rum.backend.domain.place.dto.response.PlaceFriendInfo2Response;
import ku_rum.backend.domain.place.dto.response.PlaceFriendInfoResponse;
import ku_rum.backend.domain.place.dto.response.PlaceSearchInfoResponse;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.utill.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PlaceFriendInfoService {

    private final CategoryRepository categoryRepository;
    private final PlaceRepository placeRepository;
    private final FriendRepository friendRepository;
    private final BuildingRepository buildingRepository;


    private final AmazonS3 amazonS3;
    private final UserUtil userUtil;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public List<PlaceFriendInfoResponse> getPlaceFriendInfoList(String chipName) {
        //chipName으로 category_id 리스트 조회
        List<Long> categoryIds = categoryRepository.findIdsByChipName(chipName);

        //category_id에 해당하는 장소 리스트 조회
        List<Place> places = placeRepository.findByCategoryIds(categoryIds);

        //각 장소별 friendList 생성
        List<PlaceFriendInfoResponse> responseList = places.stream().map(place -> {
            // 장소별(장소가 위치한 빌딩별) 현재 위치 공유중인 친구 목록 조회
            List<User> activeFriends = friendRepository.findActiveFriendsInPlace(place.getBuilding().getName(), userUtil.getUser().getId());

            // 친구 정보로 Friend record 리스트 생성
            List<PlaceFriendInfoResponse.Friend> friendList = activeFriends.stream()
                    .map(friend -> new PlaceFriendInfoResponse.Friend(
                            friend.getNickname(),
                            amazonS3.getUrl(bucket, friend.getProfileImageKey()).toString()
                    ))
                    .toList();

            // 장소 정보 + 친구 목록 응답 생성
            return new PlaceFriendInfoResponse(
                    place.getName(),
                    place.getSubName(),
                    place.getText(),
                    place.getLatitude(),
                    place.getLongitude(),
                    friendList
            );
        }).toList();

        return responseList;
    }


    @Transactional
    public List<PlaceFriendInfo2Response> getPlaceFriendInfoList2() {
        //현재 로그인 사용자 ID 가져오기
        Long currentUserId = userUtil.getUser().getId();

        //현재 사용자의 친구 목록
        List<User> friends = friendRepository.findFriendsByUserIdAndStatus(currentUserId, FriendStatus.ACCEPT);

        //친구별로 위치 공유 중인 장소 리스트 조회 및 응답 변환
        List<PlaceFriendInfo2Response> result = friends.stream().map(friend -> {
            List<PlaceFriendInfo2Response.Place> places = placeRepository.findPlacesByActiveBuildingName(friend.getActiveBuildingName())
                    .stream()
                    .map(place -> new PlaceFriendInfo2Response.Place(
                            place.getName(),
                            place.getSubName(),
                            place.getText(),
                            place.getLatitude(),
                            place.getLongitude()
                    ))

                    .toList();

            return new PlaceFriendInfo2Response(friend.getNickname(), places);
        }).toList();

        return result;
    }

    @Transactional
    public List<PlaceSearchInfoResponse> getPlacesNameBySearch(String search) {
        List<PlaceSearchInfoResponse> resultList = new ArrayList<>();

        //place.name, place.subName 에서 검색
        List<PlaceSearchInfoResponse> nameMatches = placeRepository.searchPlacesByName(search);
        resultList.addAll(nameMatches);

        //building.name, building.subName 에서 검색
        List<Building> buildingNames = buildingRepository.findBuildingNamesByKeyword(search);

        if (!buildingNames.isEmpty()) {
            // building의 abbreviation과 name 둘 다 resultList에 추가
            List<PlaceSearchInfoResponse> buildingMatches = buildingNames.stream()
                    .flatMap(place -> List.of(
                            new PlaceSearchInfoResponse(place.getName()),
                            new PlaceSearchInfoResponse(place.getAbbreviation())
                    ).stream())
                    .toList();

            resultList.addAll(buildingMatches);
        }

        //중복 제거 후 반환
        return resultList.stream()
                .distinct()
                .toList();
    }


    @Transactional
    public List<PlaceFriendInfoResponse> getDetailWithPlacesNameBySearch(String search) {
        Long currentUserId = userUtil.getUser().getId();

        List<PlaceFriendInfoResponse> resultList = new ArrayList<>();

        // 1) Place에서 name, subName으로 검색
        List<Place> placesByName = placeRepository.findByNameOrSubNameContaining(search);

        for (Place place : placesByName) {
            // place의 building_id를 현재 공유 장소로 가지고 있는 친구 목록 조회
            List<User> activeFriends = friendRepository.findActiveFriendsInPlaceByBuildingId(currentUserId,  place.getBuilding().getName());

            List<PlaceFriendInfoResponse.Friend> friendList = activeFriends.stream()
                    .map(friend -> new PlaceFriendInfoResponse.Friend(
                            friend.getNickname(),
                            amazonS3.getUrl(bucket, friend.getProfileImageKey()).toString()
                    ))
                    .toList();

            resultList.add(new PlaceFriendInfoResponse(
                    place.getName(),
                    place.getSubName(),
                    place.getText(),
                    place.getLatitude(),
                    place.getLongitude(),
                    friendList
            ));
        }

        // 2) Building에서 name, abbreviation으로 검색
        List<Building> buildings = buildingRepository.findBuildingNamesByKeyword(search);

        for (Building building : buildings) {
            // 해당 빌딩 이름을 현재 공유 장소로 가지고 있는 친구 목록 조회
            List<User> activeFriends = friendRepository.findActiveFriendsInPlace(building.getName(), currentUserId);

            // 해당 빌딩에 속한 Place 리스트 조회
            List<Place> placesByBuilding = placeRepository.findByBuildingId(building.getId());

            List<PlaceFriendInfoResponse.Friend> friendList = activeFriends.stream()
                    .map(friend -> new PlaceFriendInfoResponse.Friend(
                            friend.getNickname(),
                            amazonS3.getUrl(bucket, friend.getProfileImageKey()).toString()
                    ))
                    .toList();

            for (Place place : placesByBuilding) {
                resultList.add(new PlaceFriendInfoResponse(
                        place.getName(),
                        place.getSubName(),
                        place.getText(),
                        place.getLatitude(),
                        place.getLongitude(),
                        friendList
                ));
            }
        }

        // 중복 제거 후 반환
        return resultList.stream()
                .distinct()
                .toList();
    }

}
