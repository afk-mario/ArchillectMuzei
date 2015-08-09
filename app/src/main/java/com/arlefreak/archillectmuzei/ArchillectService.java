package com.arlefreak.archillectmuzei;

/**
 * Created by arlefreak on 08/08/2015.
 */

import java.util.List;

public class ArchillectService {
    static class PhotosResponse {
        List<Photo> photos;
    }

    static class Photo {
        int id;
        String image_url;
        String name;
        User user;
    }

    static class User {
        String fullname;
    }
}
