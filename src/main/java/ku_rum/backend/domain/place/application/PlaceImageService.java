package ku_rum.backend.domain.place.application;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import ku_rum.backend.global.exception.place.PlaceImageNotFoundException;
import ku_rum.backend.global.support.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaceImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 특정 placeId 폴더 내 모든 객체의 presigned URL 리스트 반환
     */
    public List<String> getPresignedImageUrls(Long placeId) {
        String prefix = "places/" + placeId + "/";

        List<String> matchedKeys = findS3KeysByPrefix(prefix);
        if (matchedKeys.isEmpty()) {
            throw new PlaceImageNotFoundException(BaseExceptionResponseStatus.PLACE_IMAGE_NOT_FOUND);
        }

        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 12); // 12분 유효

        List<String> urls = new ArrayList<>();
        for (String key : matchedKeys) {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);
            urls.add(amazonS3.generatePresignedUrl(request).toString());
        }

        return urls;
    }

    /**
     * 주어진 prefix에 해당하는 모든 S3 객체 키 반환
     */
    private List<String> findS3KeysByPrefix(String prefix) {
        List<String> matchedKeys = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(prefix)
                .withMaxKeys(1000);
        ListObjectsV2Result result;

        do {
            result = amazonS3.listObjectsV2(req);
            for (S3ObjectSummary summary : result.getObjectSummaries()) {
                matchedKeys.add(summary.getKey());
            }
            req.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return matchedKeys;
    }
}
