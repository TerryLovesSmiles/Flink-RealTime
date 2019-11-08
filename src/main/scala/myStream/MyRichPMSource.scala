package myStream

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

/**
  * Created by Administrator on 2019/9/25.
  */
class MyRichPMSource extends RichParallelSourceFunction[Long]{
  var num = 0
  var isRun = true

  //核心方法
  override def run(sourceContext: SourceContext[Long]): Unit = {
    while(isRun){
      num+=1
      sourceContext.collect(num)//输出数据

      Thread.sleep(500)
    }
  }


  override def cancel(): Unit = {
    isRun=false

  }
}
