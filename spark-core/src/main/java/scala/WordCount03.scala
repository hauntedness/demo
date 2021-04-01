package scala

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount03 {
  def main(args: Array[String]): Unit = {
    //connection
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)
    //E:\ProgramData\workspace\IdeaProjects\demo\spark-core\src\main\resources\1.txt
    val path: String = "E:\\ProgramData\\workspace\\IdeaProjects\\demo\\spark-core\\datas"
    val lines: RDD[String] = sc.textFile(path)
    val words: RDD[String] = lines.flatMap(_.split(" "))
    val wordToOne = words.map(
      word => (word, 1)
    )
    // Spark
    val wordToCount: RDD[(String, Int)] = wordToOne.reduceByKey((x, y) => x + y)
    val array = wordToCount.collect()
    array.foreach(println)
    sc.stop()
  }
}
