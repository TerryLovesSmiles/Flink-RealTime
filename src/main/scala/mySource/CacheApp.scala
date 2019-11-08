package mySource

import org.apache.commons.io.FileUtils
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
/**
  * Created by Administrator on 2019/9/25.
  */
object CacheApp {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    myCache(env)
  }


  def myCache(env: ExecutionEnvironment): Unit ={
    //测试文件注册缓存,然后从缓存中取出数据
    //1,将文件内容注入到缓存中
    env.registerCachedFile("D:\\Text\\aaa.txt","localFile",executable = true)
    //
    val list = List(1,2,3,4)
    val data = env.fromCollection(list)

    data.map(new RichMapFunction[Int,Int] {
      override def open(parameters: Configuration): Unit = {
        //从缓存中拿数据
        val result = getRuntimeContext.getDistributedCache.getFile("localFile")
        val ss = result.toString
        val string = FileUtils.readFileToString(result,"utf-8")
        println("我的输出:"+  string)
      }
      override def map(value: Int): Int = {
        value
      }

    }).print()

  }

}
