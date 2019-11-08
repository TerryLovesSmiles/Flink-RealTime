package myStream

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.scala._
/**
  * Created by Administrator on 2019/9/26.
  */
object myReduceFunction {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val data = env.socketTextStream("localhost",9000)

    /*data.map(x=>(1,x.toInt))
      .keyBy(0)
      .timeWindow(Time.seconds(5))
      .reduce((v1,v2)={
        print(v1+"---"+v2)
        (v1._1,v1._2+v2._2)
      })*/
  }
}
