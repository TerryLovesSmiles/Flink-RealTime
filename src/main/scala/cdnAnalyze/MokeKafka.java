package cdnAnalyze;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Administrator on 2019/9/28.
 */
public class MokeKafka {
    /*aliyun	CN	A	E	[17/Jul/2018:17:07:50 +0800]	2	223.104.18.110	-	112.
            29.213.35:80	0	v2.go2yd.com	GET	http://v1.go2yd.com/user_upload/1531633977627104fdec
    dc68fe7a2c4b96b2226fd3f4c.mp4_bd.mp4	HTTP/1.1	-	bytes 13869056-13885439/25136186	TCP_HIT/206	112.29.213.35	video/mp4	17168	16384	-:0	0	0	-	-	11451601	-	"JSP3/2.0.14"	"-"	"-"	"-"	http	-	2	v1.g
    o2yd.com	0.002	25136186	16384	-	-	-	-	-	-	-	1531818470104-114516
            01-112.29.213.66#2705261172	644514568

    aliyun
    CN
    E
    [17/Jul/2018:17:07:50 +0800]
    223.104.18.110
    v2.go2yd.com
    17168*/

    public static void main(String[] args){

        /*FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                "localhost:9092",            // broker list
                "my-topic",                  // target topic
                new SimpleStringSchema());   // serialization schema
        //不断产生数据--->kafka
        */

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.198.121:9092");
        //键和值的序列化
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

       KafkaProducer<String,String> producer=new KafkaProducer<String, String>(properties);

        while(true){
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append("aliyun").append("\t")
                    .append("CN").append("\t")
                    .append(getLevel()).append("\t")
                    .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\t")
                    .append(getIp()).append("\t")
                    .append(getDomins()).append("\t")
                    .append(getTraffic()).append("\t");

            //向Kafka输出信息
            System.out.println(stringbuffer);
            //为了方便观察

            producer.send(new ProducerRecord<>("aaa", stringbuffer.toString()));
            //歇一歇
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }
    //自定义一下信息级别
    public static String getLevel(){
        String[] level = new String[2];
        level[0] = "E";
        level[1] = "F";
        int i = new Random().nextInt(level.length);
        return level[i];
    }

    //自定义一下ip
    public static String getIp(){
        String[] ips = new String[]{
                "223.104.18.110",
                "113.101.75.194",
                "27.17.127.135",
                "183.225.139.16",
                "112.1.66.34",
                "175.148.211.190",
                "183.227.58.21",
                "59.83.198.84",
                "117.28.38.28",
                "117.59.39.169" };
        String ip = ips[new Random().nextInt(ips.length)];
        return ip;

    }

    //自定义一下域名
    public static String getDomins(){
        String[] domains = new String[]{
                "v1.go2yd.com",
                "v2.go2yd.com",
                "v3.go2yd.com",
                "v4.go2yd.com",
                "vmi.go2yd.com"
        };
        String domain = domains[new Random().nextInt(domains.length)];
        return domain;
    }

    //自定义一下流量
    //类型纠结---考虑在生产的是日志数据---数据产出都是字符串
    public static int getTraffic(){
        int i = new Random().nextInt(10000);

        return i;
    }



}
