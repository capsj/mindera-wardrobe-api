package controllers

import akka.stream.scaladsl.Source
import akka.NotUsed
import akka.stream.scaladsl.Flow
import kantan.csv._
import models.ClothingItemName
import models.CategoryName
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart

object CsvReader {
  import kantan.csv.ops._

  case class CSVRow(clothingItemName: String, categoryName: String)
  object CSVRow {
    implicit val CSVRowDecoder: RowDecoder[CSVRow] = RowDecoder.ordered { (clothingItemName: String, categoryName: String) =>
      CSVRow(clothingItemName, categoryName)
    }
  }

  def parseCsv(filePart: FilePart[TemporaryFile]): Source[ReadResult[CSVRow], NotUsed] =
    Source.fromIterator { () =>
      filePart.ref.path.toFile
        .asCsvReader[CSVRow](rfc.withHeader(true))
        .iterator
    }

  type ApiModels = (ClothingItemName, CategoryName)
  def csvRowToModels: Flow[ReadResult[CSVRow], Option[ApiModels], _] =
    Flow[ReadResult[CSVRow]]
      .map { readResult =>
        (for {
          line <- readResult
          clothingItemName = line.clothingItemName
          categoryName     = line.categoryName
        } yield (clothingItemName, categoryName)).toOption
      }

}
