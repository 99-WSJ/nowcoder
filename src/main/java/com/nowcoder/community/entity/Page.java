package com.nowcoder.community.entity;

/**
 * 封装分页相关的信息
 */
public class Page {

        // 当前页码
        private int current = 1;

        // 显示上限
        private int limit = 10;

        // 数据总数(用于计算总页数)
        private int rows;

        // 查询路径(用于复用分页链接)
        private String path;

        public int getCurrent() {
            return current;
        }

        // 设置当前页码
        public void setCurrent(int current) {
            if (current >= 1) {
                this.current = current;
            }
        }

        public int getLimit() {
            return limit;
        }

        // 设置每页显示上限
        public void setLimit(int limit) {
            if (limit >= 1 && limit <= 100) {
                this.limit = limit;
            }
        }

        // 获取当前页的起始行
        public int getOffset() {
            // current * limit - limit
            return (current - 1) * limit;
        }

        // 获取总页数
        public int getTotal() {
            // rows / limit [+1]
            // 如果有多余的数据，显示在更多的页码上
            if (rows % limit == 0) {
                return rows / limit;
            } else {
                return rows / limit + 1;
            }
        }

        // 获取起始页码
        public int getFrom() {
            // current - 2
            int from = current - 2;
            return from < 1 ? 1 : from;
        }

        // 获取结束页码
        public int getTo() {
            // current + 2
            int to = current + 2;
            int total = getTotal();
            return to > total ? total : to;
        }

        // 获取数据总数
        public int getRows() {
            return rows;
        }

        // 设置数据总数
        public void setRows(int rows) {
            if (rows >= 0) {
                this.rows = rows;
            }
        }

        // 获取查询路径
        public String getPath() {
            return path;
        }

        // 设置查询路径
        public void setPath(String path) {
            this.path = path;
        }
}
