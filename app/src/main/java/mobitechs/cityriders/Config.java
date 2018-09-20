package mobitechs.cityriders;


public class Config {

    // Google Console APIs developer key
    // Replace this key with your's
    public static final String DEVELOPER_KEY = "AIzaSyBnIAqzNEjw_ZJjqsRenVctNQMrIwHtAJw";

    // YouTube video id  cityrider 3dc2b
    public static final String CHANNEL_ID = "UC884bbXh9jnHdwrSyj9soHg";
    public static String PlayListURL= "https://www.googleapis.com/youtube/v3/search?key=AIzaSyBnIAqzNEjw_ZJjqsRenVctNQMrIwHtAJw&channelId=UC884bbXh9jnHdwrSyj9soHg&part=snippet,id&order=date";
    public static String VideoURL= "https://www.googleapis.com/youtube/v3/videos?key=AIzaSyBnIAqzNEjw_ZJjqsRenVctNQMrIwHtAJw&part=snippet,id,statistics&id=LsA-WbEOiRU";


    String subscriptionURL= "https://www.googleapis.com/youtube/v3/subscriptions?part=snippet&key=AIzaSyBnIAqzNEjw_ZJjqsRenVctNQMrIwHtAJw";
}