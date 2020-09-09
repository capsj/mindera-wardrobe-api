package controllers

import akka.stream.scaladsl.Source
import akka.NotUsed
import kantan.csv._
import kantan.csv.ops._
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart

case class CsvRow(clothingItemName: String, categoryName: String)
object CsvRow {
  implicit val CSVRowDecoder: RowDecoder[CsvRow] = RowDecoder.ordered { (clothingItemName: String, categoryName: String) =>
    CsvRow(clothingItemName, categoryName)
  }
}

trait CsvReader {
  def parseCsv(filePart: FilePart[TemporaryFile]): Source[Option[CsvRow], NotUsed]
}

class CsvReaderImpl extends CsvReader {
  def parseCsv(filePart: FilePart[TemporaryFile]): Source[Option[CsvRow], NotUsed] =
    Source.fromIterator({ () =>
      filePart.ref.path.toFile
        .asCsvReader[CsvRow](rfc.withHeader(true))
        .iterator
    }).map { readResult =>
      (for {
        line <- readResult
      } yield line).toOption
    }
}
