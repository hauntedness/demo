package scala.RDDs

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDIntersection {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc: SparkContext = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd1: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7), 2)
    val rdd2: RDD[Int] = sc.makeRDD(List(4, 5, 6, 7, 8, 9, 10), 2)
    val rdd3: RDD[Int] = rdd1.intersection(rdd2)
    // Can only zip RDDs with same number of elements in each partition
    // Can only zip RDDs with same number of partitions
    val rdd4 = rdd1.zip(rdd2)
    println(rdd3.collect().mkString(","))
    println(rdd4.collect().mkString(","))
    sc.stop()
  }
}
