package org.training.spark.core

import java.util.StringTokenizer

import org.apache.spark.ml.linalg.DenseVector
import org.apache.spark.{SparkConf, SparkContext}

object LogisticRegrassion {
  val D = 100
  val ITERATIINS = 10000

  class DataPoint(x: DenseVector, y: Int) {

  }

  /*def parsePoint(line: String): DataPoint = {
    val tok = new StringTokenizer(line, "")
    var y = tok.nextToken.toDouble
    val x = new Array[Double](D)
    var i = 0
    while (i < D) {
      x(i) = tok.nextToken().toDouble
      i += 1
    }
    DataPoint(new DenseVector(x), y)
  }

  def calcGradient(p: DataPoint, weight: Vector): Int =
    (1 /( 1 + exp(-p.y * weight.dot(p.x))) - 1) * p.x * p.y

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("LogisticRegression").setMaster("local[1]")
    val sc = new SparkContext(conf)
    val dataSet = sc.textFile("")

    dataSet.map(parsePoint _).cache()

    var weight = Vector.random()

    for (1 to ITERATIINS) {
      val gradient = dataSet.map(p => calcGradient(p, weight))
        .reduce(_ + _)
      weight -= gradient
    }

    println("Result: " + weight)

  }*/
}
