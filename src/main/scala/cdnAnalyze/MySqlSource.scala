package cdnAnalyze

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

import scala.collection.mutable

/**
  * Created by Administrator on 2019/10/8.
  */
class MySqlSource extends RichParallelSourceFunction[mutable.HashMap[String,String]]{
  var con:Connection=null
  var ps:PreparedStatement=null
  var rs:ResultSet=null

  //open 准备工作
  override def open(parameters: Configuration): Unit = {
    var username = "root"
    var pwd = "root"
    var url="jdbc:mysql://192.168.198.121:3306/cdn"
    var driverName="com.mysql.jdbc.Driver"
    //1.加载驱动
    Class.forName(driverName)
    //2.建立连接
    con = DriverManager.getConnection(url,username,pwd)
    //3.准备sql
    var sql = "select * from user_domain_config"
    //4.得到执行对象
    ps = con.prepareStatement(sql)

  }
  //核心方法
  override def run(ctx: SourceContext[mutable.HashMap[String, String]]): Unit = {
    //拿数据
    rs = ps.executeQuery()
    //准备一个map
    var map = mutable.HashMap[String,String]()
    while (rs.next()){
        //把域名当做建,把用户当做值
      map.put(rs.getString(3),rs.getString(2))
    }
    //输出
    ctx.collect(map)

  }
  //关闭资源
  override def close(): Unit = {
    if (rs!=null){
      rs.close()
    }
    if(ps!=null){
      ps.close()
    }
    if(con!=null){
      con.close()
    }

  }

  override def cancel(): Unit = {

  }


}
