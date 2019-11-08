package MyWork

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Administrator on 2019/9/25.
  */
object TestApp2 {
  def main(args: Array[String]): Unit = {
    val file = Source.fromFile("D:\\AAAAAAAAAAAAAAAA\\Flink\\task\\taobaoData.txt")
    val list = new ListBuffer[Taotao]
    for(line<-file.getLines()){
     val aa = line.split(",")

      list.append(new Taotao(aa(0),aa(1),aa(2),aa(3),aa(4)))
    }




  }
}
case class Taotao(userId:String,itemId:String,itemTypeId:String,action:String,time:String)