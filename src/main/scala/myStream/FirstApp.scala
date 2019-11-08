package myStream

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
/**
  * Created by Administrator on 2019/9/25.
  */
object FirstApp {
  def main(args: Array[String]): Unit = {
    //1.获取执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //2.获取数据从socket
    val data = env.socketTextStream("localhost",9000)
    //3.算子
    data.flatMap(x=>{x.split(",") filter(_.nonEmpty)})
        .map((_,1))
      .keyBy(0)
      .timeWindow(Time.seconds(10))
        .sum(1).print()
    env.execute("FirstApp")
  }



}
