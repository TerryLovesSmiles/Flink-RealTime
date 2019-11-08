package myStream

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Created by Administrator on 2019/9/26.
  */
object myReduceFunction2 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val data = env.socketTextStream("localhost",9000)

    //在接受信息时,第一条进来,填充了匿名函数的v1参数,第二条进来,填充了匿名函数中的
    //v2参数,当两个参数都传入是,开始执行方法体,reduce的返回值又会作为盖匿名函数的v1
    //参数再次传入
    //等待第三条信息进来,填充匿名函数中的v2参数,再次执行 方法体....

    /*data.map(x=>(1,x.toInt))
      .keyBy(0)
      .timeWindow(Time.seconds(5))
      .reduce((v1,v2)={
        (v1._1,v1._2+v2._2)
      }).print().setParallelism(1)*/

    env.execute("myReduceFunction2")
  }
}
