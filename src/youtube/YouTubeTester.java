package youtube;

/**
 * User: mihai.panaitescu
 * Date: 21-May-2010
 * Time: 15:18:39
 */

import java.util.List;

// http://www.dzone.com/links/r/getting_started_with_youtube_java_api.html
public class YouTubeTester {

    public static void main(String[] args) throws Exception {

        String clientID = "Mike";
        String textQuery = "NextReports";
        int maxResults = 10;
        boolean filter = true;
        int timeout = 500000;

        YouTubeManager ym = new YouTubeManager(clientID);

        List<YouTubeVideo> videos = ym.retrieveVideos(textQuery, maxResults, filter, timeout);

        for (YouTubeVideo youtubeVideo : videos) {
            System.out.println(youtubeVideo.getWebPlayerUrl());
            System.out.println("Thumbnails");
            for (String thumbnail : youtubeVideo.getThumbnails()) {
                System.out.println("\t" + thumbnail);
            }
            System.out.println(youtubeVideo.getEmbeddedWebPlayerUrl());
            System.out.println("************************************");
        }

    }

}