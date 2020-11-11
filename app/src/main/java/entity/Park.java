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
        parkUrlList.add("https://media.timeout.com/images/105447160/1024/576/image.jpg");
        parkUrlList.add("https://www.straitstimes.com/sites/default/files/styles/article_pictrure_780x520_/public/articles/2017/04/22/42323577_-_22_04_2017_-_anwindsor23.jpg?itok=I6GCBbRJ&timestamp=1492858658");
        parkUrlList.add("https://media.timeout.com/images/105666462/1024/576/image.jpg");
        parkUrlList.add("https://cdn.pixabay.com/photo/2012/08/06/00/53/bridge-53769_1280.jpg");
        parkUrlList.add("https://media.istockphoto.com/photos/picturesque-morning-in-plitvice-national-park-colorful-spring-scene-picture-id1093110112");
        parkUrlList.add("https://previews.123rf.com/images/sathaporn/sathaporn1510/sathaporn151000348/46205632-green-park-and-sky.jpg");
        parkUrlList.add("https://previews.123rf.com/images/sathaporn/sathaporn1504/sathaporn150400001/39567990-green-park-and-sky.jpg");
        parkUrlList.add("https://previews.123rf.com/images/sathaporn/sathaporn1611/sathaporn161100118/70897592-green-park-in-the-daytime-sky.jpg");
        parkUrlList.add("https://previews.123rf.com/images/tortoon/tortoon1309/tortoon130900032/22173288-green-grass-pathway-in-a-lush-green-park.jpg");
        parkUrlList.add("https://previews.123rf.com/images/tortoon/tortoon1309/tortoon130900013/22017332-stone-pathway-in-a-lush-green-park.jpg");
        parkUrlList.add("https://previews.123rf.com/images/tortoon/tortoon1309/tortoon130900031/22173276-step-stone-pathway-in-a-lush-green-park.jpg");
        parkUrlList.add("https://previews.123rf.com/images/mooboyba/mooboyba1204/mooboyba120400022/13371341-walk-in-the-park.jpg");
        parkUrlList.add("https://previews.123rf.com/images/art9858/art98581311/art9858131100017/23932330-stone-pathway-in-the-green-park.jpg");
        parkUrlList.add("https://previews.123rf.com/images/theerapoll/theerapoll1506/theerapoll150600112/40844451-city-park-under-blue-sky-with-building-background.jpg");
        parkUrlList.add("https://previews.123rf.com/images/theerapoll/theerapoll1506/theerapoll150600113/40844452-city-park-under-blue-sky-with-building-background.jpg");
        parkUrlList.add("https://previews.123rf.com/images/theerapoll/theerapoll1606/theerapoll160600056/60747442-city-park-under-blue-sky-with-building-background.jpg");
        parkUrlList.add("https://previews.123rf.com/images/themorningglory/themorningglory1610/themorningglory161000055/64168895-green-tree-scene-of-city-park-fresh-air-in-city.jpg");
        parkUrlList.add("https://previews.123rf.com/images/theerapoll/theerapoll1506/theerapoll150600031/40843908-city-park-under-blue-sky-with-building-background.jpg");
        parkUrlList.add("https://previews.123rf.com/images/theerapoll/theerapoll1606/theerapoll160600014/60453849-city-park-under-blue-sky-with-building-background.jpg");
        parkUrlList.add("https://previews.123rf.com/images/zorabc/zorabc1706/zorabc170603290/80822259-city-park-with-modern-building-background-in-shanghai.jpg");
        parkUrlList.add("https://media.gettyimages.com/photos/beautiful-young-sporty-asian-woman-running-at-public-park-picture-id1164622971?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/singapore-botanic-gardens-picture-id622256872?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/palm-trees-growing-by-lake-at-pasir-ris-park-picture-id675590207?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/singapore-bishan-park-hdb-picture-id173786643?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/woman-travel-to-fort-canning-park-picture-id1135429111?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/the-highest-public-pedestrian-bridge-in-singapore-henderson-waves-36-picture-id1173823962?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/trees-in-park-picture-id1041191676?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/sam_458312jpg-picture-id1000296352?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/jurong-lake-and-park-singapore-picture-id492556613?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/singapore-botanic-gardens-picture-id1167901472?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/chek-jawa-intertidal-zone-on-pulau-ubin-picture-id664595430?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/beach-park-in-singapore-picture-id1160897405?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/rear-view-of-woman-walking-on-footpath-against-trees-picture-id1161398383?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/sunlight-in-misty-forest-at-mcritchie-reservior-picture-id580177817?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/women-mountain-biking-holding-a-map-picture-id639114906?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/gazebo-at-singapore-botanic-gardens-against-blue-sky-picture-id726849115?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/happy-mountain-biker-riding-outdoors-picture-id639114738?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/kallang-river-picture-id143403134?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/chinese-garden-in-singapore-picture-id953061848?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/empty-bench-in-park-picture-id988519984?s=2048x2048");
        parkUrlList.add("https://media.gettyimages.com/photos/lakeside-sunset-macritchie-reservoir-park-picture-id115963395?s=2048x2048");




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