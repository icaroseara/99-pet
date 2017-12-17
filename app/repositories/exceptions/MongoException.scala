package repositories.exceptions

sealed abstract class MongoException(msg: String, cause: Option[Throwable] = None) extends Exception(msg, cause.orNull)

case class MongoInsertException(msg: String, cause: Option[Throwable] = None) extends MongoException(msg, cause)

case class MongoUpdateException(msg: String, cause: Option[Throwable] = None) extends MongoException(msg, cause)

case class MongoObjectNotFoundException(msg: String, cause: Option[Throwable] = None) extends MongoException(msg, cause)