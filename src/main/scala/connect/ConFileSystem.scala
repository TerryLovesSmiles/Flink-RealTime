package connect

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.fs.StringWriter
import org.apache.flink.streaming.connectors.fs.bucketing.{BucketingSink, DateTimeBucketer}

/**
  * Created by Administrator on 2019/9/27.
  */
object ConFileSystem {
  def main(args: Array[String]): Unit = {
    //读数据
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val data = env.socketTextStream("localhost",9000)
    //写出去
    val path = "D:\\Text\\aaaaa"
    val sink = new BucketingSink[String](path)

    sink.setBucketer(new DateTimeBucketer[String]("yyyy-MM-dd--HHmm"))
    sink.setWriter(new StringWriter[String]())
    sink.setBatchSize(1024*1024*400)
    sink.setBatchRolloverInterval(20*1000)

    data.addSink(sink)

    env.execute("ConFileSystem")
  }

}
