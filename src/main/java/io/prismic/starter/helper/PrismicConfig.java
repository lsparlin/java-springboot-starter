package io.prismic.starter.helper;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PrismicConfig {

    private String apiEndpoint;
    private String accessToken;
    private String clientId;
    private String clientSecret;

    @Value("${prismic.api.url}")
    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    @Value("${prismic.api.token}")
    public void setAccessToken(String accessToken) {
        if (Strings.isNullOrEmpty(accessToken))
            accessToken = null;
        this.accessToken = accessToken;
    }

    @Value("${prismic.api.client.id}")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Value("${prismic.api.client.secret}")
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

}