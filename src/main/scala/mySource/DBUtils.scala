package mySource

/**
  * Created by Administrator on 2019/9/24.
  */
object DBUtils {
  //模拟一个数据库连接工具类
  //获取连接
  def getConnection(): String ={
    "得到一个连接"+ (Math.random()*10).toInt
  }

  //模拟一个返回连接(关闭连接)
  def returnConnection(connection:String):String={

    "关闭连接"
  }

}
