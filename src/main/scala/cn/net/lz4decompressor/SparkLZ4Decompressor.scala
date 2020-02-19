package cn.net.lz4decompressor

import java.io.{FileInputStream, FileOutputStream}

import net.jpountz.lz4.LZ4BlockInputStream

object SparkLZ4Decompressor {
  final val BLOCK_SIZE = 2048
  val buffer = new Array[Byte](BLOCK_SIZE)

  private def decompress(lz4File: String, unzippedFile: String): Unit = {
    val is = new FileInputStream(lz4File)
    val os = new FileOutputStream(unzippedFile, true)
    val bis = new LZ4BlockInputStream(is)
    val start = System.currentTimeMillis()
    try {
      Iterator.continually(bis.read(buffer))
        .takeWhile(-1 !=)
        .foreach {
          len =>
            os.write(buffer, 0, len)
        }
    } finally {
      os.close()
      bis.close()
    }
    val end = System.currentTimeMillis()
    println(s"Decompression took ${end-start} ms")
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      System.err.println("Usage: *.lz4")
      System.exit(1)
    }

    val lz4FileName = args(0)
    val unzippedFileName = lz4FileName.replaceAll(".lz4", ".txt")
    decompress(lz4FileName, unzippedFileName)
  }
}
