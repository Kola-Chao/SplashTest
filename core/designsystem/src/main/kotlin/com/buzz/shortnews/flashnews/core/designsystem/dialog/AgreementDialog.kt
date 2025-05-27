package com.buzz.shortnews.flashnews.core.designsystem.dialog

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.buzz.shortnews.flashnews.core.analytics.AnalyticsEvent
import com.buzz.shortnews.flashnews.core.analytics.LocalAnalyticsHelper
import com.buzz.shortnews.flashnews.core.designsystem.theme.text_auxiliary
import com.buzz.shortnews.flashnews.core.designsystem.theme.text_content
import com.buzz.shortnews.flashnews.core.designsystem.theme.text_title
import com.buzz.shortnews.flashnews.core.designsystem.theme.theme_color
import com.buzz.shortnews.flashnews.core.ui.BuildConfig
import com.buzz.shortnews.flashnews.core.ui.commonString

@Composable
fun AgreementDialog(onReject: () -> Unit, onAgree: () -> Unit) {
    val analyticsHelper = LocalAnalyticsHelper.current
    analyticsHelper.logEvent(
        AnalyticsEvent(
            pageTitle = AnalyticsEvent.PageTitle.userAgreementPage,
            event = AnalyticsEvent.Event.FNShowPage
        )
    )
    BaseDialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            decorFitsSystemWindows = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = commonString.agreement_title),
                modifier = Modifier.wrapContentWidth(),
                color = text_title,
                fontSize = 18.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(contentAlignment = Alignment.BottomCenter) {
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val context = LocalContext.current
//                    val agreementText = stringResource(id = commonString.agreement_content)
                    val privacyPolicyUrl = BuildConfig.PRIVACYPOLICY_URL
                    val termsOfUseUrl = BuildConfig.TERMSOFUSE_URL

                    val annotatedString = buildAnnotatedString {
                        // 普通文本
                        append(stringResource(id = commonString.agreement_content_1))

                        // 《Privacy Policy》
                        pushStringAnnotation(tag = "URL", annotation = privacyPolicyUrl)
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = text_title,
                                textDecoration = TextDecoration.None
                            )
                        ) {
                            append(stringResource(id = commonString.agreement_content_2))
                        }
                        pop()

                        append(stringResource(id = commonString.agreement_content_3))

                        // 《Terms of Use》
                        pushStringAnnotation(tag = "URL", annotation = termsOfUseUrl)
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = text_title,
                                textDecoration = TextDecoration.None
                            )
                        ) {
                            append(stringResource(id = commonString.agreement_content_4))
                        }
                        pop()

                        append(stringResource(id = commonString.agreement_content_5))

                        // 《Privacy Policy》
                        pushStringAnnotation(tag = "URL", annotation = privacyPolicyUrl)
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = text_title,
                                textDecoration = TextDecoration.None
                            )
                        ) {
                            append(stringResource(id = commonString.agreement_content_2))
                        }
                        pop()

                        append(stringResource(id = commonString.agreement_content_3))

                        // 《Terms of Use》
                        pushStringAnnotation(tag = "URL", annotation = termsOfUseUrl)
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = text_title,
                                textDecoration = TextDecoration.None
                            )
                        ) {
                            append(stringResource(id = commonString.agreement_content_4))
                        }
                        pop()

                        append("\n")
                    }


                    ClickableText(
                        text = annotatedString,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = text_content,
                            lineHeight = 18.sp
                        ),
                        onClick = { offset ->
                            // 获取点击位置的标签
                            annotatedString.getStringAnnotations(
                                tag = "URL",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { annotation ->
                                openUrl(context, annotation.item)
                            }
                        }
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = mutableListOf(
                                    Color(0x00ffffff),
                                    Color.White
                                )
                            )
                        )
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier
                    .height(48.dp)
                    .background(color = theme_color, shape = RoundedCornerShape(6.dp))
                    .fillMaxWidth(),
                onClick = {
                    onAgree()
                    analyticsHelper.logEvent(
                        AnalyticsEvent(
                            pageTitle = AnalyticsEvent.PageTitle.userAgreementPage,
                            event = AnalyticsEvent.Event.FNClickButton,
                            remarks = "1"
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // 正常状态背景色
//                    contentColor = Color.White, // 正常状态文字颜色
//                    disabledContainerColor = Color.LightGray, // 禁用状态背景色
//                    disabledContentColor = Color.DarkGray // 禁用状态文字颜色
                ),
            ) {
                Text(
                    text = stringResource(id = commonString.common_Agree),
                    color = text_title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                modifier = Modifier
                    .clickable {
                        onReject()
                        analyticsHelper.logEvent(
                            AnalyticsEvent(
                                pageTitle = AnalyticsEvent.PageTitle.userAgreementPage,
                                event = AnalyticsEvent.Event.FNClickButton,
                                remarks = "2"
                            )
                        )
                    },
                text = stringResource(id = commonString.common_Quit),
                color = text_auxiliary,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// 打开 URL
private fun openUrl(context: android.content.Context, url: String) {
//    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//    context.startActivity(intent)
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(
        context,
        Uri.parse(url)
    )
}

@Preview
@Composable
fun AgreementDialogPreview() {
    AgreementDialog(onReject = {}, onAgree = {})
}