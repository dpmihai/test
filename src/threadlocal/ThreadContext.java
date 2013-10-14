package threadlocal;

// http://javarecipes.com/2012/07/11/understanding-the-concept-behind-threadlocal/
public class ThreadContext {
	 
    private String userId;
    private Long transactionId;
 
    private static ThreadLocal<ThreadContext> threadLocal = new ThreadLocal<ThreadContext>(){
        @Override
        protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    };
    
    public static ThreadContext get() {
        return threadLocal.get();
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
 
    public String toString() {
        return "userId:" + userId + ",transactionId:" + transactionId;
    }
}