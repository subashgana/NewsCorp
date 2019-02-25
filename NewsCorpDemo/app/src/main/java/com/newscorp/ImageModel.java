package com.newscorp;

import java.io.Serializable;

public class ImageModel implements Serializable {

    int ambumId;
    int id;
    String title;
    String url;
    String thumbnailUrl;

    /**
     * Required for Bindings
     *
     * @return The image title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Required for Bindings
     *
     * @return The image fullscreen url
     */
    public String getUrl() {
        return url;
    }
}
