package mySource

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration


/**
  * Created by Administrator on 2019/9/24.
  */
object SourceApp {
  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    //调用1
    //sourceCollection(env)
    //调用2
    //sourceFile(env)
    //调用3
    //sourceCSV(env)
    //调用4
    //sourceFile2(env)
  }

  //数据源的获取

  //5.从压缩文件
  def sourcePress(): Unit ={

  }



  //4.递归文件夹
  def sourceFile2(env:ExecutionEnvironment): Unit ={
    val configuration = new Configuration()
    configuration.setBoolean("recursive.file.enumeration",true)
    env.readTextFile("D:\\Text\\").withParameters(configuration).print()
  }




  //3.从CSV(Excel表格)中读
 /* def sourceCSV(env: ExecutionEnvironment): Unit ={
    val data = env.readCsvFile[MyPeople]("D:\\Text\\people.csv",ignoreFirstLine = true)
    data.print()
  }*/

  case class MyPeople(name:String,age:Int,job:String)


  //2.从文件中获取
  def sourceFile(env: ExecutionEnvironment): Unit ={
    //给文件/文件夹都可以
    //env.setParallelism(2) //设置并行度为1,按顺序读
    val data = env.readTextFile("D:\\Text")
    data.print()
  }



  //1.从集合中获取
  def sourceCollection(env: ExecutionEnvironment): Unit ={
    //val data = 1 to 10
    //val data = Tuple4(1,2,3,4)//元组不可以
    //val data = (1,2,3,4)//不可以
    val data = List(1,23.44,5,65.76,7,32,465)
    import org.apache.flink.api.scala._ //引入隐式转换
    env.fromCollection(data).print()
  }


}
