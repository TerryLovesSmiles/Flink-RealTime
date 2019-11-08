package mySource

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

import scala.collection.mutable.ListBuffer

/**
  * Created by Administrator on 2019/9/24.
  */
object TransformationsApp {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    //tflatMap(env)
    //tFilter(env)
    //tMapPartation(env)
    //tFist(env)
    //tWC(env)
    //tDistinct(env)
    //tOutJoin(env)
    tCross(env)
  }

  //算子
  def temp(env:ExecutionEnvironment): Unit ={
    val data = 1 to 10
    val result = env.fromCollection(data)
    //作用上算子
    result.map((x:Int)=>x+1).print()
    //result.map(x=>x+1).print()
    //result.map(_+1).print()
  }

  def tflatMap(env: ExecutionEnvironment): Unit ={
    val data = env.readTextFile("D:\\Text\\aaa.txt")
    data.flatMap(x=>x.split("\t")).print()
  }

  def tFilter(env:ExecutionEnvironment): Unit ={
    //val data = 1 to 10
    val result = env.fromCollection(List(1,2,3,4,5,7,8,1,3,5,5))
    result.filter(x=>x>4).print()

  }

  //mapPartation
  def tMapPartation(env:ExecutionEnvironment): Unit ={
    //先用map
    //模拟将100个数据存入数据库
    val data = 1 to 100
    val result = env.fromCollection(data)
    //map是一个数据产生一个新数据
    //result.print()
    //模拟将100个数据存入数据库
    /*result.map(x=>{
      //获取连接
      val str = DBUtils.getConnection()
      //模拟存入数据库
      println(str + "存入数据库")
      //关闭(返回连接)
      DBUtils.returnConnection("1")
    }).print()*/
    //上述代码在存入数据库100个数据的操作中,获取了100次连接、关闭了100次连接，不合理

    result.mapPartition(x=>{
      //获取连接
      val str = DBUtils.getConnection()
      //模拟存入数据库
      println(str + "存入数据库")
      //关闭(返回连接)
      DBUtils.returnConnection("1")
      x
    }).setParallelism(6).print()
    //所有操作使用了获取1次连接 setParallelism(1)
    //所有操作使用了获取5次连接 setParallelism(5)

    //map和mapPartition区别 ,后者多了分区操作
    //(map每个元素用一个连接   mapParallelism每一个区用一个连接)

  }


  def tFist(env:ExecutionEnvironment): Unit ={
    //构建数据
    val list = new ListBuffer[(String,Int)]
    list.append(("a",100))
    list.append(("a",200))
    list.append(("b",-20))
    list.append(("b",-30))
    list.append(("c",1))
    list.append(("c",2))
    list.append(("c",3))
    val result = env.fromCollection(list)
    //on算子
    //result.first(3).print()//集合的前两个
    //result.groupBy(0).first(2).print()//每一组的前两个
    result.groupBy(0).sortGroup(1,Order.ASCENDING).first(2).print()
  }

  def tWC(env: ExecutionEnvironment): Unit ={
    val data = env.readTextFile("D:\\AAAAAAAAAAAAAAAA\\Flink\\flink-a\\abc.txt")

    //wc统计
    //data.print()
    /*hello
    hello	welcome
    hello	world	welcome*/
    //第一步:1 变 n
    //data.flatMap(x=>x.split("\t")).print()
    /*hello
    world
    welcome
    hello
    hello
    welcome*/
    //第二步:(hello,1) (word,1) (welcome,1).......
    //data.flatMap(x=>x.split("\t")).map(x=>(x,1)).print()
    /*(hello,1)
    (welcome,1)
    (hello,1)
    (hello,1)
    (world,1)
    (welcome,1)*/
    //第三步 分组 求和
    data.flatMap(x=>x.split("\t")).map(x=>(x,1)).groupBy(0).sum(1).print()

  }


  def tDistinct(env:ExecutionEnvironment): Unit ={
    val list = List(1,2,3,45,654,34,3,2,1,54,45,3,2,2)
    //源
    val data = env.fromCollection(list)
    //算子
    data.distinct().print()

  }

  def tJoin(env: ExecutionEnvironment): Unit ={
    val data1 = new ListBuffer[(Int,String)]
    data1.append((1,"小白"))
    data1.append((2,"小花"))
    data1.append((3,"莉莉"))
    val data2 = new ListBuffer[(Int,String)]
    data2.append((1,"北京"))
    data2.append((1,"天津"))
    data2.append((1,"郑州"))
    data2.append((1,"武汉"))
    //join
    //源
    val result1 = env.fromCollection(data1)
    val result2 = env.fromCollection(data2)
    //算子 //join  两个整成一个
    result1.join(result2).where(0).equalTo(0)

  }

  def tOutJoin(env: ExecutionEnvironment): Unit ={
    val data1 = new ListBuffer[(Int,String)]
    data1.append((1,"小白"))
    data1.append((2,"小花"))
    data1.append((3,"莉莉"))
    data1.append((5,"如花"))
    val data2 = new ListBuffer[(Int,String)]
    data2.append((1,"北京"))
    data2.append((2,"天津"))
    data2.append((3,"郑州"))
    data2.append((4,"武汉"))
    env.setParallelism(1)

    val result1 = env.fromCollection(data1)
    val result2 = env.fromCollection(data2)
    //左连接
    /*result1.leftOuterJoin(result2).where(0).equalTo(0).apply((first,second)=>{
      if (second==null){
        (first._1,first._2,"")
      }else{
        (first._1,first._2,second._2)
      }

    }).print()*/

    //全连接
    result1.fullOuterJoin(result2).where(0).equalTo(0).apply((first,second)=>{
      if (second==null){
        (first._1,first._2,"")
      }else if(first==null){
        (first._1,"",second._2)
      }else{
        (first._1,first._2,second._2)
      }
    }).print()
  }


  def tCross(env: ExecutionEnvironment): Unit ={
    val data1 = new ListBuffer[(Int,String)]
    data1.append((1,"小白"))
    data1.append((2,"小花"))
    data1.append((3,"莉莉"))
    data1.append((5,"如花"))
    val data2 = new ListBuffer[(Int,String)]
    data2.append((1,"北京"))
    data2.append((2,"天津"))
    data2.append((3,"郑州"))
    data2.append((4,"武汉"))
    env.setParallelism(1)

    val result1 = env.fromCollection(data1)
    val result2 = env.fromCollection(data2)


    result1.cross(result2).print()
  }

}
