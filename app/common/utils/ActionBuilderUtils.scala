package common.utils

import slick.jdbc.{PositionedParameters, SQLActionBuilder, SetParameter}

/**
 * Created by bohm on 1/6/17.
 */
object ActionBuilderUtils {
  def concat(a: SQLActionBuilder, b: SQLActionBuilder): SQLActionBuilder = {
    SQLActionBuilder(a.queryParts ++ b.queryParts, new SetParameter[Unit] {
      def apply(p: Unit, pp: PositionedParameters): Unit = {
        a.unitPConv.apply(p, pp)
        b.unitPConv.apply(p, pp)
      }
    })
  }

}
