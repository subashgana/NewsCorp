package com.newscorp;

import java.util.List;

public class ImagesApiResponse {
    List<ImageModel> images;
    public boolean failure;

    public ImagesApiResponse(List<ImageModel> images) {
        this.images = images;
        if (images == null || images.isEmpty()) {
            failure = true;
        }
    }
}