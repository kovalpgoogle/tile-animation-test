package com.example.weartile.tile

import android.content.Context
import android.graphics.Color
import androidx.wear.protolayout.ColorBuilders.ColorProp
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.expression.AnimationParameterBuilders
import androidx.wear.protolayout.expression.AnimationParameterBuilders.REPEAT_MODE_REVERSE
import androidx.wear.protolayout.expression.AnimationParameterBuilders.Repeatable.INFINITE_REPEATABLE_WITH_RESTART
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.material.CircularProgressIndicator
import androidx.wear.protolayout.material.ProgressIndicatorColors
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val RESOURCES_VERSION = "0"

class MainTileService : TileService() {

  override fun onTileResourcesRequest(
    requestParams: RequestBuilders.ResourcesRequest
  ): ListenableFuture<ResourceBuilders.Resources> {
    return Futures.immediateFuture(
      ResourceBuilders.Resources.Builder().setVersion(RESOURCES_VERSION).build()
    )
  }

  override fun onTileRequest(requestParams: TileRequest): ListenableFuture<TileBuilders.Tile> {
    return Futures.immediateFuture(tile(this))
  }
}

private var startValue = 0.02f
private var endValue = 0.95f
private const val animationDurationInMillis = 2000L // 2 seconds

val COLOR_KEY: AppDataKey<DynamicBuilders.DynamicColor> =
  AppDataKey<DynamicBuilders.DynamicColor>("color")

private fun tileLayout(context: Context): LayoutElementBuilders.LayoutElement {
  val spec1 =
    AnimationParameterBuilders.AnimationSpec.Builder()
      .setRepeatable(INFINITE_REPEATABLE_WITH_RESTART)
      .setAnimationParameters(
        AnimationParameterBuilders.AnimationParameters.Builder()
          .setDurationMillis(animationDurationInMillis * 3)
          .build()
      )
      .build()

  val spec2 =
    AnimationParameterBuilders.AnimationSpec.Builder()
      .setRepeatable(
        AnimationParameterBuilders.Repeatable.Builder()
          .setRepeatMode(REPEAT_MODE_REVERSE)
          .setReverseRepeatOverride(
            AnimationParameterBuilders.AnimationParameters.Builder()
              .setDurationMillis(animationDurationInMillis)
              .build()
          )
          .build()
      )
      .setAnimationParameters(
        AnimationParameterBuilders.AnimationParameters.Builder()
          .setDurationMillis(animationDurationInMillis + 100)
          .build()
      )
      .build()

  val progressIndicator =
    CircularProgressIndicator.Builder()
      .setProgress(
        TypeBuilders.FloatProp.Builder(/* static value */ 0.25f)
          .setDynamicValue(
            //    DynamicBuilders.DynamicFloat.animate(10f, 20f, spec1).div(100f).animate(spec1)
            DynamicBuilders.DynamicFloat.animate(startValue, endValue, spec2)
          )
          .build()
      )
      .setCircularProgressIndicatorColors(
        ProgressIndicatorColors(
          ColorProp.Builder(Color.GRAY)
            .setDynamicValue(DynamicBuilders.DynamicColor.animate(-0x12300, -0xff6f01, spec1))
            .build(),
          ColorProp.Builder(Color.BLACK).build(),
        )
      )
      .build()

  val text =
    Text.Builder(context, "Hello World!")
      .setColor(argb(Color.WHITE))
      .setTypography(Typography.TYPOGRAPHY_BODY1)
      .build()

  return LayoutElementBuilders.Box.Builder()
    .setModifiers(
      ModifiersBuilders.Modifiers.Builder()
        .setBackground(
          ModifiersBuilders.Background.Builder()
            .setColor(
              ColorProp.Builder(Color.BLACK)
                .setDynamicValue(
                  DynamicBuilders.DynamicColor.animate(
                    Color.BLUE,
                    Color.WHITE,
                    AnimationParameterBuilders.AnimationSpec.Builder()
                      .setAnimationParameters(
                        AnimationParameterBuilders.AnimationParameters.Builder()
                          .setDurationMillis(3000)
                          .build()
                      )
                      .build(),
                  )
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .addContent(text)
    .addContent(progressIndicator)
    .build()
}

fun tile(context: Context): TileBuilders.Tile {
  val singleTileTimeline =
    TimelineBuilders.Timeline.Builder()
      .addTimelineEntry(
        TimelineBuilders.TimelineEntry.Builder()
          .setLayout(LayoutElementBuilders.Layout.Builder().setRoot(tileLayout(context)).build())
          .build()
      )
      .build()

  return TileBuilders.Tile.Builder()
    .setResourcesVersion(RESOURCES_VERSION)
    .setTileTimeline(singleTileTimeline)
    .build()
}

@Preview
fun tilePreview(context: Context): TilePreviewData {
  return TilePreviewData { tile(context) }
}
