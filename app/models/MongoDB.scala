package models
import javax.inject.{ Inject, Singleton }
import play.api._
import com.typesafe.config._
import scala.concurrent._
import scala.collection.JavaConverters._

@Singleton
class MongoDB @Inject() (config: Configuration) {
  import org.mongodb.scala._

  case class MongoDbConfig(url: String, dbName: String)
  implicit val configLoader: ConfigLoader[MongoDbConfig] = new ConfigLoader[MongoDbConfig] {
    def load(rootConfig: Config, path: String): MongoDbConfig = {
      val myConfig = rootConfig.getConfig(path)
      val url = myConfig.getString("url")
      val dbName = myConfig.getString("db")
      MongoDbConfig(url, dbName)
    }
  }

  private val dbConfig = config.get[MongoDbConfig]("my.mongodb")

  val mongoClient: MongoClient = MongoClient(dbConfig.url)
  val database: MongoDatabase = mongoClient.getDatabase(dbConfig.dbName);

  def cleanup = {
    mongoClient.close()
  }
}