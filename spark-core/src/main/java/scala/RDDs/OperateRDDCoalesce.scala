package scala.RDDs

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object OperateRDDCoalesce {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc: SparkContext = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 3)
    // in default coalesce doesn't shuffle data into balanced dataset
    val rdd2: RDD[Int] = rdd.coalesce(2)
    rdd2.saveAsTextFile("spark-core/output/output2")
    val rdd3: RDD[Int] = rdd.coalesce(2,shuffle = true)
    rdd3.saveAsTextFile("spark-core/output/output3")
    sc.stop()
  }
}
