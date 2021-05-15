package newly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Addanno {
    static {
        System.setProperty("fileName", "addanno/addanno.log");
    }
    public static Logger log = LoggerFactory.getLogger(Addanno.class);
    public static void main(String[] args) {
        String url = "https://goic.sf-express.com/msgcenter/api/msgList?pageSize=20&status=&msgType=11&pageNum=1";

        String cookie = "logisticsCenter=6; " +
                "USS=EoPAAAxCWY7Wnhbdh10Jgt0RB5HLRRTP2lafW5hWQheMioIUDEeWE9oJipuX19ihZ7gQAAYC0edXkxdE8KO21IDBI3L0NSbQ9mehUsHGlAe1laOjUORSYzXFXdBQrvmKkGhUIAAGbR9Q4hyWcNoOSLDkDFhg8JjkYOZR~kDWravA0hnDYMUOQDAHR0JyBoAA; STOKEN=B8AAJoTAAAoeSZJeT9dG14ZBj4jDwxH0985Cla2mAr0dQQMC2EAoAACiTsBb8d4wWPX2MFwqJAACpuuEacajVGlwCrhheqAAAVL55IVPbAADxFwAA1YwwLe8LnitFHQi";

        System.out.println(BaseUtils.getByForm(url,cookie));

        //        for (int i = 265; i < 295; i++) {
//            String params = "type=1300&" +
//                    "class=11&" +
//                    "lc_ids=6&" +
//                    "supplier_ids=100006&" +
//                    "station_ids=10000006&" +
//                    "aoi_ids=&" +
//                    "rider_work_type=&" +
//                    "is_push=0&" +
//                    "title=android tanchuang 00"+i+"&" +
//                    "sub_title=&" +
//                    "content=<p>abbb</p>&" +
//                    "action=1&" +
//                    "publish_type=0&" +
//                    "rider_trait_ids=";
//            BaseUtils.postByForm(url,cookie,params);
//            System.out.println("创建--->"+i);
//
//        }
    }
}
