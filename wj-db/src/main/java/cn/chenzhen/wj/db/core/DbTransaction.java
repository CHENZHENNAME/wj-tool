package cn.chenzhen.wj.db.core;


public class DbTransaction {


    /**
     * 开启事务
     */
    public static DbSession open(){
        return open(ConnectionManager.DEFAULT);
    }

    /**
     * 开启世界
     * @param type 数据库类型
     */
    public static DbSession open(String type){
        return ConnectionManager.open(type);
    }
    /**
     * 提交事务
     */
    public static void commit(){
        commit(ConnectionManager.DEFAULT);
    }

    /**
     * 提交事务
     * @param type 数据库类型
     */
    public static void commit(String type){
        ConnectionManager.commit(type);
    }

    /**
     * 回滚事务
     */
    public static void rollback(){
        rollback(ConnectionManager.DEFAULT);
    }

    /**
     * 回滚事务
     * @param type 数据库类型
     */
    public static void rollback(String type){
        ConnectionManager.rollback(type);
    }
}
