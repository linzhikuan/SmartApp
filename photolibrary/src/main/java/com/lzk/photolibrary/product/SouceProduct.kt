package com.lzk.photolibrary.product

import android.Manifest
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lzk.photolibrary.inner.SourceInner
import com.lzk.photolibrary.model.ImageFileModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File


class SouceProduct(private val context: Context) : SourceInner {
    private lateinit var rxPermissions: RxPermissions

    private fun myLog(msg: String) {
        Log.d("SouceProduct", msg)
    }

    init {
        when (context) {
            is Fragment -> rxPermissions = RxPermissions(context)
            is FragmentActivity -> rxPermissions = RxPermissions(context)
        }
    }

    override fun loadPhotoData(): Observable<ArrayList<ImageFileModel>> {
        return rxPermissions
            .request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribeOn(Schedulers.io())
            .map {
                val imageFileModels = ArrayList<ImageFileModel>()
                val cursor = context.contentResolver
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
                while (cursor!!.moveToNext()) {
                    //获取图片的名称
                    val name =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                    //获取图片的生成日期
                    val data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    //获取图片的详细信息
                    val desc =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION))
                    val imageFileModel = ImageFileModel()
                    imageFileModel.name = name
                    imageFileModel.description = desc
                    imageFileModel.path = String(
                        data,
                        0,
                        data.size - 1
                    )
                    imageFileModels.add(imageFileModel)

                }
                cursor.close()
                imageFileModels
            }.observeOn(AndroidSchedulers.mainThread())
    }
}