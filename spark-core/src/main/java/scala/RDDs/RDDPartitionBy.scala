package scala.RDDs

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object RDDPartitionBy {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc: SparkContext = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 15, 6, 27, 8, 9, 10), 2)
    val rdd2: RDD[(Int, Int)] = rdd.map(x=>(x,x))
    val rdd3: RDD[(Int, Int)] = rdd2.partitionBy(new HashPartitioner(2))
    rdd3.saveAsTextFile("spark-core/output/a2")
    sc.stop()
  }
}
