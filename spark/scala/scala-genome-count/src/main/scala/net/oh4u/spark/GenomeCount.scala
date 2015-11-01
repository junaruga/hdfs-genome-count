package net.oh4u.spark

import scala.collection.mutable.HashMap
import scala.collection.mutable.Map

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext


/**
 * Created by jun.aruga on 29/10/15.
 */
object GenomeCount {
  val GENOME_FILE = "/tmp/genome.txt"

  def parseLine(line: String): Map[String, Int] = {
    val map = new HashMap[String, Int]
    for (i <- 0 to line.length() - 1) {
      val c = line.charAt(i).toString()
      if (map.contains(c)) {
        val n = map.get(c).get
        map.put(c, n + 1)
      }
    }

    map
  }

  def main(args: Array[String]): Unit ={
    val conf = new SparkConf().setAppName("GenomeCount").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val lines = sc.textFile(GENOME_FILE)

    // TODO: implement
    val result = lines.map(parseLine _).reduce((a, b) => a ++ b)

    println(result.toString())

    // val count = sc.parallelize(Array(1,2,3,4,5)).count()
    // println(count)
    System.exit(0)
  }
}
