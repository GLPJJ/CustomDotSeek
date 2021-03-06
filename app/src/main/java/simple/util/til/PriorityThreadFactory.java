package simple.util.til;

import android.os.Process;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityThreadFactory implements ThreadFactory {
    private final String mName;
    private final int mPriority;
    private final AtomicInteger mNumber = new AtomicInteger();

    public PriorityThreadFactory(String name, int priority) {
        mName = name;// 线程池的名称
        mPriority = priority;// 线程池的优先级
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, mName + "-" + mNumber.getAndIncrement()) {
            @Override
            public void run() {
                // 设置线程的优先级
                Process.setThreadPriority(mPriority);
                super.run();
            }
        };
    }

}

