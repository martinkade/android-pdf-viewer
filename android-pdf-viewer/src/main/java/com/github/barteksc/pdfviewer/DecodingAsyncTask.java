/**
 * Copyright 2016 Bartosz Schiller
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.barteksc.pdfviewer;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.barteksc.pdfviewer.source.DocumentSource;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.shockwave.pdfium.util.Size;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

class DecodingAsyncTask implements Callable<PdfFile> {

    private final WeakReference<Context> contextRef;

    private final PdfiumCore pdfiumCore;
    private final String password;
    private final DocumentSource docSource;
    private final int[] userPages;
    private final boolean isVertical;
    private final int spacingPx;
    private final boolean autoSpacing;
    private final FitPolicy pageFitPolicy;
    private final boolean fitEachPage;
    private final Size viewSize;

    public DecodingAsyncTask(@NonNull Context context, DocumentSource docSource, String password, int[] userPages, FitPolicy pageFitPolicy, Size viewSize, boolean isVertical, int spacing, boolean autoSpacing, boolean fitEachPage, PdfiumCore pdfiumCore) {
        this.contextRef = new WeakReference<>(context);
        this.docSource = docSource;
        this.password = password;
        this.userPages = userPages;
        this.pageFitPolicy = pageFitPolicy;
        this.viewSize = viewSize;
        this.isVertical = isVertical;
        this.spacingPx = spacing;
        this.autoSpacing = autoSpacing;
        this.fitEachPage = fitEachPage;
        this.pdfiumCore = pdfiumCore;
    }

    @Override
    public PdfFile call() throws Exception {
        final Context context = contextRef.get();
        if (context != null) {
            final PdfDocument pdfDocument = docSource.createDocument(context, pdfiumCore, password);
            return new PdfFile(pdfiumCore, pdfDocument, pageFitPolicy, viewSize,
                    userPages, isVertical, spacingPx, autoSpacing,
                    fitEachPage);
        } else {
            throw new NullPointerException("context == null");
        }
    }
}
