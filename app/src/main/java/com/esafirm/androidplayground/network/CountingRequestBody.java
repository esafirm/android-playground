package com.esafirm.androidplayground.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;

public class CountingRequestBody extends RequestBody {

    private static final int SEGMENT_SIZE = 12 * 1024;

    private RequestBody delegate;
    private Listener listener;

    public CountingRequestBody(RequestBody delegate, Listener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return delegate.contentLength();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        Buffer buffer = new Buffer();
        delegate.writeTo(buffer);

        long read;
        long total = 0;

        Buffer bufferSink = sink.buffer();
        while ((read = buffer.read(bufferSink, SEGMENT_SIZE)) != -1) {
            total += read;
            listener.onRequestProgress(total, contentLength());
        }
    }

    public interface Listener {
        void onRequestProgress(long bytesWritten, long contentLength);
    }
}
