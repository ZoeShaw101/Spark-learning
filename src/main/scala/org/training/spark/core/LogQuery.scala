package org.training.spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 日志查询任务：统计每个用户在每台机器(ip)上查询(query)的次数和返回结果累积大小（byte）
  * 分析：key: 每个用户在每台机器上的query ，value：次数和结果累积大小(byte)
  */

object LogQuery {

  val apacheLogRegex =
    """^([\d.]+) (\S+) (\S+) \[([\w\d:/]+\s[+\-­‐]\d{4})\] "(.+?)" (\d{3}) ([\d\-­‐]+) "([^"]+)" "([^"]+)".*""".r

  def extractKey(line : String): (String, String, String) = {
    apacheLogRegex.findFirstIn(line) match {
      case Some(apacheLogRegex(ip, _, user, dateTime, query, status, bytes, referer, ua)) =>
        if (user != "\"-­‐\"") (ip, user, query)
        else (null, null, null)
      case _ => (null, null, null)
    }
  }

  def extractStats(line: String): Stats = {
    apacheLogRegex.findFirstIn(line) match {
      case Some(apacheLogRegex(ip, _, user, dateTime, query, status, bytes, referer, ua)) =>
        new Stats(1, bytes.toInt)
      case _ => new Stats(1, 0)
    }
  }

  class Stats(val count: Int, val numBytes: Int) extends Serializable {
    def merge(other: Stats) = new Stats(count + other.count, numBytes + other.numBytes)
    override def toString = "bytes=%s\tn=%s".format(numBytes, count)
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("LogQuery").setMaster("local[1]")
    val sc = new SparkContext(conf)
    val dataset = sc.textFile(args(0))

    dataset.map(line => (extractKey(line), extractStats(line)))
      .reduceByKey((a, b) => a.merge(b))
        .collect().foreach {
      case (user, query) => println("%s\t%s".format(user, query))
    }
  }
}
