package MyWork

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.streaming.api.scala._

import scala.collection.mutable.ListBuffer
/**
  * Created by Administrator on 2019/9/25.
  */
object TestApp {
  def main(args: Array[String]): Unit = {
    //val env = StreamExecutionEnvironment.getExecutionEnvironment
    val env = ExecutionEnvironment.getExecutionEnvironment
    val list = new ListBuffer[String]
    val data = env.readTextFile("D:\\AAAAAAAAAAAAAAAA\\Flink\\task\\taobaoData.txt")

    /*list.append(data.flatMap(_.split("/n")))*/
    //data.map(_.split(",")(1)).map(_.split("'t")).groupBy().sum(1).print()
    //data.map(_.split(","))
    //data.map(_.split(","))
    //data.map(x=>((x.split(",")(0)),1)).groupBy(0).sum(1).groupBy(1).sortGroup(1,Order.DESCENDING).first(3).print()

    //用户ID、商品ID、商品类目ID、行为类型和时间戳
    val result = data.filter(x=>x.contains("pv")).map(x=>(x.split(",")(0),x.split(",")(1),x.split(",")(2),x.split(",")(3),x.split(",")(4),1))
    result.groupBy(1).sum(5).groupBy(4).sortGroup(5,Order.ASCENDING).first(3).print()
  }
}

//case class Taotao(userId:String,itemId:String,itemTypeId:String,action:String,time:String)
//
case class TT1(itemId:String,count:Int)