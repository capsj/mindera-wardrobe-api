package controllers

import java.time.Instant

import akka.stream.scaladsl.Source
import akka.NotUsed
import akka.stream.scaladsl.Flow
import kantan.csv._
import models.api.Category
import models.api.ClothingItem
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart

object CsvReader {
  import kantan.csv.ops._

  case class CSVRow(clothingItemName: String, categoryName: String)
  object CSVRow {
    implicit val CSVRowDecoder: RowDecoder[CSVRow] = RowDecoder.ordered {
      (clothingItemName: String, categoryName: String) => CSVRow(clothingItemName, categoryName)
    }
  }

  def parseCsv(filePart: FilePart[TemporaryFile]): Source[ReadResult[CSVRow], NotUsed] =
    Source.fromIterator( () => {
      filePart.ref.path.toFile
        .asCsvReader[CSVRow](rfc)
        .iterator
    })

  type ApiModels = (ClothingItem, Category)
  def csvRowToModels: Flow[ReadResult[CSVRow], Option[ApiModels], _] =
    Flow[ReadResult[CSVRow]]
      .map({ readResult =>
        (for {
          line <- readResult
          clothingItem = ClothingItem(None, line.clothingItemName, Instant.now, Instant.now)
          category = Category(None, line.categoryName)
        } yield (clothingItem, category)).toOption
      })

}
