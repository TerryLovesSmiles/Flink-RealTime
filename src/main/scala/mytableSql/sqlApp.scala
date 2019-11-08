package mytableSql

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.types.Row
/**
  * Created by Administrator on 2019/9/26.
  */
object sqlApp {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    val btableEnv = TableEnvironment.getTableEnvironment(env)

    //注册一个表
    //读取数据
    val data = env.readCsvFile[Sales]("D:\\AAAAAAAAAAAAAAAA\\Flink\\flink-d\\sales.csv",ignoreFirstLine = true)
    //表对象
    val table = btableEnv.fromDataSet(data)
    //注册表
    btableEnv.registerTable("sales",table)

    //查询 ---- sql(sql语句就相当于算子)
    val result = btableEnv.sqlQuery("select customerId,sum(amountPaId) from sales group by customerId")

    btableEnv.toDataSet[Row](result).print()


  }

  case class Sales(transactionId:Int,customerId:Int,itemId:Int,amountPaId:Int)

}
