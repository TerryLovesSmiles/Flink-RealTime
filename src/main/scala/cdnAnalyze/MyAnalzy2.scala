package cdnAnalyze

import java.text.SimpleDateFormat
import java.util.Properties

import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

import scala.collection.mutable.ArrayBuffer


import scala.collection.mutable

/**
  * Created by Administrator on 2019/9/28.
  */
object MyAnalzy2 {
  def main(args: Array[String]): Unit = {
    //第一部分
    //接收来自kafka的数据(源)

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "192.168.198.121:9092")
    properties.setProperty("group.id", "test")

    //接收kafka数据
    val stream = env.addSource(new FlinkKafkaConsumer[String]("aaa",new SimpleStringSchema(),properties))

    //打印测试
    //stream.print()

    //第二部分 -- 数据清洗
    val data2 = stream.map(x=>{
      //规划, 取 级别  时间(Long)  ip 域名 流量(Long)
      val data = x.split("\t")
      val level = data(2)
      val timestr = data(3)
      //时间转成long
      val format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val time = format.parse(timestr).getTime
      val ip = data(4)
      val domin = data(5)
      val traffic = data(6).toLong

      (level,time,ip,domin,traffic)

    }).filter(_._2!=0).filter(_._1=="F")
      .map(x=>{
        (x._2,x._4,x._5) //时间--域名--流量
      })

    //打印测试
    //data2.print()

    //新第三部分;   数据源日志&&数据库---合并
    val mysqldata = env.addSource(new MySqlSource)
   /* var newData3 = data2.connect(mysqldata).flatMap(new CoFlatMapFunction[(Long,String,Long),mutable.HashMap[String,String],(Long,String,Long,String)] {override def flatMap2(value: mutable.HashMap[String, String], out: Collector[(Long, String, Long, String)]): Unit = ???
      //map
      var map = mutable.HashMap[String,String]()
      //日志
      override def flatMap1(in1: (Long, String, Long), collector: Collector[(Long, String, Long, String)]): Unit = {
        //联合到一起,,输出就ok
        val userid = map.getOrElse(in1._2,"默认用户")

        //输出
        collector.collect((in1._1,in1._2,in1._3,userid))

      }

      //数据库的
      override def flatMap2(in2: mutable.HashMap[String,String], collector: Collector[(Long, String, Long, String)]): Unit = {
        map = in2

      }
    })
    newData3.print()*/








    //第三步 (eventTime结合水印解决乱序问题)
    //为数据分配时间戳和水印
      /*/*val data3 = data2.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[(Long,String,Long)] {
            //最大容忍乱序值
            val maxOutOfOrderness = 3500L // 3.5 seconds
            //当前最大时间戳
            var currentMaxTimestamp: Long = _
            //取水印
            override def getCurrentWatermark: Watermark = {
              new Watermark(currentMaxTimestamp - maxOutOfOrderness)

            }
            //取时间戳
            override def extractTimestamp(element: (Long, String, Long), previousElementTimestamp: Long): Long = {
              val timestamp = element._1
              currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp)
                timestamp
            }*/
      //第四步: WindowFunction
    }).keyBy(1) //(x._2,x._4,x._5) Tuple结构
        .window(TumblingEventTimeWindows.of(Time.seconds(60))) //窗口一分钟 按Event
                                                      //输出时,时间为了方便观察,我们将它再次转成String类型yyyy-MM-dd HH:mm:ss
          .apply(new WindowFunction[(Long,String,Long),(String,String,Long),Tuple,TimeWindow] {
      override def apply(key: Tuple, window: TimeWindow, input: Iterable[(Long, String, Long)], out: Collector[(String, String, Long)]): Unit = {
        //输入(迭代器)---一直在不断地输入
        //求每一分钟内到达数据按域名分组的流量总和
        //获取迭代器
        var sum = 0l
        val iterator = input.iterator

        //整一个数据,把所有生成的时间放入数组中
        var times = ArrayBuffer[Long]()
        while(iterator.hasNext){
          val tuple = iterator.next()
          //流量累加求和
          sum += tuple._3
          //从这里可以拿到时间
          times.append(tuple._1)
        }

        //取到域名
        val domin = key.getField(0).toString

        //将时间再转成String结构
        val time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(times.min)

        //输出(String,String,Long)(时间,域名,总流量)
        out.collect(time,domin,sum)

      }
    })*/



   //data3.print()
    //第五步:  写入es  联合kibana


    /*val httpHosts = new java.util.ArrayList[HttpHost]
    //连接es ,
    httpHosts.add(new HttpHost("192.168.198.121", 9200, "http"))
    //httpHosts.add(new HttpHost("10.2.3.1", 9300, "http"))

    val esSinkBuilder = new ElasticsearchSink.Builder[(String,String,Long)](
      httpHosts,
      new ElasticsearchSinkFunction[(String,String,Long)] {
        def createIndexRequest(element: (String,String,Long)): IndexRequest = {
          val json = new java.util.HashMap[String, String]
          json.put("domain", element._2)
          json.put("traffics", element._3.toString)
          json.put("time", element._1)

          //防止数据重复,自己整一个id(每条数据的唯一标识)
          val id = element._1+element._2  //时间+域名   当做每条数据的唯一标识


          return Requests.indexRequest()
            .index("cdn")
            .`type`("traffic")
              .id(id)
            .source(json)
        }

        override def process(t: (String, String, Long), runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(t))
        }


      }
    )

    // configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
    esSinkBuilder.setBulkFlushMaxActions(1)*/



    // finally, build and add the sink to the job's pipeline
    //data3.addSink(esSinkBuilder.build)




  env.execute("MyAnalzy")

  }
}

/*
重要的点:在数据处理过程中,最终和打印日志信息核对正确,多亏了环境在EventTime下工作

在流处理里,时间的正确处理,对于最终的结果是否正确统计,有着至关重要的作用

*/

