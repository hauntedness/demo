package scala.RDDs
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CreateRDD {
  def main(args: Array[String]): Any = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create rdd")
    val sc = new SparkContext(conf)
    val seq: Seq[Int] = Seq[Int](1, 2, 3, 4)
    val rdd: RDD[Int] = sc.parallelize(seq)
    rdd.collect().foreach(println)
    return sc.stop()
  }
}
