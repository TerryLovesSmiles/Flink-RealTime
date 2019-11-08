package myStream

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._

/**
  * Created by Administrator on 2019/9/25.
  */
object TransformationsApp {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //myUnion(env)
    mySplitAndSelect(env)
  }

  //拆分和选择算子
  def mySplitAndSelect(env:StreamExecutionEnvironment): Unit ={
    env.setParallelism(1)
    val d1 = env.addSource(new MyPMSource)
    //拆分
    val data = d1.split(x=>(x%2) match {
        case 0 =>List("even")  //把偶数放在这个集合中,名字很重要,后边选择的时候会用到这个名字
        case 1 =>List("odd")
    })

    //选择
    //data.select("even").print()//输出偶数
    //data.select("odd").print()//输出计数
    data.select("even","odd")//输出奇数和偶数
    env.execute("TransformationsApp")
  }




  //联合
  def myUnion(env:StreamExecutionEnvironment): Unit ={
    //n合1
    /*val d1 = env.addSource(new MyNoPMSource)*/
    val d1 = env.socketTextStream("localhost",9000)
    val d2 = env.addSource(new MyNoPMSource)

    d1.union(d2).print()

    env.execute("TransformationsApp")
  }


}
