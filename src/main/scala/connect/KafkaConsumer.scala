package connect




import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.api.scala._


/**
  * Created by Administrator on 2019/9/27.
  */
object KafkaConsumer {  //消费者
def main(args: Array[String]): Unit = {
   val env = StreamExecutionEnvironment.getExecutionEnvironment

  val properties = new Properties()
  properties.setProperty("bootstrap.servers", "hadoop000:9092")
  // only required for Kafka 0.8
  //properties.setProperty("zookeeper.connect", "localhost:2181")
  properties.setProperty("group.id", "test")
  val stream = env
    .addSource(new FlinkKafkaConsumer[String]("aaa", new SimpleStringSchema(), properties))
    .print()

  env.execute("KafkaConsumer")

}

}
