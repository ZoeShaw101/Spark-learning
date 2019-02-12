package org.training.spark.core


import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

/**
  * 并行估算pi
  * Area1 = x * x , Area2 = Pi * (x / 2) * (x / 2)
  * Area1 / Area2 = 4 / pi
  * 4 / pi = x / y  => pi = 4 * y / x
  */

object SparkPi {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Spark Pi").setMaster("local[1]");
    val sc = new SparkContext(conf);
    val slices = if (args.length > 0) args(0).toInt else 2;
    val areaSqure = 100000 * slices;

    //并行估算areaCircle的值：也就是撒areaSqure这么多个点，求落在圆内的多少个点，就近似等于圆的面积
    val areaCircle = sc.parallelize(1 to areaSqure, slices).map{i =>
      val x = new Random().nextInt() * 2 - 1
      val y = new Random().nextInt() * 2 - 1
      if (x * x + y * y < 1) 1 else 0
    }.reduce(_ + _)

    println("Pi is roughly " + 4.0 * areaCircle / areaSqure)
  }
}
