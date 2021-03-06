/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kafka

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}


object FileWordCount {
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("StreamingTest").setMaster("local[2]")
    sparkConf.set("spark.testing.memory", "471859200")
    // Create the context
    val sc = new StreamingContext(sparkConf, Seconds(2))  // 2s 处理一个批次

    // Create the FileInputDStream on the directory and use the
    // stream to count words in new files created
    val file = "file:///c:\\stream"
//
    val lines = sc.textFileStream(file)
    val words = lines.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _)
//    words.print(2)

//    words.saveAsTextFiles("file:///c:\\streamOut")

    words.foreachRDD(rdd =>
    {
      rdd.foreachPartition( p =>{
        p.foreach(println)
      })
//      rdd.foreach(println)
    })
    sc.start()
    sc.awaitTermination()
  }
}
// scalastyle:on println
