package com.cattledog.im.message.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.cattledog.im.R
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.view_message.view.*
import org.drinkless.td.libcore.telegram.TdApi

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 02/04/2019
 * ************************************************************
 */
class MessageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    var position: Int = 0
    var textColor: Int
        get() {
            return tv_message.textColors.defaultColor
        }
        set(value) {
            tv_message.setTextColor(context.getColor(value))
        }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_message, this)
    }

    fun setMessage(message: TdApi.Message) {
        clearVisibilities()
        parseMessageContent(message.content)
    }

    private fun clearVisibilities() {
        tv_message.visibility = GONE
        iv_message.visibility = GONE
    }

    private fun parseMessageContent(content: TdApi.MessageContent) {
        when (content.constructor) {
            TdApi.MessageText.CONSTRUCTOR -> {
                val message = content as TdApi.MessageText
                tv_message.visibility = View.VISIBLE
                tv_message.text = message.text.text
            }
            TdApi.MessageAnimation.CONSTRUCTOR -> {

            }
            TdApi.MessageAudio.CONSTRUCTOR -> {

            }
            TdApi.MessageDocument.CONSTRUCTOR -> {

            }
            TdApi.MessagePhoto.CONSTRUCTOR -> {
                val message = content as TdApi.MessagePhoto
                val photoSize = getBestPhoto(message.photo.sizes)
                val path = photoSize.photo.local.path
                iv_message.visibility = View.VISIBLE
                Ion.with(iv_message)
                    .placeholder(R.drawable.ic_add)
                    .error(R.drawable.ic_add)
                    .resize(photoSize.width, photoSize.height)
                    .load(path)
            }
            TdApi.MessageExpiredPhoto.CONSTRUCTOR -> {

            }
            TdApi.MessageSticker.CONSTRUCTOR -> {

            }
            TdApi.MessageVideo.CONSTRUCTOR -> {

            }
            TdApi.MessageExpiredVideo.CONSTRUCTOR -> {

            }
            TdApi.MessageVideoNote.CONSTRUCTOR -> {

            }
            TdApi.MessageVoiceNote.CONSTRUCTOR -> {

            }
            TdApi.MessageLocation.CONSTRUCTOR -> {

            }
            TdApi.MessageVenue.CONSTRUCTOR -> {

            }
            TdApi.MessageContact.CONSTRUCTOR -> {

            }
            TdApi.MessageGame.CONSTRUCTOR -> {

            }
            TdApi.MessageInvoice.CONSTRUCTOR -> {

            }
            TdApi.MessageCall.CONSTRUCTOR -> {

            }
            TdApi.MessageBasicGroupChatCreate.CONSTRUCTOR -> {

            }
            TdApi.MessageSupergroupChatCreate.CONSTRUCTOR -> {

            }
            TdApi.MessageChatChangeTitle.CONSTRUCTOR -> {

            }
            TdApi.MessageChatChangePhoto.CONSTRUCTOR -> {

            }
            TdApi.MessageChatDeletePhoto.CONSTRUCTOR -> {

            }
            TdApi.MessageChatAddMembers.CONSTRUCTOR -> {

            }
            TdApi.MessageChatJoinByLink.CONSTRUCTOR -> {

            }
            TdApi.MessageChatDeleteMember.CONSTRUCTOR -> {

            }
            TdApi.MessageChatUpgradeTo.CONSTRUCTOR -> {

            }
            TdApi.MessageChatUpgradeFrom.CONSTRUCTOR -> {

            }
            TdApi.MessagePinMessage.CONSTRUCTOR -> {

            }
            TdApi.MessageScreenshotTaken.CONSTRUCTOR -> {

            }
            TdApi.MessageChatSetTtl.CONSTRUCTOR -> {

            }
            TdApi.MessageCustomServiceAction.CONSTRUCTOR -> {

            }
            TdApi.MessageGameScore.CONSTRUCTOR -> {

            }
            TdApi.MessagePaymentSuccessful.CONSTRUCTOR -> {

            }
            TdApi.MessagePaymentSuccessfulBot.CONSTRUCTOR -> {

            }
            TdApi.MessageContactRegistered.CONSTRUCTOR -> {

            }
            TdApi.MessageWebsiteConnected.CONSTRUCTOR -> {

            }
            TdApi.MessagePassportDataSent.CONSTRUCTOR -> {

            }
            TdApi.MessagePassportDataReceived.CONSTRUCTOR -> {

            }
            TdApi.MessageUnsupported.CONSTRUCTOR -> {

            }
            else -> {
            }
        }
    }

    private fun getBestPhoto(sizes: Array<out TdApi.PhotoSize>): TdApi.PhotoSize {
        var photoSize = sizes[sizes.size - 1]
        if (!photoSize.photo.local.path.isBlank()) return photoSize
        for (i in sizes.size - 2..0) {
            photoSize = sizes[i]
            if (!photoSize.photo.local.path.isBlank()) return photoSize
        }
        return sizes[sizes.size - 1]
    }
}