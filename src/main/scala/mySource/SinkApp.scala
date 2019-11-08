package mySource

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
/**
  * Created by Administrator on 2019/9/25.
  */
object SinkApp {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //mySink(env)
  }

  //将数据输出
  def mySink(env: ExecutionEnvironment): Unit ={
    val data = List("z",1,3,4,5,"dd",2)
    //val result = env.fromCollection(data)
    //val result = env.fromCollection(data)
    val result = env.fromCollection(data)
    //算子
    val result1 = result.map(x=>x+"hello")

    //输出
    result1.writeAsText("D:\\Text\\ccc.txt")

    env.execute("SinkApp")


  }

}
