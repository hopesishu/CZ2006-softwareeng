package entity;

import java.util.ArrayList;

public class Park {
    private static ArrayList<String> parkUrlList;
    private String name;
    private String detail;

    public Park() {
    }

    public Park(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }

    public static ArrayList<String> getParkDatabase(){
        parkUrlList = new ArrayList<>();
        parkUrlList.add("https://www.nparks.gov.sg/-/media/nparks-real-content/gardens-parks-and-nature/parks-and-nature-reserve/admiralty-park/admiralty-park-play-areas.jpg");
        parkUrlList.add("https://www.nparks.gov.sg/-/media/nparks-real-content/gardens-parks-and-nature/parks-and-nature-reserve/bukit-batok-nature-park/rsz_quarry.jpg");
        parkUrlList.add("https://www.nparks.gov.sg/-/media/nparks-real-content/gardens-parks-and-nature/parks-and-nature-reserve/labrador-nature-reserve/088.jpg");
        parkUrlList.add("https://www.nparks.gov.sg/-/media/nparks-real-content/gardens-parks-and-nature/parks-and-nature-reserve/bukit-batok-town-park/rsz_footpath.jpg");
        parkUrlList.add("https://www.nparks.gov.sg/-/media/nparks-real-content/gardens-parks-and-nature/parks-and-nature-reserve/choa-chu-kang-park/cck-pic-cck-pk-ext--playgd-2.jpg");
        parkUrlList.add("https://www.nparks.gov.sg/-/media/nparks-real-content/gardens-parks-and-nature/parks-and-nature-reserve/admiralty-park/admiralty-park-playground.jpg");
        parkUrlList.add("https://static.honeykidsasia.com/wp-content/uploads/2018/08/Hiking-trail-Southern-Ridges-768x567.jpg");
        parkUrlList.add("https://www.nparks.gov.sg/-/media/nparks-real-content/gardens-parks-and-nature/parks-and-nature-reserve/hortpark/gallery-image-update-jan-2017/golden-garden_gallery.jpg");
        parkUrlList.add("https://i0.wp.com/www.onlinecitizenasia.com/wp-content/uploads/2020/10/featured-image-Changi-Beach-near-carpark-6-1.jpg?resize=750%2C375&ssl=1");
        parkUrlList.add("https://media.timeout.com/images/105672938/750/422/image.jpg");
        parkUrlList.add("https://media.timeout.com/images/103461712/750/422/image.jpg");
        parkUrlList.add("https://media.timeout.com/images/103461720/750/422/image.jpg");
        parkUrlList.add("https://media.timeout.com/images/103163916/750/422/image.jpg");
        parkUrlList.add("https://www.sassymamasg.com/wp-content/uploads/2020/07/labrador-park-dragons-tooth-seesaw.jpg");
        parkUrlList.add("https://www.sassymamasg.com/wp-content/uploads/2020/07/labrador-park-jungle.jpeg");
        parkUrlList.add("https://www.sassymamasg.com/wp-content/uploads/2020/07/labrador-park-playground-slides.jpg");
        parkUrlList.add("https://sgmagazine.com/sites/default/files/u143089/fort_canning_park.jpg");
        parkUrlList.add("https://sgmagazine.com/sites/default/files/u143089/jurong_lake_gardens.jpg");
        parkUrlList.add("https://sgmagazine.com/sites/default/files/u143089/macritchie_reservoir_park.jpg");
        parkUrlList.add("https://sgmagazine.com/sites/default/files/u143089/punggol_waterway_park.jpg");
        parkUrlList.add("https://hypeandstuff.com/wp-content/uploads/2017/06/Tampines-Eco-Green-Park-Singapore-14-768x512.jpg");



        return parkUrlList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}