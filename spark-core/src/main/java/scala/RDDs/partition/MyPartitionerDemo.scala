package scala.RDDs.partition

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object MyPartitionerDemo {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc: SparkContext = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd: RDD[String] = sc.makeRDD(List("NBA", "WNBA", "CBA", "SOMEA"))
    val rdd1: RDD[(String, String)] = rdd.map(x => (x, "aaa"))
    val rdd2: RDD[(String, String)] = rdd1.partitionBy(new MyPartitioner())
    rdd2.saveAsTextFile("spark-core/output/"+System.currentTimeMillis())
    sc.stop()
  }
}

class MyPartitioner extends Partitioner {
  override def numPartitions: Int = 3

  override def getPartition(key: Any): Int = {
    key match {
      case "NBA" => 0
      case "WNBA" => 1
      case _ => 2
    }
  }
}