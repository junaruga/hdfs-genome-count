import scala.collection.mutable._
import scala.io.Source

/**
 * Created by jun.aruga on 1/11/15.
 */
object GenomeCount {
  val GENOME_FILE = "/tmp/genome.txt"
  var queue = new Queue[HashMap[String, Int]]()

  def main(args: Array[String]): Unit = {
    for (line <- Source.fromFile(GENOME_FILE).getLines) {
      val countedMap = mapForLine(line)
      queue.enqueue(countedMap)
    }

    var totalMap = new HashMap[String, Int]
    while (queue.size > 0) {
      val map = queue.dequeue
      totalMap = reduce(totalMap, map)
    }

    printCountedMap(totalMap)

    println("Done")
  }

  def mapForLine(line: String): HashMap[String, Int] = {
    val countedMap = new HashMap[String, Int]

    for (c <- line) {
      val cStr = c.toString
      if (!(countedMap contains cStr)) {
        countedMap(cStr) = 0
      }
      countedMap(cStr) += 1
    }

    countedMap
  }

  def reduce(totalMap: HashMap[String, Int], map: HashMap[String, Int]):
    HashMap[String, Int] = {

    for (cStr <- map.keys) {
      if (!(totalMap contains cStr)) {
        totalMap(cStr) = 0
      }
      totalMap(cStr) += map(cStr)
    }

    totalMap
  }

  def printCountedMap(countedMap: HashMap[String, Int]): Unit = {
    var total = 0
    for (cStr <- countedMap.keys) {
      val count = countedMap(cStr)
      println(s"$cStr: $count")
      total += count
    }
    println(s"Total: $total")
  }

}


