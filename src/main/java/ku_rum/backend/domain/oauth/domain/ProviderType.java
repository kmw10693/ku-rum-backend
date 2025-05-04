package ku_rum.backend.domain.oauth.domain;

public enum ProviderType {
    GOOGLE, KAKAO, NAVER;

    public String getProviderName() {
        return this.name();
    }
}
