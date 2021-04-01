package scala.RDDs.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDLineage2 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc: SparkContext = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd: RDD[String] = sc.textFile("spark-core/datas",1)
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))
    val rdd4: RDD[(String, Int)] = rdd3.reduceByKey(_ + _)
    print(rdd2.dependencies)
    print(rdd3.dependencies)
    print(rdd4.dependencies)

    sc.stop()
  }
}

