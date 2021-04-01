package scala.RDDs.action

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object RDDPersistent2 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc: SparkContext = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd: RDD[String] = sc.textFile("spark-core/datas",2)
    val rdd2: RDD[String] = rdd.flatMap(_.split(" "))
    // 对象会重用但是数据无法重用,所以代码会执行两次以为RDD4和RDD5拉取数据
    val rdd3: RDD[(String, Int)] = rdd2.map(word=>{
      println("--------------------")
      (word,1)
    })
    // cache 之后数据也可以重用
    rdd3.persist(StorageLevel.DISK_ONLY) // rdd3.cache() 没有参数
    val rdd4: RDD[(String, Int)] = rdd3.reduceByKey(_ + _)
    val rdd5: RDD[(String, Iterable[Int])] = rdd3.groupByKey()
    rdd4.foreach(println)
    rdd5.foreach(println)

    sc.stop()
  }
}

