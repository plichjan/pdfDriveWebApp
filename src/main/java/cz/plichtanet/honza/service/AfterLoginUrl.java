package cz.plichtanet.honza.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author Jan.Plichta
 * @since 8.6.2016
 */
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
@Component
public class AfterLoginUrl implements IAfterLoginUrl {
    private String url;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }
}
