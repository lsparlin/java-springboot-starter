package io.prismic.starter.helper;

import io.prismic.DocumentLinkResolver;
import io.prismic.Fragment;
import org.springframework.stereotype.Component;

@Component
public class MyLinkResolver extends DocumentLinkResolver {

    private PrismicContext prismic;

    public void setPrismicContext(PrismicContext prismic) {
        this.prismic = prismic;
    }

    public String resolve(Fragment.DocumentLink link) {
        StringBuilder sb = new StringBuilder("/documents/").append(link.getId()).append("/").append(link.getSlug());
        if (prismic.maybeRef() != null)
            sb.append("?ref=").append(prismic.maybeRef());
        return sb.toString();
    }
}