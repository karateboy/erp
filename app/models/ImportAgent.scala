package models

import akka.actor._
import javax.inject.{Inject, Singleton}
import models.ModelHelper._
import play.api._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global

object ImportAgent {

  case class DirConfig(path: String, tags: Seq[String])

  case object ImportImage

}

@Singleton
class ImportAgent @Inject()(configuration: Configuration, imageOps: ImageOps) extends Actor {

  import ImportAgent._
  import com.typesafe.config._

  implicit val configLoader: ConfigLoader[Seq[DirConfig]] = new ConfigLoader[Seq[DirConfig]] {
    def load(rootConfig: Config, path: String): Seq[DirConfig] = {
      val dirConfigList = rootConfig.getConfigList(path)
      dirConfigList.asScala map {
        dirConfig =>
          val path = dirConfig.getString("path")
          val tags = dirConfig.getStringList("tags")
          DirConfig(path, tags.asScala)
      }
    }
  }

  Logger.info("ImportAgent started.")
  val dirConfigList = configuration.get[Seq[DirConfig]]("importDir")
  self ! ImportImage

  def receive = {
    case ImportImage =>
      try {
        processInputPath(parser)
      } catch {
        case ex: Throwable =>
          Logger.error("process InputPath failed", ex)
      }

      context.system.scheduler.scheduleOnce(
        scala.concurrent.duration.Duration(1, scala.concurrent.duration.MINUTES), self, ImportImage)
  }

  import java.io.File

  def parser(file: File, tags: Seq[String]): Boolean = {
    import java.nio.file.{Files, Paths}

    import org.mongodb.scala.bson._

    val imgId = new ObjectId()
    val img = Image(imgId, file.getName, tags, file.lastModified(),
      Files.readAllBytes(Paths.get(file.getAbsolutePath)))

    val f1 = imageOps.collection.insertOne(img).toFuture()
    f1.failed.foreach(errorHandler)
    waitReadyResult(f1)

    true
  }

  def processInputPath(parser: (File, Seq[String]) => Boolean) {

    for {
      dirConfig <- dirConfigList
      tags = dirConfig.tags
      files = listAllFiles(dirConfig.path)
      f <- files
    } {
      try {
        if (parser(f, tags)) {
          Logger.info(s"${f.getAbsolutePath} success.")
          f.delete()
        }
      } catch {
        case ex: Throwable =>
          Logger.warn(s"${f.getAbsolutePath} is not ready...", ex)
      }
    }
  }

  def listAllFiles(files_path: String) = {
    //import java.io.FileFilter
    val path = new java.io.File(files_path)
    if (path.exists() && path.isDirectory()) {
      val allFiles = new java.io.File(files_path).listFiles().toList
      allFiles.filter(p => p != null)
    } else {
      Logger.warn(s"invalid input path ${files_path}")
      List.empty[File]
    }
  }

  override def postStop = {}
}