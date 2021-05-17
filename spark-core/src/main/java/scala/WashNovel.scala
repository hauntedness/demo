package scala

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object WashNovel {
  def main(args: Array[String]): Unit = {
    //connection
    val sparkConf = new SparkConf().setMaster("local[1]").setAppName("WashNovel")

    val sc = new SparkContext(sparkConf)
    //E:\ProgramData\workspace\IdeaProjects\demo\spark-core\src\main\resources\1.txt
    val path: String = """C:\Users\Administrator\Desktop\大奉打更人.txt"""
    val lines: RDD[String] = sc.textFile(path)
    val value: RDD[String] = lines.map(line => line.replace("<title>", "").replace("</title>", "")
      .replace("<div id=\"content\" name=\"content\">", "")
      .replace("修真小说", "").replace("顶点小说", ""))

    value.saveAsTextFile( """C:\Users\Administrator\Desktop\大奉打更人2.txt""")
    sc.stop()
  }
}
