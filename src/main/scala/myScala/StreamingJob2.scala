/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package myScala

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * Skeleton for a Flink Streaming Job.
 *
 * For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
object StreamingJob2 {
  def main(args: Array[String]) {
    // set up the streaming execution environment   // 得到执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //以流的形式读取数据
    val value = env.socketTextStream("localhost",9000)
    //将数据打印,模拟六方式接收数据
    //value.print().setParallelism(1)//并行度写成1
    //操作算子----之前map算子中将结果映射成一个对象pojo,所以keyBy(字段)
    //value.flatMap(_.split(",")).map((_,1)).keyBy(0).timeWindow(Time.seconds(10)).sum(1).print().setParallelism(1)
    //操作算子
    value.flatMap(_.split(",")).map(WC(_,1)).keyBy(_.word).timeWindow(Time.seconds(10)).sum(1).print().setParallelism(1)

    //value.keyBy()


    /*
     * Here, you can start creating your execution plan for Flink.
     *
     * Start with getting some data from the environment, like
     *  env.readTextFile(textPath);
     *
     * then, transform the resulting DataStream[String] using operations
     * like
     *   .filter()
     *   .flatMap()
     *   .join()
     *   .group()
     *
     * and many more.
     * Have a look at the programming guide:
     *
     * http://flink.apache.org/docs/latest/apis/streaming/index.html
     *
     */

    // execute program
    env.execute("StreamingJob")
  }

  //样例类
  case class WC(word:String,count:Int)
}
