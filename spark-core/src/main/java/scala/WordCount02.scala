package scala
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object WordCount02 {
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
    val wordGroup: RDD[(String, Iterable[(String, Int)])] = wordToOne.groupBy(
      t => t._1
    )
    val wordToCount = wordGroup.map {
      case (word, list) => {
        list.reduce((t1, t2) => {
          (t1._1, t1._2 + t2._2)
        })
      }
    }
    val array = wordToCount.collect()
    array.foreach(println)
    sc.stop()
  }
}
