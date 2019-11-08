package myStream

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._
/**
  * Created by Administrator on 2019/9/25.
  */
object SourceApp {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

      //sourceFile(env)
    //sourceConllect(env)
    //myNoPMSource(env)
    //myPMSource(env)
    myRichPMSource(env)
  }

  //基于文件
  def sourceFile(env:StreamExecutionEnvironment): Unit ={
    val data = env.readTextFile("D:\\AAAAAAAAAAAAAAAA\\Flink\\flink-a\\abc.txt")
    data.print()
    env.execute("SourceApp")
  }

  //基于集合
  def sourceConllect(env:StreamExecutionEnvironment): Unit ={
    val list = List(1,2,3,"11","china","wakanda forever",21,"gagaga")

    val data = env.fromCollection(list)

    data.print()
    env.execute("SourceApp")
  }


  //自定义数据源---非并存
  def myNoPMSource(env:StreamExecutionEnvironment): Unit ={
    val data = env.addSource(new MyNoPMSource).setParallelism(1)//非并行,并行度设为>=2的情况 会报错
    data.print()
    env.execute("SourceApp")


  }


  def myPMSource(env:StreamExecutionEnvironment): Unit ={
    val data = env.addSource(new MyPMSource).setParallelism(1)//非并行,并行度设为>=2的情况 会报错
    //data.print()
    println("~~~~~~~~~~")
    data.filter(x=>x%2==0).print()
    println("~~~~~~~~~~")
    env.execute("SourceApp")


  }

  def myRichPMSource(env:StreamExecutionEnvironment): Unit ={
    val data = env.addSource(new MyRichPMSource).setParallelism(1)//非并行,并行度设为>=2的情况 会报错
    //data.print()
    println("~~~~~~~~~~")
    data.filter(x=>x%2==0).print()
    println("~~~~~~~~~~")
    env.execute("SourceApp")


  }

}
