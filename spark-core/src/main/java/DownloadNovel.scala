import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.webclients.MyVertx

object DownloadNovel {

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create file rdd")
    val sc = new SparkContext(sparkConf)
    // correlative path , base location project root path
    val rdd: RDD[String] = sc.textFile("spark-core/datas/chapters.txt")
    // /32_32339/13887819.html -> https://www.x23us.us/32_32339/13887819.html

    val value: RDD[String] = rdd.map(e => {
      val host = "www.x23us.us"
      val ext = """.*href='([^']+)'.*""".r
      val uri = e match {
        case ext(u) => u
        case _ => ""
      }
      uri
    }).filter(e => !e.equals(""))
    value.saveAsTextFile("spark-core/datas/dagengren.txt")
  }

}