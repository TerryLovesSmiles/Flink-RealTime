package myStream

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
/**
  * Created by Administrator on 2019/9/26.
  */
object TimeAndWindows {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val data = env.socketTextStream("localhost",9000)

    //算子
    data.flatMap(_.split(","))
        .map((_,1))
      .keyBy(0)
      //.timeWindow(Time.seconds(60)) //滚动窗口, 不重叠
        .timeWindow(Time.minutes(1),Time.seconds(50))
        .sum(1).print()

    env.execute("TimeAndWindows")
  }
}

/*aaa,aaa,aaa,vv,v,vv,vv,
dd,dd,cc,vv,aa,dd,aaa,cc,vv*/
