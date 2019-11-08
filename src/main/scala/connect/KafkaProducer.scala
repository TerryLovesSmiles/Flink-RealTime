package connect

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer

/**
  * Created by Administrator on 2019/9/27.
  */
object KafkaProducer {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val data = env.socketTextStream("localhost",9000)

    //val stream: DataStream[String] = ...

    val myProducer = new FlinkKafkaProducer[String](
      "192.168.198.121:9092",         // broker list
      "aaa",               // target topic
      new SimpleStringSchema)   // serialization schema

    // versions 0.10+ allow attaching the records' event timestamp when writing them to Kafka;
    // this method is not available for earlier Kafka versions
    myProducer.setWriteTimestampToKafka(true)

    data.addSink(myProducer)
    env.execute("KafkaProducer")
  }
}
