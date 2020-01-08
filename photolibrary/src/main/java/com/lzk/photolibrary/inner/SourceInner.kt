package com.lzk.photolibrary.inner

import com.lzk.photolibrary.model.ImageFileModel
import io.reactivex.Observable

interface SourceInner {
    fun loadPhotoData(): Observable<ArrayList<ImageFileModel>>
}