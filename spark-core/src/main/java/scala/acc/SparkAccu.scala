package scala.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object SparkAccu {
  def main(args: Array[String]): Unit = {
    //connection
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    val sum: LongAccumulator = sc.longAccumulator("sum")
    rdd.foreach(num =>
      sum.add(num)
    )
    println(sum.value)
    sc.stop()
  }
}
