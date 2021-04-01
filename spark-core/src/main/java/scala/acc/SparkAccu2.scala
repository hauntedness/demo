package scala.acc

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object SparkAccu2 {

  def main(args: Array[String]): Unit = {
    //connection
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[String] = sc.makeRDD(List("spark", "scala", "hello", "hello"))
    val wcAcc = new MyAccumulator()
    sc.register(wcAcc, "myacc")
    rdd.foreach(word =>
      wcAcc.add(word)
    )
    println(wcAcc.value)
    sc.stop()
  }

  class MyAccumulator extends AccumulatorV2[String, mutable.Map[String, Long]] {

    private val wcMap = mutable.Map[String, Long]()

    override def isZero: Boolean = {
      wcMap.isEmpty
    }

    override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = {
      new MyAccumulator()
    }

    override def reset(): Unit = {
      wcMap.clear()
    }

    override def add(word: String): Unit = {
      this.wcMap.update(word, this.wcMap.getOrElse(word, 0L) + 1)
    }

    override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {
      val map2 = other.value
      map2.foreach {
        case (word, count) => {
          this.wcMap.update(word, this.wcMap.getOrElse(word, 0L) + count)
        }
      }
    }

    override def value: mutable.Map[String, Long] = wcMap
  }
}
