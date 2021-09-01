package com.helloworld.photoeditor.common_lib;


import android.os.Handler;
import android.os.Message;
import android.os.Process;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MyAsyncTask<Params, Progress, Result> {
    private static int[] myAsynStatus = null;
    private static final int CORE_POOL_SIZE = 1;
    private static final int KEEP_ALIVE = 10;
    private static final String LOG_TAG = "MyAsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int MESSAGE_POST_CANCEL = 3;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    private static MyAsyncTaskResult result;
    private static BlockingQueue<Runnable> sWorkQueue;
    private static ThreadFactory sThreadFactory;
    private static ThreadPoolExecutor sExecutor;
    private static InternalHandler sHandler;
    private FutureTask<Result> mFuture;
    private volatile Status mStatus = Status.PENDING;
    private WorkerRunnable<Params, Result> mWorker;
    public Result result2;

    static class C05141 implements ThreadFactory {
        AtomicInteger mCount;

        C05141() {
        }

        public Thread newThread(@NonNull Runnable runnable) {
            this.mCount = new AtomicInteger(1);
            return new Thread(runnable, "MyAsyncTask #" + this.mCount.getAndIncrement());
        }
    }

    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;

        private WorkerRunnable() {
        }
    }

    class C05152 extends WorkerRunnable<Params, Result> {
        C05152() {
            super();
        }

        public Result call() throws Exception {
            Process.setThreadPriority(10);
            return MyAsyncTask.this.doInBackground(this.mParams);
        }
    }

    private static class InternalHandler extends Handler {
        private InternalHandler() {
        }

        public void handleMessage(Message msg) {
            MyAsyncTask.result = (MyAsyncTaskResult) msg.obj;
            switch (msg.what) {
                case 1:
                    MyAsyncTask.result.mTask.finish(MyAsyncTask.result.mData[0]);
                    break;
                case 2:
                case 3:
                    break;
                default:
                    return;
            }
            MyAsyncTask.result.mTask.onProgressUpdate(MyAsyncTask.result.mData);
            MyAsyncTask.result.mTask.onCancelled();
        }
    }

    private static class MyAsyncTaskResult<Data> {
        final Data[] mData;
        final MyAsyncTask mTask;

        MyAsyncTaskResult(MyAsyncTask task, Data... data) {
            this.mTask = task;
            this.mData = data;
        }
    }

    public enum Status {
        PENDING,
        RUNNING,
        FINISHED
    }

    protected abstract Result doInBackground(Params... paramsArr);

    static int[] MyCamiAsyncTaskStatus() {
        int[] iArr = myAsynStatus;
        if (iArr == null) {
            iArr = new int[Status.values().length];
            try {
                iArr[Status.FINISHED.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Status.PENDING.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Status.RUNNING.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            myAsynStatus = iArr;
        }
        return iArr;
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    protected void onPreExecute() {
    }

    protected void onPostExecute(Result result) {
    }

    protected void onProgressUpdate(Progress... progressArr) {
    }

    protected void onCancelled() {
    }

    public final boolean isCancelled() {
        return this.mFuture.isCancelled();
    }

    public final boolean cancel(boolean mayInterruptIfRunning) {
        return this.mFuture.cancel(mayInterruptIfRunning);
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }

    public final Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(timeout, unit);
    }

    public final MyAsyncTask<Params, Progress, Result> execute(Params... params) {
        if (this.mStatus != Status.PENDING) {
            switch (MyCamiAsyncTaskStatus()[this.mStatus.ordinal()]) {
                case 2:
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                case 3:
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        }


        //Dt Change start
        sWorkQueue = new LinkedBlockingQueue(10);
        sThreadFactory = new C05141();
        sExecutor = new ThreadPoolExecutor(1, 10, 10, TimeUnit.SECONDS, sWorkQueue, sThreadFactory);
        sHandler = new InternalHandler();
        mWorker = new C05152();
        mFuture = new FutureTask<Result>(this.mWorker) {
            protected void done() {
                try {
                    MyAsyncTask.this.result2 = get();
                } catch (InterruptedException e) {
                    Log.w(MyAsyncTask.LOG_TAG, e);
                } catch (ExecutionException e2) {
                    throw new RuntimeException("An error occured while executing doInBackground()", e2.getCause());
                } catch (CancellationException e3) {
                    MyAsyncTask.sHandler.obtainMessage(3, new MyAsyncTaskResult(MyAsyncTask.this, (Object[]) null)).sendToTarget();
                    return;
                } catch (Throwable t) {
                    RuntimeException runtimeException = new RuntimeException("An error occured while executing doInBackground()", t);
                }
                MyAsyncTask.sHandler.obtainMessage(1, new MyAsyncTaskResult(MyAsyncTask.this, MyAsyncTask.this.result2)).sendToTarget();
            }
        };
        //DT Change End



        this.mStatus = Status.RUNNING;
        onPreExecute();
        this.mWorker.mParams = params;
        sExecutor.execute(this.mFuture);
        return this;
    }

    protected final void publishProgress(Progress... values) {
        sHandler.obtainMessage(2, new MyAsyncTaskResult(this, values)).sendToTarget();
    }

    private void finish(Result result) {
        onPostExecute(result);
        this.mStatus = Status.FINISHED;
    }
}
