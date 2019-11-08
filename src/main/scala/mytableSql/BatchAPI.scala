package mytableSql

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
/**
  * Created by Administrator on 2019/9/26.
  */
object BatchAPI {
  def main(args: Array[String]): Unit = {
    //获取执行环境
    val env = ExecutionEnvironment.getExecutionEnvironment
    //2 源
    val data = env.readCsvFile[Sales]("D:\\AAAAAAAAAAAAAAAA\\Flink\\flink-d\\sales.csv",ignoreFirstLine = true)
    //3 算子
    data.print()

  }

  //样例类
  case class Sales(transactionId:Int,customerId:Int,itemId:Int,amountPaId:Int)
}
