package scala.RDDs

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CreateRDDFromFile {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd: RDD[String] = sc.textFile("spark-core/datas")
    rdd.collect().foreach(println)
    sc.stop()
  }
}
