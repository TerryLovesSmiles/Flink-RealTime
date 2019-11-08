package cdnAnalyze

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
/**
  * Created by Administrator on 2019/10/8.
  */
object myTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val data= env.addSource(new MySqlSource)

    data.print()
    env.execute("myTest")
  }
}
