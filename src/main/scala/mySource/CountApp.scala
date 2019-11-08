package mySource

import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.api.scala._
/**
  * Created by Administrator on 2019/9/25.
  */
object CountApp {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    myCount(env)
  }

  def myCount(env: ExecutionEnvironment){
    val data = env.fromElements("aa","bb","cc","dd","中国")


    /*data.map(new RichMapFunction[String,Long] {
      var count = 0
      override def map(in: String): Long = {
        count+=1
        println("现在有"+count+"个元素")
        count
      }
    }).setParallelism(2).print()  //并行度为2*/      //该计数器不合理,会受并行度影响

    //定义计数器
    val counter = new LongCounter()

    val result = data.map(new RichMapFunction[String,String] {
      override def open(parameters: Configuration): Unit = {
        //准备工作一般放在这里
          //2注册计数器
        getRuntimeContext.addAccumulator("mycounter",counter)

      }

      override def map(value: String): String = {
        //计数
        counter.add(1)
        value+"hello"
      }
    })

    //把数据sink出去
    result.writeAsText("D:\\Text\\ccc.txt",WriteMode.OVERWRITE)

    val aa = env.execute("CountApp")

    //取计数器结果
    val num = aa.getAccumulatorResult[Long]("mycounter")

    println("计数的结果是"+num)

  }

}
