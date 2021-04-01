package scala.RDDs.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDReduce {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc: SparkContext = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 15, 6, 27, 8, 9, 10), 2)
    val i: Int = rdd.reduce(_ + _)
    println(i)
    sc.stop()
  }
}

