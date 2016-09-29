package com.github.taivokasper.httpchoker

import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import okio.BufferedSink
import org.slf4j.LoggerFactory
import java.security.SecureRandom
import kotlin.concurrent.thread

open class HttpChoker(val arguments: Args) {
  val log = LoggerFactory.getLogger(HttpChoker::class.java)

  val JSON = MediaType.parse("application/json; charset=utf-8")
  val client = OkHttpClient()

  fun slowPost(): String {
    val body = InfiniteBody(arguments, JSON)
    val request = Request.Builder()
        .url(arguments.url)
        .method(arguments.requestMethod, body)
        .build()
    val response = client.newCall(request).execute()
    return response.body().string()
  }
}

open class InfiniteBody(val arguments: Args, val mediaType: MediaType) : RequestBody() {
  val log = LoggerFactory.getLogger(HttpChoker::class.java)
  val random = SecureRandom()

  override fun contentType(): MediaType {
    return mediaType
  }

  override fun writeTo(sink: BufferedSink?) {
    while (true) {
      val byte = random.nextInt()
      log.info("Writing byte {}", byte)
      sink?.writeByte(byte)
      sink?.flush()

      Thread.sleep(arguments.sleepPeriod)
    }
  }

  override fun contentLength(): Long {
    return -1
  }
}

data class Args(val threads: Int, val sleepPeriod: Long, val requestMethod: String, val url: String)

fun main(args: Array<String>) {
  val arguments = Args(args[0].toInt(), args[1].toLong(), args[2], args[3])

  IntRange(0, arguments.threads).forEach {
    thread(true, false, HttpChoker::class.java.classLoader, "Choker thread " + it) {
      HttpChoker(arguments)
          .slowPost()
    }
  }
}
