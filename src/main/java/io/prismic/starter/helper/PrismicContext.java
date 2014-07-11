package io.prismic.starter.helper;

import io.prismic.Api;
import io.prismic.Cache;
import io.prismic.Document;
import io.prismic.DocumentLinkResolver;
import io.prismic.Documents;
import io.prismic.Logger;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A Prismic context that help to keep the reference
 * to useful primisc.io contextual data
 */
@Component
public class PrismicContext {

    // -- Define the key name to use for storing the Prismic.io access token into the Play session
    public final static String ACCESS_TOKEN = "ACCESS_TOKEN";

    @Autowired
    private PrismicConfig config;
    @Autowired
    private Cache cache;
    @Autowired
    private Logger logger;

    private Api api;
    private String ref;
    private MyLinkResolver linkResolver;
    private String accessToken;


    @Autowired
    public void setLinkResolver(MyLinkResolver linkResolver) {
        linkResolver.setPrismicContext(this);
        this.linkResolver = linkResolver;
    }

    public Api getApi() {
        return api;
    }

    public DocumentLinkResolver getLinkResolver() {
        return linkResolver;
    }

    public String getRef() {
        return ref;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String maybeRef() {
        if (ref.equals(api.getMaster().getRef())) {
            return null;
        }
        return ref;
    }

    public boolean hasPrivilegedAccess() {
        return accessToken != null;
    }

    @PostConstruct
    private void init() {

        // Override the common accessToken with the one in Session if provided
        this.accessToken = config.getAccessToken();
        //if (session != null && session.getAttribute(ACCESS_TOKEN) != null) {
        //this.accessToken = (String) session.getAttribute(ACCESS_TOKEN);
        //}

        // Retrieve the API
        this.api = Api.get(config.getApiEndpoint(), accessToken, cache, logger);

        // Ref
        //this.ref = request != null ? request.getParameter("ref") : null;
        //if (this.ref == null || this.ref.trim().isEmpty()) {
        this.ref = this.api.getMaster().getRef();
        //}

    }

    // -- Helper: Retrieve a single document by Id
    public Document getDocument(String id) {
        Documents docs = this.getApi().getForm("everything").query("[[:d = at(document.id, \"" + id + "\")]]").ref(this.getRef()).submit();
        if (docs.getResults().size() > 0) {
            return docs.getResults().get(0);
        }
        return null;
    }

    // -- Helper: Retrieve several documents by Id
    public Documents getDocuments(List<String> ids) {
        if (ids.isEmpty()) {
            return null;
        } else {
            StringBuilder q = new StringBuilder();
            q.append("[[:d = any(document.id, [");
            String sep = "";
            for (String id : ids) {
                q.append(sep).append("\"").append(id).append("\"");
                sep = ",";
            }
            q.append("\"]]");
            return this.getApi().getForm("everything").query(q.toString()).ref(this.getRef()).submit();
        }
    }

    // -- Helper: Retrieve a single document from its bookmark
    public Document getBookmark(String bookmark) {
        String id = this.getApi().getBookmarks().get(bookmark);
        if (id != null) {
            return getDocument(id);
        } else {
            return null;
        }
    }

    // -- Helper: Check if the slug is valid and return to the most recent version to redirect to if needed, or return DOCUMENT_NOT_FOUND if there is no match
    public static final String DOCUMENT_NOT_FOUND = "DOCUMENT_NOT_FOUND".intern();

    public String checkSlug(Document document, String slug) {
        if (document != null) {
            if (document.getSlug().equals(slug)) {
                return null;
            }
            if (document.getSlugs().contains(slug)) {
                return document.getSlug();
            }
        }
        return DOCUMENT_NOT_FOUND;
    }


}