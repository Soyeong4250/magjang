package B208.mag_jang.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class AsyncService {
    // millis 초 후(ms) roomId 반환
    @Async
    public ListenableFuture<String> sleep(String roomId, int millis) throws InterruptedException {
        System.out.println(roomId + " : sleep "+ millis);
        Thread.sleep(millis); // 이거 문제 있는지 확인하기
        return new AsyncResult<>(roomId);
    }
    // millis 초 후(ms) roomId 반환
    @Async
    public ListenableFuture<String> isPossible(String roomId) throws InterruptedException {
        System.out.println(roomId + " : sleep ");
        return new AsyncResult<>(roomId);
    }
}
