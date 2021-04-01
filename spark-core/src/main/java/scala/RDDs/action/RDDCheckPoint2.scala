package scala.RDDs.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDCheckPoint2 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc: SparkContext = new SparkContext(sparkConf)
    sc.setCheckpointDir("spark-core/check-point")
    // correlative path , base location project root path
    val rdd1: RDD[String] = sc.makeRDD(List("hello scala", "hello spark"))
    val rdd2: RDD[String] = rdd1.flatMap(_.split(" "))
    val rdd3: RDD[(String, Int)] = rdd2.map(word => (word, 1))
    // 一般放到HDFS 系统中
    // 为了提高效率, 往往说cache 一起使用
    //rdd3.cache()
    rdd3.checkpoint()
    println(rdd3.toDebugString)
    val rdd4: RDD[(String, Int)] = rdd3.reduceByKey(_ + _)
    rdd4.collect().foreach(println)
    println("************************")
    println(rdd3.toDebugString)
    sc.stop()
  }
}

